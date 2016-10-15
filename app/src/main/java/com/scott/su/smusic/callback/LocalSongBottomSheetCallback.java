package com.scott.su.smusic.callback;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetFragment;

/**
 * Created by Administrator on 2016/9/22.
 */

public interface LocalSongBottomSheetCallback {
    void onAddToBillClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity);

    void onAlbumClick(LocalSongBottomSheetFragment fragment,LocalSongEntity songEntity);

    void onDeleteClick(LocalSongBottomSheetFragment fragment,LocalSongEntity songEntity);
}
