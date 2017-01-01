package com.scott.su.smusic.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.callback.LocalSongDisplayCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.LocalBillDetailContract;
import com.scott.su.smusic.mvp.presenter.impl.LocalBillDetailPresenterImpl;
import com.scott.su.smusic.ui.fragment.LocalBillSongDisplayFragment;
import com.scott.su.smusic.ui.fragment.CommonInputDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetMenuFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.CirclarRevealUtil;
import com.su.scott.slibrary.util.DialogUtil;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * 2016-8-28
 */
public class LocalBillDetailActivity extends BaseActivity<LocalBillDetailContract.LocalBillDetailView, LocalBillDetailContract.ILocalBillDetailPresenter>
        implements LocalBillDetailContract.LocalBillDetailView {
    private LocalBillDetailContract.ILocalBillDetailPresenter mBillDetailPresenter;
    private LocalBillEntity mBillEntity;
    private AppBarLayout mAppBarLayout;
    private ImageView mCoverImageView;
    private LocalBillSongDisplayFragment mLocalBillSongDisplayFragment;
    private FloatingActionButton mPlayFAB;
    private CommonInputDialogFragment mEditDialogFragment;

    private static final int REQUESt_CODE_LOCAL_SONG_SELECTION = 123;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_bill_detail);

        mBillDetailPresenter.onViewFirstTimeCreated();

        if (SdkUtil.isLolipopOrLatter()) {
            if (getWindow().getSharedElementEnterTransition() != null) {
                getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        mBillDetailPresenter.onTransitionEnd();
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
            } else {
                mBillDetailPresenter.onTransitionEnd();
            }
        } else {
            mBillDetailPresenter.onTransitionEnd();
        }
    }

    @Override
    protected LocalBillDetailContract.ILocalBillDetailPresenter getPresenter() {
        if (mBillDetailPresenter == null) {
            mBillDetailPresenter = new LocalBillDetailPresenterImpl(this);
        }
        return mBillDetailPresenter;
    }

    @Override
    public void initPreData() {
        mBillEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_BILL);
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_local_bill_detail);
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
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_local_bill_detail);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_bill_detail);
        mCoverImageView = (ImageView) findViewById(R.id.iv_cover_local_bill_detail);
        mPlayFAB = (FloatingActionButton) findViewById(R.id.fab_local_bill_detail);

        if (mBillEntity.isBillEmpty()) {
            ViewUtil.setViewGone(mPlayFAB);
        }
    }

    @Override
    public void initData() {
        mLocalBillSongDisplayFragment = LocalBillSongDisplayFragment.newInstance(mBillEntity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_display_songs_local_bill_detail, mLocalBillSongDisplayFragment)
                .commitNow();
    }

    @Override
    public void initListener() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            private int lastVerticalOffset = 0;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                int scrollDistance = verticalOffset - lastVerticalOffset;

                if (scrollDistance > 0) {
                    //Scrolling down.
                    if (Math.abs(verticalOffset) < (totalScrollRange / 3)) {
                        if (!mLocalBillSongDisplayFragment.getDisplayDataList().isEmpty()) {
                            showFab();
                        }
                    }
                } else {
                    //Scrolling up.
                    if (!mLocalBillSongDisplayFragment.getDisplayDataList().isEmpty()) {
                        if (Math.abs(verticalOffset) > (totalScrollRange / 3)) {
                            hideFab();
                        }
                    }
                }

                lastVerticalOffset = verticalOffset;
            }
        });

        mLocalBillSongDisplayFragment.setDisplayCallback(new LocalSongDisplayCallback() {

            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mBillDetailPresenter.onBillSongItemClick(itemView, position, entity);
            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                mBillDetailPresenter.onBillSongItemMoreClick(view, position, entity);
            }

            @Override
            public void onDataLoading() {

            }

            @Override
            public void onDisplayDataChanged(List<LocalSongEntity> dataList) {

            }
        });

        mPlayFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBillDetailPresenter.onPlayFabClick();
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
        if (id == R.id.action_edit_local_song_bill_detaile) {
            mBillDetailPresenter.onEditBillMenuItemClick();
        } else if (id == R.id.action_add_local_song_bill_detaile) {
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
    public ImageView getCoverImageView() {
        return mCoverImageView;
    }

    @Override
    public void onEnterBillEmpty() {
        showSnackbarShort(getString(R.string.ask_add_song_to_empty_bill), getString(R.string.ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LocalBillDetailActivity.this, LocalSongSelectionActivity.class);
                        intent.putExtra(Constants.KEY_EXTRA_BILL, mBillEntity);
                        goToForResultWithTransition(intent, REQUESt_CODE_LOCAL_SONG_SELECTION);
                    }
                });
    }

    @Override
    public void setBillEntity(LocalBillEntity billEntity) {
        this.mBillEntity = billEntity;
    }

    @Override
    public void loadCover(final String coverPath, boolean needReveal) {
        if (needReveal) {
            CirclarRevealUtil.revealOut(mCoverImageView,
                    CirclarRevealUtil.DIRECTION.CENTER,
                    CirclarRevealUtil.DURATION_REVEAL_SHORT,
                    new DecelerateInterpolator(),
                    new AnimUtil.SimpleAnimListener() {
                        @Override
                        public void onAnimStart() {

                        }

                        @Override
                        public void onAnimEnd() {
                            ImageLoader.load(LocalBillDetailActivity.this,
                                    coverPath,
                                    mCoverImageView,
                                    R.color.place_holder_loading,
                                    R.drawable.ic_cover_default_song_bill
                            );
                            CirclarRevealUtil.revealIn(mCoverImageView, CirclarRevealUtil.DIRECTION.CENTER);
                        }
                    }, false);

        } else {
            ImageLoader.load(LocalBillDetailActivity.this,
                    coverPath,
                    mCoverImageView,
                    R.color.place_holder_loading,
                    R.drawable.ic_cover_default_song_bill
            );
        }

    }

    @Override
    public void showFab() {
        if (!ViewUtil.isViewVisiable(mPlayFAB)) {
            AnimUtil.alphaIn(mPlayFAB, AnimUtil.DURATION_SHORT).start();
        }
    }

    @Override
    public void hideFab() {
        if (ViewUtil.isViewVisiable(mPlayFAB)) {
            AnimUtil.alphaOut(mPlayFAB, AnimUtil.DURATION_SHORT);
        }
    }

    @Override
    public void goToLocalSongSelectionActivity() {
        Intent intent = new Intent(LocalBillDetailActivity.this, LocalSongSelectionActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_BILL, mBillEntity);
        goToForResultWithTransition(intent, REQUESt_CODE_LOCAL_SONG_SELECTION);
    }

    @Override
    public void onAddSongsToBillSuccessfully() {
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
        mLocalBillSongDisplayFragment.setSongBillEntity(billEntity);
        mLocalBillSongDisplayFragment.reInitialize();
    }

    @Override
    public void showBillSongBottomSheet(LocalSongEntity songEntity) {
        LocalSongBottomSheetMenuFragment.newInstance()
                .setLocalSongEntity(songEntity)
                .setMenuClickCallback(new LocalSongBottomSheetCallback() {
                    @Override
                    public void onAddToBillClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetAddToBillClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onAlbumClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetAlbumClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onDeleteClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mBillDetailPresenter.onBottomSheetDeleteClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                })
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public void showClearBillSongsConfirmDialog() {
        DialogUtil.showDialog(getViewContext(),
                getString(R.string.tip),
                getString(R.string.ask_clear_bill),
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
                getString(R.string.tip),
                getString(R.string.ask_delete_bill),
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
    public void onDeleteBillSuccessfully() {
        finish();
    }


    @Override
    public void goToMusicPlayWithCoverSharedElement(LocalSongEntity songEntity) {
        Intent intent = new Intent(LocalBillDetailActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mLocalBillSongDisplayFragment.getDisplayDataList());

        goToWithSharedElement(intent, mCoverImageView, getString(R.string.transition_name_cover));
    }

    @Override
    public void goToMusicPlayWithCoverAndFabSharedElement(LocalSongEntity entity) {
        Intent intent = new Intent(LocalBillDetailActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, entity);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mLocalBillSongDisplayFragment.getDisplayDataList());

        goToWithSharedElements(intent,
                new View[]{mPlayFAB, mCoverImageView},
                new String[]{getString(R.string.transition_name_fab), getString(R.string.transition_name_cover)});

    }

    @Override
    public void goToMusicPlayWithFab(LocalSongEntity songEntity) {
        Intent intent = new Intent(LocalBillDetailActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mLocalBillSongDisplayFragment.getDisplayDataList());

        goToWithSharedElement(intent, mPlayFAB, getString(R.string.transition_name_fab));
    }

    @Override
    public void showEditBillNameDialog() {
        mEditDialogFragment = new CommonInputDialogFragment();
        mEditDialogFragment.setTitle(getString(R.string.edit_bill));
        mEditDialogFragment.setHint(getString(R.string.bill_name));
        mEditDialogFragment.setCallback(new CommonInputDialogFragment.CommonInputDialogCallback() {
            @Override
            public void onConfirmClick(String text) {
                mBillDetailPresenter.onEditBillNameConfiemed(text);
            }
        });
        if (!mEditDialogFragment.isVisible()) {
            mEditDialogFragment.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void dismissEditBillNameDialog() {
        if (mEditDialogFragment != null && mEditDialogFragment.isVisible()) {
            mEditDialogFragment.dismiss();
        }
    }

    @Override
    public void updateBillInfo() {
        mCollapsingToolbarLayout.setTitle(mBillEntity.getBillTitle());
    }

    @Override
    public boolean isFabVisiable() {
        return ViewUtil.isViewVisiable(mPlayFAB);
    }

    @Override
    public void goToMusicPlayWithoutFab(LocalSongEntity songEntity) {
        Intent intent = new Intent(LocalBillDetailActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mLocalBillSongDisplayFragment.getDisplayDataList());

        goToWithTransition(intent);
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

    @Override
    public void goToAlbumDetail(LocalAlbumEntity entity) {
        Intent intent = new Intent(LocalBillDetailActivity.this, LocalAlbumDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_ALBUM, entity);
        goToWithTransition(intent);
    }

    @Override
    public void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(LocalBillDetailActivity.this, LocalAlbumDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_ALBUM, entity);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void showDeleteDialog(final LocalSongEntity songEntity) {
        DialogUtil.showDialog(getViewContext(),
                "《" + songEntity.getTitle() + "》",
                getString(R.string.ask_remove_from_bill),
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mBillDetailPresenter.onBottomSheetDeleteConfirmed(songEntity);
                    }
                }, null, null);
    }


}
