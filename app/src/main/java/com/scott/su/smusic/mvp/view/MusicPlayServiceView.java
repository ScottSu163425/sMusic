package com.scott.su.smusic.mvp.view;

import android.support.annotation.NonNull;

import com.scott.su.smusic.callback.MusicPlayCallback;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.ArrayList;

/**
 * Created by asus on 2016/9/15.
 */
public interface MusicPlayServiceView {
    PlayStatus getCurrentPlayStatus();

    LocalSongEntity getCurrentPlayingSong();

    ArrayList<LocalSongEntity> getCurrentPlayingSongs();

    void setPlaySong(LocalSongEntity currentPlaySong, ArrayList<LocalSongEntity> playSongs);

    void setPlayMode(PlayMode playMode);

    void play();

    void pause();

    void seekTo(int position);

    void playPrevious();

    void playNext();

    void registerPlayCallback(@NonNull MusicPlayCallback callback);

    void unregisterPlayCallback();
}
