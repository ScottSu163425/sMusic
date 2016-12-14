package com.scott.su.smusic.mvp.contract;

import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalSongSelectionContract {
    interface LocalSongSelectionPresenter extends IPresenter<LocalSongSelectionView> {

        void onSelectedCountChanged(boolean isEmpty);

        void onSelectAllClick();

        void onFinishSelectionClick();
    }

    interface LocalSongSelectionView extends IBaseView {

        void selectAll();

        void showOrHideFinishSelectionButton(boolean isShow);

        void finishSelection();

    }
}
