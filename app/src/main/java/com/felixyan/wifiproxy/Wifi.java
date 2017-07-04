package com.felixyan.wifiproxy;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by yanfei on 2017/07/02.
 */

public class Wifi {
    private static final String TAG = Wifi.class.getSimpleName();

    // Constants used for different security types
    public static final String WPA2 = "WPA2";
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";
    public static final String OPEN = "Open";
    /* For EAP Enterprise fields */
    public static final String WPA_EAP = "WPA-EAP";
    public static final String IEEE8021X = "IEEE8021X";

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

    public boolean startScan() {
        return mWifiManager.startScan();
    }

    public List<ScanResult> getScanResults() {
        List<ScanResult> list = null;
        //if(mWifiManager.startScan()) {
            list = mWifiManager.getScanResults();
        //}

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

    /**
     * @return The security of a given {@link ScanResult}.
     */
    public static String getScanResultSecurity(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] securityModes = { WEP, WPA, WPA2, WPA_EAP, IEEE8021X };
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                return securityModes[i];
            }
        }

        return OPEN;
    }

    /**
     * @return The security of a given {@link WifiConfiguration}.
     */
    public static String getWifiConfigurationSecurity(WifiConfiguration wifiConfig) {
        if (wifiConfig.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE)) {
            // If we never set group ciphers, wpa_supplicant puts all of them.
            // For open, we don't set group ciphers.
            // For WEP, we specifically only set WEP40 and WEP104, so CCMP
            // and TKIP should not be there.
            if (!wifiConfig.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.CCMP)
                    && (wifiConfig.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP40)
                    || wifiConfig.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP104))) {
                return WEP;
            } else {
                return OPEN;
            }
        } else if (wifiConfig.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP)) {
            return WPA_EAP;
        } else if (wifiConfig.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return IEEE8021X;
        } else if (wifiConfig.allowedProtocols.get(WifiConfiguration.Protocol.RSN)) {
            return WPA2;
        } else if (wifiConfig.allowedProtocols.get(WifiConfiguration.Protocol.WPA)) {
            return WPA;
        } else {
            Log.w(TAG, "Unknown security type from WifiConfiguration, falling back on open.");
            return OPEN;
        }
    }

    public enum WifiCipherType {
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID
    }
}