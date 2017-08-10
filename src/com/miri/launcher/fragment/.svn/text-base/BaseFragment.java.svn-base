/* 
 * 文件名：BaseFragment.java
 * 版权：Copyright
 */
package com.miri.launcher.fragment;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miri.launcher.MoretvConstants;
import com.miri.launcher.MoretvData;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.utils.ApkUtil;
import com.miri.launcher.utils.AppManager;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.ResourceHelper;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomToast;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-17
 */
public class BaseFragment extends Fragment {

    private DownloadService downloadService;

    private ServiceConnection dlSvcConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ResourceHelper.updateConfig(getActivity());
        super.onCreate(savedInstanceState);
        dlSvcConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadService = ((DownloadService.MyBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        // Bind the DownloadService.
        getActivity().bindService(new Intent(getActivity(), DownloadService.class),
                dlSvcConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(dlSvcConnection);
        super.onDestroy();
    }

    /**
     * 跳转到Movetv详情页面
     * @param context
     * @param linkData
     */
    protected void toDetail(Context context, String linkData) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            CustomToast.makeText(context, "网络未连接，请检查网络", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction("moretv.service.action");
            if (MoretvData.sLoginInfo != null) {
                intent.putExtra("UserID", MoretvData.sLoginInfo.getUserId());
                intent.putExtra("Token", MoretvData.sLoginInfo.getToken());
            }
            intent.putExtra("Data", linkData);
            intent.putExtra("launcher", 1);
            intent.putExtra("ReturnMode", 0);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            boolean installExist = ApkUtil.installExist(context, MoretvConstants.FILE_NAME);
            Log.d("Debug", "installExist:" + installExist);
            if (!installExist && downloadService != null) {
                downloadService.createDownload(MoretvConstants.APK_URL, null,
                        MoretvConstants.APK_NAME, null, true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param context
     * @param pkgName 包名
     * @param url 应用下载路径
     * @param appName 应用名称
     * @param iconPath 应用图标路径
     */
    protected void startApp(Context context, String pkgName, String url, String appName,
            String iconPath) {
        boolean isNeedInstall = false;
        if (!Toolkit.isEmpty(pkgName)) {
            // 判断应用是否已安装
            boolean isExist = Toolkit.isExistApp(context, pkgName);
            if (isExist) {
                // 启动app
                AppManager.runApp(context, pkgName);
            } else {
                isNeedInstall = true;
            }
        } else {
            isNeedInstall = true;
        }

        if (isNeedInstall) {
            boolean installExist = false;
            if (!Toolkit.isEmpty(pkgName) && pkgName.equals(MoretvConstants.PACKAGE_NAME)) {
                installExist = ApkUtil.installExist(context, MoretvConstants.FILE_NAME);
            }
            Log.d("Debug", "installExist:" + installExist);
            // TODO 去下载安装
            if (!installExist && downloadService != null) {
                downloadService.createDownload(url, null, appName, iconPath, true, true);
            }
        }

    }

}
