package com.h.tachikoma.utli;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.graphics.Palette;
import android.view.View;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 图片处理工具
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
        int color ;
        switch (i) {
            case 1:
                color = vibrantSwatch != null ? vibrantSwatch.getRgb() : 0;
                break;
            case 2:
                color = darkVibrantSwatch != null ? darkVibrantSwatch.getRgb() : 0;
                break;
            default:
                color = darkMutedSwatch != null ? darkMutedSwatch.getRgb() : 0;
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
            WeakReference<Bitmap> weak = new WeakReference<>(BitmapFactory.decodeFile(absolutePath, options));
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


        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        int height1 = drawingCache.getHeight();
        int width1 = drawingCache.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(drawingCache, 0, statusBarHeights, width1, height1 - statusBarHeights);
        decorView.destroyDrawingCache();

        return bitmap;
    }



    /**
     * 图片缩放比例
     */
    private static final float BITMAP_SCALE = 0.4f;
    /**
     * 最大模糊度(在0.0到25.0之间)
     */
    private static final float BLUR_RADIUS = 25f;

    /**
     * 模糊图片的具体方法
     *
     * @param context   上下文对象
     * @param image     需要模糊的图片
     * @return          模糊处理后的图片
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap image) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片。
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(BLUR_RADIUS);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
