package com.scott.su.smusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalSongDisplayAdapter;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongBillDetailPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongBillDetailPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalSongBillDetailView;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetFragment;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * 2016-8-28
 */
public class LocalSongBillDetailActivity extends BaseActivity implements LocalSongBillDetailView {
    private LocalSongBillDetailPresenter mBillDetailPresenter;
    private LocalSongBillEntity mBillEntity;
    private ImageView mCoverImageView;
    private LocalSongDisplayFragment mBillSongDisplayFragment;
    private FloatingActionButton mFloatingActionButton;

    private static final int REQUESt_CODE_LOCAL_SONG_SELECTION = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_song_bill_detail);

        mBillDetailPresenter = new LocalSongBillDetailPresenterImpl(this);
        mBillDetailPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void initPreData() {
        mBillEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_BILL);
    }

    @Override
    public void initToolbar() {
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

    @Override
    public void initView() {
        mCoverImageView = (ImageView) findViewById(R.id.iv_cover_local_song_bill_detail);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_local_song_bill_detail);

        if (mBillEntity.isBillEmpty()) {
            ViewUtil.setViewGone(mFloatingActionButton);
        }
    }

    @Override
    public void initData() {
        mBillSongDisplayFragment = LocalSongDisplayFragment.newInstance(mBillEntity,
                LocalSongDisplayAdapter.DISPLAY_TYPE.NumberDivider);
        mBillSongDisplayFragment.setSwipeRefreshEnable(false);
        mBillSongDisplayFragment.setLoadMoreEnable(false);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_display_local_song_bill_detail, mBillSongDisplayFragment)
                .commit();
    }

    @Override
    public void initListener() {
        mBillSongDisplayFragment.setDisplayCallback(new LocalSongDisplayFragment.LocalSongDisplayCallback() {
            @Override
            public void onItemClick(View view, int position, LocalSongEntity entity) {

            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                mBillDetailPresenter.onBillSongItemMoreClick(view, position, entity);
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_song_bill_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_local_song_bill_detaile) {
            mBillDetailPresenter.onAddSongsMenuClick();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESt_CODE_LOCAL_SONG_SELECTION && data != null) {
                List<LocalSongEntity> selectedSongs = data.getParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS);
                mBillDetailPresenter.onSelectedLocalSongsResult(mBillEntity, selectedSongs);
            }
        }
    }

    @Override
    public LocalSongBillEntity getBillEntity() {
        return mBillEntity;
    }

    @Override
    public void setBillEntity(LocalSongBillEntity billEntity) {
        this.mBillEntity = billEntity;
    }

    @Override
    public void loadCover(final String coverPath, boolean needReveal) {
        if (needReveal) {
            CirclarRevealUtil.revealIn(mCoverImageView,
                    CirclarRevealUtil.DIRECTION.LEFT_TOP,
                    CirclarRevealUtil.DEFAULT_REVEAL_DURATION,
                    new DecelerateInterpolator(),
                    new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {
                            Glide.with(LocalSongBillDetailActivity.this)
                                    .load(coverPath)
                                    .placeholder(R.color.place_holder_loading)
                                    .error(R.drawable.ic_cover_default_song_bill)
                                    .into(mCoverImageView);
                        }

                        @Override
                        public void onAnimEnd() {

                        }
                    });

        } else {
            Glide.with(this)
                    .load(coverPath)
                    .placeholder(R.color.place_holder_loading)
                    .error(R.drawable.ic_cover_default_song_bill)
                    .into(mCoverImageView);
        }

    }

    @Override
    public void showFab() {
        if (!ViewUtil.isViewVisiable(mFloatingActionButton)) {
            ViewUtil.setViewVisiable(mFloatingActionButton);
        }
    }

    @Override
    public void hideFab() {
        if (ViewUtil.isViewVisiable(mFloatingActionButton)) {
            ViewUtil.setViewGone(mFloatingActionButton);
        }
    }

    @Override
    public void goToLocalSongSelectionActivity() {
        Intent intent = new Intent(LocalSongBillDetailActivity.this, LocalSongSelectionActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_BILL, mBillEntity);
        startActivityForResult(intent, REQUESt_CODE_LOCAL_SONG_SELECTION);
    }

    @Override
    public void showAddSongsUnsuccessfully(String msg) {
        showSnackbarShort(mCoverImageView, msg);
    }

    @Override
    public void showAddSongsSuccessfully(String msg) {
        showSnackbarShort(mCoverImageView, msg);
    }

    @Override
    public void refreshBillSongDisplay(LocalSongBillEntity billEntity) {
        if (billEntity.isBillEmpty()) {
            CirclarRevealUtil.revealOut(mFloatingActionButton, CirclarRevealUtil.DIRECTION.CENTER, true);
        } else {
            if (ViewUtil.isViewGone(mFloatingActionButton)) {
                CirclarRevealUtil.revealIn(mFloatingActionButton, CirclarRevealUtil.DIRECTION.CENTER);
            }
        }
        mBillSongDisplayFragment.setSongBillEntity(billEntity);
        mBillSongDisplayFragment.reInitialize();
    }

    @Override
    public void showSongBottomSheet(LocalSongEntity songEntity) {
        LocalSongBottomSheetFragment.newInstance()
                .setLocalSongEntity(songEntity)
                .setMenuClickCallback(new LocalSongBottomSheetFragment.MenuClickCallback() {

                    @Override
                    public void onAddToBillClick(LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetAddToBillClick(songEntity);
                    }

                    @Override
                    public void onArtistClick(LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetArtistClick(songEntity);
                    }

                    @Override
                    public void onAlbumClick(LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetAlbumClick(songEntity);
                    }

                    @Override
                    public void onShareClick(LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetShareClick(songEntity);
                    }

                    @Override
                    public void onDeleteClick(LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetDeleteClick(songEntity);
                    }
                })
                .show(getSupportFragmentManager(), "");
    }


}
