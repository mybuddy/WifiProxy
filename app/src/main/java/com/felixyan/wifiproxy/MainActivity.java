package com.felixyan.wifiproxy;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;

import com.felixyan.wifiproxy.adapter.WifiRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AbstractActivity {
    private static final int REQUEST_CODE_PERMISSION = 1;
    private String[] mPermission = new String[] {
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private SwitchCompat mSwitchWifi;
    private RecyclerView mRvWifiList;

    private WifiCenter mWifi;
    private Handler mHandler;
    private List<ScanResult> mScanResultList;
    private Map<String, ScanResult> mScanResultMap;
    private WifiRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        registerReceiver(mScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(checkPermission(REQUEST_CODE_PERMISSION, mPermission)) {
            scan();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mScanReceiver);
        super.onDestroy();
    }

    private void initView() {
        mSwitchWifi = (SwitchCompat) findViewById(R.id.switchWifi);
        mRvWifiList = (RecyclerView) findViewById(R.id.rvWifiList);
        mRvWifiList.setLayoutManager(new LinearLayoutManager(this));

        mSwitchWifi.setChecked(WifiCenter.getInstance(this).isWifiEnabled());
        mSwitchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mWifi.setWifiEnabled(isChecked);
                if(isChecked) {
                    scan();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(PermissionUtil.checkPermissionGrantResult(grantResults)) {
            scan();
        }
    }

    private void initData() {
        mWifi = WifiCenter.getInstance(this);
        mHandler = new Handler();
        mScanResultList = new ArrayList<>();
        mScanResultMap = new HashMap<>();
        mAdapter = new WifiRecyclerViewAdapter(this, mWifi.getConnectionInfo(), mScanResultList);
        mRvWifiList.setAdapter(mAdapter);
    }

    private void scan() {
        /*if(mWifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            doScan();
            return;
        }

        if(mWifi.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            mHandler.postDelayed(mScanRunnable, 100);
        }*/

        mWifi.startScan();
    }

    private void doScan() {
        Log.d("main", "doScan");
        List<ScanResult> resultList = mWifi.getScanResults();
        Log.d("main", "scan result: " + resultList.size());
        mScanResultList.clear();
        mScanResultList.addAll(updateScanResultMap(resultList).values());
        mAdapter.notifyDataSetChanged();
    }

    private Map<String, ScanResult> updateScanResultMap(List<ScanResult> resultList) {
        mScanResultMap.clear();

        // 这里需要根据SSID做过滤，因为
        // 1. 存在不同AP发出相同SSID热点的情况（如公司等比较大的场所里通常会这么做）
        // 2. SSID相同但频段不同的热点（2.4G、5G）
        for (ScanResult result : resultList) {
            if(!mScanResultMap.containsKey(result.SSID)) {
                // WIFI尚未添加，则添加
                mScanResultMap.put(result.SSID, result);
            } else {
                // WIFI已添加，则比较信号强弱，取信号强的
                ScanResult exist = mScanResultMap.get(result.SSID);
                if(result.level > exist.level) {
                    mScanResultMap.put(result.SSID, result);
                }
            }
        }
        return mScanResultMap;
    }

    private Runnable mScanRunnable = new Runnable() {
        @Override
        public void run() {
            scan();
        }
    };

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doScan();
        }
    };
}
