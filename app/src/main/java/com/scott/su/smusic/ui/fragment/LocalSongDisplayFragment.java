package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongDisplayPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalSongDisplayView;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;
import com.su.scott.slibrary.util.L;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayFragment extends BaseDisplayFragment<LocalSongEntity> implements LocalSongDisplayView {
    private LocalSongDisplayPresenter mSongDisplayPresenter;
    private LocalSongDisplayAdapter mSongDisplayAdapter;
    private LocalSongDisplayAdapter.DISPLAY_TYPE mDisplayType = LocalSongDisplayAdapter.DISPLAY_TYPE.NumberDivider;
    private LocalSongBillEntity mSongsBillEntity;
    private LocalSongDisplayCallback mDisplayCallback;

    private static final String KEY_DISPLAY_BILL_ENTITY = "KEY_DISPLAY_BILL_ENTITY";
    private static final String KEY_DISPLAY_TYPE = "KEY_DISPLAY_TYPE";

    /**
     * Get the instance of LocalSongDisplayFragment.
     *
     * @param songsBillEntity The bill that contain all displaying songs;
     * @param displayType
     * @return
     */
    public static LocalSongDisplayFragment newInstance(@Nullable LocalSongBillEntity songsBillEntity, LocalSongDisplayAdapter.DISPLAY_TYPE displayType) {
        LocalSongDisplayFragment instance = new LocalSongDisplayFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_DISPLAY_TYPE, displayType);
        arguments.putParcelable(KEY_DISPLAY_BILL_ENTITY, songsBillEntity);
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSongsBillEntity = (LocalSongBillEntity) getArguments().get(KEY_DISPLAY_BILL_ENTITY);
        this.mDisplayType = (LocalSongDisplayAdapter.DISPLAY_TYPE) getArguments().get(KEY_DISPLAY_TYPE);
    }

    @Override
    public void onDestroy() {
        mSongDisplayPresenter.onViewWillDestroy();
        super.onDestroy();
    }

    @Override
    protected void onFirstTimeCreateView() {
        mSongDisplayPresenter = new LocalSongDisplayPresenterImpl(this);
        mSongDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mSongDisplayAdapter = new LocalSongDisplayAdapter(getActivity(), mDisplayType) {
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

        return mSongDisplayAdapter;
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
        mSongDisplayPresenter.onSwipRefresh();
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
        mSongDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void setDisplayData(@NonNull List<LocalSongEntity> dataList) {
        mSongDisplayAdapter.setDataList(dataList);
        mSongDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadMoreData(@NonNull List<LocalSongEntity> dataList) {
    }

    @Override
    public void handleItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
        if (mDisplayCallback != null) {
            mDisplayCallback.onItemClick(itemView, position, entity);
        }
    }

    @Override
    public LocalSongBillEntity getSongBillEntity() {
        return mSongsBillEntity;
    }

    public void setSongBillEntity(LocalSongBillEntity billEntity) {
        this.mSongsBillEntity = billEntity;
    }

    public void setDisplayType(LocalSongDisplayAdapter.DISPLAY_TYPE mDisplayType) {
        this.mDisplayType = mDisplayType;
    }

    public void setDisplayCallback(LocalSongDisplayCallback callback) {
        this.mDisplayCallback = callback;
    }

    public interface LocalSongDisplayCallback {
        void onItemClick(View view, int position, LocalSongEntity entity);

        void onItemMoreClick(View view, int position, LocalSongEntity entity);
    }

}
