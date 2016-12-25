package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.PermissionUtil;

public class SplashActivity extends BaseActivity {
    private View mBackgroundLayout;
    private TextView mAppNameTextView;

    @Override
    protected IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBackgroundLayout = findViewById(R.id.view_background_root_activity_splash);
        mAppNameTextView = (TextView) findViewById(R.id.tv_app_name_activity_splash);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_east);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(AnimUtil.DURATION_SHORT);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                CirclarRevealUtil.revealIn(mBackgroundLayout, CirclarRevealUtil.DIRECTION.CENTER);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mAppNameTextView.startAnimation(animation);

    }

    @Override
    public void initPreData() {

    }

    @Override
    public void initToolbar() {

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
