package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.adapter.holder.LocalSongViewHolder;
import com.scott.su.smusic.callback.LocalSongDisplayCallback;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.LocalSongDisplayContract;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongDisplayPresenterImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayFragment extends BaseDisplayFragment<LocalSongDisplayContract.LocalSongDisplayView,LocalSongDisplayContract.LocalSongBaseDisplayPresenter,LocalSongEntity, LocalSongViewHolder>
        implements LocalSongDisplayContract.LocalSongDisplayView {
    private LocalSongDisplayContract.LocalSongBaseDisplayPresenter mSongDisplayPresenter;
    private LocalSongDisplayAdapter mSongDisplayAdapter;

    private LocalSongDisplayCallback mDisplayCallback;


    public static LocalSongDisplayFragment newInstance() {
        LocalSongDisplayFragment instance = new LocalSongDisplayFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected LocalSongDisplayContract.LocalSongBaseDisplayPresenter getPresenter() {
        if (mSongDisplayPresenter == null) {
            mSongDisplayPresenter = new LocalSongDisplayPresenterImpl(this);
        }
        return mSongDisplayPresenter;
    }

    @Override
    protected void onFirstTimeCreateView() {
        mSongDisplayPresenter.onViewFirstTimeCreated();
    }


    @NonNull
    @Override
    protected BaseDisplayAdapter<LocalSongViewHolder, LocalSongEntity> getAdapter() {
        if (mSongDisplayAdapter == null) {
            mSongDisplayAdapter = new LocalSongDisplayAdapter(getActivity()) {
                @Override
                public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                    if (mDisplayCallback != null) {
                        mDisplayCallback.onItemMoreClick(view, position, entity);
                    }
                }
            };

            mSongDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalSongEntity>() {
                @Override
                public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                    mSongDisplayPresenter.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
                }
            });
        }
        return mSongDisplayAdapter;
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
        return R.layout.display_empty_local_song;
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
        mSongDisplayPresenter.onSwipeRefresh();
    }

    @Override
    protected void onLoadMore() {
        mSongDisplayPresenter.onLoadMore();
    }

    @Override
    protected void onEmptyClick() {

    }

    @Override
    protected void onErrorClick() {

    }

    @Override
    public ArrayList<LocalSongEntity> getDisplayDataList() {
        return (ArrayList<LocalSongEntity>) mSongDisplayAdapter.getDataList();
    }

    @Override
    public void reInitialize() {
        mSongDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void setDisplayData(@NonNull List<LocalSongEntity> dataList) {
        mSongDisplayAdapter.setDataList(dataList);
        mSongDisplayAdapter.notifyDataSetChanged();
        if (mDisplayCallback != null) {
            mDisplayCallback.onDisplayDataChanged(mSongDisplayAdapter.getDataList());
        }
    }

    @Override
    public void addLoadMoreData(@NonNull List<LocalSongEntity> dataList) {
        mSongDisplayAdapter.addDataList(dataList);
        mSongDisplayAdapter.notifyDataSetChanged();
        if (mDisplayCallback != null) {
            mDisplayCallback.onDisplayDataChanged(mSongDisplayAdapter.getDataList());
        }
    }

    @Override
    public void handleItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        if (mDisplayCallback != null) {
            mDisplayCallback.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
        }
    }

    public void setDisplayCallback(LocalSongDisplayCallback callback) {
        this.mDisplayCallback = callback;
    }

}
