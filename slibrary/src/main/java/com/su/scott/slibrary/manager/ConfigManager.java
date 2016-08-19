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

    private ConfigManager() {
    }

    /**
     * 存储boolean类型值
     *
     * @param context
     * @param key
     * @param value
     */
    private static void putBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    /**
     * 存储int类型值
     *
     * @param context
     * @param key
     * @param value
     */
    private static void putInt(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply();
    }

    /**
     * 存储String类型值
     *
     * @param context
     * @param key
     * @param value
     */
    private static void putString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    /**
     * 存储long类型值
     *
     * @param context
     * @param key
     * @param value
     */
    private static void putLong(Context context, String key, long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, value).apply();
    }

    /**
     * 获取boolean类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    private static void getBoolean(Context context, String key, boolean valueDefault) {
        PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, valueDefault);
    }

    /**
     * 获取int类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    private static void getInt(Context context, String key, int valueDefault) {
        PreferenceManager.getDefaultSharedPreferences(context).getInt(key, valueDefault);
    }

    /**
     * 获取String类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    private static void getString(Context context, String key, String valueDefault) {
        PreferenceManager.getDefaultSharedPreferences(context).getString(key, valueDefault);
    }

    /**
     * 获取long类型值
     *
     * @param context
     * @param key
     * @param valueDefault
     */
    private static void getLong(Context context, String key, long valueDefault) {
        PreferenceManager.getDefaultSharedPreferences(context).getLong(key, valueDefault);
    }


}
