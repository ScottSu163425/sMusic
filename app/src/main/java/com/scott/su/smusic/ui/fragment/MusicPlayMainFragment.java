package com.scott.su.smusic.ui.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.ActivityBackPressCallback;
import com.scott.su.smusic.callback.MusicPlayMainFragmentCallback;
import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.callback.PlayListBottomSheetCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.MusicPlayMainContract;
import com.scott.su.smusic.mvp.presenter.impl.MusicPlayMainPresenterImpl;
import com.scott.su.smusic.service.MusicPlayService;
import com.su.scott.slibrary.fragment.BaseFragment;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.DialogUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.TimeUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/20.
 */

public class MusicPlayMainFragment extends BaseFragment implements MusicPlayMainContract.MusicPlayMainView, ActivityBackPressCallback {
    private MusicPlayMainContract.MusicPlayMainPresenter mMusicPlayPresenter;
    private View mRootView;
    private TextView mMusicTitleTextView, mMusicArtistTextView, mCurrentTimeTextView, mTotalTimeTextView;
    private ImageView mCoverImageView;
    private CardView mPlayControlCard;
    private FloatingActionButton mPlayButton;
    private ImageButton mRepeatButton, mSkipPreviousButton, mSkipNextButton, mPlayListButton;
    private AppCompatSeekBar mPlayProgressSeekBar;
    private LocalSongEntity mInitialPlayingSong;
    private LocalSongEntity mCurrentPlayingSong;
    private PlayMode mCurrentPlayMode = PlayMode.RepeatAll;
    private PlayStatus mCurrentPlayStatus;
    private ServiceConnection mMusicPlayServiceConnection;
    private MusicPlayService.MusicPlayServiceBinder mMusicPlayServiceBinder;
    private boolean mSeeking;  //Is Seekbar seeking.
    boolean mExisting = false; //Is activity existing.
    private MusicPlayMainFragmentCallback mMusicPlayMainFragmentCallback;


    public static MusicPlayMainFragment newInstance(LocalSongEntity songEntity, ArrayList<LocalSongEntity> songEntities) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        args.putParcelableArrayList(Constants.KEY_EXTRA_LOCAL_SONGS, songEntities);
        MusicPlayMainFragment fragment = new MusicPlayMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_music_play_main, container, false);

        return mRootView;
    }

    @Override
    protected IPresenter getPresenter() {
        if (mMusicPlayPresenter == null) {
            mMusicPlayPresenter = new MusicPlayMainPresenterImpl(this);
        }
        return mMusicPlayPresenter;
    }

    @Override
    protected void onFirstTimeCreateView() {
        mMusicPlayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void initPreData() {
        initTransition();

        mCurrentPlayingSong = getArguments().getParcelable(Constants.KEY_EXTRA_LOCAL_SONG);
        final ArrayList<LocalSongEntity> playingSongs = getArguments().getParcelableArrayList(Constants.KEY_EXTRA_LOCAL_SONGS);
        mInitialPlayingSong = mCurrentPlayingSong;

        mMusicPlayServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mMusicPlayServiceBinder = (MusicPlayService.MusicPlayServiceBinder) iBinder;

                //To keep the order what it is,set Play list songs befor set current play song.
                if (playingSongs != null) {
                    setServicePlayListSongs(playingSongs);
                }

                if (mCurrentPlayingSong != null) {
                    setServiceCurrentPlaySong(mCurrentPlayingSong);
                }


                setServicePlayMode(mCurrentPlayMode);
                registerServicePlayCallback(mMusicPlayServiceCallback);
                mCurrentPlayStatus = mMusicPlayServiceBinder.getServiceCurrentPlayStatus();
                mMusicPlayPresenter.onMusicPlayServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mMusicPlayPresenter.onMusicPlayServiceDisconnected();
                mMusicPlayServiceBinder = null;
            }
        };
        Intent intent = new Intent(getActivity(), MusicPlayService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, mMusicPlayServiceConnection, getActivity().BIND_AUTO_CREATE);
    }

    private void initTransition() {
        if (SdkUtil.isLolipopOrLatter()) {
            getActivity().getWindow().setEnterTransition(new Fade());

            getActivity().getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    if (mExisting) {

                    } else {
                        CirclarRevealUtil.revealIn(mPlayControlCard,
                                CirclarRevealUtil.DIRECTION.CENTER,
                                CirclarRevealUtil.DURATION_REVEAL_NORMAL);
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


    @Override
    public void initToolbar() {

    }

    @Override
    public void initView() {
        mMusicTitleTextView = (TextView) mRootView.findViewById(R.id.tv_music_title_music_play);
        mMusicArtistTextView = (TextView) mRootView.findViewById(R.id.tv_music_artist_music_play);
        mCurrentTimeTextView = (TextView) mRootView.findViewById(R.id.tv_current_time_music_play);
        mTotalTimeTextView = (TextView) mRootView.findViewById(R.id.tv_total_time_music_play);
        mCoverImageView = (ImageView) mRootView.findViewById(R.id.iv_cover_music_play);
        mPlayControlCard = (CardView) mRootView.findViewById(R.id.card_view_play_control_music_play);
        mPlayButton = (FloatingActionButton) mRootView.findViewById(R.id.fab_play_music_play);
        mPlayProgressSeekBar = (AppCompatSeekBar) mRootView.findViewById(R.id.seek_bar_progress_music_play);
        mSkipPreviousButton = (ImageButton) mRootView.findViewById(R.id.imgbtn_skip_previous_music_play);
        mSkipNextButton = (ImageButton) mRootView.findViewById(R.id.imgbtn_skip_next_music_play);
        mRepeatButton = (ImageButton) mRootView.findViewById(R.id.imgbtn_repeat_music_play);
        mPlayListButton = (ImageButton) mRootView.findViewById(R.id.imgbtn_play_list_music_play);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mCoverImageView.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mSkipPreviousButton.setOnClickListener(this);
        mSkipNextButton.setOnClickListener(this);
        mRepeatButton.setOnClickListener(this);
        mPlayListButton.setOnClickListener(this);

        mPlayProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentTimeTextView.setText(TimeUtil.millisecondToMMSS(progress));
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
            if (ViewUtil.isFastClick()) {
                return;
            }
            mMusicPlayPresenter.onSkipPreviousClick(view);
        } else if (id == mSkipNextButton.getId()) {
            if (ViewUtil.isFastClick()) {
                return;
            }
            mMusicPlayPresenter.onSkipNextClick(view);
        } else if (id == mRepeatButton.getId()) {
            mMusicPlayPresenter.onRepeatClick(view);
        } else if (id == mPlayListButton.getId()) {
            showPlayListBottomSheet();
        } else if (id == mCoverImageView.getId()) {
            mMusicPlayPresenter.onCoverClick(view);
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
    public LocalSongEntity getServiceCurrentPlayingSong() {
        if (mMusicPlayServiceBinder != null) {
            return mMusicPlayServiceBinder.getServiceCurrentPlayingSong();
        }
        return null;
    }

    @Override
    public ArrayList<LocalSongEntity> getServicePlayListSongs() {
        if (mMusicPlayServiceBinder != null) {
            return mMusicPlayServiceBinder.getServicePlayListSongs();
        }
        return null;
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
//        if (isFragmentDestroyed()) {
//            return;
//        }

        if (needReveal) {
            CirclarRevealUtil.revealOut(mCoverImageView,
                    CirclarRevealUtil.DIRECTION.CENTER,
                    CirclarRevealUtil.DURATION_REVEAL_SHORT,
                    new DecelerateInterpolator(),
                    new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {
                        }

                        @Override
                        public void onAnimEnd() {
                            ImageLoader.load(getActivity(),
                                    path,
                                    mCoverImageView,
                                    R.color.transparent,
                                    R.color.background_music_play
                            );
                            CirclarRevealUtil.revealIn(mCoverImageView, CirclarRevealUtil.DIRECTION.CENTER);
                        }
                    }, false);
        } else {
            ImageLoader.load(getActivity(),
                    path,
                    mCoverImageView,
                    R.color.transparent,
                    R.color.background_music_play
            );
        }
    }

    @Override
    public void loadBlurCover(final Bitmap bitmap) {
        if (this.isRemoving()) {
            return;
        }

        mMusicPlayPresenter.onBlurCoverChanged(bitmap);
    }

    @Override
    public boolean isPlayRepeatAll() {
        return mCurrentPlayMode == PlayMode.RepeatAll;
    }

    @Override
    public boolean isPlayRepeatOne() {
        return mCurrentPlayMode == PlayMode.RepeatOne;
    }

    @Override
    public boolean isPlayShuffle() {
        return mCurrentPlayMode == PlayMode.Shuffle;
    }

    @Override
    public void setPlayRepeatAll(boolean needAnim) {
        if (needAnim) {
            AnimUtil.rotate2DPositive(mRepeatButton, AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_SHORT);
            mRepeatButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRepeatButton.setImageResource(R.drawable.ic_repeat_all_selected_24dp);
                }
            }, AnimUtil.DURATION_SHORT_HALF);
        } else {
            mRepeatButton.setImageResource(R.drawable.ic_repeat_all_selected_24dp);
        }
        mCurrentPlayMode = PlayMode.RepeatAll;
    }

    @Override
    public void setPlayRepeatOne(boolean needAnim) {
        if (needAnim) {
            AnimUtil.rotate2DPositive(mRepeatButton, AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_SHORT);
            mRepeatButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRepeatButton.setImageResource(R.drawable.ic_repeat_one_selected_24dp);
                }
            }, AnimUtil.DURATION_SHORT_HALF);
        } else {
            mRepeatButton.setImageResource(R.drawable.ic_repeat_one_selected_24dp);
        }
        mCurrentPlayMode = PlayMode.RepeatOne;
    }

    @Override
    public void setPlayRepeatShuffle(boolean needAnim) {
        if (needAnim) {
            AnimUtil.rotate2DPositive(mRepeatButton, AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_SHORT);
            mRepeatButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRepeatButton.setImageResource(R.drawable.ic_shuffle_selected_24dp);
                }
            }, AnimUtil.DURATION_SHORT_HALF);
        } else {
            mRepeatButton.setImageResource(R.drawable.ic_shuffle_selected_24dp);
        }
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
        mPlayButton.setImageResource(R.drawable.ic_play_arrow__white_24dp);
    }

    PlayListBottomSheetDisplayFragment mPlayListBottomSheetDisplayFragment;

    @Override
    public void showPlayListBottomSheet() {
        mPlayListBottomSheetDisplayFragment = PlayListBottomSheetDisplayFragment.newInstance()
                .setDataList(mMusicPlayServiceBinder.getServicePlayListSongs(), mMusicPlayServiceBinder.getServiceCurrentPlayingSong())
                .setItemCallback(new PlayListBottomSheetCallback() {
                    @Override
                    public void onPlayListItemClick(View itemView, LocalSongEntity entity, int position) {
                        mMusicPlayPresenter.onPlayListItemClick(itemView, entity, position);
                    }

                    @Override
                    public void onPlayListItemRemoveClick(View view, int position, LocalSongEntity entity) {
                        mMusicPlayPresenter.onPlayListItemRemoveClick(view, position, entity);
                    }

                    @Override
                    public void onPlayListClearClick(final View view) {
                        DialogUtil.showDialog(getActivity(), null, getString(R.string.ask_clear_play_list), null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPlayListBottomSheetDisplayFragment.dismissAllowingStateLoss();
                                dialog.dismiss();
                                mMusicPlayPresenter.onPlayListClearClick(view);
                            }
                        }, null, null);
                    }
                });
        mPlayListBottomSheetDisplayFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void updatePlayListBottomSheet(List<LocalSongEntity> playListSongs, LocalSongEntity currentSong) {
        if (mPlayListBottomSheetDisplayFragment != null && mPlayListBottomSheetDisplayFragment.isResumed()) {
            mPlayListBottomSheetDisplayFragment.updatePlayList(playListSongs, currentSong);
        }
    }

    @Override
    public MusicPlayMainFragmentCallback getMusicPlayMainFragmentCallback() {
        return mMusicPlayMainFragmentCallback;
    }

    @Override
    public int getCurrentPositon() {
        return 0;
    }

    @Override
    public PlayStatus getServiceCurrentPlayStatus() {
        return null;
    }

    @Override
    public void setServiceCurrentPlaySong(LocalSongEntity currentPlaySong) {
        mMusicPlayServiceBinder.setServiceCurrentPlaySong(currentPlaySong);
    }

    @Override
    public void setServicePlayListSongs(ArrayList<LocalSongEntity> playSongs) {
        mMusicPlayServiceBinder.setServicePlayListSongs(playSongs);
    }

    @Override
    public void setServicePlayMode(PlayMode playMode) {
        if (mMusicPlayServiceBinder != null) {
            mMusicPlayServiceBinder.setServicePlayMode(playMode);
        }
    }

    @Override
    public PlayMode getServicePlayMode() {
        return mMusicPlayServiceBinder.getServicePlayMode();
    }

    @Override
    public void play() {
        mMusicPlayServiceBinder.play();
    }

    @Override
    public void playSkip() {
        mMusicPlayServiceBinder.playSkip();
    }

    @Override
    public void play(int position) {
        mMusicPlayServiceBinder.play(position);
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
    public void removeServiceSong(LocalSongEntity songEntity) {
        mMusicPlayServiceBinder.removeServiceSong(songEntity);
    }

    @Override
    public void clearServiceSongs() {
        mMusicPlayServiceBinder.clearServiceSongs();
    }

    @Override
    public void registerServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
        mMusicPlayServiceBinder.registerServicePlayCallback(callback);
    }

    @Override
    public void unregisterServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
        mMusicPlayServiceBinder.unregisterServicePlayCallback(mMusicPlayServiceCallback);
    }

    private MusicPlayServiceCallback mMusicPlayServiceCallback = new MusicPlayServiceCallback() {
        @Override
        public void onPlayStart() {
            mCurrentPlayStatus = PlayStatus.Playing;
            mMusicPlayPresenter.onPlayStart();
        }

        @Override
        public void onPlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong, int currentPosition) {
            mMusicPlayPresenter.onPlaySongChanged(previousPlaySong, currentPlayingSong, currentPosition);
            mCurrentPlayingSong = currentPlayingSong;
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
            getActivity().onBackPressed();
        }

        @Override
        public void onPlayComplete() {
            mMusicPlayPresenter.onPlayComplete();
        }
    };

    public boolean isSameSong() {
        return mInitialPlayingSong.getSongId() == mCurrentPlayingSong.getSongId();
    }

    @Override
    public void onActivityBackPressed() {
        mExisting = true;
    }

    @Override
    public void onDestroy() {
        mMusicPlayServiceBinder.unregisterServicePlayCallback(mMusicPlayServiceCallback);
        getActivity().unbindService(mMusicPlayServiceConnection);

        super.onDestroy();
    }

    public void setMusicPlayMainFragmentCallback(MusicPlayMainFragmentCallback mMusicPlayMainFragmentCallback) {
        this.mMusicPlayMainFragmentCallback = mMusicPlayMainFragmentCallback;
    }


}
