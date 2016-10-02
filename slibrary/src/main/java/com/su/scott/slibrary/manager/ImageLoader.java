package com.su.scott.slibrary.manager;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Administrator on 2016/9/26.
 */

public class ImageLoader {

    public static void load(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void load(Context context, String path, ImageView imageView, int placeholder, int errorholder) {
        Glide.with(context)
                .load(path)
                .placeholder(placeholder)
                .error(errorholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


}
