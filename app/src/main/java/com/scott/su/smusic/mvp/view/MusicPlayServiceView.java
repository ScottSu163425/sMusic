package com.scott.su.smusic.mvp.view;

import android.support.annotation.NonNull;

import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.ArrayList;

/**
 * Created by asus on 2016/9/15.
 */
public interface MusicPlayServiceView {
    PlayStatus getServiceCurrentPlayStatus();

    LocalSongEntity getServiceCurrentPlayingSong();

    ArrayList<LocalSongEntity> getServicePlayListSongs();

    void setServiceCurrentPlaySong(LocalSongEntity currentPlaySong);

    void addServicePlayListSongs(ArrayList<LocalSongEntity> playSongs);

    void setServicePlayMode(PlayMode playMode);

    PlayMode getServicePlayMode();

    /**
     * Play without song change callback
     */
    void play();

    /**
     * Play with song change callback
     */
    void playSkip();

    void play(int position);

    void pause();

    void seekTo(int position);

    void playPrevious();

    void playNext();

    void removeServiceSong(LocalSongEntity songEntity);

    void clearServiceSongs();

    void registerServicePlayCallback(@NonNull MusicPlayServiceCallback callback);

    void unregisterServicePlayCallback(@NonNull MusicPlayServiceCallback callback);

}
