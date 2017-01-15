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
import com.scott.su.smusic.mvp.contract.MainContract;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.util.MusicPlayUtil;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;
import com.su.scott.slibrary.util.TimeUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class MainPresenterImpl extends BasePresenter<MainContract.MainView>
        implements MainContract.MainPresenterI {
    private LocalSongModel mSongModel;
    private LocalBillModel mBillModel;
    private LocalAlbumModel mAlbumModel;

    public MainPresenterImpl(MainContract.MainView mView) {
        super(mView);
        this.mSongModel = new LocalSongModelImpl();
        this.mBillModel = new LocalBillModelImpl();
        this.mAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onInitDataComplete() {
        getView().bindMusicPlayService();
        getView().bindShutDownTimerService();
    }

    @Override
    public void onLocalSongItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        getView().goToMusicWithSharedElement(entity, sharedElements[0], transitionNames[0]);
    }

    @Override
    public void onLocalSongItemMoreClick(LocalSongEntity songEntity) {
        getView().showLocalSongBottomSheet(songEntity);
    }

    @Override
    public void onFabClick() {
        if (getView().isCurrentTabSong()) {
            if (getView().getDisplaySongs() == null || getView().getDisplaySongs().isEmpty()) {
                getView().showSnackbarShort(getView().getViewContext().getString(R.string.empty_local_song));
                return;
            }

            if (getView().getServiceCurrentPlayingSong() == null || getView().isFabPlayRandom()) {
                //Has not played any song,play random;
                getView().playRandomSong();
            } else {
                //A song is playing or paused;
                final int currentPlayingSongPositon = MusicPlayUtil.getSongPosition(getView().getServiceCurrentPlayingSong(), getView().getDisplaySongs());
                getView().playSongInPosition(currentPlayingSongPositon, true);
            }
        } else if (getView().isCurrentTabBill()) {
            getView().showCreateBillDialog();
        } else if (getView().isCurrentTabAlbum()) {

        }

    }

    @Override
    public void onFabLongClick() {
        if (getView().isCurrentTabSong()) {
            if (getView().isFabPlayRandom()) {
                getView().setFabPlayCurrent();
            } else {
                getView().setFabPlayRandom();
            }
        }
    }

    @Override
    public void onCreateBillConfirm(String text) {
        LocalBillEntity billEntity = new LocalBillEntity(text);

        if (mBillModel.isBillTitleExist(getView().getViewContext(), billEntity)) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.error_already_exist));
            return;
        }

        mBillModel.saveOrUpdateBill(getView().getViewContext(), billEntity);
        getView().updateBillDisplay();
        AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), false);
        getView().dismissCreateBillDialog();
        getView().showCreateBillSuccessfully(billEntity);
    }

    @Override
    public void onBillItemClick(View itemView, LocalBillEntity entity, int position,
                                @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        getView().goToBillDetailWithSharedElement(entity, sharedElements[0], transitionNames[0]);
    }

    @Override
    public void onAlbumItemClick(View itemView, LocalAlbumEntity entity, int position,
                                 @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        getView().goToAlbumDetailWithSharedElement(entity, sharedElements[0], transitionNames[0]);
    }

    @Override
    public void onSelectedLocalSongsResult(final LocalBillEntity billToAddSong,
                                           final List<LocalSongEntity> songsToAdd) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().showLoadingDialog(getView().getViewContext(), getView().getViewContext().getString(R.string.please_waiting), false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                mBillModel.addSongsToBill(getView().getViewContext(), songsToAdd, billToAddSong);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isViewAttaching()) {
                    return;
                }

                getView().updateBillDisplay();
                getView().dismissLoadingDialog();
                getView().showToastShort(getView().getViewContext().getString(R.string.add_successfully));
                getView().goToBillDetail(mBillModel.getBill(getView().getViewContext(), billToAddSong.getBillId()));
            }
        }.execute();

    }

    @Override
    public void onDrawerUserHeadClick(View sharedElement, String transitionName) {
        getView().goToUserCenter(sharedElement, transitionName);
    }

    @Override
    public void onDrawerMenuUserCenterClick(View v, View sharedElement, String transitionName) {
        getView().goToUserCenter(sharedElement, transitionName);
    }

    @Override
    public void onDrawerMenuNightModeOn() {
        if (AppConfig.isNightModeOn(getView().getViewContext())) {
            return;
        }
        AppConfig.setNightMode(getView().getViewContext(), true);
        getView().turnOnNightMode();
    }

    @Override
    public void onDrawerMenuNightModeOff() {
        if (!AppConfig.isNightModeOn(getView().getViewContext())) {
            return;
        }
        AppConfig.setNightMode(getView().getViewContext(), false);
        getView().turnOffNightMode();
    }

    @Override
    public void onDrawerMenuLanguageModeOn() {
        if (AppConfig.isLanguageModeOn(getView().getViewContext())) {
            return;
        }
        AppConfig.setLanguageMode(getView().getViewContext(), true);
        getView().turnOnLanguageMode();
        updateDefaultBillName();
    }

    @Override
    public void onDrawerMenuLanguageModeOff() {
        if (!AppConfig.isLanguageModeOn(getView().getViewContext())) {
            return;
        }
        AppConfig.setLanguageMode(getView().getViewContext(), false);
        getView().turnOffLanguageMode();
        updateDefaultBillName();
    }

    @Override
    public void onDrawerMenuSettingsClick(View v) {
        getView().goToSettings();
    }

    @Override
    public void onDrawerMenuTimerCancelClick() {
        getView().cancelShutDownTimer();
    }

    @Override
    public void onDrawerMenuTimerMinutesClick(long millisOfmin) {
        getView().startShutDownTimer(millisOfmin, TimeUtil.MILLISECONDS_OF_SECOND);
    }

    @Override
    public void onDrawerMenuStatisticClick(View v) {
        getView().goToPlayStatistic();
    }


    private void updateDefaultBillName() {
        LocalBillEntity defaultBill = mBillModel.getDefaultBill(getView().getViewContext());
        defaultBill.setBillTitle(getView().getViewContext().getString(R.string.my_favourite));
        mBillModel.saveOrUpdateBill(getView().getViewContext(), defaultBill);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
        getView().initToolbar();
        getView().initView();
        getView().initData();
        getView().initListener();
    }

    @Override
    public void onViewResume() {
        if (getView().isInitDataComplete()) {
            if (AppConfig.isNeedToRefreshLocalSongDisplay(getView().getViewContext())) {
                getView().updateSongDisplay();
                AppConfig.setNeedToRefreshLocalSongDisplay(getView().getViewContext(), false);
            }

            if (AppConfig.isNeedToRefreshLocalBillDisplay(getView().getViewContext())) {
                getView().updateBillDisplay();
                AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), false);
            }

            if (AppConfig.isNeedToRefreshLocalAlbumDisplay(getView().getViewContext())) {
                getView().updateAlbumDisplay();
                AppConfig.setNeedToRefreshLocalAlbumDisplay(getView().getViewContext(), false);
            }
        }
    }

    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {
        getView().showBillSelectionDialog(songEntity);
    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
        getView().goToAlbumDetail(mAlbumModel.getLocalAlbum(getView().getViewContext(), songEntity.getAlbumId()));
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
//        mAppConfigModel.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
        getView().updateBillDisplay();
        getView().showSnackbarShort(getView().getViewContext().getString(R.string.add_successfully));
    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {
        getView().showLoadingDialog(getView().getViewContext(), false);

        if (mSongModel.deleteLocalSong(getView().getViewContext(), songEntity.getSongId())) {
            //To update info of music play service;
            getView().removeServiceSong(songEntity);
            //To update info of bill;
            mBillModel.deleteSong(getView().getViewContext(), songEntity);
            getView().dismissLoadingDialog();
            getView().showToastShort(getView().getViewContext().getString(R.string.delete_local_song_successfully));
            getView().updateSongDisplay();
            getView().updateBillDisplay();
            getView().updateAlbumDisplay();
        } else {
            getView().showToastShort(getView().getViewContext().getString(R.string.delete_local_song_unsuccessfully));
            getView().dismissLoadingDialog();
        }
    }
}
