package com.bb.zinglibrary.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zing on 2016/12/5.
 */

public class NetworkStatusCheck {
    /**
     * 获取网络连接管理器
     *
     * @param context
     * @return
     */
    private static ConnectivityManager getConnection(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 手机有没有连接无线网络
     *
     * @param context
     * @return
     */
    public static boolean isMobileInternetConnected(Context context) {
        if (context == null) {
            return false;
        }
        return getConnection(context).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
    }

    /**
     * 手机有没有连接WiFi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConected(Context context) {
        if (context == null) {
            return false;
        }
        return getConnection(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
    }

    /**
     * 手机是否可以上网
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo mNetworkInfo = getConnection(context).getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
