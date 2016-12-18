package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.su.scott.slibrary.mvp.presenter.IBaseDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalAlbumDisplayContract {

    interface LocalAlbumDisplayView extends IBaseDisplayView<LocalAlbumEntity> {
    }

    interface LocalAlbumBaseDisplayPresenter extends IBaseDisplayPresenter<LocalAlbumDisplayView, LocalAlbumEntity> {
    }

}
