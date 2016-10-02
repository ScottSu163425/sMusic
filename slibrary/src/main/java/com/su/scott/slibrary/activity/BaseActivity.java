package com.su.scott.slibrary.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.su.scott.slibrary.R;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.Snack;
import com.su.scott.slibrary.util.T;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private ProgressDialog mLoadingDialog;
    private String mNetworkErrorTip;
    private boolean mDestroyed;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mNetworkErrorTip = getString(R.string.network_error);

        mLoadingDialog = new ProgressDialog(this);
    }

    @Override
    public Activity getViewContext() {
        return this;
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
        T.showShort(this, msg);
    }

    @Override
    public void showToastLong(String msg) {
        T.showLong(this, msg);
    }

    @Override
    public void showSnackbarShort(View parent, String msg) {
        Snack.showShort(parent, msg);
    }

    @Override
    public void showSnackbarLong(View parent, String msg) {
        Snack.showLong(parent, msg);
    }

    @Override
    public void showSnackbarShort(View parent, String msg, String action, View.OnClickListener actionListener) {
        Snack.showShort(parent, msg, action, actionListener);
    }

    @Override
    public void showSnackbarLong(View parent, String msg, String action, View.OnClickListener actionListener) {
        Snack.showLong(parent, msg, action, actionListener);
    }

    @Override
    public void showNetworkError(View parent) {
        showSnackbarShort(parent, mNetworkErrorTip);
    }

    protected void setNetworkErrorTip(String mNetworkErrorTip) {
        this.mNetworkErrorTip = mNetworkErrorTip;
    }

    protected void goTo(Class destination) {
        if (SdkUtil.isLolipopOrLatter()) {
            startActivity(new Intent(this, destination), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(new Intent(this, destination));
        }
    }

    protected void goTo(Intent intent) {
        if (SdkUtil.isLolipopOrLatter()) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected void goToForResult(Intent intent, int requestCode) {
        if (SdkUtil.isLolipopOrLatter()) {
            startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    protected void goToWithSharedElement(Class destination, @NonNull View shareView, @NonNull String transitionName) {
        Intent intent = new Intent(this, destination);
        if (SdkUtil.isLolipopOrLatter()) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, shareView, transitionName);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected void goToWithSharedElement(Intent intent, @NonNull View shareView, @NonNull String transitionName) {
        if (SdkUtil.isLolipopOrLatter()) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, shareView, transitionName);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected void goToWithSharedElements(Intent intent, @NonNull View[] shareViews, @NonNull String[] transitionNames) {
        if (SdkUtil.isLolipopOrLatter()) {
            Pair<View, String>[] pairs = new Pair[shareViews.length];
            for (int i = 0; i < shareViews.length; i++) {
                pairs[i] = new Pair<>(shareViews[i], transitionNames[i]);
            }

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected boolean isActivityDestroyed() {
        return mDestroyed;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDestroyed = true;
    }
}
