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

    List<LocalSongEntity> getLocalSongsBySongIds(Context context, long... songIds);

}
