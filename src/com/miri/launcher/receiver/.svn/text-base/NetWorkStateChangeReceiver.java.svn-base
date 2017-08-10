package com.miri.launcher.receiver;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.miri.launcher.Constants;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Summary;
import com.miri.launcher.utils.Toolkit;

/**
 * 检测网络状态
 */
public class NetWorkStateChangeReceiver extends BroadcastReceiver {

    private Context mContext;

    private Handler mHandler;

    public NetWorkStateChangeReceiver(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        String action = intent.getAction();
        Logger.getLogger().d("onReceive:" + action);
        //监听wifi的打开与关闭，与wifi的连接无关 
        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {

            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            int prev_state = intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, -1);
            Logger.getLogger().d("wifiState:" + state + ", prev_state:" + prev_state);

            if (state == WifiManager.WIFI_STATE_ENABLED && NetworkUtil.isWifiConnected(mContext)) {
                Message msg = mHandler.obtainMessage(Constants.WIFI_STATE_CONNECTED);
                msg.arg1 = NetworkUtil.getRssiLevel(mContext);
                msg.obj = Summary.removeDoubleQuotes(wifiManager.getConnectionInfo().getSSID());
                Logger.getLogger().d("wifi SSID:" + msg.obj);
                mHandler.sendMessage(msg);
            } else {
                Message msg = mHandler.obtainMessage(Constants.WIFI_STATE_DISCONNECT);
                msg.arg1 = state;
                mHandler.sendMessage(msg);
            }
        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。  
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线  
        else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
            Logger.getLogger().d("bssid--->" + bssid);
            //通过这个wifiInfo第一次获取的rssi是-9999,因此建议不用这个。WifiManager.EXTRA_WIFI_INFO 4.0可用
            Parcelable wifiInfoExtra = intent.getParcelableExtra("wifiInfo");
            if (wifiInfoExtra != null) {
                WifiInfo wifiInfo = (WifiInfo) wifiInfoExtra;
                Logger.getLogger().d(
                        "wifiInfo:" + wifiInfo + ", #WIFI_SERVICE#-->wifiInfo:"
                                + wifiManager.getConnectionInfo());
            }

            Parcelable networkInfoExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfoExtra != null) {
                NetworkInfo networkInfo = (NetworkInfo) networkInfoExtra;
                State state = networkInfo.getState();
                boolean isConnected = state == State.CONNECTED;
                Logger.getLogger().d("isConnected:" + isConnected + ",networkInfo:" + networkInfo);
                if (isConnected) {
                    Message msg = mHandler.obtainMessage(Constants.WIFI_STATE_CONNECTED);
                    msg.arg1 = NetworkUtil.getRssiLevel(mContext);
                    msg.obj = Summary.removeDoubleQuotes(wifiManager.getConnectionInfo().getSSID());
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = mHandler.obtainMessage(Constants.WIFI_STATE_DISCONNECT);
                    msg.arg1 = state == State.UNKNOWN ? WifiManager.WIFI_STATE_UNKNOWN
                            : WifiManager.WIFI_STATE_DISABLED;
                    mHandler.sendMessage(msg);
                }
            }
        } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            if (!NetworkUtil.isWifiConnected(mContext)) {
                Logger.getLogger().e("No Wi-Fi AP is connected!");
                return;
            }
            int rssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -100);
            int level = NetworkUtil.getRssiLevel(rssi);
            Message msg = mHandler.obtainMessage(Constants.WIFI_RSSI_CHANGED);
            Logger.getLogger().d("RSSI change action,rssi: " + rssi + ", level: " + level);
            msg.arg1 = level;
            mHandler.sendMessage(msg);
        } else if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            List<ScanResult> list = wifiManager.getScanResults();
            if (!Toolkit.isEmpty(list)) {
                for (ScanResult sr: list) {
                    Logger.getLogger().d("ScanResult:" + sr);
                }
            }
        }
        //这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。
        else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                Logger.getLogger().d("network type-->" + info.getType());
                State state = info.getState();
                boolean isConnected = state == State.CONNECTED;

                //判断wifi连接
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (isConnected) {
                        Message msg = mHandler.obtainMessage(Constants.WIFI_STATE_CONNECTED);
                        msg.arg1 = NetworkUtil.getRssiLevel(mContext);
                        Logger.getLogger().d("CONNECTIVITY_ACTION, level: " + msg.arg1);
                        msg.obj = Summary.removeDoubleQuotes(wifiManager.getConnectionInfo()
                                .getSSID());
                        Logger.getLogger().d("CONNECTIVITY_ACTION, msg.obj: " + msg.obj);
                        mHandler.sendMessage(msg);
                    } else {
                        Message msg = mHandler.obtainMessage(Constants.WIFI_STATE_DISCONNECT);
                        msg.arg1 = state == State.UNKNOWN ? WifiManager.WIFI_STATE_UNKNOWN
                                : WifiManager.WIFI_STATE_DISABLED;
                        mHandler.sendMessage(msg);
                    }

                }

                // 判断有线网络连接,[ConnectivityManager.TYPE_ETHERNET==9 , 4.0可用]
                if (info.getType() == 9) {
                    //有线可用，可能未连接成功
                    if (info.isAvailable() && isConnected) {
                        Message msg = mHandler.obtainMessage(Constants.WIRED_STATE_CONNECTED);
                        mHandler.sendMessage(msg);
                    } else {
                        Message msg = mHandler.obtainMessage(Constants.WIRED_STATE_DISCONNECT);
                        mHandler.sendMessage(msg);
                    }
                }

                StringBuilder sb = new StringBuilder();
                sb.append("info.getTypeName() : " + info.getTypeName() + "\n");
                sb.append("getSubtypeName() : " + info.getSubtypeName() + "\n");
                sb.append("getState() : " + info.getState() + "\n");
                sb.append("getDetailedState() : " + info.getDetailedState().name() + "\n");
                sb.append("getExtraInfo() : " + info.getExtraInfo() + "\n");
                sb.append("getType() : " + info.getType());
                Logger.getLogger().d(sb.toString());
            }
        }
    }
}
