package com.su.scott.slibrary.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.su.scott.slibrary.http.callback.OkHttpCallback;
import com.su.scott.slibrary.http.param.OkHttpParam;
import com.su.scott.slibrary.http.param.OkHttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/28.
 */

public class OkHttpHelper {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient mOkHttpClient;
    private static OkHttpHelper mInstance;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    private OkHttpHelper() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();
        }
    }

    public static OkHttpHelper getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClient.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpHelper();
                }
            }
        }
        return mInstance;
    }

    public void doStringGet(@NonNull String url, @Nullable final OkHttpCallback callback) {
        stringGet(url, null, null, callback);
    }

    public void doStringGet(@NonNull String url, OkHttpParams params, @Nullable final OkHttpCallback callback) {
        stringGet(url, null, params, callback);
    }

    public void doStringGet(@NonNull String url, OkHttpParams headers, OkHttpParams params, @Nullable final OkHttpCallback callback) {
        stringGet(url, headers, params, callback);
    }

    private void stringGet(@NonNull String url, @Nullable OkHttpParams headers, @Nullable OkHttpParams params, @Nullable final OkHttpCallback callback) {
        Request.Builder requestBuilder = new Request.Builder();
        String urlGet = buildGetUrl(url, params);

        //Add header if provided.
        if (headers != null && !headers.isEmpty()) {
            for (OkHttpParam param : headers.getParams())
                requestBuilder.header(param.getKey(), param.getValue());
        }

        requestBuilder.url(urlGet);

//        Log.e("===>send", urlGet);

        mOkHttpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                if (callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(responseStr);
                        }
                    });
                }
            }
        });

    }

    public void doStringPost(@NonNull String url, OkHttpParams params, final OkHttpCallback callback) {
        stringPost(url, null, params, callback);
    }

    public void doStringPost(@NonNull String url, OkHttpParams headers, OkHttpParams params, final OkHttpCallback callback) {
        stringPost(url, headers, params, callback);
    }

    private void stringPost(@NonNull String url, @Nullable OkHttpParams headers, @Nullable OkHttpParams params, @Nullable final OkHttpCallback callback) {
        Request.Builder requestBuilder = new Request.Builder();
        FormBody.Builder bodyBuilder = new FormBody.Builder();

        if (params != null && !params.isEmpty()) {
            for (OkHttpParam param : params.getParams()) {
                bodyBuilder.addEncoded(param.getKey(), param.getValue());
            }
        }

        if (headers != null && !headers.isEmpty()) {
            for (OkHttpParam param : headers.getParams())
                requestBuilder.header(param.getKey(), param.getValue());
        }

        requestBuilder.url(url);
        requestBuilder.post(bodyBuilder.build());

        mOkHttpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                if (callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(responseStr);
                        }
                    });
                }
            }
        });

    }

    public void jsonPost(@NonNull String url, @NonNull OkHttpParams params, @Nullable final OkHttpCallback callback) {
        JSONObject jsonObject = new JSONObject();
        for (OkHttpParam param : params.getParams()) {
            try {
                jsonObject.put(param.getKey(), param.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonPost(url, jsonObject.toString(), callback);
    }

    public void jsonPost(@NonNull String url, @NonNull String json, @Nullable final OkHttpCallback callback) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        mOkHttpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                if (callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(responseStr);
                        }
                    });
                }
            }
        });
    }

    private String buildGetUrl(@NonNull String url, @Nullable OkHttpParams params) {
        StringBuilder builder = new StringBuilder(url);

        if (params != null) {
            boolean isFirstTimeTraversal = true;
            for (OkHttpParam param : params.getParams()) {
                if (isFirstTimeTraversal) {
                    builder.append("?" + param.getKey() + "=" + param.getValue());
                    isFirstTimeTraversal = false;
                } else {
                    builder.append("&" + param.getKey() + "=" + param.getValue());
                }
            }
        }
        return builder.toString();
    }

}
