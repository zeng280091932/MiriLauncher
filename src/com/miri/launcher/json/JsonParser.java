/* 
 * 文件名：JsonParser.java
 * 版权：Copyright
 */
package com.miri.launcher.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.GsonBuilder;
import com.miri.launcher.http.HttpRequestHelper;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.utils.GsonHelper;
import com.miri.launcher.utils.Logger;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-20
 */
public class JsonParser {

    /**
     * 解析json 读取文件30秒超时
     * @param urlString url地址
     * @param clazz
     * @return
     * @throws JsonInstanceParserException
     */
    public static Object parse(String urlString, Class<?> clazz) throws JsonParserException,
            NetWorkInfoException {
        return parse(urlString, clazz, -1);
    }

    /**
     * 解析json
     * @param urlString url地址
     * @param clazz
     * @param timeOut 设置读取网络文件超时时间 (毫秒) 小于等于0,默认30秒超时
     * @return
     * @throws JsonInstanceParserException
     */
    public static Object parse(String urlString, Class<?> clazz, int timeOut)
            throws JsonParserException, NetWorkInfoException {
        Object document = null;
        Long start = System.currentTimeMillis();
        Logger.getLogger().d(
                "parameters:{urlString:" + urlString + ",clazz:" + clazz.getName() + "}");
        BufferedReader jsonReader = null;
        String jsonStr = null;
        try {
            //读取网络文件流
            //            jsonReader = (BufferedReader) HttpRequestHelper.sendGetBeReader(urlString, timeOut);

            //读取网络文件字符串
            jsonStr = HttpRequestHelper.sendGet(urlString, timeOut);
            //            Logger.getLogger().d(jsonStr);

            Long end1 = System.currentTimeMillis();
            //            Logger.getLogger().d("读取[" + urlString + "]文件时间－－>" + (end1 - start) + "ms");

            GsonBuilder builder = GsonHelper.createGsonBuilder();
            //            document = builder.create().fromJson(json, clazz);
            document = builder.create().fromJson(jsonStr, clazz);
        } catch (NetWorkInfoException e) {
            Logger.getLogger().e("获取json数据网络错误！" + e.getMessage());
            throw new NetWorkInfoException(e);
        } catch (Exception e) {
            Logger.getLogger().e("解析json数据错误！" + e.getMessage());
            throw new JsonParserException(e);
        } finally {
            try {
                if (jsonReader != null) {
                    jsonReader.close();
                    jsonReader = null;
                }
            } catch (IOException e) {
                //ignore
            }
        }
        Long end = System.currentTimeMillis();
        //        Logger.getLogger().d("解析Json[" + urlString + "]总时间－－>" + (end - start) + "ms");
        return document;
    }

    /**
     * 解析json
     * @param jsonStr
     * @param clazz
     * @param timeOut 设置读取网络文件超时时间 (毫秒) 小于等于0,默认30秒超时
     * @return
     * @throws JsonInstanceParserException
     */
    public static Object parseForString(String jsonStr, Class<?> clazz, int timeOut)
            throws JsonParserException {
        Object document = null;
        try {
            GsonBuilder builder = GsonHelper.createGsonBuilder();
            document = builder.create().fromJson(jsonStr, clazz);
        } catch (Exception e) {
            Logger.getLogger().e("解析json数据错误！" + e.getMessage());
            throw new JsonParserException(e);
        }
        return document;
    }

    /**
     * 解析json
     * @param is
     * @param clazz
     * @param timeOut 设置读取网络文件超时时间 (毫秒) 小于等于0,默认30秒超时
     * @return
     * @throws JsonInstanceParserException
     */
    public static Object parseForStream(Reader is, Class<?> clazz, int timeOut)
            throws JsonParserException {
        Object document = null;
        try {
            GsonBuilder builder = GsonHelper.createGsonBuilder();
            document = builder.create().fromJson(is, clazz);
        } catch (Exception e) {
            Logger.getLogger().e("解析json数据错误！" + e.getMessage());
            throw new JsonParserException(e);
        }
        return document;
    }

    /**
     * @param clazz
     * @return
     */
    public static String generateJson(Object obj) {
        GsonBuilder builder = GsonHelper.createGsonBuilder();
        String jsonStr = builder.create().toJson(obj);
        return jsonStr;
    }

}
