package com.scott.su.smusic.callback;



/**
 * Created by asus on 2016/9/15.
 */
public interface ShutDownServiceCallback {
    void onStart(long duration);

    void onTick(long millisUntilFinished);

    void onFinish();
}
