package com.scott.su.smusic.mvp.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalSongModel {

    List<LocalSongEntity> getLocalSongs(Context context);

    //    Bitmap getLocalSongAlbumCover(Context context,long albumId,int size);
//
//    Bitmap getLocalSongAlbumBlurCover(Context context,long albumId,int size);

    String getAlbumCoverPath(Context context, long albumId);

}
