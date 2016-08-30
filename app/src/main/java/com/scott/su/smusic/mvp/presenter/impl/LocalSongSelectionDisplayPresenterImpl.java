package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongSelectionDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalSongSelectionDisplayView;

import java.util.List;

/**
 * Created by asus on 2016/8/27.
 */
public class LocalSongSelectionDisplayPresenterImpl implements LocalSongSelectionDisplayPresenter {
    private LocalSongSelectionDisplayView mDisplayView;
    private LocalSongModel mLocalSongModel;

    public LocalSongSelectionDisplayPresenterImpl(LocalSongSelectionDisplayView mDisplayView) {
        this.mDisplayView = mDisplayView;
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
        mDisplayView.showLoading();
        getAndDisplayLocalSongs();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    private void getAndDisplayLocalSongs() {
        new AsyncTask<Void, Void, List<LocalSongEntity>>() {
            @Override
            protected List<LocalSongEntity> doInBackground(Void... voids) {
                return mLocalSongModel.getLocalSongs(mDisplayView.getViewContext());
            }

            @Override
            protected void onPostExecute(List<LocalSongEntity> localSongEntities) {
                super.onPostExecute(localSongEntities);
                if (localSongEntities == null || localSongEntities.size() == 0) {
                    mDisplayView.showEmpty();
                    return;
                }
                mDisplayView.setDisplayData(localSongEntities);
                mDisplayView.display();
            }
        }.execute();
    }

}
