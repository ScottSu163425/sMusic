package com.su.scott.slibrary.util;

import android.os.Build;

/**
 * Created by Administrator on 2016/8/11.
 */
public class SdkUtil {

    public static int getSdkVersionCode() {
        return Build.VERSION.PREVIEW_SDK_INT;
    }

    /**
     * return true if the android version of device is above 5.0(21);
     *
     * @return
     */
    public static boolean isLolipopOrLatter() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * return true if the android version of device is above 6.0(23);
     *
     * @return
     */
    public static boolean isMarshmallowOrLatter() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
