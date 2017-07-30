package com.felixyan.wifiproxy.view;

import android.view.View;

/**
 * Created by yanfei on 2017/07/31.
 */

public interface IDataView<T> {
    View getView();
    T getData();
    void setData(T data);
}
