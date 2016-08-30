package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/29.
 */
public interface LocalSongBillDetailView extends BaseView {

    void goToLocalSongSelectionActivity();

    void showAddSongsUnsuccessfully(String msg);

    void showAddSongsSuccessfully(String msg);

    void refreshBillCover(LocalSongBillEntity billEntity);

    void refreshBillSongDisplay(LocalSongBillEntity billEntity);

}
