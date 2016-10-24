package com.scott.su.smusic.util;

import android.support.annotation.NonNull;

import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by asus on 2016/10/1.
 */

public class MusicPlayUtil {
    public static LocalSongEntity getPreviousSong(@NonNull LocalSongEntity currentSong, @NonNull List<LocalSongEntity> songList, @NonNull PlayMode playMode) {
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

    public static LocalSongEntity getNextSong(@NonNull LocalSongEntity currentSong, @NonNull List<LocalSongEntity> songList, @NonNull PlayMode playMode) {
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

    public static int getSongPosition(@NonNull LocalSongEntity currentSong, @NonNull List<LocalSongEntity> songList) {
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

    public static void addSongToPlayList(@NonNull List<LocalSongEntity> playList, @NonNull LocalSongEntity songEntity) {
        if (playList.isEmpty()) {
            playList.add(songEntity);
            return;
        }

        if (isPlayListContains(playList, songEntity)) {
            return;
        }

//        if (isPlayListContains(playList, songEntity)) {
//            //If the song is already exists in the play list, put the song on the first position of the list;
//            playList.remove(getSongPosition(songEntity, playList));
//            return;
//        }

//        playList.add(0, songEntity);
        playList.add(songEntity);
    }


    public static void addSongsToPlayList(@NonNull List<LocalSongEntity> playList, @NonNull List<LocalSongEntity> songEntities, @NonNull LocalSongEntity currentPlayingSong) {
        if (songEntities.isEmpty()) {
            return;
        }

        if (playList.isEmpty()) {
            playList.addAll(songEntities);
            return;
        }

//        //Keep the order;
//        for (int i = songEntities.size() - 1; i >= 0; i--) {
//            if (songEntities.get(i).getSongId() == currentPlayingSong.getSongId()) {
//                continue;
//            }
//            addSongToPlayList(playList, songEntities.get(i));
//        }
//        addSongToPlayList(playList, currentPlayingSong);

        for (LocalSongEntity songEntity : songEntities) {
            addSongToPlayList(playList, songEntity);
        }
    }

    public static boolean isPlayListContains(@NonNull List<LocalSongEntity> playList, @NonNull LocalSongEntity songEntity) {
        for (LocalSongEntity localSongEntity : playList) {
            if (localSongEntity.getSongId() == songEntity.getSongId()) {
                return true;
            }
        }
        return false;
    }

}
