package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.presenter.MainPresenter;
import com.scott.su.smusic.mvp.view.MainView;
import com.scott.su.smusic.util.MusicPlayUtil;
import com.su.scott.slibrary.callback.SimpleCallback;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mMainView;
    private LocalBillModel mBillModel;
    private LocalAlbumModel mAlbumModel;

    public MainPresenterImpl(MainView mView) {
        this.mMainView = mView;
        this.mBillModel = new LocalBillModelImpl();
        this.mAlbumModel = new LocalAlbumModelImpl();
    }


    @Override
    public void onLocalSongItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mMainView.goToMusicWithSharedElement(entity, sharedElements[0], transitionNames[0]);
    }

    @Override
    public void onLocalSongItemMoreClick(LocalSongEntity songEntity) {
        mMainView.showLocalSongBottomSheet(songEntity);
    }

    @Override
    public void onFabClick() {
        if (mMainView.isCurrentTabSong()) {
            if (mMainView.getCurrentPlayingSong() == null) {
                //Has not played any song,play random;
                mMainView.playRandomSong();
            } else {
                //A song is playing or paused;
                final int currentPlayingSongPositon = MusicPlayUtil.getSongPosition(mMainView.getCurrentPlayingSong(), mMainView.getCurrentPlayingSongs());
                mMainView.scrollSongPositionTo(currentPlayingSongPositon, new SimpleCallback() {
                    @Override
                    public void onCallback() {
                        mMainView.playSongInPosition(currentPlayingSongPositon);
                    }
                });
            }

        } else if (mMainView.isCurrentTabBill()) {
            mMainView.showCreateBillDialog();
        }

    }

    @Override
    public void onCreateBillConfirm(String text) {
        LocalBillEntity billEntity = new LocalBillEntity(text);

        if (mBillModel.isBillTitleExist(mMainView.getViewContext(), billEntity)) {
            mMainView.showSnackbarShort(mMainView.getSnackbarParent(), mMainView.getViewContext().getString(R.string.error_already_exist));
            return;
        }

        mBillModel.saveOrUpdateBill(mMainView.getViewContext(), billEntity);
        mMainView.updateBillDisplay();
        AppConfig.setNeedToRefreshLocalBillDisplay(mMainView.getViewContext(), false);
        mMainView.dismissCreateBillDialog();
        mMainView.showCreateBillSuccessfully(billEntity);
    }

    @Override
    public void onBillItemClick(View itemView, LocalBillEntity entity, int position,
                                @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mMainView.goToBillDetailWithSharedElement(entity, sharedElements[0], transitionNames[0]);
    }

    @Override
    public void onAlbumItemClick(View itemView, LocalAlbumEntity entity, int position,
                                 @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mMainView.goToAlbumDetailWithSharedElement(entity, sharedElements[0], transitionNames[0]);
    }

    @Override
    public void onSelectedLocalSongsResult(final LocalBillEntity billToAddSong,
                                           final List<LocalSongEntity> songsToAdd) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mMainView.showLoadingDialog(mMainView.getViewContext(), mMainView.getViewContext().getString(R.string.please_waiting), false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                mBillModel.addSongsToBill(mMainView.getViewContext(), songsToAdd, billToAddSong);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mMainView.updateBillDisplay();
                mMainView.dismissLoadingDialog();
                mMainView.showToastShort(mMainView.getViewContext().getString(R.string.add_successfully));
                mMainView.goToBillDetail(mBillModel.getBill(mMainView.getViewContext(), billToAddSong.getBillId()));
            }
        }.execute();

    }

    @Override
    public void onDrawerMenuStatisticClick() {
        mMainView.showToastShort("Statistic");
    }

    @Override
    public void onDrawerMenuUpdateClick() {
        mMainView.showToastShort("Update");
    }

    @Override
    public void onDrawerMenuAboutClick() {
        mMainView.showToastShort("About");
    }

    @Override
    public void onNightModeOn() {
        AppConfig.setNightMode(mMainView.getViewContext(), true);
        mMainView.turnOnNightMode();
        mMainView.updateBillDisplay();
    }

    @Override
    public void onNightModeOff() {
        AppConfig.setNightMode(mMainView.getViewContext(), false);
        mMainView.turnOffNightMode();
        mMainView.updateBillDisplay();
    }

    @Override
    public void onLanguageModeOn() {
        AppConfig.setLanguageMode(mMainView.getViewContext(), true);
        mMainView.turnOnLanguageMode();
        updateDefaultBillName();
        mMainView.updateBillDisplay();
    }

    @Override
    public void onLanguageModeOff() {
        AppConfig.setLanguageMode(mMainView.getViewContext(), false);
        mMainView.turnOffLanguageMode();
        updateDefaultBillName();
        mMainView.updateBillDisplay();
    }

    private void updateDefaultBillName() {
        LocalBillEntity defaultBill = mBillModel.getDefaultBill(mMainView.getViewContext());
        defaultBill.setBillTitle(mMainView.getViewContext().getString(R.string.my_favourite));
        mBillModel.saveOrUpdateBill(mMainView.getViewContext(), defaultBill);
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMainView.initPreData();
        mMainView.initToolbar();
        mMainView.initView();
        mMainView.initData();
        mMainView.initListener();
    }

    @Override
    public void onViewResume() {
        if (mMainView.isDataInitFinish()) {
            if (AppConfig.isNeedToRefreshLocalSongDisplay(mMainView.getViewContext())) {
                mMainView.updateSongDisplay();
                AppConfig.setNeedToRefreshLocalSongDisplay(mMainView.getViewContext(), false);
            }

            if (AppConfig.isNeedToRefreshLocalBillDisplay(mMainView.getViewContext())) {
                mMainView.updateBillDisplay();
                AppConfig.setNeedToRefreshLocalBillDisplay(mMainView.getViewContext(), false);
            }

            if (AppConfig.isNeedToRefreshLocalAlbumDisplay(mMainView.getViewContext())) {
                mMainView.updateAlbumDisplay();
                AppConfig.setNeedToRefreshLocalAlbumDisplay(mMainView.getViewContext(), false);
            }
        }
    }

    @Override
    public void onViewWillDestroy() {

    }


    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {
        mMainView.showBillSelectionDialog(songEntity);
    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
        mMainView.goToAlbumDetail(mAlbumModel.getLocalAlbum(mMainView.getViewContext(), songEntity.getAlbumId()));
    }

    @Override
    public void onBottomSheetShareClick(LocalSongEntity songEntity) {
        mMainView.showToastShort("Share:" + songEntity.getTitle());
    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            mMainView.showSnackbarShort(mMainView.getSnackbarParent(), mMainView.getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(mMainView.getViewContext(), songEntity, billEntity);
//        mAppConfigModel.setNeedToRefreshLocalBillDisplay(mMainView.getViewContext(), true);
        mMainView.updateBillDisplay();
        mMainView.showSnackbarShort(mMainView.getSnackbarParent(), mMainView.getViewContext().getString(R.string.add_successfully));
    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {
        mMainView.showToastShort("Delete:" + songEntity.getTitle());
    }
}
