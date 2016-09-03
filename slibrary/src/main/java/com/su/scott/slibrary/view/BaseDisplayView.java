package com.su.scott.slibrary.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.su.scott.slibrary.presenter.BaseDisplayPresenter;
import com.su.scott.slibrary.presenter.BasePresenter;

import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface BaseDisplayView<E> extends BaseView {

    void showLoading();

    void showEmpty();

    void showError();

    void reInitialize();

    void notifyItemChanged(int position);

    void performSwipeRefresh();

    void stopSwipeRefresh();

    void display();

    void setDisplayData(@NonNull List<E> dataList);

    void setLoadMoreData(@NonNull List<E> dataList);

    void handleItemClick(View itemView, E entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

}
