package com.scott.su.smusic.mvp.contract;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongBottomSheetPresenter;
import com.scott.su.smusic.mvp.view.LocalSongBottomSheetView;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

import java.util.List;

/**
 * Created by asus on 2016/12/14.
 */

public interface SearchContract {
    interface SearchView extends IBaseView, LocalSongBottomSheetView {
        String getCurrentKeyword();

        void showLoading();

        void setResult(List result);

        void showResult();

        void showEmpty();

        void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName);

        void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName);

        void goToAlbumDetail(LocalAlbumEntity entity);

        void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName);

        void showLocalSongBottomSheet(LocalSongEntity songEntity);
    }

    interface SearchPresenter extends IPresenter<SearchView>, LocalSongBottomSheetPresenter {

        void onSearchTextChanged(String keyword);

        void onLocalSongClick(LocalSongEntity entity, View sharedElement, String transitionName);

        void onLocalSongMoreClick(LocalSongEntity entity);

        void onLocalBillClick(LocalBillEntity entity, View sharedElement, String transitionName);

        void onLocalAlbumClick(LocalAlbumEntity entity, View sharedElement, String transitionName);
    }

}
