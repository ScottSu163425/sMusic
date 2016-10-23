package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.constant.LocalSongDisplayStyle;
import com.scott.su.smusic.entity.LocalSongEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/10/23.
 */

public class PlayListBottomSheetDisplayFragment extends BottomSheetDialogFragment {
    private View mRootView;
    private RecyclerView mPlayListSongRecyclerView;
    private LocalSongDisplayAdapter mDisplayAdapter;
    private List<LocalSongEntity> mPlayListSongs = new ArrayList<>();
    private boolean mDataListChanged;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_bottom_sheet_play_list_display, container, false);
            mPlayListSongRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view_fragment_play_list_bottom_sheet);
            mPlayListSongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mDisplayAdapter = new LocalSongDisplayAdapter(getActivity(), LocalSongDisplayStyle.OnlyNumber) {
                @Override
                public void onItemMoreClick(View view, int position, LocalSongEntity entity) {

                }
            };
            mDisplayAdapter.setDataList(mPlayListSongs);
            mPlayListSongRecyclerView.setAdapter(mDisplayAdapter);
        }
        return mRootView;
    }

    public void setDataList(List<LocalSongEntity> dataList) {
        mPlayListSongs = dataList;
        mDataListChanged = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataListChanged){
            mDisplayAdapter.notifyDataSetChanged();
            mDataListChanged = false;
        }
    }


}
