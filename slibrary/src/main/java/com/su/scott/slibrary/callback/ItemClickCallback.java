package com.su.scott.slibrary.callback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface ItemClickCallback<E> {
    void onItemClick(View itemView, E entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data);

    public interface SimpleItemClickCallback<T> {
        void onItemClick(View itemView, T entity, int position);
    }
}
