package com.h.tachikoma.base;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * application
 * Created by tony on 2016/4/26.
 */
public class App extends Application {

    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        app = this;
    }

    public static Application getApplication() {
        return app;
    }
}
