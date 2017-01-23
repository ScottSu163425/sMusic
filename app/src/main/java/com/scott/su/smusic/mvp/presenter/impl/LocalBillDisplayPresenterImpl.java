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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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
    public void onSwipeRefresh() {
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

    private void getAndDisplayLocalSongBills(final boolean isRefresh) {
        Observable.create(new ObservableOnSubscribe<List<LocalBillEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocalBillEntity>> e) throws Exception {
                e.onNext(mBillModel.getBills(getView().getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!isRefresh) {
                            getView().showLoading();
                        }
                    }
                })
                .subscribe(new Consumer<List<LocalBillEntity>>() {
                    @Override
                    public void accept(List<LocalBillEntity> localBillEntities) throws Exception {
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
