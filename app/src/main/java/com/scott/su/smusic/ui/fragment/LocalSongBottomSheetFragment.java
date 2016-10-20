package com.scott.su.smusic.ui.fragment;

import android.support.annotation.NonNull;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.fragment.BaseListBottomSheetMenuFragment;
import com.su.scott.slibrary.util.FileUtil;
import com.su.scott.slibrary.util.TimeUtil;

/**
 * Created by Administrator on 2016/8/30.
 */
public class LocalSongBottomSheetFragment extends BaseListBottomSheetMenuFragment {
    private LocalSongEntity mSongEntity;
    private LocalSongBottomSheetCallback mLocalSongBottomSheetCallback;


    public static LocalSongBottomSheetFragment newInstance() {
        LocalSongBottomSheetFragment instance = new LocalSongBottomSheetFragment();
        return instance;
    }

    @Override
    protected String getTitle() {
        return mSongEntity == null ? "" : mSongEntity.getTitle();
    }

    @NonNull
    @Override
    protected int[] getMenuItemIcons() {
        return new int[]{R.drawable.ic_menu_item_bill_24dp,
                R.drawable.ic_menu_item_album_24dp,
                R.drawable.ic_menu_item_time_24dp,
                R.drawable.ic_menu_item_file_24dp,
                R.drawable.ic_menu_item_delete_24dp};
    }

    @NonNull
    @Override
    protected String[] getMenuItemNames() {
        return new String[]{
                getString(R.string.add_to_bill),
                getString(R.string.album) + ": " + mSongEntity.getAlbum(),
                getString(R.string.time) + ": " + TimeUtil.millisecondToMMSS(mSongEntity.getDuration()),
                getString(R.string.file) + ": " + FileUtil.getFileOrFilesSize(mSongEntity.getPath(), FileUtil.SIZETYPE_MB) + "M",
                getString(R.string.delete),};
    }


    @Override
    protected void onMenuItemClick(int position, String itemName) {
        if (mLocalSongBottomSheetCallback != null) {
            if (position == 0) {
                mLocalSongBottomSheetCallback.onAddToBillClick(this, mSongEntity);
                dismissAllowingStateLoss();
            } else if (position == 1) {
                mLocalSongBottomSheetCallback.onAlbumClick(this, mSongEntity);
                dismissAllowingStateLoss();
            } else if (position == 4) {
                mLocalSongBottomSheetCallback.onDeleteClick(this, mSongEntity);
                dismissAllowingStateLoss();
            }
        }
    }

    public LocalSongBottomSheetFragment setLocalSongEntity(LocalSongEntity songEntity) {
        this.mSongEntity = songEntity;
        return LocalSongBottomSheetFragment.this;
    }

    public LocalSongBottomSheetFragment setMenuClickCallback(LocalSongBottomSheetCallback mLocalSongBottomSheetCallback) {
        this.mLocalSongBottomSheetCallback = mLocalSongBottomSheetCallback;
        return LocalSongBottomSheetFragment.this;
    }

}
