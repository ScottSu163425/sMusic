package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.PlayListSecondDisplayContract;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2016/12/3.
 */

public class PlayListSecondDisplayPresenterImpl extends BasePresenter<PlayListSecondDisplayContract.PlayListSecondDisplayView>
        implements PlayListSecondDisplayContract.PlayListSecondBaseDisplayPresenter {


    public PlayListSecondDisplayPresenterImpl(PlayListSecondDisplayContract.PlayListSecondDisplayView view) {
        super(view);
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

    }


}
