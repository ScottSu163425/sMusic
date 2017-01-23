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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticDisplayPresenterImpl extends BasePresenter<PlayStatisticDisplayContract.PlayStatisticDisplayView>
        implements PlayStatisticDisplayContract.PlayStatisticBaseDisplayPresenter {
    private PlayStatisticModel mPlayStatisticModel;

    public PlayStatisticDisplayPresenterImpl(PlayStatisticDisplayContract.PlayStatisticDisplayView view) {
        super(view);
        this.mPlayStatisticModel = new PlayStatisticModelImpl();
    }

    @Override
    public void onSwipeRefresh() {
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
        Observable.create(new ObservableOnSubscribe<List<PlayStatisticEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PlayStatisticEntity>> e) throws Exception {
                e.onNext(mPlayStatisticModel.getTotalPlayStatistic(getView().getViewContext()));
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
                .subscribe(new Consumer<List<PlayStatisticEntity>>() {
                    @Override
                    public void accept(List<PlayStatisticEntity> playStatisticEntityList) throws Exception {
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


}
