package com.su.scott.slibrary.util;

import android.util.Log;

/**
 * @类名 L
 * @描述 日志打印类
 * @作者 Su
 * @时间 2015年12月9日 11:08:24
 */

public class L {

    public static final String TAG = "=======>su";
    private static boolean needPrint = true;

    private L() {

    }

    public static void v(String msg) {
        if (needPrint) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (needPrint) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (needPrint) {
            Log.i(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (needPrint) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (needPrint) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (needPrint) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (needPrint) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (needPrint) {
            Log.e(tag, msg);
        }
    }

}
