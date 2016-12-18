package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.LocalBillDetailContract;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.manager.AsyncTaskHelper;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public class ILocalBillDetailPresenterImpl extends BasePresenter<LocalBillDetailContract.LocalBillDetailView>
        implements LocalBillDetailContract.ILocalBillDetailPresenter {
    private LocalBillModel mBillModel;
    private LocalAlbumModel mAlbumModel;

    public ILocalBillDetailPresenterImpl(LocalBillDetailContract.LocalBillDetailView view) {
        super(view);
        this.mBillModel = new LocalBillModelImpl();
        this.mAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onTransitionEnd() {

        if (getView().getBillEntity().isBillEmpty()) {
            getView().hideFab();
            getView().onEnterBillEmpty();
        } else {
            getView().showFab();
        }
    }

    @Override
    public void onPlayFabClick() {
        getView().goToMusicPlayWithCoverAndFabSharedElement(
                mBillModel.getBillSong(getView().getViewContext(),
                        getView().getBillEntity().getLatestSongId())
        );
    }

    @Override
    public void onEditBillMenuItemClick() {
        if (mBillModel.isDefaultBill(getView().getBillEntity())) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.cannot_edit_bill));
            return;
        }
        getView().showEditBillNameDialog();
    }

    @Override
    public void onAddSongsMenuItemClick() {
        getView().goToLocalSongSelectionActivity();
    }

    @Override
    public void onClearBillMenuItemClick() {
        if (getView().getBillEntity().isBillEmpty()) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.error_empty_bill));
            return;
        }

        getView().showClearBillSongsConfirmDialog();
    }

    @Override
    public void onDeleteBillMenuItemClick() {
        if (mBillModel.isDefaultBill(getView().getBillEntity())) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.bill_cannot_be_deleted));
            return;
        }
        getView().showDeleteBillConfirmDialog();
    }

    @Override
    public void onBillSongItemMoreClick(View view, int position, LocalSongEntity entity) {
        getView().showBillSongBottomSheet(entity);
    }

    @Override
    public void onSelectedLocalSongsResult(final LocalBillEntity billToAddSong, final List<LocalSongEntity> songsToAdd) {
        if (songsToAdd.size() == 1) {
            //Only select one song to add;
            LocalSongEntity songToAdd = songsToAdd.get(0);
            if (mBillModel.isBillContainsSong(billToAddSong, songToAdd)) {
                getView().showSnackbarShort(getView().getViewContext().getString(R.string.error_already_exist));
                return;
            }
            mBillModel.addSongToBill(getView().getViewContext(), songToAdd, billToAddSong);
            LocalBillEntity billAfterAddSong = mBillModel.getBill(getView().getViewContext(), billToAddSong.getBillId());
            //Update the bill entitiy of the activity;
            getView().setBillEntity(billAfterAddSong);
            //Update the bill songs display;
            getView().refreshBillSongDisplay(billAfterAddSong);
            //Update the bill cover;
            loadCover(true);
            getView().onAddSongsToBillSuccessfully();
            //When back to main activity ,the bill display show be updated
            AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
            //When back to search activity ,the bill display show be updated
            AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), true);
        } else {
            //More than one song was selected to be added into current bill;
            if (mBillModel.isBillContainsSongs(billToAddSong, songsToAdd)) {
                //Add songs failed if the bill contains all these selected songs;
                getView().showSnackbarShort(getView().getViewContext().getString(R.string.error_already_exist));
                return;
            }
            AsyncTaskHelper.excuteSimpleTask(new Runnable() {
                @Override
                public void run() {
                    mBillModel.addSongsToBill(getView().getViewContext(), songsToAdd, billToAddSong);
                }
            }, new AsyncTaskHelper.SimpleAsyncTaskCallback() {
                @Override
                public void onPreExecute() {
                    getView().showLoadingDialog(getView().getViewContext());
                }

                @Override
                public void onPostExecute() {
                    if (!isViewAttaching()) {
                        return;
                    }

                    getView().dismissLoadingDialog();
                    LocalBillEntity billAfterAddSong = mBillModel.getBill(getView().getViewContext(), billToAddSong.getBillId());
                    //Update the bill entitiy of the activity;
                    getView().setBillEntity(billAfterAddSong);
                    //Update the bill songs display;
                    getView().refreshBillSongDisplay(billAfterAddSong);
                    //Update the bill cover;
                    loadCover(true);
                    getView().onAddSongsToBillSuccessfully();
                    //When back to main activity ,the bill display show be updated
                    AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
                    AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), true);
                }
            });
        }
    }

    @Override
    public void onEditBillNameConfiemed(String text) {
        LocalBillEntity billEntity = getView().getBillEntity();
        billEntity.setBillTitle(text);
        mBillModel.saveOrUpdateBill(getView().getViewContext(), billEntity);

        getView().setBillEntity(mBillModel.getBill(getView().getViewContext(),
                billEntity.getBillId()));
        getView().updateBillInfo();
        getView().dismissEditBillNameDialog();
        getView().showSnackbarShort(getView().getViewContext().getString(R.string.edit_successfully));
        AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
        AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), true);
    }

    @Override
    public void onClearBillConfirmed() {
        AsyncTaskHelper.excuteSimpleTask(new Runnable() {
            @Override
            public void run() {
                mBillModel.clearBillSongs(getView().getViewContext(), getView().getBillEntity());
            }
        }, new AsyncTaskHelper.SimpleAsyncTaskCallback() {
            @Override
            public void onPreExecute() {
                getView().showLoadingDialog(getView().getViewContext());
            }

            @Override
            public void onPostExecute() {
                if (!isViewAttaching()) {
                    return;
                }

                getView().dismissLoadingDialog();
                LocalBillEntity billAfterClear = mBillModel.getBill(getView().getViewContext(), getView().getBillEntity().getBillId());
                getView().refreshBillSongDisplay(billAfterClear);
                getView().setBillEntity(billAfterClear);
                loadCover(true);
                AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
                AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), true);
            }
        });

    }

    @Override
    public void onDeleteBillMenuItemConfirmed() {
        AsyncTaskHelper.excuteSimpleTask(new Runnable() {
            @Override
            public void run() {
                mBillModel.deleteBill(getView().getViewContext(), getView().getBillEntity());
            }
        }, new AsyncTaskHelper.SimpleAsyncTaskCallback() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute() {
                if (!isViewAttaching()) {
                    return;
                }

                AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
                getView().onDeleteBillSuccessfully();
            }
        });
    }

    @Override
    public void onBillSongItemClick(View view, int position, LocalSongEntity entity) {
        if (position == 0) {
            if (getView().isFabVisiable()) {
                getView().goToMusicPlayWithCoverAndFabSharedElement(entity);
            } else {
                getView().goToMusicPlayWithCoverSharedElement(entity);
            }
        } else {
            if (getView().isFabVisiable()) {
                getView().goToMusicPlayWithFab(entity);
            } else {
                getView().goToMusicPlayWithoutFab(entity);
            }
        }

    }


    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
        getView().initToolbar();
        getView().initView();
        getView().initData();
        getView().initListener();

        loadCover(false);

        AsyncTaskHelper.excuteSimpleTask(new Runnable() {
            @Override
            public void run() {
                AppConfig.setPositionOfBillToRefresh(getView().getViewContext(),
                        mBillModel.getBillPosition(getView().getViewContext(), getView().getBillEntity()));
            }
        }, null);


    }

    private void loadCover(boolean needReveal) {
        String path = getView().getBillEntity().isBillEmpty() ? "" :
                mAlbumModel.getAlbumCoverPathBySongId(getView().getViewContext(),
                        getView().getBillEntity().getLatestSongId());
        getView().loadCover(path, needReveal);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {
        getView().showBillSelectionDialog(songEntity);
    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
        if (songEntity.getSongId() == getView().getBillEntity().getLatestSongId()) {
            getView().goToAlbumDetailWithSharedElement(mAlbumModel.getLocalAlbum(getView().getViewContext(), songEntity.getAlbumId()),
                    getView().getCoverImageView(),
                    getView().getViewContext().getString(R.string.transition_name_cover));
        } else {
            getView().goToAlbumDetail(mAlbumModel.getLocalAlbum(getView().getViewContext(), songEntity.getAlbumId()));
        }
    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {
        getView().showDeleteDialog(songEntity);
    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(getView().getViewContext(), songEntity, billEntity);
        AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
        AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), true);
        getView().showSnackbarShort(getView().getViewContext().getString(R.string.add_successfully));
    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {
        long lastSongIDBeforeDelete = getView().getBillEntity().getLatestSongId();
        mBillModel.removeBillSong(getView().getViewContext(), getView().getBillEntity(), songEntity);
        LocalBillEntity billAfterDelete = mBillModel.getBill(getView().getViewContext(), getView().getBillEntity().getBillId());
        getView().refreshBillSongDisplay(billAfterDelete);
        getView().setBillEntity(billAfterDelete);
        //Only when the deleted song is the latest song of the bill,should the bill cover perform reveal animation;
        loadCover(lastSongIDBeforeDelete == songEntity.getSongId());
        AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
        AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), true);
//        getView().showSnackbarShort(getView().getSnackbarParent(),
//                getView().getViewContext().getString(R.string.success_delete));
    }
}
