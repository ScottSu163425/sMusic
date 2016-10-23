package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.constant.LocalSongDisplayStyle;
import com.scott.su.smusic.constant.LocalSongDisplayType;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.util.T;

import java.util.List;

/**
 * Created by asus on 2016/10/23.
 */

public class PlayListBottomSheetDisplayFragment extends BottomSheetDialogFragment {
    private View mRootView;
    private LocalSongDisplayFragment mSongDisplayFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_bottom_sheet_play_list_display, container, false);
        mSongDisplayFragment = LocalSongDisplayFragment.newInstance(LocalSongDisplayType.Normal, null,
                LocalSongDisplayStyle.OnlyNumber);
        mSongDisplayFragment.setDisplayCallback(new LocalSongDisplayFragment.LocalSongDisplayCallback() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                T.showShort(getActivity(), "onItemClick:" + entity.getTitle());
            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                T.showShort(getActivity(), "onItemMoreClick:" + entity.getTitle());
            }

            @Override
            public void onDataLoading() {
            }

            @Override
            public void onDisplayDataChanged(List<LocalSongEntity> dataList) {

            }
        });

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_display_play_list_bottom_sheet, mSongDisplayFragment)
                .commit();

        return mRootView;
    }


}
