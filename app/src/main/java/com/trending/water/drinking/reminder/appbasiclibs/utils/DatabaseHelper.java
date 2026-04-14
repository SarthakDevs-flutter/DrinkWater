package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    
    private final Context context;
    private final Activity activity;
    private final StringHelper stringHelper;

    public DatabaseHelper(@NonNull Context context) {
        this(context, null);
    }

    @SuppressLint("WrongConstant")
    public DatabaseHelper(@NonNull Context context, @Nullable Activity activity) {
        this.context = context;
        this.activity = activity;
        this.stringHelper = new StringHelper(context);

        // Ensure database directory exists
        Constant.database = context.openOrCreateDatabase(Constant.DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
    }

    public void createTable(@NonNull String tableName, @NonNull HashMap<String, String> fields) {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");
        boolean first = true;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (!first) query.append(",");
            query.append(entry.getKey()).append(" ").append(entry.getValue());
            first = false;
        }
        query.append(");");
        Log.d(TAG, "SQL Create Query: " + query);
        fire(query.toString());
    }

    public void insert(@NonNull String tableName, @NonNull HashMap<String, String> fields) {
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }
        insert(tableName, values);
    }

    public void insert(@NonNull String tableName, @NonNull ContentValues values) {
        if (Constant.database != null) {
            Constant.database.insert(tableName, null, values);
        }
    }

    public void update(@NonNull String tableName, @NonNull HashMap<String, String> fields, @Nullable String whereClause) {
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }
        update(tableName, values, whereClause);
    }

    public void update(@NonNull String tableName, @NonNull ContentValues values, @Nullable String whereClause) {
        if (Constant.database != null) {
            Constant.database.update(tableName, values, whereClause, null);
        }
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getDataQuery(@NonNull String query) {
        ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
        Log.d(TAG, "SQL Select Query: " + query);
        
        if (Constant.database == null) return mapList;

        try (Cursor cursor = Constant.database.rawQuery(query, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    HashMap<String, String> row = new HashMap<>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        row.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    mapList.add(row);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error executing SQL query", e);
        }
        return mapList;
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getData(@NonNull String tableName) {
        return getData(tableName, null);
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getData(@NonNull String tableName, @Nullable String whereClause) {
        String query = "SELECT * FROM " + tableName;
        if (!stringHelper.check_blank_data(whereClause)) {
            query += " WHERE " + whereClause;
        }
        return getDataQuery(query);
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getData(@NonNull String tableName, @Nullable String orderByField, int orderType) {
        String query = "SELECT * FROM " + tableName;
        if (!stringHelper.check_blank_data(orderByField)) {
            query += " ORDER BY " + orderByField + (orderType == 0 ? " ASC" : " DESC");
        }
        return getDataQuery(query);
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getData(@NonNull String tableName, @Nullable String whereClause, @Nullable String orderByField, int orderType) {
        String query = "SELECT * FROM " + tableName;
        if (!stringHelper.check_blank_data(whereClause)) {
            query += " WHERE " + whereClause;
        }
        if (!stringHelper.check_blank_data(orderByField)) {
            query += " ORDER BY " + orderByField + (orderType == 0 ? " ASC" : " DESC");
        }
        return getDataQuery(query);
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getData(@NonNull String fields, @NonNull String tableName, @Nullable String whereClause) {
        String query = "SELECT " + fields + " FROM " + tableName;
        if (!stringHelper.check_blank_data(whereClause)) {
            query += " WHERE " + whereClause;
        }
        return getDataQuery(query);
    }

    @NonNull
    public ArrayList<HashMap<String, String>> getData(@NonNull String fields, @NonNull String tableName, @Nullable String whereClause, @Nullable String orderByField, int orderType) {
        String query = "SELECT " + fields + " FROM " + tableName;
        if (!stringHelper.check_blank_data(whereClause)) {
            query += " WHERE " + whereClause;
        }
        if (!stringHelper.check_blank_data(orderByField)) {
            query += " ORDER BY " + orderByField + (orderType == 0 ? " ASC" : " DESC");
        }
        return getDataQuery(query);
    }

    public void remove(@NonNull String tableName) {
        fire("DELETE FROM " + tableName);
    }

    public void remove(@NonNull String tableName, @Nullable String whereClause) {
        String query = "DELETE FROM " + tableName;
        if (!stringHelper.check_blank_data(whereClause)) {
            query += " WHERE " + whereClause;
        }
        fire(query);
    }

    public int totalRows(@NonNull String tableName) {
        return totalRows(tableName, null);
    }

    public int totalRows(@NonNull String tableName, @Nullable String whereClause) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        if (!stringHelper.check_blank_data(whereClause)) {
            query += " WHERE " + whereClause;
        }
        
        if (Constant.database == null) return 0;

        try (Cursor cursor = Constant.database.rawQuery(query, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error counting rows in table: " + tableName, e);
        }
        return 0;
    }

    public boolean isExists(@NonNull String tableName, @Nullable String whereClause) {
        return totalRows(tableName, whereClause) > 0;
    }

    @NonNull
    public String getLastId(@NonNull String tableName) {
        if (Constant.database == null) return "0";
        try (Cursor cursor = Constant.database.rawQuery("SELECT id FROM " + tableName + " ORDER BY id DESC LIMIT 1", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting last ID from table: " + tableName, e);
        }
        return "0";
    }

    public long getAffectedRowsCount() {
        if (Constant.database == null) return 0;
        try (Cursor cursor = Constant.database.rawQuery("SELECT changes() AS affected_row_count", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("affected_row_count");
                if (index != -1) {
                    return cursor.getLong(index);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting affected rows count", e);
        }
        return 0;
    }

    public void fire(@NonNull String query) {
        if (Constant.database != null) {
            try {
                Constant.database.execSQL(query);
            } catch (Exception e) {
                Log.e(TAG, "Error executing SQL: " + query, e);
            }
        }
    }

    @Nullable
    public static String md5(@NonNull String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] array = digest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "MD5 algorithm not found", e);
            return null;
        }
    }

    public boolean exportDb() {
        File backupDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Databackup");
        if (!backupDir.exists() && !backupDir.mkdirs()) return false;
        
        File currentDb = context.getDatabasePath(Constant.DATABASE_NAME);
        File backupDb = new File(backupDir, Constant.DATABASE_NAME);
        
        try (FileChannel source = new FileInputStream(currentDb).getChannel();
             FileChannel destination = new FileOutputStream(backupDb).getChannel()) {
            destination.transferFrom(source, 0, source.size());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error exporting database", e);
            return false;
        }
    }
}
