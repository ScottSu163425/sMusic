package com.scott.su.smusic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.T;

public class SettingsActivity extends BaseActivity {

    @Override
    protected int getContentLayoutResId() {
        return R.layout.activity_settings;
    }

    @Override
    protected IPresenter getPresenter() {
        return null;
    }

    @Override
    protected boolean subscribeEvents() {
        return false;
    }

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initPreData();
        initToolbar();
    }

    @Override
    public void initPreData() {
        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_right));
        }
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_activity_settings);
        toolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.onBackPressed();
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

    public void onMenuItemClick(View view) {
        T.showShort(this, "敬请期待..");
    }

}
