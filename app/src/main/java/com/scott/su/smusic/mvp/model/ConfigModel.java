package com.scott.su.smusic.mvp.model;

import android.content.Context;

/**
 * Created by asus on 2016/9/15.
 */
public interface ConfigModel {

    boolean isPlayRepeatOne(Context context);

    boolean isPlayRepeatAll(Context context);

    boolean isPlayShuffle(Context context);

    void setPlayRepeatOne(Context context);

    void setPlayRepeatAll(Context context);

    void setPlayShuffle(Context context);
}
