package com.scott.su.smusic.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private static final long DURATION_STATY = 1500;
    private TextView mAppNameTextView, mCopyRightTextView;
    private Animator mAppNameInAnimator, mAppNameOutAnimator, mCopyRightInAnimator, mCopyRightOutAnimator;


    @Override
    protected int getContentLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initView();
        initData();

        startAnim();
    }

    private void startAnim() {
        mAppNameInAnimator.setStartDelay(AnimUtil.DURATION_SHORT);
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
        mCopyRightTextView = (TextView) findViewById(R.id.tv_copy_right_activity_splash);

        ViewUtil.setViewInVisiable(mAppNameTextView);
        ViewUtil.setViewInVisiable(mCopyRightTextView);
    }

    @Override
    public void initData() {
        mAppNameInAnimator = AnimUtil.translateX(mAppNameTextView, ScreenUtil.getScreenWidth(this), 0,
                AnimUtil.DURATION_NORMAL, new FastOutSlowInInterpolator(), new AnimUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        ViewUtil.setViewVisiable(mAppNameTextView);
                    }

                    @Override
                    public void onAnimEnd() {
                        mCopyRightInAnimator.start();
                    }
                });

        mAppNameOutAnimator = AnimUtil.translateX(mAppNameTextView, 0, -ScreenUtil.getScreenWidth(this),
                AnimUtil.DURATION_NORMAL, new FastOutSlowInInterpolator(), new AnimUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        mAppNameTextView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                goTo(MainActivity.class);
                                finish();
                                overridePendingTransition(R.anim.in_east, R.anim.out_west);
                            }
                        }, AnimUtil.DURATION_NORMAL_HALF);
                    }

                    @Override
                    public void onAnimEnd() {

                    }
                });

        mCopyRightInAnimator = AnimUtil.translateX(mCopyRightTextView, -ScreenUtil.getScreenWidth(this), 0,
                AnimUtil.DURATION_NORMAL, new FastOutSlowInInterpolator(), new AnimUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        ViewUtil.setViewVisiable(mAppNameTextView);
                    }

                    @Override
                    public void onAnimEnd() {
                        mCopyRightOutAnimator.setStartDelay(DURATION_STATY);
                        mCopyRightOutAnimator.start();
                    }
                });

        mCopyRightOutAnimator = AnimUtil.translateX(mCopyRightTextView, 0, ScreenUtil.getScreenWidth(this),
                AnimUtil.DURATION_NORMAL, new FastOutSlowInInterpolator(), new AnimUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        mAppNameOutAnimator.setStartDelay(AnimUtil.DURATION_NORMAL_HALF);
                        mAppNameOutAnimator.start();
                    }

                    @Override
                    public void onAnimEnd() {

                    }
                });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onBackPressed() {

    }

}
