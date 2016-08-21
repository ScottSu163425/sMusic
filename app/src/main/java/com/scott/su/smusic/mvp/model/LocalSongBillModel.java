package com.scott.su.smusic.mvp.model;

import android.content.Context;

import com.scott.su.smusic.entity.LocalSongBillEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalSongBillModel {

    List<LocalSongBillEntity> getLocalSongBills(Context context);

    //    Bitmap getLocalSongAlbumCover(Context context,long albumId,int size);
//
//    Bitmap getLocalSongAlbumBlurCover(Context context,long albumId,int size);

}
