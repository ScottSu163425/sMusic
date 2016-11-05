package com.scott.su.smusic.callback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.List;

/**
 * Created by asus on 2016/11/5.
 */

public interface LocalSongDisplayCallback {
    void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

    void onItemMoreClick(View view, int position, LocalSongEntity entity);

    void onDataLoading();

    void onDisplayDataChanged(List<LocalSongEntity> dataList);
}
