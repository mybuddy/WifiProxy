package com.felixyan.wifiproxy.activity;

import android.content.Intent;
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
import com.felixyan.wifiproxy.adapter.IViewWrapper;
import com.felixyan.wifiproxy.model.WifiItemData;
import com.felixyan.wifiproxy.view.DetailGeneralLayout;
import com.felixyan.wifiproxy.view.DetailIpLayout;
import com.felixyan.wifiproxy.view.DetailProxyLayout;

public class DetailActivity extends AppCompatActivity {
    //public static final String EXTRA_SSID = "extra_ssid";
    public static final String EXTRA_WIFI_INFO = "extra_wifi_info";

    private RadioGroup mRgTab;
    private FrameLayout mFlContainer;
    private SparseArray<IViewWrapper<WifiItemData>> mContentLayoutArray = new SparseArray<>(3);
    private WifiItemData mData;

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
                IViewWrapper<WifiItemData> contentLayout = mContentLayoutArray.get(checkedId);
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
                            break;
                        case R.id.rbProxy:
                            contentLayout = new DetailProxyLayout(DetailActivity.this);
                            break;
                        case R.id.rbIp:
                            contentLayout = new DetailIpLayout(DetailActivity.this);
                            break;
                        default:
                            return;
                    }
                    contentLayout.setData(0, mData);
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
                onBackPressed();
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}