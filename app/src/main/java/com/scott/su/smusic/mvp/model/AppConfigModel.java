package com.scott.su.smusic.mvp.model;

import android.content.Context;

/**
 * Created by Administrator on 2016/8/30.
 */
public interface AppConfigModel {
    boolean isNightModeOn(Context context);

    boolean isNeedToRefreshLocalSongDisplay(Context context);

    boolean isNeedToRefreshLocalBillDisplay(Context context);

    boolean isNeedToRefreshLocalAlbumDisplay(Context context);

    void setNeedToRefreshLocalSongDisplay(Context context, boolean isNeedToRefresh);

    void setNeedToRefreshLocalBillDisplay(Context context, boolean isNeedToRefresh);

    void setNeedToRefreshLocalAlbumDisplay(Context context, boolean isNeedToRefresh);

    void setNightMode(Context context, boolean isOn);

    int getPositionOfBillToRefresh(Context context);

    void setPositionOfBillToRefresh(Context context, int positionOfBillToRefresh);

}
