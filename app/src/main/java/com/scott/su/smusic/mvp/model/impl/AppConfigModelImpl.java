package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;
import android.preference.PreferenceManager;

import com.scott.su.smusic.mvp.model.AppConfigModel;

/**
 * Created by Administrator on 2016/8/30.
 */
public class AppConfigModelImpl implements AppConfigModel {
    private static final String NEED_TO_REFRESH_SONG = "NEED_REFRESH_SONG";
    private static final String NEED_TO_REFRESH_BILL = "NEED_REFRESH_BILL";
    private static final String NEED_TO_REFRESH_ALBUM = "NEED_REFRESH_ALBUM";
    private static final String POSITION_OF_BILL_TO_REFRESH_ = "POSITION_OF_BILL_TO_REFRESH_";

    @Override
    public boolean isNeedToRefreshLocalSongDisplay(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NEED_TO_REFRESH_SONG, false);
    }

    @Override
    public boolean isNeedToRefreshLocalBillDisplay(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NEED_TO_REFRESH_BILL, false);
    }

    @Override
    public boolean isNeedToRefreshLocalAlbumDisplay(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NEED_TO_REFRESH_ALBUM, false);
    }

    @Override
    public void setNeedToRefreshLocalSongDisplay(Context context, boolean isNeedToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(NEED_TO_REFRESH_SONG, isNeedToRefresh)
                .apply();
    }

    @Override
    public void setNeedToRefreshLocalBillDisplay(Context context, boolean isNeedToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(NEED_TO_REFRESH_BILL, isNeedToRefresh)
                .apply();
    }

    @Override
    public void setNeedToRefreshLocalAlbumDisplay(Context context, boolean isNeedToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(NEED_TO_REFRESH_ALBUM, isNeedToRefresh)
                .apply();
    }

    @Override
    public int getPositionOfBillToRefresh(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(POSITION_OF_BILL_TO_REFRESH_, 0);
    }

    @Override
    public void setPositionOfBillToRefresh(Context context, int positionOfBillToRefresh) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(POSITION_OF_BILL_TO_REFRESH_, positionOfBillToRefresh)
                .apply();
    }


}
