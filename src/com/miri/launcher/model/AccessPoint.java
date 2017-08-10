/* 
 * 文件名：AccessPoint.java
 * 版权：Copyright
 */
package com.miri.launcher.model;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-11
 */
public class AccessPoint {

    private ScanResult scanResult;

    private WifiConfiguration config;

    private boolean cuurConnectPoint;

    public AccessPoint(ScanResult result, boolean cuurConnectPoint, WifiConfiguration config) {
        scanResult = result;
        this.config = config;
        this.cuurConnectPoint = cuurConnectPoint;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public WifiConfiguration getConfig() {
        return config;
    }

    public boolean isConnectPoint() {
        return cuurConnectPoint;
    }

}
