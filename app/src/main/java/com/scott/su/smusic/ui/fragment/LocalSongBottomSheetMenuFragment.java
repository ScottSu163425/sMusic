package com.scott.su.smusic.ui.fragment;

import android.support.annotation.NonNull;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.LocalSongBottomSheetCallback;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.fragment.BaseBottomSheetMenuFragment;
import com.su.scott.slibrary.util.FileUtil;
import com.su.scott.slibrary.util.TimeUtil;

/**
 * Created by Administrator on 2016/8/30.
 */
public class LocalSongBottomSheetMenuFragment extends BaseBottomSheetMenuFragment {
    private static final int INDEX_MENU_ITEM_ADD_TO_BILL = 0;
    private static final int INDEX_MENU_ITEM_ALBUM = 1;
    private static final int INDEX_MENU_ITEM_DELETE = 4;

    private LocalSongEntity mSongEntity;
    private LocalSongBottomSheetCallback mLocalSongBottomSheetCallback;


    public static LocalSongBottomSheetMenuFragment newInstance() {
        LocalSongBottomSheetMenuFragment instance = new LocalSongBottomSheetMenuFragment();
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
            if (position == INDEX_MENU_ITEM_ADD_TO_BILL) {
                mLocalSongBottomSheetCallback.onAddToBillClick(this, mSongEntity);
                dismissAllowingStateLoss();
            } else if (position == INDEX_MENU_ITEM_ALBUM) {
                mLocalSongBottomSheetCallback.onAlbumClick(this, mSongEntity);
                dismissAllowingStateLoss();
            } else if (position == INDEX_MENU_ITEM_DELETE) {
                mLocalSongBottomSheetCallback.onDeleteClick(this, mSongEntity);
                dismissAllowingStateLoss();
            }
        }
    }

    public LocalSongBottomSheetMenuFragment setLocalSongEntity(LocalSongEntity songEntity) {
        this.mSongEntity = songEntity;
        return LocalSongBottomSheetMenuFragment.this;
    }

    public LocalSongBottomSheetMenuFragment setMenuClickCallback(LocalSongBottomSheetCallback mLocalSongBottomSheetCallback) {
        this.mLocalSongBottomSheetCallback = mLocalSongBottomSheetCallback;
        return LocalSongBottomSheetMenuFragment.this;
    }

}
