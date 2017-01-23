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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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
    public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {

    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().showLoading();
        getAndDisplayLocalSongs();
    }

    private void getAndDisplayLocalSongs() {
        getView().showLoading();

        Observable.create(new ObservableOnSubscribe<List<LocalSongEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocalSongEntity>> e) throws Exception {
                e.onNext(mLocalSongModel.getLocalSongs(getView().getViewContext()));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().showLoading();
                    }
                })
                .subscribe(new Consumer<List<LocalSongEntity>>() {
                    @Override
                    public void accept(List<LocalSongEntity> songEntities) throws Exception {
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
