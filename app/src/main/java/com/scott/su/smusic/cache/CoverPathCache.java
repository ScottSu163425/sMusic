package com.scott.su.smusic.cache;


import android.support.v4.util.LruCache;


/**
 * Created by Administrator on 2016/8/18.
 */
public class CoverPathCache {
    private LruCache<String, String> coverPathCache;
    private static CoverPathCache instance;

    private CoverPathCache() {
        //2M cache space;
        coverPathCache = new LruCache<>(2 * 1024 * 1024);
    }

    public static CoverPathCache getInstance() {
        if (null == instance) {
            instance = new CoverPathCache();
        }
        return instance;
    }

    public void put(String albumId, String coverPath) {
        coverPathCache.put(albumId, coverPath);
    }

    public String get(String albumId) {
        return coverPathCache.get(albumId);
    }

    public void release() {
        if (coverPathCache != null) {
            coverPathCache.evictAll();
        }
    }

}
