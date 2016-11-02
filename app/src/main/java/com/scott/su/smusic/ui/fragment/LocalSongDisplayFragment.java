package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.adapter.holder.LocalSongViewHolder;
import com.scott.su.smusic.constant.LocalSongDisplayStyle;
import com.scott.su.smusic.constant.LocalSongDisplayType;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongDisplayPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongDisplayPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalSongDisplayView;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.fragment.BaseDisplayFragment;
import com.su.scott.slibrary.util.L;
import com.su.scott.slibrary.util.T;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayFragment extends BaseDisplayFragment<LocalSongEntity, LocalSongViewHolder> implements LocalSongDisplayView {
    private LocalSongDisplayPresenter mSongDisplayPresenter;
    private LocalSongDisplayAdapter mSongDisplayAdapter;
    private LocalSongDisplayStyle mLocalSongDisplayStyle = LocalSongDisplayStyle.NumberDivider;
    private LocalSongDisplayType mDisplayType = LocalSongDisplayType.Normal;
    private LocalBillEntity mSongsBillEntity;
    private LocalAlbumEntity mSongsAlbumEntity;
    private LocalSongDisplayCallback mDisplayCallback;

    private static final String KEY_DISPLAY_TYPE_ENTITY = "KEY_DISPLAY_TYPE_ENTITY ";
    private static final String KEY_DISPLAY_TYPE = "KEY_DISPLAY_TYPE";
    private static final String KEY_DISPLAY_STYLE = "KEY_DISPLAY_STYLE";


    public static LocalSongDisplayFragment newInstance(LocalSongDisplayType displayType, @Nullable Object entity, LocalSongDisplayStyle localSongDisplayStyle) {
        LocalSongDisplayFragment instance = new LocalSongDisplayFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_DISPLAY_TYPE, displayType);
        arguments.putParcelable(KEY_DISPLAY_TYPE_ENTITY, (Parcelable) entity);
        arguments.putSerializable(KEY_DISPLAY_STYLE, localSongDisplayStyle);
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDisplayType = (LocalSongDisplayType) getArguments().get(KEY_DISPLAY_TYPE);
        this.mLocalSongDisplayStyle = (LocalSongDisplayStyle) getArguments().get(KEY_DISPLAY_STYLE);
        if (mDisplayType == LocalSongDisplayType.Bill) {
            this.mSongsBillEntity = (LocalBillEntity) getArguments().get(KEY_DISPLAY_TYPE_ENTITY);
        } else if (mDisplayType == LocalSongDisplayType.Album) {
            this.mSongsAlbumEntity = (LocalAlbumEntity) getArguments().get(KEY_DISPLAY_TYPE_ENTITY);
        }
    }

    @Override
    protected void onFirstTimeCreateView() {
        mSongDisplayPresenter = new LocalSongDisplayPresenterImpl(this);
        mSongDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mSongDisplayAdapter = new LocalSongDisplayAdapter(getActivity(), mLocalSongDisplayStyle) {
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
        return true;
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
    public ArrayList<LocalSongEntity> getDisplayDataList() {
        return (ArrayList<LocalSongEntity>) mSongDisplayAdapter.getDataList();
    }

    @Override
    public void reInitialize() {
        mSongDisplayPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void setDisplayData(@NonNull List<LocalSongEntity> dataList) {
        L.e("===>etDisplayData");
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
    public LocalAlbumEntity getSongAlbumEntity() {
        return mSongsAlbumEntity;
    }

    @Override
    public boolean isDisplayForNormal() {
        return mDisplayType == LocalSongDisplayType.Normal;
    }

    @Override
    public boolean isDisplayForBill() {
        return mDisplayType == LocalSongDisplayType.Bill;
    }

    @Override
    public boolean isDisplayForAlbum() {
        return mDisplayType == LocalSongDisplayType.Album;
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

    public void setDisplayStyle(LocalSongDisplayStyle mLocalSongDisplayStyle) {
        this.mLocalSongDisplayStyle = mLocalSongDisplayStyle;
    }

    public void setDisplayCallback(LocalSongDisplayCallback callback) {
        this.mDisplayCallback = callback;
    }

    public interface LocalSongDisplayCallback {
        void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

        void onItemMoreClick(View view, int position, LocalSongEntity entity);

        void onDataLoading();

        void onDisplayDataChanged(List<LocalSongEntity> dataList);
    }
}
