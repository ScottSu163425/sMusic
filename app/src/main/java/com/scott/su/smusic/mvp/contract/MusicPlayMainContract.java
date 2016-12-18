package com.scott.su.smusic.mvp.contract;

import android.graphics.Bitmap;
import android.view.View;

import com.scott.su.smusic.callback.MusicPlayMainFragmentCallback;
import com.scott.su.smusic.callback.MusicPlayServiceCallback;
import com.scott.su.smusic.callback.PlayListBottomSheetCallback;
import com.scott.su.smusic.constant.PlayMode;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.view.MusicPlayServiceView;
import com.su.scott.slibrary.mvp.presenter.IPresenter;
import com.su.scott.slibrary.mvp.view.IBaseView;

import java.util.List;

/**
 * Created by asus on 2016/12/14.
 */

public interface MusicPlayMainContract {

    interface MusicPlayMainView extends IBaseView, MusicPlayServiceView, View.OnClickListener {

        void setCurrentPlayingSong(LocalSongEntity songEntity);

        LocalSongEntity getCurrentPlayingSong();

        void setPlayingMusicTitle(String title);

        void setPlayingMusicArtist(String artist);

        void setSeekBarMax(long max);

        void setSeekBarCurrentPosition(long currentPosition);

        void setTotalPlayTime(String totalPlayTime);

        void loadCover(String path, boolean needReveal);

        void loadBlurCover(Bitmap bitmap);

        boolean isPlayRepeatAll();

        boolean isPlayRepeatOne();

        boolean isPlayShuffle();

        void setPlayRepeatAll(boolean needAnim);

        void setPlayRepeatOne(boolean needAnim);

        void setPlayRepeatShuffle(boolean needAnim);

        PlayMode getCurrentPlayMode();

        boolean isMusicPlaying();

        boolean isMusicPause();

        boolean isMusicStop();

        void setPlayButtonPlaying();

        void setPlayButtonPause();

        void showPlayListBottomSheet();

        void updatePlayListBottomSheet(List<LocalSongEntity> playListSongs, LocalSongEntity currentSong);

        MusicPlayMainFragmentCallback getMusicPlayMainFragmentCallback();

    }

    interface MusicPlayMainPresenter extends IPresenter<MusicPlayMainView>, MusicPlayServiceCallback, PlayListBottomSheetCallback, MusicPlayMainFragmentCallback {
        void onPlayClick(View view);

        void onSkipPreviousClick(View view);

        void onSkipNextClick(View view);

        void onRepeatClick(View view);

        void onMusicPlayServiceConnected();

        void onMusicPlayServiceDisconnected();

        void onSeekStart();

        void onSeekStop(int progress);

    }

}
