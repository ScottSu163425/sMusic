package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.view.View;

import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.presenter.MusicPlayMainPresenter;
import com.scott.su.smusic.mvp.view.MusicPlayMainView;
import com.su.scott.slibrary.util.TimeUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayMainPresenterImpl implements MusicPlayMainPresenter {
    private MusicPlayMainView mMusicPlayMainView;
    private LocalAlbumModel mAlbumModel;
    private LocalBillModel mBillModel;


    public MusicPlayMainPresenterImpl(MusicPlayMainView mMusicPlayMainView) {
        this.mMusicPlayMainView = mMusicPlayMainView;
        this.mAlbumModel = new LocalAlbumModelImpl();
        this.mBillModel = new LocalBillModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMusicPlayMainView.initPreData();
        mMusicPlayMainView.initToolbar();
        mMusicPlayMainView.initView();
        mMusicPlayMainView.initData();
        mMusicPlayMainView.initListener();

        if (AppConfig.isPlayRepeatOne(mMusicPlayMainView.getViewContext())) {
            mMusicPlayMainView.setPlayRepeatOne(false);
            mMusicPlayMainView.setServicePlayMode(mMusicPlayMainView.getCurrentPlayMode());
        } else if (AppConfig.isPlayRepeatAll(mMusicPlayMainView.getViewContext())) {
            mMusicPlayMainView.setPlayRepeatAll(false);
            mMusicPlayMainView.setServicePlayMode(mMusicPlayMainView.getCurrentPlayMode());
        } else if (AppConfig.isPlayShuffle(mMusicPlayMainView.getViewContext())) {
            mMusicPlayMainView.setPlayRepeatShuffle(false);
            mMusicPlayMainView.setServicePlayMode(mMusicPlayMainView.getCurrentPlayMode());
        } else {
            mMusicPlayMainView.setPlayRepeatAll(false);
            mMusicPlayMainView.setServicePlayMode(mMusicPlayMainView.getCurrentPlayMode());
            AppConfig.setPlayRepeatAll(mMusicPlayMainView.getViewContext());
        }

        updateCurrentPlayingSongInfo(false);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
        if (mMusicPlayMainView != null) {
            mMusicPlayMainView = null;
        }
    }

    @Override
    public void onPlayClick(View view) {
        if (mMusicPlayMainView.isMusicPlaying()) {
            mMusicPlayMainView.pause();
        } else {
            mMusicPlayMainView.play();
        }
    }

    @Override
    public void onSkipPreviousClick(View view) {
        mMusicPlayMainView.playPrevious();
    }

    @Override
    public void onSkipNextClick(View view) {
        mMusicPlayMainView.playNext();
    }

    @Override
    public void onRepeatClick(View view) {
        if (mMusicPlayMainView.isPlayRepeatAll()) {
            mMusicPlayMainView.setPlayRepeatOne(true);
            AppConfig.setPlayRepeatOne(mMusicPlayMainView.getViewContext());
        } else if (mMusicPlayMainView.isPlayRepeatOne()) {
            mMusicPlayMainView.setPlayRepeatShuffle(true);
            AppConfig.setPlayRepeatShuffle(mMusicPlayMainView.getViewContext());
        } else if (mMusicPlayMainView.isPlayShuffle()) {
            mMusicPlayMainView.setPlayRepeatAll(true);
            AppConfig.setPlayRepeatAll(mMusicPlayMainView.getViewContext());
        }
        mMusicPlayMainView.setServicePlayMode(mMusicPlayMainView.getCurrentPlayMode());
    }

    @Override
    public void onMusicPlayServiceConnected() {
        if (mMusicPlayMainView.isMusicPlaying()) {
            mMusicPlayMainView.setPlayButtonPlaying();
        } else {
            mMusicPlayMainView.setPlayButtonPause();
        }

        mMusicPlayMainView.play();
    }

    @Override
    public void onMusicPlayServiceDisconnected() {

    }

    @Override
    public void onPlayStart() {
        mMusicPlayMainView.setPlayButtonPlaying();
    }

    @Override
    public void onPlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong,int currentPosition) {
        mMusicPlayMainView.setCurrentPlayingSong(currentPlayingSong);
        updateCurrentPlayingSongInfo(true);
    }

    @Override
    public void onPlayProgressUpdate(long currentPositionMillSec) {
        mMusicPlayMainView.setSeekBarCurrentPosition(currentPositionMillSec);
    }

    @Override
    public void onPlayPause(long currentPositionMillSec) {
        mMusicPlayMainView.setPlayButtonPause();
    }

    @Override
    public void onPlayResume() {
        mMusicPlayMainView.setPlayButtonPlaying();

    }

    @Override
    public void onPlayStop() {
        mMusicPlayMainView.setPlayButtonPause();
    }

    @Override
    public void onPlayComplete() {
//        mMusicPlayMainView.setPlayButtonPause();
    }

    @Override
    public void onSeekStart() {
    }

    @Override
    public void onSeekStop(int progress) {
        mMusicPlayMainView.seekTo(progress);
    }

    private void updateCurrentPlayingSongInfo(boolean needReveal) {
        String path = mAlbumModel.getAlbumCoverPathByAlbumId(mMusicPlayMainView.getViewContext(), mMusicPlayMainView.getCurrentPlayingSong().getAlbumId());
        mMusicPlayMainView.loadCover(path, needReveal);

        if (!AppConfig.isNightModeOn(mMusicPlayMainView.getViewContext())) {
            Observable.just(mMusicPlayMainView.getCurrentPlayingSong().getAlbumId())
                    .map(new Func1<Long, Bitmap>() {
                        @Override
                        public Bitmap call(Long albumId) {
                            return mAlbumModel.getAlbumCoverBitmapBlur(mMusicPlayMainView.getViewContext(), albumId);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            if (mMusicPlayMainView != null) {
                                mMusicPlayMainView.loadBlurCover(bitmap);
                            }
                        }
                    });
        }
        mMusicPlayMainView.setSeekBarCurrentPosition(0);
        mMusicPlayMainView.setSeekBarMax(mMusicPlayMainView.getCurrentPlayingSong().getDuration());
        mMusicPlayMainView.setPlayingMusicTitle(mMusicPlayMainView.getCurrentPlayingSong().getTitle());
        mMusicPlayMainView.setPlayingMusicArtist(mMusicPlayMainView.getCurrentPlayingSong().getArtist());
        mMusicPlayMainView.setTotalPlayTime(TimeUtil.millisecondToMMSS(mMusicPlayMainView.getCurrentPlayingSong().getDuration()));
    }

    @Override
    public void onPlayListItemClick(View itemView, LocalSongEntity entity, int position) {
        mMusicPlayMainView.setServiceCurrentPlaySong(entity);
        //Need play with callback to update current playing music info.
        mMusicPlayMainView.playSkip();
    }

    @Override
    public void onPlayListItemRemoveClick(View view, int position, LocalSongEntity entity) {
        mMusicPlayMainView.removeServiceSong(entity);
        mMusicPlayMainView.updatePlayListBottomSheet(mMusicPlayMainView.getServicePlayListSongs(), mMusicPlayMainView.getServiceCurrentPlayingSong());
    }

    @Override
    public void onPlayListClearClick(View view) {
        mMusicPlayMainView.clearServiceSongs();
    }

    @Override
    public void onBlurCoverChanged(Bitmap bitmap) {
        if (mMusicPlayMainView.getMusicPlayMainFragmentCallback() != null) {
            mMusicPlayMainView.getMusicPlayMainFragmentCallback().onBlurCoverChanged(bitmap);
        }
    }

    @Override
    public void onCoverClick(View view) {
        if (mMusicPlayMainView.getMusicPlayMainFragmentCallback() != null) {
            mMusicPlayMainView.getMusicPlayMainFragmentCallback().onCoverClick(view);
        }
    }


}
