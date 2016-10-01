package com.scott.su.smusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scott.su.smusic.callback.MusicPlayCallback;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.view.MusicPlayServiceView;
import com.scott.su.smusic.util.MusicPlayUtil;
import com.su.scott.slibrary.util.TimeUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MusicPlayService extends Service implements MusicPlayServiceView {
    private MediaPlayer mMediaPlayer;
    private MusicPlayCallback mMusicPlayCallback;
    private LocalSongEntity mCurrentPlaySong;
    private boolean mPlaySameSong;
    private List<LocalSongEntity> mPlaySongs;

    private PlayStatus mCurrentPlayStatus = PlayStatus.Stop;
    private PlayMode mCurrentPlayMode = PlayMode.RepeatAll;
    private Handler mTimerHandler = new Handler();
    private Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMusicPlayCallback != null) {
                mMusicPlayCallback.onPlayProgressUpdate(mMediaPlayer.getCurrentPosition());
            }
            mTimerHandler.postDelayed(this, TimeUtil.MILLISECONDS_OF_SECOND / 10);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mMusicPlayCallback != null) {
                    mMusicPlayCallback.onPlayComplete();
                }
                playNext();
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCurrentPlayMode = AppConfig.isPlayRepeatOne(this) ? PlayMode.RepeatOne
                : (AppConfig.isPlayRepeatAll(this) ? PlayMode.RepeatAll : PlayMode.Shuffle);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void setPlaySong(LocalSongEntity currentPlaySong, List<LocalSongEntity> playSongs) {
        if (mCurrentPlaySong == null) {
            mPlaySameSong = false;
        } else if (mCurrentPlaySong.getSongId() == currentPlaySong.getSongId()) {
            mPlaySameSong = true;
        } else {
            mPlaySameSong = false;
        }
        this.mCurrentPlaySong = currentPlaySong;
        this.mPlaySongs = playSongs;
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        this.mCurrentPlayMode = playMode;
    }

    @Override
    public void play() {
        if (mMediaPlayer != null && mCurrentPlaySong != null) {
            if (mPlaySameSong) {
                playResume();
            } else {
                //Play different song.
                playNew();
            }
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && isPlaying()) {
            stopTimer();
            mMediaPlayer.pause();
            mCurrentPlayStatus = PlayStatus.Pause;
            mPlaySameSong = true;
            if (isRegisterCallback()) {
                mMusicPlayCallback.onPlayPause(mMediaPlayer.getCurrentPosition());
            }
        }
    }

    @Override
    public void seekTo(int position) {
        if (isPause()) {
            mMediaPlayer.seekTo(position);
        } else {
            pause();
            mMediaPlayer.seekTo(position);
        }
        mMusicPlayCallback.onPlayProgressUpdate(mMediaPlayer.getCurrentPosition());
        playResume();
    }

    @Override
    public void playPrevious() {
        mCurrentPlaySong = MusicPlayUtil.getPreviousSong(mCurrentPlaySong, mPlaySongs, mCurrentPlayMode);
        playNew();
    }

    @Override
    public void playNext() {
        mCurrentPlaySong = MusicPlayUtil.getNextSong(mCurrentPlaySong, mPlaySongs, mCurrentPlayMode);
        playNew();
    }

    private void playResume() {
        if (isPause()) {
            startMediaPlayer();
        }
    }

    private void playNew() {
        try {
            if (!mPlaySameSong) {
                mMediaPlayer.reset();
            }
            mMediaPlayer.setDataSource(mCurrentPlaySong.getPath());
            mMediaPlayer.prepare();
            startMediaPlayer();
            if (mMusicPlayCallback != null) {
                mMusicPlayCallback.onPlaySongChanged(mCurrentPlaySong);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startMediaPlayer() {
        mMediaPlayer.start();
        startTimer();
        mCurrentPlayStatus = PlayStatus.Playing;
        if (isRegisterCallback()) {
            mMusicPlayCallback.onPlayStart();
        }
    }

    private void startTimer() {
        stopTimer();

//        mPlayTimer = new CountDownTimer(mCurrentPlaySong.getDuration() - mMediaPlayer.getCurrentPosition(), TimeUtil.MILLISECONDS_OF_SECOND / 2) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (isRegisterCallback()) {
//                    mMusicPlayCallback.onPlayProgressUpdate(mMediaPlayer.getCurrentPosition());
//
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                if (isRegisterCallback()) {
//                    mMusicPlayCallback.onPlayComplete();
//                }
//            }
//        }.start();
        mTimerHandler.post(mTimerRunnable);
    }

    private void stopTimer() {
        mTimerHandler.removeCallbacks(mTimerRunnable);
    }

    private boolean isRegisterCallback() {
        return mMusicPlayCallback != null;
    }

    private boolean isStop() {
        return mCurrentPlayStatus == PlayStatus.Stop;
    }

    private boolean isPlaying() {
        return mCurrentPlayStatus == PlayStatus.Playing;
    }

    private boolean isPause() {
        return mCurrentPlayStatus == PlayStatus.Pause;
    }

    @Override
    public PlayStatus getCurrentPlayStatus() {
        return mCurrentPlayStatus;
    }

    @Override
    public void registerPlayCallback(@NonNull MusicPlayCallback callback) {
        this.mMusicPlayCallback = callback;
    }

    @Override
    public void unregisterPlayCallback() {
        this.mMusicPlayCallback = null;
    }

    public class MusicPlayServiceBinder extends Binder implements MusicPlayServiceView {

        @Override
        public PlayStatus getCurrentPlayStatus() {
            return MusicPlayService.this.getCurrentPlayStatus();
        }

        @Override
        public void setPlaySong(LocalSongEntity currentPlaySong, List<LocalSongEntity> playSongs) {
            MusicPlayService.this.setPlaySong(currentPlaySong, playSongs);
        }

        @Override
        public void setPlayMode(PlayMode playMode) {
            MusicPlayService.this.setPlayMode(playMode);
        }

        @Override
        public void play() {
            MusicPlayService.this.play();
        }

        @Override
        public void pause() {
            MusicPlayService.this.pause();
        }

        @Override
        public void seekTo(int position) {
            MusicPlayService.this.seekTo(position);
        }

        @Override
        public void playPrevious() {
            MusicPlayService.this.playPrevious();
        }

        @Override
        public void playNext() {
            MusicPlayService.this.playNext();
        }

        @Override
        public void registerPlayCallback(@NonNull MusicPlayCallback callback) {
            MusicPlayService.this.registerPlayCallback(callback);
        }

        @Override
        public void unregisterPlayCallback() {
            MusicPlayService.this.unregisterPlayCallback();
        }
    }


}
