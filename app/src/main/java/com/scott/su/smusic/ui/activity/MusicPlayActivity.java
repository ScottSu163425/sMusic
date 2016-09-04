package com.scott.su.smusic.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.activity.BaseActivity;

public class MusicPlayActivity extends BaseActivity {
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public View getSnackbarParent() {
        return mToolbar;
    }

    @Override
    public void initPreData() {

    }

    @Override
    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_music_play);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
