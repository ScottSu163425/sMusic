package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.callback.ShutDownServiceCallback;
import com.scott.su.smusic.constant.TimerStatus;

/**
 * Created by asus on 2016/9/15.
 */
public interface ShutDownTimerServiceView {

    TimerStatus getServiceCurrentTimerShutDownStatus();

    void startShutDownTimer(long duration, long interval );

    void cancelShutDownTimer();

    void setTimerCallback(ShutDownServiceCallback callback);

}
