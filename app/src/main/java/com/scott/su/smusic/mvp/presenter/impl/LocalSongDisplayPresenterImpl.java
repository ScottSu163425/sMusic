package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongDisplayPresenter;
import com.scott.su.smusic.mvp.view.LocalSongDisplayView;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayPresenterImpl implements LocalSongDisplayPresenter {
    private LocalSongDisplayView mLocalSongDisplayView;
    private LocalSongModel mLocalSongModel;

    public LocalSongDisplayPresenterImpl(LocalSongDisplayView localSongDisplayView){
        this.mLocalSongDisplayView = localSongDisplayView;
        this.mLocalSongModel = new LocalSongModelImpl();
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
        mLocalSongDisplayView.handleItemClick(itemView,entity,position,sharedElements,transitionNames,data);
    }

    @Override
    public void onViewFirstTimeCreated() {
        mLocalSongDisplayView.showLoading();
         getAndDisplayLocalSongs();
    }

    private void getAndDisplayLocalSongs() {
        new AsyncTask<Void,Void,List<LocalSongEntity>>(){
            @Override
            protected List<LocalSongEntity> doInBackground(Void... voids) {
                return mLocalSongModel.getLocalSongs(mLocalSongDisplayView.getViewContext());
            }

            @Override
            protected void onPostExecute(List<LocalSongEntity> localSongEntities) {
                super.onPostExecute(localSongEntities);
                mLocalSongDisplayView.setDisplayData(localSongEntities);
                mLocalSongDisplayView.display();
            }
        }.execute();
    }


}
