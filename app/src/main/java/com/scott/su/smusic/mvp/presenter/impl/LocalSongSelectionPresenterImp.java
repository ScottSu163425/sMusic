package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.presenter.LocalSongSelectionPresenter;
import com.scott.su.smusic.mvp.view.LocalSongSelectionView;

/**
 * Created by asus on 2016/8/28.
 */
public class LocalSongSelectionPresenterImp implements LocalSongSelectionPresenter {
    private LocalSongSelectionView mSongSelectionView;

    public LocalSongSelectionPresenterImp(LocalSongSelectionView mSongSelectionView) {
        this.mSongSelectionView = mSongSelectionView;
    }

    @Override
    public void onSelectedCountChanged(boolean isEmpty) {
        mSongSelectionView.showOrHideFinishSelectionButtn(!isEmpty);
    }

    @Override
    public void onSelectAllClick() {
        mSongSelectionView.selectAll();
    }

    @Override
    public void onFinishSelectionClick() {
        mSongSelectionView.finishSelection();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mSongSelectionView.initPreData();
        mSongSelectionView.initToolbar();
        mSongSelectionView.initView();
        mSongSelectionView.initData();
        mSongSelectionView.initListener();
    }

    @Override
    public void onViewWillDestroy() {

    }
}
