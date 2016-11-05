package com.su.scott.slibrary.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ViewUtil {
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isViewVisiable(@NonNull View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static boolean isViewInVisiable(@NonNull View view) {
        return view.getVisibility() == View.INVISIBLE;
    }

    public static boolean isViewGone(@NonNull View view) {
        return view.getVisibility() == View.GONE;
    }

    public static void setViewVisiable(@NonNull View view) {
        if (isViewVisiable(view)) {
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void setViewInVisiable(@NonNull View view) {
        if (isViewInVisiable(view)) {
            return;
        }
        view.setVisibility(View.INVISIBLE);
    }

    public static void setViewGone(@NonNull View view) {
        if (isViewGone(view)) {
            return;
        }
        view.setVisibility(View.GONE);
    }

    public static void setText(@NonNull TextView editText, String text, String defaultText) {
        editText.setText(TextUtils.isEmpty(text) ? defaultText : text);
    }

    public static void setText(@NonNull TextView editText, String text) {
        editText.setText(text);
    }

}
