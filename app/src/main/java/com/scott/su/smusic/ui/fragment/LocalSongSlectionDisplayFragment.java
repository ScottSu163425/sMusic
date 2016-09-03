package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongSelectionDisplayAdapter;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongSelectionDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongSelectionDisplayPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalSongSelectionDisplayView;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/27.
 */
public class LocalSongSlectionDisplayFragment extends BaseDisplayFragment<LocalSongEntity> implements LocalSongSelectionDisplayView {
    private LocalSongSelectionDisplayAdapter mSongSelectionDisplayAdapter;
    private LocalSongSelectionDisplayPresenter mSongSelectionDisplayPresenter;
    private OnSelectedSongChangedListener mOnSelectedSongChangedListener;


    @Override
    protected void onFirstTimeCreateView() {
        mSongSelectionDisplayPresenter = new LocalSongSelectionDisplayPresenterImpl(this);
        mSongSelectionDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mSongSelectionDisplayAdapter = new LocalSongSelectionDisplayAdapter(getActivity());
        mSongSelectionDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalSongEntity>() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                if (mOnSelectedSongChangedListener != null) {
                    mOnSelectedSongChangedListener.onSelectedCountChanged(mSongSelectionDisplayAdapter.getSelectedSongsCount() == 0);
                }
            }
        });

        return mSongSelectionDisplayAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected int getLoadingLayout() {
        return R.layout.display_loading_default;
    }

    @Override
    protected int getEmptyLayout() {
        return R.layout.display_empty_default;
    }

    @Override
    protected int getErrorLayout() {
        return R.layout.display_error_default;
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
    public void notifyItemChanged(int position) {

    }

    @Override
    public void setDisplayData(@NonNull List<LocalSongEntity> dataList) {
        mSongSelectionDisplayAdapter.setDataList(dataList);
        mSongSelectionDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreData(@NonNull List<LocalSongEntity> dataList) {

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
