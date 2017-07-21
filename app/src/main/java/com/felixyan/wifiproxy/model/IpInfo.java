package com.felixyan.wifiproxy.model;

/**
 * Created by yanfei on 2017/7/21.
 */

public class IpInfo {
    public static final int TYPE_DHCP = 0;
    public static final int TYPE_STATIC = 1;

    private int type;
    private String ipAddress;
    private String gateway;
    private int prefixLength;
    private String dns1;
    private String dns2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public int getPrefixLength() {
        return prefixLength;
    }

    public void setPrefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }
}
