package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.view.View;

import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.MusicPlayMainContract;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;
import com.su.scott.slibrary.util.TimeUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayMainPresenterImpl extends BasePresenter<MusicPlayMainContract.MusicPlayMainView>
        implements MusicPlayMainContract.MusicPlayMainPresenter {
    private LocalAlbumModel mAlbumModel;
    private LocalBillModel mBillModel;


    public MusicPlayMainPresenterImpl(MusicPlayMainContract.MusicPlayMainView view) {
        super(view);
        this.mAlbumModel = new LocalAlbumModelImpl();
        this.mBillModel = new LocalBillModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
        getView().initToolbar();
        getView().initView();
        getView().initData();
        getView().initListener();

        if (AppConfig.isPlayRepeatOne(getView().getViewContext())) {
            getView().setPlayRepeatOne(false);
            getView().setServicePlayMode(getView().getCurrentPlayMode());
        } else if (AppConfig.isPlayRepeatAll(getView().getViewContext())) {
            getView().setPlayRepeatAll(false);
            getView().setServicePlayMode(getView().getCurrentPlayMode());
        } else if (AppConfig.isPlayShuffle(getView().getViewContext())) {
            getView().setPlayRepeatShuffle(false);
            getView().setServicePlayMode(getView().getCurrentPlayMode());
        } else {
            getView().setPlayRepeatAll(false);
            getView().setServicePlayMode(getView().getCurrentPlayMode());
            AppConfig.setPlayRepeatAll(getView().getViewContext());
        }

        updateCurrentPlayingSongInfo(false);
    }

    @Override
    public void onPlayClick(View view) {
        if (getView().isMusicPlaying()) {
            getView().pause();
        } else {
            getView().play();
        }
    }

    @Override
    public void onSkipPreviousClick(View view) {
        getView().playPrevious();
    }

    @Override
    public void onSkipNextClick(View view) {
        getView().playNext();
    }

    @Override
    public void onRepeatClick(View view) {
        if (getView().isPlayRepeatAll()) {
            getView().setPlayRepeatOne(true);
            AppConfig.setPlayRepeatOne(getView().getViewContext());
        } else if (getView().isPlayRepeatOne()) {
            getView().setPlayRepeatShuffle(true);
            AppConfig.setPlayRepeatShuffle(getView().getViewContext());
        } else if (getView().isPlayShuffle()) {
            getView().setPlayRepeatAll(true);
            AppConfig.setPlayRepeatAll(getView().getViewContext());
        }
        getView().setServicePlayMode(getView().getCurrentPlayMode());
    }

    @Override
    public void onMusicPlayServiceConnected() {
        if (getView().isMusicPlaying()) {
            getView().setPlayButtonPlaying();
        } else {
            getView().setPlayButtonPause();
        }

        getView().play();
    }

    @Override
    public void onMusicPlayServiceDisconnected() {

    }

    @Override
    public void onPlayStart() {
        getView().setPlayButtonPlaying();
    }

    @Override
    public void onPlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong, int currentPosition) {
        getView().setCurrentPlayingSong(currentPlayingSong);
        updateCurrentPlayingSongInfo(true);
    }

    @Override
    public void onPlayProgressUpdate(long currentPositionMillSec) {
        getView().setSeekBarCurrentPosition(currentPositionMillSec);
    }

    @Override
    public void onPlayPause(long currentPositionMillSec) {
        getView().setPlayButtonPause();
    }

    @Override
    public void onPlayResume() {
        getView().setPlayButtonPlaying();

    }

    @Override
    public void onPlayStop() {
        getView().setPlayButtonPause();
    }

    @Override
    public void onPlayComplete() {
//        getView().setPlayButtonPause();
    }

    @Override
    public void onSeekStart() {
    }

    @Override
    public void onSeekStop(int progress) {
        getView().seekTo(progress);
    }

    private void updateCurrentPlayingSongInfo(boolean needReveal) {
        if (getView() == null) {
            return;
        }

        String path = mAlbumModel.getAlbumCoverPathByAlbumId(getView().getViewContext(), getView().getCurrentPlayingSong().getAlbumId());
        getView().loadCover(path, needReveal);

        if (!AppConfig.isNightModeOn(getView().getViewContext())) {
            Observable.just(getView().getCurrentPlayingSong().getAlbumId())
                    .map(new Func1<Long, Bitmap>() {
                        @Override
                        public Bitmap call(Long albumId) {
                            return mAlbumModel.getAlbumCoverBitmapBlur(getView().getViewContext(), albumId);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            if (!isViewAttaching()) {
                                return;
                            }

                            getView().loadBlurCover(bitmap);
                        }
                    });
        }
        getView().setSeekBarCurrentPosition(0);
        getView().setSeekBarMax(getView().getCurrentPlayingSong().getDuration());
        getView().setPlayingMusicTitle(getView().getCurrentPlayingSong().getTitle());
        getView().setPlayingMusicArtist(getView().getCurrentPlayingSong().getArtist());
        getView().setTotalPlayTime(TimeUtil.millisecondToMMSS(getView().getCurrentPlayingSong().getDuration()));
    }

    @Override
    public void onPlayListItemClick(View itemView, LocalSongEntity entity, int position) {
        getView().setServiceCurrentPlaySong(entity);
        //Need play with callback to update current playing music info.
        getView().playSkip();
    }

    @Override
    public void onPlayListItemRemoveClick(View view, int position, LocalSongEntity entity) {
        getView().removeServiceSong(entity);
        getView().updatePlayListBottomSheet(getView().getServicePlayListSongs(), getView().getServiceCurrentPlayingSong());
    }

    @Override
    public void onPlayListClearClick(View view) {
        getView().clearServiceSongs();
    }

    @Override
    public void onBlurCoverChanged(Bitmap bitmap) {
        if (getView().getMusicPlayMainFragmentCallback() != null) {
            getView().getMusicPlayMainFragmentCallback().onBlurCoverChanged(bitmap);
        }
    }

    @Override
    public void onCoverClick(View view) {
        if (getView().getMusicPlayMainFragmentCallback() != null) {
            getView().getMusicPlayMainFragmentCallback().onCoverClick(view);
        }
    }


}
