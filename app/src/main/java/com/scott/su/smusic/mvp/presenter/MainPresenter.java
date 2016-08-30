package com.scott.su.smusic.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.presenter.BasePresenter;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public interface MainPresenter extends BasePresenter {

    void onFabClick();

    void onCreateBillConfirm(String text);

    void onBillItemClick(View itemView, LocalSongBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

    void onSelectedLocalSongsResult(LocalSongBillEntity billToAddSong, List<LocalSongEntity> songsToAdd);

}
