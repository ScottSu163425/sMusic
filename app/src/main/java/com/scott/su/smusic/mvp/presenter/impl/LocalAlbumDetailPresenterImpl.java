package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.presenter.LocalAlbumDetailPresenter;
import com.scott.su.smusic.mvp.view.LocalAlbumDetailView;

/**
 * Created by Administrator on 2016/9/19.
 */
public class LocalAlbumDetailPresenterImpl implements LocalAlbumDetailPresenter {
    private LocalAlbumDetailView mAlbumDetailView;

    public LocalAlbumDetailPresenterImpl(LocalAlbumDetailView mAlbumDetailView) {
        this.mAlbumDetailView = mAlbumDetailView;
    }

    @Override
    public void onViewFirstTimeCreated() {
        mAlbumDetailView.initPreData();
        mAlbumDetailView.initToolbar();
        mAlbumDetailView.initView();
        mAlbumDetailView.initData();
        mAlbumDetailView.initListener();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }
}
