package com.scott.su.smusic.mvp.presenter;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface ILocalSongBottomSheetPresenter {
    void onBottomSheetAddToBillClick(LocalSongEntity songEntity);

    void onBottomSheetAlbumClick(LocalSongEntity songEntity);

    void onBottomSheetDeleteClick(LocalSongEntity songEntity);

    void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity);

    void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity);

}
