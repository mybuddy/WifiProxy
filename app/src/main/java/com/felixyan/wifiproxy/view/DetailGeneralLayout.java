package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.model.GeneralInfoWrapper;
import com.felixyan.wifiproxy.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanfei on 2017/7/21.
 */

public class DetailGeneralLayout extends LinearLayout implements IDataView<GeneralInfoWrapper> {
    //private EditText mEtPassword;
    private ListView mRvWifiInfo;
    private GeneralInfoWrapper mData;

    public DetailGeneralLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public DetailGeneralLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DetailGeneralLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public DetailGeneralLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        inflate(context, R.layout.layout_detail_general, this);

        //mEtPassword = (EditText) findViewById(R.id.etPassword);
        mRvWifiInfo = (ListView) findViewById(R.id.rvWifiInfo);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public GeneralInfoWrapper getData() {
        return mData;
    }

    @Override
    public void setData(final GeneralInfoWrapper data) {
        mData = data;

        if(data == null) {
            return;
        }

        List<Map<String, String>> list = createAdapterData(data);

        SimpleAdapter adapter = new SimpleAdapter(getContext(),
                list, R.layout.layout_detail_general_list_item,
                new String[]{ "name", "value" }, new int[] { R.id.tvName, R.id.tvValue });
        mRvWifiInfo.setAdapter(adapter);
    }

    private List<Map<String, String>> createAdapterData(final GeneralInfoWrapper info) {
        List<Map<String, String>> list = new ArrayList<>();
        // 信号强度
        list.add(new HashMap<String, String>() {
            {
                put("name", StringUtil.getString(R.string.detail_general_signal_strength));
                put("value", StringUtil.getString(R.string.detail_general_signal_strength_format, info.getLevel()));
            }
        });
        // 安全性
        list.add(new HashMap<String, String>() {
            {
                put("name", StringUtil.getString(R.string.detail_general_security));
                put("value", info.getCapabilities());
            }
        });

        if(info.isConnected()) {
            // 连接速度
            list.add(new HashMap<String, String>() {
                {
                    put("name", StringUtil.getString(R.string.detail_general_link_speed));
                    put("value", info.getSpeed() + WifiInfo.LINK_SPEED_UNITS);
                }
            });
            // IP地址
            list.add(new HashMap<String, String>() {
                {
                    put("name", StringUtil.getString(R.string.detail_general_ip_address));
                    put("value", info.getIpAddress());
                }
            });
            // 子网掩码
            list.add(new HashMap<String, String>() {
                {
                    put("name", StringUtil.getString(R.string.detail_general_subnet_mask));
                    put("value", info.getMask());
                }
            });
            // 路由器
            list.add(new HashMap<String, String>() {
                {
                    put("name", StringUtil.getString(R.string.detail_general_gateway));
                    put("value", info.getGateway());
                }
            });
        }
        return list;
    }
}
