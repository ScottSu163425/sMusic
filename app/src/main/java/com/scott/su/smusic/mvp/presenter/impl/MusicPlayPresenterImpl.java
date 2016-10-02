package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.view.MusicPlayView;
import com.su.scott.slibrary.util.TimeUtil;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayPresenterImpl implements MusicPlayPresenter {
    private MusicPlayView mMusicPlayView;
    private LocalAlbumModel mAlbumModel;
    private boolean isFirstTimePlay = true;


    public MusicPlayPresenterImpl(MusicPlayView mMusicPlayView) {
        this.mMusicPlayView = mMusicPlayView;
        this.mAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMusicPlayView.initPreData();
        mMusicPlayView.initToolbar();
        mMusicPlayView.initView();
        mMusicPlayView.initData();
        mMusicPlayView.initListener();
        if (AppConfig.isPlayRepeatOne(mMusicPlayView.getViewContext())) {
            mMusicPlayView.setPlayRepeatOne();
            mMusicPlayView.setPlayMode(mMusicPlayView.getCurrentPlayMode());
        } else if (AppConfig.isPlayRepeatAll(mMusicPlayView.getViewContext())) {
            mMusicPlayView.setPlayRepeatAll();
            mMusicPlayView.setPlayMode(mMusicPlayView.getCurrentPlayMode());
        } else if (AppConfig.isPlayShuffle(mMusicPlayView.getViewContext())) {
            mMusicPlayView.setPlayShuffleFromRepeatAll();
            mMusicPlayView.setPlayMode(mMusicPlayView.getCurrentPlayMode());
        } else {
            mMusicPlayView.setPlayRepeatAll();
            AppConfig.setPlayRepeatAll(mMusicPlayView.getViewContext());
            mMusicPlayView.setPlayMode(mMusicPlayView.getCurrentPlayMode());
        }
        updateCurrentPlayingSongInfo(false);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
    }

    @Override
    public void onPlayClick(View view) {
        if (mMusicPlayView.isMusicPlaying()) {
            mMusicPlayView.pause();
        } else {
            mMusicPlayView.play();
        }
    }

    @Override
    public void onSkipPreviousClick(View view) {
        mMusicPlayView.playPrevious();
    }

    @Override
    public void onSkipNextClick(View view) {
        mMusicPlayView.playNext();
    }

    @Override
    public void onRepeatClick(View view) {
        if (mMusicPlayView.isPlayRepeatAll()) {
            mMusicPlayView.setPlayRepeatOne();
            AppConfig.setPlayRepeatOne(mMusicPlayView.getViewContext());
        } else if (mMusicPlayView.isPlayRepeatOne()) {
            mMusicPlayView.setPlayRepeatAll();
            AppConfig.setPlayRepeatAll(mMusicPlayView.getViewContext());
        } else if (mMusicPlayView.isPlayShuffle()) {
            if (mMusicPlayView.isRepeatAll()) {
                mMusicPlayView.setPlayRepeatAll();
                AppConfig.setPlayRepeatAll(mMusicPlayView.getViewContext());
            } else if (mMusicPlayView.isRepeatOne()) {
                mMusicPlayView.setPlayRepeatOne();
                AppConfig.setPlayRepeatOne(mMusicPlayView.getViewContext());
            }
        }
    }

    @Override
    public void onShuffleClick(View view) {
        if (mMusicPlayView.isRepeatAll()) {
            mMusicPlayView.setPlayShuffleFromRepeatAll();
        } else if (mMusicPlayView.isRepeatOne()) {
            mMusicPlayView.setPlayShuffleFromRepeatOne();
        }
        AppConfig.setPlayShuffle(mMusicPlayView.getViewContext());
    }

    @Override
    public void onServiceConnected() {
        if (mMusicPlayView.isMusicPlaying()) {
            mMusicPlayView.setPlayButtonPlaying();
        } else {
            mMusicPlayView.setPlayButtonPause();
        }
        mMusicPlayView.play();
    }

    @Override
    public void onServiceDisconnected() {

    }

    @Override
    public void onPlayStart() {
        mMusicPlayView.setPlayButtonPlaying();
    }

    @Override
    public void onPlaySongChanged(LocalSongEntity songEntity) {
        mMusicPlayView.setCurrentPlayingSong(songEntity);
        updateCurrentPlayingSongInfo(!isFirstTimePlay);
        isFirstTimePlay = false;
    }

    @Override
    public void onPlayProgressUpdate(long currentPositionMillSec) {
        mMusicPlayView.setSeekBarCurrentPosition(currentPositionMillSec);
    }

    @Override
    public void onPlayPause(long currentPositionMillSec) {
        mMusicPlayView.setPlayButtonPause();
    }

    @Override
    public void onPlayResume() {
        mMusicPlayView.setPlayButtonPlaying();

    }

    @Override
    public void onPlayStop() {
        mMusicPlayView.setPlayButtonPause();

    }

    @Override
    public void onPlayComplete() {
        mMusicPlayView.setPlayButtonPause();

    }

    @Override
    public void onSeekStart() {
    }

    @Override
    public void onSeekStop(int progress) {
        mMusicPlayView.seekTo(progress);
    }

    private void updateCurrentPlayingSongInfo(boolean needReveal) {
        String path = mAlbumModel.getAlbumCoverPath(mMusicPlayView.getViewContext(), mMusicPlayView.getCurrentPlayingSong().getAlbumId());
        mMusicPlayView.loadCover(path, needReveal);

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                return mAlbumModel.getAlbumCoverBitmapBlur(mMusicPlayView.getViewContext(), mMusicPlayView.getCurrentPlayingSong().getAlbumId());
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                mMusicPlayView.loadBlurCover(bitmap);
            }
        }.execute();

        mMusicPlayView.setSeekBarCurrentPosition(0);
        mMusicPlayView.setSeekBarMax(mMusicPlayView.getCurrentPlayingSong().getDuration());
        mMusicPlayView.setPlayingMusicTitle(mMusicPlayView.getCurrentPlayingSong().getTitle());
        mMusicPlayView.setPlayingMusicArtist(mMusicPlayView.getCurrentPlayingSong().getArtist());
        mMusicPlayView.setTotalPlayTime(TimeUtil.millisecondToTimeWithinHour(mMusicPlayView.getCurrentPlayingSong().getDuration()));
    }
}
