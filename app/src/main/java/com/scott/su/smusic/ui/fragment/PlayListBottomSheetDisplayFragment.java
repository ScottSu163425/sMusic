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
import com.scott.su.smusic.adapter.PlayListDisplayAdapter;
import com.scott.su.smusic.callback.PlayListBottomSheetCallback;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.util.MusicPlayUtil;
import com.su.scott.slibrary.callback.ItemClickCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/10/23.
 */

public class PlayListBottomSheetDisplayFragment extends BottomSheetDialogFragment {
    private View mRootView;
    private RecyclerView mPlayListSongRecyclerView;
    private PlayListDisplayAdapter mDisplayAdapter;
    private List<LocalSongEntity> mPlayListSongs = new ArrayList<>();
    private boolean mDataListChanged;
    private View mClearBtn;
    private PlayListBottomSheetCallback mItemCallback;
    private int mCurrentPlayPosition = -1;


    public static PlayListBottomSheetDisplayFragment newInstance() {
        PlayListBottomSheetDisplayFragment instance = new PlayListBottomSheetDisplayFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_bottom_sheet_play_list_display, container, false);
            mPlayListSongRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view_fragment_play_list_bottom_sheet);
            mPlayListSongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mDisplayAdapter = new PlayListDisplayAdapter(getActivity()) {
                @Override
                public void onItemRemoveClick(View view, int position, LocalSongEntity entity) {
                    if (mItemCallback != null) {
                        mItemCallback.onPlayListItemRemoveClick(view, position, entity);
                    }
                }
            };
            mDisplayAdapter.setItemClickCallback(new ItemClickCallback<LocalSongEntity>() {
                @Override
                public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                    if (mItemCallback != null) {
                        mItemCallback.onPlayListItemClick(itemView, entity, position);
                    }
                }
            });
            mDisplayAdapter.setDataList(mPlayListSongs);
            mPlayListSongRecyclerView.setAdapter(mDisplayAdapter);
            mClearBtn = mRootView.findViewById(R.id.tv_clear_fragment_play_list_bottom_sheet);

            mClearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemCallback != null) {
                        mItemCallback.onPlayListClearClick(v);
                    }
                }
            });
        }
        return mRootView;
    }

    public PlayListBottomSheetDisplayFragment setDataList(List<LocalSongEntity> playListSongs, LocalSongEntity currentSong) {
        mPlayListSongs = playListSongs;
        mDataListChanged = true;
        mCurrentPlayPosition = MusicPlayUtil.getSongPosition(currentSong, playListSongs);
        return this;
    }

    public void updatePlayList(List<LocalSongEntity> playListSongs, LocalSongEntity currentSong) {
        mPlayListSongs = playListSongs;
        mCurrentPlayPosition = MusicPlayUtil.getSongPosition(currentSong, playListSongs);
        mDisplayAdapter.setSelectedPosition(mCurrentPlayPosition, true);
        mDisplayAdapter.notifyDataSetChanged();
        scrollToCurrentPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataListChanged) {
            mDisplayAdapter.setSelectedPosition(mCurrentPlayPosition,true);
            mDisplayAdapter.notifyDataSetChanged();
            scrollToCurrentPosition();
            mDataListChanged = false;
        }
    }

    private void scrollToCurrentPosition() {
        if (mCurrentPlayPosition != -1) {
            mPlayListSongRecyclerView.smoothScrollToPosition(mCurrentPlayPosition);
        }
    }

    public PlayListBottomSheetDisplayFragment setItemCallback(PlayListBottomSheetCallback itemCallback) {
        this.mItemCallback = itemCallback;
        return this;
    }


}
