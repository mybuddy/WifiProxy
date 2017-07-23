package com.felixyan.wifiproxy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.felixyan.wifiproxy.WifiCenter;

/**
 * Created by yanfei on 2017/07/08.
 */

public class WifiItemData implements Parcelable{
    private String ssid; // 热点名称
    private int networkId; // 连接热点使用的真正id
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


    public WifiItemData() {}

    protected WifiItemData(Parcel in) {
        ssid = in.readString();
        networkId = in.readInt();
        isConnected = in.readByte() != 0;
        isSaved = in.readByte() != 0;
        isProxyOn = in.readByte() != 0;
        proxy = in.readString();
        level = in.readInt();
        frequency = in.readInt();
    }

    public static final Creator<WifiItemData> CREATOR = new Creator<WifiItemData>() {
        @Override
        public WifiItemData createFromParcel(Parcel in) {
            return new WifiItemData(in);
        }

        @Override
        public WifiItemData[] newArray(int size) {
            return new WifiItemData[size];
        }
    };

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ssid);
        dest.writeInt(networkId);
        dest.writeByte((byte) (isConnected ? 1 : 0));
        dest.writeByte((byte) (isSaved ? 1 : 0));
        dest.writeByte((byte) (isProxyOn ? 1 : 0));
        dest.writeString(proxy);
        dest.writeInt(level);
        dest.writeInt(frequency);
    }
}
