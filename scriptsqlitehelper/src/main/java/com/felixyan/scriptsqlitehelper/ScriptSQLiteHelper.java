package com.felixyan.scriptsqlitehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yanfei on 2017/07/30.
 */

public class ScriptSQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = ScriptSQLiteHelper.class.getSimpleName();
    private final Context mContext;
    private final String mName;
    private String mCreatePath;
    private String mUpgradePathFormat;

    public ScriptSQLiteHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        if(version < 1) {
            throw new IllegalArgumentException("Version must be >= 1, was " + version);
        } else if(name == null) {
            throw new IllegalArgumentException("Database name cannot be null");
        } else {
            mContext = context;
            mName = name;
            mCreatePath = "databases/" + name + "_create.sql";
            mUpgradePathFormat = "databases/" + name + "_upgrade_%s-%s.sql";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeSqlFile(db, mCreatePath);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database " + mName + " from version " + oldVersion + " to " + newVersion + "...");
        ArrayList<String> paths = new ArrayList<>();
        getUpgradeFilePaths(oldVersion, newVersion - 1, newVersion, paths);

        if(paths.isEmpty()) {
            Log.e(TAG, "no upgrade script path from " + oldVersion + " to " + newVersion);
            throw new ScriptSQLiteException("no upgrade script path from " + oldVersion + " to " + newVersion);
        } else {
            Collections.sort(paths, new VersionComparator());

            for(String path : paths) {
                Log.w(TAG, "processing upgrade: " + path);
                executeSqlFile(db, path);
            }

            Log.w(TAG, "Successfully upgraded database " + mName + " from version " + oldVersion + " to " + newVersion);
        }
    }

    /**
     * 判断数据库升级的SQL脚本文件是否存在
     *
     * @param oldVersion 老版本
     * @param newVersion 新版本
     * @return
     */
    private boolean isUpgradeFileExists(int oldVersion, int newVersion) {
        String path = String.format(mUpgradePathFormat, oldVersion, newVersion);

        try {
            if(mContext.getAssets().open(path) != null) {
                return true;
            }
        } catch (IOException ex) {
            Log.w(TAG, "missing database upgrade script: " + path);
        }
        return false;
    }

    /**
     * 递归获取数据库升级的SQL脚本路径
     *
     * @param baseVersion 当前版本
     * @param start
     * @param end
     * @param paths
     */
    private void getUpgradeFilePaths(int baseVersion, int start, int end, ArrayList<String> paths) {
        int a;
        int b;
        if(isUpgradeFileExists(start, end)) {
            String path = String.format(mUpgradePathFormat, start, end);
            paths.add(path);
            a = start - 1;
            b = start;
        } else {
            a = start - 1;
            b = end;
        }

        if(a >= baseVersion) {
            getUpgradeFilePaths(baseVersion, a, b, paths);
        }
    }

    /**
     * 执行SQL脚本文件
     * @param db
     * @param path
     */
    private void executeSqlFile(SQLiteDatabase db, String path) {
        InputStream is = null;
        try {
            is = mContext.getAssets().open(path);
            String sql = Utils.convertStreamToString(is);
            if(sql != null) {
                // SQL逐语句执行
                List<String> cmds = Utils.splitSqlScript(sql, ';');
                for(String cmd : cmds) {
                    if(!cmd.trim().isEmpty()) {
                        db.execSQL(cmd);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
