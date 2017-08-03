package com.felixyan.wifiproxy.model;

import android.net.DhcpInfo;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

import com.felixyan.wifiproxy.App;
import com.felixyan.wifiproxy.WifiCenter;
import com.felixyan.wifiproxy.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanfei on 2017/07/30.
 */

public class WifiDetail {
    private String ssid;
    private GeneralInfoWrapper generalInfoWrapper;
    private ProxyInfoWrapper proxyInfoWrapper;
    private IpInfoWrapper ipInfoWrapper;

    public WifiDetail(WifiItemData data) {
        this.ssid = data.getSsid();

        WifiCenter wifiCenter = WifiCenter.getInstance(App.getInstance());
        setGeneralInfoWrapper(wifiCenter, data);

        if(data.isConnected() || data.isSaved()) {
            WifiConfiguration config = wifiCenter.getSsidConfig(data.getSsid());
            if(config != null) {
                setProxyInfoWrapper(data.getSsid(), config);
                setIpInfoWrapper(config);
            }
        }
    }

    public String getSsid() {
        return ssid;
    }

    public GeneralInfoWrapper getGeneralInfoWrapper() {
        return generalInfoWrapper;
    }

    public ProxyInfoWrapper getProxyInfoWrapper() {
        return proxyInfoWrapper;
    }

    public IpInfoWrapper getIpInfoWrapper() {
        return ipInfoWrapper;
    }

    private void setGeneralInfoWrapper(WifiCenter wifiCenter, WifiItemData data) {
        // 通用信息
        GeneralInfoWrapper info = new GeneralInfoWrapper();
        info.setConnected(data.isConnected());
        info.setLevel(data.getLevel());
        info.setCapabilities(data.getCapabilities());
        if(data.isConnected()) {
            WifiInfo wifiInfo = wifiCenter.getConnectionInfo();
            DhcpInfo dhcpInfo = wifiCenter.getDhcpInfo();

            info.setSpeed(wifiInfo.getLinkSpeed());
            info.setGateway(NetworkUtil.intToInetAddress(dhcpInfo.gateway).getHostAddress());
            info.setMask(NetworkUtil.intToInetAddress(dhcpInfo.netmask).getHostAddress());
            info.setIpAddress(NetworkUtil.intToInetAddress(wifiInfo.getIpAddress()).getHostAddress());
        }

        this.generalInfoWrapper = info;
    }

    private void setProxyInfoWrapper(String ssid, WifiConfiguration config) {
        // 代理配置
        ProxyInfoWrapper proxyInfoWrapper = new ProxyInfoWrapper();
        WifiCenter.ProxySettings settings = WifiCenter.getProxySettings(config);
        proxyInfoWrapper.setType(settings);
        ProxyInfo proxyInfo = WifiCenter.getHttpProxy(config);

        List<StaticProxy> staticProxyList = StaticProxy.getList(ssid);
        if(settings == WifiCenter.ProxySettings.STATIC) {
            StaticProxy currentStaticProxy = new StaticProxy();
            currentStaticProxy.setSsid(ssid);
            currentStaticProxy.setHostname(proxyInfo.getHost());
            currentStaticProxy.setPort(proxyInfo.getPort());
            currentStaticProxy.setExclusionHosts(proxyInfo.getExclusionList());
            currentStaticProxy.setInUse(true);
            boolean contains = false;
            if(staticProxyList != null) {
                for (StaticProxy proxy : staticProxyList) {
                    if (currentStaticProxy.equals(proxy)) {
                        proxy.setInUse(true);
                        contains = true;
                        break;
                    }
                }
            } else {
                staticProxyList = new ArrayList<>();
            }
            if(!contains) {
                staticProxyList.add(0, currentStaticProxy);
            }
        }
        proxyInfoWrapper.setStaticProxyList(staticProxyList);

        if(settings == WifiCenter.ProxySettings.PAC) {
            proxyInfoWrapper.setPacUrl(proxyInfo.getPacFileUrl().toString());
        }

        this.proxyInfoWrapper = proxyInfoWrapper;
    }

    private void setIpInfoWrapper(WifiConfiguration config) {
        // IP配置
        IpInfoWrapper ipInfoWrapper = new IpInfoWrapper();
        WifiCenter.IpAssignment ipAssignment = WifiCenter.getIpAssignment(config);
        ipInfoWrapper.setType(ipAssignment);
        if(ipAssignment == WifiCenter.IpAssignment.STATIC) {
            ipInfoWrapper.setStaticIpConfiguration(WifiCenter.getStaticIpConfiguration(config));
        }

        this.ipInfoWrapper = ipInfoWrapper;
    }
}
