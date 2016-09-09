package com.scott.su.smusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.util.TimeUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MusicPlayService extends Service {
    //    private MediaPlayer mMediaPlayer;
    private MusicPlayCallback mMusicPlayCallback;
    private LocalSongEntity mCurrentPlaySong;
    private List<LocalSongEntity> mPlaySongs;

    private PlayStatus mCurrentPlayStatus = PlayStatus.Stop;
    private long mCurrentPlayPosition;
    private CountDownTimer mPlayTimer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mMediaPlayer = new MediaPlayer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private void setPlaySong(LocalSongEntity currentPlaySong, List<LocalSongEntity> playSongs) {
        this.mCurrentPlaySong = currentPlaySong;
        this.mPlaySongs = playSongs;
    }

    private void play() {
        if (/*mMediaPlayer != null &&*/ mCurrentPlaySong != null) {
            if (isPlaying()) {
                return;
            }

            if (isPause()) {
                startMediaPlayer();
                return;
            }

            //Stopped;
           /* try {
                mMediaPlayer.setDataSource(mCurrentPlaySong.getPath());
                mMediaPlayer.prepare();*/
            startMediaPlayer();
           /* } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    private void pause() {
        if (/*mMediaPlayer != null &&*/ isPlaying()) {
            stopTimer(true);
          /*  mMediaPlayer.pause();*/
            mCurrentPlayStatus=PlayStatus.Pause;
            if (isRegisterCallback()) {
//                mCurrentPlayPosition = mMediaPlayer.getCurrentPosition();
                mMusicPlayCallback.onPlayPause(mCurrentPlayPosition);
            }
        }
    }

    private void startMediaPlayer() {
//        mMediaPlayer.start();
        startTimer();
        mCurrentPlayStatus=PlayStatus.Playing;
        if (isRegisterCallback()) {
            mMusicPlayCallback.onPlayStart();
        }
    }

    private void startTimer() {
        mPlayTimer = new CountDownTimer(mCurrentPlaySong.getDuration() - mCurrentPlayPosition, TimeUtil.MILLISECONDS_OF_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentPlayPosition = mCurrentPlaySong.getDuration() - millisUntilFinished;
                if (isRegisterCallback()) {
                    mMusicPlayCallback.onPlayProgressUpdate(mCurrentPlayPosition);
                }
            }

            @Override
            public void onFinish() {
                if (isRegisterCallback()) {
                    mMusicPlayCallback.onPlayComplete();
                }
            }
        }.start();
    }

    private void stopTimer(boolean isPause) {
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
            mPlayTimer = null;
        }

        if (!isPause) {
            mCurrentPlayPosition = 0;
        }
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


    private void registerPlayCallback(@NonNull MusicPlayCallback callback) {
        this.mMusicPlayCallback = callback;
    }


    private void unregisterPlayCallback() {
        this.mMusicPlayCallback = null;
    }

    public class MusicPlayServiceBinder extends Binder {
        public void setPlaySong(LocalSongEntity currentPlaySong, List<LocalSongEntity> playSongs) {
            MusicPlayService.this.setPlaySong(currentPlaySong, playSongs);
        }

        public void play() {
            MusicPlayService.this.play();
        }

        public void pause() {
            MusicPlayService.this.pause();
        }

        public void registerPlayCallback(@NonNull MusicPlayCallback callback) {
            MusicPlayService.this.registerPlayCallback(callback);
        }

        public void unregisterPlayCallback() {
            MusicPlayService.this.unregisterPlayCallback();
        }

    }

    public interface MusicPlayCallback {
        void onPlayStart();

        void onPlayProgressUpdate(long currentPositionMillSec);

        void onPlayPause(long currentPositionMillSec);

        void onPlayResume();

        void onPlayStop();

        void onPlayComplete();
    }

}
