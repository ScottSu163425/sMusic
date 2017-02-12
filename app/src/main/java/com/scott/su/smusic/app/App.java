package com.scott.su.smusic.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;

import com.scott.su.smusic.cache.BitmapLruCache;
import com.scott.su.smusic.cache.CoverPathCache;
import com.scott.su.smusic.cache.LocalSongEntityCache;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.db.GreenDaoHelper;

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

        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (AppConfig.isLanguageModeOn(this)) {
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.CHINESE;
        }
        resources.updateConfiguration(config, dm);

        super.onCreate();

        //Init greenDao
        GreenDaoHelper.init(this, "sMusic.db");
    }

    @Override
    public void onTerminate() {
        //Release com.scott.su.smusic.cache.
        BitmapLruCache.getInstance().release();
        CoverPathCache.getInstance().release();
        LocalSongEntityCache.getInstance().release();

        super.onTerminate();
    }
}
