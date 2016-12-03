package com.scott.su.smusic.mvp.presenter;

import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by asus on 2016/11/20.
 */

public interface MusicPlaySecondPresenter extends BasePresenter {
    void onMusicPlayServiceConnected();

    void onMusicPlayServiceDisconnected();
}
