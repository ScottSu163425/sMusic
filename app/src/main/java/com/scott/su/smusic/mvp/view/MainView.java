package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainView extends BaseView {

    void updateSongDisplay();

    void updateBillDisplay();

    void updateAlbumDisplay();

    void showFab();

    void hideFab();

    void showCreateBillDialog();

    void dismissCreateBillDialog();

    void showCreateBillUnsuccessfully(String msg);

    void showCreateBillSuccessfully(LocalSongBillEntity billEntity);

}
