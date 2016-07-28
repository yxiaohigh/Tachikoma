package com.h.tachikoma.act;

import android.view.MotionEvent;

import com.h.tachikoma.R;
import com.h.tachikoma.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * test
 * Created by tony on 2016/7/27.
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void setContent() {
        setContentView(R.layout.test_activity);
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
