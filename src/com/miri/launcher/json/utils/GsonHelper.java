package com.miri.launcher.json.utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.GsonBuilder;

/**
 * Gson常用封装工具类
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-20
 */
public class GsonHelper {

    /** 空的 数据 */
    public static final String EMPTY_JSON = "{}";

    /** 空的 数组(集合)数据 */
    public static final String EMPTY_JSON_ARRAY = "[]";

    /** 默认的 日期/时间字段的格式化模式。 */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     ** 构建GsonBuilder对象 <br>
     * @param isSerializeNulls 是否序列化 {@code null} 值字段。 <br>
     * @param version 字段的版本号注解。 <br>
     * @param datePattern 日期字段的格式化模式。 <br>
     * @param typeAdpater 适配器<br>
     * @param excludesFieldsWithoutExpose 是否排除未标注 { @Expose} 注解的字段。
     * @param strategy 进行反序化时，进行排除策略
     */
    public static GsonBuilder createGsonBuilder(boolean isSerializeNulls, Double version,
            String datePattern, Map<Type, Object> typeAdpater, boolean excludesFieldsWithoutExpose,
            ExclusionStrategy strategy) {
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls) {
            builder.serializeNulls();
        }
        if (version != null) {
            builder.setVersion(version.doubleValue());
        }
        if (datePattern == null || datePattern.equals("")) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        if (strategy != null) {
            builder.addDeserializationExclusionStrategy(strategy);
        }
        if (typeAdpater != null && typeAdpater.size() > 0) {
            for (Map.Entry<Type, Object> entry: typeAdpater.entrySet()) {
                Type type = entry.getKey();
                Object value = entry.getValue();
                builder.registerTypeAdapter(type, value);
            }
        }
        return builder;

    }

    /**
     ** 构建GsonBuilder对象 <br>
     * <ul>
     * <li>该方法会转换所有未标注或已标注 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     */
    public static GsonBuilder createGsonBuilder(Map<Type, Object> typeAdpater) {
        return GsonHelper.createGsonBuilder(false, null, null, typeAdpater, false, null);

    }

    /**
     ** 构建GsonBuilder对象 <br>
     * <ul>
     * <li>该方法会转换所有未标注或已标注 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     */
    public static GsonBuilder createGsonBuilder() {
        return GsonHelper.createGsonBuilder(false, null, null, null, false, null);
    }

}
