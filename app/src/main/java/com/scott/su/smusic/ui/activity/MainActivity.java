package com.scott.su.smusic.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.MainPagerAdapter;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 2016-8-18
 */
public class MainActivity extends BaseActivity {
    private Toolbar mToolbar;//标题栏
    private DrawerLayout mDrawerLayout;//抽屉式布局
    private ViewPager mViewPager; //内容展示ViewPager
    private TabLayout mTabLayout; //ViewPager对应选项卡布局
    private LocalSongDisplayFragment mLocalSongDisplayFragment; //本地音乐列表fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
                return;
            }
        }

        setupToolbar();
        initView();
        initData();
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

        pageFragments.add(LocalSongDisplayFragment.newInstance());
        pageFragments.add(LocalSongDisplayFragment.newInstance());
        pageFragments.add(LocalSongDisplayFragment.newInstance());
        pageTitles.add("本地音乐");
        pageTitles.add("歌单");
        pageTitles.add("专辑");

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),pageFragments,pageTitles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        showSnackbarShort("退出应用？", "是", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
