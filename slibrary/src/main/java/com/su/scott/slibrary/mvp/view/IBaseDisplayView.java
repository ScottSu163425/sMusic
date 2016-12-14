package com.su.scott.slibrary.mvp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface IBaseDisplayView<E> extends IBaseView {

    ArrayList<E> getDisplayDataList();

    void showLoading();

    void showEmpty();

    void showError();

    void reInitialize();

    void performSwipeRefresh();

    void stopSwipeRefresh();

    void display();

    void setDisplayData(@NonNull List<E> dataList);

    void addLoadMoreData(@NonNull List<E> dataList);

    void handleItemClick(View itemView, E entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

    void notifyItemChanged(int position);
}
