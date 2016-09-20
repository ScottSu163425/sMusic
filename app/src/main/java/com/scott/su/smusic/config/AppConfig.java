package com.scott.su.smusic.config;

import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by asus on 2016/9/15.
 */
public class AppConfig {
    private static final String PLAY_MODE_REPEAT_ONE = "PLAY_MODE_REPEAT_ONE";
    private static final String PLAY_MODE_REPEAT_ALL = "PLAY_MODE_REPEAT_ALL";
    private static final String PLAY_MODE_SHUFFLE = "PLAY_MODE_SHUFFLE";
    private static final String IS_NIGHT_MODE_ON = "IS_NIGHT_MODE_ON";
    private static final String NEED_TO_REFRESH_SONG = "NEED_REFRESH_SONG";
    private static final String NEED_TO_REFRESH_BILL = "NEED_REFRESH_BILL";
    private static final String NEED_TO_REFRESH_ALBUM = "NEED_REFRESH_ALBUM";
    private static final String POSITION_OF_BILL_TO_REFRESH_ = "POSITION_OF_BILL_TO_REFRESH_";

    public static boolean isNightModeOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_NIGHT_MODE_ON, false);
    }

    public static boolean isNeedToRefreshLocalSongDisplay(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NEED_TO_REFRESH_SONG, false);
    }

    public static boolean isNeedToRefreshLocalBillDisplay(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NEED_TO_REFRESH_BILL, false);
    }

    public static boolean isNeedToRefreshLocalAlbumDisplay(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NEED_TO_REFRESH_ALBUM, false);
    }

    public static void setNeedToRefreshLocalSongDisplay(Context context, boolean isNeedToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(NEED_TO_REFRESH_SONG, isNeedToRefresh)
                .apply();
    }

    public static void setNeedToRefreshLocalBillDisplay(Context context, boolean isNeedToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(NEED_TO_REFRESH_BILL, isNeedToRefresh)
                .apply();
    }

    public static void setNeedToRefreshLocalAlbumDisplay(Context context, boolean isNeedToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(NEED_TO_REFRESH_ALBUM, isNeedToRefresh)
                .apply();
    }

    public static void setNightMode(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_NIGHT_MODE_ON, isOn)
                .apply();
    }

    public static int getPositionOfBillToRefresh(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(POSITION_OF_BILL_TO_REFRESH_, 0);
    }

    public static void setPositionOfBillToRefresh(Context context, int positionOfBillToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(POSITION_OF_BILL_TO_REFRESH_, positionOfBillToRefresh)
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
