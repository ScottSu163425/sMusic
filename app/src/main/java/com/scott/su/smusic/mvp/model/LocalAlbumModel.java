package com.scott.su.smusic.mvp.model;

import android.content.Context;

import com.scott.su.smusic.entity.LocalAlbumEntity;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalAlbumModel {

    List<LocalAlbumEntity> getLocalAlbums(Context context);

}
