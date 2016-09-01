package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.AppConfigModel;
import com.scott.su.smusic.mvp.model.LocalSongBillModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.AppConfigModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongBillModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongBillDetailPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBillDetailView;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public class LocalSongBillDetailPresenterImpl implements LocalSongBillDetailPresenter {
    private LocalSongBillDetailView mBillDetailView;
    private LocalSongBillModel mBillModel;
    private LocalSongModel mSongModel;
    private AppConfigModel mAppConfigModel;

    public LocalSongBillDetailPresenterImpl(LocalSongBillDetailView mBillDetailView) {
        this.mBillDetailView = mBillDetailView;
        this.mBillModel = new LocalSongBillModelImpl();
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
            mBillDetailView.showDeleteBillUnsuccessfully("这张歌单不能删除");
            return;
        }
        mBillDetailView.showDeleteBillConfirmDialog();
    }

    @Override
    public void onBillSongItemMoreClick(View view, int position, LocalSongEntity entity) {
        mBillDetailView.showSongBottomSheet(entity);
    }

    @Override
    public void onSelectedLocalSongsResult(LocalSongBillEntity billToAddSong, List<LocalSongEntity> songsToAdd) {
        if (songsToAdd.size() == 1) {
            //Only select one song to add;
            LocalSongEntity songToAdd = songsToAdd.get(0);
            if (mBillModel.isBillContains(billToAddSong, songToAdd)) {
                mBillDetailView.showAddSongsUnsuccessfully(mBillDetailView.getViewContext().getString(R.string.error_already_exist));
                return;
            }
            mBillModel.addSongToBill(mBillDetailView.getViewContext(), songToAdd, billToAddSong);
        } else {
            //More than one song was selected to be added into current bill;
            if (mBillModel.isBillContainsAll(billToAddSong, songsToAdd)) {
                //Add songs failed if the bill contains all these selected songs;
                mBillDetailView.showAddSongsUnsuccessfully(mBillDetailView.getViewContext().getString(R.string.error_already_exist));
                return;
            }
            mBillModel.addSongsToBill(mBillDetailView.getViewContext(), songsToAdd, billToAddSong);
        }
        LocalSongBillEntity billAfterAddSong = mBillModel.getBill(mBillDetailView.getViewContext(), billToAddSong.getBillId());
        //Update the bill entitiy of the activity;
        mBillDetailView.setBillEntity(billAfterAddSong);
        //Update the bill songs display;
        mBillDetailView.refreshBillSongDisplay(billAfterAddSong);
        //Update the bill cover;
        loadCover(true);
        mBillDetailView.showAddSongsSuccessfully();
        //When back to main activity ,the bill display show be updated
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
    }

    @Override
    public void onDeleteBillSongConfirmed(LocalSongEntity songEntity) {
        LocalSongBillEntity billBeforeDelete = mBillDetailView.getBillEntity();
        mBillModel.deleteBillSong(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity(), songEntity);
        LocalSongBillEntity billAfterDelete = mBillModel.getBill(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity().getBillId());
        mBillDetailView.refreshBillSongDisplay(billAfterDelete);
        mBillDetailView.setBillEntity(billAfterDelete);
        //Only when the deleted song is the latest song of the bill,should the bill cover perform reveal animation;
        loadCover(billBeforeDelete.getLatestSong().getSongId() == songEntity.getSongId());
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);

//        mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(),
//                mBillDetailView.getViewContext().getString(R.string.success_delete));
    }

    @Override
    public void onClearBillConfirmed() {
        mBillModel.clearBillSongs(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity());
        LocalSongBillEntity billAfterClear = mBillModel.getBill(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity().getBillId());
        mBillDetailView.refreshBillSongDisplay(billAfterClear);
        mBillDetailView.setBillEntity(billAfterClear);
        loadCover(true);
        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
    }

    @Override
    public void onDeleteBillConfirmed() {
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
        mBillDetailView.showToastShort("Add :" + songEntity.getTitle());
    }

    @Override
    public void onBottomSheetArtistClick(LocalSongEntity songEntity) {
        mBillDetailView.showToastShort("Artist :" + songEntity.getTitle());
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
}
