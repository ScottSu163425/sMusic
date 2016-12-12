package com.scott.su.smusic.mvp.view;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.view.BaseView;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface LocalAlbumDetailView extends BaseView ,LocalSongBottomSheetView{
    LocalAlbumEntity getCurrentAlbumEntity();

    void loadAlbumCover(String path);

    void goToMusicPlay(LocalSongEntity songEntity);

    void goToMusicPlayWithCover(LocalSongEntity songEntity);

    void showAlbumSongBottomSheet(LocalSongEntity songEntity);
}
