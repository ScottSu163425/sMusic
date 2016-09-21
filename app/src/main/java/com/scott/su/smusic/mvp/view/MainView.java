package com.scott.su.smusic.mvp.view;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainView extends BaseView {

    boolean isDataInitFinish();

    void showLocalSongBottomSheet(LocalSongEntity songEntity);

    void showBillSelectionDialog(LocalSongEntity songToBeAdd);

    void updateSongDisplay();

    void updateBillDisplay();

    void updateAlbumDisplay();

    void showCreateBillDialog();

    void dismissCreateBillDialog();

    void showCreateBillSuccessfully(LocalBillEntity billEntity);

    void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName);

    void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName);

    void goToBillDetail(LocalBillEntity entity);

    void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName);

    void turnOnNightMode();

    void turnOffNightMode();

    void turnOnLanguageMode();

    void turnOffLanguageMode();

}
