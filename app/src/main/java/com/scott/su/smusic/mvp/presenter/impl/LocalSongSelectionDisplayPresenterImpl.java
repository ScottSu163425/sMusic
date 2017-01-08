package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.LocalSongSelectionDisplayContract;
import com.scott.su.smusic.mvp.model.LocalSongModel;
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
 * Created by asus on 2016/8/27.
 */
public class LocalSongSelectionDisplayPresenterImpl extends BasePresenter<LocalSongSelectionDisplayContract.LocalSongSelectionDisplayView>
        implements LocalSongSelectionDisplayContract.LocalSongSelectionBaseDisplayPresenter {
    private LocalSongModel mLocalSongModel;

    public LocalSongSelectionDisplayPresenterImpl(LocalSongSelectionDisplayContract.LocalSongSelectionDisplayView view) {
        super(view);
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
        getView().showLoading();
        getAndDisplayLocalSongs();
    }

    private void getAndDisplayLocalSongs() {
        getView().showLoading();

        Observable.create(new Observable.OnSubscribe<List<LocalSongEntity>>() {
            @Override
            public void call(Subscriber<? super List<LocalSongEntity>> subscriber) {
                subscriber.onNext(mLocalSongModel.getLocalSongs(getView().getViewContext()));
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
