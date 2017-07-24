package com.felixyan.wifiproxy;

import android.net.LinkAddress;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by yanfei on 2017/07/24.
 */

public class StaticIpConfiguration {
    private LinkAddress ipAddress;
    private InetAddress gateway;
    private ArrayList<InetAddress> dnsServers = new ArrayList<>();
    private String domains;

    public StaticIpConfiguration() {
    }

    public LinkAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(LinkAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public InetAddress getGateway() {
        return gateway;
    }

    public void setGateway(InetAddress gateway) {
        this.gateway = gateway;
    }

    public ArrayList<InetAddress> getDnsServers() {
        return dnsServers;
    }

    public void setDnsServers(ArrayList<InetAddress> dnsServers) {
        this.dnsServers.clear();
        if(dnsServers != null) {
            this.dnsServers.addAll(dnsServers);
        }
    }

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }
}
