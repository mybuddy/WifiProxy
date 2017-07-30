package com.felixyan.wifiproxy.model;

import com.felixyan.wifiproxy.StaticIpConfiguration;
import com.felixyan.wifiproxy.WifiCenter;

/**
 * Created by yanfei on 2017/7/21.
 */

public class IpInfoWrapper {
    private WifiCenter.IpAssignment type;
    private StaticIpConfiguration staticIpConfiguration;

    public WifiCenter.IpAssignment getType() {
        return type;
    }

    public void setType(WifiCenter.IpAssignment type) {
        this.type = type;
    }

    public StaticIpConfiguration getStaticIpConfiguration() {
        return staticIpConfiguration;
    }

    public void setStaticIpConfiguration(StaticIpConfiguration staticIpConfiguration) {
        this.staticIpConfiguration = staticIpConfiguration;
    }
}
