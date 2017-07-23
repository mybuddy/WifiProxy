package com.felixyan.wifiproxy.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by yanfei on 2017/07/02.
 */

public class PermissionUtil {

    /**
     * 权限检查
     * @param permissions
     *  权限名列表 Manifest.permission.xyz
     * @return
     *  已授权true，否则false
     */
    public static boolean checkPermission(Context context, String[] permissions){
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 权限授予结果检查
     * @param grantResults
     *  授权结果列表
     * @return
     *  已授权true，否则false
     */
    public static boolean checkPermissionGrantResult(int[] grantResults) {
        if(grantResults.length > 0) {
            for(int result : grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
