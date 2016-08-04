package com.h.tachikoma.act;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.h.tachikoma.R;
import com.h.tachikoma.base.App;
import com.h.tachikoma.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * test
 * Created by tony on 2016/7/27.
 */
public class SwNightActivity extends BaseActivity {

    @BindView(R.id.image_bg)
    ImageView imageBg;


    @Override
    protected void setContent() {
        setContentView(R.layout.test_activity);

    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        imageBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void initData() {

        Bitmap bitmap = (Bitmap) App.getApplication().getAppArrayMap(getString(R.string.night_cache));
        if (bitmap != null) {
            imageBg.setImageBitmap(bitmap);
        }


        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                overridePendingTransition(0,0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageBg.startAnimation(alphaAnimation);
    }

}
