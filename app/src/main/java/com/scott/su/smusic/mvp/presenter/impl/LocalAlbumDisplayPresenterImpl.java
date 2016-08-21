package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalAlbumDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalAlbumDisplayView;

import java.util.List;


/**
 * Created by asus on 2016/8/19.
 */
public class LocalAlbumDisplayPresenterImpl implements LocalAlbumDisplayPresenter {
    private LocalAlbumDisplayView mLocalAlbumDisplayView;
    private LocalAlbumModel mLocalAlbumModel;

    public LocalAlbumDisplayPresenterImpl(LocalAlbumDisplayView localAlbumDisplayView) {
        this.mLocalAlbumDisplayView = localAlbumDisplayView;
        this.mLocalAlbumModel = new LocalAlbumModelImpl();
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
    public void onItemClick(View itemView, LocalAlbumEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mLocalAlbumDisplayView.handleItemClick(itemView, entity, position, sharedElements, transitionNames, data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        mLocalAlbumDisplayView.showLoading();
        getAndDisplayLocalSongs();
    }

    @Override
    public void onViewWillDestroy() {

    }

    private void getAndDisplayLocalSongs() {
        new AsyncTask<Void, Void, List<LocalAlbumEntity>>() {
            @Override
            protected List<LocalAlbumEntity> doInBackground(Void... voids) {
                return mLocalAlbumModel.getLocalAlbums(mLocalAlbumDisplayView.getViewContext());
            }

            @Override
            protected void onPostExecute(List<LocalAlbumEntity> localAlbumEntities) {
                super.onPostExecute(localAlbumEntities);
                if (localAlbumEntities == null || localAlbumEntities.size() == 0) {
                    mLocalAlbumDisplayView.showEmpty();
                    return;
                }
                mLocalAlbumDisplayView.setDisplayData(localAlbumEntities);
                mLocalAlbumDisplayView.display();
            }
        }.execute();
    }


}
