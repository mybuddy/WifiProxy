package com.felixyan.wifiproxy.adapter;

import android.view.View;

/**
 * Created by yanfei on 2017/6/2.
 */

public interface IViewWrapper<T> {
    View getView();
    void setData(int position, T data);
}
