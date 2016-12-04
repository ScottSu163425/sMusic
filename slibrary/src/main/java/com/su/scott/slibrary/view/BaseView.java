package com.su.scott.slibrary.view;

import android.app.Activity;
import android.view.View;

import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface BaseView {

    boolean isNetworkConnected();

    boolean checkNetworkSnack();

    boolean checkNetworkToast();

    Activity getViewContext();

    View getSnackbarParent();

    void initPreData();

    void initToolbar();

    void initView();

    void initData();

    void initListener();

    void showLoadingDialog(Activity activity);

    void showLoadingDialog(Activity activity, boolean cancelable);

    void showLoadingDialog(Activity activity, String msg, boolean cancelable);

    void dismissLoadingDialog();

    void showToastShort(String msg);

    void showToastLong(String msg);

    void showSnackbarShort(String msg);

    void showSnackbarLong(String msg);

    void showSnackbarShort(String msg, String action, View.OnClickListener actionListener);

    void showSnackbarLong(String msg, String action, View.OnClickListener actionListener);

    void showNetworkErrorSnack( );

    void showNetworkErrorToast( );
}
