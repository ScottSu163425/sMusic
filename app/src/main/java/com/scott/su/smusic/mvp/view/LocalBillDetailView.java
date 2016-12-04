package com.scott.su.smusic.mvp.view;

import android.widget.ImageView;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/29.
 */
public interface LocalBillDetailView extends BaseView, LocalSongBottomSheetView {

    LocalBillEntity getBillEntity();

    ImageView getCoverImageView();

    void onEnterBillEmpty();

    void setBillEntity(LocalBillEntity billEntity);

    void loadCover(String coverPath, boolean needReveal);

    void showFab();

    void hideFab();

    void goToLocalSongSelectionActivity();

    void onAddSongsToBillSuccessfully();

    void refreshBillSongDisplay(LocalBillEntity billEntity);

    void showBillSongBottomSheet(LocalSongEntity songEntity);

    void showClearBillSongsConfirmDialog();

    void showDeleteBillConfirmDialog();

    void onDeleteBillSuccessfully();

    void goToMusicPlayWithCoverSharedElement(LocalSongEntity entity);

    void goToMusicPlayWithCoverAndFabSharedElement(LocalSongEntity entity);

    void goToMusicPlayWithFab(LocalSongEntity entity);

    void showEditBillNameDialog();

    void dismissEditBillNameDialog();

    void updateBillInfo();

    boolean isFabVisiable();

    void goToMusicPlayWithoutFab(LocalSongEntity entity);

}
