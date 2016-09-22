package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalAlbumDetailPresenter;
import com.scott.su.smusic.mvp.view.LocalAlbumDetailView;
import com.su.scott.slibrary.manager.AsyncTaskHelper;

/**
 * Created by Administrator on 2016/9/19.
 */
public class LocalAlbumDetailPresenterImpl implements LocalAlbumDetailPresenter {
    private LocalAlbumDetailView mAlbumDetailView;
    private LocalAlbumModel mAlbumModel;
    private LocalBillModel mBillModel;

    public LocalAlbumDetailPresenterImpl(LocalAlbumDetailView mAlbumDetailView) {
        this.mAlbumDetailView = mAlbumDetailView;
        this.mAlbumModel = new LocalAlbumModelImpl();
        this.mBillModel = new LocalBillModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mAlbumDetailView.initPreData();
        mAlbumDetailView.initToolbar();
        mAlbumDetailView.initView();
        mAlbumDetailView.initData();
        mAlbumDetailView.initListener();

        mAlbumDetailView.loadAlbumCover(mAlbumModel.getAlbumCoverPath(mAlbumDetailView.getViewContext(),
                mAlbumDetailView.getCurrentAlbumEntity().getAlbumId()));
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {
        mAlbumDetailView.showBillSelectionDialog(songEntity);
    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
    }

    @Override
    public void onBottomSheetShareClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            mAlbumDetailView.showSnackbarShort(mAlbumDetailView.getSnackbarParent(), mAlbumDetailView.getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(mAlbumDetailView.getViewContext(), songEntity, billEntity);
        AppConfig.setNeedToRefreshLocalBillDisplay(mAlbumDetailView.getViewContext(), true);
        mAlbumDetailView.showSnackbarShort(mAlbumDetailView.getSnackbarParent(), mAlbumDetailView.getViewContext().getString(R.string.add_successfully));
    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {

    }

    @Override
    public void onAlbumSongItemClick(View view, int position, LocalSongEntity entity) {
        if (position == 0) {
            mAlbumDetailView.goToMusicPlayWithCover(entity);
        } else {
            mAlbumDetailView.goToMusicPlay(entity);
        }
    }

    @Override
    public void onAlbumSongItemMoreClick(View view, int position, LocalSongEntity entity) {
        mAlbumDetailView.showAlbumSongBottomSheet(entity);
    }

    @Override
    public void onPlayFabClick() {
        mAlbumDetailView.goToMusicPlayWithCover(mAlbumDetailView.getCurrentAlbumEntity().getAlbumSongs().get(0));
    }
}
