package com.su.scott.slibrary.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.su.scott.slibrary.mvp.view.IView;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface IBaseDisplayPresenter<V extends IView,E> extends IPresenter<V> {

    void onSwipeRefresh();

    void onLoadMore();

    void onEmptyClick();

    void onErrorClick();

    void onItemClick(View itemView, E entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

}
