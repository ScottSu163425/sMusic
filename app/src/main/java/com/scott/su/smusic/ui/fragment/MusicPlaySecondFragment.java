package com.scott.su.smusic.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.MusicPlaySecondContract;
import com.scott.su.smusic.mvp.presenter.impl.MusicPlaySecondPresenterImpl;
import com.scott.su.smusic.service.MusicPlayService;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseFragment;
import com.su.scott.slibrary.mvp.presenter.IPresenter;

/**
 * Created by asus on 2016/11/20.
 */

public class MusicPlaySecondFragment extends BaseFragment
        implements MusicPlaySecondContract.MusicPlaySecondView {
    private static final int STEP_TIMES = 5;
    private MusicPlaySecondContract.MusicPlaySecondPresenter mMusicPlaySecondPresenter;
    private AudioManager mAudioManager;
    private View mRootView;
    private AppCompatSeekBar mVolumeSeekBar;
    private MusicPlayService.MusicPlayServiceBinder mMusicPlayServiceBinder;
    private PlayListSecondDisplayFragment mPlayListSecondDisplayFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_music_play_second, container, false);
        }
        return mRootView;
    }

    @Override
    protected IPresenter getPresenter() {
        if (mMusicPlaySecondPresenter == null) {
            mMusicPlaySecondPresenter = new MusicPlaySecondPresenterImpl(this);
        }
        return mMusicPlaySecondPresenter;
    }

    @Override
    protected void onFirstTimeCreateView() {
        mMusicPlaySecondPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void initPreData() {
    }

    @Override
    public void initView() {
        mVolumeSeekBar = (AppCompatSeekBar) mRootView.findViewById(R.id.seek_bar_volume_fragment_music_play_second);
    }

    @Override
    public void initData() {
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mPlayListSecondDisplayFragment = new PlayListSecondDisplayFragment();

        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * STEP_TIMES;
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * STEP_TIMES;

        mVolumeSeekBar.setMax(max);
        mVolumeSeekBar.setProgress(current);

        if (mMusicPlayServiceBinder != null) {
            mPlayListSecondDisplayFragment.setCurrentPosition(mMusicPlayServiceBinder.getCurrentPositon());
            mPlayListSecondDisplayFragment.setPlayingSongEntityList(mMusicPlayServiceBinder.getServicePlayListSongs());
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_display_play_list_fragment_music_play_second, mPlayListSecondDisplayFragment)
                .commitNow();
    }

    @Override
    public void initListener() {
        mPlayListSecondDisplayFragment.setItemClickCallback(new ItemClickCallback<LocalSongEntity>() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mMusicPlaySecondPresenter.onPlayListItemClick(itemView, entity, position);
            }
        });
        
        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mMusicPlaySecondPresenter.onUserSeekVolume(progress / STEP_TIMES);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public LocalSongEntity getCurrentPlayingSong() {
        if (mMusicPlayServiceBinder != null) {
            return mMusicPlayServiceBinder.getServiceCurrentPlayingSong();
        }
        return null;
    }

    @Override
    public void setCurrentPlayingSong(LocalSongEntity entity) {
        if (mMusicPlayServiceBinder != null) {
            mMusicPlayServiceBinder.setServiceCurrentPlaySong(entity);
            mMusicPlayServiceBinder.playSkip();
        }
    }

    @Override
    public void updatePlayList(int currentPosition) {
       mPlayListSecondDisplayFragment.setCurrentPosition(currentPosition);
    }

    @Override
    public void backToMusicPlayMain() {
        getActivity().onBackPressed();
    }

    @Override
    public void updateVolume(int realVoume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, realVoume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    @Override
    public void bindMusicPlayService() {
        Intent intent = new Intent(getActivity(), MusicPlayService.class);
        getActivity().bindService(intent, mMusicPlayServiceConnection, getActivity().BIND_AUTO_CREATE);
    }

    @Override
    public void unbindMusicPlayService() {
        if (mMusicPlayServiceConnection != null) {
            getActivity().unbindService(mMusicPlayServiceConnection);
        }
    }

    @Override
    public void registerMusicPlayCallback() {
        if (mMusicPlayServiceBinder != null) {
            mMusicPlayServiceBinder.registerServicePlayCallback(mMusicPlayServiceCallback);
        }
    }

    @Override
    public void unregisterMusicPlayCallback() {
        if (mMusicPlayServiceBinder != null) {
            mMusicPlayServiceBinder.unregisterServicePlayCallback(mMusicPlayServiceCallback);
        }
    }

    @Override
    public void registerVolumeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        getActivity().registerReceiver(mVolumeReceiver, filter);
    }

    @Override
    public void unregisterVolumeReceiver() {
        getActivity().unregisterReceiver(mVolumeReceiver);
    }

    private BroadcastReceiver mVolumeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                mVolumeSeekBar.setProgress(currVolume * STEP_TIMES);
            }
        }
    };

    private ServiceConnection mMusicPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicPlayServiceBinder = (MusicPlayService.MusicPlayServiceBinder) service;
            mMusicPlaySecondPresenter.onMusicPlayServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicPlaySecondPresenter.onMusicPlayServiceDisconnected();
        }
    };

    private MusicPlayServiceCallback mMusicPlayServiceCallback = new MusicPlayServiceCallback() {
        @Override
        public void onPlayStart() {

        }

        @Override
        public void onPlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong, int currentPosition) {
            mMusicPlaySecondPresenter.onServicePlaySongChanged(previousPlaySong, currentPlayingSong, currentPosition);
        }

        @Override
        public void onPlayProgressUpdate(long currentPositionMillSec) {

        }

        @Override
        public void onPlayPause(long currentPositionMillSec) {

        }

        @Override
        public void onPlayResume() {

        }

        @Override
        public void onPlayStop() {

        }

        @Override
        public void onPlayComplete() {

        }
    };

    @Override
    public void onDestroy() {
        mMusicPlaySecondPresenter.onViewWillDestroy();
        super.onDestroy();
    }

}
