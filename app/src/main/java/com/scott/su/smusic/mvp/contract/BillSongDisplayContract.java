package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IBaseDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface BillSongDisplayContract {

    interface BillSongDisplayView extends IBaseDisplayView<LocalSongEntity> {
        LocalBillEntity getSongBillEntity();

        void setLoading();
    }

    interface BillSongBaseDisplayPresenter extends IBaseDisplayPresenter<BillSongDisplayView, LocalSongEntity> {

    }

}
