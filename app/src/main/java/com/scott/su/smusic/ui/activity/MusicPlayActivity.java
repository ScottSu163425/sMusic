package com.scott.su.smusic.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.MusicPlayCallback;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.MusicPlayPresenterImpl;
import com.scott.su.smusic.mvp.view.MusicPlayView;
import com.scott.su.smusic.service.MusicPlayService;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.TimeUtil;

import java.util.ArrayList;

/**
 * 2016-09-07 22:01:51
 */
public class MusicPlayActivity extends BaseActivity implements MusicPlayView, View.OnClickListener {
    private MusicPlayPresenter mMusicPlayPresenter;
    private TextView mMusicTitleTextView, mMusicArtistTextView, mCurrentTimeTextView, mTotalTimeTextView;
    private ImageView mCoverImageView, mBlurCoverImageView;
    private CardView mPlayControlCard;
    private FloatingActionButton mPlayButton;
    private ImageButton mRepeatButton, mSkipPreviousButton, mSkipNextButton, mShuffleButton;
    private AppCompatSeekBar mPlayProgressSeekBar;
    private LocalSongEntity mInitialPlayingSong;
    private LocalSongEntity mCurrentPlayingSong;
    private ArrayList<LocalSongEntity> mCurrentPlayingSongList;
    private PlayMode mCurrentPlayMode = PlayMode.RepeatAll;
    private PlayMode mCurrentRepeatMode = PlayMode.RepeatAll;
    private PlayStatus mCurrentPlayStatus;
    private ServiceConnection mMusicPlayServiceConnection;
    private MusicPlayService.MusicPlayServiceBinder mMusicPlayServiceBinder;
    private boolean mSeeking;  //Is Seekbar seeking.
    boolean mExisting = false; //Is activity existing.

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
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public View getSnackbarParent() {
        return mCoverImageView;
    }

    @Override
    public void initPreData() {
        initTransition();

        mCurrentPlayingSong = getIntent().getParcelableExtra(Constants.KEY_EXTRA_LOCAL_SONG);
        mCurrentPlayingSongList = getIntent().getParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS);
        mInitialPlayingSong = mCurrentPlayingSong;

        mMusicPlayServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mMusicPlayServiceBinder = (MusicPlayService.MusicPlayServiceBinder) iBinder;
                if (mCurrentPlayingSong != null && mCurrentPlayingSongList != null) {
                    mMusicPlayServiceBinder.setPlaySong(mCurrentPlayingSong, mCurrentPlayingSongList);
                }
                mMusicPlayServiceBinder.registerPlayCallback(new MusicPlayCallback() {
                    @Override
                    public void onPlayStart() {
                        mCurrentPlayStatus = PlayStatus.Playing;
                        mMusicPlayPresenter.onPlayStart();
                    }

                    @Override
                    public void onPlaySongChanged(LocalSongEntity songEntity) {
                        mMusicPlayPresenter.onPlaySongChanged(songEntity);
                        mCurrentPlayingSong = songEntity;
                    }

                    @Override
                    public void onPlayProgressUpdate(long currentPositionMillSec) {
                        mMusicPlayPresenter.onPlayProgressUpdate(currentPositionMillSec);
                    }

                    @Override
                    public void onPlayPause(long currentPositionMillSec) {
                        mCurrentPlayStatus = PlayStatus.Pause;
                        mMusicPlayPresenter.onPlayPause(currentPositionMillSec);
                    }

                    @Override
                    public void onPlayResume() {
                        mCurrentPlayStatus = PlayStatus.Playing;
                        mMusicPlayPresenter.onPlayResume();
                    }

                    @Override
                    public void onPlayStop() {
                        mCurrentPlayStatus = PlayStatus.Stop;
                        mMusicPlayPresenter.onPlayStop();
                    }

                    @Override
                    public void onPlayComplete() {
                        mMusicPlayPresenter.onPlayComplete();
                    }
                });
                mCurrentPlayStatus = mMusicPlayServiceBinder.getCurrentPlayStatus();
                mMusicPlayPresenter.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mMusicPlayPresenter.onServiceDisconnected();
                mMusicPlayServiceBinder = null;
            }
        };
        Intent intent = new Intent(MusicPlayActivity.this, MusicPlayService.class);
        startService(intent);
        bindService(intent, mMusicPlayServiceConnection, BIND_AUTO_CREATE);
    }

    private void initTransition() {
        if (SdkUtil.isLolipopOrLatter()) {
            if (getWindow().getSharedElementEnterTransition() != null) {
                getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        if (mExisting) {
//                            CirclarRevealUtil.revealOut(mPlayControlCard,
//                                    CirclarRevealUtil.DIRECTION.RIGHT_CENTER,
//                                    CirclarRevealUtil.DURATION_REVEAL_SHORT,
//                                    false);
                        } else {

                            if (AppConfig.isNightModeOn(MusicPlayActivity.this)) {
                                CirclarRevealUtil.revealIn(mPlayControlCard,
                                        CirclarRevealUtil.DIRECTION.CENTER,
                                        CirclarRevealUtil.DURATION_REVEAL_NORMAL,
                                        new AccelerateInterpolator());
                            } else {
                                CirclarRevealUtil.revealIn(mPlayControlCard,
                                        CirclarRevealUtil.DIRECTION.CENTER,
                                        CirclarRevealUtil.DURATION_REVEAL_NORMAL);
                            }
                        }
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {

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

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_music_play);
        toolbar.setTitle("");
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
        mCurrentTimeTextView = (TextView) findViewById(R.id.tv_current_time_music_play);
        mTotalTimeTextView = (TextView) findViewById(R.id.tv_total_time_music_play);
        mCoverImageView = (ImageView) findViewById(R.id.iv_cover_music_play);
        mBlurCoverImageView = (ImageView) findViewById(R.id.iv_cover_blur_music_play);
        mPlayControlCard = (CardView) findViewById(R.id.card_view_play_control_music_play);
        mPlayButton = (FloatingActionButton) findViewById(R.id.fab_play_music_play);
        mPlayProgressSeekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar_progress_music_play);
        mSkipPreviousButton = (ImageButton) findViewById(R.id.imgbtn_skip_previous_music_play);
        mSkipNextButton = (ImageButton) findViewById(R.id.imgbtn_skip_next_music_play);
        mRepeatButton = (ImageButton) findViewById(R.id.imgbtn_repeat_music_play);
        mShuffleButton = (ImageButton) findViewById(R.id.imgbtn_shuffle_music_play);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mPlayButton.setOnClickListener(this);
        mSkipPreviousButton.setOnClickListener(this);
        mSkipNextButton.setOnClickListener(this);
        mRepeatButton.setOnClickListener(this);
        mShuffleButton.setOnClickListener(this);

        mPlayProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentTimeTextView.setText(TimeUtil.millisecondToTimeWithinHour(progress));
                mMusicTitleTextView.requestFocus();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mSeeking = true;
                mMusicPlayPresenter.onSeekStart();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeeking = false;
                mMusicPlayPresenter.onSeekStop(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == mPlayButton.getId()) {
            mMusicPlayPresenter.onPlayClick(view);
        } else if (id == mSkipPreviousButton.getId()) {
            mMusicPlayPresenter.onSkipPreviousClick(view);
        } else if (id == mSkipNextButton.getId()) {
            mMusicPlayPresenter.onSkipNextClick(view);
        } else if (id == mRepeatButton.getId()) {
            mMusicPlayPresenter.onRepeatClick(view);
        } else if (id == mShuffleButton.getId()) {
            mMusicPlayPresenter.onShuffleClick(view);
        }
    }

    @Override
    public void setCurrentPlayingSong(LocalSongEntity songEntity) {
        mCurrentPlayingSong = songEntity;
    }

    @Override
    public LocalSongEntity getCurrentPlayingSong() {
        return mCurrentPlayingSong;
    }

    @Override
    public ArrayList<LocalSongEntity> getCurrentPlayingSongs() {
        return mCurrentPlayingSongList;
    }

    @Override
    public void setPlayingMusicTitle(String title) {
        mMusicTitleTextView.setText(title);
    }

    @Override
    public void setPlayingMusicArtist(String artist) {
        mMusicArtistTextView.setText(artist);
    }

    @Override
    public void setSeekBarMax(long max) {
        mPlayProgressSeekBar.setMax((int) max);
    }

    @Override
    public void setSeekBarCurrentPosition(long currentPosition) {
        if (mSeeking) {
            return;
        }
        mPlayProgressSeekBar.setProgress((int) currentPosition);
    }

    @Override
    public void setTotalPlayTime(String totalPlayTime) {
        mTotalTimeTextView.setText(totalPlayTime);
    }

    @Override
    public void loadCover(final String path, boolean needReveal) {
        if (isActivityDestroyed()) {
            return;
        }

        if (needReveal) {
            CirclarRevealUtil.revealOut(mCoverImageView, CirclarRevealUtil.DIRECTION.CENTER, CirclarRevealUtil.DURATION_REVEAL_NORMAL
                    , null, new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {
                        }

                        @Override
                        public void onAnimEnd() {
                            ImageLoader.load(MusicPlayActivity.this,
                                    path,
                                    mCoverImageView,
                                    R.color.background_music_play,
                                    R.color.background_music_play
                            );
                            CirclarRevealUtil.revealIn(mCoverImageView, CirclarRevealUtil.DIRECTION.CENTER);
                        }
                    }, false);
        } else {
            ImageLoader.load(MusicPlayActivity.this,
                    path,
                    mCoverImageView,
                    R.color.background_music_play,
                    R.color.background_music_play
            );
        }
    }

    @Override
    public void loadBlurCover(final Bitmap bitmap) {
        if (this.isFinishing()) {
            return;
        }

        AnimUtil.alpha(mBlurCoverImageView, AnimUtil.ACTION.OUT, 1.0f, 0, AnimUtil.DURATION_LONG, null, new AnimUtil.SimpleAnimListener() {
            @Override
            public void onAnimStart() {

            }

            @Override
            public void onAnimEnd() {
                mBlurCoverImageView.setImageBitmap(bitmap);
                AnimUtil.alphaIn(mBlurCoverImageView);
            }
        });

//        CirclarRevealUtil.revealIn(mBlurCoverImageView, CirclarRevealUtil.DIRECTION.CENTER_TOP, AnimUtil.DURATION_LONG);
    }

    @Override
    public boolean isPlayRepeatAll() {
        return mCurrentPlayMode == PlayMode.RepeatAll;
    }

    @Override
    public boolean isRepeatAll() {
        return mCurrentRepeatMode == PlayMode.RepeatAll;
    }

    @Override
    public boolean isPlayRepeatOne() {
        return mCurrentPlayMode == PlayMode.RepeatOne;
    }

    @Override
    public boolean isRepeatOne() {
        return mCurrentRepeatMode == PlayMode.RepeatOne;
    }

    @Override
    public boolean isPlayShuffle() {
        return mCurrentPlayMode == PlayMode.Shuffle;
    }

    @Override
    public void setPlayRepeatAll() {
        mRepeatButton.setImageResource(R.drawable.ic_repeat_all_selected_24dp);
        mShuffleButton.setImageResource(R.drawable.ic_shuffle_normal_24dp);
        mCurrentPlayMode = PlayMode.RepeatAll;
        mCurrentRepeatMode = PlayMode.RepeatAll;
    }

    @Override
    public void setPlayRepeatOne() {
        mRepeatButton.setImageResource(R.drawable.ic_repeat_one_selected_24dp);
        mShuffleButton.setImageResource(R.drawable.ic_shuffle_normal_24dp);
        mCurrentPlayMode = PlayMode.RepeatOne;
        mCurrentRepeatMode = PlayMode.RepeatOne;
    }

    @Override
    public void setPlayShuffleFromRepeatAll() {
        mShuffleButton.setImageResource(R.drawable.ic_shuffle_selected_24dp);
        mRepeatButton.setImageResource(R.drawable.ic_repeat_all_normal_24dp);
        mCurrentPlayMode = PlayMode.Shuffle;
    }

    @Override
    public void setPlayShuffleFromRepeatOne() {
        mShuffleButton.setImageResource(R.drawable.ic_shuffle_selected_24dp);
        mRepeatButton.setImageResource(R.drawable.ic_repeat_one_normal_24dp);
        mCurrentPlayMode = PlayMode.Shuffle;
    }

    @Override
    public PlayMode getCurrentPlayMode() {
        return mCurrentPlayMode;
    }

    @Override
    public boolean isMusicPlaying() {
        return mCurrentPlayStatus == PlayStatus.Playing;
    }

    @Override
    public boolean isMusicPause() {
        return mCurrentPlayStatus == PlayStatus.Pause;
    }

    @Override
    public boolean isMusicStop() {
        return mCurrentPlayStatus == PlayStatus.Stop;
    }

    @Override
    public void setPlayButtonPlaying() {
        mPlayButton.setImageResource(R.drawable.ic_pause_24dp);
    }

    @Override
    public void setPlayButtonPause() {
        mPlayButton.setImageResource(R.drawable.ic_play_arrow_24dp);
    }

    @Override
    public PlayStatus getCurrentPlayStatus() {
        return null;
    }

    @Override
    public void setPlaySong(LocalSongEntity currentPlaySong, ArrayList<LocalSongEntity> playSongs) {

    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        if (mMusicPlayServiceBinder != null) {
            mMusicPlayServiceBinder.setPlayMode(playMode);
        }
    }

    @Override
    public void play() {
        mMusicPlayServiceBinder.play();
    }

    @Override
    public void pause() {
        mMusicPlayServiceBinder.pause();
    }

    @Override
    public void seekTo(int position) {
        mMusicPlayServiceBinder.seekTo(position);
    }

    @Override
    public void playPrevious() {
        mMusicPlayServiceBinder.playPrevious();
    }

    @Override
    public void playNext() {
        mMusicPlayServiceBinder.playNext();
    }

    @Override
    public void registerPlayCallback(@NonNull MusicPlayCallback callback) {

    }

    @Override
    public void unregisterPlayCallback() {

    }

    @Override
    public void onBackPressed() {
        mExisting = true;
        if (mInitialPlayingSong.getSongId() != mCurrentPlayingSong.getSongId()) {
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

    @Override
    protected void onDestroy() {
        unbindService(mMusicPlayServiceConnection);
        mMusicPlayPresenter.onViewWillDestroy();
        super.onDestroy();
    }

}
