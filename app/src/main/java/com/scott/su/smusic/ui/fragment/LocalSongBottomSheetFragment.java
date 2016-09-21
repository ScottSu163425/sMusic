package com.scott.su.smusic.ui.fragment;

import android.support.annotation.NonNull;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.fragment.BaseListBottomSheetMenuFragment;

/**
 * Created by Administrator on 2016/8/30.
 */
public class LocalSongBottomSheetFragment extends BaseListBottomSheetMenuFragment {
    private LocalSongEntity mSongEntity;
    private MenuClickCallback mMenuClickCallback;


    public static LocalSongBottomSheetFragment newInstance() {
        LocalSongBottomSheetFragment instance = new LocalSongBottomSheetFragment();
        return instance;
    }

    @Override
    protected String getTitle() {
        return mSongEntity == null ? "" : getString(R.string.song) +":"+ mSongEntity.getTitle();
    }

    @NonNull
    @Override
    protected int[] getMenuItemIcons() {
        return new int[]{R.drawable.ic_my_library_music_grey600_24dp,/* R.drawable.ic_supervisor_account_grey600_24dp,*/
                R.drawable.ic_album_grey600_24dp, R.drawable.ic_share_grey600_24dp,
                R.drawable.ic_delete_grey600_24dp};
    }

    @NonNull
    @Override
    protected String[] getMenuItemNames() {
        return new String[]{getString(R.string.add_to_bill),/* "歌手: " + mSongEntity.getArtist(),*/
                getString(R.string.album) +":"+ mSongEntity.getAlbum(), getString(R.string.share), getString(R.string.delete),};
    }


    @Override
    protected void onMenuItemClick(int position, String itemName) {
        if (mMenuClickCallback != null) {
            if (position == 0) {
                mMenuClickCallback.onAddToBillClick(mSongEntity);
            } else if (position == 1) {
                mMenuClickCallback.onAlbumClick(mSongEntity);
            } else if (position == 2) {
                mMenuClickCallback.onShareClick(mSongEntity);
            } else if (position == 3) {
                mMenuClickCallback.onDeleteClick(mSongEntity);
            }
        }
    }

    public LocalSongBottomSheetFragment setLocalSongEntity(LocalSongEntity songEntity) {
        this.mSongEntity = songEntity;
        return LocalSongBottomSheetFragment.this;
    }

    public LocalSongBottomSheetFragment setMenuClickCallback(MenuClickCallback mMenuClickCallback) {
        this.mMenuClickCallback = mMenuClickCallback;
        return LocalSongBottomSheetFragment.this;
    }

    public interface MenuClickCallback {
        void onAddToBillClick(LocalSongEntity songEntity);

        void onAlbumClick(LocalSongEntity songEntity);

        void onShareClick(LocalSongEntity songEntity);

        void onDeleteClick(LocalSongEntity songEntity);
    }

}
