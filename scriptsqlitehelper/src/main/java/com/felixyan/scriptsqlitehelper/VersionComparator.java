package com.felixyan.scriptsqlitehelper;

import android.util.Log;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL脚本文件按版本排序
 *
 * Created by yanfei on 2017/07/30.
 */
class VersionComparator implements Comparator<String> {
    private static final String TAG = ScriptSQLiteHelper.class.getSimpleName();
    private Pattern pattern = Pattern.compile(".*_upgrade_([0-9]+)-([0-9]+).*");

    public int compare(String file0, String file1) {
        Matcher m0 = this.pattern.matcher(file0);
        Matcher m1 = this.pattern.matcher(file1);
        if (!m0.matches()) {
            Log.w(TAG, "could not parse upgrade script file: " + file0);
            throw new ScriptSQLiteException("Invalid upgrade script file");
        } else if (!m1.matches()) {
            Log.w(TAG, "could not parse upgrade script file: " + file1);
            throw new ScriptSQLiteException("Invalid upgrade script file");
        } else {
            int v0_from = Integer.valueOf(m0.group(1));
            int v1_from = Integer.valueOf(m1.group(1));
            int v0_to = Integer.valueOf(m0.group(2));
            int v1_to = Integer.valueOf(m1.group(2));
            return v0_from == v1_from ? (v0_to == v1_to ? 0 : (v0_to < v1_to ? -1 : 1)) : (v0_from < v1_from ? -1 : 1);
        }
    }
}
