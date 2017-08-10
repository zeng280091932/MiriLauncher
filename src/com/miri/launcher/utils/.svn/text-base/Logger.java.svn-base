package com.miri.launcher.utils;

import android.util.Log;

/**
 * 自定义日志类,输出日志格式[线程名+文件名+日志行号]
 */
public class Logger {

    private final static String tag = "MiRiLauncher";

    public static int logLevel = Log.VERBOSE;

    private static boolean logFlag = true;

    private static Logger logger = new Logger();

    public static Logger getLogger() {
        return logger;
    }

    private Logger() {
    }

    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }

        for (StackTraceElement st: sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(getClass().getName())) {
                continue;
            }

            return "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":"
                    + st.getLineNumber() + " ]";
        }

        return null;
    }

    public void i(String str) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.INFO) {
            String name = getFunctionName();
            if (name != null) {
                Log.i(tag, name + " - " + str);
            } else {
                Log.i(tag, str);
            }
        }
    }

    public void i(String str, Throwable ex) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.INFO) {
            String name = getFunctionName();
            if (name != null) {
                Log.i(tag, name + " - " + str, ex);
            } else {
                Log.i(tag, str, ex);
            }
        }
    }

    public void v(String str) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.VERBOSE) {
            String name = getFunctionName();
            if (name != null) {
                Log.v(tag, name + " - " + str);
            } else {
                Log.v(tag, str);
            }

        }
    }

    public void v(String str, Throwable ex) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.VERBOSE) {
            String name = getFunctionName();
            if (name != null) {
                Log.v(tag, name + " - " + str, ex);
            } else {
                Log.v(tag, str, ex);
            }

        }
    }

    public void w(String str) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.WARN) {
            String name = getFunctionName();

            if (name != null) {
                Log.w(tag, name + " - " + str);
            } else {
                Log.w(tag, str);
            }

        }
    }

    public void w(String str, Throwable ex) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.WARN) {
            String name = getFunctionName();

            if (name != null) {
                Log.w(tag, name + " - " + str, ex);
            } else {
                Log.w(tag, str, ex);
            }

        }
    }

    public void e(String str) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.ERROR) {
            String name = getFunctionName();
            if (name != null) {
                Log.e(tag, name + " - " + str);
            } else {
                Log.e(tag, str);
            }
        }
    }

    public void e(String str, Throwable ex) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.ERROR) {
            String name = getFunctionName();
            if (name != null) {
                Log.e(tag, name + " - " + str, ex);
            } else {
                Log.e(tag, str, ex);
            }
        }
    }

    public void e(Throwable ex) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.ERROR) {
            Log.e(tag, "error", ex);
        }
    }

    public void d(String str) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.DEBUG) {
            String name = getFunctionName();
            if (name != null) {
                Log.d(tag, name + " - " + str);
            } else {
                Log.d(tag, str);
            }

        }
    }

    public void d(String str, Throwable ex) {
        if (!logFlag) {
            return;
        }
        if (logLevel <= Log.DEBUG) {
            String name = getFunctionName();
            if (name != null) {
                Log.d(tag, name + " - " + str, ex);
            } else {
                Log.d(tag, str, ex);
            }

        }
    }
}
