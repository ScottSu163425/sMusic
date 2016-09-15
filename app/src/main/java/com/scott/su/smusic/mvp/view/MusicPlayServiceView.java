package com.scott.su.smusic.mvp.view;

import android.support.annotation.NonNull;

import com.scott.su.smusic.callback.MusicPlayCallback;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.service.MusicPlayService;

import java.util.List;

/**
 * Created by asus on 2016/9/15.
 */
public interface MusicPlayServiceView {
    PlayStatus getCurrentPlayStatus();

    void setPlaySong(LocalSongEntity currentPlaySong, List<LocalSongEntity> playSongs);

    void setPlayMode(PlayMode playMode);

    void play();

    void pause();

    void seekTo(int position);

    void registerPlayCallback(@NonNull MusicPlayCallback callback);

    void unregisterPlayCallback();
}
