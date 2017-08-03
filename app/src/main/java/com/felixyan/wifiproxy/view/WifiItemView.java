package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.activity.DetailActivity;
import com.felixyan.wifiproxy.adapter.IViewWrapper;
import com.felixyan.wifiproxy.model.WifiItemData;
import com.felixyan.wifiproxy.util.StringUtil;

/**
 * Created by yanfei on 2017/07/02.
 */

public class WifiItemView extends FrameLayout implements IViewWrapper<WifiItemData> {
    private TextView mTvSsid;
    private TextView mTvProxy;
    private SwitchCompat mSwitchProxy;
    private ImageView mIvSignal;
    private ImageView mIvMore;

    private WifiItemData mData;

    public WifiItemView(@NonNull Context context) {
        super(context);

        init(context, null);
    }

    public WifiItemView(@NonNull Context context,
                        @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WifiItemView(@NonNull Context context,
                        @Nullable AttributeSet attrs,
                        @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("newApi")
    public WifiItemView(@NonNull Context context,
                        @Nullable AttributeSet attrs,
                        @AttrRes int defStyleAttr,
                        @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_main_list_item_wifi, this);

        mTvSsid = (TextView) findViewById(R.id.tvSsid);
        mTvProxy = (TextView) findViewById(R.id.tvProxy);
        mSwitchProxy = (SwitchCompat) findViewById(R.id.switchProxy);
        mIvSignal = (ImageView) findViewById(R.id.ivSignal);
        mIvMore = (ImageView) findViewById(R.id.ivMore);

        mIvMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mData != null) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_WIFI_INFO, mData);
                    getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setData(int position, WifiItemData data) {
        mData = data;

        mTvSsid.setText(data.getSsid());
        // 突出当前连接的WIFI
        if (data.isConnected()) {
            mTvSsid.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            // 普通颜色
            mTvSsid.setTextColor(ContextCompat.getColor(getContext(), R.color.text_primary_dark));
        }

        if (!TextUtils.isEmpty(data.getProxy())) {
            mTvProxy.setText(StringUtil.getString(R.string.list_proxy_format, data.getProxy()));
            mTvProxy.setVisibility(VISIBLE);
            if (data.isConnected()) {
                mSwitchProxy.setVisibility(VISIBLE);
                mSwitchProxy.setChecked(data.isProxyOn());
            } else {
                mSwitchProxy.setVisibility(GONE);
            }
        } else {
            mTvProxy.setVisibility(GONE);
            mSwitchProxy.setVisibility(GONE);
        }

        if (WifiCenter.OPEN.equals(WifiCenter.getSecurity(data.getCapabilities()))) {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar);
        } else {
            mIvSignal.setImageResource(R.drawable.signal_wifi_bar_lock);
        }
        int signalLevel = WifiManager.calculateSignalLevel(data.getLevel(), 5);
        mIvSignal.getDrawable().setLevel(signalLevel);
    }
}
