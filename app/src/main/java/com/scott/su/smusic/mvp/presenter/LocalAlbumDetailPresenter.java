package com.scott.su.smusic.mvp.presenter;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface LocalAlbumDetailPresenter extends IPresenter,LocalSongBottomSheetPresenter {
    void onAlbumSongItemClick(View view, int position, LocalSongEntity entity);

    void onAlbumSongItemMoreClick(View view, int position, LocalSongEntity entity);

    void onPlayFabClick();
}
