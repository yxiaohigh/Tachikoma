package com.h.tachikoma.utli;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

/**
 * Created by tony on 2016/5/23.
 */
public class PicUtil {

    /**
     * 得到图片中的颜色
     * @param bitmap
     * @return
     */
    public static int getRgb2(Bitmap bitmap) {
        Palette.Builder from = Palette.from(bitmap);
        Palette generate = from.generate();
        Palette.Swatch vibrantSwatch = generate.getVibrantSwatch();
        Palette.Swatch darkVibrantSwatch = generate.getDarkVibrantSwatch();
        Palette.Swatch darkMutedSwatch = generate.getDarkMutedSwatch();
        int rgb1 = darkVibrantSwatch.getRgb();
        int rgb = vibrantSwatch.getRgb();
        return darkMutedSwatch.getRgb();
    }
}
