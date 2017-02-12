package com.scott.su.smusic.config;

import android.content.Context;
import android.preference.PreferenceManager;

import com.su.scott.slibrary.manager.ConfigManager;

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
        return ConfigManager.getInstance().getBoolean(context,IS_NIGHT_MODE_ON,false);
    }

    public static boolean isLanguageModeOn(Context context) {
        return ConfigManager.getInstance().getBoolean(context,IS_LANGUAGE_MODE_ON,false);
    }

    public static void setNightMode(Context context, boolean isOn) {
        ConfigManager.getInstance().putBoolean(context,IS_NIGHT_MODE_ON,isOn);
    }

    public static void setLanguageMode(Context context, boolean isOn) {
        ConfigManager.getInstance().putBoolean(context,IS_LANGUAGE_MODE_ON,isOn);
    }

    public static boolean isPlayRepeatOne(Context context) {
        return ConfigManager.getInstance().getBoolean(context,PLAY_MODE_REPEAT_ONE,false);
    }

    public static boolean isPlayRepeatAll(Context context) {
        return ConfigManager.getInstance().getBoolean(context,PLAY_MODE_REPEAT_ALL,false);
    }

    public static boolean isPlayShuffle(Context context) {
        return ConfigManager.getInstance().getBoolean(context,PLAY_MODE_SHUFFLE, false);
    }

    public static void setPlayRepeatOne(Context context) {
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_REPEAT_ONE, true);
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_REPEAT_ALL, false);
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_SHUFFLE, false);
    }

    public static void setPlayRepeatAll(Context context) {
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_REPEAT_ONE, false);
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_REPEAT_ALL, true);
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_SHUFFLE, false);
    }

    public static void setPlayRepeatShuffle(Context context) {
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_REPEAT_ONE, false);
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_REPEAT_ALL, false);
        ConfigManager.getInstance().putBoolean(context,PLAY_MODE_SHUFFLE, true);
    }

}
