package com.scott.su.smusic.mvp.presenter.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.scott.su.smusic.mvp.model.LocalSongModel;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.scott.su.smusic.mvp.presenter.MusicPlayPresenter;
import com.scott.su.smusic.mvp.view.MusicPlayView;
import com.su.scott.slibrary.util.BlurUtil;

/**
 * Created by asus on 2016/9/4.
 */
public class MusicPlayPresenterImpl implements MusicPlayPresenter {
    private MusicPlayView mMusicPlayView;
    private LocalSongModel mLocalSongModel;


    public MusicPlayPresenterImpl(MusicPlayView mMusicPlayView) {
        this.mMusicPlayView = mMusicPlayView;
        this.mLocalSongModel = new LocalSongModelImpl();
    }

    @Override
    public void onViewFirstTimeCreated() {
        mMusicPlayView.initPreData();
        mMusicPlayView.initToolbar();
        mMusicPlayView.initView();
        mMusicPlayView.initData();
        mMusicPlayView.initListener();

        String path = mLocalSongModel.getAlbumCoverPath(mMusicPlayView.getViewContext(), mMusicPlayView.getCurrentPlayingLocalSongEntity().getAlbumId());
        mMusicPlayView.loadCover(path);

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                String path = mLocalSongModel.getAlbumCoverPath(mMusicPlayView.getViewContext(), mMusicPlayView.getCurrentPlayingLocalSongEntity().getAlbumId());
                return BlurUtil.blur(BitmapFactory.decodeFile(path));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                mMusicPlayView.loadBlurCover(bitmap);
            }
        }.execute();

    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewWillDestroy() {

    }
}
