package com.grank.logger;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Process;
import android.text.TextUtils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;


public class Log {
    private static int LOGGER_LEVEL = android.util.Log.VERBOSE;
    private static boolean USE_LOGGER = true;
    private static DiskLogStrategy diskLogStrategy;

    public static void initLogger(Context context,
                                  String processName,
                                  String globalTag,
                                  String diskFileLabel,
                                  boolean saveDisk,
                                  DiskLogMonitor logMonitor
                                  ) {
        LogFormatStrategy.Builder builder = LogFormatStrategy.newBuilder();
        if (saveDisk) {
            String folder = context.getExternalFilesDir(null).toString() + File.separatorChar + "logger";
            HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
            ht.start();
            DiskLogStrategy.WriteHandler handler = new DiskLogStrategy.WriteHandler(ht.getLooper(), folder, DiskLogStrategy.MAX_BYTES, diskFileLabel, logMonitor);
            diskLogStrategy = new DiskLogStrategy(handler);
            builder.logStrategy(diskLogStrategy);
        }
        builder.logStrategy(new LogcatLogStrategy() {
            @Override
            public void log(int priority, String tag, String message) {
                if (priority >= LOGGER_LEVEL) {
                    int max_str_length = 2001 - (TextUtils.isEmpty(tag) ? 0 : tag.length());
                    while (message.length() > max_str_length) {
                        super.log(priority, tag, message.substring(0, max_str_length));
                        message = message.substring(max_str_length);
                    }
                    super.log(priority, tag, message);
                }
            }
        })
            .pid(Process.myPid())
            .processName(processName)
            .tag(globalTag);
        Logger.addLogAdapter(new AndroidLogAdapter(builder.build()));
    }

    public static ArrayList<DiskLogStrategy.DiskLog> getAllLogFile() {
        if (diskLogStrategy == null) {
            return null;
        }
        return diskLogStrategy.getAllDiskLogFile();
    }

    public static void syncLog2File() {
        if (diskLogStrategy != null) {
            diskLogStrategy.syncLog2File();
        }
    }

    public static void trimLog() {
        if (diskLogStrategy != null) {
            diskLogStrategy.trimLog();
        }
    }

    public static void setLoggerLevel(int logLevel) {
        LOGGER_LEVEL = logLevel;
    }

    public static void setUserLogger(boolean userLogger) {
        USE_LOGGER = userLogger;
    }

    public static void v(String tag, String msg) {
        if (USE_LOGGER) {
            Logger.t(tag).v(msg);
        } else if (android.util.Log.VERBOSE >= LOGGER_LEVEL) {
            android.util.Log.v(tag, msg);
        }
    }


    public static void v(String tag, String msg, Throwable tr) {
        if (USE_LOGGER ) {
            Logger.log(Logger.VERBOSE, tag, msg, tr);
        } else if (android.util.Log.VERBOSE >= LOGGER_LEVEL) {
            android.util.Log.v(tag, msg, tr);
        }
    }


    public static void d(String tag, String msg) {
        if (USE_LOGGER ) {
            Logger.t(tag).d(msg);
        } else if (android.util.Log.DEBUG >= LOGGER_LEVEL) {
            android.util.Log.d(tag, msg);
        }
    }


    public static void d(String tag, String msg, Throwable tr) {
        if (USE_LOGGER ) {
            Logger.log(Logger.DEBUG, tag, msg, tr);
        } else if (android.util.Log.DEBUG >= LOGGER_LEVEL) {
            android.util.Log.d(tag, msg, tr);
        }
    }


    public static void i(String tag, String msg) {
        if (USE_LOGGER ) {
            Logger.t(tag).i(msg);
        } else if (android.util.Log.INFO >= LOGGER_LEVEL) {
            android.util.Log.i(tag, msg);
        }
    }


    public static void i(String tag, String msg, Throwable tr) {
        if (USE_LOGGER ) {
            Logger.log(Logger.INFO, tag, msg, tr);
        } else if (android.util.Log.INFO >= LOGGER_LEVEL) {
            android.util.Log.i(tag, msg, tr);
        }
    }


    public static void w(String tag, String msg) {
        if (USE_LOGGER ) {
            Logger.t(tag).w(msg);
        } else if (android.util.Log.WARN >= LOGGER_LEVEL) {
            android.util.Log.w(tag, msg);
        }
    }


    public static void w(String tag, String msg, Throwable tr) {
        if (USE_LOGGER ) {
            Logger.log(Logger.WARN, tag, msg, tr);
        } else if (android.util.Log.WARN >= LOGGER_LEVEL) {
            android.util.Log.w(tag, msg, tr);
        }
    }


    public static void w(String tag, Throwable tr) {
        if (USE_LOGGER ) {
            Logger.log(Logger.WARN, tag, null, tr);
        } else if (android.util.Log.WARN >= LOGGER_LEVEL) {
            android.util.Log.w(tag, tr);
        }
    }


    public static void e(String tag, String msg) {
        if (USE_LOGGER  ) {
            Logger.t(tag).e(msg);
        } else if (android.util.Log.ERROR >= LOGGER_LEVEL) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (USE_LOGGER  ) {
            Logger.log(Logger.ERROR, tag, msg, tr);
        } else if (android.util.Log.ERROR >= LOGGER_LEVEL) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    public static void v(String msg, Object... args) {
        if (USE_LOGGER ) {
            Logger.v(msg, args);
        }
    }

    public static void d(String msg, Object... args) {
        if (USE_LOGGER ) {
            Logger.d(msg, args);
        }
    }

    public static void w(String msg, Object... args) {
        if (USE_LOGGER ) {
            Logger.w(msg, args);
        }
    }

    public static void i(String msg, Object... args) {
        if (USE_LOGGER ) {
            Logger.i(msg, args);
        }
    }

    public static void e(String msg, Object... args) {
        if (USE_LOGGER ) {
            Logger.e(msg, args);
        }
    }

    public static void e(String message, Throwable throwable, Object... args) {
        if (USE_LOGGER ) {
            Logger.e(throwable, message, args);
        }
    }

    public static void xml(String tag, String xml) {
        if (USE_LOGGER) {
            Logger.t(tag).xml(xml);
        }
    }

    public static void json(String tag, String json) {
        if (USE_LOGGER) {
            Logger.t(tag).json(json);
        }
    }
}
