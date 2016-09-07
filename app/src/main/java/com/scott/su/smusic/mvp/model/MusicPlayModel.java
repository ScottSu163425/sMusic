package com.scott.su.smusic.mvp.model;

import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public interface MusicPlayModel {
    int getSongPosition(LocalSongEntity playingSong, List<LocalSongEntity> playSongingList);

    int skipPreviousInRepeatAll(LocalSongEntity playingSong,List<LocalSongEntity> playSongingList);

    int skipNextInRepeatAll(LocalSongEntity playingSong,List<LocalSongEntity> playSongingList);

    int shuffle(LocalSongEntity playingSong,List<LocalSongEntity> playSongingList);
}
