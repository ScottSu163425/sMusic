package com.scott.su.smusic.mvp.view;

import com.su.scott.slibrary.view.BaseView;

/**
 * Created by asus on 2016/8/27.
 */
public interface LocalSongSelectionView extends BaseView {

    void selectAll();

    void showOrHideFinishSelectionButtn(boolean isShow);

    void finishSelection();

}
