package com.felixyan.wifiproxy.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.ViewGroup;
import com.felixyan.wifiproxy.WifiListItemView;

import java.util.List;

/**
 * Created by yanfei on 2017/07/02.
 */

public class WifiRecyclerViewAdapter extends BaseRecyclerViewAdapter<ScanResult> {
    public WifiRecyclerViewAdapter(Context context, List<ScanResult> dataList) {
        super(context, dataList);
    }

    @Override
    IViewWrapper newViewWrapper(ViewGroup parent, int viewType) {
        return new WifiListItemView(getContext());
    }
}
