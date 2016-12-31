package com.scott.su.smusic.mvp.contract;

import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IView;

/**
 * Created by asus on 2016/12/31.
 */

public interface LocalBillCreationContract {

    interface  LocalBillCreationView extends IView{

    }

    interface LocalBillCreationPresenter extends IPresenter<LocalBillCreationView>{

    }

}
