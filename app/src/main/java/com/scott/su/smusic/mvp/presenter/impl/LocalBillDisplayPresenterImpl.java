package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalBillDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalBillDisplayView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by asus on 2016/8/19.
 */
public class LocalBillDisplayPresenterImpl implements LocalBillDisplayPresenter {
    private LocalBillDisplayView mBillDisplayView;
    private LocalBillModelImpl mBillModel;

    public LocalBillDisplayPresenterImpl(LocalBillDisplayView localBillDisplayView) {
        this.mBillDisplayView = localBillDisplayView;
        this.mBillModel = new LocalBillModelImpl();
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
    public void onItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mBillDisplayView.handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        mBillDisplayView.showLoading();
        getAndDisplayLocalSongBills(false);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    private void getAndDisplayLocalSongBills(boolean isRefresh) {
        if (!isRefresh) {
            mBillDisplayView.showLoading();
        }

        Observable.create(new Observable.OnSubscribe<List<LocalBillEntity>>() {
            @Override
            public void call(Subscriber<? super List<LocalBillEntity>> subscriber) {
                subscriber.onNext(mBillModel.getBills(mBillDisplayView.getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LocalBillEntity>>() {
                    @Override
                    public void call(List<LocalBillEntity> localBillEntities) {

                        List<LocalBillEntity> result = localBillEntities;

                        if (result == null) {
                            result = new ArrayList<LocalBillEntity>();
                        }

                        mBillDisplayView.setDisplayData(result);

                        if (result.size() == 0) {
                            mBillDisplayView.showEmpty();
                        } else {
                            mBillDisplayView.display();
                        }
                    }
                });
    }


}
