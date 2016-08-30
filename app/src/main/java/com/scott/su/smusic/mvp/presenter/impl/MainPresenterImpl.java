package com.scott.su.smusic.mvp.presenter.impl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.AppConfigModel;
import com.scott.su.smusic.mvp.model.LocalSongBillModel;
import com.scott.su.smusic.mvp.model.impl.AppConfigModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongBillModelImpl;
import com.scott.su.smusic.mvp.presenter.MainPresenter;
import com.scott.su.smusic.mvp.view.MainView;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mMainView;
    private LocalSongBillModel mBillModel;
    private AppConfigModel mConfigModel;


    public MainPresenterImpl(MainView mView) {
        this.mMainView = mView;
        this.mBillModel = new LocalSongBillModelImpl();
        this.mConfigModel = new AppConfigModelImpl();
    }

    @Override
    public void onFabClick() {
        mMainView.showCreateBillDialog();
    }

    @Override
    public void onCreateBillConfirm(String text) {
        LocalSongBillEntity billEntity = new LocalSongBillEntity(text);

        if (mBillModel.isBillTitleExist(mMainView.getViewContext(), billEntity)) {
            mMainView.showCreateBillUnsuccessfully(text + mMainView.getViewContext().getString(R.string.error_already_exist));
            return;
        }

        mBillModel.addBill(mMainView.getViewContext(), billEntity);
        mMainView.updateBillDisplay();
        mConfigModel.setNeedToRefreshLocalBillDisplay(mMainView.getViewContext(), false);
        mMainView.dismissCreateBillDialog();
        mMainView.showCreateBillSuccessfully(billEntity);
    }

    @Override
    public void onBillItemClick(View itemView, LocalSongBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        mMainView.goToBillDetailWithSharedElement(entity, sharedElements[0], transitionNames[0]);
    }

    @Override
    public void onSelectedLocalSongsResult(final LocalSongBillEntity billToAddSong, final List<LocalSongEntity> songsToAdd) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mMainView.showLoadingDialog(mMainView.getViewContext(), "请稍候..", false);
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
                mMainView.showToastShort("添加成功");
                mMainView.goToBillDetail(mBillModel.getBill(mMainView.getViewContext(), billToAddSong.getBillId()));
            }
        }.execute();

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
            if (mConfigModel.isNeedToRefreshLocalSongDisplay(mMainView.getViewContext())) {
                mMainView.updateSongDisplay();
                mConfigModel.setNeedToRefreshLocalSongDisplay(mMainView.getViewContext(), false);
            }

            if (mConfigModel.isNeedToRefreshLocalBillDisplay(mMainView.getViewContext())) {
                mMainView.updateBillDisplay();
                mConfigModel.setNeedToRefreshLocalBillDisplay(mMainView.getViewContext(), false);
            }

            if (mConfigModel.isNeedToRefreshLocalAlbumDisplay(mMainView.getViewContext())) {
                mMainView.updateAlbumDisplay();
                mConfigModel.setNeedToRefreshLocalAlbumDisplay(mMainView.getViewContext(), false);
            }
        }
    }

    @Override
    public void onViewWillDestroy() {

    }


}
