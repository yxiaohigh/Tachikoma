package com.h.tachikoma.base;

import android.app.Application;
import android.support.v4.util.ArrayMap;

import com.h.tachikoma.di.commponent.AppComponent;
import com.h.tachikoma.di.commponent.DaggerAppComponent;
import com.h.tachikoma.di.module.AppModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * application
 * Created by tony on 2016/4/26.
 */
public class App extends Application {

    private static App app;
    private AppComponent component;
    private ArrayMap<String, Object> arrayMap;

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

    /**
     * 存储共享数据
     * @param k
     * @param v
     */
    public void putAppArrayMap(String k,Object v) {
        if (arrayMap == null) {
            arrayMap =  new ArrayMap<String, Object>();
        }
        arrayMap.put(k,v);
    }

    /**
     * 获取共享数据
     * @param k
     * @return
     */
    public Object getAppArrayMap(String k) {
        return arrayMap.get(k);
    }

    /**
     * 删除共享数据
     * @param k
     * @return
     */
    public Object removeAppArrayMap(String k) {
        return arrayMap.remove(k);
    }


}
