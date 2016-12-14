package com.scott.su.smusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.PlayStatisticPagerAdapter;
import com.scott.su.smusic.callback.PlayStatisticItemClickCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.scott.su.smusic.mvp.contract.PlayStatisticContract;
import com.scott.su.smusic.mvp.presenter.impl.PlayStatisticPresenterImpl;
import com.scott.su.smusic.ui.fragment.PlayStatisticDisplayFragment;
import com.scott.su.smusic.ui.fragment.PlayStatisticWeekFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.SdkUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayStatisticActivity extends BaseActivity implements PlayStatisticContract.PlayStatisticView {
    private PlayStatisticContract.PlayStatisticPresenter mPlayStatisticPresenter;
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

            mPlayStatisticDisplayFragment.setItemClickCallback(new PlayStatisticItemClickCallback() {
                @Override
                public void onPlayStatisticItemClick(int position, PlayStatisticEntity entity, ArrayList<PlayStatisticEntity> statisticEntityList, View sharedElement, String transitionName) {
                    mPlayStatisticPresenter.onPlayStatisticItemClick(position, entity, statisticEntityList, sharedElement, transitionName);
                }
            });
        }
        return mPlayStatisticDisplayFragment;
    }

    @Override
    public void goToMusicPlay(LocalSongEntity songEntity, ArrayList<LocalSongEntity> songEntityList) {
        Intent intent = new Intent(PlayStatisticActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, songEntityList);
        goToWithTransition(intent);
    }

    @Override
    public void goToMusicPlayWithCover(LocalSongEntity songEntity, ArrayList<LocalSongEntity> songEntityList, View sharedElement, String transitionName) {
        Intent intent = new Intent(PlayStatisticActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, songEntityList);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }


}
