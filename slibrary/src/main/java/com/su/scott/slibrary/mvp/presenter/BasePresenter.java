package com.su.scott.slibrary.mvp.presenter;

import com.su.scott.slibrary.mvp.view.IView;

import java.lang.ref.WeakReference;

/**
 * Created by asus on 2016/12/14.
 */

public abstract class BasePresenter<V extends IView> implements IPresenter<V> {
    private WeakReference<V> mViewReference;


    public BasePresenter(V view) {
        attachView(view);
    }

    @Override
    public void attachView(V view) {
        if (!isViewAttaching()) {
            mViewReference = new WeakReference<>(view);
        }
    }

    @Override
    public void detachView() {
        if (isViewAttaching()) {
            mViewReference.clear();
            mViewReference = null;
        }
    }

    @Override
    public boolean isViewAttaching() {
        return mViewReference != null && mViewReference.get() != null;
    }

    @Override
    public V getView() {
        if (isViewAttaching()) {
            return mViewReference.get();
        }
        return null;
    }

}
