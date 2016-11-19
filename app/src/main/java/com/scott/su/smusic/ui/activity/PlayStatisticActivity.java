package com.scott.su.smusic.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.PlayStatisticPagerAdapter;
import com.scott.su.smusic.mvp.presenter.PlayStatisticPresenter;
import com.scott.su.smusic.mvp.presenter.impl.PlayStatisticPresenterImpl;
import com.scott.su.smusic.mvp.view.PlayStatisticView;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.scott.su.smusic.ui.fragment.PlayStatisticDisplayFragment;
import com.scott.su.smusic.ui.fragment.PlayStatisticWeekFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.SdkUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayStatisticActivity extends BaseActivity implements PlayStatisticView {
    private PlayStatisticPresenter mPlayStatisticPresenter;
    private PlayStatisticWeekFragment mPlayStatisticWeekFragment;
    private PlayStatisticDisplayFragment mPlayStatisticDisplayFragment;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_statistic);

        mPlayStatisticPresenter = new PlayStatisticPresenterImpl(this);
        mPlayStatisticPresenter.onViewFirstTimeCreated();
    }

    @Override
    protected void onDestroy() {
        mPlayStatisticPresenter.onViewWillDestroy();
        super.onDestroy();
    }

    @Override
    public View getSnackbarParent() {
        return null;
    }

    @Override
    public void initPreData() {
        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_right));
        }
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_play_statistic);
        toolbar.setTitle(getString(R.string.statistic));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayStatisticActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager_play_statistic);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_play_statistic);
    }

    @Override
    public void initData() {
        List<Fragment> pageFragments = new ArrayList<>();
        pageFragments.add(getPlayStatisticWeekFragment());
        pageFragments.add(getPlayStatisticDisplayFragment());

        mViewPager.setAdapter(new PlayStatisticPagerAdapter(getSupportFragmentManager(),
                pageFragments,
                getResources().getStringArray(R.array.titles_tab_play_statistic)));
        mViewPager.setOffscreenPageLimit(pageFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void initListener() {

    }

    private PlayStatisticWeekFragment getPlayStatisticWeekFragment() {
        if (mPlayStatisticWeekFragment == null) {
            mPlayStatisticWeekFragment = new PlayStatisticWeekFragment();
        }
        return mPlayStatisticWeekFragment;
    }

    public PlayStatisticDisplayFragment getPlayStatisticDisplayFragment() {
        if (mPlayStatisticDisplayFragment == null) {
            mPlayStatisticDisplayFragment = new PlayStatisticDisplayFragment();
        }
        return mPlayStatisticDisplayFragment;
    }
}
