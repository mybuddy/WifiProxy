package com.felixyan.wifiproxy.database;

import android.content.Context;

import com.felixyan.scriptsqlitehelper.ScriptSQLiteHelper;

/**
 * Created by yanfei on 2017/07/30.
 */

public class MySQLiteHelper extends ScriptSQLiteHelper {
    private static final String DB_NAME = "wifi_config.db";
    private static final int DB_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }
}
