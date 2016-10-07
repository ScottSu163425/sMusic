package com.scott.su.smusic.mvp.view;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.callback.SimpleCallback;
import com.su.scott.slibrary.view.BaseView;

import java.util.ArrayList;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainView extends BaseView, LocalSongBottomSheetView,MusicPlayServiceView {

    boolean isDataInitFinish();

    void showLocalSongBottomSheet(LocalSongEntity songEntity);

    boolean isCurrentTabSong();

    boolean isCurrentTabBill();

    boolean isCurrentTabAlbum();

    ArrayList<LocalSongEntity> getDisplaySongs();

    void scrollSongPositionTo(int position, SimpleCallback scrollCompleteCallback);

    void updateSongDisplay();

    void updateBillDisplay();

    void updateAlbumDisplay();

    void playSongInPosition(int position);

    void playRandomSong();

    void showCreateBillDialog();

    void dismissCreateBillDialog();

    void showCreateBillSuccessfully(LocalBillEntity billEntity);

    void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName);

    void goToAlbumDetail(LocalAlbumEntity entity);

    void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName);

    void goToBillDetail(LocalBillEntity entity);

    void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName);

    void goToMusicWithSharedElementFromFAB();

    void turnOnNightMode();

    void turnOffNightMode();

    void turnOnLanguageMode();

    void turnOffLanguageMode();

}
