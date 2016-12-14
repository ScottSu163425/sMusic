package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.contract.LocalSongSelectionContract;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2016/8/28.
 */
public class LocalSongSelectionPresenterImp extends BasePresenter<LocalSongSelectionContract.LocalSongSelectionView>
        implements LocalSongSelectionContract.LocalSongSelectionPresenter {

    public LocalSongSelectionPresenterImp(LocalSongSelectionContract.LocalSongSelectionView view) {
        super(view);
    }

    @Override
    public void onSelectedCountChanged(boolean isEmpty) {
        getView().showOrHideFinishSelectionButton(!isEmpty);
    }

    @Override
    public void onSelectAllClick() {
        getView().selectAll();
    }

    @Override
    public void onFinishSelectionClick() {
        getView().finishSelection();
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
        getView().initToolbar();
        getView().initView();
        getView().initData();
        getView().initListener();
    }

    @Override
    public void onViewResume() {

    }


}
