package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.adapter.PlayStatisticDisplayAdapter;
import com.scott.su.smusic.callback.PlayStatisticItemClickCallback;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.contract.PlayStatisticDisplayContract;
import com.scott.su.smusic.mvp.presenter.impl.PlayStatisticDisplayPresenterImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticDisplayFragment extends BaseDisplayFragment<PlayStatisticDisplayContract.PlayStatisticDisplayView, PlayStatisticDisplayContract.PlayStatisticBaseDisplayPresenter, PlayStatisticEntity, RecyclerView.ViewHolder>
        implements PlayStatisticDisplayContract.PlayStatisticDisplayView {
    private PlayStatisticDisplayContract.PlayStatisticBaseDisplayPresenter mDisplayPresenter;
    private PlayStatisticDisplayAdapter mDisplayAdapter;
    private PlayStatisticItemClickCallback mItemClickCallback;

    @NonNull
    @Override
    protected BaseDisplayAdapter<RecyclerView.ViewHolder, PlayStatisticEntity> getAdapter() {
        if (mDisplayAdapter == null) {
            mDisplayAdapter = new PlayStatisticDisplayAdapter(getActivity());
            mDisplayAdapter.setItemClickCallback(new ItemClickCallback<PlayStatisticEntity>() {
                @Override
                public void onItemClick(View itemView, PlayStatisticEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                    mDisplayPresenter.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
                }
            });
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
        return true;
    }

    @Override
    protected boolean canLoadMore() {
        return false;
    }

    @Override
    protected void onSwipeRefresh() {
        mDisplayPresenter.onSwipRefresh();
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
    protected PlayStatisticDisplayContract.PlayStatisticBaseDisplayPresenter getPresenter() {
        if (mDisplayPresenter == null) {
            mDisplayPresenter = new PlayStatisticDisplayPresenterImpl(this);
        }
        return mDisplayPresenter;
    }

    @Override
    protected void onFirstTimeCreateView() {
        mDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void reInitialize() {
        mDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void handleItemClick(View itemView, PlayStatisticEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        if (mItemClickCallback != null) {
            if (getDisplayDataList().size() > 3 && position < 3) {
                mItemClickCallback.onPlayStatisticItemClick(position, entity, getDisplayDataList(), sharedElements[0], transitionNames[0]);
            } else {
                mItemClickCallback.onPlayStatisticItemClick(position, entity, getDisplayDataList(), null, null);
            }
        }
    }

    @Override
    public void onDestroy() {
        mDisplayPresenter.onViewWillDestroy();
        super.onDestroy();
    }

    public void setItemClickCallback(PlayStatisticItemClickCallback mItemClickCallback) {
        this.mItemClickCallback = mItemClickCallback;
    }


}
