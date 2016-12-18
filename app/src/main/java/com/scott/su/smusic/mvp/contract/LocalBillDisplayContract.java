package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.su.scott.slibrary.mvp.presenter.IBaseDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalBillDisplayContract {

    interface LocalBillDisplayView extends IBaseDisplayView<LocalBillEntity> {
    }

    interface LocalBillBaseDisplayPresenter extends IBaseDisplayPresenter<LocalBillDisplayView, LocalBillEntity> {
    }

}
