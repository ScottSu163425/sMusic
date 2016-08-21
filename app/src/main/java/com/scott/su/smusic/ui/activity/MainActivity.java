package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.adapter.MainPagerAdapter;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongBillModelImpl;
import com.scott.su.smusic.ui.fragment.LocalAlbumDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBillDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 2016-8-18
 */
public class MainActivity extends BaseActivity {
    private Toolbar mToolbar;   //Title bar.
    private DrawerLayout mDrawerLayout;     //Content drawer.
    private ViewPager mViewPager;   //Content ViewPager.
    private TabLayout mTabLayout;   //Tabs for ViewPager.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        setupToolbar();
        initView();
        initData();
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
        List<String> pageTitles = new ArrayList<>();

        pageFragments.add(LocalSongDisplayFragment.newInstance(LocalSongDisplayAdapter.DISPLAY_TYPE.CoverDivider));
        pageFragments.add(LocalSongBillDisplayFragment.newInstance());
        pageFragments.add(LocalAlbumDisplayFragment.newInstance());
        pageTitles.add("本地音乐");
        pageTitles.add("歌单");
        pageTitles.add("专辑");

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), pageFragments, pageTitles));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(pageFragments.size());
    }

    @Override
    public void onBackPressed() {
        showSnackbarShort(mToolbar, "退出应用？", "确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
