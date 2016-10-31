package com.su.scott.slibrary.http.param;

/**
 * Created by Administrator on 2016/10/28.
 */

public class OkHttpParam {
    private String key;
    private String value;

    public OkHttpParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "OkHttpParams{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
