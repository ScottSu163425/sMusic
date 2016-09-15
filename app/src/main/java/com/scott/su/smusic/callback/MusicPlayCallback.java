package com.scott.su.smusic.callback;

/**
 * Created by asus on 2016/9/15.
 */
public interface MusicPlayCallback {
    void onPlayStart();

    void onPlayProgressUpdate(long currentPositionMillSec);

    void onPlayPause(long currentPositionMillSec);

    void onPlayResume();

    void onPlayStop();

    void onPlayComplete();
}
