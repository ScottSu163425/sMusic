package com.scott.su.smusic.mvp.model.impl;

import com.scott.su.smusic.db.GreenDaoHelper;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.entity.PlayStatisticEntityDao;
import com.scott.su.smusic.mvp.model.PlayStatisticModel;
import com.su.scott.slibrary.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by asus on 2016/11/13.
 */

public class PlayStatisticModelImpl implements PlayStatisticModel {
    private static final String DIVIDER_DATE = "-";


    @Override
    public void saveOrAddPlayRecord(LocalSongEntity songEntity) {
        PlayStatisticEntity entity = GreenDaoHelper.getDaoSession().getPlayStatisticEntityDao()
                .queryBuilder()
                .where(PlayStatisticEntityDao.Properties.SongId.eq(songEntity.getSongId()))
                .unique();

        if (entity == null) {
            entity = new PlayStatisticEntity();
            entity.copy(songEntity);
            entity.setPlayCount(1);
        } else {
            entity.setPlayCount(entity.getPlayCount() + 1);
        }
        entity.setLastPlayTime(DateUtil.getCurrentYear() + DIVIDER_DATE + DateUtil.getCurrentMonth() + DIVIDER_DATE + DateUtil.getCurrentDay());

        GreenDaoHelper.getDaoSession().getPlayStatisticEntityDao().insertOrReplace(entity);
    }

    @Override
    public int getMaxPlayCount() {
        List<PlayStatisticEntity> list = getTotalPlayStatistic();

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
    public List<PlayStatisticEntity> getRecent7DaysStatistic() {
        return null;
    }

    @Override
    public List<PlayStatisticEntity> getTotalPlayStatistic() {
        List<PlayStatisticEntity> result;

        result = GreenDaoHelper.getDaoSession().getPlayStatisticEntityDao().loadAll();
        sortDes(result);

        if (result == null) {
            result = Collections.EMPTY_LIST;
        }

        return result;
    }

    @Override
    public List<LocalSongEntity> getLocalSongsByPlayStatistic(List<PlayStatisticEntity> playStatisticEntityList) {
        List<LocalSongEntity> songEntityList = new ArrayList<>();

        for (PlayStatisticEntity statisticEntity : playStatisticEntityList) {
            songEntityList.add(statisticEntity.toLocalSongEntity());
        }

//        Collections.reverse(songEntityList);

        return songEntityList;
    }

    @Override
    public void deletePlayRecord(LocalSongEntity songEntity) {
        if (songEntity == null) {
            return;
        }

        if (!contains(songEntity)) {
            return;
        }

        GreenDaoHelper.getDaoSession().getPlayStatisticEntityDao().delete(getPlayStatisticEntity(songEntity));
    }

    @Override
    public boolean contains(LocalSongEntity songEntity) {
        if (songEntity == null) {
            return false;
        }

        List<PlayStatisticEntity> list = getTotalPlayStatistic();

        if (list.isEmpty()) {
            return false;
        }

        for (PlayStatisticEntity entity : list) {
            if (entity.getSongId() == songEntity.getSongId()) {
                return true;
            }
        }

        return false;
    }

    private PlayStatisticEntity getPlayStatisticEntity(LocalSongEntity songEntity) {
        if (songEntity == null) {
            return null;
        }

        List<PlayStatisticEntity> list = getTotalPlayStatistic();

        if (list.isEmpty()) {
            return null;
        }

        for (PlayStatisticEntity entity : list) {
            if (entity.getSongId() == songEntity.getSongId()) {
                return entity;
            }
        }

        return null;
    }

    private void sortDes(List<PlayStatisticEntity> result) {
        Collections.sort(result, new Comparator<PlayStatisticEntity>() {
            @Override
            public int compare(PlayStatisticEntity o1, PlayStatisticEntity o2) {
                return o2.getPlayCount() - o1.getPlayCount();
            }
        });
    }


}
