package com.scott.su.smusic.app;

import android.app.Application;

import org.xutils.x;

/**
 * Created by asus on 2016/8/21.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this); //Initialize for xUtils;
    }
}
