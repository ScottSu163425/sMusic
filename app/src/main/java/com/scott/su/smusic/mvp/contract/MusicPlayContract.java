package com.scott.su.smusic.mvp.contract;

import android.graphics.Bitmap;

import com.scott.su.smusic.callback.MusicPlayMainFragmentCallback;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

/**
 * Created by asus on 2016/12/14.
 */

public interface MusicPlayContract {

    interface MusicPlayView extends IBaseView {
        void showBillSelectionDialog(LocalSongEntity songEntity);

        LocalSongEntity getCurrentPlayingSong();

        void loadBlurCover(Bitmap bitmap);

        void showMusicPlayMainFragment();

        void hideMusicPlayMainFragment();

        void showMusicPlaySecondFragment();

        void hideMusicPlaySecondFragment();
    }

    interface MusicPlayPresenter extends IPresenter<MusicPlayView>, MusicPlayMainFragmentCallback {
        void onAddToBillMenuItemClick();

        void onAddToBillConfirmed(LocalBillEntity billEntity, LocalSongEntity songEntity);
    }


}
