package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.contract.PlayStatisticContract;
import com.scott.su.smusic.mvp.model.PlayStatisticModel;
import com.scott.su.smusic.mvp.model.impl.PlayStatisticModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticPresenterImpl extends BasePresenter<PlayStatisticContract.PlayStatisticView>
        implements PlayStatisticContract.PlayStatisticPresenter {
    private PlayStatisticModel mPlayStatisticModel;


    public PlayStatisticPresenterImpl(PlayStatisticContract.PlayStatisticView view) {
        super(view);
        this.mPlayStatisticModel = new PlayStatisticModelImpl();
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
        getView().refreshPlayStatisticList();
    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onPlayStatisticItemClick(int position, PlayStatisticEntity entity, ArrayList<PlayStatisticEntity> statisticEntityList, View sharedElement, String transitionName) {
        if (position < 3) {
            //Top 3 with cover.
            getView().goToMusicPlayWithCover(entity.toLocalSongEntity(),
                    (ArrayList<LocalSongEntity>) mPlayStatisticModel.getLocalSongsByPlayStatistic(statisticEntityList),
                    sharedElement, transitionName);
        } else {
            getView().goToMusicPlay(entity.toLocalSongEntity(),
                    (ArrayList<LocalSongEntity>) mPlayStatisticModel.getLocalSongsByPlayStatistic(statisticEntityList));
        }

    }


}
