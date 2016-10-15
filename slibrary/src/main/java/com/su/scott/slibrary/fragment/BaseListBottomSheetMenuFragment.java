package com.su.scott.slibrary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.su.scott.slibrary.R;
import com.su.scott.slibrary.adapter.ListBottomSheetMenuAdapter;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.entity.BottomSheetMenuItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public abstract class BaseListBottomSheetMenuFragment extends BottomSheetDialogFragment {
    private View mRootView;
    private TextView mTitleTextView;
    private RecyclerView mMenuRecyclerView;
    private ListBottomSheetMenuAdapter mMenuAdapter;

    protected abstract String getTitle();

    protected abstract
    @NonNull
    int[] getMenuItemIcons();

    protected abstract
    @NonNull
    String[] getMenuItemNames();

    protected abstract void onMenuItemClick(int position, String itemName);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_base_list_bottom_sheet, container, false);
        mTitleTextView = (TextView) mRootView.findViewById(R.id.tv_title_fragment_base_list_bottom_sheet);
        mMenuRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view_menu_fragment_base_list_bottom_sheet);

        mTitleTextView.setText(getTitle());

        mMenuAdapter = new ListBottomSheetMenuAdapter(getActivity());
        List<BottomSheetMenuItemEntity> menuItemEntities = new ArrayList<>();
        for (int i = 0; i < getMenuItemIcons().length; i++) {
            BottomSheetMenuItemEntity menuItemEntity = new BottomSheetMenuItemEntity(getMenuItemIcons()[i], getMenuItemNames()[i]);
            menuItemEntities.add(menuItemEntity);
        }
        mMenuAdapter.setDataList(menuItemEntities);
        mMenuAdapter.setItemClickCallback(new ItemClickCallback<BottomSheetMenuItemEntity>() {
            @Override
            public void onItemClick(View itemView, BottomSheetMenuItemEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                onMenuItemClick(position, entity.getItemName());
            }
        });

        mMenuRecyclerView.setAdapter(mMenuAdapter);
        mMenuRecyclerView.setHasFixedSize(true);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return mRootView;
    }
}
