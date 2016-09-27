package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.SearchPresenter;
import com.scott.su.smusic.mvp.view.SearchView;

/**
 * Created by Administrator on 2016/9/27.
 */

public class SearchPresenterImpl implements SearchPresenter {
    private SearchView mSearchView;

    public SearchPresenterImpl(SearchView mSearchView) {
        this.mSearchView = mSearchView;
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
