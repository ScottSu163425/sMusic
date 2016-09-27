package com.scott.su.smusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.SearchResultAdapter;
import com.scott.su.smusic.constant.Constants;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.SearchPresenter;
import com.scott.su.smusic.mvp.presenter.impl.SearchPresenterImpl;
import com.scott.su.smusic.mvp.view.SearchView;
import com.su.scott.slibrary.activity.BaseActivity;
import com.su.scott.slibrary.util.SdkUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchView {
    private SearchPresenter mSearchPresenter;
    private EditText mEditText;
    private AppCompatImageButton mSearchButton;
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
    public View getSnackbarParent() {
        return mSearchButton;
    }

    @Override
    public void initPreData() {
        if (SdkUtil.isLolipopOrLatter()) {
            getWindow().setEnterTransition(new Explode());
        }
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView() {
        mEditText = (EditText) findViewById(R.id.et_input_search);
        mSearchButton = (AppCompatImageButton) findViewById(R.id.btn_search_search);
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
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List result = new ArrayList();
                List<LocalAlbumEntity> localAlbumEntities = new LocalAlbumModelImpl().searchLocalAlbum(SearchActivity.this, mEditText.getText().toString().trim());
                List<LocalSongEntity> localSongEntities = new LocalSongModelImpl().searchLocalSong(SearchActivity.this, mEditText.getText().toString().trim());
                List<LocalBillEntity> localBillEntities = new LocalBillModelImpl().searchBill(SearchActivity.this, mEditText.getText().toString().trim());

                if (!localAlbumEntities.isEmpty()) {
                    result.add(getString(R.string.album));
                    result.addAll(localAlbumEntities);
                }

                if (!localSongEntities.isEmpty()) {
                    result.add(getString(R.string.local_music));
                    result.addAll(localSongEntities);
                }

                if (!localBillEntities.isEmpty()) {
                    result.add(getString(R.string.local_bill));
                    result.addAll(localBillEntities);
                }
                mResultAdapter.setResult(result);
                mResultAdapter.notifyDataSetChanged();
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
}
