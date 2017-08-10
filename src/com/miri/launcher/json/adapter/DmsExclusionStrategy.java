package com.miri.launcher.json.adapter;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * 自定义序列化器和排除策略
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-20
 */
@Deprecated
public class DmsExclusionStrategy implements ExclusionStrategy {

    private String[] excludeFields;

    private Class<?>[] excludeClasses;

    public DmsExclusionStrategy(Class<?> excludeClasses) {
        this(null, excludeClasses);
    }

    public DmsExclusionStrategy(String excludeFields) {
        this(excludeFields, null);
    }

    public DmsExclusionStrategy(String excludeFields, Class<?> excludeClasses) {
        if (excludeFields != null) {
            this.excludeFields = new String[1];
            this.excludeFields[0] = excludeFields;
        }
        if (excludeClasses != null) {
            this.excludeClasses = new Class<?>[1];
            this.excludeClasses[0] = excludeClasses;
        }

    }

    public DmsExclusionStrategy(String[] excludeFields, Class<?>[] excludeClasses) {
        this.excludeFields = excludeFields;
        this.excludeClasses = excludeClasses;
    }

    public DmsExclusionStrategy() {

    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        if (excludeClasses == null) {
            return false;
        }

        for (Class<?> excludeClass: excludeClasses) {
            if (excludeClass.getName().equals(clazz.getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (excludeFields == null) {
            return false;
        }

        for (String field: excludeFields) {
            if (field.equals(f.getName())) {
                return true;
            }
        }

        return false;
    }

    public final String[] getExcludeFields() {
        return excludeFields;
    }

    public final Class<?>[] getExcludeClasses() {
        return excludeClasses;
    }

}
