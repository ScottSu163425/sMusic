package com.scott.su.smusic.mvp.presenter.impl;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalAlbumDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalAlbumDisplayView;
import com.su.scott.slibrary.manager.AsyncTaskHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by asus on 2016/8/19.
 */
public class LocalAlbumDisplayPresenterImpl implements LocalAlbumDisplayPresenter {
    private LocalAlbumDisplayView mLocalAlbumDisplayView;
    private LocalAlbumModel mLocalAlbumModel;

    public LocalAlbumDisplayPresenterImpl(LocalAlbumDisplayView localAlbumDisplayView) {
        this.mLocalAlbumDisplayView = localAlbumDisplayView;
        this.mLocalAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onSwipRefresh() {
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
    public void onItemClick(View itemView, LocalAlbumEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mLocalAlbumDisplayView.handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getAndDisplayLocalSongs(false);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    private void getAndDisplayLocalSongs(boolean isRefresh) {
        if (!isRefresh) {
            mLocalAlbumDisplayView.showLoading();
        }

        Observable.create(new Observable.OnSubscribe<List<LocalAlbumEntity>>() {
            @Override
            public void call(Subscriber<? super List<LocalAlbumEntity>> subscriber) {
                subscriber.onNext(mLocalAlbumModel.getLocalAlbums(mLocalAlbumDisplayView.getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LocalAlbumEntity>>() {
                    @Override
                    public void call(List<LocalAlbumEntity> localAlbumEntities) {
                        List<LocalAlbumEntity> result = localAlbumEntities;

                        if (result == null) {
                            result = new ArrayList<LocalAlbumEntity>();
                        }

                        mLocalAlbumDisplayView.setDisplayData(result);

                        if (result.size() == 0) {
                            mLocalAlbumDisplayView.showEmpty();
                        } else {
                            mLocalAlbumDisplayView.display();
                        }
                    }
                });
    }


}
