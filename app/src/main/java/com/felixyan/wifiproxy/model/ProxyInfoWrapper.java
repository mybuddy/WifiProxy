package com.felixyan.wifiproxy.model;

import com.felixyan.wifiproxy.WifiCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanfei on 2017/7/21.
 */

public class ProxyInfoWrapper {
    private String ssid;
    private WifiCenter.ProxySettings type;
    // manual
    private List<ManualProxy> manualProxyList;
    // pac
    private String pacUrl;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public WifiCenter.ProxySettings getType() {
        return type;
    }

    public void setType(WifiCenter.ProxySettings type) {
        this.type = type;
    }

    public List<ManualProxy> getManualProxyList() {
        return this.manualProxyList;
    }

    public void setManualProxyList(List<ManualProxy> manualProxyList) {
        this.manualProxyList = manualProxyList;
    }

    public void addManualProxy(ManualProxy proxy) {
        if(manualProxyList == null) {
            manualProxyList = new ArrayList<>();
        }
        manualProxyList.add(proxy);
    }

    public String getPacUrl() {
        return pacUrl;
    }

    public void setPacUrl(String pacUrl) {
        this.pacUrl = pacUrl;
    }
}
