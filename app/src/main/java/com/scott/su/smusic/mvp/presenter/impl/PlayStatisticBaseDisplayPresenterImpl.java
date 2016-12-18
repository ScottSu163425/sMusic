package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.contract.PlayStatisticDisplayContract;
import com.scott.su.smusic.mvp.model.PlayStatisticModel;
import com.scott.su.smusic.mvp.model.impl.PlayStatisticModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticBaseDisplayPresenterImpl extends BasePresenter<PlayStatisticDisplayContract.PlayStatisticDisplayView>
        implements PlayStatisticDisplayContract.PlayStatisticBaseDisplayPresenter {
    private PlayStatisticModel mPlayStatisticModel;

    public PlayStatisticBaseDisplayPresenterImpl(PlayStatisticDisplayContract.PlayStatisticDisplayView view) {
        super(view);
        this.mPlayStatisticModel = new PlayStatisticModelImpl();
    }

    @Override
    public void onSwipRefresh() {
        getAndDisplayData(true);
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
        getView().handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getAndDisplayData(false);
    }

    private void getAndDisplayData(final boolean isRefresh) {
        Observable.create(new Observable.OnSubscribe<List<PlayStatisticEntity>>() {
            @Override
            public void call(Subscriber<? super List<PlayStatisticEntity>> subscriber) {
                subscriber.onNext(mPlayStatisticModel.getTotalPlayStatistic(getView().getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isRefresh) {
                            getView().showLoading();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PlayStatisticEntity>>() {
                    @Override
                    public void call(List<PlayStatisticEntity> playStatisticEntityList) {
                        if (!isViewAttaching()) {
                            return;
                        }

                        if (playStatisticEntityList.isEmpty()) {
                            getView().showEmpty();
                        } else {
                            getView().setDisplayData(playStatisticEntityList);
                            getView().display();
                        }
                    }
                });
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

}
