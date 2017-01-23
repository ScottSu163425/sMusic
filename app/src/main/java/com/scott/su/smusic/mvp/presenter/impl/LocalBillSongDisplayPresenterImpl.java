package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.cache.LocalSongEntityCache;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.BillSongDisplayContract;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by asus on 2016/8/19.
 */
public class LocalBillSongDisplayPresenterImpl extends BasePresenter<BillSongDisplayContract.BillSongDisplayView>
        implements BillSongDisplayContract.BillSongBaseDisplayPresenter {
    private LocalSongModelImpl mSongModel;


    public LocalBillSongDisplayPresenterImpl(BillSongDisplayContract.BillSongDisplayView billSongDisplayView) {
        super(billSongDisplayView);
        this.mSongModel = new LocalSongModelImpl();
    }

    @Override
    public void onSwipeRefresh() {
        LocalSongEntityCache.getInstance().release();
        getAndDisplayLocalSongs();
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
        getAndDisplayLocalSongs();
    }

    private void getAndDisplayLocalSongs() {
        Observable.just(getView().getSongBillEntity().getBillSongIdsLongArray())
                .map(new Function<long[], List<LocalSongEntity>>() {
                    @Override
                    public List<LocalSongEntity> apply(long[] songIds) throws Exception {
                        return mSongModel.getLocalSongsBySongIds(getView().getViewContext(), songIds);
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
