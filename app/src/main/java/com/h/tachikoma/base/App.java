package com.h.tachikoma.base;

import android.app.Application;

import com.h.tachikoma.di.module.AppComponent;
import com.h.tachikoma.di.module.AppModule;
import com.h.tachikoma.di.module.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;

/**
 * application
 * Created by tony on 2016/4/26.
 */
public class App extends Application {

    private static App app;
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        app = this;

    }

    public AppComponent getCommponent() {
        return component;
    }

    public static App getApplication() {
        return app;
    }
}
