package com.felixyan.scriptsqlitehelper;

import android.database.sqlite.SQLiteException;

/**
 * Created by yanfei on 2017/07/30.
 */

public class ScriptSQLiteException extends SQLiteException {
    public ScriptSQLiteException() {
    }

    public ScriptSQLiteException(String error) {
        super(error);
    }
}
