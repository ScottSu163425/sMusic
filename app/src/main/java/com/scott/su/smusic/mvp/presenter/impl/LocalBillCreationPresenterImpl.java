package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.contract.LocalBillCreationContract;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2016/12/31.
 */

public class LocalBillCreationPresenterImpl extends BasePresenter<LocalBillCreationContract.LocalBillCreationView>
        implements LocalBillCreationContract.LocalBillCreationPresenter {

    public LocalBillCreationPresenterImpl(LocalBillCreationContract.LocalBillCreationView view) {
        super(view);
    }

    @Override
    public void onViewFirstTimeCreated() {

    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }
}
