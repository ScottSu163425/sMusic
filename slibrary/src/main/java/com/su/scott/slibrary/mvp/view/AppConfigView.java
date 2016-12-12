package com.su.scott.slibrary.mvp.view;

import android.content.Context;

/**
 * Created by Administrator on 2016/12/9.
 */

public interface AppConfigView {
    void putBoolean(Context context, String key, boolean value);

    void putInt(Context context, String key, int value);

    void putLong(Context context, String key, long value);

    void putFloat(Context context, String key, float value);

    void putString(Context context, String key, String value);

    boolean getBoolean(Context context, String key, boolean valueDefaultDefault);

    int getInt(Context context, String key, int valueDefault);

    long getLong(Context context, String key, long valueDefault);

    float getFloat(Context context, String key, float valueDefault);

    String getString(Context context, String key, String valueDefault);

}
