package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IBaseDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface AlbumSongDisplayContract {

    interface AlbumSongDisplayView extends IBaseDisplayView<LocalSongEntity> {
        LocalAlbumEntity getSongAlbumEntity();

        void setLoading();
    }

    interface AlbumSongBaseDisplayPresenter extends IBaseDisplayPresenter<AlbumSongDisplayView, LocalSongEntity> {

    }

}
