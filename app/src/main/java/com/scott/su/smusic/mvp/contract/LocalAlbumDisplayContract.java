package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.su.scott.slibrary.mvp.presenter.IDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalAlbumDisplayContract {
    interface LocalAlbumDisplayPresenter extends IDisplayPresenter<LocalAlbumDisplayView,LocalAlbumEntity> {
    }

    interface LocalAlbumDisplayView extends IBaseDisplayView<LocalAlbumEntity> {
    }

}
