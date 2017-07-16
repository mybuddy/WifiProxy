package com.felixyan.wifiproxy;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by yanfei on 2017/07/02.
 */

public class WifiCenter {
    private static final String TAG = WifiCenter.class.getSimpleName();

    // Constants used for different security types
    public static final String WPA2 = "WPA2";
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";
    public static final String OPEN = "Open";
    /* For EAP Enterprise fields */
    public static final String WPA_EAP = "WPA-EAP";
    public static final String IEEE8021X = "IEEE8021X";

    private static WifiCenter sWifi;
    private WifiManager mWifiManager;
    private WifiCenter() {}

    public static WifiCenter getInstance(Context context) {
        if(sWifi == null) {
            synchronized (WifiCenter.class) {
                if(sWifi == null) {
                    sWifi = new WifiCenter();
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
            //if(config.SSID.equals("\"" + ssid + "\"")) {
            if(isSameSsid(config.SSID, ssid)) {
                return config;
            }
        }
        return null;
    }

    public boolean disableNetwork(int networkId) {
        return mWifiManager.disableNetwork(networkId);
    }

    public boolean removeNetwork(int networkId) {
        return networkId != -1 && mWifiManager.removeNetwork(networkId);
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

    public boolean connect(String ssid, String password, WifiCipherType type) {
        WifiConfiguration config = createWifiConfig(ssid, password, type);
        if(config != null) {
            return connect(config);
        }
        return false;
    }

    public boolean connect(int networkId) {
        return networkId != -1 && mWifiManager.enableNetwork(networkId, true);
    }

    public boolean connect(WifiConfiguration config) {
        int networkId = config.networkId != -1 ? config.networkId : mWifiManager.addNetwork(config);
        return connect(networkId);
    }

    public boolean connectSavedWifi(String ssid) {
        WifiConfiguration config = getSsidConfig(ssid);
        if(config != null) {
            return connect(config);
        }
        return false;
    }

    public WifiInfo getConnectionInfo() {
        return mWifiManager.getConnectionInfo();
    }

    public static boolean isSameSsid(String ssidA, String ssidB) {
        if(ssidA == null || ssidB == null) {
            return false;
        }

        if((ssidA.startsWith("\"") && ssidB.startsWith("\"")) || (!ssidA.startsWith("\"") && !ssidB.startsWith("\""))) {
            return ssidA.equals(ssidB);
        }

        if(ssidA.startsWith("\"") && !ssidB.startsWith("\"")) {
            return ssidA.equals("\"" + ssidB + "\"");
        } else {
            return ssidB.equals("\"" + ssidA + "\"");
        }
    }

    public static boolean is24GHz(int frequency) {
        return frequency > 2400 && frequency < 2500;
    }

    public static boolean is5GHz(int frequency) {
        return frequency > 4900 && frequency < 5900;
    }

    public static String getSecurity(String capabilities) {
        final String[] securityModes = { WEP, WPA, WPA2, WPA_EAP, IEEE8021X };
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (capabilities.contains(securityModes[i])) {
                return securityModes[i];
            }
        }

        return OPEN;
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


    /*
     * 以下与代理相关的方法，参考
     * https://stackoverflow.com/questions/12486441/how-can-i-set-proxysettings-and-proxyproperties-on-android-wi-fi-connection-usin#33949339
     * 及 https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/net/IpConfiguration.java
     */

    /**
     * 获取代理
     *
     * @param config
     * @return
     */
    public static ProxyInfo getHttpProxy(WifiConfiguration config) {
        ProxyInfo proxyInfo = null;

        try {
            Method getHttpProxyMethod = WifiConfiguration.class.getMethod("getHttpProxy");
            getHttpProxyMethod.setAccessible(true);

            proxyInfo = (ProxyInfo) getHttpProxyMethod.invoke(config);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return proxyInfo;
    }

    /**
     * 获取代理类型
     *
     * @param config
     * @return
     */
    public static ProxySettings getProxySettings(WifiConfiguration config) {
        ProxySettings proxySettings = ProxySettings.UNASSIGNED;

        try {
            Method getProxySettingsMethod = WifiConfiguration.class.getMethod("getProxySettings");
            getProxySettingsMethod.setAccessible(true);

            Object settings = getProxySettingsMethod.invoke(config);
            if(settings != null) {
                proxySettings = ProxySettings.valueOf(settings.toString());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return proxySettings;
    }

    /**
     * 设置代理
     *
     * @param config
     * @param settings
     * @param proxy
     */
    public static void setProxy(WifiConfiguration config, ProxySettings settings, ProxyInfo proxy) {
        try {
            // 参数类型
            Class proxySettingsClazz = Class.forName("android.net.IpConfiguration$ProxySettings");

            Class[] setProxyParamsClazz = new Class[2];
            setProxyParamsClazz[0] = proxySettingsClazz;
            setProxyParamsClazz[1] = ProxyInfo.class;

            // 查找方法
            Method setProxyMethod = WifiConfiguration.class.getDeclaredMethod("setProxy", setProxyParamsClazz);
            setProxyMethod.setAccessible(true);

            // 参数
            Object[] methodParams = new Object[2];
            methodParams[0] = Enum.valueOf(proxySettingsClazz, settings.name());
            methodParams[1] = proxy;

            // 调用方法
            setProxyMethod.invoke(config, methodParams);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public enum WifiCipherType {
        /*[WPA-PSK-CCMP][WPA2-PSK-CCMP][ESS]*/
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID;

        public static WifiCipherType convert(String name) {
            if(name.contains("WEP")) {
                return WIFICIPHER_WEP;
            }
            if(name.contains("WPA")) {
                return WIFICIPHER_WPA;
            }
            return WIFICIPHER_NOPASS;
        }
    }

    /**
     * 代理类型
     */
    public enum ProxySettings {
        /* No proxy is to be used. Any existing proxy settings
         * should be cleared. */
        NONE,
        /* Use statically configured proxy. Configuration can be accessed
         * with httpProxy. */
        STATIC,
        /* no proxy details are assigned, this is used to indicate
         * that any existing proxy settings should be retained */
        UNASSIGNED,
        /* Use a Pac based proxy.
         */
        PAC
    }
}
