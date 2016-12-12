package com.scott.su.smusic.mvp.view;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.view.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public interface SearchView extends BaseView ,LocalSongBottomSheetView{
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
