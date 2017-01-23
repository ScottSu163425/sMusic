package com.scott.su.smusic.mvp.contract;

import android.view.View;
import android.widget.ImageView;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.ILocalSongBottomSheetPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBottomSheetView;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

import java.util.List;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalBillDetailContract {

    interface LocalBillDetailView extends IBaseView, LocalSongBottomSheetView {

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

        void notifyBillChanged();

    }

    interface ILocalBillDetailPresenter extends IPresenter<LocalBillDetailView>, ILocalSongBottomSheetPresenter {

        void onTransitionEnd();

        void onPlayFabClick();

        void onEditBillMenuItemClick();

        void onAddSongsMenuItemClick();

        void onClearBillMenuItemClick();

        void onDeleteBillMenuItemClick();

        void onClearBillConfirmed();

        void onDeleteBillMenuItemConfirmed();

        void onBillSongItemClick(View view, int position, LocalSongEntity entity);

        void onBillSongItemMoreClick(View view, int position, LocalSongEntity entity);

        void onSelectedLocalSongsResult(LocalBillEntity billToAddSong, List<LocalSongEntity> songsToAdd);

        void onEditBillNameConfiemed(String text);
    }

}
