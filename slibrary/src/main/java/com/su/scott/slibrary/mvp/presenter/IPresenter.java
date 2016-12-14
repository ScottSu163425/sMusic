package com.su.scott.slibrary.mvp.presenter;

import com.su.scott.slibrary.mvp.view.IView;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface IPresenter<V extends IView> {
    void attachView(V view);

    void detachView();

    boolean isViewAttaching();

    V getView();

    void onViewFirstTimeCreated();

    void onViewResume();

    void onViewWillDestroy();
}
