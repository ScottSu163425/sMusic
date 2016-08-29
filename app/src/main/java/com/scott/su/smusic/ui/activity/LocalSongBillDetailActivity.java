package com.scott.su.smusic.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.ViewUtil;

/**
 * 2016-8-28
 */
public class LocalSongBillDetailActivity extends BaseActivity {

    private LocalSongBillEntity mBillEntity;
    private ImageView mCoverImageView;
    private LocalSongDisplayFragment mBillSongDisplayFragment;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_song_bill_detail);

        mBillEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_BILL);

        setupToolbar();
        initView();
        initData();
        initListener();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_local_song_bill_detail);
        toolbar.setTitle(mBillEntity.getBillTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalSongBillDetailActivity.this.onBackPressed();
            }
        });
    }

    private void initView() {
        mCoverImageView = (ImageView) findViewById(R.id.iv_cover_local_song_bill_detail);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_local_song_bill_detail);

        if (mBillEntity.isBillEmpty()) {
            ViewUtil.setViewGone(mFloatingActionButton);
        }
    }

    private void initData() {
        String billCoverPath = "";
        if (!mBillEntity.isBillEmpty()) {
            billCoverPath = new LocalSongModelImpl().getAlbumCoverPath(this,
                    mBillEntity.getLatestSong().getAlbumId());
        }

        Glide.with(this)
                .load(billCoverPath)
                .placeholder(R.color.place_holder_loading)
                .error(R.drawable.ic_cover_default_song_bill)
                .into(mCoverImageView);

        mBillSongDisplayFragment = LocalSongDisplayFragment.newInstance(mBillEntity,
                LocalSongDisplayAdapter.DISPLAY_TYPE.NumberDivider);
        mBillSongDisplayFragment.setSwipeRefreshEnable(false);
        mBillSongDisplayFragment.setLoadMoreEnable(false);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_display_local_song_bill_detail, mBillSongDisplayFragment)
                .commit();

    }

    private void initListener() {
        mBillSongDisplayFragment.setDisplayCallback(new LocalSongDisplayFragment.LocalSongDisplayCallback() {
            @Override
            public void onItemClick(View view, int position, LocalSongEntity entity) {

            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {

            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
