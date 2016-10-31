package com.su.scott.slibrary.http.callback;

/**
 * Created by Administrator on 2016/10/28.
 */

public interface OkHttpCallback {
    void onFailure(String msg);
    void onResponse(String response);
}
