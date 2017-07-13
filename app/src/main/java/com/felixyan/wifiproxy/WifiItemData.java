package com.felixyan.wifiproxy;

/**
 * Created by yanfei on 2017/07/08.
 */

public class WifiItemData {
    private String ssid; // 热点名称
    private int netId; // 连接热点使用的真正id
    private boolean isConnected;
    private boolean isSaved;
    private boolean isProxyOn;
    private String proxy;
    private int level; // 信号强度
    private WifiCenter.WifiCipherType capabilities; // 安全性
    private int frequency;

    //private ScanResult scanResult; // 扫描结果
    //private WifiInfo wifiInfo; // 连接的热点信息
    //private DhcpInfo dhcpInfo; // 连接的热点dhcp信息

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isProxyOn() {
        return isProxyOn;
    }

    public void setProxyOn(boolean proxyOn) {
        isProxyOn = proxyOn;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public WifiCenter.WifiCipherType getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(WifiCenter.WifiCipherType capabilities) {
        this.capabilities = capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = WifiCenter.WifiCipherType.convert(capabilities);
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
