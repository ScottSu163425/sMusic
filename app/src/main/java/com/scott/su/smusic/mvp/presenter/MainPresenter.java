package com.scott.su.smusic.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.callback.DrawerMenuCallback;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.presenter.BasePresenter;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainPresenter extends BasePresenter, LocalSongBottomSheetPresenter, DrawerMenuCallback {

    void onLocalSongItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

    void onLocalSongItemMoreClick(LocalSongEntity songEntity);

    void onFabClick();

    void onFabLongClick();

    void onCreateBillConfirm(String text);

    void onBillItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

    void onAlbumItemClick(View itemView, LocalAlbumEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

    void onSelectedLocalSongsResult(LocalBillEntity billToAddSong, List<LocalSongEntity> songsToAdd);

}
