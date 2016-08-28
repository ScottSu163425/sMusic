package com.su.scott.slibrary.view;

import android.app.Activity;
import android.view.View;

import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface BaseView {

    Activity getViewContext();

    void showLoadingDialog(Activity activity,String msg, boolean cancelable);

    void dismissLoadingDialog();

    void showToastShort(String msg);

    void showToastLong(String msg);

    void showSnackbarShort(View parent,String msg);

    void showSnackbarLong(View parent,String msg);

    void showSnackbarShort(View parent,String msg, String action, View.OnClickListener actionListener);

    void showSnackbarLong(View parent,String msg, String action, View.OnClickListener actionListener);

    void showNetworkError(View parent);

}
