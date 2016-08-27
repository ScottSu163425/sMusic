package com.scott.su.smusic.mvp.presenter;

import com.su.scott.slibrary.presenter.BasePresenter;

/**
 * Created by asus on 2016/8/28.
 */
public interface LocalSongSelectionPresenter extends BasePresenter {

    void onSelectedCountChanged(boolean isEmpty);

    void onSelectAllClick();

    void onFinishSelectionClick();
}
