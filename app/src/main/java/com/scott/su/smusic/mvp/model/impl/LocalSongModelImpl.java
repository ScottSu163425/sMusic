package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;

import com.scott.su.smusic.cache.CoverPathCache;
import com.scott.su.smusic.cache.LocalSongEntityCache;

import com.su.scott.slibrary.util.L;
import com.su.scott.slibrary.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongModelImpl implements LocalSongModel {


    public LocalSongModelImpl() {
    }

    @Override
    public List<LocalSongEntity> getLocalSongs(Context context) {
        LocalAlbumModel localAlbumModel = new LocalAlbumModelImpl();
        List<LocalSongEntity> songEntities = new ArrayList<>();
        if (context == null) {
            return songEntities;
        }

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while (cursor.moveToNext()) {
            int isMusic = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0) {
                continue;
            }

            //Get entity from com.scott.su.smusic.cache directly if it exists in com.scott.su.smusic.cache.
            long songId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            LocalSongEntity entityFromCache = LocalSongEntityCache.getInstance().get(songId + "");
            if (entityFromCache != null) {
                songEntities.add(entityFromCache);
                continue;
            }

            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            LocalSongEntity localSongEntity = new LocalSongEntity();
            localSongEntity.setSongId(songId);
            localSongEntity.setTitle(title);
            localSongEntity.setArtist(artist);
            localSongEntity.setAlbum(album);
            localSongEntity.setAlbumId(albumId);
            localSongEntity.setDuration(duration);
            localSongEntity.setSize(size);
            localSongEntity.setPath(path);

            //Get and set covert path from LruCache if exists,otherwise com.scott.su.smusic.cache it.
            String coverPath = CoverPathCache.getInstance().get(albumId + "");
            if (TextUtils.isEmpty(coverPath)) {
                coverPath = localAlbumModel.getAlbumCoverPathByAlbumId(context, albumId);
                CoverPathCache.getInstance().put(albumId + "", coverPath);
            }

            localSongEntity.setCoverPath(coverPath);

            //Put the whole entity into the com.scott.su.smusic.cache;
            LocalSongEntityCache.getInstance().put(songId + "", localSongEntity);

            songEntities.add(localSongEntity);
        }
        cursor.close();
        return songEntities;
    }


    @Override
    public LocalSongEntity getLocalSong(Context context, long songId) {
        List<LocalSongEntity> songs = getLocalSongs(context);

        if (songs.isEmpty()) {
            return null;
        }

        for (LocalSongEntity songEntity : songs) {
            if (songEntity.getSongId() == songId) {
                return songEntity;
            }
        }
        return null;
    }

    @Override
    public List<LocalSongEntity> getLocalSongsBySongIds(Context context, long... songIds) {
        List<LocalSongEntity> result = new ArrayList<>();
        List<LocalSongEntity> songs = getLocalSongs(context);

        if (songIds != null) {
            //Traverse in the order to make sure that the latest added song always in the first postion;
            for (long id : songIds) {
                for (LocalSongEntity songEntity : songs) {
                    if (songEntity.getSongId() == id) {
                        result.add(songEntity);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<LocalSongEntity> searchLocalSong(Context context, @NonNull String keyword) {
        List<LocalSongEntity> localSongEntities = getLocalSongs(context);
        List<LocalSongEntity> result = new ArrayList<>();

        for (LocalSongEntity songEntity : localSongEntities) {
            if (StringUtil.isContainsKeywordIgnoreCase(songEntity.getTitle(), keyword)
                    || StringUtil.isContainsKeywordIgnoreCase(songEntity.getAlbum(), keyword)
                    || StringUtil.isContainsKeywordIgnoreCase(songEntity.getArtist(), keyword)
                    ) {
                result.add(songEntity);
            }

        }
        return result;
    }

    @Override
    public boolean deleteLocalSong(Context context, long songId) {
        LocalSongEntity songEntity = getLocalSong(context, songId);
        if (songEntity == null) {
            return false;
        }

        File fileToDelete = new File(songEntity.getPath());
        if (!fileToDelete.exists()) {
            return false;
        }

        if (fileToDelete.delete()) {
            int deletedNum = context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Audio.Media._ID + "=" + songId, null);
            return deletedNum > 0;
        } else {
            return false;
        }
    }


}
