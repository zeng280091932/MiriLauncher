package com.miri.launcher.utils;

/* 
 * 文件名：WifiConnect.java
 * 版权：Copyright
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.miri.launcher.model.AccessPoint;

public class WifiConnect {

    WifiManager wifiManager;

    //定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况  
    public enum WifiCipherType {
        WIFICIPHER_NOPASS, WIFICIPHER_WEP, WIFICIPHER_PSK, WIFICIPHER_EAP
    }

    //psk加密的类型
    public enum PskType {
        UNKNOWN, WPA, WPA2, WPA_WPA2
    }

    //构造函数  
    public WifiConnect(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    /**
     * 打开Wifi网卡
     */
    public boolean openWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    /**
     * 关闭Wifi网卡
     */
    public boolean closeWifi() {
        boolean bRet = true;
        if (wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(false);
        }
        return bRet;
    }

    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    //提供一个外部接口，传入要连接的无线网  
    public boolean connect(String SSID, String Password, WifiCipherType Type) {
        if (!this.openWifi()) {
            return false;
        }
        //开启wifi功能需要一段时间(手机上测试一般需要1-3秒左右)，所以要等到wifi  
        //状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句  
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                //为了避免程序一直while循环，让它睡个100毫秒在检测……  
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        WifiConfiguration wifiConfig = this.createWifiInfo(SSID, Password, Type);
        Log.d("@@@", "wifiConfig: " + wifiConfig);
        //  
        if (wifiConfig == null) {
            return false;
        }

        WifiConfiguration tempConfig = this.IsExsits(SSID);

        Log.d("@@@", "tempConfig: " + tempConfig);

        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        int netID = wifiManager.addNetwork(wifiConfig);
        boolean bRet = wifiManager.enableNetwork(netID, true);
        wifiManager.saveConfiguration();
        return bRet;
    }

    public boolean removeNetwork(int networkId) {
        return wifiManager.removeNetwork(networkId);
    }

    public void disableNetwork(int networkId) {
        wifiManager.disableNetwork(networkId);
        wifiManager.disconnect();
    }

    /**
     * 查看以前是否也配置过这个网络
     * @param SSID
     * @return
     */
    public WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig: existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 获取可用的网络配置
     * @return
     */
    public WifiConfiguration getEnabledWifiConfig() {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs != null && existingConfigs.size() > 0) {
            for (WifiConfiguration existingConfig: existingConfigs) {
                if (existingConfig.status == WifiConfiguration.Status.ENABLED) {
                    return existingConfig;
                }
            }
        }
        return null;
    }

    /**
     * 根据WifiConfiguration对象来自动连接WIFI网络
     * @param wcg WifiConfiguration对象
     */
    public boolean connectWifiByConfig(WifiConfiguration wcg) {
        if (wcg != null) {
            int wcgID = wifiManager.addNetwork(wcg);
            boolean bRet = wifiManager.enableNetwork(wcgID, true);
            wifiManager.saveConfiguration();
            return bRet;
        }
        return false;
    }

    public WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        Log.d("@@@", "WifiCipherType  ->" + Type);
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.hiddenSSID = true;
            //            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //            config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_PSK) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            //设置判断加密方法
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            //设置GroupCipher的加密方式
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            //            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            return null;
        }
        return config;
    }

    public boolean startWifiScan() {
        return wifiManager.startScan();
    }

    /**
     * 扫描周边网络
     */
    @SuppressWarnings("unchecked")
    public List<AccessPoint> getScanResult() {
        //        wifiManager.startScan();
        List<AccessPoint> accessPoint = new ArrayList<AccessPoint>();
        WifiInfo info = wifiManager.getConnectionInfo();
        List<ScanResult> listResult = wifiManager.getScanResults();
        if (!Toolkit.isEmpty(listResult)) {
            for (ScanResult result: listResult) {
                // Ignore hidden and ad-hoc networks.
                if (result.SSID == null || result.SSID.length() == 0
                        || result.capabilities.contains("[IBSS]")) {
                    continue;
                }

                boolean isCurrPoint = false;
                if (info != null && result.SSID.equals(info.getSSID())) {
                    isCurrPoint = true;
                }
                WifiConfiguration config = IsExsits(result.SSID);
                AccessPoint ap = new AccessPoint(result, isCurrPoint, config);
                accessPoint.add(ap);
            }
        }
        if (!Toolkit.isEmpty(accessPoint)) {
            Collections.sort(accessPoint, new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    AccessPoint a1 = (AccessPoint) o1;
                    AccessPoint a2 = (AccessPoint) o2;
                    if (a1.isConnectPoint() && !a2.isConnectPoint()) {
                        return -1;
                    }

                    if (!a1.isConnectPoint() && a2.isConnectPoint()) {
                        return 1;
                    }

                    int level1 = a1.getScanResult().level;
                    int level2 = a2.getScanResult().level;
                    if (level1 > level2) {
                        return -1;
                    } else if (level1 < level2) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        return accessPoint;
    }

    /**
     * 判断加密类型
     * @param result
     * @return
     */
    public static WifiCipherType getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (result.capabilities.contains("PSK")) {
            return WifiCipherType.WIFICIPHER_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return WifiCipherType.WIFICIPHER_EAP;
        }
        return WifiCipherType.WIFICIPHER_NOPASS;
    }

    /**
     * 判断加密类型
     * @param config
     * @return
     */
    public static WifiCipherType getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
            return WifiCipherType.WIFICIPHER_PSK;
        }
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
                || config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
            return WifiCipherType.WIFICIPHER_EAP;
        }
        return (config.wepKeys[0] != null && !config.wepKeys[0].equals("")) ? WifiCipherType.WIFICIPHER_WEP
                : WifiCipherType.WIFICIPHER_NOPASS;
    }

    public static PskType getPskType(ScanResult result) {
        boolean wpa = result.capabilities.contains("WPA-PSK");
        boolean wpa2 = result.capabilities.contains("WPA2-PSK");
        if (wpa2 && wpa) {
            return PskType.WPA_WPA2;
        } else if (wpa2) {
            return PskType.WPA2;
        } else if (wpa) {
            return PskType.WPA;
        } else {
            Log.w("@@@", "Received abnormal flag string: " + result.capabilities);
            return PskType.UNKNOWN;
        }
    }

}
