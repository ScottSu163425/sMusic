package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.view.BaseDisplayView;

import java.util.List;

/**
 * Created by asus on 2016/8/27.
 */
public interface LocalSongSelectionDisplayView extends BaseDisplayView<LocalSongEntity> {

    void selectAll();

    List<LocalSongEntity> getSelectedSongs();
}
