package com.h.tachikoma.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by tony on 2016/6/17.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent();
        initViews();
        initData();
    }
    protected abstract void setContent();

    protected abstract void initViews();

    protected abstract void initData();




}
