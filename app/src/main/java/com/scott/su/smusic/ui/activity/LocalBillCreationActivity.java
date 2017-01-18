package com.scott.su.smusic.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.StatusBarUtil;
import com.su.scott.slibrary.util.ViewUtil;

public class LocalBillCreationActivity extends BaseActivity {

    private View mBackgroundView;
    private CardView mBodyCardView;
    private FloatingActionButton mFAB;
    private LinearLayout mBodyLayout;
    private boolean mAnimating;
    private boolean mExiting;   //To handle onBackPressed.

    @Override
    protected int getContentLayoutResId() {
        return R.layout.activity_local_bill_creation;
    }

    @Override
    protected IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initView();
        initListener();
    }

    @Override
    public void initPreData() {

    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView() {
        mBackgroundView = findViewById(R.id.view_background_activity_local_bill_creation);
        mBodyCardView = (CardView) findViewById(R.id.card_body_activity_local_bill_creation);
        mFAB = (FloatingActionButton) findViewById(R.id.fab_activity_local_bill_creation);
        mBodyLayout = (LinearLayout) findViewById(R.id.ll_body_activity_local_bill_creation);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.changebounds_with_arcmotion));

            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    mAnimating = true;

                    if (mExiting) {

                    } else {
                        ViewUtil.runDelay(mFAB, new Runnable() {
                            @Override
                            public void run() {
                                AnimUtil.scaleOut(mFAB);
                            }
                        }, AnimUtil.DURATION_SHORT_HALF);
                    }
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    CirclarRevealUtil.revealIn(mBodyCardView,
                            CirclarRevealUtil.DIRECTION.CENTER,
                            AnimUtil.DURATION_SHORT,
                            null,
                            new AnimUtil.SimpleAnimListener() {
                                @Override
                                public void onAnimStart() {

                                }

                                @Override
                                public void onAnimEnd() {
                                    CirclarRevealUtil.revealIn(mBodyLayout,
                                            CirclarRevealUtil.DIRECTION.CENTER_TOP,
                                            AnimUtil.DURATION_SHORT,
                                            null,
                                            new AnimUtil.SimpleAnimListener() {
                                                @Override
                                                public void onAnimStart() {

                                                }

                                                @Override
                                                public void onAnimEnd() {
                                                    mAnimating = false;
                                                }
                                            });
                                }
                            });
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }

        mBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBillCreationActivity.this.onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        closeKeyboard();

        if (ViewUtil.isFastClick()) {
            return;
        }

        if (mAnimating) {
            return;
        }

        if (mExiting) {
            super.onBackPressed();
        }

        if (SdkUtil.isLolipopOrLatter()) {
            CirclarRevealUtil.revealOut(mBodyLayout, CirclarRevealUtil.DIRECTION.CENTER_TOP,
                    AnimUtil.DURATION_SHORT,
                    new FastOutLinearInInterpolator(),
                    new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {

                        }

                        @Override
                        public void onAnimEnd() {
                            CirclarRevealUtil.revealOut(mBodyCardView, CirclarRevealUtil.DIRECTION.CENTER,
                                    AnimUtil.DURATION_SHORT, null,
                                    new AnimUtil.SimpleAnimListener() {
                                        @Override
                                        public void onAnimStart() {

                                        }

                                        @Override
                                        public void onAnimEnd() {
                                            AnimUtil.scaleIn(mFAB, AnimUtil.DURATION_SHORT_HALF,
                                                    new OvershootInterpolator(),
                                                    new AnimUtil.SimpleAnimListener() {
                                                        @Override
                                                        public void onAnimStart() {

                                                        }

                                                        @Override
                                                        public void onAnimEnd() {
                                                            mExiting = true;
                                                            LocalBillCreationActivity.this.onBackPressed();
                                                        }
                                                    });
                                        }
                                    }, true);
                        }
                    },
                    true);
        } else {
            super.onBackPressed();
        }

    }

}
