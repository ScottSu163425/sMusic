package com.su.scott.slibrary.http.param;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/10/28.
 */

public class OkHttpParams {
    LinkedList<OkHttpParam> params = new LinkedList<>();


    public OkHttpParams() {
    }

    public OkHttpParams(OkHttpParam param) {
        params.add(param);
    }
//
//    public OkHttpParams add(OkHttpParam param) {
//        params.add(param);
//        return this;
//    }

    public OkHttpParams add(String key, String value) {
        params.add(new OkHttpParam(key,value));
        return this;
    }

    public boolean isEmpty(){
        return params.isEmpty();
    }

    public LinkedList<OkHttpParam> getParams() {
        return params;
    }

}
