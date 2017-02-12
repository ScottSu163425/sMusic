package com.scott.su.smusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.scott.su.smusic.callback.ShutDownServiceCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.TimerStatus;
import com.scott.su.smusic.mvp.view.ShutDownTimerServiceView;

/**
 * Created by Administrator on 2016/9/9.
 */
public class ShutDownTimerService extends Service implements ShutDownTimerServiceView {
    private CountDownTimer mShutDownTimer;
    private TimerStatus mCurrentShutDownTimerStatus = TimerStatus.Stop;
    private ShutDownServiceCallback mTimerCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ShutDownTimerServiceBinder();
    }


    @Override
    public TimerStatus getServiceCurrentTimerShutDownStatus() {
        return mCurrentShutDownTimerStatus;
    }

    @Override
    public void startShutDownTimer(long duration, long interval) {
        cancelShutDownTimer();
        mShutDownTimer = generateCountDownTimer(duration, interval);
        mShutDownTimer.start();
        if (mTimerCallback != null) {
            mTimerCallback.onStart(duration);
        }
        mCurrentShutDownTimerStatus = TimerStatus.Ticking;
    }

    @Override
    public void cancelShutDownTimer() {
        if (mShutDownTimer != null) {
            mShutDownTimer.cancel();
            mShutDownTimer = null;
            mCurrentShutDownTimerStatus = TimerStatus.Stop;
        }
    }

    @Override
    public void setTimerCallback(ShutDownServiceCallback callback) {
        this.mTimerCallback = callback;
    }

    private CountDownTimer generateCountDownTimer(long duration, long interval) {
        return new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mTimerCallback != null) {
                    mTimerCallback.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (mTimerCallback != null) {
                    mTimerCallback.onFinish();
                }
                mCurrentShutDownTimerStatus = TimerStatus.Stop;
                sendStopBroadcast();
            }
        };
    }

    /**
     * Send a broadcast to Music play service to stop it.
     */
    private void sendStopBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_STOP_MUSIC_PLAY_SERVICE);
        sendBroadcast(intent);
    }

    public class ShutDownTimerServiceBinder extends Binder implements ShutDownTimerServiceView {

        @Override
        public TimerStatus getServiceCurrentTimerShutDownStatus() {
            return ShutDownTimerService.this.getServiceCurrentTimerShutDownStatus();
        }

        @Override
        public void startShutDownTimer(long duration, long interval) {
            ShutDownTimerService.this.startShutDownTimer(duration, interval);
        }

        @Override
        public void cancelShutDownTimer() {
            ShutDownTimerService.this.cancelShutDownTimer();
        }

        @Override
        public void setTimerCallback(ShutDownServiceCallback callback) {
            ShutDownTimerService.this.setTimerCallback(callback);
        }
    }

}
