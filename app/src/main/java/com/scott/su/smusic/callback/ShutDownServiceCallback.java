package com.scott.su.smusic.callback;

import com.scott.su.smusic.entity.LocalSongEntity;


/**
 * Created by asus on 2016/9/15.
 */
public interface ShutDownServiceCallback {
    void onStart();

    void onTick(long millisUntilFinished);

    void onFinish();
}
