package com.su.scott.slibrary.callback;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface RequestCallback<E> {
    void onError(String errorMsg);

    void onResopnse(E response);
}
