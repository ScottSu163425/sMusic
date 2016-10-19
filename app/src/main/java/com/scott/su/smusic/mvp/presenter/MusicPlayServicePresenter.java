package com.scott.su.smusic.mvp.presenter;

import android.view.View;

import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by asus on 2016/9/4.
 */
public interface MusicPlayServicePresenter extends BasePresenter, MusicPlayServiceCallback {
    void onAddToBillMenuItemClick();

    void onAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity);

    void onPlayClick(View view);

    void onSkipPreviousClick(View view);

    void onSkipNextClick(View view);

    void onRepeatClick(View view);

    void onShuffleClick(View view);

    void onServiceConnected();

    void onServiceDisconnected();

    void onSeekStart();

    void onSeekStop(int progress);

}
