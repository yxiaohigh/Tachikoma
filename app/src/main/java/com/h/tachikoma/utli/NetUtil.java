package com.h.tachikoma.utli;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  网络状态检查工具
 * Created by tony on 2016/5/23.
 */
public class NetUtil {


    /**
     *
     * 判断网络是否连接
     * @return
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                return true;
            }
        }
        return false;

    }
}
