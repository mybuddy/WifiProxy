package com.felixyan.wifiproxy.activity;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanfei on 2017/07/02.
 */

public class AbstractActivity extends AppCompatActivity {
    /**
     * 权限检查，若未授权则请求权限
     *
     * @param requestCode
     *  当前请求权限的权限码
     * @param permissions
     *  权限名列表 Manifest.permission.xyz
     * @return
     *  已授权true，否则false
     */
    public boolean checkPermission(int requestCode, String... permissions){
        List<String> permissionsToGrant = new ArrayList<>();
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionsToGrant.add(permission);
            }
        }
        if(permissionsToGrant.size() != 0){
            ActivityCompat.requestPermissions(this, permissionsToGrant.toArray(new String[permissionsToGrant.size()]), requestCode);
            return false;
        }
        return true;
    }
}
