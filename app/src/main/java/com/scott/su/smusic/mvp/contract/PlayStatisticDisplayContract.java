package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.su.scott.slibrary.mvp.presenter.IDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface PlayStatisticDisplayContract {
    interface PlayStatisticDisplayPresenter extends IDisplayPresenter<PlayStatisticDisplayView,PlayStatisticEntity> {
    }

    interface PlayStatisticDisplayView extends IBaseDisplayView<PlayStatisticEntity> {
    }

}
