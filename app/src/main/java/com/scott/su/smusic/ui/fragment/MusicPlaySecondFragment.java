package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.fragment.BaseFragment;

/**
 * Created by asus on 2016/11/20.
 */

public class MusicPlaySecondFragment extends BaseFragment {
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_music_play_second, container, false);

        return mRootView;
    }

    @Override
    protected void onFirstTimeCreateView() {

    }

    @Override
    public View getSnackbarParent() {
        return null;
    }

    @Override
    public void initPreData() {

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
}
