package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.MainPagerAdapter;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.LocalSongDisplayStyle;
import com.scott.su.smusic.constant.LocalSongDisplayType;
import com.scott.su.smusic.entity.LocalAlbumEntity;
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
import com.su.scott.slibrary.util.PermissionUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private static final String NEED_OPEN_DRAWER = "NEED_OPEN_DRAWER";


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
        if (item.getItemId() == R.id.action_search_menu_main) {
//            stopService(new Intent(this, MusicPlayService.class));
            goTo(SearchActivity.class);
        }
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

        if (getIntent().getBooleanExtra(NEED_OPEN_DRAWER,false)){
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void initData() {
        List<Fragment> pageFragments = new ArrayList<>();

        mDrawerMenuFragment = new DrawerMenuFragment();
        mSongDisplayFragment = LocalSongDisplayFragment.newInstance(LocalSongDisplayType.Normal, null,
                LocalSongDisplayStyle.CoverDivider);
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
                mMainPresenter.onDrawerMenuStatisticClick();
            }

            @Override
            public void onNightModeOn() {
                mMainPresenter.onNightModeOn();
            }

            @Override
            public void onNightModeOff() {
                mMainPresenter.onNightModeOff();
            }

            @Override
            public void onLanguageModeOn() {
                mMainPresenter.onLanguageModeOn();
            }

            @Override
            public void onLanguageModeOff() {
                mMainPresenter.onLanguageModeOff();
            }


            @Override
            public void onUpdateClick(View v) {
                mMainPresenter.onDrawerMenuUpdateClick();
            }

            @Override
            public void onAboutClick(View v) {
                mMainPresenter.onDrawerMenuAboutClick();
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

        mAlbumDisplayFragment.setAlbumItemClickCallback(new LocalAlbumDisplayFragment.AlbumItemClickCallback() {
            @Override
            public void onAlbumItemClick(View itemView, LocalAlbumEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mMainPresenter.onAlbumItemClick(itemView, entity, position, sharedElements, transitionNames, data);
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
        LocalSongBottomSheetFragment.newInstance()
                .setLocalSongEntity(songEntity)
                .setMenuClickCallback(new LocalSongBottomSheetCallback() {
                    @Override
                    public void onAddToBillClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity) {
                        mMainPresenter.onBottomSheetAddToBillClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onAlbumClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity) {
                        mMainPresenter.onBottomSheetAlbumClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onShareClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity) {
                        mMainPresenter.onBottomSheetShareClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onDeleteClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity) {
                        mMainPresenter.onBottomSheetDeleteClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                })
                .show(getSupportFragmentManager(), "");
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
                getString(R.string.ok),
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

    @Override
    public void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(MainActivity.this, LocalBillDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_BILL, entity);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void goToAlbumDetail(LocalAlbumEntity entity) {
        Intent intent = new Intent(MainActivity.this, LocalAlbumDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_ALBUM, entity);
        goTo(intent);
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
    public void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(MainActivity.this, LocalAlbumDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_ALBUM, entity);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void showDeleteDialog(LocalSongEntity songEntity) {

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
    public void turnOnNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//        getSupportFragmentManager().beginTransaction().remove(mAlbumDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mBillDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mSongDisplayFragment).commitAllowingStateLoss();

//        recreate();
        recreateActivity(true);
    }

    @Override
    public void turnOffNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        getSupportFragmentManager().beginTransaction().remove(mAlbumDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mBillDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mSongDisplayFragment).commitAllowingStateLoss();

        //        recreate();
        recreateActivity(true);
    }

    @Override
    public void turnOnLanguageMode() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale = Locale.ENGLISH;
        resources.updateConfiguration(config, dm);

//        getSupportFragmentManager().beginTransaction().remove(mAlbumDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mBillDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mSongDisplayFragment).commitAllowingStateLoss();

//        recreate();
        recreateActivity(false);
    }

    @Override
    public void turnOffLanguageMode() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale = Locale.getDefault();
        resources.updateConfiguration(config, dm);

//        getSupportFragmentManager().beginTransaction().remove(mAlbumDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mBillDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mSongDisplayFragment).commitAllowingStateLoss();

//        recreate();
        recreateActivity(false);
    }

    private void recreateActivity(boolean isForNightMode) {
        Intent intent =new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra(NEED_OPEN_DRAWER,true);
        if (isForNightMode) {
            startActivity(intent);
            if (AppConfig.isNightModeOn(this)) {
                overridePendingTransition(R.anim.in_north, R.anim.out_south);
            } else {
                overridePendingTransition(R.anim.in_south, R.anim.out_north);
            }
            finish();
        } else {
            startActivity(intent);
            if (AppConfig.isLanguageModeOn(this)) {
                overridePendingTransition(R.anim.in_east, R.anim.out_west);
            } else {
                overridePendingTransition(R.anim.in_west, R.anim.out_east);
            }
            finish();
        }
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
