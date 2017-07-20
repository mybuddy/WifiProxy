package com.felixyan.wifiproxy;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ProxyInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.felixyan.wifiproxy.adapter.OnListItemClickListener;
import com.felixyan.wifiproxy.adapter.WifiRecyclerViewAdapter;
import com.felixyan.wifiproxy.dialog.WifiItemDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private List<WifiItemData> mSavedWifiList;
    private List<WifiItemData> mNearbyWifiList;
    private Map<String, ScanResult> mScanResultMap;
    private WifiRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

        registerReceiver(mScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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
        mRvWifiList.setAdapter(mAdapter);
        mAdapter.setOnListItemClickListener(new OnListItemClickListener<WifiItemData>() {
            @Override
            public void onItemClick(View v, int viewType, final int position, WifiItemData data) {
                /*WifiItemDialog dialog = new WifiItemDialog(MainActivity.this, data);
                dialog.setOnDialogResultListener(new WifiItemDialog.OnDialogResultListener() {
                    @Override
                    public void onDialogResult(int result, WifiItemData data) {
                        if(result == WifiItemDialog.DIALOG_RESULT_CONNECT_SAVED_NETWORK) {
                            // do nothing
                            mAdapter.notifyDataSetChanged();
                        } else if (result == WifiItemDialog.DIALOG_RESULT_CONNECT_NEW_NETWORK) {
                            mSavedWifiList.add(0, data);
                            mNearbyWifiList.remove(data);
                            mAdapter.notifyDataSetChanged();
                        } else if (result == WifiItemDialog.DIALOG_RESULT_REMOVE_NETWORK) {
                            mSavedWifiList.remove(data);
                            mNearbyWifiList.add(data);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show();*/

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_SSID, data.getSsid());
                startActivity(intent);
            }
        });

        mSwitchWifi.setChecked(WifiCenter.getInstance(this).isWifiEnabled());
        mSwitchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mWifi.setWifiEnabled(isChecked);
                if(isChecked) {
                    if(checkPermission(REQUEST_CODE_PERMISSION, mPermission)) {
                        mWifi.startScan();
                    }
                }
            }
        });

        if(mSwitchWifi.isChecked()) {
            if(checkPermission(REQUEST_CODE_PERMISSION, mPermission)) {
                mWifi.startScan();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(PermissionUtil.checkPermissionGrantResult(grantResults)) {
            mWifi.startScan();
        }
    }

    private void initData() {
        mWifi = WifiCenter.getInstance(this);
        mSavedWifiList = new ArrayList<>();
        mNearbyWifiList = new ArrayList<>();
        mScanResultMap = new HashMap<>();
        mAdapter = new WifiRecyclerViewAdapter(this, mSavedWifiList, mNearbyWifiList);
    }

    private void updateScanResult() {
        Log.d("main", "doScan");
        List<ScanResult> scanResultList = mWifi.getScanResults();
        Log.d("main", "scan result: " + scanResultList.size());

        mSavedWifiList.clear();
        mNearbyWifiList.clear();
        List<WifiItemData> list = convertToWifiItemDataList(scanResultList);
        for (WifiItemData item : list) {
            if(item.isConnected() || item.isSaved()) {
                mSavedWifiList.add(item);
            } else {
                mNearbyWifiList.add(item);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private List<WifiItemData> convertToWifiItemDataList(List<ScanResult> resultList) {
        mScanResultMap.clear();

        // SSID去重，因为
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

        // 填充Wifi数据列表
        List<WifiItemData> list = new ArrayList<>();
        WifiInfo connectionInfo = mWifi.getConnectionInfo();
        List<WifiConfiguration> configList = mWifi.getConfiguredNetworks();

        for(ScanResult scan : mScanResultMap.values()) {
            WifiItemData data = new WifiItemData();
            data.setSsid(scan.SSID);
            data.setCapabilities(scan.capabilities);
            data.setLevel(scan.level);
            data.setFrequency(scan.frequency);

            // 获取已保存的信息
            if(configList != null && !configList.isEmpty()) {
                for(WifiConfiguration config : configList) {
                    if(WifiCenter.isSameSsid(data.getSsid(), config.SSID)) {
                        data.setSaved(true);
                        data.setNetworkId(config.networkId);

                        if (WifiCenter.getProxySettings(config) == WifiCenter.ProxySettings.STATIC) {
                            ProxyInfo proxyInfo = WifiCenter.getHttpProxy(config);
                            if (proxyInfo != null) {
                                data.setProxy(proxyInfo.getHost() + ":" + proxyInfo.getPort());
                                data.setProxyOn(true);
                            }
                        }
                        break;
                    }
                }
            }

            // 获取已连接的信息
            if(connectionInfo != null) {
                if(WifiCenter.isSameSsid(data.getSsid(), connectionInfo.getSSID())) {
                    data.setNetworkId(connectionInfo.getNetworkId());
                    data.setConnected(true);
                }
            }

            list.add(data);
        }

        // 按已连接、已保存、SSID字母序排序
        Collections.sort(list, new Comparator<WifiItemData>() {
            @Override
            public int compare(WifiItemData data1, WifiItemData data2) {
                if(data1.isConnected() && !data2.isConnected()) {
                    return -1;
                }
                if(!data1.isConnected() && data2.isConnected()) {
                    return 1;
                }
                if(data1.isSaved() && !data2.isSaved()) {
                    return -1;
                }
                if(!data1.isSaved() && data2.isSaved()) {
                    return 1;
                }
                return data1.getSsid().compareToIgnoreCase(data2.getSsid());
            }
        });

        return list;
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateScanResult();
        }
    };
}
