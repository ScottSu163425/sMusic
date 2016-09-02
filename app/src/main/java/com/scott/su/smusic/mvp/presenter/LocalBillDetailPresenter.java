package com.scott.su.smusic.mvp.presenter;

import android.view.View;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.presenter.BasePresenter;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public interface LocalBillDetailPresenter extends BasePresenter, LocalSongBottomSheetPresenter {

    void onAddSongsMenuItemClick();

    void onClearBillMenuItemClick();

    void onDeleteBillMenuItemClick();

    void onClearBillConfirmed();

    void onDeleteBillMenuItemConfirmed();

    void onBillSongItemMoreClick(View view, int position, LocalSongEntity entity);

    void onSelectedLocalSongsResult(LocalBillEntity billToAddSong, List<LocalSongEntity> songsToAdd);


}
