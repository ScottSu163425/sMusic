package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.contract.MusicPlaySecondContract;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2016/11/20.
 */

public class MusicPlaySecondPresenterImpl extends BasePresenter<MusicPlaySecondContract.MusicPlaySecondView>
        implements MusicPlaySecondContract.MusicPlaySecondPresenter {


    public MusicPlaySecondPresenterImpl(MusicPlaySecondContract.MusicPlaySecondView view) {
    super(view);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onMusicPlayServiceConnected() {
        getView().initView();
        getView().initData();
        getView().initListener();
    }

    @Override
    public void onMusicPlayServiceDisconnected() {

    }
}
