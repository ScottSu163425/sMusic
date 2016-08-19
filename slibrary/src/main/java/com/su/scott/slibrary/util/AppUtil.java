package com.su.scott.slibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * @类名 AppUtil
 * @描述 应用相关工具类
 * @作者 Su
 * @时间 2015年12月
 */
public class AppUtil {
    private AppUtil() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName
     *            目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isAppInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 启动指定应用
     *
     * @param packageName
     *            目标应用包名
     */
    public static void launchApp(Activity context, String packageName) {
        // 启动目标应用
        if (isAppInstalled(packageName)) {
            // 获取目标应用安装包的Intent
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        }
    }

    /**
     * 打开安装Apk界面
     *
     * @param file
     */
    public static void installApk(Activity activity, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }
}
