package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.model.PlayStatisticModel;
import com.scott.su.smusic.mvp.model.impl.PlayStatisticModelImpl;
import com.scott.su.smusic.mvp.presenter.PlayStatisticDisplayPresenter;
import com.scott.su.smusic.mvp.view.PlayStatisticDisplayView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticDisplayPresenterImpl implements PlayStatisticDisplayPresenter {
    private PlayStatisticDisplayView mPlayStatisticDisplayView;
    private PlayStatisticModel mPlayStatisticModel;

    public PlayStatisticDisplayPresenterImpl(PlayStatisticDisplayView mPlayStatisticDisplayView) {
        this.mPlayStatisticDisplayView = mPlayStatisticDisplayView;
        this.mPlayStatisticModel = new PlayStatisticModelImpl();
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
    public void onItemClick(View itemView, PlayStatisticEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {

    }

    @Override
    public void onViewFirstTimeCreated() {
        Observable.create(new Observable.OnSubscribe<List<PlayStatisticEntity>>() {
            @Override
            public void call(Subscriber<? super List<PlayStatisticEntity>> subscriber) {
                subscriber.onNext(mPlayStatisticModel.getTotalPlayStatistic(mPlayStatisticDisplayView.getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mPlayStatisticDisplayView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PlayStatisticEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mPlayStatisticDisplayView.showError();
                    }

                    @Override
                    public void onNext(List<PlayStatisticEntity> playStatisticEntities) {
                        if (playStatisticEntities.isEmpty()) {
                            mPlayStatisticDisplayView.showEmpty();
                        } else {
                            mPlayStatisticDisplayView.setDisplayData(playStatisticEntities);
                            mPlayStatisticDisplayView.display();
                        }
                    }
                });
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
        mPlayStatisticDisplayView = null;
    }
}
