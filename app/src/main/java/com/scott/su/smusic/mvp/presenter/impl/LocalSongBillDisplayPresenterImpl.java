package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongBillModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongBillDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBillDisplayView;
import com.su.scott.slibrary.util.L;

import java.util.List;


/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongBillDisplayPresenterImpl implements LocalSongBillDisplayPresenter {
    private LocalSongBillDisplayView mLocalSongBillDisplayView;
    private LocalSongBillModelImpl mLocalSongBillModel;

    public LocalSongBillDisplayPresenterImpl(LocalSongBillDisplayView LocalSongBillDisplayView) {
        this.mLocalSongBillDisplayView = LocalSongBillDisplayView;
        this.mLocalSongBillModel = new LocalSongBillModelImpl();
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
        mLocalSongBillDisplayView.handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        mLocalSongBillDisplayView.showLoading();
        getAndDisplayLocalSongBills();
    }

    @Override
    public void onViewWillDestroy() {
//        mLocalSongBillModel.clearCache();
    }

    private void getAndDisplayLocalSongBills() {
        new AsyncTask<Void, Void, List<LocalSongBillEntity>>() {
            @Override
            protected List<LocalSongBillEntity> doInBackground(Void... voids) {
                return mLocalSongBillModel.getLocalSongBills(mLocalSongBillDisplayView.getViewContext());
            }

            @Override
            protected void onPostExecute(List<LocalSongBillEntity> localSongBillEntities) {
                super.onPostExecute(localSongBillEntities);

                L.e("===>",localSongBillEntities.toString());
                if (localSongBillEntities == null || localSongBillEntities.size() == 0) {
                    mLocalSongBillDisplayView.showEmpty();
                    return;
                }
                mLocalSongBillDisplayView.setDisplayData(localSongBillEntities);
                mLocalSongBillDisplayView.display();
            }
        }.execute();
    }


}
