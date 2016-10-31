package com.scott.su.smusic.cache;


import android.support.v4.util.LruCache;

import com.scott.su.smusic.entity.LocalSongEntity;


/**
 * Created by Administrator on 2016/10/30.
 */
public class LocalSongEntityCache {
    private LruCache<String, LocalSongEntity> mEntityCache;
    private static LocalSongEntityCache mInstance;

    private LocalSongEntityCache() {
        //2M com.scott.su.smusic.cache space;
        mEntityCache = new LruCache<>(2 * 1024 * 1024);
    }

    public static LocalSongEntityCache getInstance() {
        if (null == mInstance) {
            mInstance = new LocalSongEntityCache();
        }
        return mInstance;
    }

    public void put(String albumId, LocalSongEntity entity) {
        mEntityCache.put(albumId, entity);
    }

    public LocalSongEntity get(String songId) {
        return mEntityCache.get(songId);
    }

    public void release() {
        if (mEntityCache != null) {
            mEntityCache.evictAll();
        }
    }

}
