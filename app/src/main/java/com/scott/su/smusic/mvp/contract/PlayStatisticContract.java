package com.scott.su.smusic.mvp.contract;

import android.view.View;

import com.scott.su.smusic.callback.PlayStatisticItemClickCallback;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

import java.util.ArrayList;

/**
 * Created by asus on 2016/12/14.
 */

public interface PlayStatisticContract {
    interface PlayStatisticPresenter extends IPresenter<PlayStatisticView>,PlayStatisticItemClickCallback {
    }

    interface PlayStatisticView extends IBaseView {
        void goToMusicPlay(LocalSongEntity songEntity, ArrayList<LocalSongEntity> songEntityList);

        void goToMusicPlayWithCover(LocalSongEntity songEntity, ArrayList<LocalSongEntity> songEntityList, View sharedElement, String transitionName);
    }

}
