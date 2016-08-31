package com.scott.su.smusic.mvp.presenter;

import com.scott.su.smusic.entity.LocalSongEntity;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface LocalSongBottomSheetPresenter {
    void onBottomSheetAddToBillClick(LocalSongEntity songEntity);

    void onBottomSheetArtistClick(LocalSongEntity songEntity);

    void onBottomSheetAlbumClick(LocalSongEntity songEntity);

    void onBottomSheetShareClick(LocalSongEntity songEntity);

    void onBottomSheetDeleteClick(LocalSongEntity songEntity);
}
