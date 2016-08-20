package com.su.scott.slibrary.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.view.View;

import com.su.scott.slibrary.util.Snack;
import com.su.scott.slibrary.util.T;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by Administrator on 2016/8/4.
 */
public class BaseFragment extends Fragment implements BaseView{

    private ProgressDialog mLoadingDialog;

    private String mNetworkErrorTip = DEFAULT_NETWORK_ERROR_TIP;

    private static final String DEFAULT_NETWORK_ERROR_TIP = "网络异常，请检查设备的网络连接状况";

    @Override
    public Activity getViewContext() {
        return getActivity();
    }
    @Override
    public void showLoadingDialog(String msg, boolean cancelable) {
        dismissLoadingDialog();
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showToastShort(String msg) {
        T.showShort(getActivity(), msg);
    }

    @Override
    public void showToastLong(String msg) {
        T.showLong(getActivity(), msg);
    }

    @Override
    public void showSnackbarShort(View parent,String msg) {
        Snack.showShort(parent, msg);
    }

    @Override
    public void showSnackbarLong(View parent,String msg) {
        Snack.showLong(parent, msg);
    }

    @Override
    public void showSnackbarShort(View parent,String msg, String action, View.OnClickListener actionListener) {
        Snack.showShort(parent, msg, action, actionListener);
    }

    @Override
    public void showSnackbarLong(View parent,String msg, String action, View.OnClickListener actionListener) {
        Snack.showLong(parent, msg, action, actionListener);
    }

    @Override
    public void showNetworkError(View parent) {
        showSnackbarShort(parent,mNetworkErrorTip);
    }

    protected void setNetworkErrorTip(String mNetworkErrorTip) {
        this.mNetworkErrorTip = mNetworkErrorTip;
    }


}
