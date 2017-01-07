package com.scott.su.smusic.db;

import android.content.Context;
import android.os.Handler;

import com.scott.su.smusic.entity.DaoMaster;
import com.scott.su.smusic.entity.DaoSession;

/**
 * Created by asus on 2017/1/7.
 */

public class GreenDaoHelper {
    private static DaoMaster.DevOpenHelper devOpenHelper;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;


    public static void init(Context context, String dbName) {
        if (devOpenHelper == null) {
            devOpenHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            daoSession = daoMaster.newSession();
        }
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            throw new IllegalStateException("You must call init() first for greenDAO.");
        }
        return daoSession;
    }

}
