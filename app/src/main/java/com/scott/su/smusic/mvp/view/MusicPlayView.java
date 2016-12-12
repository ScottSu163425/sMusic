package com.scott.su.smusic.mvp.view;

import android.graphics.Bitmap;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.view.BaseView;

/**
 * Created by asus on 2016/9/4.
 */
public interface MusicPlayView extends BaseView {
    void showBillSelectionDialog(LocalSongEntity songEntity);

    LocalSongEntity getCurrentPlayingSong();

    void loadBlurCover(Bitmap bitmap);

    void showMusicPlayMainFragment();

    void hideMusicPlayMainFragment();

    void showMusicPlaySecondFragment();

    void hideMusicPlaySecondFragment();
}
