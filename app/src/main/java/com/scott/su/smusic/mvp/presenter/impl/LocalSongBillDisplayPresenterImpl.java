package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongBillModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongBillDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBillDisplayView;
import com.su.scott.slibrary.util.T;

import java.util.List;


/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongBillDisplayPresenterImpl implements LocalSongBillDisplayPresenter {
    private LocalSongBillDisplayView mBillDisplayView;
    private LocalSongBillModelImpl mBillModel;

    public LocalSongBillDisplayPresenterImpl(LocalSongBillDisplayView localSongBillDisplayView) {
        this.mBillDisplayView = localSongBillDisplayView;
        this.mBillModel = new LocalSongBillModelImpl();
    }

    @Override
    public void onSwipRefresh() {
        getAndDisplayLocalSongBills();
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
    public void onItemClick(View itemView, LocalSongBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mBillDisplayView.handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        mBillDisplayView.showLoading();
        getAndDisplayLocalSongBills();
    }

    @Override
    public void onViewWillDestroy() {

    }

    private void getAndDisplayLocalSongBills() {
        new AsyncTask<Void, Void, List<LocalSongBillEntity>>() {
            @Override
            protected List<LocalSongBillEntity> doInBackground(Void... voids) {
                return mBillModel.getBills(mBillDisplayView.getViewContext());
            }

            @Override
            protected void onPostExecute(List<LocalSongBillEntity> localSongBillEntities) {
                super.onPostExecute(localSongBillEntities);
                if (localSongBillEntities == null || localSongBillEntities.size() == 0) {
                    mBillDisplayView.showEmpty();
                    return;
                }
                mBillDisplayView.setDisplayData(localSongBillEntities);
                mBillDisplayView.display();
            }
        }.execute();
    }


}
