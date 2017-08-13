package com.felixyan.wifiproxy.dialog;

import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.model.WifiItemData;

/**
 * Created by yanfei on 2017/08/13.
 */

public class EnterpriseWifiItemDialog extends WifiItemDialog {
    private Spinner mSpEapMethod;
    private Spinner mSpPhase2Authentication;
    private Spinner mSpCaCertificate;
    private EditText mEtIdentity;
    private EditText mEtAnonymousIdentity;
    private EditText mEtPassword;

    public EnterpriseWifiItemDialog(@NonNull Context context, WifiItemData data) {
        super(context, data);
    }

    /**
     * 获取布局View
     *
     * @return
     */
    @Override
    protected View getLayoutView() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.layout_dialog_unconnected_enterprise, null);
        mSpEapMethod = (Spinner) view.findViewById(R.id.spEapMethod);
        mSpPhase2Authentication = (Spinner) view.findViewById(R.id.spPhase2Authentication);
        mSpCaCertificate = (Spinner) view.findViewById(R.id.spCaCertificate);
        mEtIdentity = (EditText) view.findViewById(R.id.etIdentity);
        mEtAnonymousIdentity = (EditText) view.findViewById(R.id.etAnonymousIdentity);
        mEtPassword = (EditText) view.findViewById(R.id.etPassword);

        return view;
    }

    /**
     * 连接网络
     *
     * @return
     */
    @Override
    protected boolean connectNetwork() {
        WifiItemData data = getData();
        // 连接
        boolean connectSucceed = false;
        if(!data.isConnected()) {
            if (!data.isSaved()) {
                WifiEnterpriseConfig eapConfig = new WifiEnterpriseConfig();
                //eapConfig.setP
                eapConfig.setIdentity("");
                eapConfig.setAnonymousIdentity(mEtAnonymousIdentity.getText().toString());
                eapConfig.setPassword("");
                eapConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
                connectSucceed = WifiCenter.getInstance(getContext()).connect(data.getSsid(),
                        mEtPassword.getText().toString(), data.getCapabilities(), eapConfig);
            } else {
                connectSucceed = WifiCenter.getInstance(getContext()).connect(data.getNetworkId());
            }
        }
        return connectSucceed;
    }

    private boolean check() {
        int eapMethodPos = mSpEapMethod.getSelectedItemPosition();
        int phase2MethodPos = mSpPhase2Authentication.getSelectedItemPosition();

        WifiEnterpriseConfig eapConfig = new WifiEnterpriseConfig();
        eapConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        eapConfig.setPhase2Method(0);
        eapConfig.setCaCertificate(null);
        eapConfig.setIdentity("");
        eapConfig.setAnonymousIdentity(mEtAnonymousIdentity.getText().toString());
        eapConfig.setPassword("");

        return false;
    }
}
