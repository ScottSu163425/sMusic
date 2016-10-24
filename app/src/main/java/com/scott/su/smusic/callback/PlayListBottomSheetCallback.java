package com.scott.su.smusic.callback;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;

/**
 * Created by Administrator on 2016/10/24.
 */

public interface PlayListBottomSheetCallback {
    void onPlayListItemClick(View itemView, LocalSongEntity entity, int position);

    void onPlayListItemRemoveClick(View view, int position, LocalSongEntity entity);

    void onPlayListClearClick(View view);
}
