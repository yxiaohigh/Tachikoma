package com.h.tachikoma.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * BaseActivity
 * Created by tony on 2016/5/6.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 定制流程
        setContentView();
        initViews();
        initListeners();
        initData();
    }

    protected abstract void setContentView();
    protected abstract void initViews();
    protected abstract void initListeners();
    protected abstract void initData();
}
