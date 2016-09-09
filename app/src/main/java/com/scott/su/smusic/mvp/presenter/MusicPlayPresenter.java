package com.scott.su.smusic.mvp.presenter;

import android.view.View;

import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by asus on 2016/9/4.
 */
public interface MusicPlayPresenter extends BasePresenter {
    void onPlayClick(View view);

    void onSkipPreviousClick(View view);

    void onSkipNextClick(View view);

    void onRepeatClick(View view);

    void onShuffleClick(View view);

    void onServiceConnected();

    void onServiceDisconnected();

    void onPlayStart();

    void onPlayProgressUpdate(long currentPositionMillSec);

    void onPlayPause(long currentPositionMillSec);

    void onPlayResume();

    void onPlayStop();

    void onPlayComplete();

}
