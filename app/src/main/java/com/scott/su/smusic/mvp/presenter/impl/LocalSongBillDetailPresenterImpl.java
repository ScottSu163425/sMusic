package com.scott.su.smusic.mvp.presenter.impl;

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
            LocalSongEntity songToAdd = songsToAdd.get(0);
            if (mBillModel.isBillContains(billToAddSong, songToAdd)) {
                mBillDetailView.showAddSongsUnsuccessfully(songToAdd.getTitle()
                        + "在 " + billToAddSong.getBillTitle() + " 中已存在");
            } else {
                mBillModel.addSongToBill(mBillDetailView.getViewContext(), songToAdd, billToAddSong);
                mBillDetailView.showAddSongsSuccessfully("添加成功");
                mBillDetailView.refreshBillCover(mBillModel.getBill(mBillDetailView.getViewContext(),
                        billToAddSong.getBillId()));
                mBillDetailView.refreshBillSongDisplay();
            }
        }
    }


    @Override
    public void onViewFirstTimeCreated() {
        mBillDetailView.initPreData();
        mBillDetailView.initToolbar();
        mBillDetailView.initView();
        mBillDetailView.initData();
        mBillDetailView.initListener();
    }

    @Override
    public void onViewWillDestroy() {

    }


}
