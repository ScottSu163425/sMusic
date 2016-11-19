package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.presenter.PlayStatisticPresenter;
import com.scott.su.smusic.mvp.view.PlayStatisticView;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticPresenterImpl implements PlayStatisticPresenter {
    private PlayStatisticView mPlayStatisticView;


    public PlayStatisticPresenterImpl(PlayStatisticView mPlayStatisticView) {
        this.mPlayStatisticView = mPlayStatisticView;
    }

    @Override
    public void onViewFirstTimeCreated() {
        mPlayStatisticView.initPreData();
        mPlayStatisticView.initToolbar();
        mPlayStatisticView.initView();
        mPlayStatisticView.initData();
        mPlayStatisticView.initListener();

    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
        mPlayStatisticView = null;
    }

}
