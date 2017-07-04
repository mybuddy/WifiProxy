package com.felixyan.wifiproxy;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
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

    public WifiListItemView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public WifiListItemView(@NonNull Context context,
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
    }

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
        WifiConfiguration config = Wifi.getInstance(getContext()).getSsidConfig(data.SSID);
        if(config != null) {

        }
        if(Wifi.OPEN.equals(Wifi.getScanResultSecurity(data))) {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar);
        } else {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar_lock);
        }
        int signalLevel = WifiManager.calculateSignalLevel(data.level, 5);
        mIvSignal.getDrawable().setLevel(signalLevel);
    }
}
