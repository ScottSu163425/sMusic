package com.scott.su.smusic.callback;

import com.scott.su.smusic.entity.LocalSongEntity;


/**
 * Created by asus on 2016/9/15.
 */
public interface MusicPlayServiceCallback {

    void onPlayStart();

    void onPlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong, int currentPosition);

    void onPlayProgressUpdate(long currentPositionMillSec);

    void onPlayPause(long currentPositionMillSec);

    void onPlayResume();

    void onPlayStop();

    void onPlayComplete();
}
