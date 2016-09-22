package com.scott.su.smusic.mvp.view;

import android.view.View;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;

/**
 * Created by Administrator on 2016/9/22.
 */

public interface LocalSongBottomSheetView {
    void showSelectBillDialog(LocalSongEntity songToBeAdd);
    void goToAlbumDetail(LocalAlbumEntity entity);
    void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName);
    void showDeleteDialog(LocalSongEntity songEntity);
}
