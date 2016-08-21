package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/20.
 */
public class LocalAlbumModelImpl implements LocalAlbumModel {

    @Override
    public List<LocalAlbumEntity> getLocalAlbums(Context context) {
        List<LocalSongEntity> songEntities = getLocalSongs(context);
        List<LocalAlbumEntity> albumEntities = new ArrayList<>();
        List<String> albumIds = new ArrayList<>();

        //Collect album id.
        for (LocalSongEntity songEntity : songEntities) {
            String albumId = songEntity.getAlbumId() + "";
            if (!albumIds.contains(albumId)) {
                albumIds.add(albumId);
            }
        }

        for (int i = 0; i < albumIds.size(); i++) {
            LocalAlbumEntity albumEntity = new LocalAlbumEntity();
            List<LocalSongEntity> albumSongEntities = new ArrayList<>();
            long albumId = Long.valueOf(albumIds.get(i));

            albumEntity.setAlbumId(albumId);

            for (LocalSongEntity songEntity : songEntities) {
                if (albumId == songEntity.getAlbumId()) {
                    albumSongEntities.add(songEntity);
                }
            }
            albumEntity.setAlbumSongs(albumSongEntities);
            albumEntity.setAlbumTitle(albumSongEntities.get(0).getTitle());
            albumEntity.setArtist(albumSongEntities.get(0).getArtist());

            albumEntities.add(albumEntity);
        }

        return albumEntities;
    }

    @Override
    public List<LocalSongEntity> getLocalSongs(Context context) {
        List<LocalSongEntity> songEntities = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while (cursor.moveToNext()) {
            int isMusic = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0) {
                continue;
            }

            long songId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
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
            songEntities.add(localSongEntity);
        }
        cursor.close();
        return songEntities;
    }

}
