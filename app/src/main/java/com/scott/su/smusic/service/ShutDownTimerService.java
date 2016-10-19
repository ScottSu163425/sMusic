package com.scott.su.smusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.scott.su.smusic.callback.ShutDownServiceCallback;
import com.scott.su.smusic.constant.TimerStatus;
import com.scott.su.smusic.mvp.view.ShutDownTimerServiceView;

/**
 * Created by Administrator on 2016/10/19.
 */

public class ShutDownTimerService extends Service implements ShutDownTimerServiceView {
    private CountDownTimer mCountDownTimer;
    private TimerStatus mCurrentTimerStatus = TimerStatus.Stop;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ShutDownTimerServiceBinder();
    }

    @Override
    public TimerStatus getServiceCurrentTimerShutDownStatus() {
        return mCurrentTimerStatus;
    }

    @Override
    public void startShutDownTimer(long duration, long interval, ShutDownServiceCallback callback) {
        stopShutDownTimer();
        mCountDownTimer=generateCountDownTimer(duration,interval,callback);
        mCountDownTimer.start();
        callback.onStart();
    }

    @Override
    public void stopShutDownTimer() {
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer=null;
        }
    }

    private CountDownTimer generateCountDownTimer(long duration, long interval, final ShutDownServiceCallback callback){
        return new CountDownTimer(duration,interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (callback!=null){
                    callback.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (callback!=null){
                    callback.onFinish();
                }
            }
        };
    }

    public class ShutDownTimerServiceBinder extends Binder implements ShutDownTimerServiceView{

        @Override
        public TimerStatus getServiceCurrentTimerShutDownStatus() {
            return ShutDownTimerService.this.getServiceCurrentTimerShutDownStatus();
        }

        @Override
        public void startShutDownTimer(long duration, long interval, ShutDownServiceCallback callback) {
            ShutDownTimerService.this.startShutDownTimer(duration,interval,callback);
        }

        @Override
        public void stopShutDownTimer() {
            ShutDownTimerService.this.stopShutDownTimer();
        }
    }


}
