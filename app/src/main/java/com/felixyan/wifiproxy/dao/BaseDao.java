package com.felixyan.wifiproxy.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.felixyan.wifiproxy.App;
import com.felixyan.wifiproxy.database.MySQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanfei on 2016/06/19.
 */
public abstract class BaseDao<T> {
    private static final String TAG = BaseDao.class.getSimpleName();
    private static SQLiteDatabase sDatabase;
    private Map<String, Integer> sColumnIndexMap = null;

    protected abstract String getTableName();
    protected abstract String[] getColumns();
    protected abstract String[] getUniqueColumns();
    protected abstract ContentValues getContentValues(@NonNull T model);
    protected abstract T getModel(@NonNull Cursor cursor);

    protected static SQLiteDatabase getDatabase(){
        if(sDatabase == null) {
            sDatabase = new MySQLiteHelper(App.getInstance()).getWritableDatabase();
        }
        return sDatabase;
    }

    protected boolean isTableWritable() {
        return true;
    }

    protected Map<String, Integer> getColumnIndexMap(@NonNull Cursor cursor) {
        if(sColumnIndexMap == null){
            String[] columns = getColumns();
            if(columns != null){
                sColumnIndexMap = new HashMap<>();
                for(String c : columns){
                    sColumnIndexMap.put(c, cursor.getColumnIndex(c));
                }
            }
        }
        return sColumnIndexMap;
    }

    protected int getInt(Cursor cursor, int columnIndex){
        return !cursor.isNull(columnIndex) ? cursor.getInt(columnIndex) : 0;
    }

    protected long getLong(Cursor cursor, int columnIndex){
        return !cursor.isNull(columnIndex) ? cursor.getLong(columnIndex) : 0;
    }

    protected float getFloat(Cursor cursor, int columnIndex){
        return getFloat(cursor, columnIndex, 2);
    }

    protected float getFloat(Cursor cursor, int columnIndex, int scale){
        float result = !cursor.isNull(columnIndex) ? cursor.getFloat(columnIndex) : 0;
        if(result != 0) {
            BigDecimal bd = new BigDecimal(result);
            result = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
        }

        return result;
    }

    protected double getDouble(Cursor cursor, int columnIndex){
        return getDouble(cursor, columnIndex, 2);
    }

    protected double getDouble(Cursor cursor, int columnIndex, int scale){
        double result = !cursor.isNull(columnIndex) ? cursor.getDouble(columnIndex) : 0;
        if(result != 0) {
            BigDecimal bd = new BigDecimal(result);
            result = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return result;
    }

    public boolean exists(String where, String[] whereArgs){
        boolean exists = false;
        Cursor cursor = get(getUniqueColumns(), where, whereArgs, null);
        if(cursor != null){
            exists = cursor.moveToFirst();
            cursor.close();
        }
        return exists;
    }

    public Cursor get(String[] projection, String where, String[] whereArgs, String sortOrder){
        SQLiteDatabase db = getDatabase();
        String tableName = getTableName();
        try {
            return db != null && tableName != null
                    ? db.query(tableName, projection, where, whereArgs, null, null, sortOrder)
                    : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public T get(String where, String[] whereArgs){
        T model = null;
        Cursor cursor = get(null, where, whereArgs, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                model = getModel(cursor);
            }
            cursor.close();
        }
        return model;
    }

    public List<T> getList(String where, String[] whereArgs){
        List<T> modelList = null;
        Cursor cursor = get(null, where, whereArgs, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                modelList = new ArrayList<>();
                do {
                    modelList.add(getModel(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return modelList;
    }

    public boolean insert(T model){
        return insert(getContentValues(model));
    }

    public boolean insertBatch(List<T> modelList){
        if(modelList == null || modelList.size() == 0) return false;

        List<ContentValues> valueList = new ArrayList<>();
        for(T model : modelList){
            valueList.add(getContentValues(model));
        }
        return insertBatch(valueList.toArray(new ContentValues[valueList.size()]));
    }

    protected boolean insert(ContentValues values){
        if(!isTableWritable()) {
            return false;
        }
        SQLiteDatabase db = getDatabase();
        String tableName = getTableName();
        return values != null && db != null && tableName != null && db.insert(tableName, null, values) > 0;
    }

    protected boolean insertBatch(ContentValues[] values){
        if(!isTableWritable()) {
            return false;
        }
        SQLiteDatabase db = getDatabase();
        String tableName = getTableName();

        if(values != null && db != null && tableName != null) {
            boolean insertSuccess = true;
            try {
                db.beginTransaction();
                for (ContentValues value : values) {
                    if(db.insert(tableName, null, value) == -1) {
                        throw new SQLException("Insert row id returns -1");
                    }
                }
                db.setTransactionSuccessful();
            } catch (SQLException ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                insertSuccess = false;
            } finally {
                db.endTransaction();
            }
            return insertSuccess;
        }
        return false;
    }

    protected boolean update(ContentValues values, String where, String[] whereArgs){
        if(!isTableWritable()) {
            return false;
        }
        SQLiteDatabase db = getDatabase();
        String tableName = getTableName();
        try {
            return db != null && tableName != null && db.update(tableName, values, where, whereArgs) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean save(T model, String where, String[] whereArgs){
        return save(getContentValues(model), where, whereArgs);
    }

    protected boolean save(ContentValues values, String where, String[] whereArgs){
        if(!isTableWritable()) {
            return false;
        }
        SQLiteDatabase db = getDatabase();
        String tableName = getTableName();
        try {
            if(values != null && db != null && tableName != null){
                if(exists(where, whereArgs)){
                    // 更新
                    return db.update(tableName, values, where, whereArgs) > 0;
                }
                // 插入
                return db.insert(tableName, null, values) > 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public boolean delete(String where, String[] whereArgs){
        if(!isTableWritable()) {
            return false;
        }
        SQLiteDatabase db = getDatabase();
        String tableName = getTableName();
        try{
            return db != null && tableName != null && db.delete(tableName, where, whereArgs) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
