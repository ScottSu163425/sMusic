package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.ScreenUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.StatusBarUtil;
import com.su.scott.slibrary.util.ViewUtil;
import com.tbruyelle.rxpermissions2.Permission;

import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    private static final long DURATION_STAY = 3000;
    private TextView mAppNameTextView, mAppDescribeTextView, mCopyRightTextView;
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
    protected boolean subscribeEvents() {
        return false;
    }

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));

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
        mAppDescribeTextView = (TextView) findViewById(R.id.tv_app_describe_activity_splash);
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
                                requestPermission();
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
                    }

                    @Override
                    public void onAnimEnd() {
                        if (SdkUtil.isLolipopOrLatter()) {
                            TransitionManager.beginDelayedTransition((ViewGroup) getContentView());
                        }
                        ViewUtil.setViewVisiable(mAppDescribeTextView);

                        mCopyRightOutAnimator.setStartDelay(DURATION_STAY);
                        mCopyRightOutAnimator.start();
                    }
                });

        mCopyRightOutAnimator = AnimUtil.translateX(mCopyRightTextView, 0, ScreenUtil.getScreenWidth(this),
                AnimUtil.DURATION_NORMAL, new FastOutSlowInInterpolator(), new AnimUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        AnimUtil.alphaOut(mAppDescribeTextView,AnimUtil.DURATION_SHORT).start();
                        mAppNameOutAnimator.setStartDelay(AnimUtil.DURATION_NORMAL_HALF);
                        mAppNameOutAnimator.start();
                    }

                    @Override
                    public void onAnimEnd() {

                    }
                });
    }

    private void requestPermission() {
        getRxPermissionManager().requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.name.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            if (permission.granted) {
                                goTo(MainActivity.class);
                                finish();
                                overridePendingTransition(R.anim.in_right, R.anim.out_left);
                            } else {
                                if (permission.shouldShowRequestPermissionRationale) {
                                    showToastLong("你拒绝了权限" + permission.name + ",但这是应用必要的权限");
                                    requestPermission();
                                } else {
                                    showToastLong("你永久拒绝了权限" + permission.name + ",请前往设置界面开启");
                                    finish();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onBackPressed() {
        //Disable the back press;
    }

}
