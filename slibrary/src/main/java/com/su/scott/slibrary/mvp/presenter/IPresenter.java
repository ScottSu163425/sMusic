package com.su.scott.slibrary.mvp.presenter;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface IPresenter {
    void onViewFirstTimeCreated();

    void onViewResume();

    void onViewWillDestroy();
}
