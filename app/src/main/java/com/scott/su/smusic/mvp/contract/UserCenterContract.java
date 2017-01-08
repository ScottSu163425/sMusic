package com.scott.su.smusic.mvp.contract;

import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

/**
 * Created by asus on 2017/1/8.
 */

public interface UserCenterContract {
    interface UserCenterView extends IBaseView{

    }

    interface UserCenterPresenter extends IPresenter<UserCenterView> {

    }

}
