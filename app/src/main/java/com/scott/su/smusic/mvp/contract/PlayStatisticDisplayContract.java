package com.scott.su.smusic.mvp.contract;

import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.su.scott.slibrary.mvp.presenter.IBaseDisplayPresenter;
import com.su.scott.slibrary.mvp.view.IBaseDisplayView;

/**
 * Created by asus on 2016/12/14.
 */

public interface PlayStatisticDisplayContract {

    interface PlayStatisticDisplayView extends IBaseDisplayView<PlayStatisticEntity> {
    }

    interface PlayStatisticBaseDisplayPresenter extends IBaseDisplayPresenter<PlayStatisticDisplayView,PlayStatisticEntity> {
    }

}
