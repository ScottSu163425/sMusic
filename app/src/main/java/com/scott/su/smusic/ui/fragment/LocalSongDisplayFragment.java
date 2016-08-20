package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongDisplayPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalSongDisplayView;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;
import com.su.scott.slibrary.util.T;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayFragment extends BaseDisplayFragment<LocalSongEntity> implements LocalSongDisplayView {
    private LocalSongDisplayPresenter mLocalSongDisplayPresenter;
    private LocalSongDisplayAdapter mLocalSongDisplayAdapter;
    private LocalSongDisplayAdapter.DISPLAY_TYPE mDisplayType = LocalSongDisplayAdapter.DISPLAY_TYPE.NumberDivider;

    private static final String KEY_DISPLAY_TYPE = "KEY_DISPLAY_TYPE";

    public static LocalSongDisplayFragment newInstance(LocalSongDisplayAdapter.DISPLAY_TYPE displayType) {
        LocalSongDisplayFragment instance = new LocalSongDisplayFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_DISPLAY_TYPE, displayType);
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDisplayType = (LocalSongDisplayAdapter.DISPLAY_TYPE) getArguments().get(KEY_DISPLAY_TYPE);
    }

    @Override
    public void onDestroy() {
        mLocalSongDisplayPresenter.onViewWillDestroy();
        super.onDestroy();
    }

    @Override
    protected void onFirstTimeCreateView() {
        mLocalSongDisplayPresenter = new LocalSongDisplayPresenterImpl(this);
        mLocalSongDisplayPresenter.onViewFirstTimeCreated();

        this.setSwipeRefreshEnable(true);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mLocalSongDisplayAdapter = new LocalSongDisplayAdapter(getActivity(), mDisplayType);

        mLocalSongDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalSongEntity>() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mLocalSongDisplayPresenter.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
            }
        });

        return mLocalSongDisplayAdapter;
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
        mLocalSongDisplayPresenter.onSwipRefresh();
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
    public void setDisplayData(@NonNull List<LocalSongEntity> dataList) {
        mLocalSongDisplayAdapter.setDataList(dataList);
        mLocalSongDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreData(@NonNull List<LocalSongEntity> dataList) {
    }

    @Override
    public void handleItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        T.showShort(getActivity(), entity.getTitle());
    }


}
