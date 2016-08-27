package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.adapter.MainPagerAdapter;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.mvp.presenter.MainPresenter;
import com.scott.su.smusic.mvp.presenter.impl.MainPresenterImpl;
import com.scott.su.smusic.mvp.view.MainView;
import com.scott.su.smusic.ui.fragment.CreateBillDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalAlbumDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBillDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.PermissionUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 2016-8-18
 */
public class MainActivity extends BaseActivity implements MainView {
    private MainPresenter mPresenter; //Presenter of mvp;
    private Toolbar mToolbar;   //Title bar;
    private DrawerLayout mDrawerLayout;     //Content drawer;
    private ViewPager mViewPager;   //Content ViewPager;
    private TabLayout mTabLayout;   //Tabs for ViewPager;
    private FloatingActionButton mFloatingActionButton; //FAB;
    private LocalSongDisplayFragment mSongDisplayFragment;
    private LocalSongBillDisplayFragment mBillDisplayFragment;
    private LocalAlbumDisplayFragment mAlbumDisplayFragment;
    private CreateBillDialogFragment mCreateBillDialogFragment;

    private static final int INDEX_PAGE_FAB = 1; //Index of page that fab will be shown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        setupToolbar();
        initView();
        initData();
        initListener();

        mPresenter = new MainPresenterImpl(this);
        mPresenter.onViewFirstTimeCreated();

        //debug
        startActivity(new Intent(MainActivity.this, LocalSongSelectionActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_main);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_main);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });
    }

    private void initData() {
        List<Fragment> pageFragments = new ArrayList<>();

        mSongDisplayFragment = LocalSongDisplayFragment.newInstance(LocalSongDisplayAdapter.DISPLAY_TYPE.CoverDivider);
        mBillDisplayFragment = LocalSongBillDisplayFragment.newInstance();
        mAlbumDisplayFragment = LocalAlbumDisplayFragment.newInstance();

        pageFragments.add(mSongDisplayFragment);
        pageFragments.add(mBillDisplayFragment);
        pageFragments.add(mAlbumDisplayFragment);

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),
                pageFragments,
                getResources().getStringArray(R.array.titles_tab_main)));
        mViewPager.setOffscreenPageLimit(pageFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    if (mViewPager.getCurrentItem() == INDEX_PAGE_FAB) {
                        showFab();
                    } else {
                        hideFab();
                    }

                }

            }

        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onFabClick();
            }
        });
    }

    @Override
    public void updateSongDisplay() {
        mSongDisplayFragment.reInitialize();
    }

    @Override
    public void updateBillDisplay() {
        mBillDisplayFragment.reInitialize();
    }

    @Override
    public void updateAlbumDisplay() {
        mAlbumDisplayFragment.reInitialize();
    }

    /**
     * Show fab with animation.
     */
    @Override
    public void showFab() {
        if (!ViewUtil.isViewVisiable(mFloatingActionButton)) {
            AnimUtil.scaleIn(mFloatingActionButton, AnimUtil.DEFAULT_DURATION,
                    new OvershootInterpolator(),
                    new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {
                            ViewUtil.setViewVisiable(mFloatingActionButton);
                        }

                        @Override
                        public void onAnimEnd() {

                        }
                    }
            );
        }
    }

    /**
     * Hide fab with animation.
     */
    @Override
    public void hideFab() {
        if (ViewUtil.isViewVisiable(mFloatingActionButton)) {
            AnimUtil.scaleOut(mFloatingActionButton, AnimUtil.DEFAULT_DURATION,
                    new AnticipateOvershootInterpolator(),
                    new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {
                        }

                        @Override
                        public void onAnimEnd() {
                            ViewUtil.setViewGone(mFloatingActionButton);
                        }
                    }
            );
        }
    }

    @Override
    public void showCreateBillDialog() {
//        if (mCreateBillDialogFragment == null) {
        mCreateBillDialogFragment = new CreateBillDialogFragment() {
            @Override
            public void onConfirmClick(String text) {
                mPresenter.onCreateBillConfirm(text);
            }
        };
//        }

        if (!mCreateBillDialogFragment.isVisible()) {
            mCreateBillDialogFragment.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void dismissCreateBillDialog() {
        if (mCreateBillDialogFragment != null && mCreateBillDialogFragment.isVisible()) {
            mCreateBillDialogFragment.dismiss();
        }
    }

    @Override
    public void showCreateBillUnsuccessfully(String msg) {
        showSnackbarShort(mToolbar, msg);
    }

    @Override
    public void showCreateBillSuccessfully(final LocalSongBillEntity billEntity) {
        showSnackbarLong(mToolbar,
                getString(R.string.create_bill_successfully),
                getString(R.string.confirm_positive),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, LocalSongSelectionActivity.class));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        showSnackbarShort(mToolbar,
                getString(R.string.tips_exit_app),
                getString(R.string.confirm_positive),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }
}
