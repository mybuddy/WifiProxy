package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.felixyan.wifiproxy.adapter.IViewWrapper;
import com.felixyan.wifiproxy.model.GeneralInfo;

/**
 * Created by yanfei on 2017/7/21.
 */

public class DetailGeneralLayout extends LinearLayout implements IViewWrapper<GeneralInfo> {
    public DetailGeneralLayout(Context context) {
        super(context);
    }

    public DetailGeneralLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailGeneralLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DetailGeneralLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void setData(int position, GeneralInfo data) {

    }
}
