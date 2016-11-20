package com.scott.su.smusic.mvp.view;

import android.graphics.Bitmap;

import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/9/4.
 */
public interface MusicPlayView extends BaseView {
    void showBillSelectionDialog();
    void loadBlurCover(Bitmap bitmap);
}
