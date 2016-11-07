package com.su.scott.slibrary.http;

/**
 * Created by Administrator on 2016/11/7.
 */

public interface HttpCallback<E> {

    /**
     * Running on main thread.
     *
     * @param response
     */
    void onSuccess(E response);


    /**
     * Running on main thread.
     *
     * @param msg
     */
    void onFailure(String msg);

}
