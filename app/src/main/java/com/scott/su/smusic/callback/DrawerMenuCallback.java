package com.scott.su.smusic.callback;

import android.view.View;

/**
 * Created by Administrator on 2016/10/19.
 */

public interface DrawerMenuCallback {
    void onDrawerMenuNightModeOn();

    void onDrawerMenuNightModeOff();

    void onDrawerMenuLanguageModeOn();

    void onDrawerMenuLanguageModeOff();

    void onDrawerMenuUpdateClick(View v);

    void onDrawerMenuAboutClick(View v);

    void onDrawerMenuTimerCancelClick();

    void onDrawerMenuTimerMinutesClick(long millisOfmin);

    void onDrawerMenuStaticticClick(View v);

}
