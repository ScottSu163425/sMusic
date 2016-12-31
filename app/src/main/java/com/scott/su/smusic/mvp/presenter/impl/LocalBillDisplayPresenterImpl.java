package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.mvp.contract.LocalBillDisplayContract;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

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
public class LocalBillDisplayPresenterImpl extends BasePresenter<LocalBillDisplayContract.LocalBillDisplayView>
        implements LocalBillDisplayContract.LocalBillBaseDisplayPresenter {
    private LocalBillModelImpl mBillModel;

    
    public LocalBillDisplayPresenterImpl(LocalBillDisplayContract.LocalBillDisplayView localBillDisplayView) {
        super(localBillDisplayView);
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
        getView().handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().showLoading();
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
            getView().showLoading();
        }

        Observable.create(new Observable.OnSubscribe<List<LocalBillEntity>>() {
            @Override
            public void call(Subscriber<? super List<LocalBillEntity>> subscriber) {
                subscriber.onNext(mBillModel.getBills(getView().getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LocalBillEntity>>() {
                    @Override
                    public void call(List<LocalBillEntity> localBillEntities) {
                        if (!isViewAttaching()) {
                            return;
                        }

                        List<LocalBillEntity> result = localBillEntities;

                        if (result == null) {
                            result = new ArrayList<LocalBillEntity>();
                        }

                        getView().setDisplayData(result);

                        if (result.size() == 0) {
                            getView().showEmpty();
                        } else {
                            getView().display();
                        }
                    }
                });
    }


}
