package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.SearchPresenter;
import com.scott.su.smusic.mvp.view.SearchView;
import com.scott.su.smusic.ui.activity.SearchActivity;
import com.su.scott.slibrary.manager.AsyncTaskHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/27.
 */

public class SearchPresenterImpl implements SearchPresenter {
    private SearchView mSearchView;
    private LocalSongModel mLocalSongModel;
    private LocalBillModel mLocalBillModel;
    private LocalAlbumModel mLocalAlbumModel;

    public SearchPresenterImpl(SearchView mSearchView) {
        this.mSearchView = mSearchView;
        this.mLocalSongModel = new LocalSongModelImpl();
        this.mLocalBillModel = new LocalBillModelImpl();
        this.mLocalAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mSearchView.initPreData();
        mSearchView.initToolbar();
        mSearchView.initView();
        mSearchView.initData();
        mSearchView.initListener();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onSearchClick(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            mSearchView.showSnackbarShort(mSearchView.getSnackbarParent(), mSearchView.getViewContext().getString(R.string.empty_keyword));
            return;
        }

        new AsyncTask<Void, Void, List[]>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mSearchView.showLoading();
            }

            @Override
            protected List[] doInBackground(Void... voids) {
                List<LocalSongEntity> localSongEntities = mLocalSongModel.searchLocalSong(mSearchView.getViewContext(), keyword);
                List<LocalBillEntity> localBillEntities = mLocalBillModel.searchBill(mSearchView.getViewContext(), keyword);
                List<LocalAlbumEntity> localAlbumEntities = mLocalAlbumModel.searchLocalAlbum(mSearchView.getViewContext(), keyword);
                List[] result = new List[3];
                result[0] = localSongEntities;
                result[1] = localBillEntities;
                result[2] = localAlbumEntities;

                return result;
            }

            @Override
            protected void onPostExecute(List[] lists) {
                super.onPostExecute(lists);

                List<LocalSongEntity> localSongEntities = lists[0];
                List<LocalBillEntity> localBillEntities = lists[1];
                List<LocalAlbumEntity> localAlbumEntities = lists[2];
                List result = new ArrayList();

                if (!localSongEntities.isEmpty()) {
                    result.add(mSearchView.getViewContext().getString(R.string.local_music));
                    result.addAll(localSongEntities);
                }

                if (!localBillEntities.isEmpty()) {
                    result.add(mSearchView.getViewContext().getString(R.string.local_bill));
                    result.addAll(localBillEntities);
                }

                if (!localAlbumEntities.isEmpty()) {
                    result.add(mSearchView.getViewContext().getString(R.string.album));
                    result.addAll(localAlbumEntities);
                }

                if (result.isEmpty()) {
                    mSearchView.showEmpty();
                } else {
                    mSearchView.showResult(result);
                }
            }
        }.execute();


    }

    @Override
    public void onLocalSongClick(LocalSongEntity entity, View sharedElement, String transitionName) {
        mSearchView.goToMusicWithSharedElement(entity, sharedElement, transitionName);
    }

    @Override
    public void onLocalSongMoreClick(LocalSongEntity entity) {

    }

    @Override
    public void onLocalBillClick(LocalBillEntity entity, View sharedElement, String transitionName) {
        mSearchView.goToBillDetailWithSharedElement(entity, sharedElement, transitionName);
    }

    @Override
    public void onLocalAlbumClick(LocalAlbumEntity entity, View sharedElement, String transitionName) {
        mSearchView.goToAlbumDetailWithSharedElement(entity, sharedElement, transitionName);
    }

}
