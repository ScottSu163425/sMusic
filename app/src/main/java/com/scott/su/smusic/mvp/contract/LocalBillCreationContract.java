package com.scott.su.smusic.mvp.contract;

import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

/**
 * Created by asus on 2017/1/21.
 */

public interface LocalBillCreationContract {

    interface LocalBillCreationView extends IBaseView {
        void onCreateBillSuccessfully();

        void notifyLocalBillChanged();
    }

    interface LocalBillCreationPresenter extends IPresenter<LocalBillCreationView> {
        void onCreateBillConfirm(String billName);
    }

}
