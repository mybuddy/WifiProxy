package com.felixyan.wifiproxy.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.adapter.TextAdapter;
import com.felixyan.wifiproxy.model.ManualProxy;
import com.felixyan.wifiproxy.util.ConversionUtil;
import com.felixyan.wifiproxy.util.StringUtil;

/**
 * Created by yanfei on 2017/07/23.
 */

public class ManualProxyDialog extends AlertDialog implements DialogInterface.OnClickListener {
    private EditText mEtHostname;
    private EditText mEtPort;
    private EditText mEtBypassFor;
    private OnDialogButtonClickListener mOnDialogButtonClickListener;
    private TextAdapter mTextAdapter = new TextAdapter() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String hostname = mEtHostname.getText() != null ? mEtHostname.getText().toString().trim() : "";
            String port = mEtPort.getText() != null ? mEtPort.getText().toString().trim() : "";

            getButton(BUTTON_POSITIVE).setEnabled(hostname.length() != 0 && ConversionUtil.str2Int(port) != 0);
        }
    };

    public ManualProxyDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public void setOnDialogButtonClickListener(
            OnDialogButtonClickListener onDialogButtonClickListener) {
        mOnDialogButtonClickListener = onDialogButtonClickListener;
    }

    private void initView() {
        setTitle(StringUtil.getString(R.string.detail_proxy));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_manual_proxy, null);
        setView(view);

        mEtHostname = (EditText) view.findViewById(R.id.etHostname);
        mEtPort = (EditText) view.findViewById(R.id.etPort);
        mEtBypassFor = (EditText) view.findViewById(R.id.etBypassFor);

        mEtHostname.addTextChangedListener(mTextAdapter);
        mEtPort.addTextChangedListener(mTextAdapter);

        setButton(BUTTON_POSITIVE, StringUtil.getString(R.string.dialog_button_save), this);
        setButton(BUTTON_NEGATIVE, StringUtil.getString(R.string.dialog_button_cancel), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if(mOnDialogButtonClickListener != null) {
                    String hostname = mEtHostname.getText() != null ? mEtHostname.getText().toString().trim() : "";
                    String port = mEtPort.getText() != null ? mEtPort.getText().toString().trim() : "";
                    String byPassFor = mEtBypassFor.getText() != null ? mEtBypassFor.getText().toString() : "";
                    ManualProxy proxy = new ManualProxy();
                    proxy.setHostname(hostname);
                    proxy.setPort(ConversionUtil.str2Int(port));
                    proxy.setExclusionHosts(byPassFor);
                    proxy.setInUse(true);
                    mOnDialogButtonClickListener.onPositiveButtonClick(proxy);
                }
                break;
        }
        dismiss();
    }

    public interface OnDialogButtonClickListener {
        void onPositiveButtonClick(ManualProxy proxy);
    }
}
