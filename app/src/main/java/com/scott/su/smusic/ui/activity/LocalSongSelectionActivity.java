package com.scott.su.smusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scott.su.smusic.R;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.mvp.presenter.LocalSongSelectionPresenter;
import com.scott.su.smusic.mvp.presenter.impl.LocalSongSelectionPresenterImp;
import com.scott.su.smusic.mvp.view.LocalSongSelectionView;
import com.scott.su.smusic.ui.fragment.LocalSongSlectionDisplayFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.SdkUtil;

/**
 * 2016-8-27
 */
public class LocalSongSelectionActivity extends BaseActivity implements LocalSongSelectionView {
    private LinearLayout mRootLayout;
    private Button mFinishSelectionButton;
    private LocalSongSelectionPresenter mSongSelectionPresenter;
    private LocalSongSlectionDisplayFragment mLocalSongSlectionDisplayFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_song_selection);

        mSongSelectionPresenter = new LocalSongSelectionPresenterImp(this);
        mSongSelectionPresenter.onViewFirstTimeCreated();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done_all_local_song_selection) {
            mSongSelectionPresenter.onSelectAllClick();
        }
        return true;
    }

    @Override
    public void initPreData() {
        if (SdkUtil.isLolipopOrLatter()){
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_right));
        }
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_local_song_selection);
        toolbar.setTitle(getResources().getString(R.string.toolbar_title_local_song_selection));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalSongSelectionActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void initView() {
        mRootLayout = (LinearLayout) findViewById(R.id.ll_root_local_song_selection);
        mFinishSelectionButton = (Button) findViewById(R.id.btn_finish_selection_local_song_selection);
    }

    @Override
    public void initData() {
        mLocalSongSlectionDisplayFragment = new LocalSongSlectionDisplayFragment();
        mLocalSongSlectionDisplayFragment.setOnSelectedSongChangedListener(new LocalSongSlectionDisplayFragment.OnSelectedSongChangedListener() {
            @Override
            public void onSelectedCountChanged(boolean isEmpty) {
                mSongSelectionPresenter.onSelectedCountChanged(isEmpty);
            }
        });

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fl_container_display_local_song_selection, mLocalSongSlectionDisplayFragment)
                .commit();
    }

    @Override
    public void initListener() {
        mFinishSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSongSelectionPresenter.onFinishSelectionClick();
            }
        });
    }

    @Override
    public void selectAll() {
        mLocalSongSlectionDisplayFragment.selectAll();
    }

    @Override
    public void finishSelection() {
        //Put all selected song entities to the result;
        Intent intent = new Intent();
        Bundle data = new Bundle();
        data.putParcelable(Constants.KEY_EXTRA_BILL, getIntent().getParcelableExtra(Constants.KEY_EXTRA_BILL));
        data.putParcelableArrayList(Constants.KEY_EXTRA_LOCAL_SONGS, mLocalSongSlectionDisplayFragment.getSelectedSongs());
        intent.setExtrasClassLoader(LocalBillEntity.class.getClassLoader());
        intent.putExtras(data);
        setResult(RESULT_OK, intent);
//        finish();
        this.onBackPressed();
    }

    @Override
    public void showOrHideFinishSelectionButtn(boolean isShow) {
//        if (SdkUtil.isLolipopOrLatter()) {
//            TransitionManager.beginDelayedTransition(mRootLayout);
//        }
//
//        if (isShow) {
//            ViewUtil.setViewVisiable(mFinishSelectionButton);
//        } else {
//            ViewUtil.setViewGone(mFinishSelectionButton);
//        }
        mFinishSelectionButton.setEnabled(isShow);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //If the local song list is empty, hide the menu;
        menu.findItem(R.id.action_done_all_local_song_selection)
                .setVisible(mLocalSongSlectionDisplayFragment.getSelectedSongs().size() == 0);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_song_selection, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mSongSelectionPresenter!=null){
            mSongSelectionPresenter.onViewWillDestroy();
        }
        super.onDestroy();
    }
}
