package com.scott.su.smusic.mvp.contract;

import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

/**
 * Created by asus on 2016/12/14.
 */

public interface MusicPlaySecondContract {
    interface MusicPlaySecondPresenter extends IPresenter<MusicPlaySecondView> {
        void onMusicPlayServiceConnected();

        void onMusicPlayServiceDisconnected();
    }

    interface MusicPlaySecondView extends IBaseView {
    }

}
