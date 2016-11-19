package com.scott.su.smusic.callback;

import android.view.View;

import com.scott.su.smusic.entity.PlayStatisticEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/11/19.
 */

public interface PlayStatisticItemClickCallback {
    void onPlayStatisticItemClick(int position, PlayStatisticEntity entity, ArrayList<PlayStatisticEntity> statisticEntityList, View sharedElement, String transitionName);
}
