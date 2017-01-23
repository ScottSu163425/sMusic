package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.mvp.contract.LocalAlbumDisplayContract;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
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
public class LocalAlbumDisplayPresenterImpl extends BasePresenter<LocalAlbumDisplayContract.LocalAlbumDisplayView>
        implements LocalAlbumDisplayContract.LocalAlbumBaseDisplayPresenter {
    private LocalAlbumModel mLocalAlbumModel;

    public LocalAlbumDisplayPresenterImpl(LocalAlbumDisplayContract.LocalAlbumDisplayView localAlbumDisplayView) {
       super(localAlbumDisplayView);
        this.mLocalAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onSwipeRefresh() {
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

        Observable.create(new Observable.OnSubscribe<List<LocalAlbumEntity>>() {
            @Override
            public void call(Subscriber<? super List<LocalAlbumEntity>> subscriber) {
                subscriber.onNext(mLocalAlbumModel.getLocalAlbums(getView().getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LocalAlbumEntity>>() {
                    @Override
                    public void call(List<LocalAlbumEntity> localAlbumEntities) {
                        if (!isViewAttaching()) {
                            return;
                        }

                        List<LocalAlbumEntity> result = localAlbumEntities;

                        if (result == null) {
                            result = new ArrayList<LocalAlbumEntity>();
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
