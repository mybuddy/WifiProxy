package com.felixyan.wifiproxy.adapter;

import android.view.View;

/**
 * Created by yanfei on 2017/6/8.
 */

public class ViewWrapper<T> implements IViewWrapper<T> {
    private View mView;

    ViewWrapper(View view) {
        mView = view;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void setData(int position, T data) {

    }
}
