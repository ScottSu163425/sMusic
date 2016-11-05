package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.cache.LocalSongEntityCache;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.AlbumSongDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.BillSongDisplayPresenter;
import com.scott.su.smusic.mvp.view.AlbumSongDisplayView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by asus on 2016/8/19.
 */
public class AlbumSongDisplayPresenterImpl implements AlbumSongDisplayPresenter {
    private AlbumSongDisplayView mSongDisplayView;
    private LocalSongModelImpl mSongModel;

    public AlbumSongDisplayPresenterImpl(AlbumSongDisplayView songDisplayView) {
        this.mSongDisplayView = songDisplayView;
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
        new AsyncTask<Void, Void, List<LocalSongEntity>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mSongDisplayView.setLoading();
            }

            @Override
            protected List<LocalSongEntity> doInBackground(Void... voids) {
                return mSongModel.getLocalSongsBySongIds(mSongDisplayView.getViewContext(),
                        mSongDisplayView.getSongAlbumEntity().getAlbumSongIdsLongArray());
            }

            @Override
            protected void onPostExecute(List<LocalSongEntity> localSongEntities) {
                super.onPostExecute(localSongEntities);

                List<LocalSongEntity> result = localSongEntities;

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
        }.execute();
    }


}
