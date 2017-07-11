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
}
