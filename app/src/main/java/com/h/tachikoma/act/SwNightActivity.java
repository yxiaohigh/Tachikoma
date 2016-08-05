package com.h.tachikoma.act;

import android.graphics.Bitmap;
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
        setContentView(R.layout.sw_nigh_activity);

    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {

        Bitmap bitmap = (Bitmap) App.getApplication().getAppArrayMap(getString(R.string.night_cache));
        if (bitmap != null) {
            imageBg.setImageBitmap(bitmap);
        }


        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                App.getApplication().removeAppArrayMap(getString(R.string.night_cache));
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
