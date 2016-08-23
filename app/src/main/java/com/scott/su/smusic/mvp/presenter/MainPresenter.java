package com.scott.su.smusic.mvp.presenter;

import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainPresenter extends BasePresenter {

    void onFabClick();

    void onCreateBillConfirm(String text);

}
