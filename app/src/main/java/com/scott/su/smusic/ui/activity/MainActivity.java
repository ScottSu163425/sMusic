package com.scott.su.smusic.ui.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.MainPagerAdapter;
import com.scott.su.smusic.callback.DrawerMenuCallback;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.callback.LocalSongDisplayCallback;
import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.callback.ShutDownServiceCallback;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.constant.PlayStatus;
import com.scott.su.smusic.constant.TimerStatus;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.event.LocalBillChangedEvent;
import com.scott.su.smusic.mvp.contract.MainContract;
import com.scott.su.smusic.mvp.presenter.impl.MainPresenterImpl;
import com.scott.su.smusic.service.MusicPlayService;
import com.scott.su.smusic.service.ShutDownTimerService;
import com.scott.su.smusic.ui.fragment.DrawerMenuFragment;
import com.scott.su.smusic.ui.fragment.LocalAlbumDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalBillDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetMenuFragment;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.callback.SimpleCallback;
import com.su.scott.slibrary.constant.BaseConstants;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.DialogUtil;
import com.su.scott.slibrary.util.StatusBarUtil;
import com.su.scott.slibrary.util.T;
import com.su.scott.slibrary.util.TimeUtil;
import com.su.scott.slibrary.util.ViewUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * 2016-8-18
 */
public class MainActivity extends BaseActivity<MainContract.MainView, MainContract.MainPresenter>
        implements MainContract.MainView {

    private static final String NEED_OPEN_DRAWER = "NEED_OPEN_DRAWE";
    private static final String CURRENT_TAB_POSITION = "CURRENT_TAB_POSITION";
    private static final int TAB_POSITION_SONG = 0;
    private static final int TAB_POSITION_BILL = 1;
    private static final int TAB_POSITION_ALBUM = 2;

    private MainContract.MainPresenter mMainPresenter; //Presenter of mvp;
    private Toolbar mToolbar;   //Title bar;
    private DrawerLayout mDrawerLayout;     //Content drawer;
    private ViewPager mViewPager;   //Content ViewPager;
    private TabLayout mTabLayout;   //Tabs for ViewPager;
    private FloatingActionButton mFAB;
    private DrawerMenuFragment mDrawerMenuFragment;
    private LocalSongDisplayFragment mSongDisplayFragment;
    private LocalBillDisplayFragment mBillDisplayFragment;
    private LocalAlbumDisplayFragment mAlbumDisplayFragment;
    private ServiceConnection mMusicPlayServiceConnection;
    private MusicPlayService.MusicPlayServiceBinder mMusicPlayServiceBinder;
    private ServiceConnection mShutDownTimerServiceConnection;
    private ShutDownTimerService.ShutDownTimerServiceBinder mShutDownTimerServiceBinder;
    private boolean mFabPlayRandom;

    /*To fix the problem that the item position of return shared element transition will be wrong,
     * after created(added) local bill and changed a bill to trigger update.
     * Maybe it caused by the change of item count,and it remains to be improved in the future.
      * */
    private boolean mNeedRefreshBill;


    @Override
    protected int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainContract.MainPresenter getPresenter() {
        if (mMainPresenter == null) {
            mMainPresenter = new MainPresenterImpl(this);
        }
        return mMainPresenter;
    }

    @Override
    protected boolean subscribeEvents() {
        return true;
    }

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mMainPresenter.onViewFirstTimeCreated();

        StatusBarUtil.setColorForDrawerLayout(MainActivity.this,
                mDrawerLayout,
                getResources().getColor(R.color.colorPrimaryDark),
                BaseConstants.ALPHA_TRANSLUCENT_STATUS_BAR);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNeedRefreshBill) {
            updateBillDisplay();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search_menu_main) {
            goToWithTransition(SearchActivity.class);
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void initPreData() {
        if (getIntent().getBooleanExtra(Constants.KEY_EXTRA_IS_FROM_NOTIFICATION, false)) {
            Intent intent = new Intent(this, MusicPlayActivity.class);
            intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG,
                    getIntent().getParcelableExtra(Constants.KEY_EXTRA_LOCAL_SONG));
            intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS,
                    getIntent().getParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS));
            goTo(intent);

            //Set the entrance value false, to avoid to excute duplicated logic when switch day-night mode.
            Intent getIntent = getIntent();
            getIntent.putExtra(Constants.KEY_EXTRA_IS_FROM_NOTIFICATION, false);
            setIntent(getIntent);
        }
    }

    @Override
    public void bindMusicPlayService() {
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
    public void bindShutDownTimerService() {
        mShutDownTimerServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mShutDownTimerServiceBinder = (ShutDownTimerService.ShutDownTimerServiceBinder) iBinder;
                mShutDownTimerServiceBinder.setTimerCallback(new ShutDownServiceCallback() {
                    @Override
                    public void onStart(long duration) {
                        T.showShort(getApplicationContext(), TimeUtil.millisecondToMinute(duration) + "分钟后停止播放");
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        mDrawerMenuFragment.setTimerTime(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        mDrawerMenuFragment.setTimerTime(0);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mShutDownTimerServiceBinder = null;
            }
        };
        Intent intent = new Intent(MainActivity.this, ShutDownTimerService.class);
        startService(intent);
        bindService(intent, mShutDownTimerServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void initToolbar() {
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

    @Override
    public void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mFAB = (FloatingActionButton) findViewById(R.id.fab_main);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void initData() {
        List<Fragment> pageFragments = new ArrayList<>();
        mDrawerMenuFragment = new DrawerMenuFragment();
        mSongDisplayFragment = LocalSongDisplayFragment.newInstance();
        mBillDisplayFragment = LocalBillDisplayFragment.newInstance();
        mAlbumDisplayFragment = LocalAlbumDisplayFragment.newInstance();

        pageFragments.add(mSongDisplayFragment);
        pageFragments.add(mBillDisplayFragment);
        pageFragments.add(mAlbumDisplayFragment);

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),
                pageFragments,
                getResources().getStringArray(R.array.titles_tab_main)));
        mViewPager.setOffscreenPageLimit(pageFragments.size());

        mTabLayout.setupWithViewPager(mViewPager, false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_drawer_menu_main, mDrawerMenuFragment).commitNow();

        if (getIntent().getBooleanExtra(NEED_OPEN_DRAWER, false)) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

        int tabPosition = getIntent().getIntExtra(CURRENT_TAB_POSITION, 0);
        mViewPager.setCurrentItem(tabPosition, true);

        if (tabPosition == TAB_POSITION_SONG) {
            ViewUtil.setViewVisiable(mFAB);
            mFAB.setImageResource(R.drawable.ic_play_arrow__white_24dp);
        } else if (tabPosition == TAB_POSITION_BILL) {
            ViewUtil.setViewVisiable(mFAB);
            mFAB.setImageResource(R.drawable.ic_add_fab_24dp);
        } else if (tabPosition == TAB_POSITION_ALBUM) {
            ViewUtil.setViewGone(mFAB);
        }

        mMainPresenter.onInitDataComplete();
    }

    @Override
    public void initListener() {
        mDrawerMenuFragment.setMenuCallback(new DrawerMenuCallback() {


            @Override
            public void onDrawerUserHeadClick(View sharedElement, String transitionName) {
                mMainPresenter.onDrawerUserHeadClick(sharedElement, transitionName);
            }

            @Override
            public void onDrawerMenuUserCenterClick(View v, View sharedElement, String transitionName) {
                mMainPresenter.onDrawerUserHeadClick(sharedElement, transitionName);
            }

            @Override
            public void onDrawerMenuNightModeOn() {
                mMainPresenter.onDrawerMenuNightModeOn();
            }

            @Override
            public void onDrawerMenuNightModeOff() {
                mMainPresenter.onDrawerMenuNightModeOff();
            }

            @Override
            public void onDrawerMenuLanguageModeOn() {
                mMainPresenter.onDrawerMenuLanguageModeOn();
            }

            @Override
            public void onDrawerMenuLanguageModeOff() {
                mMainPresenter.onDrawerMenuLanguageModeOff();
            }

            @Override
            public void onDrawerMenuSettingsClick(View v) {
                mMainPresenter.onDrawerMenuSettingsClick(v);
            }

            @Override
            public void onDrawerMenuTimerCancelClick() {
                mMainPresenter.onDrawerMenuTimerCancelClick();
            }

            @Override
            public void onDrawerMenuTimerMinutesClick(final long millisOfmin) {
                mMainPresenter.onDrawerMenuTimerMinutesClick(millisOfmin);
            }

            @Override
            public void onDrawerMenuStatisticClick(View v) {
                mMainPresenter.onDrawerMenuStatisticClick(v);
            }
        });

        mSongDisplayFragment.setDisplayCallback(new LocalSongDisplayCallback() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mMainPresenter.onLocalSongItemClick(itemView, entity, position, sharedElements, transitionNames, data);
            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                mMainPresenter.onLocalSongItemMoreClick(entity);
            }

            @Override
            public void onDataLoading() {
                // FIXME: 2016/10/24
//                hideFab(false);
            }

            @Override
            public void onDisplayDataChanged(List<LocalSongEntity> dataList) {
                if (dataList == null || dataList.isEmpty()) {
                    //Show fab when current tab is bill,even through the data is empty.
                    if (mViewPager.getCurrentItem() == TAB_POSITION_SONG) {
                        hideFab(false);
                    }
                } else {
                    //Fix problem that after recreate main activity the fab still showing,when current position of viewpager
                    //is album,which is not supposed to be shown ;
                    if (mViewPager.getCurrentItem() != TAB_POSITION_ALBUM) {
                        showFab(false);
                    }
                }
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
                if (position == TAB_POSITION_SONG) {
                    showFab(true);
                    if (mFabPlayRandom) {
                        setFabPlayRandom();
                    } else {
                        setFabPlayCurrent();
                    }
                } else if (position == TAB_POSITION_BILL) {
                    showFab(true);
                    AnimUtil.rotate2DPositive(mFAB, AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_SHORT).start();
                    mFAB.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFAB.setImageResource(R.drawable.ic_add_fab_24dp);
                        }
                    }, AnimUtil.DURATION_SHORT_HALF);
                } else if (position == TAB_POSITION_ALBUM) {
                    hideFab(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPresenter.onFabClick();
            }
        });

        mFAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mMainPresenter.onFabLongClick();
                return true;
            }
        });

    }

    @Override
    public void showLocalSongBottomSheet(LocalSongEntity songEntity) {
        LocalSongBottomSheetMenuFragment.newInstance()
                .setLocalSongEntity(songEntity)
                .setMenuClickCallback(new LocalSongBottomSheetCallback() {
                    @Override
                    public void onAddToBillClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mMainPresenter.onBottomSheetAddToBillClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onAlbumClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mMainPresenter.onBottomSheetAlbumClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onDeleteClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
//                        mMainPresenter.onBottomSheetDeleteClick(songEntity);
//                        fragment.dismissAllowingStateLoss();

                        Toast.makeText(MainActivity.this, "暂时关闭", Toast.LENGTH_SHORT).show();
                    }

                })
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public boolean isCurrentTabSong() {
        return mViewPager.getCurrentItem() == TAB_POSITION_SONG;
    }

    @Override
    public boolean isCurrentTabBill() {
        return mViewPager.getCurrentItem() == TAB_POSITION_BILL;
    }

    @Override
    public boolean isCurrentTabAlbum() {
        return mViewPager.getCurrentItem() == TAB_POSITION_ALBUM;
    }

    @Override
    public int getCurrentPosition() {
        return mMusicPlayServiceBinder.getCurrentPosition();
    }

    @Override
    public PlayStatus getServiceCurrentPlayStatus() {
        return mMusicPlayServiceBinder.getServiceCurrentPlayStatus();
    }

    @Override
    public LocalSongEntity getServiceCurrentPlayingSong() {
        return mMusicPlayServiceBinder.getServiceCurrentPlayingSong();
    }

    @Override
    public ArrayList<LocalSongEntity> getServicePlayListSongs() {
        return mMusicPlayServiceBinder.getServicePlayListSongs();
    }

    @Override
    public void setServiceCurrentPlaySong(LocalSongEntity currentPlaySong) {
    }

    @Override
    public void setServicePlayListSongs(ArrayList<LocalSongEntity> playSongs) {

    }

    @Override
    public void setServicePlayMode(PlayMode playMode) {
    }

    @Override
    public PlayMode getServicePlayMode() {
        return mMusicPlayServiceBinder.getServicePlayMode();
    }

    @Override
    public void play() {

    }

    @Override
    public void playSkip() {

    }

    @Override
    public void play(int position) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seekTo(int position) {

    }

    @Override
    public void playPrevious() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void removeServiceSong(LocalSongEntity songEntity) {
        mMusicPlayServiceBinder.removeServiceSong(songEntity);
    }

    @Override
    public void clearServiceSongs() {

    }

    @Override
    public void registerServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {

    }

    @Override
    public void unregisterServicePlayCallback(@NonNull MusicPlayServiceCallback callback) {

    }

    @Override
    public ArrayList<LocalSongEntity> getDisplaySongs() {
        return mSongDisplayFragment.getDisplayDataList();
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
        if (isActivityResume()) {
            if (mBillDisplayFragment.isVisible()) {
                mBillDisplayFragment.reInitialize();
                mNeedRefreshBill = false;
            }
        } else {
            mNeedRefreshBill = true;
        }
    }

    @Override
    public void updateAlbumDisplay() {
        if (mAlbumDisplayFragment.isVisible()) {
            mAlbumDisplayFragment.reInitialize();
        }
    }

    //
    @Override
    public void playSongInPosition(final int position, final boolean needOpenMusicPlay) {
        scrollSongPositionTo(position, new SimpleCallback() {
            @Override
            public void onCallback() {
                if (needOpenMusicPlay) {
                    goToMusicWithSharedElementInPosition(position);
                } /*else {
                    if (mMusicPlayServiceBinder.getServicePlayListSongs().isEmpty()) {
                        mMusicPlayServiceBinder.setServicePlayListSongs(mSongDisplayFragment.getDisplayDataList());
                    }
                    mMusicPlayServiceBinder.play(position);
                }*/
            }
        });
    }

    @Override
    public void playRandomSong() {
        final int randomPosition = new Random().nextInt(mSongDisplayFragment.getDisplayDataList().size());
        playSongInPosition(randomPosition, true);
    }

    private void goToMusicWithSharedElementInPosition(int position) {
        Intent intent = new Intent(MainActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, mSongDisplayFragment.getDisplayDataList().get(position));
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mSongDisplayFragment.getDisplayDataList());
        goToWithSharedElements(intent,
                new View[]{mSongDisplayFragment.getViewHolder(position).getCoverImageView(), mFAB},
                new String[]{getString(R.string.transition_name_cover), getString(R.string.transition_name_fab)});
    }

    /**
     * Show fab with animation.
     */
    @Override
    public void showFab(boolean needAnim) {
        if (!ViewUtil.isViewVisiable(mFAB)) {
            if (needAnim) {
                AnimUtil.scaleIn(mFAB, AnimUtil.DURATION_SHORT);
            } else {
                ViewUtil.setViewVisiable(mFAB);
            }
        }
    }

    /**
     * Hide fab with animation.
     */
    @Override
    public void hideFab(boolean needAnim) {
        if (ViewUtil.isViewVisiable(mFAB)) {
            if (needAnim) {
                AnimUtil.scaleOut(mFAB, AnimUtil.DURATION_SHORT);
                AnimUtil.rotate2DNegative(mFAB, AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_SHORT).start();
            } else {
                ViewUtil.setViewGone(mFAB);
            }
        }
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
    public void showDeleteDialog(final LocalSongEntity songEntity) {
        DialogUtil.showDialog(getViewContext(),
                "《" + songEntity.getTitle() + "》",
                getString(R.string.ask_remove_from_device),
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMainPresenter.onBottomSheetDeleteConfirmed(songEntity);
                    }
                }, null, null);
    }

    @Override
    public void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(MainActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, entity);
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mSongDisplayFragment.getDisplayDataList());
        goToWithSharedElements(intent,
                new View[]{sharedElement, mFAB},
                new String[]{transitionName, getString(R.string.transition_name_fab)});
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
        config.locale = Locale.CHINESE;
        resources.updateConfiguration(config, dm);

        recreateActivity(false);
    }

    @Override
    public boolean isFabPlayRandom() {
        return mFabPlayRandom;
    }

    @Override
    public void setFabPlayRandom() {
        AnimUtil.rotate2DPositive(mFAB, AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_SHORT).start();
        mFAB.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFAB.setImageResource(R.drawable.ic_shuffle_white_24dp);
            }
        }, AnimUtil.DURATION_SHORT_HALF);
        mFabPlayRandom = true;
    }

    @Override
    public void setFabPlayCurrent() {
        AnimUtil.rotate2DPositive(mFAB, AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_SHORT).start();
        mFAB.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFAB.setImageResource(R.drawable.ic_play_arrow__white_24dp);
            }
        }, AnimUtil.DURATION_SHORT_HALF);
        mFabPlayRandom = false;
    }

    @Override
    public void goToPlayStatistic() {
        goToWithTransition(PlayStatisticActivity.class);
    }

    @Override
    public void goToUserCenter(View sharedElement, String transitionName) {
        goToWithSharedElement(UserCenterActivity.class, sharedElement, transitionName);
    }

    @Override
    public void goToSettings() {
        goToWithTransition(SettingsActivity.class);
    }

    @Override
    public void goToLocalBillCreation() {
        goToWithSharedElement(LocalBillCreationActivity.class, mFAB, getString(R.string.transition_name_fab));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocalBillChangedEvent(LocalBillChangedEvent event) {
        mMainPresenter.onLocalBillChangedEvent(event);
    }

    private void recreateActivity(boolean isForNightMode) {
//        getSupportFragmentManager().beginTransaction().remove(mAlbumDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mSongDisplayFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().remove(mBillDisplayFragment).commitAllowingStateLoss();
//        recreate();

        //Another way to recreate activity with animation;
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NEED_OPEN_DRAWER, true);
        intent.putExtra(CURRENT_TAB_POSITION, mViewPager.getCurrentItem());
        startActivity(intent);

        if (isForNightMode) {
            if (AppConfig.isNightModeOn(MainActivity.this)) {
                overridePendingTransition(R.anim.in_top, R.anim.out_bottom);
            } else {
                overridePendingTransition(R.anim.in_bottom, R.anim.out_top);
            }
        } else {
            if (AppConfig.isLanguageModeOn(MainActivity.this)) {
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
            } else {
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }

        showSnackbarShort(getString(R.string.ask_exit_app), getString(R.string.confirm_positive),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mMusicPlayServiceConnection != null) {
            unbindService(mMusicPlayServiceConnection);
        }

        if (mShutDownTimerServiceConnection != null) {
            unbindService(mShutDownTimerServiceConnection);
        }
        super.onDestroy();
    }

    @Override
    public TimerStatus getServiceCurrentTimerShutDownStatus() {
        return mShutDownTimerServiceBinder.getServiceCurrentTimerShutDownStatus();
    }

    @Override
    public void startShutDownTimer(long duration, long interval) {
        mShutDownTimerServiceBinder.startShutDownTimer(duration, interval);
    }

    @Override
    public void cancelShutDownTimer() {
        mShutDownTimerServiceBinder.cancelShutDownTimer();
        mDrawerMenuFragment.setTimerTime(0);
    }

    @Override
    public void setTimerCallback(ShutDownServiceCallback callback) {
        mShutDownTimerServiceBinder.setTimerCallback(callback);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}
