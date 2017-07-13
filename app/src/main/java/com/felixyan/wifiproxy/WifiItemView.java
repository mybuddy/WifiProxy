package com.felixyan.wifiproxy;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.felixyan.wifiproxy.adapter.IViewWrapper;

/**
 * Created by yanfei on 2017/07/02.
 */

public class WifiItemView extends FrameLayout implements IViewWrapper<WifiItemData> {
    private TextView mTvSsid;
    private TextView mTvProxy;
    private SwitchCompat mSwitchProxy;
    private ImageView mIvSignal;

    public WifiItemView(@NonNull Context context) {
        super(context);

        init(context, null);
    }

    /*public WifiListItemView(@NonNull Context context,
                            @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WifiListItemView(@NonNull Context context,
                            @Nullable AttributeSet attrs,
                            @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("newApi")
    public WifiListItemView(@NonNull Context context,
                            @Nullable AttributeSet attrs,
                            @AttrRes int defStyleAttr,
                            @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }*/

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_wifi_list_item, this);

        mTvSsid = (TextView) findViewById(R.id.tvSsid);
        mTvProxy = (TextView) findViewById(R.id.tvProxy);
        mSwitchProxy = (SwitchCompat) findViewById(R.id.switchProxy);
        mIvSignal = (ImageView) findViewById(R.id.ivSignal);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setData(int position, WifiItemData data) {
        mTvSsid.setText(data.getSsid());
        // 突出当前连接的WIFI
        if(data.isConnected()) {
            mTvSsid.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            // 普通颜色
            mTvSsid.setTextColor(ContextCompat.getColor(getContext(), R.color.text_primary_dark));
        }

        if(!TextUtils.isEmpty(data.getProxy())) {
            mTvProxy.setText("代理 " + data.getProxy());
            mTvProxy.setVisibility(VISIBLE);
            if(data.isConnected()) {
                mSwitchProxy.setVisibility(VISIBLE);
                mSwitchProxy.setChecked(data.isProxyOn());
            } else {
                mSwitchProxy.setVisibility(GONE);
            }
        } else {
            mTvProxy.setVisibility(GONE);
            mSwitchProxy.setVisibility(GONE);
        }

        if(WifiCenter.OPEN.equals(WifiCenter.getSecurity(data.getCapabilities().name()))) {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar);
        } else {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar_lock);
        }
        int signalLevel = WifiManager.calculateSignalLevel(data.getLevel(), 5);
        mIvSignal.getDrawable().setLevel(signalLevel);
    }
}
