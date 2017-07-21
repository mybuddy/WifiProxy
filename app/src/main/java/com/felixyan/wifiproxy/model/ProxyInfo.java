package com.felixyan.wifiproxy.model;

/**
 * Created by yanfei on 2017/7/21.
 */

public class ProxyInfo {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_MANUAL = 1;
    public static final int TYPE_AUTO_CONFIG = 2;

    private int type;
    private String hostname;
    private int port;
    private String bypassFor;
    private String pacUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getBypassFor() {
        return bypassFor;
    }

    public void setBypassFor(String bypassFor) {
        this.bypassFor = bypassFor;
    }

    public String getPacUrl() {
        return pacUrl;
    }

    public void setPacUrl(String pacUrl) {
        this.pacUrl = pacUrl;
    }
}
