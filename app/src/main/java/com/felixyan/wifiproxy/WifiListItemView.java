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
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.felixyan.wifiproxy.adapter.IViewWrapper;

/**
 * Created by yanfei on 2017/07/02.
 */

public class WifiListItemView extends FrameLayout implements IViewWrapper<ScanResult> {
    private TextView mTvSsid;
    private TextView mTvProxy;
    private SwitchCompat mSwitchProxy;
    private ImageView mIvSignal;
    private WifiInfo mConnectionInfo;

    public WifiListItemView(@NonNull Context context, WifiInfo connectionInfo) {
        super(context);

        init(context, null);
        mConnectionInfo = connectionInfo;
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
    public void setData(int position, ScanResult data) {
        mTvSsid.setText(data.SSID);
        boolean hasProxy = false;
        // 当前连接的WIFI
        if(mConnectionInfo != null && WifiCenter.isSameSsid(data.SSID, mConnectionInfo.getSSID())) {
            // 颜色突出
            mTvSsid.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            // 检查是否有代理
            WifiConfiguration config = WifiCenter.getInstance(getContext()).getSsidConfig(data.SSID);
            if (config != null) {
                if (WifiCenter.getProxySettings(config) == WifiCenter.ProxySettings.STATIC) {
                    ProxyInfo proxyInfo = WifiCenter.getHttpProxy(config);
                    if (proxyInfo != null) {
                        String proxyStr = String.format("代理 %1$s:%2$d", proxyInfo.getHost(), proxyInfo.getPort());
                        mTvProxy.setText(proxyStr);
                        hasProxy = true;
                    }
                }
            }
        } else {
            // 普通颜色
            mTvSsid.setTextColor(ContextCompat.getColor(getContext(), R.color.text_primary_dark));
        }

        if(hasProxy) {
            mTvProxy.setVisibility(VISIBLE);
            mSwitchProxy.setVisibility(VISIBLE);
        } else {
            mTvProxy.setVisibility(GONE);
            mSwitchProxy.setVisibility(GONE);
        }
        if(WifiCenter.OPEN.equals(WifiCenter.getScanResultSecurity(data))) {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar);
        } else {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar_lock);
        }
        int signalLevel = WifiManager.calculateSignalLevel(data.level, 5);
        mIvSignal.getDrawable().setLevel(signalLevel);
    }
}
