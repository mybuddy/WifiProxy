package com.felixyan.wifiproxy.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.WifiItemData;
import com.felixyan.wifiproxy.util.StringUtil;

/**
 * Created by yanfei on 2017/7/11.
 */

public class WifiItemDialog extends AlertDialog implements DialogInterface.OnClickListener{
    public static final int DIALOG_RESULT_NOTHING_CHANGED = -1;
    public static final int DIALOG_RESULT_CONNECT_SAVED_NETWORK = 0;
    public static final int DIALOG_RESULT_CONNECT_NEW_NETWORK = 1;
    public static final int DIALOG_RESULT_REMOVE_NETWORK = 2;

    private WifiItemData mWifiItemData;
    private EditText mEtPassword;
    private OnDialogResultListener mOnDialogResultListener;

    public WifiItemDialog(@NonNull Context context, WifiItemData data) {
        super(context);

        mWifiItemData = data;
        initView();
    }

    public OnDialogResultListener getOnDialogResultListener() {
        return mOnDialogResultListener;
    }

    public void setOnDialogResultListener(
            OnDialogResultListener onDialogResultListener) {
        mOnDialogResultListener = onDialogResultListener;
    }

    private void initView() {
        setTitle(mWifiItemData.getSsid());

        if(!mWifiItemData.isConnected()) {
            if(!mWifiItemData.isSaved()) {
                View view = LayoutInflater.from(getContext()).inflate(
                        R.layout.layout_unconnected_dialog, null);
                mEtPassword = (EditText) view.findViewById(R.id.etPassword);
                setView(view);
            } else {
                setButton(BUTTON_NEUTRAL, StringUtil.getString(R.string.dialog_button_cancel_save), this);
            }

            setButton(BUTTON_POSITIVE, StringUtil.getString(R.string.dialog_button_connect), this);
            setButton(BUTTON_NEGATIVE, StringUtil.getString(R.string.dialog_button_cancel), this);
        } else {
            setButton(BUTTON_NEUTRAL, StringUtil.getString(R.string.dialog_button_cancel_save), this);
            setButton(BUTTON_NEGATIVE, StringUtil.getString(R.string.dialog_button_cancel), this);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int result = DIALOG_RESULT_NOTHING_CHANGED;
        switch (which) {
            case BUTTON_POSITIVE:
                // 连接
                if(connectNetwork()) {
                    result = mWifiItemData.isSaved() ? DIALOG_RESULT_CONNECT_SAVED_NETWORK : DIALOG_RESULT_CONNECT_NEW_NETWORK;
                    mWifiItemData.setSaved(true);
                    mWifiItemData.setConnected(true);
                }
                break;
            case BUTTON_NEUTRAL:
                // 取消保存
                if(removeNetwork()) {
                    result = DIALOG_RESULT_REMOVE_NETWORK;
                    mWifiItemData.setSaved(false);
                    mWifiItemData.setConnected(false);
                }
                break;
            case BUTTON_NEGATIVE:
            default:
                // 取消
                break;
        }

        if(mOnDialogResultListener != null) {
            mOnDialogResultListener.onDialogResult(result, mWifiItemData);
        }
        dismiss();
    }

    private boolean connectNetwork() {
        // 连接
        boolean connectSucceed = false;
        if(!mWifiItemData.isConnected()) {
            if (!mWifiItemData.isSaved()) {
                connectSucceed = WifiCenter.getInstance(getContext()).connect(mWifiItemData.getSsid(),
                        mEtPassword.getText().toString(), mWifiItemData.getCapabilities());
            } else {
                connectSucceed = WifiCenter.getInstance(getContext()).connect(mWifiItemData.getNetworkId());
            }
        }
        return connectSucceed;
    }

    private boolean removeNetwork() {
        // 取消保存
        boolean removeSucceed = false;
        if(mWifiItemData.isSaved()) {
            if(WifiCenter.getInstance(getContext()).removeNetwork(mWifiItemData.getNetworkId())) {
                removeSucceed = true;
            }
        }
        return removeSucceed;
    }

    public interface OnDialogResultListener {
        void onDialogResult(int result, WifiItemData data);
    }
}
