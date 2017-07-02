package com.felixyan.wifiproxy.adapter;

import android.view.View;

/**
 * Created by yanfei on 2017/6/5.
 */

public interface OnListItemClickListener<T> {
    void onItemClick(View v, int viewType, int position, T data);
}
