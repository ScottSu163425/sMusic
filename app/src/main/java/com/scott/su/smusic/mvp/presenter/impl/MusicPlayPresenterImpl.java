package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.view.MusicPlayView;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayPresenterImpl implements MusicPlayPresenter {
    private MusicPlayView mMusicPlayView;
    private LocalBillModel mBillModel;


    public MusicPlayPresenterImpl(MusicPlayView mMusicPlayView) {
        this.mMusicPlayView = mMusicPlayView;
        this.mBillModel = new LocalBillModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMusicPlayView.initPreData();
        mMusicPlayView.initToolbar();
        mMusicPlayView.initView();
        mMusicPlayView.initData();
        mMusicPlayView.initListener();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
        if (mMusicPlayView != null) {
            mMusicPlayView = null;
        }
    }

    @Override
    public void onAddToBillMenuItemClick() {
        mMusicPlayView.showBillSelectionDialog(mMusicPlayView.getCurrentPlayingSong());
    }


    @Override
    public void onAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            mMusicPlayView.showSnackbarShort(mMusicPlayView.getSnackbarParent(),
                    mMusicPlayView.getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(mMusicPlayView.getViewContext(), songEntity, billEntity);
        AppConfig.setNeedToRefreshLocalBillDisplay(mMusicPlayView.getViewContext(), true);
        mMusicPlayView.showSnackbarShort(mMusicPlayView.getSnackbarParent(), mMusicPlayView.getViewContext().getString(R.string.add_successfully));
    }


    @Override
    public void onBlurCoverChanged(Bitmap bitmap) {
        mMusicPlayView.loadBlurCover(bitmap);
    }

    @Override
    public void onCoverClick(View view) {
        mMusicPlayView.hideMusicPlayMainFragment();
        mMusicPlayView.showMusicPlaySecondFragment();
    }


}
