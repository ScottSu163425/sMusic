package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.adapter.MainPagerAdapter;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.MainPresenter;
import com.scott.su.smusic.mvp.presenter.impl.MainPresenterImpl;
import com.scott.su.smusic.mvp.view.MainView;
import com.scott.su.smusic.ui.fragment.CreateBillDialogFragment;
import com.scott.su.smusic.ui.fragment.DrawerMenuFragment;
import com.scott.su.smusic.ui.fragment.LocalAlbumDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalBillDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetFragment;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.L;
import com.su.scott.slibrary.util.PermissionUtil;
import com.su.scott.slibrary.util.PopupMenuUtil;
import com.su.scott.slibrary.util.T;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 2016-8-18
 */
public class MainActivity extends BaseActivity implements MainView {
    private MainPresenter mMainPresenter; //Presenter of mvp;
    private Toolbar mToolbar;   //Title bar;
    private DrawerLayout mDrawerLayout;     //Content drawer;
    private ViewPager mViewPager;   //Content ViewPager;
    private TabLayout mTabLayout;   //Tabs for ViewPager;
    private FloatingActionButton mFloatingActionButton; //FAB;
    private DrawerMenuFragment mDrawerMenuFragment;
    private LocalSongDisplayFragment mSongDisplayFragment;
    private LocalBillDisplayFragment mBillDisplayFragment;
    private LocalAlbumDisplayFragment mAlbumDisplayFragment;
    private CreateBillDialogFragment mCreateBillDialogFragment;
    private boolean mDataInitFinish = false;

    private static final int INDEX_PAGE_FAB = 1; //Index of page that fab will be shown;
    private static final int REQUEST_CODE_LOCAL_SONG_SELECTION = 1111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainPresenter = new MainPresenterImpl(this);
        mMainPresenter.onViewFirstTimeCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.onViewResume();
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

    @Override
    public View getSnackbarParent() {
        return mToolbar;
    }

    @Override
    public void initPreData() {
        PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_main);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void initData() {
        List<Fragment> pageFragments = new ArrayList<>();

        mDrawerMenuFragment = new DrawerMenuFragment();
        mSongDisplayFragment = LocalSongDisplayFragment.newInstance(null,
                LocalSongDisplayAdapter.DISPLAY_TYPE.CoverDivider);
        mBillDisplayFragment = LocalBillDisplayFragment.newInstance();
        mAlbumDisplayFragment = LocalAlbumDisplayFragment.newInstance();
        mSongDisplayFragment.setSwipeRefreshEnable(true);
        mSongDisplayFragment.setLoadMoreEnable(false);

        pageFragments.add(mSongDisplayFragment);
        pageFragments.add(mBillDisplayFragment);
        pageFragments.add(mAlbumDisplayFragment);

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),
                pageFragments,
                getResources().getStringArray(R.array.titles_tab_main)));
        mViewPager.setOffscreenPageLimit(pageFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_drawer_menu_main, mDrawerMenuFragment).commit();
        mDataInitFinish = true;
    }

    @Override
    public void initListener() {
        mDrawerMenuFragment.setMenuCallback(new DrawerMenuFragment.DrawerMenuCallback() {
            @Override
            public void onStatisticsClick(View v) {
                T.showShort(getApplicationContext(), "统计");
            }

            @Override
            public void onNightModeOn() {
                T.showShort(getApplicationContext(), "开启夜间模式");
            }

            @Override
            public void onNightModeOff() {
                T.showShort(getApplicationContext(), "关闭夜间模式");
            }


            @Override
            public void onUpdateClick(View v) {
                T.showShort(getApplicationContext(), "版本更新");
            }

            @Override
            public void onAboutClick(View v) {
                T.showShort(getApplicationContext(), "关于");
            }
        });

        mSongDisplayFragment.setDisplayCallback(new LocalSongDisplayFragment.LocalSongDisplayCallback() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mMainPresenter.onLocalSongItemClick(itemView, entity, position, sharedElements, transitionNames, data);
            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                mMainPresenter.onLocalSongItemMoreClick(entity);
            }
        });

        mBillDisplayFragment.setBillItemClickCallback(new LocalBillDisplayFragment.BillItemClickCallback() {
            @Override
            public void onBillItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mMainPresenter.onBillItemClick(itemView, entity, position, sharedElements, transitionNames, data);
            }
        });

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
                mMainPresenter.onFabClick();
            }
        });
    }

    @Override
    public boolean isDataInitFinish() {
        return mDataInitFinish;
    }

    @Override
    public void showLocalSongBottomSheet(LocalSongEntity songEntity) {
        final LocalSongBottomSheetFragment songBottomSheetFragment = new LocalSongBottomSheetFragment();
        songBottomSheetFragment.setLocalSongEntity(songEntity);
        songBottomSheetFragment.setMenuClickCallback(new LocalSongBottomSheetFragment.MenuClickCallback() {
            @Override
            public void onAddToBillClick(LocalSongEntity songEntity) {
                mMainPresenter.onBottomSheetAddToBillClick(songEntity);
                songBottomSheetFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onAlbumClick(LocalSongEntity songEntity) {
                mMainPresenter.onBottomSheetAlbumClick(songEntity);
                songBottomSheetFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onShareClick(LocalSongEntity songEntity) {
                mMainPresenter.onBottomSheetShareClick(songEntity);
                songBottomSheetFragment.dismissAllowingStateLoss();
            }

            @Override
            public void onDeleteClick(LocalSongEntity songEntity) {
                mMainPresenter.onBottomSheetDeleteClick(songEntity);
                songBottomSheetFragment.dismissAllowingStateLoss();
            }
        });

        songBottomSheetFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void showBillSelectionDialog(final LocalSongEntity songToBeAdd) {
        final LocalBillSelectionDialogFragment billSelectionDialogFragment = new LocalBillSelectionDialogFragment();
        billSelectionDialogFragment.setCallback(new LocalBillSelectionDialogFragment.BillSelectionCallback() {
            @Override
            public void onBillSelected(LocalBillEntity billEntity) {
                mMainPresenter.onBottomSheetAddToBillConfirmed(billEntity, songToBeAdd);
                billSelectionDialogFragment.dismissAllowingStateLoss();
            }
        });
        billSelectionDialogFragment.show(getSupportFragmentManager(), "");
    }


    @Override
    public void updateSongDisplay() {
        if (mSongDisplayFragment.isVisible()) {
            mSongDisplayFragment.reInitialize();
        }
    }

    @Override
    public void updateBillDisplay() {
        if (mBillDisplayFragment.isVisible()) {
            mBillDisplayFragment.reInitialize();
        }
    }

    @Override
    public void updateAlbumDisplay() {
        if (mAlbumDisplayFragment.isVisible()) {
            mAlbumDisplayFragment.reInitialize();
        }
    }

    /**
     * Show fab with animation.
     */
//    @Override
    public void showFab() {
        if (!ViewUtil.isViewVisiable(mFloatingActionButton)) {
            AnimUtil.scaleIn(mFloatingActionButton, AnimUtil.DURATION_SHORT,
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
//    @Override
    public void hideFab() {
        if (ViewUtil.isViewVisiable(mFloatingActionButton)) {
            AnimUtil.scaleOut(mFloatingActionButton, AnimUtil.DURATION_SHORT,
                    new AccelerateDecelerateInterpolator(),
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
        mCreateBillDialogFragment = new CreateBillDialogFragment();
        mCreateBillDialogFragment.setCallback(new CreateBillDialogFragment.CreateBillDialogCallback() {
            @Override
            public void onConfirmClick(String text) {
                mMainPresenter.onCreateBillConfirm(text);
            }
        });
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
    public void showCreateBillSuccessfully(final LocalBillEntity billEntity) {
        showSnackbarLong(mToolbar,
                getString(R.string.success_create_bill),
                getString(R.string.confirm_positive),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, LocalSongSelectionActivity.class);
                        intent.setExtrasClassLoader(LocalBillEntity.class.getClassLoader());
                        intent.putExtra(Constants.KEY_EXTRA_BILL, billEntity);
                        goToForResult(intent, REQUEST_CODE_LOCAL_SONG_SELECTION);
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(MainActivity.this, LocalBillDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_BILL, entity);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void goToBillDetail(LocalBillEntity entity) {
        Intent intent = new Intent(MainActivity.this, LocalBillDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_BILL, entity);
        goTo(intent);
    }

    @Override
    public void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(MainActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, entity);
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mSongDisplayFragment.getDisplayDataList());
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_LOCAL_SONG_SELECTION && data != null) {
                List<LocalSongEntity> selectedSongs = data.getParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS);
                LocalBillEntity billToAddSong = data.getParcelableExtra(Constants.KEY_EXTRA_BILL);
                mMainPresenter.onSelectedLocalSongsResult(billToAddSong, selectedSongs);
            }
        }

    }

    @Override
    public void onBackPressed() {
        showSnackbarShort(mToolbar,
                getString(R.string.ask_exit_app),
                getString(R.string.confirm_positive),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }
}
