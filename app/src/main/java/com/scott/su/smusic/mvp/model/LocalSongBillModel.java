package com.scott.su.smusic.mvp.model;

import android.content.Context;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalSongBillModel {

    boolean isDefaultBill(LocalSongBillEntity billEntity);

    boolean isBillExist(Context context, LocalSongBillEntity billEntity);

    boolean isBillTitleExist(Context context, LocalSongBillEntity billEntity);

    void addBill(Context context, LocalSongBillEntity billEntity);

    List<LocalSongEntity> getBillSongs(Context context);

    LocalSongBillEntity getBill(Context context, long billId);

    LocalSongEntity getBillSong(Context context, long songId);

    List<LocalSongBillEntity> getBills(Context context);

    void addSongToBill(Context context, LocalSongEntity songEntity, LocalSongBillEntity billToAddSong);

    void addSongsToBill(Context context, List<LocalSongEntity> songEntities, LocalSongBillEntity billToAddSong);

    List<LocalSongEntity> getSongsByBillId(Context context, long billId);

    boolean isBillContains(LocalSongBillEntity billEntity, LocalSongEntity songEntity);

    /**
     * Return true if bill contains all these songs;
     *
     * @param billEntity
     * @param songEntities
     * @return
     */
    boolean isBillContainsAll(LocalSongBillEntity billEntity, List<LocalSongEntity> songEntities);

    void deleteBill(Context context, LocalSongBillEntity billEntity);

    void deleteBillSong(Context context, LocalSongBillEntity billEntity, LocalSongEntity songEntity);

    void clearBillSongs(Context context, LocalSongBillEntity billEntity);

}
