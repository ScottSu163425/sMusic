package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/29.
 */
public interface LocalBillDetailView extends BaseView,LocalSongBottomSheetView {

    LocalBillEntity getBillEntity();

    void showEnterBillEmpty();

    void setBillEntity(LocalBillEntity billEntity);

    void loadCover(String coverPath, boolean needReveal);

    void showFab();

    void hideFab();

    void goToLocalSongSelectionActivity();

    void showAddSongsToBillSuccessfully();

    void refreshBillSongDisplay(LocalBillEntity billEntity);

    void showBillSongBottomSheet(LocalSongEntity songEntity);

    void showClearBillSongsConfirmDialog();

    void showDeleteBillConfirmDialog();

    void showDeleteBillSuccessfully();

    void goToMusicPlayWithCoverSharedElement(LocalSongEntity entity);

    void goToMusicPlay(LocalSongEntity entity);

    void showEditBillNameDialog();

    void dismissEditBillNameDialog();

    void updateBillInfo();

}
