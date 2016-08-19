package com.su.scott.slibrary.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @类名 Snack
 * @描述 SnackBar显示类
 * @作者 Su
 * @时间 2015年12月21日
 */
public class Snack {

    /**
     * 显示短时Snackbar
     * @param parent
     * @param text
     */
    public static void showShort(View parent, CharSequence text) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示短时Snackbar
     * @param parent
     * @param text
     * @param action
     * @param listener
     */
    public static void showShort(View parent, CharSequence text, CharSequence action, @NonNull View.OnClickListener listener) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).setAction(action, listener).show();
    }

    /**
     * 显示长时Snackbar
     * @param parent
     * @param text
     */
    public static void showLong(View parent, CharSequence text) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 显示长时Snackbar
     * @param parent
     * @param text
     * @param action
     * @param listener
     */
    public static void showLong(View parent, CharSequence text, CharSequence action, @NonNull View.OnClickListener listener) {
        if (null == parent) {
            return;
        }
        Snackbar.make(parent, text, Snackbar.LENGTH_LONG).setAction(action, listener).show();
    }

    /**
     * 构建短时Snackbar
     * @param parent
     * @param text
     * @return
     */
    public static Snackbar makeShort(View parent, CharSequence text) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_SHORT);
    }

    /**
     * 构建短时Snackbar
     * @param parent
     * @param text
     * @param action
     * @param listener
     * @return
     */
    public static Snackbar makeShort(View parent, CharSequence text, CharSequence action, @NonNull View.OnClickListener listener) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).setAction(action, listener);
    }

    /**
     * 构建长时Snackbar
     * @param parent
     * @param text
     * @return
     */
    public static Snackbar makeLong(View parent, CharSequence text) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_LONG);
    }

    /**
     * 构建长时Snackbar
     * @param parent
     * @param text
     * @param action
     * @param listener
     * @return
     */
    public static Snackbar makeLong(View parent, CharSequence text, CharSequence action, @NonNull View.OnClickListener listener) {
        if (null == parent) {
            return null;
        }
        return Snackbar.make(parent, text, Snackbar.LENGTH_LONG).setAction(action, listener);
    }


}
