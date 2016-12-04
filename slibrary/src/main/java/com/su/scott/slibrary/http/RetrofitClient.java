package com.su.scott.slibrary.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/7.
 */

public class RetrofitClient {
    public static final String URL_BASE = "http://192.168.1.18/RtpService.asmx/";
    public static final int TIMEOUT_SECOND_CONNECT = 10;
    public static final int TIMEOUT_SECOND_READ = 10;
    public static final int TIMEOUT_SECOND_WRITE = 30;

    private static RetrofitClient mRetrofitClient;
    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    private RetrofitClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECOND_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECOND_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECOND_WRITE, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    public static RetrofitClient getInstance() {
        if (mRetrofitClient == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofitClient == null) {
                    mRetrofitClient = new RetrofitClient();
                }
            }
        }
        return mRetrofitClient;
    }

    public <E> E createService(Class<E> classOfService) {
        return mRetrofit.create(classOfService);
    }


}
