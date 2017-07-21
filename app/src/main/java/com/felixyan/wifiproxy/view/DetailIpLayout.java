package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.felixyan.wifiproxy.adapter.IViewWrapper;
import com.felixyan.wifiproxy.model.IpInfo;

/**
 * Created by yanfei on 2017/7/21.
 */

public class DetailIpLayout extends LinearLayout implements IViewWrapper<IpInfo>{
    public DetailIpLayout(Context context) {
        super(context);
    }

    public DetailIpLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailIpLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DetailIpLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setData(int position, IpInfo data) {

    }
}
