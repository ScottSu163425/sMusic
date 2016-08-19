package com.su.scott.slibrary.util;

/**
 * Created by Administrator on 2016/8/12.
 */
public class CommonUtil {
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
