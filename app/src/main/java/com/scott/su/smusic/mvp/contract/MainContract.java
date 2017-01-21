package com.scott.su.smusic.mvp.contract;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.callback.DrawerMenuCallback;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.ILocalSongBottomSheetPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBottomSheetView;
import com.scott.su.smusic.mvp.view.MusicPlayServiceView;
import com.scott.su.smusic.mvp.view.ShutDownTimerServiceView;
import com.su.scott.slibrary.callback.SimpleCallback;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/12/14.
 */

public interface MainContract {

    interface MainView extends IBaseView, LocalSongBottomSheetView, MusicPlayServiceView, ShutDownTimerServiceView {

        boolean isInitDataComplete();

        void bindMusicPlayService();

        void bindShutDownTimerService();

        void showLocalSongBottomSheet(LocalSongEntity songEntity);

        boolean isCurrentTabSong();

        boolean isCurrentTabBill();

        boolean isCurrentTabAlbum();

        ArrayList<LocalSongEntity> getDisplaySongs();

        void scrollSongPositionTo(int position, SimpleCallback scrollCompleteCallback);

        void showFab(boolean needAnim);

        void hideFab(boolean needAnim);

        void updateSongDisplay();

        void updateBillDisplay();

        void updateAlbumDisplay();

        void playSongInPosition(int position, boolean needOpenMusicPlay);

        void playRandomSong();

        void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName);

        void goToAlbumDetail(LocalAlbumEntity entity);

        void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName);

        void goToBillDetail(LocalBillEntity entity);

        void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName);

        void turnOnNightMode();

        void turnOffNightMode();

        void turnOnLanguageMode();

        void turnOffLanguageMode();

        boolean isFabPlayRandom();

        void setFabPlayRandom();

        void setFabPlayCurrent();

        void goToPlayStatistic();

        void goToUserCenter(View sharedElement, String transitionName);

        void goToSettings();

        void goToLocalBillCreation();
    }

    interface MainPresenter extends IPresenter<MainView>, ILocalSongBottomSheetPresenter, DrawerMenuCallback {

        void onInitDataComplete();

        void onLocalSongItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

        void onLocalSongItemMoreClick(LocalSongEntity songEntity);

        void onFabClick();

        void onFabLongClick();

        void onBillItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

        void onAlbumItemClick(View itemView, LocalAlbumEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

        void onSelectedLocalSongsResult(LocalBillEntity billToAddSong, List<LocalSongEntity> songsToAdd);

    }

}
