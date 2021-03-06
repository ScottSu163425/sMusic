package com.scott.su.smusic.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.PlayStatisticModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.PlayStatisticModelImpl;
import com.scott.su.smusic.mvp.view.MusicPlayServiceView;
import com.scott.su.smusic.ui.activity.MainActivity;
import com.scott.su.smusic.util.MusicPlayUtil;
import com.su.scott.slibrary.util.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MusicPlayService extends Service implements MusicPlayServiceView {
    public static final String ACTION_PLAY_NEXT = "com.scott.su.smusic.service.play_next";
    public static final String ACTION_PLAY_PREVIOUS = "com.scott.su.smusic.service.play_previous";
    public static final String ACTION_PLAY_PAUSE = "com.scott.su.smusic.service.play_pause";
    public static final String ACTION_STOP = "com.scott.su.smusic.service.stop";
    public static final int REQUEST_CODE_PLAY_NEXT = 1;
    public static final int REQUEST_CODE_PLAY_PREVIOUS = 2;
    public static final int REQUEST_CODE_PLAY_PAUSE = 3;
    public static final int REQUEST_CODE_STOP = 4;
    public static final int ID_NOTIFICATION = 123;
    public static final long DURATION_TIMER_DELAY = TimeUtil.MILLISECONDS_OF_SECOND / 10;

    private MediaPlayer mMediaPlayer;
    private NotificationManager mNotificationManager;
    private List<MusicPlayServiceCallback> mCallbacks = new ArrayList<>();
    //    private MusicPlayServiceCallback mMusicPlayServiceCallback;
    private LocalSongEntity mPreviousPlayingSong;
    private LocalSongEntity mCurrentPlayingSong;
    private ArrayList<LocalSongEntity> mPlayListSongs;
    private boolean mPlaySameSong;
    private boolean mPlayNextByDeleteing;
    private PlayStatus mCurrentPlayStatus = PlayStatus.Stop;
    private PlayMode mCurrentPlayMode = PlayMode.RepeatAll;
    private Handler mPlayTimerHandler = new Handler();
    private Runnable mPlayTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRegisterCallback()) {
                notifyAllOnPlayProgressUpdate(mMediaPlayer.getCurrentPosition());
            }
            mPlayTimerHandler.postDelayed(this, DURATION_TIMER_DELAY);
        }
    };
    private PlayStatisticModel mPlayStatisticModel;

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

        registerReceiver(mStopPlayReceiver, new IntentFilter(Constants.ACTION_STOP_MUSIC_PLAY_SERVICE));

        init();
    }

    private void init() {
        mPlayListSongs = new ArrayList<>();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isRegisterCallback()) {
                    notifyAllOnPlayComplete();
                }
                playNext();
            }
        });
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mPlayStatisticModel = new PlayStatisticModelImpl();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMediaPlayer == null) {
            init();
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
            } else if (intent.getAction().equals(ACTION_STOP)) {
                releaseAll();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        releaseAll();
        unregisterReceiver(mStopPlayReceiver);
        super.onDestroy();
    }

    @Override
    public void setServiceCurrentPlaySong(LocalSongEntity currentPlaySong) {
        if (mCurrentPlayingSong == null) {
            mPlaySameSong = false;
        } else if (mCurrentPlayingSong.getSongId() == currentPlaySong.getSongId()) {
            mPlaySameSong = true;
        } else {
            mPlaySameSong = false;
        }

        if (!mPlaySameSong) {
            mPreviousPlayingSong = mCurrentPlayingSong;
            mCurrentPlayingSong = currentPlaySong;
            MusicPlayUtil.addSongToPlayList(mPlayListSongs, mCurrentPlayingSong);
        }
    }

    @Override
    public void setServicePlayListSongs(ArrayList<LocalSongEntity> playSongs) {
        mPlayListSongs = playSongs;
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
                playNew(false);
            }
        }
    }

    @Override
    public void playSkip() {
        if (mMediaPlayer != null && mCurrentPlayingSong != null) {
            if (mPlaySameSong) {
                playResume();
            } else {
                //Play different song.
                playNew(true);
            }
        }
    }

    @Override
    public void play(int position) {
        if (mPlayListSongs.isEmpty()) {
            return;
        }

        if (position < 0 || position >= mPlayListSongs.size()) {
            return;
        }

        setServiceCurrentPlaySong(mPlayListSongs.get(position));
        play();
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && isPlaying()) {
            stopPlayTimer();
            mMediaPlayer.pause();
            mCurrentPlayStatus = PlayStatus.Pause;
            mPlaySameSong = true;
            if (isRegisterCallback()) {
                notifyAllOnPlayPause(mMediaPlayer.getCurrentPosition());
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
        if (isRegisterCallback()) {
            notifyAllOnPlayProgressUpdate(mMediaPlayer.getCurrentPosition());
        }
        playResume();
    }

    @Override
    public void playPrevious() {
        mCurrentPlayingSong = MusicPlayUtil.getPreviousSong(mCurrentPlayingSong, mPlayListSongs, mCurrentPlayMode);
        playNew(true);
    }

    @Override
    public void playNext() {
        if (mPlayNextByDeleteing && mCurrentPlayMode == PlayMode.RepeatOne) {
            //When deleting a playing song,which is playing with repeate-one,We need to change the position of next playing song.
            mPreviousPlayingSong = mCurrentPlayingSong;
            mCurrentPlayingSong = MusicPlayUtil.getNextSong(mCurrentPlayingSong, mPlayListSongs, PlayMode.RepeatAll);
            mPlayNextByDeleteing = false;
        } else {
            mPreviousPlayingSong = mCurrentPlayingSong;
            mCurrentPlayingSong = MusicPlayUtil.getNextSong(mCurrentPlayingSong, mPlayListSongs, mCurrentPlayMode);
        }
        playNew(true);
    }

    @Override
    public void removeServiceSong(LocalSongEntity songEntity) {
        if (mPlayListSongs == null || mPlayListSongs.isEmpty()) {
            return;
        }

        for (LocalSongEntity entity : mPlayListSongs) {
            if (entity.getSongId() == songEntity.getSongId()) {
                mPlayListSongs.remove(entity);
                break;
            }
        }

        if (mPlayListSongs.size() == 0) {
            releaseAll();
            return;
        }

        //If delete current playing song, play next song;
        if (mCurrentPlayingSong.getSongId() == songEntity.getSongId()) {
            mPlayNextByDeleteing = true;
            playNext();
        }
    }

    @Override
    public void clearServiceSongs() {
        if (mPlayListSongs == null || mPlayListSongs.isEmpty()) {
            releaseAll();
            return;
        }

        mPlayListSongs.clear();
        releaseAll();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            stopPlayTimer();
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void cancelService() {
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

    private void playNew(boolean isSkipping) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mCurrentPlayingSong.getPath());
            mMediaPlayer.prepare();
            startMediaPlayer();

            //Only by skip next or previous should notify playing song changed;
            if (isSkipping && mPreviousPlayingSong != null && isRegisterCallback()) {
//                mMusicPlayServiceCallback.onPlaySongChanged(mPreviousPlayingSong, mCurrentPlayingSong);
                notifyAllOnPlaySongChanged(mPreviousPlayingSong, mCurrentPlayingSong, MusicPlayUtil.getSongPosition(mCurrentPlayingSong, mPlayListSongs));
            }
            updateNotifycation();

            //Save to play statistic;
            mPlayStatisticModel.saveOrAddPlayRecord(mCurrentPlayingSong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int mCurrentRequestCode = 0;

    private void updateNotifycation() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Intent intentGoToMusicPlay = new Intent(this, MainActivity.class);
        intentGoToMusicPlay.putExtra(Constants.KEY_EXTRA_IS_FROM_NOTIFICATION, true);
        intentGoToMusicPlay.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, mCurrentPlayingSong);
        intentGoToMusicPlay.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mPlayListSongs);
        intentGoToMusicPlay.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntentGoToMusicPlay = PendingIntent.getActivity(this, mCurrentRequestCode, intentGoToMusicPlay, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(pendingIntentGoToMusicPlay);
        builder.setContent(generateContentRemoteView());
        builder.setCustomBigContentView(generateBigContentRemoteView());
//        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setOngoing(true);

        mNotificationManager.notify(ID_NOTIFICATION, builder.build());
        startForeground(ID_NOTIFICATION, builder.build());
        mCurrentRequestCode++;
    }

    private RemoteViews generateContentRemoteView() {
        RemoteViews remoteViewNormal = new RemoteViews(getPackageName(), R.layout.remote_notification_music_play);
        remoteViewNormal.setImageViewResource(R.id.iv_play_pause_notification_music_play,
                isPlaying() ? R.drawable.ic_pause_notification_grey_600_48dp : R.drawable.ic_play_notification_grey_600_48dp);
        remoteViewNormal.setBitmap(R.id.iv_cover_notification_music_play, "setImageBitmap",
                BitmapFactory.decodeFile(new LocalAlbumModelImpl().getAlbumCoverPathByAlbumId(this, mCurrentPlayingSong.getAlbumId())));
        remoteViewNormal.setTextViewText(R.id.tv_title_notification_music_play, mCurrentPlayingSong.getTitle());
        remoteViewNormal.setTextViewText(R.id.tv_artist_notification_music_play, mCurrentPlayingSong.getArtist());
        remoteViewNormal.setOnClickPendingIntent(R.id.btn_play_pause_notification_music_play, generateOperateIntent(REQUEST_CODE_PLAY_PAUSE, ACTION_PLAY_PAUSE));
        remoteViewNormal.setOnClickPendingIntent(R.id.btn_skip_next_notification_music_play, generateOperateIntent(REQUEST_CODE_PLAY_NEXT, ACTION_PLAY_NEXT));
        remoteViewNormal.setOnClickPendingIntent(R.id.btn_cancel_notification_music_play, generateOperateIntent(REQUEST_CODE_STOP, ACTION_STOP));
        return remoteViewNormal;
    }

    private RemoteViews generateBigContentRemoteView() {
        RemoteViews remoteViewBig = new RemoteViews(getPackageName(), R.layout.remote_notification_music_play_big);
        remoteViewBig.setImageViewResource(R.id.iv_play_pause_notification_music_play_big,
                isPlaying() ? R.drawable.ic_pause_notification_grey_600_48dp : R.drawable.ic_play_notification_grey_600_48dp);
        remoteViewBig.setBitmap(R.id.iv_cover_notification_music_play_big, "setImageBitmap",
                BitmapFactory.decodeFile(new LocalAlbumModelImpl().getAlbumCoverPathByAlbumId(this, mCurrentPlayingSong.getAlbumId())));
        remoteViewBig.setTextViewText(R.id.tv_title_notification_music_play_big, mCurrentPlayingSong.getTitle());
        remoteViewBig.setTextViewText(R.id.tv_artist_album_notification_music_play_big,
                mCurrentPlayingSong.getArtist() + " - " + mCurrentPlayingSong.getAlbum());
        remoteViewBig.setOnClickPendingIntent(R.id.btn_play_pause_notification_music_play_big, generateOperateIntent(REQUEST_CODE_PLAY_PAUSE, ACTION_PLAY_PAUSE));
        remoteViewBig.setOnClickPendingIntent(R.id.btn_skip_previous_notification_music_play_big, generateOperateIntent(REQUEST_CODE_PLAY_PREVIOUS, ACTION_PLAY_PREVIOUS));
        remoteViewBig.setOnClickPendingIntent(R.id.btn_skip_next_notification_music_play_big, generateOperateIntent(REQUEST_CODE_PLAY_NEXT, ACTION_PLAY_NEXT));
        remoteViewBig.setOnClickPendingIntent(R.id.btn_cancel_notification_music_play_big, generateOperateIntent(REQUEST_CODE_STOP, ACTION_STOP));
        return remoteViewBig;
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
            notifyAllOnPlayStart();
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
        return !mCallbacks.isEmpty();
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
    public int getCurrentPosition() {
        if (mPlayListSongs == null || mPlayListSongs.isEmpty() || mCurrentPlayingSong == null) {
            return -1;
        }

        return MusicPlayUtil.getSongPosition(mCurrentPlayingSong, mPlayListSongs);
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
    public ArrayList<LocalSongEntity> getServicePlayListSongs() {
        if (mPlayListSongs == null) {
            mPlayListSongs = new ArrayList<>();
        }
        return mPlayListSongs;
    }

    @Override
    public void registerServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
        this.mCallbacks.add(callback);
    }

    @Override
    public void unregisterServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
        if (mCallbacks.isEmpty()) {
            return;
        }
        mCallbacks.remove(callback);
    }

    public class MusicPlayServiceBinder extends Binder implements MusicPlayServiceView {

        @Override
        public int getCurrentPosition() {
            return MusicPlayService.this.getCurrentPosition();
        }

        @Override
        public PlayStatus getServiceCurrentPlayStatus() {
            return MusicPlayService.this.getServiceCurrentPlayStatus();
        }

        @Override
        public LocalSongEntity getServiceCurrentPlayingSong() {
            return MusicPlayService.this.getServiceCurrentPlayingSong();
        }

        @Override
        public ArrayList<LocalSongEntity> getServicePlayListSongs() {
            return MusicPlayService.this.getServicePlayListSongs();
        }

        @Override
        public void setServiceCurrentPlaySong(LocalSongEntity currentPlaySong) {
            MusicPlayService.this.setServiceCurrentPlaySong(currentPlaySong);
        }

        @Override
        public void setServicePlayListSongs(ArrayList<LocalSongEntity> playSongs) {
            MusicPlayService.this.setServicePlayListSongs(playSongs);
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
        public void playSkip() {
            MusicPlayService.this.playSkip();
        }

        @Override
        public void play(int position) {
            MusicPlayService.this.play(position);
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
        public void clearServiceSongs() {
            MusicPlayService.this.clearServiceSongs();
        }

        @Override
        public void registerServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
            MusicPlayService.this.registerServicePlayCallback(callback);
        }

        @Override
        public void unregisterServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {
            MusicPlayService.this.unregisterServicePlayCallback(callback);
        }
    }

    private BroadcastReceiver mStopPlayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            releaseAll();
        }
    };

    private void releaseAll() {
        mCurrentPlayingSong = null;
        mPreviousPlayingSong = null;
        mPlayListSongs.clear();
        releaseMediaPlayer();
        cancelService();

        if (isRegisterCallback()) {
//            mMusicPlayServiceCallback.onPlayStop();
            notifyAllOnPlayStop();
        }
    }

    private void notifyAllOnPlayStart() {
        if (mCallbacks.isEmpty()) {
            return;
        }

        for (MusicPlayServiceCallback callback : mCallbacks) {
            callback.onPlayStart();
        }
    }

    private void notifyAllOnPlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong, int currentPositon) {
        if (mCallbacks.isEmpty()) {
            return;
        }

        for (MusicPlayServiceCallback callback : mCallbacks) {
            callback.onPlaySongChanged(previousPlaySong, currentPlayingSong, currentPositon);
        }
    }

    private void notifyAllOnPlayProgressUpdate(long currentPositionMillSec) {
        if (mCallbacks.isEmpty()) {
            return;
        }

        for (MusicPlayServiceCallback callback : mCallbacks) {
            callback.onPlayProgressUpdate(currentPositionMillSec);
        }
    }

    private void notifyAllOnPlayPause(long currentPositionMillSec) {
        if (mCallbacks.isEmpty()) {
            return;
        }

        for (MusicPlayServiceCallback callback : mCallbacks) {
            callback.onPlayPause(currentPositionMillSec);
        }
    }

    private void notifyAllOnPlayResume() {
        if (mCallbacks.isEmpty()) {
            return;
        }

        for (MusicPlayServiceCallback callback : mCallbacks) {
            callback.onPlayResume();
        }
    }

    private void notifyAllOnPlayStop() {
        if (mCallbacks.isEmpty()) {
            return;
        }

        for (MusicPlayServiceCallback callback : mCallbacks) {
            callback.onPlayStop();
        }
    }

    private void notifyAllOnPlayComplete() {
        if (mCallbacks.isEmpty()) {
            return;
        }

        for (MusicPlayServiceCallback callback : mCallbacks) {
            callback.onPlayComplete();
        }
    }


}
