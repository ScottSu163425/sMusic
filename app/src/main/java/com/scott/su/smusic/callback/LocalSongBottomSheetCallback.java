package com.scott.su.smusic.callback;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetMenuFragment;

/**
 * Created by Administrator on 2016/9/22.
 */

public interface LocalSongBottomSheetCallback {
    void onAddToBillClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity);

    void onAlbumClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity);

    void onDeleteClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity);
}
