package com.felixyan.wifiproxy.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.util.StringUtil;

/**
 * Created by yanfei on 2017/07/23.
 */

public class ManualProxyDialog extends AlertDialog implements DialogInterface.OnClickListener{
    public ManualProxyDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setTitle(StringUtil.getString(R.string.detail_proxy));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_manual_proxy, null);
        setView(view);

        setButton(BUTTON_POSITIVE, StringUtil.getString(R.string.dialog_button_save), this);
        setButton(BUTTON_NEGATIVE, StringUtil.getString(R.string.dialog_button_cancel), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
