package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IBaseDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface PlayListSecondDisplayContract {

    interface PlayListSecondDisplayView extends IBaseDisplayView<LocalSongEntity> {
    }

    interface PlayListSecondBaseDisplayPresenter extends IBaseDisplayPresenter<PlayListSecondDisplayView,LocalSongEntity> {
    }

}
