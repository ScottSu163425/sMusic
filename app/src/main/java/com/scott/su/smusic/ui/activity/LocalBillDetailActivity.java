package com.scott.su.smusic.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.LocalBillDetailPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalBillDetailPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalSongBillDetailView;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetFragment;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.DialogUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * 2016-8-28
 */
public class LocalBillDetailActivity extends BaseActivity implements LocalSongBillDetailView {
    private LocalBillDetailPresenter mBillDetailPresenter;
    private LocalBillEntity mBillEntity;
    private ImageView mCoverImageView;
    private LocalSongDisplayFragment mBillSongDisplayFragment;
    private FloatingActionButton mPlayFAB;

    private static final int REQUESt_CODE_LOCAL_SONG_SELECTION = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_bill_detail);

        mBillDetailPresenter = new LocalBillDetailPresenterImpl(this);
        mBillDetailPresenter.onViewFirstTimeCreated();
    }

    @Override
    public View getSnackbarParent() {
        return mCoverImageView;
    }

    @Override
    public void initPreData() {
        mBillEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_BILL);
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_local_bill_detail);
        toolbar.setTitle(mBillEntity.getBillTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalBillDetailActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
        mCoverImageView = (ImageView) findViewById(R.id.iv_cover_local_bill_detail);
        mPlayFAB = (FloatingActionButton) findViewById(R.id.fab_local_bill_detail);

        if (mBillEntity.isBillEmpty()) {
            ViewUtil.setViewGone(mPlayFAB);
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
                .replace(R.id.fl_container_display_local_bill_detail, mBillSongDisplayFragment)
                .commit();
    }

    @Override
    public void initListener() {
        mBillSongDisplayFragment.setDisplayCallback(new LocalSongDisplayFragment.LocalSongDisplayCallback() {

            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
//                goTo(MusicPlayActivity.class);
                Intent intent = new Intent(LocalBillDetailActivity.this, MusicPlayActivity.class);
                intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, entity);
                goToWithSharedElement(intent, mPlayFAB, getString(R.string.transition_name_fab));
            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                mBillDetailPresenter.onBillSongItemMoreClick(view, position, entity);
            }
        });

        mPlayFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_bill_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_local_song_bill_detaile) {
            mBillDetailPresenter.onAddSongsMenuItemClick();
        } else if (id == R.id.action_clear_local_song_bill_detaile) {
            mBillDetailPresenter.onClearBillMenuItemClick();
        } else if (id == R.id.action_delete_local_song_bill_detaile) {
            mBillDetailPresenter.onDeleteBillMenuItemClick();
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
    public LocalBillEntity getBillEntity() {
        return mBillEntity;
    }

    @Override
    public void setBillEntity(LocalBillEntity billEntity) {
        this.mBillEntity = billEntity;
    }

    @Override
    public void loadCover(final String coverPath, boolean needReveal) {
        if (needReveal) {
            CirclarRevealUtil.revealIn(mCoverImageView,
                    CirclarRevealUtil.DIRECTION.RIGHT_BOTTOM,
                    CirclarRevealUtil.DURATION_REVEAL_LONG,
                    new DecelerateInterpolator(),
                    new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {
                            Glide.with(LocalBillDetailActivity.this)
                                    .load(coverPath)
                                    .placeholder(R.color.place_holder_loading)
                                    .error(R.drawable.ic_cover_default_song_bill)
                                    .into(mCoverImageView);
                        }

                        @Override
                        public void onAnimEnd() {

                        }
                    });
            //Second way to reveal;
//            CirclarRevealUtil.revealOut(mCoverImageView,
//                    CirclarRevealUtil.DIRECTION.CENTER,
//                    CirclarRevealUtil.DURATION_REVEAL_DEFAULT,
//                    new DecelerateInterpolator(),
//                    new AnimUtil.SimpleAnimListener() {
//                        @Override
//                        public void onAnimStart() {
//
//                        }
//
//                        @Override
//                        public void onAnimEnd() {
//
//                            Glide.with(LocalBillDetailActivity.this)
//                                    .load(coverPath)
//                                    .placeholder(R.color.place_holder_loading)
//                                    .error(R.drawable.ic_cover_default_song_bill)
//                                    .into(mCoverImageView);
//                            CirclarRevealUtil.revealIn(mCoverImageView, CirclarRevealUtil.DIRECTION.CENTER);
//                        }
//                    }, false);

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
        if (!ViewUtil.isViewVisiable(mPlayFAB)) {
            CirclarRevealUtil.revealIn(mPlayFAB, CirclarRevealUtil.DIRECTION.CENTER);
        }
    }

    @Override
    public void hideFab() {
        if (ViewUtil.isViewVisiable(mPlayFAB)) {
            ViewUtil.setViewGone(mPlayFAB);
        }
    }

    @Override
    public void goToLocalSongSelectionActivity() {
        Intent intent = new Intent(LocalBillDetailActivity.this, LocalSongSelectionActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_BILL, mBillEntity);
        goToForResult(intent, REQUESt_CODE_LOCAL_SONG_SELECTION);
    }

    @Override
    public void showAddSongsToBillSuccessfully() {
//        showSnackbarShort(mCoverImageView, msg);
    }

    @Override
    public void refreshBillSongDisplay(LocalBillEntity billEntity) {
        if (billEntity.isBillEmpty()) {
            CirclarRevealUtil.revealOut(mPlayFAB, CirclarRevealUtil.DIRECTION.CENTER, true);
        } else {
            if (ViewUtil.isViewGone(mPlayFAB)) {
                CirclarRevealUtil.revealIn(mPlayFAB, CirclarRevealUtil.DIRECTION.CENTER);
            }
        }
        mBillSongDisplayFragment.setSongBillEntity(billEntity);
        mBillSongDisplayFragment.reInitialize();
    }

    @Override
    public void showBillSongBottomSheet(LocalSongEntity songEntity) {
        LocalSongBottomSheetFragment.newInstance()
                .setLocalSongEntity(songEntity)
                .setMenuClickCallback(new LocalSongBottomSheetFragment.MenuClickCallback() {

                    @Override
                    public void onAddToBillClick(LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetAddToBillClick(songEntity);
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

    @Override
    public void showDeleteBillSongConfirmDialog(final LocalSongEntity songEntity) {
        DialogUtil.showDialog(getViewContext(),
                "《" + songEntity.getTitle() + "》",
                "是否将它从歌单中移除?",
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mBillDetailPresenter.onBottomSheetDeleteConfirmed(songEntity);
                    }
                }, null, null
        );
    }

    @Override
    public void showClearBillSongsConfirmDialog() {
        DialogUtil.showDialog(getViewContext(),
                "提示",
                "确定移除歌单中的所有歌曲?",
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mBillDetailPresenter.onClearBillConfirmed();
                    }
                }, null, null
        );
    }

    @Override
    public void showDeleteBillConfirmDialog() {
        DialogUtil.showDialog(getViewContext(),
                "提示",
                "确定删除歌单?",
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mBillDetailPresenter.onDeleteBillMenuItemConfirmed();
                    }
                }, null, null
        );
    }

    @Override
    public void showDeleteBillSuccessfully() {
        finish();
    }

    @Override
    public void showBillSelectionDialog(final LocalSongEntity songToBeAdd) {
        final LocalBillSelectionDialogFragment billSelectionDialogFragment = new LocalBillSelectionDialogFragment();
        billSelectionDialogFragment.setCallback(new LocalBillSelectionDialogFragment.BillSelectionCallback() {
            @Override
            public void onBillSelected(LocalBillEntity billEntity) {
                mBillDetailPresenter.onBottomSheetAddToBillConfirmed(billEntity, songToBeAdd);
                billSelectionDialogFragment.dismissAllowingStateLoss();
            }
        });
        billSelectionDialogFragment.show(getSupportFragmentManager(), "");

    }


}
