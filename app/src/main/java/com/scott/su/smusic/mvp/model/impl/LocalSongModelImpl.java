package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalSongModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongModelImpl implements LocalSongModel {
//    private static LruCache<String, Bitmap> coverCache;
//    private static LruCache<String, Bitmap> blurCoverCache;
//
//    private static final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
//    private static final int CACHE_SIZE = MAX_MEMORY / 8;
//    private static final String KEY_NULL = "KEY_NULL";


    public LocalSongModelImpl() {
//        if (coverCache == null) {
//            coverCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
//                @Override
//                protected int sizeOf(String key, Bitmap value) {
//                    return value.getByteCount() / 1024;
//                }
//            };
//        }
//
//        if (blurCoverCache == null) {
//            blurCoverCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
//                @Override
//                protected int sizeOf(String key, Bitmap value) {
//                    return value.getByteCount() / 1024;
//                }
//            };
//        }
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
            if (songEntity.getTitle().contains(keyword)
                    || songEntity.getTitle().contains(keyword)
                    || songEntity.getAlbum().contains(keyword)
                    || songEntity.getArtist().contains(keyword)) {
                result.add(songEntity);
            }
        }
        return result;
    }

//    @Override
//    public Bitmap getLocalSongAlbumCover(Context context, long albumId,int size) {
//        Bitmap coverBitmap = getCover(albumId + "");
//
//        //Try to get cover from Lrucache;
//        if (coverBitmap != null) {
//            return coverBitmap;
//        }
//
//        String path =  getAlbumCoverPath(context, albumId);
//
//        if (TextUtils.isEmpty(path)) {
//            coverBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_album_cover_default);
//            putCover(KEY_NULL, coverBitmap);
//        } else {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            // 仅获取大小
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(path, options);
//            int maxLength = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
//            // 压缩尺寸，避免卡顿
//            int inSampleSize = maxLength / size;
//            if (inSampleSize < 1) {
//                inSampleSize = 1;
//            }
//            options.inSampleSize = inSampleSize;
//            // 获取bitmap
//            options.inJustDecodeBounds = false;
//            coverBitmap = BitmapFactory.decodeFile(path, options);
//            if (coverBitmap == null) {
//                coverBitmap = getLocalSongAlbumCover(context, -1,size);
//            } else {
//                putCover(albumId + "", coverBitmap);
//            }
//        }
//        return coverBitmap;
//    }

//    @Override
//    public Bitmap getLocalSongAlbumBlurCover(Context context, long albumId,int size) {
//        Bitmap blurCoverBitmap = getBlurCover(albumId + "");
//
//        if (blurCoverBitmap != null) {
//            return blurCoverBitmap;
//        }
//
//        blurCoverBitmap = ImageUtil.blur(getLocalSongAlbumBlurCover(context, albumId,size), 30);
//        putBlurCover(albumId + "", blurCoverBitmap);
//        return blurCoverBitmap;
//    }

//    private synchronized void putCover(String albumId, Bitmap bitmap) {
//        coverCache.put(albumId, bitmap);
//    }
//
//    private synchronized Bitmap getCover(String albumId) {
//        return coverCache.get(albumId);
//    }
//
//    private synchronized void putBlurCover(String albumId, Bitmap bitmap) {
//        blurCoverCache.put(albumId, bitmap);
//    }
//
//    private synchronized Bitmap getBlurCover(String albumId) {
//        return blurCoverCache.get(albumId);
//    }

//    public synchronized void clearCache() {
//        if (coverCache != null) {
//            if (coverCache.size() > 0) {
//                coverCache.evictAll();
//            }
//        }
//
//        if (blurCoverCache != null) {
//            if (blurCoverCache.size() > 0) {
//                blurCoverCache.evictAll();
//            }
//        }
//    }

}
