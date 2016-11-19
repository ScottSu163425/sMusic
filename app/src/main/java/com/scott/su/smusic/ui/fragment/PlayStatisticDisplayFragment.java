package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.adapter.PlayStatisticDisplayAdapter;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.presenter.PlayStatisticDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.PlayStatisticDisplayPresenterImpl;
import com.scott.su.smusic.mvp.view.PlayStatisticDisplayView;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticDisplayFragment extends BaseDisplayFragment<PlayStatisticEntity, RecyclerView.ViewHolder> implements PlayStatisticDisplayView {
    private PlayStatisticDisplayPresenter mDisplayPresenter;
    private PlayStatisticDisplayAdapter mDisplayAdapter;


    @NonNull
    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (mDisplayAdapter == null) {
            mDisplayAdapter = new PlayStatisticDisplayAdapter(getActivity());
        }
        return mDisplayAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected int getLoadingLayoutRes() {
        return 0;
    }

    @Override
    protected int getEmptyLayoutRes() {
        return 0;
    }

    @Override
    protected int getErrorLayoutRes() {
        return 0;
    }

    @Override
    protected int getFooterLayoutRes() {
        return 0;
    }

    @Override
    protected boolean canSwipeRefresh() {
        return false;
    }

    @Override
    protected boolean canLoadMore() {
        return false;
    }

    @Override
    protected void onSwipeRefresh() {

    }

    @Override
    protected void onLoadMore() {

    }

    @Override
    protected void onEmptyClick() {

    }

    @Override
    protected void onErrorClick() {

    }

    @Override
    protected void onFirstTimeCreateView() {
        mDisplayPresenter = new PlayStatisticDisplayPresenterImpl(this);
        mDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public ArrayList<PlayStatisticEntity> getDisplayDataList() {
        return (ArrayList<PlayStatisticEntity>) mDisplayAdapter.getDataList();
    }

    @Override
    public void reInitialize() {

    }

    @Override
    public void setDisplayData(@NonNull List<PlayStatisticEntity> dataList) {
        mDisplayAdapter.setDataList(dataList);
        mDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void addLoadMoreData(@NonNull List<PlayStatisticEntity> dataList) {
        mDisplayAdapter.addDataList(dataList);
        mDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleItemClick(View itemView, PlayStatisticEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {

    }

    @Override
    public void onDestroy() {
        mDisplayPresenter.onViewWillDestroy();
        super.onDestroy();
    }
}
