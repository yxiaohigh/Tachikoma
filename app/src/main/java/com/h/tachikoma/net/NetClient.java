package com.h.tachikoma.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络工具
 * Created by tony on 2016/4/19.
 */
public class NetClient {

    private static String path=ApiService.PATH;

    /**
     * 得到okhttpclien
     * @return
     */
    public static OkHttpClient initClient(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }


    /**
     * 得到retrofit
     * @return
     */
    public static Retrofit initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(path)
                .client(initClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;

    }

    /**
     * 得到可用请求类
     * @return
     */
    public static ApiService getApiService() {
        ApiService apiService = initRetrofit().create(ApiService.class);
        return apiService;
    }

}
