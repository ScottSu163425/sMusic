package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongBillModel;
import com.su.scott.slibrary.manager.DbUtilHelper;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/21.
 */
public class LocalSongBillModelImpl implements LocalSongBillModel {

    @Override
    public boolean isBillExist(Context context, LocalSongBillEntity billEntity) {
        for (LocalSongBillEntity entity : getBills(context)) {
            if (entity.getBillId() == billEntity.getBillId()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isBillTitleExist(Context context, LocalSongBillEntity billEntity) {
        for (LocalSongBillEntity entity : getBills(context)) {
            if (entity.getBillTitle().equals(billEntity.getBillTitle())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addBill(Context context, LocalSongBillEntity billEntity) {
        try {
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(billEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LocalSongEntity> getBillSongs(Context context) {
        try {
            return DbUtilHelper.getDefaultDbManager().findAll(LocalSongEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public LocalSongBillEntity getBill(Context context, long billId) {
        List<LocalSongBillEntity> bills = getBills(context);
        LocalSongBillEntity bill = null;

        if (bills == null) {
            return null;
        }

        for (LocalSongBillEntity billEntity : bills) {
            if (billEntity.getBillId() == billId) {
                bill = billEntity;
                break;
            }
        }

        return bill;
    }

    @Override
    public List<LocalSongBillEntity> getBills(Context context) {
        List<LocalSongBillEntity> result = null;
        try {
            result = DbUtilHelper.getDefaultDbManager().findAll(LocalSongBillEntity.class);
            if (result != null && result.size() > 0) {
                for (LocalSongBillEntity billEntity : result) {
                    billEntity.setBillSongs(getSongsByBillId(context, billEntity.getBillId()));
                }
            } else {
                //If it is the first time to open, create one bill automatically;
                result = new ArrayList<>();
                LocalSongBillEntity songBillEntity = new LocalSongBillEntity();
                songBillEntity.setBillTitle("我喜欢");
                addBill(context, songBillEntity);
                result.add(songBillEntity);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void addSongToBill(Context context, LocalSongEntity songEntity, LocalSongBillEntity billToAddSong) {
//        LocalSongBillEntity billEntity = getBill(context, billToAddSong.getBillId());

        if (isBillContains(billToAddSong,songEntity)){
            //Already contain this song.
            return;
        }

        billToAddSong.appendBillSongId(songEntity.getSongId());
        songEntity.appendBillId(billToAddSong.getBillId());

        try {
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(billToAddSong);
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(songEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSongsToBill(Context context, List<LocalSongEntity> songEntities, LocalSongBillEntity billToAddSong) {
        for (LocalSongEntity songEntity : songEntities) {
            addSongToBill(context, songEntity, billToAddSong);
        }
    }

    @Override
    public List<LocalSongEntity> getSongsByBillId(Context context, long billId) {
        List<LocalSongEntity> billSongs = getBillSongs(context);
        List<LocalSongEntity> result = new ArrayList<>();

        if (billSongs == null) {
            return null;
        }

        for (LocalSongEntity songEntity : billSongs) {
            if (songEntity.getBillIds().contains(billId + "")) {
                result.add(songEntity);
            }
        }

        return result;
    }

    @Override
    public boolean isBillContains(LocalSongBillEntity billEntity, LocalSongEntity songEntity) {
        if (billEntity.isBillEmpty()) {
            return false;
        }

        return billEntity.getBillSongIds().contains(songEntity.getSongId() + "");
    }

    @Override
    public boolean isBillContainsAll(LocalSongBillEntity billEntity, List<LocalSongEntity> songEntities) {
        for (LocalSongEntity songEntity : songEntities) {
            if (!isBillContains(billEntity, songEntity)) {
                return false;
            }
        }
        return true;
    }


}
