package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.MusicPlayModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.MusicPlayModelImpl;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.view.MusicPlayView;
import com.su.scott.slibrary.util.TimeUtil;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayPresenterImpl implements MusicPlayPresenter {
    private MusicPlayView mMusicPlayView;
    private LocalAlbumModel mAlbumModel;
    private MusicPlayModel mMusicPlayModel;

    public MusicPlayPresenterImpl(MusicPlayView mMusicPlayView) {
        this.mMusicPlayView = mMusicPlayView;
        this.mAlbumModel = new LocalAlbumModelImpl();
        this.mMusicPlayModel = new MusicPlayModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMusicPlayView.initPreData();
        mMusicPlayView.initToolbar();
        mMusicPlayView.initView();
        mMusicPlayView.initData();
        mMusicPlayView.initListener();
        updateCurrentPlayingSong(false);
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
        if (mMusicPlayView.isPlayRepeatOne()) {
            mMusicPlayView.setCurrentPlayingSongPosition(mMusicPlayModel.getSongPosition(mMusicPlayView.getCurrentPlayingSong(), mMusicPlayView.getCurrentPlayingSongList()));
        } else if (mMusicPlayView.isPlayRepeatAll()) {
            mMusicPlayView.setCurrentPlayingSongPosition(mMusicPlayModel.skipPreviousInRepeatAll(mMusicPlayView.getCurrentPlayingSong(), mMusicPlayView.getCurrentPlayingSongList()));
        } else if (mMusicPlayView.isPlayShuffle()) {
            mMusicPlayView.setCurrentPlayingSongPosition(mMusicPlayModel.shuffle(mMusicPlayView.getCurrentPlayingSong(), mMusicPlayView.getCurrentPlayingSongList()));
        }
        updateCurrentPlayingSong(true);
    }

    @Override
    public void onSkipNextClick(View view) {
        if (mMusicPlayView.isPlayRepeatOne()) {
            mMusicPlayView.setCurrentPlayingSongPosition(mMusicPlayModel.getSongPosition(mMusicPlayView.getCurrentPlayingSong(), mMusicPlayView.getCurrentPlayingSongList()));
        } else if (mMusicPlayView.isPlayRepeatAll()) {
            mMusicPlayView.setCurrentPlayingSongPosition(mMusicPlayModel.skipNextInRepeatAll(mMusicPlayView.getCurrentPlayingSong(), mMusicPlayView.getCurrentPlayingSongList()));
        } else if (mMusicPlayView.isPlayShuffle()) {
            mMusicPlayView.setCurrentPlayingSongPosition(mMusicPlayModel.shuffle(mMusicPlayView.getCurrentPlayingSong(), mMusicPlayView.getCurrentPlayingSongList()));
        }
        updateCurrentPlayingSong(true);
    }

    @Override
    public void onRepeatClick(View view) {
        if (mMusicPlayView.isPlayRepeatAll()) {
            mMusicPlayView.setPlayRepeatOne();
        } else if (mMusicPlayView.isPlayRepeatOne()) {
            mMusicPlayView.setPlayRepeatAll();
        } else if (mMusicPlayView.isPlayShuffle()) {
            if (mMusicPlayView.isRepeatAll()) {
                mMusicPlayView.setPlayRepeatAll();
            } else if (mMusicPlayView.isRepeatOne()) {
                mMusicPlayView.setPlayRepeatOne();
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
    }

    @Override
    public void onServiceConnected() {
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
    public void onPlayProgressUpdate(long currentPositionMillSec) {
        mMusicPlayView.setSeekBarCurrentPosition(currentPositionMillSec);
        mMusicPlayView.setCurrentTime(TimeUtil.millisecondToTimeWithinHour(currentPositionMillSec));
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

    private void updateCurrentPlayingSong(boolean needReveal) {
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

        mMusicPlayView.setSeekBarMax(mMusicPlayView.getCurrentPlayingSong().getDuration());
        mMusicPlayView.setPlayingMusicTitle(mMusicPlayView.getCurrentPlayingSong().getTitle());
        mMusicPlayView.setPlayingMusicArtist(mMusicPlayView.getCurrentPlayingSong().getArtist());
        mMusicPlayView.setCurrentTime(TimeUtil.millisecondToTimeWithinHour(0));
        mMusicPlayView.setTotalPlayTime(TimeUtil.millisecondToTimeWithinHour(mMusicPlayView.getCurrentPlayingSong().getDuration()));
    }
}
