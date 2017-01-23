package com.scott.su.smusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.callback.LocalSongDisplayCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.event.LocalBillChangedEvent;
import com.scott.su.smusic.mvp.contract.LocalAlbumDetailContract;
import com.scott.su.smusic.mvp.presenter.impl.LocalAlbumDetailPresenterImpl;
import com.scott.su.smusic.ui.fragment.LocalAlbumSongDisplayFragment;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetMenuFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.SdkUtil;

import java.util.List;

public class LocalAlbumDetailActivity extends BaseActivity<LocalAlbumDetailContract.LocalAlbumDetailView, LocalAlbumDetailContract.ILocalAlbumDetailPresenter>
        implements LocalAlbumDetailContract.LocalAlbumDetailView {

    private LocalAlbumDetailContract.ILocalAlbumDetailPresenter mPresenter;
    private LocalAlbumEntity mAlbumEntity;
    private CardView mAlbumInfoCard;
    private ImageView mAlbumCoverImageView;
    private TextView mAlbumTitleTextView, mAlbumArtistTextView, mAlbumCountTextView;
    private LocalAlbumSongDisplayFragment mSongDisplayFragment;


    @Override
    protected int getContentLayoutResId() {
        return R.layout.activity_local_album_detail;
    }

    @Override
    protected LocalAlbumDetailContract.ILocalAlbumDetailPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new LocalAlbumDetailPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mPresenter.onViewFirstTimeCreated();
    }

    @Override
    public void initPreData() {
        mAlbumEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_ALBUM);

        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_right));
        }
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_local_album_detail);
        toolbar.setTitle(mAlbumEntity.getAlbumTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalAlbumDetailActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
        mAlbumInfoCard = (CardView) findViewById(R.id.card_info_local_album_detail);
        mAlbumCoverImageView = (ImageView) findViewById(R.id.iv_cover_local_album_detail);
        mAlbumTitleTextView = (TextView) findViewById(R.id.tv_title_local_album_detail);
        mAlbumArtistTextView = (TextView) findViewById(R.id.tv_artist_local_album_detail);
        mAlbumCountTextView = (TextView) findViewById(R.id.tv_count_local_album_detail);

        mAlbumTitleTextView.setText(mAlbumEntity.getAlbumTitle());
        mAlbumArtistTextView.setText(mAlbumEntity.getArtist());
        mAlbumCountTextView.setText(mAlbumEntity.getAlbumSongs().size() + " " + getString(R.string.unit_song));
    }

    @Override
    public void initData() {
        mSongDisplayFragment = LocalAlbumSongDisplayFragment.newInstance(mAlbumEntity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_display_songs_local_album_detail, mSongDisplayFragment)
                .commitNow();
    }

    @Override
    public void initListener() {
        mSongDisplayFragment.setDisplayCallback(new LocalSongDisplayCallback() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                mPresenter.onAlbumSongItemClick(itemView, position, entity);
            }

            @Override
            public void onItemMoreClick(View view, int position, LocalSongEntity entity) {
                mPresenter.onAlbumSongItemMoreClick(view, position, entity);
            }

            @Override
            public void onDataLoading() {

            }

            @Override
            public void onDisplayDataChanged(List<LocalSongEntity> dataList) {

            }
        });
    }

    @Override
    public LocalAlbumEntity getCurrentAlbumEntity() {
        return mAlbumEntity;
    }

    @Override
    public void loadAlbumCover(String path) {
        ImageLoader.load(this,
                path,
                mAlbumCoverImageView,
                R.color.place_holder_loading,
                R.drawable.ic_cover_default_song_bill
        );
    }

    @Override
    public void goToMusicPlay(LocalSongEntity songEntity) {
        Intent intent = new Intent(LocalAlbumDetailActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mSongDisplayFragment.getDisplayDataList());
    }

    @Override
    public void goToMusicPlayWithCover(LocalSongEntity songEntity) {
        Intent intent = new Intent(LocalAlbumDetailActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, songEntity);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONGS, mSongDisplayFragment.getDisplayDataList());
        goToWithSharedElement(intent, mAlbumCoverImageView, getString(R.string.transition_name_cover));
    }

    @Override
    public void showAlbumSongBottomSheet(LocalSongEntity songEntity) {
        LocalSongBottomSheetMenuFragment.newInstance()
                .setLocalSongEntity(songEntity)
                .setMenuClickCallback(new LocalSongBottomSheetCallback() {
                    @Override
                    public void onAddToBillClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mPresenter.onBottomSheetAddToBillClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onAlbumClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mPresenter.onBottomSheetAlbumClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onDeleteClick(LocalSongBottomSheetMenuFragment fragment, LocalSongEntity songEntity) {
                        mPresenter.onBottomSheetDeleteClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                })
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public void notifyLocalBillChanged() {
        postEvent(new LocalBillChangedEvent());
    }

    @Override
    public void showBillSelectionDialog(final LocalSongEntity songToBeAdd) {
        final LocalBillSelectionDialogFragment billSelectionDialogFragment = new LocalBillSelectionDialogFragment();
        billSelectionDialogFragment.setCallback(new LocalBillSelectionDialogFragment.BillSelectionCallback() {
            @Override
            public void onBillSelected(LocalBillEntity billEntity) {
                mPresenter.onBottomSheetAddToBillConfirmed(billEntity, songToBeAdd);
                billSelectionDialogFragment.dismissAllowingStateLoss();
            }
        });
        billSelectionDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void goToAlbumDetail(LocalAlbumEntity entity) {

    }

    @Override
    public void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName) {

    }

    @Override
    public void showDeleteDialog(LocalSongEntity songEntity) {

    }


}
