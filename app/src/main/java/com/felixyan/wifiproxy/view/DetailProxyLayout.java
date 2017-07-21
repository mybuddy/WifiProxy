package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.felixyan.wifiproxy.adapter.IViewWrapper;

/**
 * Created by yanfei on 2017/7/21.
 */

public class DetailProxyLayout extends LinearLayout implements IViewWrapper {
    public DetailProxyLayout(Context context) {
        super(context);
    }

    public DetailProxyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailProxyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DetailProxyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void setData(int position, Object data) {

    }
}
