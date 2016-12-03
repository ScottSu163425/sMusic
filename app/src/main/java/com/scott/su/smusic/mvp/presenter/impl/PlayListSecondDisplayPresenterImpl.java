package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.PlayListSecondDisplayPresenter;
import com.scott.su.smusic.mvp.view.PlayListSecondDisplayView;
import com.scott.su.smusic.mvp.view.PlayStatisticDisplayView;

/**
 * Created by asus on 2016/12/3.
 */

public class PlayListSecondDisplayPresenterImpl implements PlayListSecondDisplayPresenter {
    private PlayListSecondDisplayView mDisplayView;


    public PlayListSecondDisplayPresenterImpl(PlayListSecondDisplayView mDisplayView) {
        this.mDisplayView = mDisplayView;
    }

    @Override
    public void onSwipRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onEmptyClick() {

    }

    @Override
    public void onErrorClick() {

    }

    @Override
    public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {

    }

    @Override
    public void onViewFirstTimeCreated() {
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
        mDisplayView = null;
    }


}
