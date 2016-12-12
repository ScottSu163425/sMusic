package com.scott.su.smusic.mvp.presenter;

import com.su.scott.slibrary.mvp.presenter.IPresenter;

/**
 * Created by asus on 2016/8/28.
 */
public interface LocalSongSelectionPresenter extends IPresenter {

    void onSelectedCountChanged(boolean isEmpty);

    void onSelectAllClick();

    void onFinishSelectionClick();
}
