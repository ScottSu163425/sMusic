package com.scott.su.smusic.mvp.presenter;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;

/**
 * Created by Administrator on 2016/9/27.
 */

public interface SearchPresenter extends IPresenter,LocalSongBottomSheetPresenter{

    void onSearchTextChanged(String keyword);

    void onLocalSongClick(LocalSongEntity entity, View sharedElement, String transitionName);

    void onLocalSongMoreClick(LocalSongEntity entity);

    void onLocalBillClick(LocalBillEntity entity, View sharedElement, String transitionName);

    void onLocalAlbumClick(LocalAlbumEntity entity, View sharedElement, String transitionName);
}
