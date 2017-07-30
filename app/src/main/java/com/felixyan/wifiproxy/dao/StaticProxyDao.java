package com.felixyan.wifiproxy.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.felixyan.wifiproxy.model.StaticProxy;

import java.util.Map;

/**
 * Created by yanfei on 2017/07/30.
 */

public class StaticProxyDao extends BaseDao<StaticProxy> {
    private static final String TABLE_NAME = "static_proxy";
    //private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SSID = "ssid";
    private static final String COLUMN_HOSTNAME = "hostname";
    private static final String COLUMN_PORT = "port";
    private static final String COLUMN_BYPASS_FOR = "bypass_for";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String[] getColumns() {
        return new String[] {
                COLUMN_SSID,
                COLUMN_HOSTNAME,
                COLUMN_PORT,
                COLUMN_BYPASS_FOR,
        };
    }

    @Override
    protected String[] getUniqueColumns() {
        return new String[] { COLUMN_SSID, COLUMN_HOSTNAME, COLUMN_PORT };
    }

    @Override
    protected ContentValues getContentValues(@NonNull StaticProxy model) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SSID, model.getSsid());
        values.put(COLUMN_HOSTNAME, model.getHostname());
        values.put(COLUMN_PORT, model.getPort());
        values.put(COLUMN_BYPASS_FOR, model.getExclusionHosts());
        return values;
    }

    @Override
    protected StaticProxy getModel(@NonNull Cursor cursor) {
        StaticProxy model = null;
        Map<String, Integer> map = getColumnIndexMap(cursor);
        if(map != null) {
            model = new StaticProxy();
            model.setSsid(cursor.getString(map.get(COLUMN_SSID)));
            model.setHostname(cursor.getString(map.get(COLUMN_HOSTNAME)));
            model.setPort(getInt(cursor, map.get(COLUMN_PORT)));
            model.setExclusionHosts(cursor.getString(map.get(COLUMN_BYPASS_FOR)));
        }
        return model;
    }
}
