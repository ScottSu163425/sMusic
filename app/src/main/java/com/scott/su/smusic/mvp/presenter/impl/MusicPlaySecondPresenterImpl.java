package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.presenter.MusicPlaySecondPresenter;
import com.scott.su.smusic.mvp.view.MusicPlaySecondView;

/**
 * Created by asus on 2016/11/20.
 */

public class MusicPlaySecondPresenterImpl implements MusicPlaySecondPresenter {
    private MusicPlaySecondView mMusicPlaySecondView;


    public MusicPlaySecondPresenterImpl(MusicPlaySecondView mMusicPlaySecondView) {
        this.mMusicPlaySecondView = mMusicPlaySecondView;
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMusicPlaySecondView.initPreData();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onMusicPlayServiceConnected() {
        mMusicPlaySecondView.initView();
        mMusicPlaySecondView.initData();
        mMusicPlaySecondView.initListener();
    }

    @Override
    public void onMusicPlayServiceDisconnected() {

    }
}
