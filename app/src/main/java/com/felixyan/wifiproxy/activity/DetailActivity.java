package com.felixyan.wifiproxy.activity;

import android.content.Intent;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.dao.ManualProxyDao;
import com.felixyan.wifiproxy.model.IpInfoWrapper;
import com.felixyan.wifiproxy.model.ManualProxy;
import com.felixyan.wifiproxy.model.ProxyInfoWrapper;
import com.felixyan.wifiproxy.model.WifiDetail;
import com.felixyan.wifiproxy.model.WifiItemData;
import com.felixyan.wifiproxy.view.DetailGeneralLayout;
import com.felixyan.wifiproxy.view.DetailIpLayout;
import com.felixyan.wifiproxy.view.DetailProxyLayout;
import com.felixyan.wifiproxy.view.IDataView;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    //public static final String EXTRA_SSID = "extra_ssid";
    public static final String EXTRA_WIFI_INFO = "extra_wifi_info";

    private RadioGroup mRgTab;
    private FrameLayout mFlContainer;
    private SparseArray<IDataView> mContentLayoutArray = new SparseArray<>(3);
    private WifiItemData mData;
    private WifiDetail mWifiDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);

        Intent intent = getIntent();
        if(intent != null) {
            mData = intent.getParcelableExtra(EXTRA_WIFI_INFO);
            if(mData != null) {
                getSupportActionBar().setTitle(mData.getSsid());
                mWifiDetail = new WifiDetail(mData);
            }
        }

        initView();
    }

    private void initView() {
        mRgTab = (RadioGroup) findViewById(R.id.rgTab);
        mFlContainer = (FrameLayout) findViewById(R.id.flContainer);

        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                IDataView contentLayout = mContentLayoutArray.get(checkedId);
                int index = contentLayout != null ? mContentLayoutArray.indexOfKey(checkedId) : -1;

                for(int i = 0; i < mContentLayoutArray.size(); i++) {
                    if(i != index) {
                        mContentLayoutArray.valueAt(i).getView().setVisibility(View.GONE);
                    }
                }

                if(contentLayout == null) {
                    switch (checkedId) {
                        case R.id.rbGeneral:
                            contentLayout = new DetailGeneralLayout(DetailActivity.this);
                            contentLayout.setData(mWifiDetail.getGeneralInfoWrapper());
                            break;
                        case R.id.rbProxy:
                            contentLayout = new DetailProxyLayout(DetailActivity.this);
                            contentLayout.setData(mWifiDetail.getProxyInfoWrapper());
                            break;
                        case R.id.rbIp:
                            contentLayout = new DetailIpLayout(DetailActivity.this);
                            contentLayout.setData(mWifiDetail.getIpInfoWrapper());
                            break;
                        default:
                            return;
                    }

                    mContentLayoutArray.put(checkedId, contentLayout);
                    mFlContainer.addView(contentLayout.getView());
                } else {
                    contentLayout.getView().setVisibility(View.VISIBLE);
                }
            }
        });

        mRgTab.check(R.id.rbGeneral);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                IDataView<ProxyInfoWrapper> proxyDataView = mContentLayoutArray.get(R.id.rbProxy);
                IDataView<IpInfoWrapper> ipDataView = mContentLayoutArray.get(R.id.rbIp);
                save(proxyDataView != null ? proxyDataView.getData() : null, ipDataView != null ? ipDataView.getData() : null);
                onBackPressed();
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save(ProxyInfoWrapper proxy, IpInfoWrapper ip) {
        WifiConfiguration config = WifiCenter.getInstance(getApplicationContext()).getSsidConfig(mWifiDetail.getSsid());
        // todo 判断config是否为空
        // proxy
        if(proxy != null) {
            if (proxy.getType() == WifiCenter.ProxySettings.STATIC) {
                List<ManualProxy> manualProxyList = proxy.getManualProxyList();
                ManualProxy proxyInUse = null;
                for (ManualProxy m : manualProxyList) {
                    m.save();
                    if (m.isInUse()) {
                        proxyInUse = m;
                    }
                }
                ProxyInfo proxyInfo = ProxyInfo.buildDirectProxy(proxyInUse.getHostname(),
                        proxyInUse.getPort(), proxyInUse.getExclusionHostList());
                WifiCenter.setProxy(config, proxy.getType(), proxyInfo);
            } else if (proxy.getType() == WifiCenter.ProxySettings.PAC) {
                ProxyInfo proxyInfo = ProxyInfo.buildPacProxy(Uri.parse(proxy.getPacUrl()));
                WifiCenter.setProxy(config, proxy.getType(), proxyInfo);
            }
        }

        // todo ip
    }
}
