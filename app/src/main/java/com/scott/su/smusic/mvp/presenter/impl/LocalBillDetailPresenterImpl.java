package com.scott.su.smusic.mvp.presenter.impl;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalBillDetailPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBillDetailView;
import com.su.scott.slibrary.manager.AsyncTaskHelper;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public class LocalBillDetailPresenterImpl implements LocalBillDetailPresenter {
    private LocalSongBillDetailView mBillDetailView;
    private LocalBillModel mBillModel;
    private LocalSongModel mSongModel;
    private LocalAlbumModel mAlbumModel;

    public LocalBillDetailPresenterImpl(LocalSongBillDetailView mBillDetailView) {
        this.mBillDetailView = mBillDetailView;
        this.mBillModel = new LocalBillModelImpl();
        this.mSongModel = new LocalSongModelImpl();
        this.mAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onPlayFabClick() {
        mBillDetailView.goToMusicPlayWithCoverSharedElement();
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
            mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), mBillDetailView.getViewContext().getString(R.string.bill_cannot_be_deleted));
            return;
        }
        mBillDetailView.showDeleteBillConfirmDialog();
    }

    @Override
    public void onBillSongItemMoreClick(View view, int position, LocalSongEntity entity) {
        mBillDetailView.showBillSongBottomSheet(entity);
    }

    @Override
    public void onSelectedLocalSongsResult(final LocalBillEntity billToAddSong, final List<LocalSongEntity> songsToAdd) {
        if (songsToAdd.size() == 1) {
            //Only select one song to add;
            LocalSongEntity songToAdd = songsToAdd.get(0);
            if (mBillModel.isBillContainsSong(billToAddSong, songToAdd)) {
                mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), mBillDetailView.getViewContext().getString(R.string.error_already_exist));
                return;
            }
            mBillModel.addSongToBill(mBillDetailView.getViewContext(), songToAdd, billToAddSong);
            LocalBillEntity billAfterAddSong = mBillModel.getBill(mBillDetailView.getViewContext(), billToAddSong.getBillId());
            //Update the bill entitiy of the activity;
            mBillDetailView.setBillEntity(billAfterAddSong);
            //Update the bill songs display;
            mBillDetailView.refreshBillSongDisplay(billAfterAddSong);
            //Update the bill cover;
            loadCover(true);
            mBillDetailView.showAddSongsToBillSuccessfully();
            //When back to main activity ,the bill display show be updated
            AppConfig.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
        } else {
            //More than one song was selected to be added into current bill;
            if (mBillModel.isBillContainsSongs(billToAddSong, songsToAdd)) {
                //Add songs failed if the bill contains all these selected songs;
                mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), mBillDetailView.getViewContext().getString(R.string.error_already_exist));
                return;
            }
            AsyncTaskHelper.excuteSimpleTask(new Runnable() {
                @Override
                public void run() {
                    mBillModel.addSongsToBill(mBillDetailView.getViewContext(), songsToAdd, billToAddSong);
                }
            }, new AsyncTaskHelper.SimpleAsyncTaskCallback() {
                @Override
                public void onPreExecute() {
                    mBillDetailView.showLoadingDialog(mBillDetailView.getViewContext());
                }

                @Override
                public void onPostExecute() {
                    mBillDetailView.dismissLoadingDialog();
                    LocalBillEntity billAfterAddSong = mBillModel.getBill(mBillDetailView.getViewContext(), billToAddSong.getBillId());
                    //Update the bill entitiy of the activity;
                    mBillDetailView.setBillEntity(billAfterAddSong);
                    //Update the bill songs display;
                    mBillDetailView.refreshBillSongDisplay(billAfterAddSong);
                    //Update the bill cover;
                    loadCover(true);
                    mBillDetailView.showAddSongsToBillSuccessfully();
                    //When back to main activity ,the bill display show be updated
                    AppConfig.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
                }
            });
        }
    }

    @Override
    public void onClearBillConfirmed() {
        AsyncTaskHelper.excuteSimpleTask(new Runnable() {
            @Override
            public void run() {
                mBillModel.clearBillSongs(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity());
            }
        }, new AsyncTaskHelper.SimpleAsyncTaskCallback() {
            @Override
            public void onPreExecute() {
                mBillDetailView.showLoadingDialog(mBillDetailView.getViewContext());
            }

            @Override
            public void onPostExecute() {
                mBillDetailView.dismissLoadingDialog();
                LocalBillEntity billAfterClear = mBillModel.getBill(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity().getBillId());
                mBillDetailView.refreshBillSongDisplay(billAfterClear);
                mBillDetailView.setBillEntity(billAfterClear);
                loadCover(true);
                AppConfig.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
            }
        });

    }

    @Override
    public void onDeleteBillMenuItemConfirmed() {
        AsyncTaskHelper.excuteSimpleTask(new Runnable() {
            @Override
            public void run() {
                mBillModel.deleteBill(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity());
            }
        }, new AsyncTaskHelper.SimpleAsyncTaskCallback() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute() {
                AppConfig.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
                mBillDetailView.showDeleteBillSuccessfully();
            }
        });
    }

    @Override
    public void onBillSongItemClick(View view, int position, LocalSongEntity entity) {
        if (position == 0) {
            mBillDetailView.goToMusicPlayWithCoverSharedElement();
        } else {
            mBillDetailView.goToMusicPlay(entity);
        }

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
            mBillDetailView.showEnterBillEmpty();
        } else {
            mBillDetailView.showFab();
        }

        AsyncTaskHelper.excuteSimpleTask(new Runnable() {
            @Override
            public void run() {
                AppConfig.setPositionOfBillToRefresh(mBillDetailView.getViewContext(),
                        mBillModel.getBillPosition(mBillDetailView.getViewContext(), mBillDetailView.getBillEntity()));
            }
        }, null);


    }

    private void loadCover(boolean needReveal) {
        String path = mBillDetailView.getBillEntity().isBillEmpty() ? "" :
                mAlbumModel.getAlbumCoverPath(mBillDetailView.getViewContext(),
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
        mBillDetailView.goToAlbumDetail(mAlbumModel.getLocalAlbum(mBillDetailView.getViewContext(), songEntity.getAlbumId()));
    }

    @Override
    public void onBottomSheetShareClick(LocalSongEntity songEntity) {
        mBillDetailView.showToastShort("Share :" + songEntity.getTitle());
    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {
        mBillDetailView.showDeleteDialog(songEntity);
    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), mBillDetailView.getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(mBillDetailView.getViewContext(), songEntity, billEntity);
        AppConfig.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);
        mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(), mBillDetailView.getViewContext().getString(R.string.add_successfully));
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
        AppConfig.setNeedToRefreshLocalBillDisplay(mBillDetailView.getViewContext(), true);

//        mBillDetailView.showSnackbarShort(mBillDetailView.getSnackbarParent(),
//                mBillDetailView.getViewContext().getString(R.string.success_delete));
    }
}
