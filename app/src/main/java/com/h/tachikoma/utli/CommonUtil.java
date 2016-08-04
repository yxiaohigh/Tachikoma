package com.h.tachikoma.utli;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;

import com.h.tachikoma.MainActivity;
import com.h.tachikoma.R;
import com.h.tachikoma.act.SwNightActivity;
import com.h.tachikoma.base.App;

/**
 * 通用工具
 * Created by tony on 2016/8/4.
 */
public class CommonUtil {

    /**
     * 切换白天黑夜模式
     *
     * @param activity
     * 得到重启activity截图 保存截图
     * 判断模式 切换模式
     * 跳转SwNightActivity
     * 取出activity截图显示
     * 动画渐渐隐藏 关闭SwNightActivity (为了重启activity时不闪屏)
     * 重启activity
     */
    public static void SwNighAndDay(MainActivity activity) {
        Bitmap bitmap = PicUtil.getScreenBitmap(activity);
        App.getApplication().putAppArrayMap(activity.getString(R.string.night_cache),bitmap);

        int defaultNightMode = AppCompatDelegate.getDefaultNightMode();
        switch (defaultNightMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }

        Intent intent = new Intent(activity, SwNightActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           activity.recreate();
        }
    }
}
