package com.scott.su.smusic.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.ViewUtil;

public class LocalBillCreationActivity extends AppCompatActivity {

    private View mBackgroundView;
    private CardView mBodyCardView;
    private FloatingActionButton mFAB;
    private LinearLayout mBodyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_bill_creation);

        mBackgroundView = findViewById(R.id.view_background_activity_local_bill_creation);
        mBodyCardView = (CardView) findViewById(R.id.card_body_activity_local_bill_creation);
        mFAB = (FloatingActionButton) findViewById(R.id.fab_activity_local_bill_creation);
        mBodyLayout = (LinearLayout) findViewById(R.id.ll_body_activity_local_bill_creation);

        mBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBillCreationActivity.this.onBackPressed();
            }
        });

        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.changebounds_with_arcmotion));

            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

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
                                    AnimUtil.alphaIn(mBodyLayout).start();
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
    }


}
