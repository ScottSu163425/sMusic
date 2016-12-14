package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.BillSongDisplayAdapter;
import com.scott.su.smusic.adapter.holder.BillSongViewHolder;
import com.scott.su.smusic.callback.LocalSongDisplayCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.BillSongDisplayContract;
import com.scott.su.smusic.mvp.presenter.impl.BillSongDisplayPresenterImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

import java.util.List;

/**
 * Created by asus on 2016/11/05.
 */
public class BillSongDisplayFragment extends BaseDisplayFragment<LocalSongEntity, BillSongViewHolder> implements BillSongDisplayContract.BillSongDisplayView {
    private BillSongDisplayContract.BillSongDisplayPresenter mSongDisplayPresenter;
    private BillSongDisplayAdapter mSongDisplayAdapter;

    private LocalBillEntity mSongsBillEntity;
    private LocalSongDisplayCallback mDisplayCallback;


    public static BillSongDisplayFragment newInstance(@NonNull LocalBillEntity entity) {
        BillSongDisplayFragment instance = new BillSongDisplayFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.KEY_EXTRA_BILL, entity);
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSongsBillEntity = (LocalBillEntity) getArguments().get(Constants.KEY_EXTRA_BILL);
    }

    @Override
    protected void onFirstTimeCreateView() {
        mSongDisplayPresenter = new BillSongDisplayPresenterImpl(this);
        mSongDisplayPresenter.onViewFirstTimeCreated();
    }

    @NonNull
    @Override
    protected BaseDisplayAdapter<BillSongViewHolder, LocalSongEntity> getAdapter() {
        if (mSongDisplayAdapter == null) {
            mSongDisplayAdapter = new BillSongDisplayAdapter(getActivity()) {
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
        return false;
    }

    @Override
    protected boolean canLoadMore() {
        return false;
    }

    @Override
    protected void onSwipeRefresh() {
        mSongDisplayPresenter.onSwipRefresh();
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
        mSongDisplayAdapter.getDataList().addAll(dataList);
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

    @Override
    public LocalBillEntity getSongBillEntity() {
        return mSongsBillEntity;
    }

    @Override
    public void setLoading() {
        if (mDisplayCallback != null) {
            mDisplayCallback.onDataLoading();
        }
    }

    public void setSongBillEntity(LocalBillEntity billEntity) {
        this.mSongsBillEntity = billEntity;
    }

    public void setDisplayCallback(LocalSongDisplayCallback callback) {
        this.mDisplayCallback = callback;
    }

}
