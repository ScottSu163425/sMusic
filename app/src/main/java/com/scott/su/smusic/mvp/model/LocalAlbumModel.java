package com.scott.su.smusic.mvp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalAlbumModel {

    List<LocalSongEntity> getLocalSongs(Context context);

    LocalSongEntity getLocalSong(Context context, long songId);

    List<LocalAlbumEntity> getLocalAlbums(Context context);

    LocalAlbumEntity getLocalAlbum(Context context, long albumId);

    String getAlbumCoverPathByAlbumId(Context context, long albumId);

    String getAlbumCoverPathBySongId(Context context, long songId);

    Bitmap getAlbumCoverBitmapBlur(Context context, long albumId);

    List<LocalAlbumEntity> searchLocalAlbum(Context context, @NonNull String keyword);
}
