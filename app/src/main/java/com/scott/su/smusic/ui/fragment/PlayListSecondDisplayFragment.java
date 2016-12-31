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
import com.scott.su.smusic.mvp.contract.PlayListSecondDisplayContract;
import com.scott.su.smusic.mvp.presenter.impl.PlayListSecondDisplayPresenterImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

import java.util.List;

/**
 * Created by asus on 2016/12/3.
 */

public class PlayListSecondDisplayFragment extends BaseDisplayFragment<PlayListSecondDisplayContract.PlayListSecondDisplayView,PlayListSecondDisplayContract.PlayListSecondBaseDisplayPresenter,LocalSongEntity, PlayListSecondViewHolder>
        implements PlayListSecondDisplayContract.PlayListSecondDisplayView {
    private PlayListSecondDisplayContract.PlayListSecondBaseDisplayPresenter mDisplayPresenter;
    private PlayListSecondDisplayAdapter mDisplayAdapter;
    private List<LocalSongEntity> mPlayingSongEntityList;
    private int mCurrentPosition = -1;
    private ItemClickCallback<LocalSongEntity> mItemClickCallback;


    @Override
    public void onResume() {
        super.onResume();

        if (mDisplayAdapter.getDataList().isEmpty() && !mPlayingSongEntityList.isEmpty()) {
            mDisplayAdapter.setDataList(mPlayingSongEntityList);
            mDisplayAdapter.notifyDataSetChanged();
        }

        if (mDisplayAdapter.getSelectedPosition() != mCurrentPosition) {
            mDisplayAdapter.setSelectedPosition(mCurrentPosition, true);
            getRecyclerView().scrollToPosition(mCurrentPosition);
        }
    }

    @NonNull
    @Override
    protected BaseDisplayAdapter<PlayListSecondViewHolder, LocalSongEntity> getAdapter() {
        if (mDisplayAdapter == null) {
            mDisplayAdapter = new PlayListSecondDisplayAdapter(getActivity());
            mDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalSongEntity>() {
                @Override
                public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                    if (mItemClickCallback != null) {
                        mItemClickCallback.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
                    }
                }
            });
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
    protected PlayListSecondDisplayContract.PlayListSecondBaseDisplayPresenter getPresenter() {
        if (mDisplayPresenter == null) {
            mDisplayPresenter = new PlayListSecondDisplayPresenterImpl(this);
        }
        return mDisplayPresenter;
    }

    @Override
    protected void onFirstTimeCreateView() {
        display();
    }

    @Override
    public void reInitialize() {

    }

    @Override
    public void handleItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {

    }

    public void setPlayingSongEntityList(List<LocalSongEntity> songEntityList) {
        this.mPlayingSongEntityList = songEntityList;
    }

    public void setCurrentPosition(int positon) {
        mCurrentPosition = positon;

        if (isAdded() && isVisible()) {
            mDisplayAdapter.setSelectedPosition(mCurrentPosition, true);
            getRecyclerView().scrollToPosition(mCurrentPosition);
        }
    }

    public void setItemClickCallback(ItemClickCallback<LocalSongEntity> callback) {
        this.mItemClickCallback = callback;
    }
}
