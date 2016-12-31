package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.adapter.LocalBillDisplayAdapter;
import com.scott.su.smusic.adapter.holder.LocalBillViewHolder;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.mvp.contract.LocalBillDisplayContract;
import com.scott.su.smusic.mvp.presenter.impl.LocalBillDisplayPresenterImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalBillDisplayFragment extends BaseDisplayFragment<LocalBillDisplayContract.LocalBillDisplayView ,LocalBillDisplayContract.LocalBillBaseDisplayPresenter ,LocalBillEntity, LocalBillViewHolder>
        implements LocalBillDisplayContract.LocalBillDisplayView {
    private LocalBillDisplayContract.LocalBillBaseDisplayPresenter mSongBillDisplayPresenter;
    private LocalBillDisplayAdapter mSongBillDisplayAdapter;

    private BillItemClickCallback mBillItemClickCallback;

    public static LocalBillDisplayFragment newInstance() {
        LocalBillDisplayFragment instance = new LocalBillDisplayFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected LocalBillDisplayContract.LocalBillBaseDisplayPresenter getPresenter() {
        if (mSongBillDisplayPresenter == null) {
            mSongBillDisplayPresenter = new LocalBillDisplayPresenterImpl(this);
        }
        return mSongBillDisplayPresenter;
    }

    @Override
    protected void onFirstTimeCreateView() {
        mSongBillDisplayPresenter.onViewFirstTimeCreated();
    }

    @NonNull
    @Override
    protected BaseDisplayAdapter<LocalBillViewHolder, LocalBillEntity> getAdapter() {
        if (mSongBillDisplayAdapter == null) {
            mSongBillDisplayAdapter = new LocalBillDisplayAdapter(getActivity());
            mSongBillDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalBillEntity>() {
                @Override
                public void onItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                    mSongBillDisplayPresenter.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
                }
            });
        }
        return mSongBillDisplayAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
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
    public void reInitialize() {
        mSongBillDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void handleItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        if (mBillItemClickCallback != null) {
            mBillItemClickCallback.onBillItemClick(itemView, entity, position, sharedElements, transitionNames, data);
        }
    }

    public void setBillItemClickCallback(BillItemClickCallback mBillItemClickCallback) {
        this.mBillItemClickCallback = mBillItemClickCallback;
    }

    public interface BillItemClickCallback {
        void onBillItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);
    }


}
