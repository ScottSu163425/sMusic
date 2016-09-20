package com.scott.su.smusic.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.scott.su.smusic.config.AppConfig;

import org.xutils.x;

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
        super.onCreate();

        x.Ext.init(this); //Initialize for xUtils;
    }
}
