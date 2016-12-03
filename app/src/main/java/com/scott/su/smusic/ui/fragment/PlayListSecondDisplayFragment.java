package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.adapter.PlayListSecondDisplayAdapter;
import com.scott.su.smusic.adapter.holder.PlayListSecondViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.PlayListSecondDisplayPresenter;
import com.scott.su.smusic.mvp.view.PlayListSecondDisplayView;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/12/3.
 */

public class PlayListSecondDisplayFragment extends BaseDisplayFragment<LocalSongEntity, PlayListSecondViewHolder> implements PlayListSecondDisplayView {
    private PlayListSecondDisplayPresenter mDisplayPresenter;
    private PlayListSecondDisplayAdapter mDisplayAdapter;
    private List<LocalSongEntity> mPlayingSongEntityList;


    @Override
    public void onResume() {
        super.onResume();

        if (mDisplayAdapter.getDataList().isEmpty() && !mPlayingSongEntityList.isEmpty()) {
            mDisplayAdapter.setDataList(mPlayingSongEntityList);
            mDisplayAdapter.notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (mDisplayAdapter == null) {
            mDisplayAdapter = new PlayListSecondDisplayAdapter(getActivity());
        }
        return mDisplayAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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
        display();
    }

    @Override
    public ArrayList<LocalSongEntity> getDisplayDataList() {
        return null;
    }

    @Override
    public void reInitialize() {

    }

    @Override
    public void setDisplayData(@NonNull List<LocalSongEntity> dataList) {
    }

    @Override
    public void addLoadMoreData(@NonNull List<LocalSongEntity> dataList) {

    }

    @Override
    public void handleItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {

    }

    public void setPlayingSongEntityList(List<LocalSongEntity> songEntityList) {
        this.mPlayingSongEntityList = songEntityList;
    }


}
