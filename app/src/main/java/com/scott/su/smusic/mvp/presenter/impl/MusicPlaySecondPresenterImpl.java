package com.scott.su.smusic.mvp.presenter.impl;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.MusicPlaySecondContract;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

/**
 * Created by asus on 2016/11/20.
 */

public class MusicPlaySecondPresenterImpl extends BasePresenter<MusicPlaySecondContract.MusicPlaySecondView>
        implements MusicPlaySecondContract.MusicPlaySecondPresenter {


    public MusicPlaySecondPresenterImpl(MusicPlaySecondContract.MusicPlaySecondView view) {
        super(view);
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().bindMusicPlayService();
    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {
        getView().unregisterVolumeReceiver();
        getView().unbindMusicPlayService();
        getView().unregisterMusicPlayCallback();
    }

    @Override
    public void onMusicPlayServiceConnected() {
        getView().initView();
        getView().initData();
        getView().initListener();

        getView().registerVolumeReceiver();
        getView().registerMusicPlayCallback();
    }

    @Override
    public void onMusicPlayServiceDisconnected() {

    }

    @Override
    public void onPlayListItemClick(View itemView, LocalSongEntity entity, int position) {
        if (getView().getCurrentPlayingSong().getSongId() == entity.getSongId()) {
            getView().backToMusicPlayMain();
        } else {
            getView().setCurrentPlayingSong(entity);
        }
    }

    @Override
    public void onServicePlaySongChanged(LocalSongEntity previousPlaySong, LocalSongEntity currentPlayingSong, int currentPosition) {
        getView().updatePlayList(currentPosition);
    }

    @Override
    public void onUserSeekVolume(int realVolume) {
        getView().updateVolume(realVolume);
    }


}
