package com.felixyan.wifiproxy.model;

import com.felixyan.wifiproxy.WifiCenter;

import java.util.List;

/**
 * Created by yanfei on 2017/7/21.
 */

public class ProxyInfoWrapper {
    private WifiCenter.ProxySettings type;
    // static
    private List<StaticProxy> staticProxyList;
    // pac
    private String pacUrl;

    public WifiCenter.ProxySettings getType() {
        return type;
    }

    public void setType(WifiCenter.ProxySettings type) {
        this.type = type;
    }

    public List<StaticProxy> getStaticProxyList() {
        return this.staticProxyList;
    }

    public void setStaticProxyList(List<StaticProxy> staticProxyList) {
        this.staticProxyList = staticProxyList;
    }

    public String getPacUrl() {
        return pacUrl;
    }

    public void setPacUrl(String pacUrl) {
        this.pacUrl = pacUrl;
    }
}
