package com.felixyan.wifiproxy.model;

import com.felixyan.wifiproxy.dao.StaticProxyDao;

import java.util.List;

/**
 * 手动代理配置
 *
 * Created by yanfei on 2017/07/30.
 */

public class StaticProxy {
    private String ssid;
    private String hostname;
    private int port;
    private String exclusionHosts;
    private boolean isInUse;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getExclusionHosts() {
        return exclusionHosts;
    }

    public void setExclusionHosts(String exclusionHosts) {
        this.exclusionHosts = exclusionHosts;
    }

    public void setExclusionHosts(String[] exclusionHosts) {
        StringBuilder sb = new StringBuilder();
        for (String host : exclusionHosts) {
            sb.append(host);
            sb.append(",");
        }
        if(sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        this.exclusionHosts = sb.toString();
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    public static List<StaticProxy> getList(String ssid) {
        return new StaticProxyDao().getList("ssid = ?", new String[]{ ssid });
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }
}
