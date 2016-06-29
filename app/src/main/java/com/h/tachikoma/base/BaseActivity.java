package com.h.tachikoma.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.h.tachikoma.di.commponent.AppComponent;

/**
 * BaseActivity
 * Created by tony on 2016/5/6.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected AppComponent commponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commponent = App.getApplication().getCommponent();
        setContent();
        initViews();
        initData();
    }

    protected abstract void setContent();
    protected abstract void initViews();
    protected abstract void initData();
}
