package com.su.scott.slibrary.util;

import android.app.Activity;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.View;

/**
 * Created by Administrator on 2016/9/9.
 */
public class PopupMenuUtil {

    public static PopupMenu popMenu(@NonNull Activity activity, @MenuRes int menuRes, @NonNull View anchor, @NonNull PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(activity, anchor);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.show();
        return popupMenu;
    }

}
