package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalSongDisplayView;
import com.su.scott.slibrary.util.L;

import java.util.List;


/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayPresenterImpl implements LocalSongDisplayPresenter {
    private LocalSongDisplayView mSongDisplayView;
    private LocalSongModelImpl mSongModel;

    public LocalSongDisplayPresenterImpl(LocalSongDisplayView localSongDisplayView) {
        this.mSongDisplayView = localSongDisplayView;
        this.mSongModel = new LocalSongModelImpl();
    }

    @Override
    public void onSwipRefresh() {
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
        mSongDisplayView.showLoading();
        getAndDisplayLocalSongs();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
//        mSongModel.clearCache();
    }

    private void getAndDisplayLocalSongs() {
        new AsyncTask<Void, Void, List<LocalSongEntity>>() {
            @Override
            protected List<LocalSongEntity> doInBackground(Void... voids) {
                if (mSongDisplayView.getSongBillEntity() == null) {
                    return mSongModel.getLocalSongs(mSongDisplayView.getViewContext());
                } else {
                    return mSongModel.getLocalSongsBySongIds(mSongDisplayView.getViewContext(),
                            mSongDisplayView.getSongBillEntity().getBillSongIdsLongArray());
                }

            }

            @Override
            protected void onPostExecute(List<LocalSongEntity> localSongEntities) {
                super.onPostExecute(localSongEntities);
                if (localSongEntities == null || localSongEntities.size() == 0) {
                    mSongDisplayView.showEmpty();
                    return;
                }
                mSongDisplayView.setDisplayData(localSongEntities);
                mSongDisplayView.display();
            }
        }.execute();
    }


}
