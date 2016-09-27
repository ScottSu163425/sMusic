package com.scott.su.smusic.mvp.presenter;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/9/27.
 */

public interface SearchPresenter extends BasePresenter{
    void onLocalSongClick(LocalSongEntity entity, View sharedElement, String transitionName);

    void onLocalSongMoreClick(LocalSongEntity entity);

    void onLocalBillClick(LocalBillEntity entity, View sharedElement, String transitionName);

    void onLocalAlbumClick(LocalAlbumEntity entity, View sharedElement, String transitionName);
}
