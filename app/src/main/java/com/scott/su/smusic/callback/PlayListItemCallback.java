package com.scott.su.smusic.callback;

import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;

/**
 * Created by Administrator on 2016/10/24.
 */

public interface PlayListItemCallback {
    void onItemClick(View itemView, LocalSongEntity entity, int position);

    void onItemRemoveClick(View view, int position, LocalSongEntity entity);
}
