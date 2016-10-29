package com.scott.su.smusic.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;

import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.util.BitmapLruCache;
import com.scott.su.smusic.util.CoverPathCache;

import org.xutils.x;

import java.util.Locale;

/**
 * Created by asus on 2016/8/21.
 */

public class App extends Application {

    @Override
    public void onCreate() {
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

        x.Ext.init(this); //Initialize for xUtils;
    }

    @Override
    public void onTerminate() {
        //Release cache.
        BitmapLruCache.getInstance().release();
        CoverPathCache.getInstance().release();

        super.onTerminate();
    }
}
