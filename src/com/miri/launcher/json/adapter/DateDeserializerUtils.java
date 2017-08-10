package com.miri.launcher.json.adapter;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * 日期序列化实用工具类
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-20
 */
public class DateDeserializerUtils implements JsonDeserializer<java.util.Date> {

    /**
     * 把指定格式的日期json字符串反序列化成java日期对象
     * @param json
     * @param typeOfT
     */
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
    }

    /**
     * 将字符串转换成日期，默认格式：yyyy-MM-dd
     * @param string - 日期字符串
     * @return 按默认格式转换后的日期
     */
    private static Date stringToDate(String string) {
        if (string == null || string.equals("") || string.length() < 1) {
            return null;
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
