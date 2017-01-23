package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.LocalAlbumDetailContract;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/9/19.
 */
public class LocalAlbumDetailPresenterImpl extends BasePresenter<LocalAlbumDetailContract.LocalAlbumDetailView>
        implements LocalAlbumDetailContract.ILocalAlbumDetailPresenter {
    private LocalAlbumModel mAlbumModel;
    private LocalBillModel mBillModel;

    public LocalAlbumDetailPresenterImpl(LocalAlbumDetailContract.LocalAlbumDetailView view) {
        super(view);
        this.mAlbumModel = new LocalAlbumModelImpl();
        this.mBillModel = new LocalBillModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
        getView().initToolbar();
        getView().initView();
        getView().initData();
        getView().initListener();

        getView().loadAlbumCover(mAlbumModel.getAlbumCoverPathByAlbumId(getView().getViewContext(),
                getView().getCurrentAlbumEntity().getAlbumId()));
    }

    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {
        getView().showBillSelectionDialog(songEntity);
    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(getView().getViewContext(), songEntity, billEntity);
        getView().notifyLocalBillChanged();
        getView().showSnackbarShort(getView().getViewContext().getString(R.string.add_successfully));
    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {

    }

    @Override
    public void onAlbumSongItemClick(View view, int position, LocalSongEntity entity) {
        if (position == 0) {
            getView().goToMusicPlayWithCover(entity);
        } else {
            getView().goToMusicPlay(entity);
        }
    }

    @Override
    public void onAlbumSongItemMoreClick(View view, int position, LocalSongEntity entity) {
        getView().showAlbumSongBottomSheet(entity);
    }

    @Override
    public void onPlayFabClick() {
        getView().goToMusicPlayWithCover(getView().getCurrentAlbumEntity().getAlbumSongs().get(0));
    }
}
