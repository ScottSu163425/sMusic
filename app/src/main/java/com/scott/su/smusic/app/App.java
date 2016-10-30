package com.scott.su.smusic.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;

import com.scott.su.smusic.config.AppConfig;

import cache.BitmapLruCache;
import cache.CoverPathCache;
import cache.LocalSongEntityCache;

import org.xutils.x;

import java.util.Locale;

/**
 * Created by asus on 2016/8/21.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        //Init the day-night theme and language configure.
        if (AppConfig.isNightModeOn(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if (AppConfig.isLanguageModeOn(this)) {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            // 应用用户选择语言
            config.locale = Locale.ENGLISH;
            resources.updateConfiguration(config, dm);
        } else {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            // 应用用户选择语言
            config.locale = Locale.CHINESE;
            resources.updateConfiguration(config, dm);
        }

        super.onCreate();

        //Initialize for xUtils;
        x.Ext.init(this);
    }

    @Override
    public void onTerminate() {
        //Release cache.
        BitmapLruCache.getInstance().release();
        CoverPathCache.getInstance().release();
        LocalSongEntityCache.getInstance().release();

        super.onTerminate();
    }
}
