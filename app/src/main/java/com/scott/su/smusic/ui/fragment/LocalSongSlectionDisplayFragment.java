package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.adapter.LocalSongSelectionDisplayAdapter;
import com.scott.su.smusic.adapter.holder.LocalSongSelectionViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.LocalSongSelectionDisplayContract;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongSelectionDisplayPresenterImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

import java.util.ArrayList;

/**
 * Created by asus on 2016/8/27.
 */
public class LocalSongSlectionDisplayFragment extends BaseDisplayFragment<LocalSongSelectionDisplayContract.LocalSongSelectionDisplayView ,LocalSongSelectionDisplayContract.LocalSongSelectionBaseDisplayPresenter ,LocalSongEntity, LocalSongSelectionViewHolder>
        implements LocalSongSelectionDisplayContract.LocalSongSelectionDisplayView {
    private LocalSongSelectionDisplayContract.LocalSongSelectionBaseDisplayPresenter mSongSelectionDisplayPresenter;
    private LocalSongSelectionDisplayAdapter mSongSelectionDisplayAdapter;
    private OnSelectedSongChangedListener mOnSelectedSongChangedListener;


    @Override
    protected LocalSongSelectionDisplayContract.LocalSongSelectionBaseDisplayPresenter getPresenter() {
        if (mSongSelectionDisplayPresenter == null) {
            mSongSelectionDisplayPresenter = new LocalSongSelectionDisplayPresenterImpl(this);
        }
        return mSongSelectionDisplayPresenter;
    }

    @Override
    protected void onFirstTimeCreateView() {
        mSongSelectionDisplayPresenter.onViewFirstTimeCreated();
    }

    @NonNull
    @Override
    protected BaseDisplayAdapter<LocalSongSelectionViewHolder, LocalSongEntity> getAdapter() {
        if (mSongSelectionDisplayAdapter == null) {
            mSongSelectionDisplayAdapter = new LocalSongSelectionDisplayAdapter(getActivity());
            mSongSelectionDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalSongEntity>() {
                @Override
                public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                    if (mOnSelectedSongChangedListener != null) {
                        mOnSelectedSongChangedListener.onSelectedCountChanged(mSongSelectionDisplayAdapter.getSelectedSongsCount() == 0);
                    }
                }
            });
        }
        return mSongSelectionDisplayAdapter;
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
    public void reInitialize() {

    }

    @Override
    public void handleItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {

    }


    @Override
    public void selectAll() {
        mSongSelectionDisplayAdapter.selectAll();
        if (mOnSelectedSongChangedListener != null) {
            mOnSelectedSongChangedListener.onSelectedCountChanged(mSongSelectionDisplayAdapter.getSelectedSongsCount() == 0);
        }
    }

    @Override
    public ArrayList<LocalSongEntity> getSelectedSongs() {
        return (ArrayList<LocalSongEntity>) mSongSelectionDisplayAdapter.getSelectedSongs();
    }

    public void setOnSelectedSongChangedListener(OnSelectedSongChangedListener onSelectedSongChangedListener) {
        this.mOnSelectedSongChangedListener = onSelectedSongChangedListener;
    }

    public interface OnSelectedSongChangedListener {
        void onSelectedCountChanged(boolean isEmpty);
    }
}
