package com.scott.su.smusic.mvp.presenter.impl;

import android.text.TextUtils;
import android.view.View;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.contract.SearchContract;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalBillModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.su.scott.slibrary.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/27.
 */

public class SearchPresenterImpl extends BasePresenter<SearchContract.SearchView>
        implements SearchContract.SearchPresenterI {
    private LocalSongModel mSongModel;
    private LocalBillModel mBillModel;
    private LocalAlbumModel mAlbumModel;

    public SearchPresenterImpl(SearchContract.SearchView view) {
       super(view);
        this.mSongModel = new LocalSongModelImpl();
        this.mBillModel = new LocalBillModelImpl();
        this.mAlbumModel = new LocalAlbumModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        getView().initPreData();
        getView().initToolbar();
        getView().initView();
        getView().initData();
        getView().initListener();

        if (AppConfig.isNeedToRefreshSearchResult(getView().getViewContext())) {
            AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), false);
        }
    }

    @Override
    public void onViewResume() {
        //To update the result,when back to the search activity  after user click result and go to other activity,
        //and made some changes.
        if (!TextUtils.isEmpty(getView().getCurrentKeyword())) {
            if (AppConfig.isNeedToRefreshSearchResult(getView().getViewContext())) {
                searchAndSetResult(getView().getCurrentKeyword());
                AppConfig.setNeedToRefreshSearchResult(getView().getViewContext(), false);
            }
        }
    }

    @Override
    public void onViewWillDestroy() {

    }

    @Override
    public void onSearchTextChanged(final String keyword) {
        searchAndSetResult(keyword);
    }

    private void searchAndSetResult(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
//            getView().showSnackbarShort(getView().getSnackbarParent(), getView().getViewContext().getString(R.string.empty_keyword));
            getView().setResult(Collections.EMPTY_LIST);
            getView().showEmpty();
            return;
        }

        getView().showLoading();
        Observable.just(keyword)
                .map(new Func1<String, List[]>() {
                    @Override
                    public List[] call(String s) {
                        List<LocalSongEntity> localSongEntities = mSongModel.searchLocalSong(getView().getViewContext(), keyword);
                        List<LocalBillEntity> localBillEntities = mBillModel.searchBill(getView().getViewContext(), keyword);
                        List<LocalAlbumEntity> localAlbumEntities = mAlbumModel.searchLocalAlbum(getView().getViewContext(), keyword);
                        List[] result = new List[3];
                        result[0] = localSongEntities;
                        result[1] = localBillEntities;
                        result[2] = localAlbumEntities;
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List[]>() {
                    @Override
                    public void call(List[] lists) {
                        if (!isViewAttaching()){
                            return;
                        }

                        List<LocalSongEntity> localSongEntities = lists[0];
                        List<LocalBillEntity> localBillEntities = lists[1];
                        List<LocalAlbumEntity> localAlbumEntities = lists[2];
                        List result = new ArrayList();

                        if (!localSongEntities.isEmpty()) {
                            result.add(getView().getViewContext().getString(R.string.local_music));
                            result.addAll(localSongEntities);
                        }

                        if (!localBillEntities.isEmpty()) {
                            result.add(getView().getViewContext().getString(R.string.local_bill));
                            result.addAll(localBillEntities);
                        }

                        if (!localAlbumEntities.isEmpty()) {
                            result.add(getView().getViewContext().getString(R.string.album));
                            result.addAll(localAlbumEntities);
                        }

                        getView().setResult(result);

                        if (result.isEmpty()) {
                            getView().showEmpty();
                        } else {
                            getView().showResult();
                        }
                    }
                });
    }

    @Override
    public void onLocalSongClick(LocalSongEntity entity, View sharedElement, String transitionName) {
        getView().goToMusicWithSharedElement(entity, sharedElement, transitionName);
    }

    @Override
    public void onLocalSongMoreClick(LocalSongEntity entity) {
        getView().showLocalSongBottomSheet(entity);
    }

    @Override
    public void onLocalBillClick(LocalBillEntity entity, View sharedElement, String transitionName) {
        getView().goToBillDetailWithSharedElement(entity, sharedElement, transitionName);
    }

    @Override
    public void onLocalAlbumClick(LocalAlbumEntity entity, View sharedElement, String transitionName) {
        getView().goToAlbumDetailWithSharedElement(entity, sharedElement, transitionName);
    }

    @Override
    public void onBottomSheetAddToBillClick(LocalSongEntity songEntity) {
        getView().showBillSelectionDialog(songEntity);
    }

    @Override
    public void onBottomSheetAlbumClick(LocalSongEntity songEntity) {
        getView().goToAlbumDetail(mAlbumModel.getLocalAlbum(getView().getViewContext(), songEntity.getAlbumId()));
    }

    @Override
    public void onBottomSheetDeleteClick(LocalSongEntity songEntity) {

    }

    @Override
    public void onBottomSheetAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity) {
        if (mBillModel.isBillContainsSong(billEntity, songEntity)) {
            getView().showSnackbarShort( getView().getViewContext().getString(R.string.already_exist_in_bill));
            return;
        }

        mBillModel.addSongToBill(getView().getViewContext(), songEntity, billEntity);
        AppConfig.setNeedToRefreshLocalBillDisplay(getView().getViewContext(), true);
        searchAndSetResult(getView().getCurrentKeyword());
        getView().showSnackbarShort(  getView().getViewContext().getString(R.string.add_successfully));
    }

    @Override
    public void onBottomSheetDeleteConfirmed(LocalSongEntity songEntity) {

    }
}
