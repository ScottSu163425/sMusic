package com.su.scott.slibrary.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.su.scott.slibrary.R;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;
import com.su.scott.slibrary.mvp.view.IView;
import com.su.scott.slibrary.util.NetworkUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.Snack;
import com.su.scott.slibrary.util.T;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseFragment<V extends IView, P extends IPresenter<V>> extends Fragment
        implements IBaseView {
    private P mPresenter;
    private ProgressDialog mLoadingDialog;
    private String mNetworkErrorTip;
    private boolean mFirstTimeCreateView = true;

    protected abstract P getPresenter();

    protected abstract void onFirstTimeCreateView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = getPresenter();
        mNetworkErrorTip = getString(R.string.network_error);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirstTimeCreateView) {
            onFirstTimeCreateView();
            mFirstTimeCreateView = false;
        }
    }

    @Override
    public void onDestroy() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }

        if (mPresenter != null) {
            getPresenter().detachView();
            mPresenter = null;
        }

        super.onDestroy();
    }

    @Override
    public void finishView(boolean hasTransition) {
        if (hasTransition) {
            if (SdkUtil.isLolipopOrLatter()) {
                getActivity().finishAfterTransition();
            } else {
                getActivity().finish();
            }
        } else {
            getActivity().finish();

            if (SdkUtil.isLolipopOrLatter()) {
                getActivity().overridePendingTransition(R.anim.in_alpha, R.anim.out_right);
            }
        }
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public Activity getViewContext() {
        return getActivity();
    }

    @Override
    public void showLoadingDialog(Activity activity) {
        showLoadingDialog(activity, true);
    }

    @Override
    public void showLoadingDialog(Activity activity, boolean cancelable) {
        showLoadingDialog(activity, getString(R.string.please_waiting), cancelable);
    }

    @Override
    public void showLoadingDialog(Activity activity, String msg, boolean cancelable) {
        dismissLoadingDialog();
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(activity);
        }
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
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
    public void showSnackbarShort(String msg) {
        Snack.showShort(getSnackbarParent(), msg);
    }

    @Override
    public void showSnackbarLong(String msg) {
        Snack.showLong(getSnackbarParent(), msg);
    }

    @Override
    public void showSnackbarShort(String msg, String action, View.OnClickListener actionListener) {
        Snack.showShort(getSnackbarParent(), msg, action, actionListener);
    }

    @Override
    public void showSnackbarLong(String msg, String action, View.OnClickListener actionListener) {
        Snack.showLong(getSnackbarParent(), msg, action, actionListener);
    }

    @Override
    public void showNetworkErrorSnack() {
        showSnackbarShort(mNetworkErrorTip);
    }

    protected void goTo(Class destination) {
        startActivity(new Intent(getActivity(), destination));
    }

    protected void goTo(Intent intent) {
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void goToWithTransition(Class destination) {
        startActivity(new Intent(getActivity(), destination),
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void goToWithTransition(Intent intent) {
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void goToWithSharedElement(Class destination, @NonNull View shareView, @NonNull String transitionName) {
        Intent intent = new Intent(getActivity(), destination);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(getActivity(), shareView, transitionName);
        startActivity(intent, options.toBundle());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void goToWithSharedElement(Intent intent, @NonNull View shareView, @NonNull String transitionName) {
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(getActivity(), shareView, transitionName);
        startActivity(intent, options.toBundle());
    }

    @Override
    public View getSnackbarParent() {
        return getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public boolean isNetworkConnected() {
        return (NetworkUtil.isNetworkConnected(getActivity()) || NetworkUtil.isNetworkConnected(getActivity()));
    }

    @Override
    public boolean checkNetworkSnack() {
        if (isNetworkConnected()) {
            return true;
        }
        showNetworkErrorSnack();
        return false;
    }

    @Override
    public void showNetworkErrorToast() {
        showToastShort(getString(R.string.network_error));
    }


    @Override
    public boolean checkNetworkToast() {
        if (isNetworkConnected()) {
            return true;
        }
        showNetworkErrorToast();
        return false;
    }

    @Override
    public String getStringByResId(@StringRes int id) {
        return getString(id);
    }

    @Override
    public void closeKeyboard() {
        View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
