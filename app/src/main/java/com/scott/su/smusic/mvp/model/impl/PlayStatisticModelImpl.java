package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.model.PlayStatisticModel;
import com.su.scott.slibrary.manager.DbUtilHelper;
import com.su.scott.slibrary.util.DateUtil;
import com.su.scott.slibrary.util.TimeUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/13.
 */

public class PlayStatisticModelImpl implements PlayStatisticModel {
    private static final String DIVIDER_DATE = "-";

    @Override
    public void saveOrAddPlayRecord(Context context, LocalSongEntity songEntity) {
        DbManager dbManager = DbUtilHelper.getDefaultDbManager();

        try {
            PlayStatisticEntity entity = dbManager.selector(PlayStatisticEntity.class)
                    .where("songId", "=", songEntity.getSongId())
                    .findFirst();
            if (entity == null) {
                entity = new PlayStatisticEntity();
                entity.copy(songEntity);
                entity.setPlayCount(1);
            } else {
                entity.setPlayCount(entity.getPlayCount() + 1);
            }
            entity.setLastPlayTime(DateUtil.getCurrentYear() + DIVIDER_DATE + DateUtil.getCurrentMonth() + DIVIDER_DATE + DateUtil.getCurrentDay());

            dbManager.saveOrUpdate(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getMaxPlayCount(Context context) {
        List<PlayStatisticEntity> list = getTotalPlayStatistic(context);

        if (list.isEmpty()) {
            return 0;
        }

        int maxCount = 0;
        for (PlayStatisticEntity entity : list) {
            if (entity.getPlayCount() > maxCount) {
                maxCount = entity.getPlayCount();
            }
        }

        return maxCount;
    }

    @Override
    public List<PlayStatisticEntity> getRecent7DaysStatistic(Context context) {
        return null;
    }

    @Override
    public List<PlayStatisticEntity> getTotalPlayStatistic(Context context) {
        List<PlayStatisticEntity> result;

        try {
            result = DbUtilHelper.getDefaultDbManager().findAll(PlayStatisticEntity.class);
        } catch (DbException e) {
            e.printStackTrace();

            result = new ArrayList<>();
            return result;
        }

        if (result == null) {
            result = new ArrayList<>();
        }

        return result;
    }

}
