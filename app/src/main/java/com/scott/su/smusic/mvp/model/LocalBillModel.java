package com.scott.su.smusic.mvp.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalBillModel {

    LocalBillEntity getDefaultBill(Context context );

    boolean isDefaultBill(LocalBillEntity billEntity);

    boolean isBillExist(Context context, LocalBillEntity billEntity);

    int getBillPosition(Context context, LocalBillEntity billEntity);

    boolean isBillTitleExist(Context context, LocalBillEntity billEntity);

    void saveOrUpdateBill(Context context, LocalBillEntity billEntity);

    List<LocalSongEntity> getBillSongs(Context context);

    LocalBillEntity getBill(Context context, long billId);

    LocalSongEntity getBillSong(Context context, long songId);

    List<LocalBillEntity> getBills(Context context);

    void addSongToBill(Context context, LocalSongEntity songEntity, LocalBillEntity billToAddSong);

    void addSongsToBill(Context context, List<LocalSongEntity> songEntities, LocalBillEntity billToAddSong);

    List<LocalSongEntity> getSongsByBillId(Context context, long billId);

    boolean isBillContainsSong(LocalBillEntity billEntity, LocalSongEntity songEntity);

    /**
     * Return true if bill contains all these songs;
     *
     * @param billEntity
     * @param songEntities
     * @return
     */
    boolean isBillContainsSongs(LocalBillEntity billEntity, List<LocalSongEntity> songEntities);

    void deleteBill(Context context, LocalBillEntity billEntity);

    void removeBillSong(Context context, LocalBillEntity billEntity, LocalSongEntity songEntity);

    void clearBillSongs(Context context, LocalBillEntity billEntity);

    List<LocalBillEntity> searchBill(Context context,@NonNull String keyword);

    void deleteSong(Context context,LocalSongEntity songEntity);

}
