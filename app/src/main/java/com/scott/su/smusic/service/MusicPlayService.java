package com.scott.su.smusic.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.callback.ShutDownServiceCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.constant.TimerStatus;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.view.MusicPlayServiceView;
import com.scott.su.smusic.mvp.view.ShutDownTimerServiceView;
import com.scott.su.smusic.ui.activity.MainActivity;
import com.scott.su.smusic.util.MusicPlayUtil;
import com.su.scott.slibrary.util.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MusicPlayService extends Service implements MusicPlayServiceView, ShutDownTimerServiceView {
    public static final String ACTION_PLAY_NEXT = "com.scott.su.smusic.service.play_next";
    public static final String ACTION_PLAY_PREVIOUS = "com.scott.su.smusic.service.play_previous";
    public static final String ACTION_PLAY_PAUSE = "com.scott.su.smusic.service.play_pause";
    public static final int REQUEST_CODE_PLAY_NEXT = 1;
    public static final int REQUEST_CODE_PLAY_PREVIOUS = 2;
    public static final int REQUEST_CODE_PLAY_PAUSE = 3;
    public static final int ID_NOTIFICATION = 123;
    public static final long DURATION_TIMER_DELAY = TimeUtil.MILLISECONDS_OF_SECOND / 10;

    private MediaPlayer mMediaPlayer;
    private NotificationManager mNotificationManager;
    private MusicPlayServiceCallback mMusicPlayServiceCallback;
    private LocalSongEntity mCurrentPlayingSong;
    private ArrayList<LocalSongEntity> mCurrentPlayingSongs;
    private boolean mPlaySameSong;
    private boolean mPlayNextByDeleteing;
    private PlayStatus mCurrentPlayStatus = PlayStatus.Stop;
    private PlayMode mCurrentPlayMode = PlayMode.RepeatAll;
    private Handler mPlayTimerHandler = new Handler();
    private Runnable mPlayTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMusicPlayServiceCallback != null) {
                mMusicPlayServiceCallback.onPlayProgressUpdate(mMediaPlayer.getCurrentPosition());
            }
            mPlayTimerHandler.postDelayed(this, DURATION_TIMER_DELAY);
        }
    };

    //ShutDown Timer
    private CountDownTimer mShutDownTimer;
    private TimerStatus mCurrentShutDownTimerStatus = TimerStatus.Stop;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mMusicPlayServiceCallback != null) {
                    mMusicPlayServiceCallback.onPlayComplete();
                }
                playNext();
            }
        });

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMediaPlayer == null) {
            initMediaPlayer();
        }

        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_PLAY_NEXT)) {
                playNext();
            } else if (intent.getAction().equals(ACTION_PLAY_PREVIOUS)) {
                playPrevious();
            } else if (intent.getAction().equals(ACTION_PLAY_PAUSE)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
                updateNotifycation();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void setServicePlaySong(LocalSongEntity currentPlaySong, ArrayList<LocalSongEntity> playSongs) {
        if (mCurrentPlayingSong == null) {
            mPlaySameSong = false;
        } else if (mCurrentPlayingSong.getSongId() == currentPlaySong.getSongId()) {
            mPlaySameSong = true;
        } else {
            mPlaySameSong = false;
        }
        this.mCurrentPlayingSong = currentPlaySong;
        this.mCurrentPlayingSongs = playSongs;
    }

    @Override
    public void setServicePlayMode(PlayMode playMode) {
        this.mCurrentPlayMode = playMode;
    }

    @Override
    public PlayMode getServicePlayMode() {
        return this.mCurrentPlayMode;
    }

    @Override
    public void play() {
        if (mMediaPlayer != null && mCurrentPlayingSong != null) {
            if (mPlaySameSong) {
                playResume();
            } else {
                //Play different song.
                playNew();
            }
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && isPlaying()) {
            stopPlayTimer();
            mMediaPlayer.pause();
            mCurrentPlayStatus = PlayStatus.Pause;
            mPlaySameSong = true;
            if (isRegisterCallback()) {
                mMusicPlayServiceCallback.onPlayPause(mMediaPlayer.getCurrentPosition());
            }
            updateNotifycation();
        }
    }

    @Override
    public void seekTo(int position) {
        if (isPause()) {
            mMediaPlayer.seekTo(position);
        } else {
            mMediaPlayer.seekTo(position);
        }
        mMusicPlayServiceCallback.onPlayProgressUpdate(mMediaPlayer.getCurrentPosition());
        playResume();
    }

    @Override
    public void playPrevious() {
        mCurrentPlayingSong = MusicPlayUtil.getPreviousSong(mCurrentPlayingSong, mCurrentPlayingSongs, mCurrentPlayMode);
        playNew();
    }

    @Override
    public void playNext() {
        if (mPlayNextByDeleteing && mCurrentPlayMode == PlayMode.RepeatOne) {
            //When deleting a playing song,which is playing with repeate-one,We need to change the position of next playing song.
            mCurrentPlayingSong = MusicPlayUtil.getNextSong(mCurrentPlayingSong, mCurrentPlayingSongs, PlayMode.RepeatAll);
            mPlayNextByDeleteing = false;
        } else {
            mCurrentPlayingSong = MusicPlayUtil.getNextSong(mCurrentPlayingSong, mCurrentPlayingSongs, mCurrentPlayMode);
        }
        playNew();
    }

    @Override
    public void removeServiceSong(LocalSongEntity songEntity) {
        if (mCurrentPlayingSongs == null || mCurrentPlayingSongs.isEmpty()) {
            return;
        }

        for (LocalSongEntity entity : mCurrentPlayingSongs) {
            if (entity.getSongId() == songEntity.getSongId()) {
                mCurrentPlayingSongs.remove(entity);
                break;
            }
        }

        if (mCurrentPlayingSongs.size() == 0) {
            stopMediaPlayer();
            return;
        }

        if (mCurrentPlayingSong.getSongId() == songEntity.getSongId()) {
            mPlayNextByDeleteing = true;
            playNext();
        }
    }

    private void stopMediaPlayer() {
        stopPlayTimer();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        stopForeground(true);
        mNotificationManager.cancelAll();
        stopSelf();
    }

    private void playResume() {
        if (isPause()) {
            startMediaPlayer();
            updateNotifycation();
        }
    }

    private void playNew() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mCurrentPlayingSong.getPath());
            mMediaPlayer.prepare();
            startMediaPlayer();
            if (mMusicPlayServiceCallback != null) {
                mMusicPlayServiceCallback.onPlaySongChanged(mCurrentPlayingSong);
            }
            updateNotifycation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int mCurrentRequestCode = 0;

    private void updateNotifycation() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Intent intentGoToMusicPlay = new Intent(this, MainActivity.class);
        intentGoToMusicPlay.putExtra(Constants.KEY_IS_FROM_NOTIFICATION, true);
        intentGoToMusicPlay.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, mCurrentPlayingSong);
        intentGoToMusicPlay.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mCurrentPlayingSongs);
        intentGoToMusicPlay.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentGoToMusicPlay = PendingIntent.getActivity(this, mCurrentRequestCode, intentGoToMusicPlay, PendingIntent.FLAG_UPDATE_CURRENT);

//        builder.setContentTitle(mCurrentPlayingSong.getTitle());
//        builder.setContentText(mCurrentPlayingSong.getArtist());
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setLargeIcon(BitmapFactory.decodeFile(new LocalAlbumModelImpl().getAlbumCoverPathByAlbumId(this, mCurrentPlayingSong.getAlbumId())));
//        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
//        builder.setPriority(NotificationCompat.PRIORITY_MAX);
//        builder.setOngoing(true);
//
//        builder.setContentIntent(pendingIntentGoToMusicPlay);
//        builder.setFullScreenIntent(pendingIntentGoToMusicPlay, false);
//
//        //第一个参数是图标资源id 第二个是图标显示的名称，第三个图标点击要启动的PendingIntent
//        builder.addAction(R.drawable.ic_skip_previous_notification_36dp, "", generateOperateIntent(REQUEST_CODE_PLAY_PREVIOUS, ACTION_PLAY_PREVIOUS));
//        builder.addAction(isPlaying() ? R.drawable.ic_pause_36dp : R.drawable.ic_play_arrow_notification_36dp, "",
//                generateOperateIntent(REQUEST_CODE_PLAY_PAUSE, ACTION_PLAY_PAUSE));
//        builder.addAction(R.drawable.ic_skip_next_notification_36dp, "", generateOperateIntent(REQUEST_CODE_PLAY_NEXT, ACTION_PLAY_NEXT));
//
//        NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
//        style.setMediaSession(new MediaSessionCompat(this, "MediaSession",
//                new ComponentName(MusicPlayService.this, Intent.ACTION_MEDIA_BUTTON), null).getSessionToken());
//        //CancelButton在5.0以下的机器有效
//        style.setCancelButtonIntent(pendingIntentGoToMusicPlay);
//        style.setShowCancelButton(true);
//        //设置要现实在通知右方的图标 最多三个
//        style.setShowActionsInCompactView(0, 1, 2);
//
//        builder.setStyle(style);
//        builder.setShowWhen(false);

        //Second way:
        RemoteViews remoteViewNormal = new RemoteViews(getPackageName(), R.layout.remote_notification_music_play);
        remoteViewNormal.setImageViewResource(R.id.iv_play_pause_notification_music_play,
                isPlaying() ? R.drawable.ic_pause_notification_grey_600_48dp : R.drawable.ic_play_notification_grey_600_48dp);
        remoteViewNormal.setBitmap(R.id.iv_cover_notification_music_play, "setImageBitmap", BitmapFactory.decodeFile(new LocalAlbumModelImpl().getAlbumCoverPathByAlbumId(this, mCurrentPlayingSong.getAlbumId())));
        remoteViewNormal.setTextViewText(R.id.tv_title_notification_music_play, mCurrentPlayingSong.getTitle());
        remoteViewNormal.setTextViewText(R.id.tv_artist_notification_music_play, mCurrentPlayingSong.getArtist());
        remoteViewNormal.setOnClickPendingIntent(R.id.btn_play_pause_notification_music_play, generateOperateIntent(REQUEST_CODE_PLAY_PAUSE, ACTION_PLAY_PAUSE));
        remoteViewNormal.setOnClickPendingIntent(R.id.btn_skip_next_notification_music_play, generateOperateIntent(REQUEST_CODE_PLAY_NEXT, ACTION_PLAY_NEXT));

//        RemoteViews remoteViewBig = new RemoteViews(getPackageName(), R.layout.remote_notification_music_play_big);
//        remoteViewNormal.setImageViewResource(R.id.iv_play_pause_notification_music_play_big,
//                isPlaying() ? R.drawable.ic_pause_notification_grey_600_48dp : R.drawable.ic_play_notification_grey_600_48dp);
//        remoteViewNormal.setBitmap(R.id.iv_cover_notification_music_play_big, "setImageBitmap", BitmapFactory.decodeFile(new LocalAlbumModelImpl().getAlbumCoverPathByAlbumId(this, mCurrentPlayingSong.getAlbumId())));
//        remoteViewBig.setTextViewText(R.id.tv_title_notification_music_play_big, mCurrentPlayingSong.getTitle());
//        remoteViewBig.setTextViewText(R.id.tv_artist_notification_music_play_big, mCurrentPlayingSong.getArtist());
//        remoteViewBig.setOnClickPendingIntent(R.id.btn_play_pause_notification_music_play_big, generateOperateIntent(REQUEST_CODE_PLAY_PREVIOUS, ACTION_PLAY_PREVIOUS));
//        remoteViewBig.setOnClickPendingIntent(R.id.btn_skip_previous_notification_music_play_big, generateOperateIntent(REQUEST_CODE_PLAY_PREVIOUS, ACTION_PLAY_PREVIOUS));
//        remoteViewBig.setOnClickPendingIntent(R.id.btn_skip_next_notification_music_play_big, generateOperateIntent(REQUEST_CODE_PLAY_NEXT, ACTION_PLAY_NEXT));

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntentGoToMusicPlay);
        builder.setContent(remoteViewNormal);

//        builder.setCustomBigContentView(remoteViewBig);// TODO: 2016/10/19 Throw exceptions if run it;
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setOngoing(true);

        mNotificationManager.notify(ID_NOTIFICATION, builder.build());
        startForeground(ID_NOTIFICATION, builder.build());
        mCurrentRequestCode++;
    }

    private PendingIntent generateOperateIntent(int requestCode, String action) {
        Intent intentPlayNext = new Intent(this, MusicPlayService.class);
        intentPlayNext.setAction(action);
        return PendingIntent.getService(this, requestCode, intentPlayNext, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void startMediaPlayer() {
        mMediaPlayer.start();
        startPlayTimer();
        mCurrentPlayStatus = PlayStatus.Playing;
        if (isRegisterCallback()) {
            mMusicPlayServiceCallback.onPlayStart();
        }
    }

    private void startPlayTimer() {
        stopPlayTimer();
        mPlayTimerHandler.post(mPlayTimerRunnable);
    }

    private void stopPlayTimer() {
        mPlayTimerHandler.removeCallbacks(mPlayTimerRunnable);
    }

    private boolean isRegisterCallback() {
        return mMusicPlayServiceCallback != null;
    }

    private boolean isStop() {
        return mCurrentPlayStatus == PlayStatus.Stop;
    }

    private boolean isPlaying() {
        return mCurrentPlayStatus == PlayStatus.Playing;
    }

    private boolean isPause() {
        return mCurrentPlayStatus == PlayStatus.Pause;
    }

    @Override
    public PlayStatus getServiceCurrentPlayStatus() {
        return mCurrentPlayStatus;
    }

    @Override
    public LocalSongEntity getServiceCurrentPlayingSong() {
        return mCurrentPlayingSong;
    }

    @Override
    public ArrayList<LocalSongEntity> getServiceCurrentPlayingSongs() {
        return mCurrentPlayingSongs;
    }

    @Override
    public void registerServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
        this.mMusicPlayServiceCallback = callback;
    }

    @Override
    public void unregisterServicePlayCallback() {
        this.mMusicPlayServiceCallback = null;
    }

    @Override
    public TimerStatus getServiceCurrentTimerShutDownStatus() {
        return mCurrentShutDownTimerStatus;
    }

    @Override
    public void startShutDownTimer(long duration, long interval, ShutDownServiceCallback callback) {
        stopShutDownTimer();
        mShutDownTimer = generateCountDownTimer(duration, interval, callback);
        mShutDownTimer.start();
        callback.onStart();
    }

    @Override
    public void stopShutDownTimer() {
        if (mShutDownTimer != null) {
            mShutDownTimer.cancel();
            mShutDownTimer = null;
        }
    }

    private CountDownTimer generateCountDownTimer(long duration, long interval, final ShutDownServiceCallback callback) {
        return new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (callback != null) {
                    callback.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (callback != null) {
                    callback.onFinish();
                }
            }
        };
    }

    public class MusicPlayServiceBinder extends Binder implements MusicPlayServiceView, ShutDownTimerServiceView {

        @Override
        public PlayStatus getServiceCurrentPlayStatus() {
            return MusicPlayService.this.getServiceCurrentPlayStatus();
        }

        @Override
        public LocalSongEntity getServiceCurrentPlayingSong() {
            return MusicPlayService.this.getServiceCurrentPlayingSong();
        }

        @Override
        public ArrayList<LocalSongEntity> getServiceCurrentPlayingSongs() {
            return MusicPlayService.this.getServiceCurrentPlayingSongs();
        }

        @Override
        public void setServicePlaySong(LocalSongEntity currentPlaySong, ArrayList<LocalSongEntity> playSongs) {
            MusicPlayService.this.setServicePlaySong(currentPlaySong, playSongs);
        }

        @Override
        public void setServicePlayMode(PlayMode playMode) {
            MusicPlayService.this.setServicePlayMode(playMode);
        }

        @Override
        public PlayMode getServicePlayMode() {
            return MusicPlayService.this.getServicePlayMode();
        }

        @Override
        public void play() {
            MusicPlayService.this.play();
        }

        @Override
        public void pause() {
            MusicPlayService.this.pause();
        }

        @Override
        public void seekTo(int position) {
            MusicPlayService.this.seekTo(position);
        }

        @Override
        public void playPrevious() {
            MusicPlayService.this.playPrevious();
        }

        @Override
        public void playNext() {
            MusicPlayService.this.playNext();
        }

        @Override
        public void removeServiceSong(LocalSongEntity songEntity) {
            MusicPlayService.this.removeServiceSong(songEntity);
        }

        @Override
        public void registerServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
            MusicPlayService.this.registerServicePlayCallback(callback);
        }

        @Override
        public void unregisterServicePlayCallback() {
            MusicPlayService.this.unregisterServicePlayCallback();
        }

        @Override
        public TimerStatus getServiceCurrentTimerShutDownStatus() {
            return MusicPlayService.this.getServiceCurrentTimerShutDownStatus();
        }

        @Override
        public void startShutDownTimer(long duration, long interval, ShutDownServiceCallback callback) {
            MusicPlayService.this.startShutDownTimer(duration, interval, callback);
        }

        @Override
        public void stopShutDownTimer() {
            MusicPlayService.this.stopShutDownTimer();
        }
    }


}
