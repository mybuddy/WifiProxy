package com.felixyan.wifiproxy.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

import com.felixyan.wifiproxy.App;

public class ViewUtil {
    public static float getDimension(int dimensionId) {
        if (dimensionId != 0) {
            return App.getInstance().getResources().getDimension(dimensionId);
        }
        return 0;
    }

    public static int getDimensionPixelSize(int dimensionId) {
        if (dimensionId != 0) {
            return App.getInstance().getResources().getDimensionPixelSize(dimensionId);
        }
        return 0;
    }

    public static int dp2px(float dpValue) {
        return dp2px(App.getInstance(), dpValue);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(float pxValue) {
        return px2dp(App.getInstance(), pxValue);
    }

    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽高，单位px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth;
        int screenHeight;
        // 注意：部分设备开启屏幕自动旋转后，宽高会互调。这里宽始终取长边，高始终取短边（横屏）
        if (dm.widthPixels >= dm.heightPixels) {
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        } else {
            screenWidth = dm.heightPixels;
            screenHeight = dm.widthPixels;
        }

        return new Point(screenWidth, screenHeight);
    }

    /**
     * 获取屏幕宽高比
     *
     * @param context
     * @return
     */
    public static float getScreenRate(Context context) {
        Point p = getScreenMetrics(context);
        return p.x / p.y;
    }

    public static int getPreviewScale(Context context) {
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels / 2;
        float heightPixels = context.getResources().getDisplayMetrics().heightPixels / 2;
        return (int) (widthPixels > heightPixels ? widthPixels : heightPixels);
    }

}
