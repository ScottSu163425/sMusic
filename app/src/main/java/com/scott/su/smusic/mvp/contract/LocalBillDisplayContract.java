package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.LocalBillEntity;
import com.su.scott.slibrary.mvp.presenter.IDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface LocalBillDisplayContract {
    interface LocalBillDisplayPresenter extends IDisplayPresenter<LocalBillDisplayView, LocalBillEntity> {
    }

    interface LocalBillDisplayView extends IBaseDisplayView<LocalBillEntity> {
    }

}
