package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.MusicPlayContract;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayPresenterImpl extends BasePresenter<MusicPlayContract.MusicPlayView>
        implements MusicPlayContract.MusicPlayPresenter {
    private LocalBillModel mBillModel;


    public MusicPlayPresenterImpl(MusicPlayContract.MusicPlayView view) {
        super(view);
        this.mBillModel = new LocalBillModelImpl();
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
    public void onAddToBillMenuItemClick() {
        getView().showBillSelectionDialog(getView().getCurrentPlayingSong());
    }


    @Override
    public void onAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(getView().getViewContext(), songEntity, billEntity);
//        AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
        getView().showSnackbarShort(getView().getViewContext().getString(R.string.add_successfully));
    }


    @Override
    public void onBlurCoverChanged(Bitmap bitmap) {
        getView().loadBlurCover(bitmap);
    }

    @Override
    public void onCoverClick(View view) {
        getView().hideMusicPlayMainFragment();
        getView().showMusicPlaySecondFragment();
    }


}
