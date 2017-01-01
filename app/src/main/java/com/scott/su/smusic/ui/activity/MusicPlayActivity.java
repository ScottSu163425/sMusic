package com.scott.su.smusic.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.MusicPlayMainFragmentCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.MusicPlayContract;
import com.scott.su.smusic.mvp.presenter.impl.MusicPlayPresenterImpl;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.MusicPlayMainFragment;
import com.scott.su.smusic.ui.fragment.MusicPlaySecondFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;

/**
 * 2016-09-07 22:01:51
 */
public class MusicPlayActivity extends BaseActivity<MusicPlayContract.MusicPlayView, MusicPlayContract.MusicPlayPresenter>
        implements MusicPlayContract.MusicPlayView, View.OnClickListener {
    private final int ID_CONTAINER = R.id.fl_container_music_play_main;

    private MusicPlayContract.MusicPlayPresenter mMusicPlayPresenter;
    private Toolbar mToolbar;
    private ImageView mBlurCoverImageView;
    private MusicPlayMainFragment mMusicPlayMainFragment;
    private MusicPlaySecondFragment mMusicPlaySecondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        mMusicPlayPresenter.onViewFirstTimeCreated();
    }

    @Override
    protected MusicPlayContract.MusicPlayPresenter getPresenter() {
        if (mMusicPlayPresenter == null) {
            mMusicPlayPresenter = new MusicPlayPresenterImpl(this);
        }
        return mMusicPlayPresenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_bill) {
            mMusicPlayPresenter.onAddToBillMenuItemClick();
        }
        return true;
    }

    @Override
    public void initPreData() {
    }

    @Override
    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_music_play);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
        mBlurCoverImageView = (ImageView) findViewById(R.id.iv_cover_blur_music_play);
    }

    @Override
    public void initData() {
        LocalSongEntity songEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_LOCAL_SONG);
        final ArrayList<LocalSongEntity> playingSongs = getIntent().getParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS);
        mMusicPlayMainFragment = MusicPlayMainFragment.newInstance(songEntity, playingSongs);

        mMusicPlaySecondFragment = new MusicPlaySecondFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(ID_CONTAINER, mMusicPlayMainFragment)
                .commitNow();
    }

    @Override
    public void initListener() {
        mMusicPlayMainFragment.setMusicPlayMainFragmentCallback(new MusicPlayMainFragmentCallback() {
            @Override
            public void onBlurCoverChanged(final Bitmap bitmap) {
                mMusicPlayPresenter.onBlurCoverChanged(bitmap);
            }

            @Override
            public void onCoverClick(View view) {
                mMusicPlayPresenter.onCoverClick(view);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void showBillSelectionDialog(final LocalSongEntity songEntity) {
        final LocalBillSelectionDialogFragment billSelectionDialogFragment = new LocalBillSelectionDialogFragment();
        billSelectionDialogFragment.setCallback(new LocalBillSelectionDialogFragment.BillSelectionCallback() {
            @Override
            public void onBillSelected(LocalBillEntity billEntity) {
                mMusicPlayPresenter.onAddToBillConfirmed(billEntity, songEntity);
                billSelectionDialogFragment.dismissAllowingStateLoss();
            }
        });
        billSelectionDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public LocalSongEntity getCurrentPlayingSong() {
        return mMusicPlayMainFragment.getCurrentPlayingSong();
    }

    @Override
    public void loadBlurCover(final Bitmap bitmap) {
        AnimUtil.alphaOut(mBlurCoverImageView, AnimUtil.DURATION_LONG, null, new AnimUtil.SimpleAnimListener() {
            @Override
            public void onAnimStart() {
            }

            @Override
            public void onAnimEnd() {
                mBlurCoverImageView.setImageBitmap(bitmap);
                AnimUtil.alphaIn(mBlurCoverImageView, AnimUtil.DURATION_LONG, null, null).start();
            }
        }).start();
    }

    @Override
    public void showMusicPlayMainFragment() {
        if (!mMusicPlayMainFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(ID_CONTAINER, mMusicPlayMainFragment)
                    .commitNow();
        } else if (!mMusicPlayMainFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .show(mMusicPlayMainFragment)
                    .commitNow();
        }
    }

    @Override
    public void hideMusicPlayMainFragment() {
        if (mMusicPlayMainFragment.isAdded() && mMusicPlayMainFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .hide(mMusicPlayMainFragment)
                    .commitNow();
        }
    }

    @Override
    public void showMusicPlaySecondFragment() {
        if (!mMusicPlaySecondFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(ID_CONTAINER, mMusicPlaySecondFragment)
                    .commitNow();
        } else if (!mMusicPlaySecondFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .show(mMusicPlaySecondFragment)
                    .commitNow();
        }
    }

    @Override
    public void hideMusicPlaySecondFragment() {
        if (mMusicPlaySecondFragment.isAdded() && mMusicPlaySecondFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .hide(mMusicPlaySecondFragment)
                    .commitNow();
        }
    }

    @Override
    public void onBackPressed() {
        if (mMusicPlaySecondFragment.isAdded() && mMusicPlaySecondFragment.isVisible()) {
            hideMusicPlaySecondFragment();
            showMusicPlayMainFragment();
        } else {
            mMusicPlayMainFragment.onActivityBackPressed();

            if (!mMusicPlayMainFragment.isSameSong()) {
                finish();
                overridePendingTransition(R.anim.in_alpha, R.anim.out_east);
            } else {
                if (SdkUtil.isLolipopOrLatter()) {
                    finishAfterTransition();
                } else {
                    finish();
                    overridePendingTransition(R.anim.in_alpha, R.anim.out_east);
                }
            }
        }
    }


}
