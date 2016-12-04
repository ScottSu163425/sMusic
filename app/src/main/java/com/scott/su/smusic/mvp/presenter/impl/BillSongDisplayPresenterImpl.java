package com.scott.su.smusic.mvp.presenter.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.cache.LocalSongEntityCache;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.BillSongDisplayPresenter;
import com.scott.su.smusic.mvp.view.BillSongDisplayView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by asus on 2016/8/19.
 */
public class BillSongDisplayPresenterImpl implements BillSongDisplayPresenter {
    private BillSongDisplayView mSongDisplayView;
    private LocalSongModelImpl mSongModel;

    public BillSongDisplayPresenterImpl(BillSongDisplayView billSongDisplayView) {
        this.mSongDisplayView = billSongDisplayView;
        this.mSongModel = new LocalSongModelImpl();
    }

    @Override
    public void onSwipRefresh() {
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
        mSongDisplayView.handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getAndDisplayLocalSongs();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
    }

    private void getAndDisplayLocalSongs() {
        mSongDisplayView.showLoading();

        Observable.just(mSongDisplayView.getSongBillEntity().getBillSongIdsLongArray())
                .map(new Func1<long[], List<LocalSongEntity>>() {
                    @Override
                    public List<LocalSongEntity> call(long[] songIds) {
                        return mSongModel.getLocalSongsBySongIds(mSongDisplayView.getViewContext(), songIds);
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

                        mSongDisplayView.setDisplayData(result);

                        if (result.size() == 0) {
                            mSongDisplayView.showEmpty();
                        } else {
                            mSongDisplayView.display();
                        }
                    }
                });
    }


}
