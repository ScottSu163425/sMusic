package com.scott.su.smusic.mvp.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalAlbumModel {

    List<LocalSongEntity> getLocalSongs(Context context);

    List<LocalAlbumEntity> getLocalAlbums(Context context);

    LocalAlbumEntity getLocalAlbum(Context context,long albumId);

    String getAlbumCoverPath(Context context, long albumId);

    Bitmap getAlbumCoverBitmapBlur(Context context, long albumId);
}
