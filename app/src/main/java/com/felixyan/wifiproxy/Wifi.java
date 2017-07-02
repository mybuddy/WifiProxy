package com.felixyan.wifiproxy;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by yanfei on 2017/07/02.
 */

public class Wifi {
    private static Wifi sWifi;
    private WifiManager mWifiManager;
    private Wifi() {}

    public static Wifi getInstance(Context context) {
        if(sWifi == null) {
            synchronized (Wifi.class) {
                if(sWifi == null) {
                    sWifi = new Wifi();
                    sWifi.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                }
            }
        }
        return sWifi;
    }

    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    public boolean setWifiEnabled(boolean wifiEnabled) {
        return mWifiManager.setWifiEnabled(wifiEnabled);
    }

    /**
     *
     * @return WIFI_STATE_DISABLED,
     *         WIFI_STATE_DISABLING,
     *         WIFI_STATE_ENABLED,
     *         WIFI_STATE_ENABLING,
     *         WIFI_STATE_UNKNOWN
     */
    public int getWifiState() {
        return mWifiManager.getWifiState();
    }

    public List<ScanResult> getScanResults() {
        List<ScanResult> list = null;
        if(mWifiManager.startScan()) {
            list = mWifiManager.getScanResults();
        }

        return list;
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        return mWifiManager.getConfiguredNetworks();
    }

    public WifiConfiguration getSsidConfig(String ssid) {
        List<WifiConfiguration> list = getConfiguredNetworks();
        for(WifiConfiguration config : list) {
            if(config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }
        return null;
    }

    public boolean disableNetwork(int netId) {
        return mWifiManager.disableNetwork(netId);
    }

    public boolean removeNetwork(int netId) {
        return mWifiManager.removeNetwork(netId);
    }

    public WifiConfiguration createWifiConfig(String ssid, String password, WifiCipherType type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        switch (type) {
            case WIFICIPHER_NOPASS:
                config.wepKeys[0] = "";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case WIFICIPHER_WEP:
                config.preSharedKey = "\"" + password + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case WIFICIPHER_WPA:
                config.preSharedKey = "\"" + password + "\"";
                config.hiddenSSID = true;
                break;
            default:
                return null;
        }

        return config;
    }

    public boolean addNewWifi(WifiConfiguration config) {
        int netId = mWifiManager.addNetwork(config);
        return mWifiManager.enableNetwork(netId, false);
    }

    public WifiInfo getConnectionInfo() {
        return mWifiManager.getConnectionInfo();
    }

    public enum WifiCipherType {
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID
    }
}
