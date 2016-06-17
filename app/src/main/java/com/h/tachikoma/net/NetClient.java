package com.h.tachikoma.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络工具 单例
 * Created by tony on 2016/4/19.
 */
public class NetClient {

    private static NetApi netApi;
    private static OkHttpClient okHttpClient;

    static {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetApi.PATH)
                .client(initClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        netApi = retrofit.create(NetApi.class);
    }

    /**
     * 得到okhttpclien
     *
     * @return
     */
    public static OkHttpClient initClient() {

        return okHttpClient;
    }


    /**
     * 得到可用请求类
     *
     * @return
     */
    public static NetApi getNetApi() {

        return netApi;
    }

}
