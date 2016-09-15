package com.scott.su.smusic.config;

import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by asus on 2016/9/15.
 */
public class Configure {
    private static final String PLAY_MODE_REPEAT_ONE = "PLAY_MODE_REPEAT_ONE";
    private static final String PLAY_MODE_REPEAT_ALL = "PLAY_MODE_REPEAT_ALL";
    private static final String PLAY_MODE_SHUFFLE = "PLAY_MODE_SHUFFLE";

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

    public static void setPlayShuffle(Context context) {
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
