package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.cache.LocalSongEntityCache;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.LocalSongDisplayContract;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
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
public class LocalSongDisplayPresenterImpl extends BasePresenter<LocalSongDisplayContract.LocalSongDisplayView>
        implements LocalSongDisplayContract.LocalSongBaseDisplayPresenter {
    private LocalSongModelImpl mSongModel;

    public LocalSongDisplayPresenterImpl(LocalSongDisplayContract.LocalSongDisplayView view) {
        super(view);
        this.mSongModel = new LocalSongModelImpl();
    }

    @Override
    public void onSwipRefresh() {
        LocalSongEntityCache.getInstance().release();
        getAndDisplayLocalSongs(true);
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
        getView().handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getAndDisplayLocalSongs(false);
    }

    private void getAndDisplayLocalSongs(boolean isRefresh) {
        if (!isRefresh) {
            getView().showLoading();
        }

        Observable.create(new Observable.OnSubscribe<List<LocalSongEntity>>() {
            @Override
            public void call(Subscriber<? super List<LocalSongEntity>> subscriber) {
                subscriber.onNext(mSongModel.getLocalSongs(getView().getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LocalSongEntity>>() {
                    @Override
                    public void call(List<LocalSongEntity> songEntities) {
                        if (!isViewAttaching()) {
                            return;
                        }

                        List<LocalSongEntity> result = songEntities;

                        if (result == null) {
                            result = new ArrayList<LocalSongEntity>();
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
