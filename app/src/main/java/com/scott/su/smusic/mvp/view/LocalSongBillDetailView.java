package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/29.
 */
public interface LocalSongBillDetailView extends BaseView {

    LocalBillEntity getBillEntity();

    void setBillEntity(LocalBillEntity billEntity);

    void loadCover(String coverPath, boolean needReveal);

    void showFab();

    void hideFab();

    void goToLocalSongSelectionActivity();

    void showAddSongsToBillUnsuccessfully(String msg);

    void showAddSongsToBillSuccessfully();

    void refreshBillSongDisplay(LocalBillEntity billEntity);

    void showSongBottomSheet(LocalSongEntity songEntity);

    void showDeleteBillSongConfirmDialog(LocalSongEntity songEntity);

    void showClearBillSongsConfirmDialog();

    void showDeleteBillConfirmDialog();

    void showDeleteBillSuccessfully();

    void showDeleteBillUnsuccessfully(String msg);

    void showBillSelectionDialog(LocalSongEntity songToBeAdd);

    void showAddSongToSelectedBillUnsuccessfully(String msg);

}
