package com.scott.su.smusic.mvp.presenter;

import com.su.scott.slibrary.mvp.presenter.IPresenter;

/**
 * Created by asus on 2016/11/20.
 */

public interface MusicPlaySecondPresenter extends IPresenter {
    void onMusicPlayServiceConnected();

    void onMusicPlayServiceDisconnected();
}
