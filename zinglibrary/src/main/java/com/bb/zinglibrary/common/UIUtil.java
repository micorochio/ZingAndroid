package com.bb.zinglibrary.common;

/**
 * Created by zing on 2016/12/8.
 */

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;


public class UIUtil {
    /**
     * 获取屏幕宽度
     *
     * @param a
     * @return
     */
    public static float getScreenWidth(Activity a) {
        DisplayMetrics dm = getDisplayMetrics(a);
        return dm.widthPixels * dm.density;
    }

    /**
     * 获取屏幕高度
     *
     * @param a
     * @return
     */
    public static float getScreenHeight(Activity a) {
        DisplayMetrics dm = getDisplayMetrics(a);
        return dm.heightPixels * dm.density;
    }

    /**
     * 获取屏幕宽高
     *
     * @param a
     * @return
     */
    public static float[] getScreenWidthHeight(Activity a) {
        DisplayMetrics dm = getDisplayMetrics(a);
        float width = dm.widthPixels * dm.density;
        float height = dm.heightPixels * dm.density;
        return new float[]{width, height};
    }


    /**
     * 获取显示选项
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
