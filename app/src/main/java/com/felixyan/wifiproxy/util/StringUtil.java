package com.felixyan.wifiproxy.util;

import android.support.annotation.StringRes;

import com.felixyan.wifiproxy.App;

/**
 * Created by yanfei on 2017/7/11.
 */

public class StringUtil {
    public static String getString(@StringRes int resId) {
        return App.getInstance().getString(resId);
    }

    public static String getString(String format, Object... values) {
        return String.format(format, values);
    }

    public static String getString(@StringRes int resId, Object... values) {
        return getString(getString(resId), values);
    }
}
