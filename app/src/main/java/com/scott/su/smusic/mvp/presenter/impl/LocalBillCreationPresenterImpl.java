package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.mvp.contract.LocalBillCreationContract;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2017/1/21.
 */

public class LocalBillCreationPresenterImpl
        extends BasePresenter<LocalBillCreationContract.LocalBillCreationView>
        implements LocalBillCreationContract.LocalBillCreationPresenter {

    private LocalBillModel mBillModel;

    public LocalBillCreationPresenterImpl(LocalBillCreationContract.LocalBillCreationView view) {
        super(view);

        mBillModel = new LocalBillModelImpl();
    }

    @Override
    public void onCreateBillConfirm(String billName) {
        LocalBillEntity billEntity = new LocalBillEntity(billName);

        if (mBillModel.isBillTitleExist(getView().getViewContext(), billEntity)) {
            getView().showSnackbarShort(getView().getViewContext().getString(R.string.error_already_exist));
            return;
        }

        mBillModel.saveOrUpdateBill(getView().getViewContext(), billEntity);
        AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
        getView().onCreateBillSuccessfully();
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initView();
        getView().initListener();
    }


}
