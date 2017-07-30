package com.felixyan.wifiproxy.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.felixyan.wifiproxy.R;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.adapter.IViewWrapper;
import com.felixyan.wifiproxy.dialog.ManualProxyDialog;
import com.felixyan.wifiproxy.model.ProxyInfoWrapper;
import com.felixyan.wifiproxy.model.StaticProxy;
import com.felixyan.wifiproxy.model.WifiItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanfei on 2017/7/21.
 */

public class DetailProxyLayout extends LinearLayout implements IDataView<ProxyInfoWrapper> {
    private Spinner mSpProxy;
    private ListView mLvManual;
    private View mPrlAuto;
    private EditText mEtProxyPacUrl;
    private ProxyInfoWrapper mData;

    private List<StaticProxy> mManualProxyList = new ArrayList<>();
    private ArrayAdapter<StaticProxy> mManualProxyAdapter;

    public DetailProxyLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public DetailProxyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DetailProxyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public DetailProxyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        inflate(context, R.layout.layout_detail_proxy, this);
        mSpProxy = (Spinner) findViewById(R.id.spProxy);
        mLvManual = (ListView) findViewById(R.id.lvManual);
        mPrlAuto = findViewById(R.id.prlAuto);
        mEtProxyPacUrl = (EditText) findViewById(R.id.etProxyPacUrl);

        mSpProxy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mLvManual.setVisibility(GONE);
                        mPrlAuto.setVisibility(GONE);
                        break;
                    case 1:
                        mLvManual.setVisibility(VISIBLE);
                        mPrlAuto.setVisibility(GONE);
                        break;
                    case 2:
                        mLvManual.setVisibility(GONE);
                        mPrlAuto.setVisibility(VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLvManual.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mManualProxyAdapter = new ArrayAdapter<>(getContext(), R.layout.layout_detail_proxy_list_item, R.id.ctvItem, mManualProxyList);
        mLvManual.setAdapter(mManualProxyAdapter);

        mLvManual.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                showPopupMenu();
                return true;
            }
        });

        View footer = inflate(getContext(), R.layout.layout_detail_proxy_list_item_footer, null);
        footer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ManualProxyDialog dialog = new ManualProxyDialog(getContext());
                dialog.show();
            }
        });
        mLvManual.addFooterView(footer);
    }

    private void showPopupMenu() {
        new AlertDialog.Builder(getContext())
                .setItems(R.array.manual_proxy_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            ManualProxyDialog manualProxyDialog = new ManualProxyDialog(getContext());
                            manualProxyDialog.show();
                        } else if (which == 1) {
                            Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public ProxyInfoWrapper getData() {
        return mData;
    }

    @Override
    public void setData(ProxyInfoWrapper data) {
        mData = data;

        if(data == null) {
            return;
        }

        if(data.getType() == WifiCenter.ProxySettings.STATIC) {
            mSpProxy.setSelection(1);
            List<StaticProxy> staticProxyList = data.getStaticProxyList();
            mManualProxyList.clear();
            /*mManualProxyList.add("192.168.1.14 (my pc1)");
            mManualProxyList.add("192.168.1.15 (my pc2)");
            mManualProxyList.add("192.168.1.16 (my pc3)");*/
            if(staticProxyList != null) {
                mManualProxyList.addAll(staticProxyList);
            }
            mManualProxyAdapter.notifyDataSetChanged();

            mLvManual.setVisibility(VISIBLE);
            mPrlAuto.setVisibility(GONE);
        } else if (data.getType() == WifiCenter.ProxySettings.PAC) {
            mSpProxy.setSelection(2);
            mEtProxyPacUrl.setText(data.getPacUrl());

            mLvManual.setVisibility(GONE);
            mPrlAuto.setVisibility(VISIBLE);
        }

    }
}
