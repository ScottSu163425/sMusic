package com.su.scott.slibrary.entity;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

/**
 * Created by Administrator on 2016/8/30.
 */
public class BottomSheetMenuItemEntity {
    private
    @DrawableRes
    int iconId;

    private String itemName;

    public BottomSheetMenuItemEntity(int iconId, String itemName) {
        this.iconId = iconId;
        this.itemName = itemName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(@DrawableRes int iconId) {
        this.iconId = iconId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
