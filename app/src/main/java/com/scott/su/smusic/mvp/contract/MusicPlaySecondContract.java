package com.scott.su.smusic.mvp.contract;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

/**
 * Created by asus on 2016/12/14.
 */

public interface MusicPlaySecondContract {

    interface MusicPlaySecondView extends IBaseView {
        LocalSongEntity getCurrentPlayingSong();

        void setCurrentPlayingSong(LocalSongEntity entity);

        void updatePlayList(int currentPosition);

        void backToMusicPlayMain();

        void updateVolume(int realVolume);

        void bindMusicPlayService();

        void unbindMusicPlayService();

        void registerMusicPlayCallback();

        void unregisterMusicPlayCallback();

        void registerVolumeReceiver();

        void unregisterVolumeReceiver();
    }

    interface MusicPlaySecondPresenter extends IPresenter<MusicPlaySecondView> {
        void onMusicPlayServiceConnected();

        void onMusicPlayServiceDisconnected();

        void onPlayListItemClick(View itemView, LocalSongEntity entity, int position);

        void onServicePlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong, int currentPosition);

        void onUserSeekVolume(int realVolume);
    }

}
