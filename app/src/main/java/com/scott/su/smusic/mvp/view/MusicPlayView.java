package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/9/4.
 */
public interface MusicPlayView extends BaseView {
    LocalSongEntity getCurrentPlayingLocalSongEntity();

    void loadCover(String path);
}
