package com.grank.logger;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.orhanobut.logger.Logger.ASSERT;
import static com.orhanobut.logger.Logger.DEBUG;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;


public class LogFormatStrategy implements FormatStrategy {
  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String NEW_LINE_REPLACEMENT = " <br> ";
  /**
   * The minimum stack trace index, starts at this class after two native calls.
   */
  private static final int MIN_STACK_OFFSET = 5;


  private final Date date;
  private final SimpleDateFormat dateFormat;
  private final ArrayList<LogStrategy> logStrategies;
  private final int pid;
  private final String processName;
  private final String tag;

  private LogFormatStrategy(Builder builder) {
    date = builder.date;
    dateFormat = builder.dateFormat;
    logStrategies = builder.logStrategies;
    pid = builder.pid;
    processName = builder.processName;
    tag = builder.tag;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static String logLevel(int value) {
    switch (value) {
      case VERBOSE:
        return "V";
      case DEBUG:
        return "D";
      case INFO:
        return "I";
      case WARN:
        return "W";
      case ERROR:
        return "E";
      case ASSERT:
        return "A";
      default:
        return "U";
    }
  }

  @Override
  public void log(int priority, String tag, String message) {
    date.setTime(System.currentTimeMillis());

    StringBuilder headerInfoBuilder = new StringBuilder();

    // human-readable date/time
    headerInfoBuilder.append(dateFormat.format(date));
    headerInfoBuilder.append(" ");

    headerInfoBuilder.append(pid);
    headerInfoBuilder.append("/");
    headerInfoBuilder.append(processName);
    headerInfoBuilder.append(" ");

    // level
    headerInfoBuilder.append(logLevel(priority));
    headerInfoBuilder.append("/");

    // tag
    headerInfoBuilder.append(tag);
    headerInfoBuilder.append(": ");

//    StringBuilder messageBuilder = new StringBuilder();
//    messageBuilder.append(message);


    StringBuilder pendingInfoBuilder = new StringBuilder();
    StackTraceElement[] trace = Thread.currentThread().getStackTrace();
    int stackOffset = getStackOffset(trace);
    //corresponding method count with the current stack may exceeds the stack trace. Trims the count
    int index = stackOffset + 1;
    if (index > trace.length - 1) {
      index = trace.length - 1;
    }
    pendingInfoBuilder.append("   [")
        .append("Thread:")
        .append(Thread.currentThread().getName())
//        .append(" Method:")
//        .append(getSimpleClassName(trace[index].getClassName()))
//        .append(".")
//        .append(trace[index].getMethodName())
        .append(" (")
        .append(trace[index].getFileName())
        .append(":")
        .append(trace[index].getLineNumber())
        .append(")]");

    // new line
    pendingInfoBuilder.append(NEW_LINE);

    for (LogStrategy logStrategy : logStrategies) {
      if (logStrategy instanceof LogcatLogStrategy) {
        String formatTag = formatTag(tag);
        logStrategy.log(priority, formatTag, message + pendingInfoBuilder.toString());
      } else if (logStrategy instanceof DiskLogStrategy) {
        logStrategy.log(priority, tag, headerInfoBuilder.toString() + message + pendingInfoBuilder.toString());
      } else {
        logStrategy.log(priority, tag, headerInfoBuilder.toString() + message + pendingInfoBuilder.toString());
      }
    }
  }

  private String formatTag(String tag) {
    if (!isEmpty(this.tag) && !isEmpty(tag) && !equals(this.tag, tag)) {
      return this.tag + "-" + tag;
    }
    if (!isEmpty(tag)) {
      return tag;
    }
    return this.tag;
  }

  private static boolean equals(CharSequence a, CharSequence b) {
    if (a == b) return true;
    if (a != null && b != null) {
      int length = a.length();
      if (length == b.length()) {
        if (a instanceof String && b instanceof String) {
          return a.equals(b);
        } else {
          for (int i = 0; i < length; i++) {
            if (a.charAt(i) != b.charAt(i)) return false;
          }
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isEmpty(CharSequence str) {
    return str == null || str.length() == 0;
  }

  /**
   * Determines the starting index of the stack trace, after method calls made by this class.
   *
   * @param trace the stack trace
   * @return the stack offset
   */
  private int getStackOffset(StackTraceElement[] trace) {
    for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
      StackTraceElement e = trace[i];
      String name = e.getClassName();
      if (!name.equals("com.orhanobut.logger.LoggerPrinter")
        && !name.equals(Logger.class.getName())
        && !name.equals(Log.class.getName())) {
        return --i;
      }
    }
    return -1;
  }

  private String getSimpleClassName(String name) {
    int lastIndex = name.lastIndexOf(".");
    return name.substring(lastIndex + 1);
  }

  public static final class Builder {
    int pid;
    String processName;
    String tag;
    Date date;
    SimpleDateFormat dateFormat;
    ArrayList<LogStrategy> logStrategies = new ArrayList<>();

    private Builder() {
    }

    public Builder pid(int pid) {
      this.pid = pid;
      return this;
    }

    public Builder processName(String processName) {
      this.processName = processName;
      return this;
    }

    public Builder date(Date val) {
      date = val;
      return this;
    }

    public Builder dateFormat(SimpleDateFormat val) {
      dateFormat = val;
      return this;
    }

    public Builder logStrategy(LogStrategy val) {
      logStrategies.add(val);
      return this;
    }

    public Builder tag(String tag) {
      this.tag = tag;
      return this;
    }

    public LogFormatStrategy build() {
      if (date == null) {
        date = new Date();
      }
      if (dateFormat == null) {
        dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS", Locale.UK);
      }
      if (logStrategies.isEmpty()) {
        LogStrategy logStrategy = new LogcatLogStrategy();
        logStrategies.add(logStrategy);
      }
      return new LogFormatStrategy(this);
    }
  }
}
