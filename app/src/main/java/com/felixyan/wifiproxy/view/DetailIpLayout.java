package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.StaticIpConfiguration;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.adapter.IViewWrapper;
import com.felixyan.wifiproxy.model.WifiItemData;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by yanfei on 2017/7/21.
 */

public class DetailIpLayout extends LinearLayout implements IViewWrapper<WifiItemData>{
    private Spinner mSpIp;
    private View mPrlStaticIp;
    private EditText mEtIpAddress;
    private EditText mEtGateway;
    private EditText mEtPrefixLength;
    private EditText mEtDns1;
    private EditText mEtDns2;

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
        mEtIpAddress = (EditText) findViewById(R.id.etIpAddress);
        mEtGateway = (EditText) findViewById(R.id.etGateway);
        mEtPrefixLength = (EditText) findViewById(R.id.etPrefixLength);
        mEtDns1 = (EditText) findViewById(R.id.etDns1);
        mEtDns2 = (EditText) findViewById(R.id.etDns2);

        mSpIp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    // DHCP
                    mPrlStaticIp.setVisibility(GONE);
                } else if (position == 1) {
                    // Static
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
        //IpInfo info = new IpInfo();
        if(data.isConnected() || data.isSaved()) {
            WifiConfiguration config = WifiCenter.getInstance(getContext()).getSsidConfig(data.getSsid());
            if(config != null) {
                WifiCenter.IpAssignment ipAssignment = WifiCenter.getIpAssignment(config);
                if(ipAssignment == WifiCenter.IpAssignment.STATIC) {
                    mSpIp.setSelection(1);
                    StaticIpConfiguration staticIpConfiguration = WifiCenter.getStaticIpConfiguration(config);
                    if(staticIpConfiguration != null) {
                        mEtIpAddress.setText(staticIpConfiguration.getIpAddress().getAddress().getHostAddress());
                        mEtGateway.setText(staticIpConfiguration.getGateway().getHostAddress());
                        mEtPrefixLength.setText(staticIpConfiguration.getIpAddress().getPrefixLength());
                        ArrayList<InetAddress> dnsServers = staticIpConfiguration.getDnsServers();
                        if(!dnsServers.isEmpty()) {
                            mEtDns1.setText(dnsServers.get(0).getHostAddress());
                            if(dnsServers.size() > 1) {
                                mEtDns2.setText(dnsServers.get(1).getHostAddress());
                            }
                        }
                    }
                } else if (ipAssignment == WifiCenter.IpAssignment.DHCP) {
                    mSpIp.setSelection(0);
                }
            }
        }
    }
}
