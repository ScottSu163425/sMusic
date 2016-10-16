package com.scott.su.smusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.EditText;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.SearchResultAdapter;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.presenter.SearchPresenter;
import com.scott.su.smusic.mvp.presenter.impl.SearchPresenterImpl;
import com.scott.su.smusic.mvp.view.SearchView;
import com.scott.su.smusic.ui.fragment.LocalBillSelectionDialogFragment;
import com.scott.su.smusic.ui.fragment.LocalSongBottomSheetFragment;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.SdkUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchView {
    private SearchPresenter mSearchPresenter;
    private View mLoadingLayout, mEmptyLayout;
    private EditText mInputEditText;
    private AppCompatImageButton mSpeakButton, mBackButton;
    private RecyclerView mResultRecyclerView;
    private SearchResultAdapter mResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchPresenter = new SearchPresenterImpl(this);
        mSearchPresenter.onViewFirstTimeCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSearchPresenter.onViewResume();
    }

    @Override
    public View getSnackbarParent() {
        return mSpeakButton;
    }

    @Override
    public void initPreData() {
        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_right));
        }
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView() {
        mLoadingLayout = findViewById(R.id.layout_loading_search);
        mEmptyLayout = findViewById(R.id.layout_empty_search);
        mInputEditText = (EditText) findViewById(R.id.et_input_search);
        mSpeakButton = (AppCompatImageButton) findViewById(R.id.btn_search_search);
        mBackButton = (AppCompatImageButton) findViewById(R.id.btn_back_search);
        mResultRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_result_search);
    }

    @Override
    public void initData() {
        mResultAdapter = new SearchResultAdapter(this);
        mResultRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mResultRecyclerView.setAdapter(mResultAdapter);
    }

    @Override
    public void initListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.onBackPressed();
            }
        });

        mSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchPresenter.onSearchTextChanged(editable.toString().trim());
            }
        });


        mResultAdapter.setOnSearchResultClickListener(new SearchResultAdapter.OnSearchResultClickListener() {
            @Override
            public void onLocalSongClick(LocalSongEntity entity, View sharedElement, String transitionName) {
                mSearchPresenter.onLocalSongClick(entity, sharedElement, transitionName);
            }

            @Override
            public void onLocalSongMoreClick(LocalSongEntity entity) {
                mSearchPresenter.onLocalSongMoreClick(entity);
            }

            @Override
            public void onLocalBillClick(LocalBillEntity entity, View sharedElement, String transitionName) {
                mSearchPresenter.onLocalBillClick(entity, sharedElement, transitionName);
            }

            @Override
            public void onLocalAlbumClick(LocalAlbumEntity entity, View sharedElement, String transitionName) {
                mSearchPresenter.onLocalAlbumClick(entity, sharedElement, transitionName);
            }
        });
    }

    @Override
    public String getCurrentKeyword() {
        return mInputEditText.getText().toString().trim();
    }

    @Override
    public void showLoading() {
        ViewUtil.setViewVisiable(mLoadingLayout);
        ViewUtil.setViewGone(mEmptyLayout);
        ViewUtil.setViewGone(mResultRecyclerView);
    }

    @Override
    public void setResult(List result) {
        mResultAdapter.setResult(result);
        mResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void showResult() {
        ViewUtil.setViewVisiable(mResultRecyclerView);
        ViewUtil.setViewGone(mLoadingLayout);
        ViewUtil.setViewGone(mEmptyLayout);
//        CirclarRevealUtil.revealIn(mResultRecyclerView, CirclarRevealUtil.DIRECTION.CENTER_TOP);
    }

    @Override
    public void showEmpty() {
        ViewUtil.setViewVisiable(mEmptyLayout);
        ViewUtil.setViewGone(mLoadingLayout);
        ViewUtil.setViewGone(mResultRecyclerView);
    }

    @Override
    public void goToMusicWithSharedElement(LocalSongEntity entity, View sharedElement, String transitionName) {
        ArrayList<LocalSongEntity> songEntities = new ArrayList<>();
        songEntities.add(entity);

        Intent intent = new Intent(SearchActivity.this, MusicPlayActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_LOCAL_SONG, entity);
        intent.putParcelableArrayListExtra(Constants.KEY_EXTRA_LOCAL_SONGS, songEntities);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void goToBillDetailWithSharedElement(LocalBillEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(SearchActivity.this, LocalBillDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_BILL, entity);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void showBillSelectionDialog(final LocalSongEntity songToBeAdd) {
        final LocalBillSelectionDialogFragment billSelectionDialogFragment = new LocalBillSelectionDialogFragment();
        billSelectionDialogFragment.setCallback(new LocalBillSelectionDialogFragment.BillSelectionCallback() {
            @Override
            public void onBillSelected(LocalBillEntity billEntity) {
                mSearchPresenter.onBottomSheetAddToBillConfirmed(billEntity, songToBeAdd);
                billSelectionDialogFragment.dismissAllowingStateLoss();
            }
        });
        billSelectionDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void goToAlbumDetail(LocalAlbumEntity entity) {
        Intent intent = new Intent(SearchActivity.this, LocalAlbumDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_ALBUM, entity);
        goTo(intent);
    }

    @Override
    public void goToAlbumDetailWithSharedElement(LocalAlbumEntity entity, View sharedElement, String transitionName) {
        Intent intent = new Intent(SearchActivity.this, LocalAlbumDetailActivity.class);
        intent.putExtra(Constants.KEY_EXTRA_ALBUM, entity);
        goToWithSharedElement(intent, sharedElement, transitionName);
    }

    @Override
    public void showDeleteDialog(LocalSongEntity songEntity) {

    }

    @Override
    public void showLocalSongBottomSheet(LocalSongEntity songEntity) {
        LocalSongBottomSheetFragment.newInstance()
                .setLocalSongEntity(songEntity)
                .setMenuClickCallback(new LocalSongBottomSheetCallback() {
                    @Override
                    public void onAddToBillClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity) {
                        mSearchPresenter.onBottomSheetAddToBillClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onAlbumClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity) {
                        mSearchPresenter.onBottomSheetAlbumClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                    @Override
                    public void onDeleteClick(LocalSongBottomSheetFragment fragment, LocalSongEntity songEntity) {
                        mSearchPresenter.onBottomSheetDeleteClick(songEntity);
                        fragment.dismissAllowingStateLoss();
                    }

                })
                .show(getSupportFragmentManager(), "");
    }

}
