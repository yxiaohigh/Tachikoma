package com.h.tachikoma.act;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.h.tachikoma.R;
import com.h.tachikoma.base.BaseActivity;
import com.h.tachikoma.utli.PicUtil;

import java.io.File;
import java.io.FileInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * test
 * Created by tony on 2016/7/27.
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.image_bg)
    ImageView imageBg;

    private FileInputStream fileInputStream;

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
        File filesDir = this.getFilesDir();
        String path = filesDir.getPath();
        File file = new File(path, getString(R.string.NIGHT_CACHE));
        int w = 0, h = 0;
        Bitmap bitmapFroFile = PicUtil.getBitmapFroFile(file, 0, 0);
        if (bitmapFroFile != null) {
            imageBg.setImageBitmap(bitmapFroFile);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
