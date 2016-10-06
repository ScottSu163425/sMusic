package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.MainPagerAdapter;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.LocalSongDisplayStyle;
import com.scott.su.smusic.constant.LocalSongDisplayType;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.MainPresenter;
import com.scott.su.smusic.mvp.presenter.impl.MainPresenterImpl;
import com.scott.su.smusic.mvp.view.MainView;
import com.scott.su.smusic.service.MusicPlayService;
import com.scott.su.smusic.ui.fragment.CreateBillDialogFragment;
import com.scott.su.smusic.ui.fragment.DrawerMenuFragment;
import com.scott.su.smusic.ui.fragment.LocalAlbumDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalBillDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetFragment;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.scott.su.smusic.util.MusicPlayUtil;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.callback.SimpleCallback;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.PermissionUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
    private ServiceConnection mMusicPlayServiceConnection;
    private MusicPlayService.MusicPlayServiceBinder mMusicPlayServiceBinder;
    private int mCurrentTabPosition = 0;
    private boolean mDataInitFinish = false;
//    private RecyclerView.OnScrollListener mSongScrollListener;


    private static final int REQUEST_CODE_LOCAL_SONG_SELECTION = 1111;
    //    private static final int INDEX_PAGE_FAB = 1; //Index of page that fab will be shown;
    //    private static final String NEED_OPEN_DRAWER = "NEED_OPEN_DRAWER";
    //    private static final String CURRENT_TAB_POSITION = "CURRENT_TAB_POSITION";
    private static final int TAB_POSITION_SONG = 0;
    private static final int TAB_POSITION_BILL = 1;
    private static final int TAB_POSITION_ALBUM = 2;

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
            goToWithTransition(SearchActivity.class);
        }
        return true;
    }

    @Override
    public View getSnackbarParent() {
        return mToolbar;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void initPreData() {
        if (getIntent().getBooleanExtra(Constants.KEY_IS_FROM_NOTIFICATION, false)) {
            Intent intent = new Intent(this, MusicPlayActivity.class);
            intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG,
                    getIntent().getParcelableExtra(Constants.KEY_EXTRA_LOCAL_SONG));
            intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS,
                    getIntent().getParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS));
            goTo(intent);

            //Set the enterance value false, to avoid to excute duplicated logic when switch day-night mode.
            Intent getIntent = getIntent();
            getIntent.putExtra(Constants.KEY_IS_FROM_NOTIFICATION, false);
            setIntent(getIntent);
        }

        PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //Bind music play service
        mMusicPlayServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mMusicPlayServiceBinder = (MusicPlayService.MusicPlayServiceBinder) iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mMusicPlayServiceBinder = null;
            }
        };
        Intent intent = new Intent(MainActivity.this, MusicPlayService.class);
        startService(intent);
        bindService(intent, mMusicPlayServiceConnection, BIND_AUTO_CREATE);
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
        mSongDisplayFragment = LocalSongDisplayFragment.newInstance(LocalSongDisplayType.Normal, null,
                LocalSongDisplayStyle.CoverDivider);
        mBillDisplayFragment = LocalBillDisplayFragment.newInstance();
        mAlbumDisplayFragment = LocalAlbumDisplayFragment.newInstance();

        mSongDisplayFragment.setSwipeRefreshEnable(true);
        mSongDisplayFragment.setLoadMoreEnable(false);
        mBillDisplayFragment.setSwipeRefreshEnable(false);
        mBillDisplayFragment.setLoadMoreEnable(false);
        mAlbumDisplayFragment.setLoadMoreEnable(false);


        pageFragments.add(mSongDisplayFragment);
        pageFragments.add(mBillDisplayFragment);
        pageFragments.add(mAlbumDisplayFragment);

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),
                pageFragments,
                getResources().getStringArray(R.array.titles_tab_main)));
        mViewPager.setOffscreenPageLimit(pageFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_drawer_menu_main, mDrawerMenuFragment).commit();

//        if (getIntent().getBooleanExtra(NEED_OPEN_DRAWER, false)) {
//            mDrawerLayout.openDrawer(Gravity.LEFT);
//        }

//        mViewPager.setCurrentItem(getIntent().getIntExtra(CURRENT_TAB_POSITION, 0), false);

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
                mCurrentTabPosition = position;
                if (position == TAB_POSITION_SONG) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_play_arrow_notification_36dp);
                } else if (position == TAB_POSITION_BILL) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_add_fab_24dp);
                } else if (position == TAB_POSITION_ALBUM) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_play_arrow_notification_36dp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (ViewPager.SCROLL_STATE_IDLE == state) {
//                    if (mViewPager.getCurrentItem() == INDEX_PAGE_FAB) {
//                        showFab();
//                    } else {
//                        hideFab();
//                    }
//                }

            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPresenter.onFabClick();
            }
        });
        mFloatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mMainPresenter.onFabLongClick();
                return true;
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
    public boolean isCurrentTabSong() {
        return mCurrentTabPosition == TAB_POSITION_SONG;
    }

    @Override
    public boolean isCurrentTabBill() {
        return mCurrentTabPosition == TAB_POSITION_BILL;
    }

    @Override
    public boolean isCurrentTabAlbum() {
        return mCurrentTabPosition == TAB_POSITION_ALBUM;
    }

    @Override
    public LocalSongEntity getCurrentPlayingSong() {
        return mMusicPlayServiceBinder.getCurrentPlayingSong();
    }

    @Override
    public ArrayList<LocalSongEntity> getCurrentPlayingSongs() {
        return mMusicPlayServiceBinder.getCurrentPlayingSongs();
    }

    @Override
    public void scrollSongPositionTo(int position, final SimpleCallback scrollCompleteCallback) {
        mSongDisplayFragment.scrollToPosition(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollCompleteCallback.onCallback();
            }
        }, 100);
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

    @Override
    public void playSongInPosition(final int position) {
        scrollSongPositionTo(position, new SimpleCallback() {
            @Override
            public void onCallback() {
                goToMusicWithSharedElementInPosition(position);
            }
        });
    }

    @Override
    public void playRandomSong() {
        final int randomPositon = new Random().nextInt(mSongDisplayFragment.getDisplayDataList().size());
        playSongInPosition(randomPositon);
    }

    private void goToMusicWithSharedElementInPosition(int position) {
        Intent intent = new Intent(MainActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, mSongDisplayFragment.getDisplayDataList().get(position));
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mSongDisplayFragment.getDisplayDataList());
        goToWithSharedElement(intent, mSongDisplayFragment.getSongViewHolder(position).getCoverImageView(), getString(R.string.transition_name_cover));

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
        goToWithTransition(intent);
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
        goToWithTransition(intent);
    }

    @Override
    public void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(MainActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, entity);
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mSongDisplayFragment.getDisplayDataList());
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void goToMusicWithSharedElementFromFAB() {

    }

    @Override
    public void turnOnNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        recreateActivity(true);
    }

    @Override
    public void turnOffNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

        recreateActivity(false);
    }

    private void recreateActivity(boolean isForNightMode) {
//        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//        intent.putExtra(NEED_OPEN_DRAWER, true);
//        intent.putExtra(CURRENT_TAB_POSITION, mViewPager.getCurrentItem());
//        if (isForNightMode) {
//            startActivity(intent);
//            if (AppConfig.isNightModeOn(this)) {
//                overridePendingTransition(R.anim.in_north, R.anim.out_south);
//            } else {
//                overridePendingTransition(R.anim.in_south, R.anim.out_north);
//            }
//            finish();
//        } else {
//            startActivity(intent);
//            if (AppConfig.isLanguageModeOn(this)) {
//                overridePendingTransition(R.anim.in_east, R.anim.out_west);
//            } else {
//                overridePendingTransition(R.anim.in_west, R.anim.out_east);
//            }
//            finish();
//        }
        getSupportFragmentManager().beginTransaction().remove(mAlbumDisplayFragment).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().remove(mSongDisplayFragment).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().remove(mBillDisplayFragment).commitAllowingStateLoss();

        recreate();
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

    @Override
    protected void onDestroy() {
        unbindService(mMusicPlayServiceConnection);
        super.onDestroy();
    }
}
