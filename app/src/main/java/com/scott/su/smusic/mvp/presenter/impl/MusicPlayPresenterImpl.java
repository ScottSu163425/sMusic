package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.view.MusicPlayView;
import com.su.scott.slibrary.util.TimeUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayPresenterImpl implements MusicPlayPresenter {
    private MusicPlayView mMusicPlayView;
    private LocalAlbumModel mAlbumModel;
    private LocalBillModel mBillModel;


    public MusicPlayPresenterImpl(MusicPlayView mMusicPlayView) {
        this.mMusicPlayView = mMusicPlayView;
        this.mAlbumModel = new LocalAlbumModelImpl();
        this.mBillModel = new LocalBillModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMusicPlayView.initPreData();
        mMusicPlayView.initToolbar();
        mMusicPlayView.initView();
        mMusicPlayView.initData();
        mMusicPlayView.initListener();

        if (AppConfig.isPlayRepeatOne(mMusicPlayView.getViewContext())) {
            mMusicPlayView.setPlayRepeatOne(false);
            mMusicPlayView.setServicePlayMode(mMusicPlayView.getCurrentPlayMode());
        } else if (AppConfig.isPlayRepeatAll(mMusicPlayView.getViewContext())) {
            mMusicPlayView.setPlayRepeatAll(false);
            mMusicPlayView.setServicePlayMode(mMusicPlayView.getCurrentPlayMode());
        } else if (AppConfig.isPlayShuffle(mMusicPlayView.getViewContext())) {
            mMusicPlayView.setPlayRepeatShuffle(false);
            mMusicPlayView.setServicePlayMode(mMusicPlayView.getCurrentPlayMode());
        } else {
            mMusicPlayView.setPlayRepeatAll(false);
            mMusicPlayView.setServicePlayMode(mMusicPlayView.getCurrentPlayMode());
            AppConfig.setPlayRepeatAll(mMusicPlayView.getViewContext());
        }

        updateCurrentPlayingSongInfo(false);
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
        if (mMusicPlayView != null) {
            mMusicPlayView = null;
        }
    }

    @Override
    public void onAddToBillMenuItemClick() {
        mMusicPlayView.showBillSelectionDialog(mMusicPlayView.getCurrentPlayingSong());
    }

    @Override
    public void onAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            mMusicPlayView.showSnackbarShort(mMusicPlayView.getSnackbarParent(),
                    mMusicPlayView.getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(mMusicPlayView.getViewContext(), songEntity, billEntity);
        AppConfig.setNeedToRefreshLocalBillDisplay(mMusicPlayView.getViewContext(), true);
        mMusicPlayView.showSnackbarShort(mMusicPlayView.getSnackbarParent(), mMusicPlayView.getViewContext().getString(R.string.add_successfully));
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
            mMusicPlayView.setPlayRepeatOne(true);
            AppConfig.setPlayRepeatOne(mMusicPlayView.getViewContext());
        } else if (mMusicPlayView.isPlayRepeatOne()) {
            mMusicPlayView.setPlayRepeatShuffle(true);
            AppConfig.setPlayRepeatShuffle(mMusicPlayView.getViewContext());
        } else if (mMusicPlayView.isPlayShuffle()) {
            mMusicPlayView.setPlayRepeatAll(true);
            AppConfig.setPlayRepeatAll(mMusicPlayView.getViewContext());
        }
        mMusicPlayView.setServicePlayMode(mMusicPlayView.getCurrentPlayMode());
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
    public void onPlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong) {
        mMusicPlayView.setCurrentPlayingSong(currentPlayingSong);
        updateCurrentPlayingSongInfo(true);
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
//        mMusicPlayView.setPlayButtonPause();
    }

    @Override
    public void onSeekStart() {
    }

    @Override
    public void onSeekStop(int progress) {
        mMusicPlayView.seekTo(progress);
    }

    private void updateCurrentPlayingSongInfo(boolean needReveal) {
        String path = mAlbumModel.getAlbumCoverPathByAlbumId(mMusicPlayView.getViewContext(), mMusicPlayView.getCurrentPlayingSong().getAlbumId());
        mMusicPlayView.loadCover(path, needReveal);

        if (!AppConfig.isNightModeOn(mMusicPlayView.getViewContext())) {
            Observable.just(mMusicPlayView.getCurrentPlayingSong().getAlbumId())
                    .map(new Func1<Long, Bitmap>() {
                        @Override
                        public Bitmap call(Long albumId) {
                            return mAlbumModel.getAlbumCoverBitmapBlur(mMusicPlayView.getViewContext(), albumId);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            if (mMusicPlayView != null) {
                                mMusicPlayView.loadBlurCover(bitmap);
                            }
                        }
                    });
        }
        mMusicPlayView.setSeekBarCurrentPosition(0);
        mMusicPlayView.setSeekBarMax(mMusicPlayView.getCurrentPlayingSong().getDuration());
        mMusicPlayView.setPlayingMusicTitle(mMusicPlayView.getCurrentPlayingSong().getTitle());
        mMusicPlayView.setPlayingMusicArtist(mMusicPlayView.getCurrentPlayingSong().getArtist());
        mMusicPlayView.setTotalPlayTime(TimeUtil.millisecondToMMSS(mMusicPlayView.getCurrentPlayingSong().getDuration()));
    }

    @Override
    public void onPlayListItemClick(View itemView, LocalSongEntity entity, int position) {
        mMusicPlayView.setServiceCurrentPlaySong(entity);
        //Need play with callback to update current playing music info.
        mMusicPlayView.playSkip();
    }

    @Override
    public void onPlayListItemRemoveClick(View view, int position, LocalSongEntity entity) {
        mMusicPlayView.removeServiceSong(entity);
        mMusicPlayView.updatePlayListBottomSheet(mMusicPlayView.getServicePlayListSongs(), mMusicPlayView.getServiceCurrentPlayingSong());
    }

    @Override
    public void onPlayListClearClick(View view) {
        mMusicPlayView.clearServiceSongs();
    }
}
