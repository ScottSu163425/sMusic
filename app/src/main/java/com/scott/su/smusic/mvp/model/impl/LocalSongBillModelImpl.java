package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;
import android.util.Log;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongBillModel;
import com.su.scott.slibrary.manager.DbUtilHelper;
import com.su.scott.slibrary.util.L;
import com.su.scott.slibrary.util.T;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/21.
 */
public class LocalSongBillModelImpl implements LocalSongBillModel {

    @Override
    public List<LocalSongBillEntity> getLocalSongBills(Context context) {
        List<LocalSongBillEntity> songBillEntities = null;
        try {
            songBillEntities = DbUtilHelper.getDefaultDbManager().findAll(LocalSongBillEntity.class);
            L.e("===>getLocalSongBills", songBillEntities == null ? "null" : songBillEntities.toString());
            if (songBillEntities == null || songBillEntities.size() == 0) {
                LocalSongBillEntity songBillEntity = new LocalSongBillEntity();
                songBillEntity.setBillTitle("我喜欢");
                songBillEntity.setId(0);

                List<LocalSongEntity> songEntities = new ArrayList<LocalSongEntity>();
                LocalSongEntity songEntity = new LocalSongEntity();
                songEntity.setTitle("GaGa");
                songEntity.setArtist("LadyGaga");
                songEntities.add(songEntity);
                songBillEntity.setBillSongEntities(songEntities);

                //Keep result not null;
                DbUtilHelper.getDefaultDbManager().saveOrUpdate(songBillEntity);
                songBillEntities = new ArrayList<>();
                songBillEntities.add(songBillEntity);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return songBillEntities;
    }

}
