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
import java.util.List;

public class MainActivity extends AbstractActivity {
    private static final int REQUEST_CODE_PERMISSION = 1;
    private String[] mPermission = new String[] {
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private SwitchCompat mSwitchWifi;
    private RecyclerView mRvWifiList;

    private Wifi mWifi;
    private Handler mHandler;
    private List<ScanResult> mScanResultList;
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

    private void initView() {
        mSwitchWifi = (SwitchCompat) findViewById(R.id.switchWifi);
        mRvWifiList = (RecyclerView) findViewById(R.id.rvWifiList);
        mRvWifiList.setLayoutManager(new LinearLayoutManager(this));

        mSwitchWifi.setChecked(Wifi.getInstance(this).isWifiEnabled());
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
        mWifi = Wifi.getInstance(this);
        mHandler = new Handler();
        mScanResultList = new ArrayList<>();
        mAdapter = new WifiRecyclerViewAdapter(this, mScanResultList);
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
        mScanResultList.addAll(resultList);
        mAdapter.notifyDataSetChanged();
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
