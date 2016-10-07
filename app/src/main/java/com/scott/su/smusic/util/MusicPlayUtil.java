package com.scott.su.smusic.util;

import android.support.annotation.NonNull;

import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;
import java.util.Random;

/**
 * Created by asus on 2016/10/1.
 */

public class MusicPlayUtil {
    public static LocalSongEntity getPreviousSong(LocalSongEntity currentSong, List<LocalSongEntity> songList, PlayMode playMode) {
        if (playMode == PlayMode.RepeatOne) {
            return currentSong;
        }

        if (songList.size() == 0) {
            return null;
        }

        if (songList.size() == 1) {
            return songList.get(0);
        }

        if (playMode == PlayMode.RepeatAll) {
            int nextSongPosition = getSongPosition(currentSong, songList) - 1;
            if (nextSongPosition < 0) {
                nextSongPosition = songList.size() - 1;
            }
            return songList.get(nextSongPosition);
        }

        if (playMode == PlayMode.Shuffle) {
            return songList.get(getRandomPosition(getSongPosition(currentSong, songList), songList.size()));
        }

        return null;
    }

    public static LocalSongEntity getNextSong(LocalSongEntity currentSong, List<LocalSongEntity> songList, PlayMode playMode) {
        if (playMode == PlayMode.RepeatOne) {
            return currentSong;
        }

        if (songList.size() == 0) {
            return null;
        }

        if (songList.size() == 1) {
            return songList.get(0);
        }

        if (playMode == PlayMode.RepeatAll) {
            int previousSongPosition = getSongPosition(currentSong, songList) + 1;
            if (previousSongPosition == songList.size()) {
                previousSongPosition = 0;
            }
            return songList.get(previousSongPosition);
        }

        if (playMode == PlayMode.Shuffle) {
            return songList.get(getRandomPosition(getSongPosition(currentSong, songList), songList.size()));
        }

        return null;
    }

    public static int getSongPosition(LocalSongEntity currentSong, List<LocalSongEntity> songList) {
        int size = songList.size();
        for (int i = 0; i < size; i++) {
            LocalSongEntity songEntity = songList.get(i);
            if (songEntity.getSongId() == currentSong.getSongId()) {
                return i;
            }
        }

        return -1;
    }

    public static int getRandomPosition(int currentPosition, int size) {
        if (size == 1) {
            return 0;
        }

        Random random = new Random();
        int randomPosition = random.nextInt(size);

        while (randomPosition == currentPosition) {
            randomPosition = random.nextInt(size);
        }

        return randomPosition;
    }

}
