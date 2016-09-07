package com.scott.su.smusic.mvp.model.impl;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.MusicPlayModel;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MusicPlayModelImpl implements MusicPlayModel {
    @Override
    public int getSongPosition(LocalSongEntity playingSong, List<LocalSongEntity> playSongingList) {
        for (int i = 0; i < playSongingList.size(); i++) {
            if (playingSong.getSongId() == playSongingList.get(i).getSongId()) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int skipPreviousInRepeatAll(LocalSongEntity playingSong, List<LocalSongEntity> playSongingList) {
        int targetPosition = getSongPosition(playingSong, playSongingList) - 1;
        if (targetPosition < 0) {
            targetPosition = playSongingList.size() - 1;
        }
        return targetPosition;
    }

    @Override
    public int skipNextInRepeatAll(LocalSongEntity playingSong, List<LocalSongEntity> playSongingList) {
        int targetPosition = getSongPosition(playingSong, playSongingList) + 1;
        if (targetPosition > playSongingList.size() - 1) {
            targetPosition = 0;
        }
        return targetPosition;
    }

    @Override
    public int shuffle(LocalSongEntity playingSong, List<LocalSongEntity> playSongingList) {
        int currentPosition = getSongPosition(playingSong, playSongingList);
        int targetPosition=new Random().nextInt(playSongingList.size());

        while (targetPosition==currentPosition){
            targetPosition=new Random().nextInt(playSongingList.size());
        }
        return targetPosition;
    }
}
