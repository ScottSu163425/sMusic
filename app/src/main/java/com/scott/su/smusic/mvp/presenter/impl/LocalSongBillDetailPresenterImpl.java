package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalSongBillModelImpl;
import com.scott.su.smusic.mvp.presenter.LocalSongBillDetailPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBillDetailView;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public class LocalSongBillDetailPresenterImpl implements LocalSongBillDetailPresenter {
    private LocalSongBillDetailView mBillDetailView;
    private LocalSongBillModel mBillModel;

    public LocalSongBillDetailPresenterImpl(LocalSongBillDetailView mBillDetailView) {
        this.mBillDetailView = mBillDetailView;
        this.mBillModel = new LocalSongBillModelImpl();
    }

    @Override
    public void onAddSongsMenuClick() {
        mBillDetailView.goToLocalSongSelectionActivity();
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
            mBillDetailView.showAddSongsSuccessfully(mBillDetailView.getViewContext().getString(R.string.success_add_songs_to_bill));
            mBillDetailView.refreshBillCover(mBillModel.getBill(mBillDetailView.getViewContext(),
                    billToAddSong.getBillId()));
            mBillDetailView.refreshBillSongDisplay(mBillModel.getBill(mBillDetailView.getViewContext(),
                    billToAddSong.getBillId()));
        } else {
            //More than one song was selected to be added into current bill;
            if (mBillModel.isBillContainsAll(billToAddSong, songsToAdd)) {
                mBillDetailView.showAddSongsUnsuccessfully(mBillDetailView.getViewContext().getString(R.string.error_already_exist));
                return;
            }
            mBillModel.addSongsToBill(mBillDetailView.getViewContext(), songsToAdd, billToAddSong);
            mBillDetailView.showAddSongsSuccessfully(mBillDetailView.getViewContext().getString(R.string.success_add_songs_to_bill));
            mBillDetailView.refreshBillCover(mBillModel.getBill(mBillDetailView.getViewContext(),
                    billToAddSong.getBillId()));
            mBillDetailView.refreshBillSongDisplay(mBillModel.getBill(mBillDetailView.getViewContext(),
                    billToAddSong.getBillId()));
        }
    }


    @Override
    public void onViewFirstTimeCreated() {
        mBillDetailView.initPreData();
        mBillDetailView.initToolbar();
        mBillDetailView.initView();
        mBillDetailView.initData();
        mBillDetailView.initListener();

        if (mBillDetailView.getBillEntity().isBillEmpty()) {
            mBillDetailView.hideFab();
        } else {
            mBillDetailView.showFab();
        }
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }


}
