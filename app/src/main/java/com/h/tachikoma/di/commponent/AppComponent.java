package com.h.tachikoma.di.commponent;

import android.content.Context;

import com.h.tachikoma.di.module.AppModule;
import com.h.tachikoma.net.NetApi;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by tony on 2016/6/24.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Context getContext();

    OkHttpClient getOkHttpClient();

    NetApi getNetApi();
}
