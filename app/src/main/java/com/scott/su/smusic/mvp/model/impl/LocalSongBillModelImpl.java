package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongBillModel;
import com.su.scott.slibrary.manager.DbUtilHelper;
import com.su.scott.slibrary.util.L;

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
                //First time open,auto create one bill;
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
    public void addSongToBill(Context context, LocalSongEntity songEntity, long billId) {
        LocalSongBillEntity billEntity = getBill(context, billId);

        billEntity.appendBillSongId(songEntity.getSongId());
        try {
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(billEntity);
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(songEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSongsToBill(Context context, List<LocalSongEntity> songEntities, long billId) {
        for (LocalSongEntity songEntity : songEntities) {
            addSongToBill(context, songEntity, billId);
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

}
