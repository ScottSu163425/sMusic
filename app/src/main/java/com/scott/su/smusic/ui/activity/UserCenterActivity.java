package com.scott.su.smusic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.mvp.contract.UserCenterContract;
import com.scott.su.smusic.mvp.presenter.impl.UserCenterPresenterImpl;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.SdkUtil;

public class UserCenterActivity extends BaseActivity<UserCenterContract.UserCenterView, UserCenterContract.UserCenterPresenter>
        implements UserCenterContract.UserCenterView {
    private UserCenterContract.UserCenterPresenter mPresenter;


    @Override
    protected int getContentLayoutResId() {
        return R.layout.activity_user_center;
    }

    @Override
    protected UserCenterContract.UserCenterPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new UserCenterPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void initPreData() {
        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_right));
        }
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_activity_user_center);
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCenterActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
