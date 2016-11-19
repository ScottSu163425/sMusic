package com.scott.su.smusic.mvp.view;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/19.
 */

public interface PlayStatisticView extends BaseView {
    void goToMusicPlay(LocalSongEntity songEntity, ArrayList<LocalSongEntity> songEntityList);

    void goToMusicPlayWithCover(LocalSongEntity songEntity, ArrayList<LocalSongEntity> songEntityList, View sharedElement, String transitionName);
}
