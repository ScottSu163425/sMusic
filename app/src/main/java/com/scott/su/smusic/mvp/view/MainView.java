package com.scott.su.smusic.mvp.view;

import android.view.View;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainView extends BaseView {

    boolean isDataInitFinish();

    void updateSongDisplay();

    void updateBillDisplay();

    void updateAlbumDisplay();

    void showCreateBillDialog();

    void dismissCreateBillDialog();

    void showCreateBillUnsuccessfully(String msg);

    void showCreateBillSuccessfully(LocalBillEntity billEntity);

    void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName);

    void goToBillDetail(LocalBillEntity entity);

}
