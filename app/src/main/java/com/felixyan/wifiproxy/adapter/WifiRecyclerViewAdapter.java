package com.felixyan.wifiproxy.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.view.ViewGroup;
import com.felixyan.wifiproxy.WifiListItemView;

import java.util.List;

/**
 * Created by yanfei on 2017/07/02.
 */

public class WifiRecyclerViewAdapter extends BaseRecyclerViewAdapter<ScanResult> {
    private WifiInfo mConnectionInfo;

    public WifiRecyclerViewAdapter(Context context, WifiInfo connectionInfo, List<ScanResult> dataList) {
        super(context, dataList);
        mConnectionInfo = connectionInfo;
    }

    @Override
    IViewWrapper newViewWrapper(ViewGroup parent, int viewType) {
        return new WifiListItemView(getContext(), mConnectionInfo);
    }
}
