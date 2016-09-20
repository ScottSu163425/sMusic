package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.su.scott.slibrary.view.BaseView;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface LocalAlbumDetailView extends BaseView {
    LocalAlbumEntity getCurrentAlbumEntity();

    void loadAlbumCover(String path);
}
