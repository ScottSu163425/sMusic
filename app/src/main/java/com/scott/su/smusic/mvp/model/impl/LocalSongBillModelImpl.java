package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;
import android.text.TextUtils;

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
    private static final String BILL_NAME_DEFAULT_BILL = "我喜欢";
    private static final long BILL_ID_DEFAULT_BILL = 1111111;

    @Override
    public boolean isDefaultBill(LocalSongBillEntity billEntity) {
        return billEntity.getBillId() == BILL_ID_DEFAULT_BILL;
    }

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
    public LocalSongEntity getBillSong(Context context, long songId) {
        for (LocalSongEntity billSongEntity : getBillSongs(context)) {
            if (billSongEntity.getSongId() == songId) {
                return billSongEntity;
            }
        }
        return null;
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
                LocalSongBillEntity defaultBill = new LocalSongBillEntity();
                defaultBill.setBillTitle(BILL_NAME_DEFAULT_BILL);
                defaultBill.setBillId(BILL_ID_DEFAULT_BILL);
                addBill(context, defaultBill);
                result.add(defaultBill);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void addSongToBill(Context context, LocalSongEntity songEntity, LocalSongBillEntity billToAddSong) {
//        LocalSongBillEntity billEntity = getBill(context, billToAddSong.getBillId());

        if (isBillContains(billToAddSong, songEntity)) {
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
            if (!TextUtils.isEmpty(songEntity.getBillIds()) && songEntity.getBillIds().contains(billId + "")) {
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

    @Override
    public void deleteBill(Context context, LocalSongBillEntity billEntity) {
        if (!isBillExist(context, billEntity)) {
            return;
        }

        try {
            DbUtilHelper.getDefaultDbManager().delete(billEntity);
            if (!billEntity.isBillEmpty()) {
                clearBillSongs(context, billEntity);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBillSong(Context context, LocalSongBillEntity billEntity, LocalSongEntity songEntity) {
        LocalSongEntity billSongEntity = getBillSong(context, songEntity.getSongId());
        if (!isBillContains(billEntity, billSongEntity)) {
            return;
        }

        billSongEntity.removeBillId(billEntity.getBillId());
        billEntity.removeSongId(billSongEntity.getSongId());

        try {
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(billSongEntity);
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(billEntity);
            if (!billSongEntity.isBelongingToAnyBill()) {
                DbUtilHelper.getDefaultDbManager().delete(billSongEntity);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearBillSongs(Context context, LocalSongBillEntity billEntity) {
        List<LocalSongEntity> billSongs = getBillSongs(context);

        try {
            for (LocalSongEntity songEntity : billSongs) {
                if (isBillContains(billEntity, songEntity)) {
                    songEntity.removeBillId(billEntity.getBillId());
                    billEntity.removeSongId(songEntity.getSongId());
                    DbUtilHelper.getDefaultDbManager().saveOrUpdate(songEntity);
                    //Delete the song if the song doesn`t belong to any bill;
                    if (!songEntity.isBelongingToAnyBill()) {
                        DbUtilHelper.getDefaultDbManager().delete(songEntity);
                    }

                }
            }
            //Update the bill;
            DbUtilHelper.getDefaultDbManager().saveOrUpdate(billEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
