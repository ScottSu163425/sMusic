package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.model.PlayStatisticModel;
import com.scott.su.smusic.mvp.model.impl.PlayStatisticModelImpl;
import com.scott.su.smusic.mvp.presenter.PlayStatisticPresenter;
import com.scott.su.smusic.mvp.view.PlayStatisticView;

import java.util.ArrayList;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticPresenterImpl implements PlayStatisticPresenter {
    private PlayStatisticView mPlayStatisticView;
    private PlayStatisticModel mPlayStatisticModel;


    public PlayStatisticPresenterImpl(PlayStatisticView mPlayStatisticView) {
        this.mPlayStatisticView = mPlayStatisticView;
        this.mPlayStatisticModel = new PlayStatisticModelImpl();
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

    @Override
    public void onPlayStatisticItemClick(int position, PlayStatisticEntity entity, ArrayList<PlayStatisticEntity> statisticEntityList, View sharedElement, String transitionName) {
        if (position < 3) {
            //Top 3 with cover.
            mPlayStatisticView.goToMusicPlayWithCover(entity.toLocalSongEntity(),
                    (ArrayList<LocalSongEntity>) mPlayStatisticModel.getLocalSongsByPlayStatistic(statisticEntityList),
                    sharedElement, transitionName);
        } else {
            mPlayStatisticView.goToMusicPlay(entity.toLocalSongEntity(),
                    (ArrayList<LocalSongEntity>) mPlayStatisticModel.getLocalSongsByPlayStatistic(statisticEntityList));
        }

    }


}
