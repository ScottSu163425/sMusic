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

    private void getAndDisplayLocalSongs(final boolean isRefresh) {
        Observable.create(new ObservableOnSubscribe<List<LocalAlbumEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocalAlbumEntity>> e) throws Exception {
                e.onNext(mLocalAlbumModel.getLocalAlbums(getView().getViewContext()));
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
                .subscribe(new Consumer<List<LocalAlbumEntity>>() {
                    @Override
                    public void accept(List<LocalAlbumEntity> localAlbumEntities) throws Exception {
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
