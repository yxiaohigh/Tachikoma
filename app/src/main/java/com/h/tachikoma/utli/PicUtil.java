package com.h.tachikoma.utli;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

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
}
