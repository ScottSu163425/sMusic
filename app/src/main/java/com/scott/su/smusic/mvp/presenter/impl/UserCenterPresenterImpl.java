package com.scott.su.smusic.mvp.presenter.impl;

import com.scott.su.smusic.mvp.contract.UserCenterContract;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2017/1/8.
 */

public class UserCenterPresenterImpl extends BasePresenter<UserCenterContract.UserCenterView>
        implements UserCenterContract.UserCenterPresenter {

    public UserCenterPresenterImpl(UserCenterContract.UserCenterView view) {
        super(view);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
        getView().initToolbar();
        getView().initView();
        getView().initData();
        getView().initListener();
    }


}
