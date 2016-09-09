package com.scott.su.smusic.mvp.view;

import android.graphics.Bitmap;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

import java.util.List;

/**
 * Created by asus on 2016/9/4.
 */
public interface MusicPlayView extends BaseView {

    void setCurrentPlayingSongPosition(int position);

    void setCurrentPlayingSongList(List<LocalSongEntity> songList);

    LocalSongEntity getCurrentPlayingSong();

    List<LocalSongEntity> getCurrentPlayingSongList();

    void setPlayingMusicTitle(String title);

    void setPlayingMusicArtist(String artist);

    void setSeekBarMax(long max);

    void setSeekBarCurrentPosition(long currentPosition);

    void setCurrentTime(String currentPlayTime);

    void setTotalPlayTime(String totalPlayTime);

    void loadCover(String path,boolean needReveal);

    void loadBlurCover(Bitmap bitmap);

    boolean isPlayRepeatAll();

    boolean isRepeatAll();

    boolean isPlayRepeatOne();

    boolean isRepeatOne();

    boolean isPlayShuffle();

    void setPlayRepeatAll();

    void setPlayRepeatOne();

    void setPlayShuffleFromRepeatAll();

    void setPlayShuffleFromRepeatOne();

    boolean isMusicPlaying();

    boolean isMusicPause();

    boolean isMusicStop();

    void setPlayButtonPlaying();

    void setPlayButtonPause();

    void play();

    void pause();
}
