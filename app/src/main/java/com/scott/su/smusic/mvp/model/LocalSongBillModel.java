package com.scott.su.smusic.mvp.model;

import android.content.Context;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalSongBillModel {

    void addBill(Context context, LocalSongBillEntity billEntity);

    List<LocalSongEntity> getBillSongs(Context context);

    LocalSongBillEntity getBill(Context context, long billId);

    List<LocalSongBillEntity> getBills(Context context);

    void addSongToBill(Context context, LocalSongEntity songEntity, long billId);

    void addSongsToBill(Context context, List<LocalSongEntity> songEntities, long billId);

    List<LocalSongEntity> getSongsByBillId(Context context, long billId);

}
