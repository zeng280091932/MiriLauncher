package com.miri.launcher.upgrade;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.miri.launcher.R;
import com.miri.launcher.activity.CheckUpgradeActivity;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.view.dialog.AlertDialog;
import com.miri.launcher.view.dialog.AlertUtil;

/**
 * 软件更新帮助类
 */
public class UpgradeHelper {

    /** appType:未知软件类型 */
    public static final int UNKNOW_APP = 0;

    /** appType:用户软件类型 */
    public static final int USER_APP = 1;

    /** appType:系统软件 */
    public static final int SYSTEM_APP = 2;

    /** appType:系统升级软件 */
    public static final int SYSTEM_UPDATE_APP = 4;

    /** appType:系统+升级软件 */
    public static final int SYSTEM_REF_APP = SYSTEM_APP | SYSTEM_UPDATE_APP;

    /** 异常类型 */
    public static final int EX_NETWORK_ERROR = 0;

    public static final int EX_PARSEFILE_FAIL = 1;

    public static final int IS_LASTEST_VERSION = 2;

    public static final int EXIST_UPGRADE_VERSION = 3;

    /**
     * 升级
     * @param context
     * @param isManual 是否手动
     */
    public void doUpgrade(final Context context, final String urlString, final boolean isManual,
            final OnCheckVersionComplete onCheckVersionComplete) {
        String pname = context.getPackageName();
        Logger.getLogger().d("Package name is : " + pname);
        new AsyncTask<Void, Void, String>() {

            //            private int force = 0;
            //
            //            private String log = "";

            private Integer remoteVersionCode;

            private String remoteVersion;

            @Override
            protected String doInBackground(Void... params) {
                int localVersionCode = getLocalVersionCode(context);
                Software software = null;
                Exception ex = null;
                int retryTimes = 10;
                int i = 0;
                // 重试至成功
                do {
                    try {
                        i++;
                        if (!NetworkUtil.isNetworkAvailable(context)) {
                            Thread.sleep(1000);
                            Logger.getLogger().e("retry time:" + i + ", network is error.");
                            continue;
                        }
                        software = getRemoteVersionInfo(urlString);
                        if (software != null) {
                            ex = null;
                            Logger.getLogger().d("software:" + software);
                            break;
                        }
                        Logger.getLogger().e("retry time:" + i + ", model is null.");
                    } catch (NetWorkInfoException e) {
                        ex = e;
                        Logger.getLogger().e("retry time:" + i + ", " + e.getMessage());
                    } catch (Exception e) {
                        ex = e;
                        Logger.getLogger().e("retry time:" + i + ", " + e.getMessage());
                    }
                } while (!isManual && i < retryTimes);
                // 没出异常
                if (ex == null) {
                    if (software == null) {
                        if (onCheckVersionComplete != null) {
                            onCheckVersionComplete.onComplete(EX_PARSEFILE_FAIL, software);
                        }
                        return null;
                    }
                    //                    String forceStr = software.getForce();
                    //                    if (forceStr != null && Toolkit.isInteger(forceStr)) {
                    //                        force = Integer.parseInt(forceStr);
                    //                    }
                    //                    log = software.getLog() == null ? "" : software.getLog();
                } else {
                    if (ex instanceof NetWorkInfoException) {
                        Logger.getLogger().e("读取升级文件出错", ex);
                        if (onCheckVersionComplete != null) {
                            onCheckVersionComplete.onComplete(EX_NETWORK_ERROR, software);
                        }
                        return null;
                    } else {
                        Logger.getLogger().e("解析升级文件出错", ex);
                        if (onCheckVersionComplete != null) {
                            onCheckVersionComplete.onComplete(EX_PARSEFILE_FAIL, software);
                        }
                        return null;
                    }
                }

                remoteVersionCode = software.getVersionCode();
                remoteVersion = software.getVersion();
                String path = software.getUrl();
                Logger.getLogger().d(
                        "localVersionCode:" + localVersionCode + ", remoteVersionCode:"
                                + remoteVersionCode);
                // 存在则检查本地文件的版本号和远程版本是否一致
                if (remoteVersionCode != null && remoteVersionCode > localVersionCode
                        && path != null) {
                    // 不一致，启动下载对话框
                    Logger.getLogger().d(
                            "Have new version! the remoteVersionCode is " + remoteVersionCode);
                    Logger.getLogger().d("Have new version! the remoteVersion is " + remoteVersion);
                    if (onCheckVersionComplete != null) {
                        onCheckVersionComplete.onComplete(EXIST_UPGRADE_VERSION, software);
                    }
                    return path;
                } else {
                    if (onCheckVersionComplete != null) {
                        onCheckVersionComplete.onComplete(IS_LASTEST_VERSION, software);
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null && result.trim().length() != 0 && !isManual) {
                    // 弹出询问用户是否安装更新的对话框
                    AlertUtil.showAlert(context, context.getString(R.string.netwrok_upgrade),
                            context.getString(R.string.upgrade_msg_tip, remoteVersion),
                            context.getString(R.string.yes),
                            new AlertDialog.OnOkBtnClickListener() {

                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(context, CheckUpgradeActivity.class);
                                    context.startActivity(intent);
                                }
                            }, context.getString(R.string.no), null);
                }
            }
        }.execute();
        // }
    }

    /**
     * 获取本地版本字符串(AndroidManifest.xml中配置的版本信息)
     * @return
     */
    private int getLocalVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        Integer versionCode = null;

        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * 获取服务器软件版本信息
     * @return
     * @throws ParserConfigurationException
     * @throws NetWorkInfoException
     * @throws SAXException
     * @throws IOException
     */
    private Software getRemoteVersionInfo(String urlString) throws NetWorkInfoException,
            ParserFileException {
        Logger.getLogger().d("upgrade file url-->" + urlString);
        // return SoftwareParser.parseXmlSoftware(urlString);
        return SoftwareParser.parseJsonSoftware(urlString);
    }

    /**
     * 检查app是否是系统rom集成的
     * @param pname
     * @return
     */
    public static int checkAppType(Context context, String pname) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(pname, 0);

            // 是系统软件或者是系统软件更新
            if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {
                return SYSTEM_REF_APP;
            } else {
                return USER_APP;
            }

        } catch (NameNotFoundException e) {
            Logger.getLogger().e(e.getMessage());
        }

        return UNKNOW_APP;
    }

    /**
     * 是否是系统软件
     * @param pInfo
     * @return
     */
    private static boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    /**
     * 是否是系统软件的更新软件
     * @param pInfo
     * @return
     */
    private static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public interface OnCheckVersionComplete {

        public void onComplete(int exType, Software softWare);
    }
}
