package com.h.tachikoma.di.module;

import android.app.Activity;

import dagger.Component;

/**
 * Created by tony on 2016/6/24.
 */
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Activity activity);
}
