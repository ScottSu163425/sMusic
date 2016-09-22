package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalAlbumDetailPresenter;
import com.scott.su.smusic.mvp.view.LocalAlbumDetailView;

/**
 * Created by Administrator on 2016/9/19.
 */
public class LocalAlbumDetailPresenterImpl implements LocalAlbumDetailPresenter {
    private LocalAlbumDetailView mAlbumDetailView;
    private LocalAlbumModel mAlbumModel;

    public LocalAlbumDetailPresenterImpl(LocalAlbumDetailView mAlbumDetailView) {
        this.mAlbumDetailView = mAlbumDetailView;
        this.mAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mAlbumDetailView.initPreData();
        mAlbumDetailView.initToolbar();
        mAlbumDetailView.initView();
        mAlbumDetailView.initData();
        mAlbumDetailView.initListener();

        mAlbumDetailView.loadAlbumCover(mAlbumModel.getAlbumCoverPath(mAlbumDetailView.getViewContext(),
                mAlbumDetailView.getCurrentAlbumEntity().getAlbumId()));
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
    }

    @Override
    public void onBottomSheetShareClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {

    }

    @Override
    public void onAlbumSongItemClick(View view, int position, LocalSongEntity entity) {
        mAlbumDetailView.goToMusicPlay(entity);
    }

    @Override
    public void onAlbumSongItemMoreClick(View view, int position, LocalSongEntity entity) {
        mAlbumDetailView.showAlbumSongBottomSheet(entity);
    }
}
