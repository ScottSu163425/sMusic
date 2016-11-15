package com.scott.su.smusic.mvp.model;

import android.content.Context;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.entity.PlayStatisticEntity;

import java.util.List;

/**
 * Created by asus on 2016/11/13.
 */

public interface PlayStatisticModel {
    void saveOrAddPlayRecord(Context context,LocalSongEntity songEntity);

    int getMaxPlayCount(Context context);

    List<PlayStatisticEntity> getRecent7DaysStatistic(Context context);

    /**
     * Return all play statistic order by play count descending.
     * @param context
     * @return
     */
    List<PlayStatisticEntity> getTotalPlayStatistic(Context context);
}
