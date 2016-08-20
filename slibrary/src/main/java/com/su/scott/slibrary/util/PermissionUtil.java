package com.su.scott.slibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by asus on 2016/8/20.
 */
public class PermissionUtil {

    /**
     *
     * @param context
     * @param permission eg.Manifest.permission.READ_EXTERNAL_STORAGE
     */
    public static void checkPermission(Context context,String  permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
                return;
            }
        }
    }

}
