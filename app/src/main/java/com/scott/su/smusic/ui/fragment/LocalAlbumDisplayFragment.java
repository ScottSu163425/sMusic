package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalAlbumDisplayAdapter;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.mvp.presenter.LocalAlbumDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalAlbumDisplayPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalAlbumDisplayView;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;
import com.su.scott.slibrary.util.T;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalAlbumDisplayFragment extends BaseDisplayFragment<LocalAlbumEntity> implements LocalAlbumDisplayView {
    private LocalAlbumDisplayPresenter mLocalAlbumDisplayPresenter;
    private LocalAlbumDisplayAdapter mLocalAlbumDisplayAdapter;

    private static final String KEY_DISPLAY_TYPE = "KEY_DISPLAY_TYPE";

    public static LocalAlbumDisplayFragment newInstance() {
        LocalAlbumDisplayFragment instance = new LocalAlbumDisplayFragment();

        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mLocalAlbumDisplayPresenter.onViewWillDestroy();
        super.onDestroy();
    }

    @Override
    protected void onFirstTimeCreateView() {
        mLocalAlbumDisplayPresenter = new LocalAlbumDisplayPresenterImpl(this);
        mLocalAlbumDisplayPresenter.onViewFirstTimeCreated();

        this.setSwipeRefreshEnable(true);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mLocalAlbumDisplayAdapter = new LocalAlbumDisplayAdapter(getActivity());

        mLocalAlbumDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalAlbumEntity>() {
            @Override
            public void onItemClick(View itemView, LocalAlbumEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mLocalAlbumDisplayPresenter.onItemClick(itemView, entity, position, sharedElements, transitionNames, data);
            }
        });

        return mLocalAlbumDisplayAdapter;
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
        return R.layout.display_empty_local_song;
    }

    @Override
    protected int getErrorLayout() {
        return R.layout.display_error_default;
    }

    @Override
    protected void onSwipeRefresh() {
        mLocalAlbumDisplayPresenter.onSwipRefresh();
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
    public void reinitialize() {
        mLocalAlbumDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void setDisplayData(@NonNull List<LocalAlbumEntity> dataList) {
        mLocalAlbumDisplayAdapter.setDataList(dataList);
        mLocalAlbumDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreData(@NonNull List<LocalAlbumEntity> dataList) {
    }

    @Override
    public void handleItemClick(View itemView, LocalAlbumEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        T.showShort(getActivity(), entity.getAlbumTitle());
    }


}
