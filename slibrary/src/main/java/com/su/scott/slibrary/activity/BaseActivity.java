package com.su.scott.slibrary.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.su.scott.slibrary.R;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;
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
public abstract class BaseActivity<V extends IView, P extends BasePresenter<V>> extends AppCompatActivity implements IBaseView {
//    private P mPresenter;
    private ProgressDialog mLoadingDialog;
    private boolean mDestroyed;

//    protected abstract P getPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

//        mPresenter = getPresenter();
        mLoadingDialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }

//        getPresenter().detachView();
//        mPresenter = null;

        mDestroyed = true;
    }

    @Override
    public void finishView(boolean hasTransition) {
        if (hasTransition) {
            if (SdkUtil.isLolipopOrLatter()) {
                finishAfterTransition();
            } else {
                finish();
            }
        } else {
            finish();

            if (SdkUtil.isLolipopOrLatter()) {
                overridePendingTransition(R.anim.in_alpha, R.anim.out_east);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();

        if (!SdkUtil.isLolipopOrLatter()) {
            overridePendingTransition(R.anim.in_alpha, R.anim.out_east);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

        if (!SdkUtil.isLolipopOrLatter()) {
            overridePendingTransition(R.anim.in_east, R.anim.out_alpha);
        }
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

    protected View getContentView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public View getSnackbarParent() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
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
        showSnackbarShort(getString(R.string.network_error));
    }

    protected void goTo(Class destination) {
        startActivity(new Intent(this, destination));
    }

    protected void goTo(Intent intent) {
        startActivity(intent);
    }

    protected void goToWithTransition(Class destination) {
        if (SdkUtil.isLolipopOrLatter()) {
            startActivity(new Intent(this, destination), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(new Intent(this, destination));
        }
    }

    protected void goToWithTransition(Intent intent) {
        if (SdkUtil.isLolipopOrLatter()) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected void goToForResultWithTransition(Intent intent, int requestCode) {
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
    public boolean isNetworkConnected() {
        return NetworkUtil.isNetworkConnected(this);
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
    public boolean checkNetworkToast() {
        if (isNetworkConnected()) {
            return true;
        }
        showNetworkErrorToast();
        return false;
    }

    @Override
    public void showNetworkErrorToast() {
        showToastShort(getString(R.string.network_error));
    }

    @Override
    public String getStringByResId(@StringRes int id) {
        return getString(id);
    }

    @Override
    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
