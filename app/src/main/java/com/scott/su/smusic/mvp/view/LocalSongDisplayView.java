package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.view.BaseDisplayView;

/**
 * Created by asus on 2016/8/19.
 */
public interface LocalSongDisplayView extends BaseDisplayView<LocalSongEntity> {

    void setLoading();

}
