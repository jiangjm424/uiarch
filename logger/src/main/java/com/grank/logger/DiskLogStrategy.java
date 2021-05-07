package com.grank.logger;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.text.TextUtils;

import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;


/**
 * Abstract class that takes care of background threading the file log operation on Android.
 * implementing classes are free to directly perform I/O operations there.
 */
public class DiskLogStrategy implements LogStrategy {
    static final int MAX_BYTES = 5 * 1024 * 1024;
    static final int FLUSH_SIZE = 30 * 1024;
    static final int MAX_FILE_COUNT = 8;
    private static final String TAG = DiskLogStrategy.class.getSimpleName();
    private final WriteHandler handler;

    DiskLogStrategy(WriteHandler handler) {
        this.handler = handler;
    }

    public void trimLog() {
        handler.trimLog();
    }

    public void syncLog2File() {
        handler.sendMessage(handler.obtainMessage(WriteHandler.MSG_SYNC, "\n"));
    }


    @Override
    public void log(int level, String tag, String message) {
        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(WriteHandler.MSG_LOG, message));
    }

    ArrayList<DiskLog> getAllDiskLogFile() {
        File[] files = handler.getAllLogFile();
        if (files == null) return null;
        File currentFile = handler.getCurrentLogFile();
        ArrayList<DiskLog> ret = new ArrayList<>();
        if (currentFile == null) {
            Log.e(TAG, "getCurrentLogFile is null");
        } else {
            for (File file : files) {
                boolean current = false;
                if (TextUtils.equals(file.getName(), currentFile.getName())) {
                    current = true;
                }
                ret.add(new DiskLog(file, current));
            }
        }
        return ret;
    }

    static class WriteHandler extends Handler {
        static final int MSG_LOG = 0;
        static final int MSG_SYNC = 1;
        private final String folder;
        private final int maxFileSize;
        private final String diskFileLabel;
        private final DiskLogMonitor diskLogMonitor;
        private Date date = new Date();
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private StringBuffer stringBuffer = new StringBuffer();

        WriteHandler(Looper looper, String folder, int maxFileSize, String diskFileLabel, DiskLogMonitor diskLogMonitor) {
            super(looper);
            this.folder = folder;
            this.maxFileSize = maxFileSize;
            this.diskFileLabel = diskFileLabel;
            this.diskLogMonitor = diskLogMonitor;
        }

        @SuppressWarnings("checkstyle:emptyblock")
        @Override
        public void handleMessage(Message msg) {
            boolean sync = msg.what == MSG_SYNC;
            String content = (String) msg.obj;
            stringBuffer.append(content);
            if (stringBuffer.length() > FLUSH_SIZE || sync) {
                File logFile = getCurrentLogFile();
                if (logFile == null) {
                    stringBuffer = new StringBuffer();
                    return;
                }
                try (FileOutputStream fileWriter = new FileOutputStream(logFile, true)) {
                    writeLog(fileWriter, stringBuffer.toString());
                    fileWriter.flush();
                    if (sync) {
                        fileWriter.getFD().sync();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /* fail silently */
                stringBuffer = new StringBuffer();
            }
        }

        private File getCurrentLogFile() {
            return getLogFile(folder, getFormatFileName(System.currentTimeMillis()));
        }

        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an INSTANCE of FileWriter already initialised to the correct file
         */
        private void writeLog(FileOutputStream fileWriter, String content) throws IOException {
            fileWriter.write(content.getBytes("UTF-8"));
        }

        private File getLogFile(String folderName, String fileName) {
            File folder = new File(folderName);
            if (!folder.exists()) {
                //TODO: What if folder is not created, what happens then?
                boolean ok = folder.mkdirs();
                if (!ok) {
                    Log.v(TAG, "mkdirs failed = " + folderName);
                    return null;
                }
            }
            int newFileCount = 0;
            File newFile;
            File existingFile = null;
            File[] allFile = getLogFile(fileName);
            if (allFile != null) {
                for (File file : allFile) {
                    String existFileName = file.getName();
                    String countString = existFileName.replace(".log", "").replace(fileName + "_", "");
                    int count = -1;
                    try {
                        count = Integer.parseInt(countString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (count >= 0) {
                        newFileCount = count;
                        break;
                    }
                }
            }
            newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
            while (newFile.exists()) {
                existingFile = newFile;
                newFileCount++;
                newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
            }
            if (existingFile != null) {
                if (existingFile.length() >= maxFileSize) {
                    checkStorageOverflowAndTrim();
                    return newFile;
                }
                return existingFile;
            }
            checkStorageOverflowAndTrim();
            return newFile;
        }

        private void checkStorageOverflowAndTrim() {
            if (checkStorageOverflow()) {
                if (diskLogMonitor != null) {
                    diskLogMonitor.onLogStorageOverflow();
                } else {
                    trimLog();
                }
            }
        }

        private String getFormatFileName(long time) {
            date.setTime(time);
            return diskFileLabel + "_" + dateFormat.format(date);
        }

        private boolean checkStorageOverflow() {
            File[] allLogs = getAllLogFile();
            boolean ret = false;
            if (allLogs != null && allLogs.length > MAX_FILE_COUNT / 2) {
                ret = true;
            } else if (allLogs != null && allLogs.length > MAX_FILE_COUNT / 4) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File file = Environment.getExternalStorageDirectory();
                    StatFs statFs = new StatFs(file.getPath());
                    //获得Sdcard上每个block的size
                    long blockSize = statFs.getBlockSize();
                    //获取可供程序使用的Block数量
                    long blockavailable = statFs.getAvailableBlocks();
                    //计算标准大小使用：1024，当然使用1000也可以
                    long blockavailableTotal = blockSize * blockavailable / 1000 / 1000;
                    if (blockavailableTotal < 100) {
                        ret = true;
                    }
                }
            }
            return ret;
        }

        private void trimLog() {
            File[] allLogs = getAllLogFile();
            if (allLogs != null && allLogs.length > MAX_FILE_COUNT) {
                Arrays.sort(allLogs, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return (int) (o1.lastModified() - o2.lastModified());
                    }
                });
                for (int i = 0; i < allLogs.length - MAX_FILE_COUNT; i++) {
                    Log.d("MiaDiskLogStrategy", "delete " + allLogs[i]);
                    boolean ok = allLogs[i].delete();
                    if (!ok) {
                        Log.v(TAG, "trimLog -> delete failed = " + allLogs[i].getPath());
                    }
                }
            }
        }


        private File[] getLogFile(final String fileName) {
            File path = new File(folder);
            if (!path.exists()) {
                return new File[0];
            }
            return path.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(fileName);
                }
            });
        }

        private File[] getAllLogFile() {
            File path = new File(folder);
            if (!path.exists()) {
                return new File[0];
            }
            return path.listFiles();
        }
    }

    public static class DiskLog {
        public File logFile;
        public boolean current;

        DiskLog(File logFile, boolean current) {
            this.logFile = logFile;
            this.current = current;
        }
    }
}