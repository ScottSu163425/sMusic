package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IBaseDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

import java.util.ArrayList;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalSongSelectionDisplayContract {

    interface LocalSongSelectionDisplayView extends IBaseDisplayView<LocalSongEntity> {

        void selectAll();

        ArrayList<LocalSongEntity> getSelectedSongs();
    }

    interface LocalSongSelectionBaseDisplayPresenter extends IBaseDisplayPresenter<LocalSongSelectionDisplayView, LocalSongEntity> {
    }

}
