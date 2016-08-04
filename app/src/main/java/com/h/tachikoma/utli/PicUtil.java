package com.h.tachikoma.utli;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.graphics.Palette;
import android.view.Display;
import android.view.View;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by tony on 2016/5/23.
 */
public class PicUtil {

    /**
     * 得到图片中的颜色
     *
     * @param bitmap
     * @param i      模式 0默认 darkVibrantSwatch
     * @return
     */
    public static int getRgb2(Bitmap bitmap, int i) {
        Palette.Builder from = Palette.from(bitmap);
        Palette generate = from.generate();
        Palette.Swatch vibrantSwatch = generate.getVibrantSwatch();
        Palette.Swatch darkVibrantSwatch = generate.getDarkVibrantSwatch();
        Palette.Swatch darkMutedSwatch = generate.getDarkMutedSwatch();
        int color = 0;
        switch (i) {
            case 1:
                color = vibrantSwatch.getRgb();
                break;
            case 2:
                color = darkVibrantSwatch.getRgb();
                break;
            default:
                color = darkMutedSwatch.getRgb();
        }
        return color;
    }

    /**
     * 从存储中获取bitmap
     *
     * @param file
     * @param w 大小
     * @param h
     */
    public static Bitmap getBitmapFroFile(File file, int w, int h) {
        boolean exists = file.exists();

        if (exists) {
            String absolutePath = file.getAbsolutePath();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeFile(absolutePath, options);
            int outWidth = options.outWidth;
            int outHeight = options.outHeight;
            float ws = 0.f, hs = 0.f;
            if (outWidth > w || outHeight > h) {
                if (w == 0) {
                    ws = 1;
                    w=outWidth;
                }
                else {
                    ws = ((float) outWidth) / w;
                }
                if (h == 0) {
                    hs = 1;
                    h=outHeight;
                }
                else {
                    hs = ((float) outHeight) / h;
                }
            }
            float max = Math.max(ws, hs);
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int) max;
            WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(absolutePath, options));
            return Bitmap.createScaledBitmap(weak.get(), w, h, true);

        }
        return null;

    }

    /**
     * 从存储中获取bitmap
     * @param file
     * @return
     */
    public static Bitmap getBitmapFroFile(File file) {
       return getBitmapFroFile(file, 0, 0);
    }

    /**
     * 得到屏幕截图
     * @param activity
     * @return
     */
    public static Bitmap getScreenBitmap(Activity activity) {
        View decorView = activity.getWindow().getDecorView();

        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
        // 获取屏幕高
        int heigh = display.getHeight();
        int width = display.getWidth();

        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(drawingCache, 0, statusBarHeights, width, heigh - statusBarHeights);
        decorView.destroyDrawingCache();

        return bitmap;
    }
}
