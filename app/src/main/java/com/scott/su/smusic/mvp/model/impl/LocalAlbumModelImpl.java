package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.cache.BitmapLruCache;
import com.su.scott.slibrary.util.BlurUtil;
import com.su.scott.slibrary.util.StringUtil;

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
            albumEntity.setAlbumTitle(albumSongEntities.get(0).getAlbum());
            albumEntity.setArtist(albumSongEntities.get(0).getArtist());

            albumEntities.add(albumEntity);
        }

        return albumEntities;
    }

    @Override
    public LocalAlbumEntity getLocalAlbum(Context context, long albumId) {
        List<LocalAlbumEntity> albumEntities = getLocalAlbums(context);
        for (LocalAlbumEntity albumEntity : albumEntities) {
            if (albumEntity.getAlbumId() == albumId) {
                return albumEntity;
            }
        }
        return null;
    }

    @Override
    public String getAlbumCoverPathByAlbumId(Context context, long albumId) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + albumId),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToNext();
            path = cursor.getString(0);
            cursor.close();
        }
        //Second way to get the path(Uri) of album cover;
//        path = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId).toString();
        return path;
    }

    @Override
    public String getAlbumCoverPathBySongId(Context context, long songId) {
        LocalSongEntity songEntity = getLocalSong(context, songId);
        if (songEntity == null) {
            return null;
        }
        return getAlbumCoverPathByAlbumId(context, songEntity.getAlbumId());
    }

    @Override
    public Bitmap getAlbumCoverBitmapBlur(Context context, long albumId) {
        String path = getAlbumCoverPathByAlbumId(context, albumId);
        Bitmap bitmap = BitmapLruCache.getInstance().get(path);

        if (bitmap == null) {
            bitmap = BlurUtil.blur(BitmapFactory.decodeFile(path));
            BitmapLruCache.getInstance().put(path, bitmap);
        }
        return bitmap;
    }

    @Override
    public List<LocalAlbumEntity> searchLocalAlbum(Context context, @NonNull String keyword) {
        List<LocalAlbumEntity> localAlbumEntities = getLocalAlbums(context);
        List<LocalAlbumEntity> result = new ArrayList<>();

        for (LocalAlbumEntity albumEntity : localAlbumEntities) {
//            if (albumEntity.getAlbumTitle().contains(keyword)
//                    || albumEntity.getArtist().contains(keyword)) {
//                result.add(albumEntity);
//            }
            if (StringUtil.isContainsKeywordIgnoreCase(albumEntity.getAlbumTitle(), keyword)
                    || StringUtil.isContainsKeywordIgnoreCase(albumEntity.getArtist(), keyword)) {
                result.add(albumEntity);
            }
        }
        return result;
    }

    @Override
    public List<LocalSongEntity> getLocalSongs(Context context) {
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

}
