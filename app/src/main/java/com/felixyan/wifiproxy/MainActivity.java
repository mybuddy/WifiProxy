package com.felixyan.wifiproxy;

import android.Manifest;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;

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

    private List<ScanResult> mScanResultList;
    private WifiRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        if(checkPermission(REQUEST_CODE_PERMISSION, mPermission)) {
            scan();
        }
    }

    private void initView() {
        mSwitchWifi = (SwitchCompat) findViewById(R.id.switchWifi);
        mRvWifiList = (RecyclerView) findViewById(R.id.rvWifiList);
        mRvWifiList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(PermissionUtil.checkPermissionGrantResult(grantResults)) {
            scan();
        }
    }

    private void initData() {
        mScanResultList = new ArrayList<>();
        mAdapter = new WifiRecyclerViewAdapter(this, mScanResultList);
    }

    private void scan() {
        List<ScanResult> resultList = Wifi.getInstance(this).getScanResults();
        mScanResultList.addAll(resultList);
        mAdapter.notifyDataSetChanged();
    }
}
