package com.scott.su.smusic.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.ScreenUtil;
import com.su.scott.slibrary.util.ViewUtil;

public class SplashActivity extends BaseActivity {
    private TextView mAppNameTextView;
    private Animator mAppNameInAnimator, mAppNameOutAnimator, mCopyRightInAnimator, mCopyRightOutAnimator;


    @Override
    protected IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();

        mAppNameInAnimator.start();
    }

    @Override
    public void initPreData() {

    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView() {
        mAppNameTextView = (TextView) findViewById(R.id.tv_app_name_activity_splash);

        ViewUtil.setViewInVisiable(mAppNameTextView);
    }

    @Override
    public void initData() {
        mAppNameInAnimator = AnimUtil.translateX(mAppNameTextView, ScreenUtil.getScreenWidth(this), 0,
                AnimUtil.DURATION_NORMAL, new LinearOutSlowInInterpolator(), new AnimUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        ViewUtil.setViewVisiable(mAppNameTextView);
                    }

                    @Override
                    public void onAnimEnd() {
                        mAppNameOutAnimator.start();
                    }
                });

        mAppNameOutAnimator = AnimUtil.translateX(mAppNameTextView, 0, -ScreenUtil.getScreenWidth(this),
                AnimUtil.DURATION_NORMAL, new FastOutSlowInInterpolator(), new AnimUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                    }

                    @Override
                    public void onAnimEnd() {
                        goTo(MainActivity.class);
                        finish();
                        overridePendingTransition(R.anim.in_east, R.anim.out_west);
                    }
                });
    }

    @Override
    public void initListener() {

    }


}
