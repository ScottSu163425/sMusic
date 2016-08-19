package com.scott.su.smusic.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;

/**
 * 2016-8-18
 */
public class MainActivity extends BaseActivity {
    private Toolbar mToolbar;//标题栏
    private DrawerLayout mDrawerLayout;//抽屉式布局
    private ViewPager mViewPager; //内容展示ViewPager
    private TabLayout mTabLayout; //ViewPager对应选项卡布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        initView();
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
        mToolbar.setTitle("SMusic");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });
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
