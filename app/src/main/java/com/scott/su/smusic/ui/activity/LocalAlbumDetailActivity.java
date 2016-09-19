package com.scott.su.smusic.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.constant.LocalSongDisplayStyle;
import com.scott.su.smusic.constant.LocalSongDisplayType;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.mvp.presenter.LocalAlbumDetailPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalAlbumDetailPresenterImpl;
import com.scott.su.smusic.mvp.view.LocalAlbumDetailView;
import com.scott.su.smusic.ui.fragment.LocalSongDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.SdkUtil;

public class LocalAlbumDetailActivity extends BaseActivity implements LocalAlbumDetailView {
    private LocalAlbumDetailPresenter mPresenter;
    private LocalAlbumEntity mAlbumEntity;
    private CardView mAlbumInfoCard;
    private LocalSongDisplayFragment mSongDisplayFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album_detail);

        mPresenter=new LocalAlbumDetailPresenterImpl(this);
        mPresenter.onViewFirstTimeCreated();
    }

    @Override
    public View getSnackbarParent() {
        return mAlbumInfoCard;
    }

    @Override
    public void initPreData() {
        mAlbumEntity = getIntent().getParcelableExtra(Constants.KEY_EXTRA_ALBUM);

        if (SdkUtil.isLolipopOrLatter()){
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_right));
        }
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_local_album_detail);
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
    }

    @Override
    public void initData() {
        mSongDisplayFragment = LocalSongDisplayFragment.newInstance(LocalSongDisplayType.Album, mAlbumEntity, LocalSongDisplayStyle.NumberDivider);

        mSongDisplayFragment.setSwipeRefreshEnable(false);
        mSongDisplayFragment.setLoadMoreEnable(false);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_display_songs_local_album_detail, mSongDisplayFragment)
                .commit();
    }

    @Override
    public void initListener() {

    }
}
