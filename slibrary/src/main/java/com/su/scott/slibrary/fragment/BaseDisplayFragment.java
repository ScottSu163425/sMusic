package com.su.scott.slibrary.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.su.scott.slibrary.R;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.ViewUtil;
import com.su.scott.slibrary.mvp.view.BaseDisplayView;

import java.util.ArrayList;
import java.util.List;

/**
 * @类名 BaseDisplayFragment
 * @描述 基于RecyclerView、SwipeRefreshLayout的列表展示封装基类
 * @作者 Su
 * @时间 2016年7月
 */
public abstract class BaseDisplayFragment<E, VH extends RecyclerView.ViewHolder> extends BaseFragment implements BaseDisplayView<E> {
    private static final int LAYOUT_ID_DISPLAY_DEFAULT_LOADING = R.layout.display_loading_default;
    private static final int LAYOUT_ID_DISPLAY_DEFAULT_EMPTY = R.layout.display_empty_default;
    private static final int LAYOUT_ID_DISPLAY_DEFAULT_ERROR = R.layout.display_error_default;
    private static final int LAYOUT_ID_DISPLAY_DEFAULT_FOOTER = R.layout.display_footer_default;

    private View mRootView;
    private RecyclerView mDisplayRecyclerView;
    private BaseDisplayAdapter<VH, E> mDisplayAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FrameLayout mLoadingLayout;
    private FrameLayout mEmptyLayout;
    private FrameLayout mErrorLayout;
    private FrameLayout mFooterLayout;

    protected abstract
    @NonNull
    BaseDisplayAdapter<VH, E> getAdapter();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract
    @LayoutRes
    int getLoadingLayoutRes();

    protected abstract
    @LayoutRes
    int getEmptyLayoutRes();

    protected abstract
    @LayoutRes
    int getErrorLayoutRes();

    protected abstract
    @LayoutRes
    int getFooterLayoutRes();

    protected abstract boolean canSwipeRefresh();

    protected abstract boolean canLoadMore();

    protected abstract void onSwipeRefresh();

    protected abstract void onLoadMore();

    protected abstract void onEmptyClick();

    protected abstract void onErrorClick();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_base_display, container, false);
            initDisplyView();
            initStateLayout(inflater, container, savedInstanceState);
            initSwipRefresh();
            initRecyclerView();
        }
        return mRootView;
    }

    private void initDisplyView() {
        mDisplayRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view_fragment_base_display);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout_fragment_base_display);
        mLoadingLayout = (FrameLayout) mRootView.findViewById(R.id.fl_container_loading_layout_fragment_base_display);
        mEmptyLayout = (FrameLayout) mRootView.findViewById(R.id.fl_container_empty_layout_fragment_base_display);
        mErrorLayout = (FrameLayout) mRootView.findViewById(R.id.fl_container_error_layout_fragment_base_display);
        mFooterLayout = (FrameLayout) mRootView.findViewById(R.id.fl_container_footer_layout_fragment_base_display);
    }

    private void initStateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set specific or default state layout.
        mLoadingLayout.addView(inflater.inflate(getLoadingLayoutRes() == 0 ? LAYOUT_ID_DISPLAY_DEFAULT_LOADING : getLoadingLayoutRes(), container, false),
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mEmptyLayout.addView(inflater.inflate(getEmptyLayoutRes() == 0 ? LAYOUT_ID_DISPLAY_DEFAULT_EMPTY : getEmptyLayoutRes(), container, false),
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mErrorLayout.addView(inflater.inflate(getErrorLayoutRes() == 0 ? LAYOUT_ID_DISPLAY_DEFAULT_ERROR : getErrorLayoutRes(), container, false),
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mFooterLayout.addView(inflater.inflate(getFooterLayoutRes() == 0 ? LAYOUT_ID_DISPLAY_DEFAULT_FOOTER : getFooterLayoutRes(), container, false),
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mEmptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEmptyClick();
            }
        });

        mErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onErrorClick();
            }
        });
    }

    private void initSwipRefresh() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_indigo_500, R.color.md_red_500, R.color.md_green_500, R.color.md_yellow_500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });

        mSwipeRefreshLayout.setEnabled(canSwipeRefresh());
    }

    private void initRecyclerView() {
        mDisplayAdapter = getAdapter();
        mDisplayRecyclerView.setHasFixedSize(true);
        mDisplayRecyclerView.setLayoutManager(getLayoutManager());
        mDisplayRecyclerView.setAdapter(mDisplayAdapter);

        //setup load more
        if (canLoadMore()) {
            mDisplayRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int firstVisibleItem = manager.findFirstVisibleItemPosition();
                        int lastVisibleItem = manager.findLastVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();

                        if (lastVisibleItem == (totalItemCount - 1)) {
//                            int itemHeight = getViewHolder(getFirstVisibleItemPosition()).itemView.getMeasuredHeight();
//                            int itemCount = mDisplayAdapter.getItemCount();
//                            int totalItemHeight = 0;
//                            int recyclerViewHeight = getRecyclerView().getMeasuredHeight();
//
//                            for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
//                                totalItemHeight += getViewHolder(i).itemView.getMeasuredHeight();
//                            }
//
//                            L.e("===>totalItemHeight",totalItemHeight+"");
//                            L.e("===>recyclerViewHeight",recyclerViewHeight+"");
////                            If the items could not fill a recycler view height,then disable load more even if user pull up the items;
//                            if (totalItemHeight < recyclerViewHeight) {
//                                return;
//                            }

                            if (mSwipeRefreshLayout.isRefreshing()) {
                                return;
                            }

                            if (ViewUtil.isViewVisiable(mFooterLayout)) {
                                return;
                            }

                            showLoadMoreFooter();
                            onLoadMore();
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }

    }

    @Override
    public void display() {
        stopSwipeRefresh();
        ViewUtil.setViewVisiable(mDisplayRecyclerView);
        ViewUtil.setViewGone(mLoadingLayout);
        ViewUtil.setViewGone(mEmptyLayout);
        ViewUtil.setViewGone(mErrorLayout);
        ViewUtil.setViewGone(mFooterLayout);
//        hideLoadMoreFooter();
    }

    @Override
    public void showLoading() {
        stopSwipeRefresh();
        ViewUtil.setViewVisiable(mLoadingLayout);
        ViewUtil.setViewGone(mDisplayRecyclerView);
        ViewUtil.setViewGone(mEmptyLayout);
        ViewUtil.setViewGone(mErrorLayout);
        ViewUtil.setViewGone(mFooterLayout);
//        hideLoadMoreFooter();
    }

    @Override
    public void showEmpty() {
        stopSwipeRefresh();
        ViewUtil.setViewVisiable(mEmptyLayout);
        ViewUtil.setViewGone(mDisplayRecyclerView);
        ViewUtil.setViewGone(mLoadingLayout);
        ViewUtil.setViewGone(mErrorLayout);
        ViewUtil.setViewGone(mFooterLayout);
//        hideLoadMoreFooter();
    }

    @Override
    public void showError() {
        stopSwipeRefresh();
        ViewUtil.setViewVisiable(mErrorLayout);
        ViewUtil.setViewGone(mDisplayRecyclerView);
        ViewUtil.setViewGone(mLoadingLayout);
        ViewUtil.setViewGone(mEmptyLayout);
        ViewUtil.setViewGone(mFooterLayout);
    }

    @Override
    public boolean checkNetworkSnack() {
        if (isNetworkConnected()) {
            return true;
        }
        stopSwipeRefresh();
        ViewUtil.setViewGone(mFooterLayout);
        showSnackbarShort(getString(R.string.network_error));
        return false;
    }

    private void showLoadMoreFooter() {
        if (!ViewUtil.isViewVisiable(mFooterLayout)) {
            AnimUtil.alphaIn(mFooterLayout);
        }
    }

//    private void hideLoadMoreFooter() {
//        if (!ViewUtil.isViewVisiable(mFooterLayout)) {
//            AnimUtil.alphaOut(mFooterLayout);
//        }
//    }

    @Override
    public void performSwipeRefresh() {
        if (!canSwipeRefresh()) {
            return;
        }
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopSwipeRefresh() {
        if (!canSwipeRefresh()) {
            return;
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        //解决Viewpager切换重复加载
        if (null != mRootView) {
            ((ViewGroup) mRootView).removeView(mRootView);
        }
        super.onDestroy();
    }


    @Override
    public void initPreData() {

    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    protected RecyclerView getDisplayRecyclerView() {
        return mDisplayRecyclerView;
    }

    private int getFirstVisibleItemPosition() {
        return ((LinearLayoutManager) (getDisplayRecyclerView().getLayoutManager())).findFirstVisibleItemPosition();
    }

    private int getLastVisibleItemPosition() {
        return ((LinearLayoutManager) (getDisplayRecyclerView().getLayoutManager())).findLastVisibleItemPosition();
    }

    public VH getViewHolder(int position) {
        int firstItemPosition = getFirstVisibleItemPosition();
        if (position - firstItemPosition >= 0) {
            //得到要更新的item的view
            View view = getDisplayRecyclerView().getChildAt(position - firstItemPosition /*+ 1*/);
            if (null != getDisplayRecyclerView().getChildViewHolder(view)) {
                VH viewHolder = (VH) getDisplayRecyclerView().getChildViewHolder(view);
                return viewHolder;
                //do something
            }
        }
        return null;
    }

    @Override
    public ArrayList<E> getDisplayDataList() {
        return (ArrayList<E>) mDisplayAdapter.getDataList();
    }

    @Override
    public void setDisplayData(@NonNull List<E> dataList) {
        mDisplayAdapter.setDataList(dataList);
        mDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void addLoadMoreData(@NonNull List<E> dataList) {
        mDisplayAdapter.addDataList(dataList);
        mDisplayAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int position) {
        if (position < 0
                || mDisplayAdapter.getItemCount() == 0
                || mDisplayAdapter.getItemCount() <= position) {
            return;
        }

        mDisplayAdapter.notifyItemChanged(position);
    }

    public void scrollToPosition(int positon) {
        if (mDisplayAdapter.getItemCount() != 0) {
            getDisplayRecyclerView().scrollToPosition(positon);
        }
    }

    public void smoothScrollToPosition(int positon) {
        if (mDisplayAdapter.getItemCount() != 0) {
            getDisplayRecyclerView().smoothScrollToPosition(positon);
        }
    }

    public RecyclerView getRecyclerView() {
        return getDisplayRecyclerView();
    }

    public void setSwipeRefreshEnable(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }


}
