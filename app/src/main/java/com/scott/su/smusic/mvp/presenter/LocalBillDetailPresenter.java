package com.scott.su.smusic.mvp.presenter;

import android.view.View;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;

import java.util.List;

/**
 * Created by asus on 2016/8/29.
 */
public interface LocalBillDetailPresenter extends IPresenter, LocalSongBottomSheetPresenter {

    void onTransitionEnd();

    void onPlayFabClick();

    void onEditBillMenuItemClick();

    void onAddSongsMenuItemClick();

    void onClearBillMenuItemClick();

    void onDeleteBillMenuItemClick();

    void onClearBillConfirmed();

    void onDeleteBillMenuItemConfirmed();

    void onBillSongItemClick(View view, int position, LocalSongEntity entity);

    void onBillSongItemMoreClick(View view, int position, LocalSongEntity entity);

    void onSelectedLocalSongsResult(LocalBillEntity billToAddSong, List<LocalSongEntity> songsToAdd);

    void onEditBillNameConfiemed(String text);

}
