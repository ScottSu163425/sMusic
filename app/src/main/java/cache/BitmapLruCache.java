package cache;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * Created by Administrator on 2016/8/18.
 */
public class BitmapLruCache {
    private LruCache<String, Bitmap> blurCache;

    private static BitmapLruCache instance;

    private BitmapLruCache() {
        //3M cache space;
        blurCache = new LruCache<String, Bitmap>(4 * 1024 * 1024) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public static BitmapLruCache getInstance() {
        if (null == instance) {
            instance = new BitmapLruCache();
        }
        return instance;
    }

    public void put(String path, Bitmap bitmap) {
        blurCache.put(path, bitmap);
    }

    public Bitmap get(String path) {
        return blurCache.get(path);
    }

    public void release() {
        if (blurCache != null) {
            blurCache.evictAll();
        }
    }


}
