package com.felixyan.wifiproxy.dialog;

import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.model.WifiItemData;

/**
 * Created by yanfei on 2017/08/13.
 */

public class CommonWifiItemDialog extends WifiItemDialog {
    private EditText mEtPassword;

    public CommonWifiItemDialog(@NonNull Context context, WifiItemData data) {
        super(context, data);
    }

    @Override
    protected View getLayoutView() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.layout_dialog_unconnected, null);
        mEtPassword = (EditText) view.findViewById(R.id.etPassword);
        return view;
    }

    @Override
    protected boolean connectNetwork() {
        WifiItemData data = getData();
        // 连接
        boolean connectSucceed = false;
        if(!data.isConnected()) {
            if (!data.isSaved()) {
                connectSucceed = WifiCenter.getInstance(getContext()).connect(data.getSsid(),
                        mEtPassword.getText().toString(), data.getCapabilities(), null);
            } else {
                connectSucceed = WifiCenter.getInstance(getContext()).connect(data.getNetworkId());
            }
        }
        return connectSucceed;
    }
}
