package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongBillDisplayAdapter;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongBillDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongBillDisplayPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalSongBillDisplayView;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;
import com.su.scott.slibrary.util.T;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongBillDisplayFragment extends BaseDisplayFragment<LocalSongBillEntity> implements LocalSongBillDisplayView {
    private LocalSongBillDisplayPresenter mSongBillDisplayPresenter;
    private LocalSongBillDisplayAdapter mSongBillDisplayAdapter;

    private static final String KEY_DISPLAY_TYPE = "KEY_DISPLAY_TYPE";

    public static LocalSongBillDisplayFragment newInstance() {
        LocalSongBillDisplayFragment instance = new LocalSongBillDisplayFragment();

        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mSongBillDisplayPresenter.onViewWillDestroy();
        super.onDestroy();
    }

    @Override
    protected void onFirstTimeCreateView() {
        mSongBillDisplayPresenter = new LocalSongBillDisplayPresenterImpl(this);
        mSongBillDisplayPresenter.onViewFirstTimeCreated();

        this.setSwipeRefreshEnable(true);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mSongBillDisplayAdapter = new LocalSongBillDisplayAdapter(getActivity());

        mSongBillDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalSongBillEntity>() {
            @Override
            public void onItemClick(View itemView, LocalSongBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mSongBillDisplayPresenter.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
            }
        });

        return mSongBillDisplayAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected int getLoadingLayout() {
        return R.layout.display_loading_default;
    }

    @Override
    protected int getEmptyLayout() {
        return R.layout.display_empty_local_song;
    }

    @Override
    protected int getErrorLayout() {
        return R.layout.display_error_default;
    }

    @Override
    protected void onSwipeRefresh() {
        mSongBillDisplayPresenter.onSwipRefresh();
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
    public void setDisplayData(@NonNull List<LocalSongBillEntity> dataList) {
        mSongBillDisplayAdapter.setDataList(dataList);
        mSongBillDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreData(@NonNull List<LocalSongBillEntity> dataList) {
    }

    @Override
    public void handleItemClick(View itemView, LocalSongBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        T.showLong(getActivity(), entity.getBillSongs().toString());
    }


}
