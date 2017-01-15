package com.scott.su.smusic.callback;

import android.view.View;

/**
 * Created by Administrator on 2016/10/19.
 */

public interface DrawerMenuCallback {
    void onDrawerUserHeadClick(View sharedElement, String transitionName);

    void onDrawerMenuUserCenterClick(View v, View sharedElement, String transitionName);

    void onDrawerMenuNightModeOn();

    void onDrawerMenuNightModeOff();

    void onDrawerMenuLanguageModeOn();

    void onDrawerMenuLanguageModeOff();

    void onDrawerMenuSettingsClick(View v);

    void onDrawerMenuTimerCancelClick();

    void onDrawerMenuTimerMinutesClick(long millisOfmin);

    void onDrawerMenuStatisticClick(View v);

}
