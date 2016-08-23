package com.scott.su.smusic.mvp.view;

import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainView extends BaseView {

    void updateSongDisplay();

    void updateBillDisplay();

    void updateAlbumDisplay();

    void scrollBillToLast();

    void showFab();

    void hideFab();

    void showCreateBillDialog();

    void dismissCreateBillDialog();


}
