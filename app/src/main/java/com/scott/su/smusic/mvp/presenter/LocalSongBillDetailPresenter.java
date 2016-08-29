package com.scott.su.smusic.mvp.presenter;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.presenter.BasePresenter;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public interface LocalSongBillDetailPresenter extends BasePresenter {
    void onAddSongsMenuClick();

    void onSelectedLocalSongsResult(LocalSongBillEntity billToAddSong, List<LocalSongEntity> songsToAdd);
}
