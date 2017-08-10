package com.miri.launcher.crashReporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.miri.launcher.msg.MsgParser;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.utils.ZipUtil;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类 来接管程序,并记录 发送错误报告.
 * <p>
 * 此类将收集两个日志文件 1、设备信息和异常日志{@link #saveCrashInfoToFile(Throwable, String)} ，保存格式：Properties
 * 2、logcat日志{@link #saveLogToFile(String)} ，保存格式：txt
 * <p>
 * 收集到的两个日志文件会自动打包成.zip文件，打包成功后日志文件将被删除
 * <p>
 * 可调用{@link #sendPreviousReportsToServer(Context)} 将日志通过邮件发送
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /** CrashHandler实例 */
    private static CrashHandler INSTANCE;

    /** 程序的Context对象 */
    private Context mContext;

    /** 使用Properties来保存设备的信息和错误堆栈信息 */
    private Properties mDeviceCrashInfo = new Properties();

    private static final String SDK_VERSION = "SDKVersion";

    private static final String VERSION_NAME = "versionName";

    private static final String VERSION_CODE = "versionCode";

    private static final String STACK_TRACE = "STACK_TRACE";

    private static final String MAC = "mac";

    private static final String AUTHCODE = "authCode";

    /** 错误报告文件的扩展名 */
    private static final String CRASH_REPORTER_EXTENSION = ".prop";

    /** 错误文件前缀 */
    private static final String CRASH_REPORTER_PRE = "crash-";

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // if (!handleException(ex) && mDefaultHandler != null) {
        // //如果用户没有处理则让系统默认的异常处理器来处理
        // mDefaultHandler.uncaughtException(thread, ex);
        // } else {
        // //Sleep一会后结束程序
        // try {
        // Thread.sleep(3000);
        // } catch (InterruptedException e) {
        // Logger.getLogger().e("Error : ", e);
        // }
        // android.os.Process.killProcess(android.os.Process.myPid());
        // System.exit(10);
        // }
        try {
            Logger.getLogger().e("uncaught exception", ex);
            handleException(ex);
        } catch (Throwable e) {
            e.printStackTrace();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        } finally {
            Logger.getLogger().d("kill app process");
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        final String msg = ex.getLocalizedMessage();

        // 收集设备信息
        collectCrashDeviceInfo(mContext);

        // 日志文件保存路径
        String crashDir = getCrashLogDir();

        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
        String now = sdf.format(new Date());

        String deviceInfoFilePath = crashDir + File.separator + CRASH_REPORTER_PRE + now
                + CRASH_REPORTER_EXTENSION;
        String logcatFilePath = crashDir + File.separator + CRASH_REPORTER_PRE + now + ".log";
        String zipFilePath = new File(crashDir).getParent() + File.separator + CRASH_REPORTER_PRE
                + now + ".zip";

        // 保存设备信息、错误报告文件
        saveCrashInfoToFile(ex, deviceInfoFilePath);
        // 保存logcat日志
        Toolkit.saveLogToFile(logcatFilePath);

        // 压缩日志文件
        try {
            ZipUtil.zip(zipFilePath, crashDir);
            // 删除日志文件，只保留压缩包
            Toolkit.deleteFile(new File(crashDir));
        } catch (Exception e) {
            Logger.getLogger().e("an error occured while ziping report file...", e);
        }

        // 以邮件的形式发送错误报告到服务器
        sendCrashReportService();
        return true;
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    public synchronized void sendPreviousReportsToServer(Context ctx) {
        sendCrashReportsToServer(ctx);
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     * @param ctx
     */
    private void sendCrashReportsToServer(Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        File parentFile = new File(getCrashLogDir()).getParentFile();
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));

            for (String fileName: sortedFiles) {
                File cr = new File(parentFile, fileName);
                if (postReport(cr)) {
                    cr.delete();// 删除已发送的报告
                }
            }
        }
    }

    // 发送错误报告邮件
    private boolean postReport(File file) {
        Mail m = new Mail(MailInfo.getMsg("mail_user"), MailInfo.getMsg("mail_pass"));
        m.setHost(MailInfo.getMsg("mail_host"));
        m.setPort(MailInfo.getMsg("mail_port"));
        m.setSport(MailInfo.getMsg("mail_sport"));
        m.setDebuggable(true);
        String[] toArr = {MailInfo.getMsg("mail_to")};
        m.setTo(toArr);
        m.setFrom(MailInfo.getMsg("mail_from"));

        String product = Build.PRODUCT;
        String mac = NetworkUtil.getMac(mContext);
        String authCode = MsgParser.getAuthCode();
        mac = (mac == null ? "cannot get mac" : mac);
        authCode = (authCode == null ? "cannot get authCode" : authCode);
        m.setSubject(String.format(MailInfo.getMsg("mail_subject"), product, authCode, mac));
        m.setBody(MailInfo.getMsg("mail_body"));
        try {
            m.addAttachment(file.getPath());
            boolean isSuccess = m.send();
            if (isSuccess) {
                Logger.getLogger().d("Email was send successfully...");
                return true;
            } else {
                Logger.getLogger().w("Email send failure...");
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger().e("An error occured while sending email with report file...", e);
            return false;
        }
    }

    /**
     * 获取错误报告文件名
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx) {
        // 压缩文件放在crash目录的父目录下
        File filesDir = new File(getCrashLogDir()).getParentFile();
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(CRASH_REPORTER_PRE) && name.endsWith(".zip");
            }
        };
        return filesDir.list(filter);
    }

    /**
     * 删除全部错误报告
     * @param ctx
     */
    public void delCrashReportFiles(Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        File parentFile = new File(getCrashLogDir()).getParentFile();
        if (crFiles != null && crFiles.length > 0) {
            for (String fileName: crFiles) {
                File cr = new File(parentFile, fileName);
                if (cr.exists()) {
                    cr.delete();
                }
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return
     */
    private void saveCrashInfoToFile(Throwable ex, String path) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        mDeviceCrashInfo.put(STACK_TRACE, result);

        try {
            FileOutputStream trace = new FileOutputStream(new File(path));
            mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
        } catch (Exception e) {
            Logger.getLogger().e("An error occured while writing report file...", e);
        }
    }

    /**
     * 收集程序崩溃的设备信息
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set"
                        : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, "" + pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            Logger.getLogger().e("Error while collect package info", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        Field[] fields = android.os.Build.class.getDeclaredFields();
        for (Field field: fields) {
            try {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName().toString(), field.get(null).toString());
                Logger.getLogger().d(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Logger.getLogger().e("Error while collect crash info", e);
            }
        }
        //保存sdk版本
        mDeviceCrashInfo.put(SDK_VERSION, String.valueOf(android.os.Build.VERSION.SDK_INT));
        // 保存设备MAC地址
        String mac = NetworkUtil.getMac(ctx);
        mDeviceCrashInfo.put(MAC, mac == null ? "cannot get mac" : mac);
        String authCode = MsgParser.getAuthCode();
        mDeviceCrashInfo.put(AUTHCODE, authCode == null ? "cannot get AUTHCODE" : authCode);
    }

    /**
     * 启动日志上报服务
     */
    private void sendCrashReportService() {
        Intent intent = new Intent(mContext, CrashReporterService.class);
        mContext.startService(intent);
    }

    /**
     * 获取异常日志临时存储目录
     * @return
     */
    private String getCrashLogDir() {
        String path = null;
        String sdpath = getSDPath();
        if (sdpath == null || sdpath.length() == 0) {
            // 使用内置存储
            File file = mContext.getDir("crash", Context.MODE_PRIVATE | Context.MODE_WORLD_READABLE
                    | Context.MODE_WORLD_WRITEABLE);
            path = file.getAbsolutePath();
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
        } else {
            //在sd目录下创建包名文件目录
            String pkgNamePath = sdpath + File.separator + mContext.getPackageName();
            File pkgNameFile = new File(pkgNamePath);
            if (!pkgNameFile.exists() || !pkgNameFile.isDirectory()) {
                pkgNameFile.mkdirs();
            }
            //在包名文件目录下创建download文件目录
            path = pkgNamePath + File.separator + "crash";
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
        }
        Logger.getLogger().d("crash log dir path:" + path);
        return path;

    }

    /**
     * 取SD卡路径
     */
    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

}
