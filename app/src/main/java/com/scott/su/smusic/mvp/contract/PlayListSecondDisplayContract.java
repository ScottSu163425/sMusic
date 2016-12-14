package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface PlayListSecondDisplayContract {
    interface PlayListSecondDisplayPresenter extends IDisplayPresenter<PlayListSecondDisplayView,LocalSongEntity> {
    }

    interface PlayListSecondDisplayView extends IBaseDisplayView<LocalSongEntity> {
    }

}
