package com.miri.launcher.http;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import android.os.Build;


/**
 * http请求帮助类
 */
public class HttpRequestHelper {

    /** 默认的http连接超时为10秒 */
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;

    /** 默认的http读取超时为10秒 */
    public static final int DEFAULT_READ_TIMEOUT = 10 * 1000;

    /**
     * 发送GET请求,请求超时{@linkplain HttpRequestHelper#DEFAULT_CONNECT_TIMEOUT} 秒，读取超时30秒
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public static String sendGet(String urlString) throws NetWorkInfoException {
        return sendGet(urlString, DEFAULT_CONNECT_TIMEOUT, -1, null);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param rTimeout 读取超时时间
     * @return 响应对象
     * @throws IOException
     */
    public static String sendGet(String urlString, int rTimeout) throws NetWorkInfoException {
        return sendGet(urlString, DEFAULT_CONNECT_TIMEOUT, rTimeout, null);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param rTimeout 读取超时时间
     * @return 响应对象
     * @throws IOException
     */
    public static String sendGet(String urlString, int rTimeout, Map<String, String> paramers)
            throws NetWorkInfoException {
        return sendGet(urlString, DEFAULT_CONNECT_TIMEOUT, rTimeout, paramers);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param timOut 超时时间
     * @return 响应对象
     * @throws IOException
     */
    public static String sendGet(String urlString, int cTimeout, int rTimeout,
            Map<String, String> paramers) throws NetWorkInfoException {
        Map<String, String> propertys = new HashMap<String, String>();
        //        propertys.put("Connection", "Keep-Alive");
        propertys.put("http.keepAlive", "false");
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            propertys.put("Connection", "close");
        }
        propertys.put("Charset", "UTF-8");
        return sendGet(urlString, cTimeout, rTimeout, paramers, propertys);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param cTimeout 连接超时时间,不大于0取默认值
     * @param timOut 读取数据超时时间 ,不大于0取默认值
     * @param paramers 参数集合
     * @param propertys 请求参数集合
     * @return 响应对象 字符串
     * @throws IOException
     */
    public static String sendGet(String urlString, int cTimeout, int rTimeout,
            Map<String, String> paramers, Map<String, String> propertys)
            throws NetWorkInfoException {
        HttpRequester request = new HttpRequester();
        request.setConnectTimeout(cTimeout);
        request.setReadTimeout(rTimeout);
        return request.sendBeStr(urlString, "GET", paramers, propertys);

    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @return 响应对象 Reader流
     * @throws IOException
     */
    public static Reader sendGetBeReader(String urlString) throws NetWorkInfoException {
        return sendGetBeReader(urlString, -1);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @return 响应对象 Reader流
     * @throws IOException
     */
    public static Reader sendGetBeReader(String urlString, int rTimeOut)
            throws NetWorkInfoException {
        return sendGetBeReader(urlString, rTimeOut, null);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param rTimeOut 读取超时时间
     * @return 响应对象 Reader流
     * @throws IOException
     */
    public static Reader sendGetBeReader(String urlString, int rTimeOut,
            Map<String, String> paramers) throws NetWorkInfoException {
        Map<String, String> propertys = new HashMap<String, String>();
        //        propertys.put("Connection", "Keep-Alive");
        propertys.put("http.keepAlive", "false");
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            propertys.put("Connection", "close");
        }
        propertys.put("Charset", "UTF-8");
        return sendGetBeReader(urlString, rTimeOut, paramers, propertys);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param rTimeOut 读取超时时间 ,不大于0取默认值
     * @param paramers 参数集合
     * @param propertys 请求参数集合
     * @return 响应对象 Reader流
     * @throws IOException
     */
    public static Reader sendGetBeReader(String urlString, int rTimeOut,
            Map<String, String> paramers, Map<String, String> propertys)
            throws NetWorkInfoException {
        HttpRequester request = new HttpRequester();
        request.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        request.setReadTimeout(rTimeOut);
        return request.sendBeReader(urlString, "GET", paramers, propertys);
    }

    /***********************************************************************************/
    /*********************************** POST请求 ***************************************/
    /***********************************************************************************/
    /***********************************************************************************/

    /**
     * 发送POST请求
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public static String sendPost(String urlString) throws NetWorkInfoException {
        Map<String, String> propertys = new HashMap<String, String>();
        //        propertys.put("Connection", "Keep-Alive");
        propertys.put("http.keepAlive", "false");
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            propertys.put("Connection", "close");
        }
        propertys.put("Charset", "UTF-8");
        return sendPost(urlString, -1, null, propertys);
    }

    /**
     * 发送POST请求
     * @param urlString URL地址
     * @param timOut
     * @param paramers
     * @return 响应对象
     * @throws IOException
     */
    public static String sendPost(String urlString, Map<String, String> paramers)
            throws NetWorkInfoException {
        Map<String, String> propertys = new HashMap<String, String>();
        //        propertys.put("Connection", "Keep-Alive");
        propertys.put("http.keepAlive", "false");
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            propertys.put("Connection", "close");
        }
        propertys.put("Charset", "UTF-8");
        return sendPost(urlString, -1, paramers, propertys);
    }

    /**
     * 发送POST请求
     * @param urlString URL地址
     * @param timOut 超时时间 ,不大于0取默认值
     * @param paramers 参数集合
     * @param propertys 请求参数集合
     * @return 响应对象
     * @throws IOException
     */
    public static String sendPost(String urlString, int timOut, Map<String, String> paramers,
            Map<String, String> propertys) throws NetWorkInfoException {
        HttpRequester request = new HttpRequester();
        request.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        request.setReadTimeout(timOut);
        return request.sendBeStr(urlString, "POST", paramers, propertys);

    }

}
