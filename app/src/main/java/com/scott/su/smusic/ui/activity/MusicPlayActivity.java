package com.scott.su.smusic.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scott.su.smusic.R;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.MusicPlayPresenterImpl;
import com.scott.su.smusic.mvp.view.MusicPlayView;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.BlurUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.ViewUtil;

public class MusicPlayActivity extends BaseActivity implements MusicPlayView, View.OnClickListener {
    private MusicPlayPresenter mMusicPlayPresenter;
    private TextView mMusicTitleTextView, mMusicArtistTextView;
    private ImageView mCoverImageView, mBlurCoverImageView;
    private FloatingActionButton mPlayButton;
    private ImageButton mRepeatButton, mSkipPreviousButton, mSkipNextButton, mShuffleButton;
    private AppCompatSeekBar mPlayingSeekBar;
    private LocalSongEntity mCurrentPlayingSong;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        mMusicPlayPresenter = new MusicPlayPresenterImpl(this);
        mMusicPlayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public View getSnackbarParent() {
        return mCoverImageView;
    }

    @Override
    public void initPreData() {
        mCurrentPlayingSong = getIntent().getParcelableExtra(Constants.KEY_EXTRA_LOCAL_SONG);
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_music_play);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
        mMusicTitleTextView = (TextView) findViewById(R.id.tv_music_title_music_play);
        mMusicArtistTextView = (TextView) findViewById(R.id.tv_music_artist_music_play);
        mCoverImageView = (ImageView) findViewById(R.id.iv_cover_music_play);
        mBlurCoverImageView = (ImageView) findViewById(R.id.iv_cover_blur_music_play);
        mPlayButton = (FloatingActionButton) findViewById(R.id.fab_play_music_play);
        mSkipPreviousButton = (ImageButton) findViewById(R.id.imgbtn_skip_previous_music_play);
        mSkipNextButton = (ImageButton) findViewById(R.id.imgbtn_skip_next_music_play);
        mRepeatButton = (ImageButton) findViewById(R.id.imgbtn_repeat_music_play);
        mShuffleButton = (ImageButton) findViewById(R.id.imgbtn_shuffle_music_play);
    }

    @Override
    public void initData() {
        ViewUtil.setText(mMusicTitleTextView, mCurrentPlayingSong.getTitle(), "");
        ViewUtil.setText(mMusicArtistTextView, mCurrentPlayingSong.getArtist(), "");
    }

    @Override
    public void initListener() {
        mPlayButton.setOnClickListener(this);
        mSkipPreviousButton.setOnClickListener(this);
        mSkipNextButton.setOnClickListener(this);
        mRepeatButton.setOnClickListener(this);
        mShuffleButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == mPlayButton.getId()) {

        } else if (id == mSkipPreviousButton.getId()) {

        } else if (id == mSkipNextButton.getId()) {

        } else if (id == mRepeatButton.getId()) {

        } else if (id == mShuffleButton.getId()) {
        }
    }

    @Override
    public LocalSongEntity getCurrentPlayingLocalSongEntity() {
        return mCurrentPlayingSong;
    }

    @Override
    public void loadCover(String path) {
        Glide.with(MusicPlayActivity.this)
                .load(path)
                .centerCrop()
                .placeholder(R.color.place_holder_loading)
                .into(mCoverImageView);
    }

    @Override
    public void loadBlurCover(Bitmap bitmap) {
        if (this.isFinishing()) {
            return;
        }
        mBlurCoverImageView.setImageBitmap(bitmap);
        CirclarRevealUtil.revealIn(mBlurCoverImageView, CirclarRevealUtil.DIRECTION.CENTER_BOTTOM, AnimUtil.DURATION_LONG);
    }


}
