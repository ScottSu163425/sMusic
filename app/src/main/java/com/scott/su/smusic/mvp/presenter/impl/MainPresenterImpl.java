package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.mvp.model.LocalSongBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalSongBillModelImpl;
import com.scott.su.smusic.mvp.presenter.MainPresenter;
import com.scott.su.smusic.mvp.view.MainView;

/**
 * Created by asus on 2016/8/19.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mView;
    private LocalSongBillModel mBillModel;


    public MainPresenterImpl(MainView mView) {
        this.mView = mView;
        mBillModel = new LocalSongBillModelImpl();
    }

    @Override
    public void onFabClick() {
        mView.showCreateBillDialog();
    }

    @Override
    public void onCreateBillConfirm(String text) {
        LocalSongBillEntity billEntity = new LocalSongBillEntity(text);

        if (mBillModel.isBillTitleExist(mView.getViewContext(), billEntity)) {
            mView.showToastShort(text + "已经存在");
            return;
        }

        mBillModel.addBill(mView.getViewContext(), billEntity);
        mView.updateBillDisplay();
        mView.dismissCreateBillDialog();
        mView.scrollBillToLast();
        mView.showToastShort("添加成功");
    }

    @Override
    public void onViewFirstTimeCreated() {

    }

    @Override
    public void onViewWillDestroy() {

    }
}
