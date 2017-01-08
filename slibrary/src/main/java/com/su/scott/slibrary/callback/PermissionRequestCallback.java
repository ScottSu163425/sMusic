package com.su.scott.slibrary.callback;

/**
 * Created by asus on 2017/1/8.
 */

public interface PermissionRequestCallback {
    void onPermissionGranted();

    void onPermissionDenied();

    void onPermissionDeniedPermanently();
}
