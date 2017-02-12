package com.su.scott.slibrary.manager;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * @类名 ConfigManager
 * @描述 配置数据管理类
 * @作者 Su
 * @时间 2016年7月
 */
public class ConfigManager {
    private static ConfigManager instance;


    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * 存储boolean类型值
     *
     * @param context
     * @param key
     * @param value
     */
    public void putBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    /**
     * 存储int类型值
     *
     * @param context
     * @param key
     * @param value
     */
    public void putInt(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply();
    }

    /**
     * 存储String类型值
     *
     * @param context
     * @param key
     * @param value
     */
    public void putString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    /**
     * 存储long类型值
     *
     * @param context
     * @param key
     * @param value
     */
    public void putLong(Context context, String key, long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, value).apply();
    }

    /**
     * 获取boolean类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    public boolean getBoolean(Context context, String key, boolean valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, valueDefault);
    }

    /**
     * 获取int类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    public int getInt(Context context, String key, int valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, valueDefault);
    }

    /**
     * 获取String类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    public String getString(Context context, String key, String valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, valueDefault);
    }

    /**
     * 获取long类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    public long getLong(Context context, String key, long valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, valueDefault);
    }


}
