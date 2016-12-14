package com.scott.su.smusic.mvp.contract;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongBottomSheetPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBottomSheetView;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalAlbumDetailContract {
    interface LocalAlbumDetailPresenter extends IPresenter<LocalAlbumDetailView>,LocalSongBottomSheetPresenter {
        void onAlbumSongItemClick(View view, int position, LocalSongEntity entity);

        void onAlbumSongItemMoreClick(View view, int position, LocalSongEntity entity);

        void onPlayFabClick();
    }

    interface LocalAlbumDetailView extends IBaseView,LocalSongBottomSheetView {
        LocalAlbumEntity getCurrentAlbumEntity();

        void loadAlbumCover(String path);

        void goToMusicPlay(LocalSongEntity songEntity);

        void goToMusicPlayWithCover(LocalSongEntity songEntity);

        void showAlbumSongBottomSheet(LocalSongEntity songEntity);
    }

}
