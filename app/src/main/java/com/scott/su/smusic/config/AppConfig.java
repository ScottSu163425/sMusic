package com.scott.su.smusic.config;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by asus on 2016/9/15.
 */
public class AppConfig {
    private static final String PLAY_MODE_REPEAT_ONE = "PLAY_MODE_REPEAT_ONE";
    private static final String PLAY_MODE_REPEAT_ALL = "PLAY_MODE_REPEAT_ALL";
    private static final String PLAY_MODE_SHUFFLE = "PLAY_MODE_SHUFFLE";
    private static final String IS_NIGHT_MODE_ON = "IS_NIGHT_MODE_ON";
    private static final String IS_LANGUAGE_MODE_ON = "IS_LANGUAGE_MODE_ON";


    public static boolean isNightModeOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_NIGHT_MODE_ON, false);
    }

    public static boolean isLanguageModeOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_LANGUAGE_MODE_ON, false);
    }

    public static void setNightMode(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_NIGHT_MODE_ON, isOn)
                .apply();
    }

    public static void setLanguageMode(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_LANGUAGE_MODE_ON, isOn)
                .apply();
    }

    public static boolean isPlayRepeatOne(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PLAY_MODE_REPEAT_ONE, false);
    }

    public static boolean isPlayRepeatAll(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PLAY_MODE_REPEAT_ALL, false);
    }

    public static boolean isPlayShuffle(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PLAY_MODE_SHUFFLE, false);
    }

    public static void setPlayRepeatOne(Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_REPEAT_ONE, true).apply();
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_REPEAT_ALL, false).apply();
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_SHUFFLE, false).apply();
    }

    public static void setPlayRepeatAll(Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_REPEAT_ONE, false).apply();
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_REPEAT_ALL, true).apply();
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_SHUFFLE, false).apply();
    }

    public static void setPlayRepeatShuffle(Context context) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_REPEAT_ONE, false).apply();
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_REPEAT_ALL, false).apply();
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PLAY_MODE_SHUFFLE, true).apply();
    }

}
