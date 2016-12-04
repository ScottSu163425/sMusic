package com.scott.su.smusic.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.MusicPlayMainFragmentCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.MusicPlayPresenterImpl;
import com.scott.su.smusic.mvp.view.MusicPlayView;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.MusicPlayMainFragment;
import com.scott.su.smusic.ui.fragment.MusicPlaySecondFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.SdkUtil;

import java.util.ArrayList;

/**
 * 2016-09-07 22:01:51
 */
public class MusicPlayActivity extends BaseActivity implements MusicPlayView, View.OnClickListener {
    private final int ID_CONTAINER = R.id.fl_container_music_play_main;

    private MusicPlayPresenter mMusicPlayPresenter;
    private Toolbar mToolbar;
    private ImageView mBlurCoverImageView;
    private MusicPlayMainFragment mMusicPlayMainFragment;
    private MusicPlaySecondFragment mMusicPlaySecondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        mMusicPlayPresenter = new MusicPlayPresenterImpl(this);
        mMusicPlayPresenter.onViewFirstTimeCreated();
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
        getSupportFragmentManager().beginTransaction()
                .replace(ID_CONTAINER, getMusicPlayMainFragment())
                .commit();
    }

    @Override
    public void initListener() {

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
        return getMusicPlayMainFragment().getCurrentPlayingSong();
    }

    @Override
    public void loadBlurCover(final Bitmap bitmap) {
        AnimUtil.alpha(mBlurCoverImageView, AnimUtil.ACTION.IN, 0, 1.0f, AnimUtil.DURATION_XLONG, null, new AnimUtil.SimpleAnimListener() {
            @Override
            public void onAnimStart() {
                mBlurCoverImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onAnimEnd() {

            }
        });
    }

    @Override
    public void showMusicPlayMainFragment() {
        if (!getMusicPlayMainFragment().isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ID_CONTAINER, getMusicPlayMainFragment())
                    .commit();
        } else if (!getMusicPlayMainFragment().isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(getMusicPlayMainFragment())
                    .commit();
        }
    }

    @Override
    public void hideMusicPlayMainFragment() {
        if (getMusicPlayMainFragment().isAdded() && getMusicPlayMainFragment().isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(getMusicPlayMainFragment())
                    .commit();
        }
    }

    @Override
    public void showMusicPlaySecondFragment() {
        if (!getMusicPlaySecondFragment().isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ID_CONTAINER, getMusicPlaySecondFragment())
                    .commit();
        } else if (!getMusicPlaySecondFragment().isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(getMusicPlaySecondFragment())
                    .commit();
        }
    }

    @Override
    public void hideMusicPlaySecondFragment() {
        if (getMusicPlaySecondFragment().isAdded() && getMusicPlaySecondFragment().isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .hide(getMusicPlaySecondFragment())
                    .commit();
        }
    }

    public MusicPlayMainFragment getMusicPlayMainFragment() {
        if (mMusicPlayMainFragment == null) {
            LocalSongEntity songEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_LOCAL_SONG);
            final ArrayList<LocalSongEntity> playingSongs = getIntent().getParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS);

            mMusicPlayMainFragment = MusicPlayMainFragment.newInstance(songEntity, playingSongs);

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
        return mMusicPlayMainFragment;
    }

    public MusicPlaySecondFragment getMusicPlaySecondFragment() {
        if (mMusicPlaySecondFragment == null) {
            mMusicPlaySecondFragment = new MusicPlaySecondFragment();
        }
        return mMusicPlaySecondFragment;
    }

    @Override
    public void onBackPressed() {
        hideMusicPlaySecondFragment();
        showMusicPlayMainFragment();

        if (getMusicPlayMainFragment().isAdded() && getMusicPlayMainFragment().isVisible()) {
            getMusicPlayMainFragment().onActivityBackPressed();

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
