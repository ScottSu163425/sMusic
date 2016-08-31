package com.scott.su.smusic.ui.fragment;

import android.support.annotation.NonNull;

import com.scott.su.smusic.R;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.fragment.BaseListBottomSheetMenuFragment;

/**
 * Created by Administrator on 2016/8/30.
 */
public class BillDetailBottomSheetFragment extends BaseListBottomSheetMenuFragment {
    private LocalSongEntity mSongEntity;
    private BillDetailBottomMenuClickCallback mMenuClickCallback;


    public static BillDetailBottomSheetFragment newInstance() {
        BillDetailBottomSheetFragment instance = new BillDetailBottomSheetFragment();

        return instance;
    }

    @Override
    protected String getTitle() {
        return mSongEntity == null ? "" : "歌曲: " + mSongEntity.getTitle();
    }

    @NonNull
    @Override
    protected int[] getMenuItemIcons() {
        return new int[]{R.drawable.ic_cover_default_song_bill, R.drawable.ic_cover_default_song_bill,
                R.drawable.ic_cover_default_song_bill, R.drawable.ic_cover_default_song_bill,
                R.drawable.ic_cover_default_song_bill, R.drawable.ic_cover_default_song_bill,};
    }

    @NonNull
    @Override
    protected String[] getMenuItemNames() {
        return new String[]{"增加", "删除", "修改", "查询", "遍历", "更多",};
    }


    @Override
    protected void onMenuItemClick(int position, String itemName) {
        if (mMenuClickCallback != null) {
            if (position == 0) {
                mMenuClickCallback.onAddClick();
            } else if (position == getMenuItemIcons().length - 1) {
                mMenuClickCallback.onMoreClick();
            }
        }
    }

    public BillDetailBottomSheetFragment setLocalSongEntity(LocalSongEntity songEntity) {
        this.mSongEntity = songEntity;
        return BillDetailBottomSheetFragment.this;
    }

    public BillDetailBottomSheetFragment setMenuClickCallback(BillDetailBottomMenuClickCallback mMenuClickCallback) {
        this.mMenuClickCallback = mMenuClickCallback;
        return BillDetailBottomSheetFragment.this;
    }

    public interface BillDetailBottomMenuClickCallback {
        void onAddClick();

        void onMoreClick();
    }

}
