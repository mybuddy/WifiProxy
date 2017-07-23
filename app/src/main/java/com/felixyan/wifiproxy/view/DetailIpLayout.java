package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.adapter.IViewWrapper;
import com.felixyan.wifiproxy.model.IpInfo;
import com.felixyan.wifiproxy.model.WifiItemData;

/**
 * Created by yanfei on 2017/7/21.
 */

public class DetailIpLayout extends LinearLayout implements IViewWrapper<WifiItemData>{
    private Spinner mSpIp;
    private View mPrlStaticIp;

    public DetailIpLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public DetailIpLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DetailIpLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public DetailIpLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        inflate(context, R.layout.layout_detail_ip, this);
        mSpIp = (Spinner) findViewById(R.id.spIP);
        mPrlStaticIp = findViewById(R.id.prlStaticIp);

        mSpIp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    mPrlStaticIp.setVisibility(GONE);
                } else if (position == 1) {
                    mPrlStaticIp.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setData(int position, WifiItemData data) {
        IpInfo info = new IpInfo();
    }
}
