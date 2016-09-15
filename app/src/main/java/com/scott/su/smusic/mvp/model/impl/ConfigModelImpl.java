package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;

import com.scott.su.smusic.config.Configure;
import com.scott.su.smusic.mvp.model.ConfigModel;

/**
 * Created by asus on 2016/9/15.
 */
public class ConfigModelImpl implements ConfigModel {
    @Override
    public boolean isPlayRepeatOne(Context context) {
        return Configure.isPlayRepeatOne(context);
    }

    @Override
    public boolean isPlayRepeatAll(Context context) {
        return Configure.isPlayRepeatAll(context);
    }

    @Override
    public boolean isPlayShuffle(Context context) {
        return Configure.isPlayShuffle(context);
    }

    @Override
    public void setPlayRepeatOne(Context context) {
        Configure.setPlayRepeatOne(context);
    }

    @Override
    public void setPlayRepeatAll(Context context) {
        Configure.setPlayRepeatAll(context);
    }

    @Override
    public void setPlayShuffle(Context context) {
        Configure.setPlayShuffle(context);
    }

}
