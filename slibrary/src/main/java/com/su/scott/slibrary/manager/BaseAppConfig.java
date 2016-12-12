package com.su.scott.slibrary.manager;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2016/12/9.
 */

public class BaseAppConfig {

    protected static void putBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    protected static void putInt(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, value)
                .apply();
    }

    protected static void putLong(Context context, String key, long value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(key, value)
                .apply();
    }

    protected static void putFloat(Context context, String key, float value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat(key, value)
                .apply();
    }

    protected static void putString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }

    protected static boolean getBoolean(Context context, String key, boolean valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, valueDefault);
    }

    protected static int getInt(Context context, String key, int valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, valueDefault);
    }

    protected static long getLong(Context context, String key, long valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, valueDefault);
    }

    protected static float getFloat(Context context, String key, float valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, valueDefault);
    }

    protected static String getString(Context context, String key, String valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, valueDefault);
    }


}
