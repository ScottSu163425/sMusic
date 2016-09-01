package com.su.scott.slibrary.util;

import android.os.Build;

/**
 * Created by Administrator on 2016/8/11.
 */
public class SdkUtil {

    /**
     * return true if the android version of device is above 5.0;
     *
     * @return
     */
    public static boolean isLolipopOrLatter() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static int getSdkVersionCode() {
        return Build.VERSION.PREVIEW_SDK_INT;
    }
}
