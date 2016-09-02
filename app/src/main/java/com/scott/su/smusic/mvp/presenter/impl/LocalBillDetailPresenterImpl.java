package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.AppConfigModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.AppConfigModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalBillDetailPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBillDetailView;
import com.su.scott.slibrary.util.L;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public class LocalBillDetailPresenterImpl implements LocalBillDetailPresenter {
    private LocalSongBillDetailView mBillDetailView;
    private LocalBillModel mBillModel;
    private LocalSongModel mSongModel;
    private AppConfigModel mAppConfigModel;

    public LocalBillDetailPresenterImpl(LocalSongBillDetailView mBillDetailView) {
        this.mBillDetailView = mBillDetailView;
        this.mBillModel = new LocalBillModelImpl();
        this.mSongModel = new LocalSongModelImpl();
        this.mAppConfigModel = new AppConfigModelImpl();
    }

    @Override
    public void onAddSongsMenuItemClick() {
        mBillDetailView.goToLocalSongSelectionActivity();
    }

    @Override
    public void onClearBillMenuItemClick() {
        if (mBillDetailView.getBillEntity().isBillEmpty()) {
            mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(),
                    mBillDetailView.getViewContext().getString(R.string.error_empty_bill));
            return;
        }

        mBillDetailView.showClearBillSongsConfirmDialog();
    }

    @Override
    public void onDeleteBillMenuItemClick() {
        if (mBillModel.isDefaultBill(mBillDetailView.getBillEntity())) {
            mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), "这张歌单不能删除");
            return;
        }
        mBillDetailView.showDeleteBillConfirmDialog();
    }

    @Override
    public void onBillSongItemMoreClick(View view, int position, LocalSongEntity entity) {
        mBillDetailView.showBillSongBottomSheet(entity);
    }

    @Override
    public void onSelectedLocalSongsResult(LocalBillEntity billToAddSong, List<LocalSongEntity> songsToAdd) {
        if (songsToAdd.size() == 1) {
            //Only select one song to add;
            LocalSongEntity songToAdd = songsToAdd.get(0);
            if (mBillModel.isBillContainsSong(billToAddSong, songToAdd)) {
                mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), mBillDetailView.getViewContext().getString(R.string.error_already_exist));
                return;
            }
            mBillModel.addSongToBill(mBillDetailView.getViewContext(), songToAdd, billToAddSong);
        } else {
            //More than one song was selected to be added into current bill;
            if (mBillModel.isBillContainsSongs(billToAddSong, songsToAdd)) {
                //Add songs failed if the bill contains all these selected songs;
                mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), mBillDetailView.getViewContext().getString(R.string.error_already_exist));
                return;
            }
            mBillModel.addSongsToBill(mBillDetailView.getViewContext(), songsToAdd, billToAddSong);
        }
        LocalBillEntity billAfterAddSong = mBillModel.getBill(mBillDetailView.getViewContext(), billToAddSong.getBillId());
        //Update the bill entitiy of the activity;
        mBillDetailView.setBillEntity(billAfterAddSong);
        //Update the bill songs display;
        mBillDetailView.refreshBillSongDisplay(billAfterAddSong);
        //Update the bill cover;
        loadCover(true);
        mBillDetailView.showAddSongsToBillSuccessfully();
        //When back to main activity ,the bill display show be updated
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
    }

    @Override
    public void onClearBillConfirmed() {
        mBillModel.clearBillSongs(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity());
        LocalBillEntity billAfterClear = mBillModel.getBill(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity().getBillId());
        mBillDetailView.refreshBillSongDisplay(billAfterClear);
        mBillDetailView.setBillEntity(billAfterClear);
        loadCover(true);
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
    }

    @Override
    public void onDeleteBillMenuItemConfirmed() {
        mBillModel.deleteBill(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity());
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
        mBillDetailView.showDeleteBillSuccessfully();
    }


    @Override
    public void onViewFirstTimeCreated() {
        mBillDetailView.initPreData();
        mBillDetailView.initToolbar();
        mBillDetailView.initView();
        mBillDetailView.initData();
        mBillDetailView.initListener();

        loadCover(false);

        if (mBillDetailView.getBillEntity().isBillEmpty()) {
            mBillDetailView.hideFab();
        } else {
            mBillDetailView.showFab();
        }
    }

    private void loadCover(boolean needReveal) {
        String path = mBillDetailView.getBillEntity().isBillEmpty() ? "" :
                mSongModel.getAlbumCoverPath(mBillDetailView.getViewContext(),
                        mBillDetailView.getBillEntity().getLatestSong().getAlbumId());
        mBillDetailView.loadCover(path, needReveal);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {
        mBillDetailView.showBillSelectionDialog(songEntity);
    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
        mBillDetailView.showToastShort("Album :" + songEntity.getTitle());
    }

    @Override
    public void onBottomSheetShareClick(LocalSongEntity songEntity) {
        mBillDetailView.showToastShort("Share :" + songEntity.getTitle());
    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {
        mBillDetailView.showDeleteBillSongConfirmDialog(songEntity);
    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), "歌单中已存在");
            return;
        }

        mBillModel.addSongToBill(mBillDetailView.getViewContext(), songEntity, billEntity);
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
        mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), "添加成功");
    }

    @Override
    public void onBottomSheetAlbumConfirmed(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetShareConfirmed(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {
        LocalBillEntity billBeforeDelete = mBillDetailView.getBillEntity();
        mBillModel.deleteBillSong(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity(), songEntity);
        LocalBillEntity billAfterDelete = mBillModel.getBill(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity().getBillId());
        mBillDetailView.refreshBillSongDisplay(billAfterDelete);
        mBillDetailView.setBillEntity(billAfterDelete);
        //Only when the deleted song is the latest song of the bill,should the bill cover perform reveal animation;
        loadCover(billBeforeDelete.getLatestSong().getSongId() == songEntity.getSongId());
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);

//        mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(),
//                mBillDetailView.getViewContext().getString(R.string.success_delete));
    }
}
