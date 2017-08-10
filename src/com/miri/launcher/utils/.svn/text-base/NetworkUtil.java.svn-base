package com.miri.launcher.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

/**
 * 网络工具类
 */
public class NetworkUtil {

    /** wifi信号等级 */
    public static final int WIFI_SIGNAL_LEVEL = 4;

    /**
     * 获得信号强度等级
     * @param ctx
     * @return
     */
    public static int getRssiLevel(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int rssi = wifiInfo.getRssi();//获得信号强度RSSI的值
        return getRssiLevel(rssi);
    }

    public static int getRssiLevel(int rssi) {
        int level = WifiManager.calculateSignalLevel(rssi, WIFI_SIGNAL_LEVEL);
        //        Logger.getLogger().d("wifiInfo.getRssi()-->rssi:" + rssi + ", level:" + level);
        return level;
    }

    /**
     * 检测网络是否可用
     * @param ctx
     * @return
     */
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            if (networkInfos != null) {
                for (NetworkInfo network: networkInfos) {
                    //            Logger.getLogger().d(
                    //                    network.getTypeName() + "-->" + "type:" + network.getType() + ", state:"
                    //                            + network.getState() + ", isConnected:" + network.isConnected()
                    //                            + ", isAvailable:" + network.isAvailable());
                    if (network.isConnected()) {
                        return true;
                        //                        //强制检查wifi的具体信息
                        //                        if (network.getType() == ConnectivityManager.TYPE_WIFI) {
                        //                            return isWifiConnected2(ctx);
                        //                        } else {
                        //                            return true;
                        //                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测wifi是否连接上了
     * @param ctx
     * @return
     */
    public static boolean isWifiConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = (cm != null) ? cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                : null;
        return (networkInfo != null) ? networkInfo.isConnected() : false;
    }

    /**
     * 检测wifi是否连接上了(检测wifi的具体连接信息)
     * @param ctx
     * @return
     */
    public static boolean isWifiConnected2(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    String ssid = wifiInfo.getSSID();
                    SupplicantState state = wifiInfo.getSupplicantState();
                    if (ssid != null && state == SupplicantState.COMPLETED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * TODO 检测有线网络以太网是否连接上了
     * @param ctx
     * @return
     */
    @SuppressLint("InlinedApi")
    public static boolean isWiredConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        if (networkInfos == null) {
            return false;
        }

        //ConnectivityManager.TYPE_ETHERNET==9
        NetworkInfo networkInfo = cm.getNetworkInfo(9);
        Logger.getLogger().d("network info---->" + networkInfo);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取mac地址
     * @param ctx
     * @return
     */
    public static String getMac(Context ctx) {
        String wifiMac = getWifiMac(ctx);
        if (!Toolkit.isEmpty(wifiMac)) {
            Logger.getLogger().i("get wifi mac");
            return wifiMac;
        } else {
            Logger.getLogger().i("The wifi mac not found, try to get it via linux");
            String netMac = getNetMac();
            return netMac == null ? "" : netMac;
        }
    }

    /**
     * 获取Wifi的Mac地址
     * @param ctx
     * @return
     */
    public static String getWifiMac(Context ctx) {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null && !TextUtils.isEmpty(info.getMacAddress())) {
                //去掉标点符号
                return info.getMacAddress().replaceAll("[\\p{Punct}\\p{Space}]+", "");
            }
        }
        return null;
    }

    /**
     * 获取以太网mac地址
     * @return
     */
    @SuppressLint("NewApi")
    public static String getNetMac() {
        String str = null;
        try {
            byte[] arrayOfByte = NetworkInterface.getByName("eth0").getHardwareAddress();
            str = hexByte(arrayOfByte[0]) + hexByte(arrayOfByte[1]) + hexByte(arrayOfByte[2])
                    + hexByte(arrayOfByte[3]) + hexByte(arrayOfByte[4]) + hexByte(arrayOfByte[5]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 输出本地网络接口以及显示其信息
     */
    @SuppressLint("NewApi")
    public static void printNetworkInfo() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                System.out.println("displayname: " + ni.getDisplayName());
                System.out.println("name: " + ni.getName());
                System.out.println("MTU: " + ni.getMTU());
                System.out.println("Loopback: " + ni.isLoopback());
                System.out.println("Virtual: " + ni.isVirtual());
                System.out.println("Up: " + ni.isUp());
                System.out.println("PointToPoint: " + ni.isPointToPoint());
                byte[] arryOfByte = ni.getHardwareAddress();
                if (arryOfByte != null) {
                    String mac = hexByte(arryOfByte[0]) + hexByte(arryOfByte[1])
                            + hexByte(arryOfByte[2]) + hexByte(arryOfByte[3])
                            + hexByte(arryOfByte[4]) + hexByte(arryOfByte[5]);
                    System.out.println("mac: " + mac);
                } else {
                    System.out.println("mac is null");
                }
                System.out.println("---------------------");
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

    }

    private static String hexByte(byte paramByte) {
        String str = "000000" + Integer.toHexString(paramByte);
        return str.substring(-2 + str.length());
    }

    /**
     * 读取任意的连接的ip ，（用于获取以太网ip时注意先关闭wifi，）并且过滤掉ipv6的ip值 只返回ipv4的值
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    // &&InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())
                    // 这句话是过滤掉ipv6的ip！
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    /**
     * IP地址转换为整数
     * @param ip
     * @return
     */
    public static long ip2Int(String ip) {
        String[] items = ip.split("\\.");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16
                | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    /**
     * 整数转换为IP地址
     * @param ipInt
     * @return
     */
    public static String int2Ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

}
