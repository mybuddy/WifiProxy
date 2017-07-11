package com.felixyan.wifiproxy.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.WifiItemData;
import com.felixyan.wifiproxy.util.StringUtil;

/**
 * Created by yanfei on 2017/7/11.
 */

public class UnConnectedDialog extends AlertDialog implements DialogInterface.OnClickListener{
    private WifiItemData mWifiItemData;
    private EditText mEtPassword;

    public UnConnectedDialog(@NonNull Context context, WifiItemData data) {
        super(context);

        mWifiItemData = data;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_unconnected_dialog, null);
        mEtPassword = (EditText) view.findViewById(R.id.etPassword);
        setView(view);

        setTitle(mWifiItemData.getSsid());
        setButton(BUTTON_POSITIVE, StringUtil.getString(R.string.dialog_button_connect), this);
        setButton(BUTTON_NEGATIVE, StringUtil.getString(R.string.dialog_button_cancel), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            //case BUTTON_POSITIVE
        }
    }
}
