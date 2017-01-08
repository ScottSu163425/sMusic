package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.ScreenUtil;
import com.su.scott.slibrary.util.ViewUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

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

    private void requestPermission() {
        getRxPermissionManager().requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.name.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            if (permission.granted) {
                                goTo(MainActivity.class);
                                finish();
                                overridePendingTransition(R.anim.in_east, R.anim.out_west);
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
    }

}
