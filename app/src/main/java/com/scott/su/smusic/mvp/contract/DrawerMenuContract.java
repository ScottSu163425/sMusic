package com.scott.su.smusic.mvp.contract;

import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IView;

/**
 * Created by asus on 2016/12/18.
 */

public interface DrawerMenuContract {
     interface DrawerMenuView extends IView {

    }

   interface DrawerMenuPresenter extends IPresenter<DrawerMenuView> {

    }

}
