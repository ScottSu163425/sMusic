package com.scott.su.smusic.mvp.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.entity.PlayStatisticEntity;

import java.util.List;

/**
 * Created by asus on 2016/11/13.
 */

public interface PlayStatisticModel {
    void saveOrAddPlayRecord( LocalSongEntity songEntity);

    int getMaxPlayCount();

    List<PlayStatisticEntity> getRecent7DaysStatistic();

    /**
     * Return all play statistic order by play count descending.
     *
     * @param context
     * @return
     */
    List<PlayStatisticEntity> getTotalPlayStatistic();

    List<LocalSongEntity> getLocalSongsByPlayStatistic(@NonNull List<PlayStatisticEntity> playStatisticEntityList);

    void deletePlayRecord(LocalSongEntity songEntity);

    boolean contains(LocalSongEntity songEntity);

}
