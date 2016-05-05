package com.h.tachikoma;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by tony on 2016/4/26.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
