package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongSelectionDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalSongSelectionDisplayView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/8/27.
 */
public class LocalSongSelectionDisplayPresenterImpl implements LocalSongSelectionDisplayPresenter {
    private LocalSongSelectionDisplayView mDisplayView;
    private LocalSongModel mLocalSongModel;

    public LocalSongSelectionDisplayPresenterImpl(LocalSongSelectionDisplayView mDisplayView) {
        this.mDisplayView = mDisplayView;
        mLocalSongModel = new LocalSongModelImpl();
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
        mDisplayView.showLoading();
        getAndDisplayLocalSongs();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    private void getAndDisplayLocalSongs() {
        mDisplayView.showLoading();

        Observable.create(new Observable.OnSubscribe<List<LocalSongEntity>>() {
            @Override
            public void call(Subscriber<? super List<LocalSongEntity>> subscriber) {
                subscriber.onNext(mLocalSongModel.getLocalSongs(mDisplayView.getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LocalSongEntity>>() {
                    @Override
                    public void call(List<LocalSongEntity> songEntities) {

                        List<LocalSongEntity> result = songEntities;

                        if (result == null) {
                            result = new ArrayList<LocalSongEntity>();
                        }

                        mDisplayView.setDisplayData(result);

                        if (result.size() == 0) {
                            mDisplayView.showEmpty();
                        } else {
                            mDisplayView.display();
                        }
                    }
                });
    }

}
