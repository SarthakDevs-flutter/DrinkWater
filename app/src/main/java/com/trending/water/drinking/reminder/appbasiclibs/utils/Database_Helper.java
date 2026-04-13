package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database_Helper {
    Activity act;
    Alert_Helper ah;
    Context mContext;
    String_Helper sh;
    Utility_Function uf;

    @SuppressLint({"WrongConstant"})
    public Database_Helper(Context mContext2, Activity act2) {
        this.mContext = mContext2;
        this.act = act2;
        this.uf = new Utility_Function(mContext2, act2);
        this.uf.permission_StrictMode();
        this.ah = new Alert_Helper(mContext2);
        this.sh = new String_Helper(mContext2, act2);
        Constant.SDB = mContext2.openOrCreateDatabase(Constant.DATABASE_NAME, 268435456, (SQLiteDatabase.CursorFactory) null);
    }

    public void CREATE_TABLE(String table_name, HashMap<String, String> fields) {
        String query = "CREATE TABLE IF NOT EXISTS " + table_name + "(";
        for (String key : fields.keySet()) {
            query = query + key + " " + fields.get(key) + ",";
        }
        String query2 = query.substring(0, query.length() - 1) + ");";
        System.out.println("CREAT QUERY : " + query2);
        FIRE(query2);
    }

    public void INSERT(String table_name, HashMap<String, String> fields) {
        ContentValues initialValues = new ContentValues();
        for (String key : fields.keySet()) {
            initialValues.put(key, fields.get(key));
        }
        Constant.SDB.insert(table_name, (String) null, initialValues);
    }

    public void INSERT(String table_name, ContentValues fields) {
        Constant.SDB.insert(table_name, (String) null, fields);
    }

    public void UPDATE(String table_name, HashMap<String, String> fields, String where_con) {
        ContentValues initialValues = new ContentValues();
        for (String key : fields.keySet()) {
            initialValues.put(key, fields.get(key));
        }
        Constant.SDB.update(table_name, initialValues, where_con, (String[]) null);
    }

    public void UPDATE(String table_name, ContentValues fields, String where_con) {
        Constant.SDB.update(table_name, fields, where_con, (String[]) null);
    }

    public void GET_LOGIN_USER_DETAILS() {
        try {
            do {
            } while (Constant.SDB.rawQuery("select * from tbl_user_login", (String[]) null).moveToNext());
        } catch (Exception e) {
        }
    }

    public ArrayList<HashMap<String, String>> getdataquery(String query) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        PrintStream printStream = System.out;
        printStream.println("SELECT QUERY : " + query);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        Cursor c = Constant.SDB.rawQuery("SELECT * FROM " + table_name, (String[]) null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = "SELECT * FROM " + table_name;
        if (!this.sh.check_blank_data(where_con)) {
            query = query + " where " + where_con;
        }
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        System.out.println("SELECT QUERY : " + query);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name, String order_field, int order_by) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = "SELECT * FROM " + table_name;
        if (!this.sh.check_blank_data(order_field)) {
            if (order_by == 0) {
                query = query + " ORDER BY " + order_field + " ASC";
            } else {
                query = query + " ORDER BY " + order_field + " DESC";
            }
        }
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        System.out.println("DESC QUERY:" + query);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con, String order_field, int order_by) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = "SELECT * FROM " + table_name;
        if (!this.sh.check_blank_data(where_con)) {
            query = query + " WHERE " + where_con;
        }
        if (!this.sh.check_blank_data(order_field)) {
            if (order_by == 0) {
                query = query + " ORDER BY " + order_field + " ASC";
            } else {
                query = query + " ORDER BY " + order_field + " DESC";
            }
        }
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String field_name, String table_name, String where_con) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = "SELECT " + field_name + " FROM " + table_name;
        if (!this.sh.check_blank_data(where_con)) {
            query = query + " where " + where_con;
        }
        System.out.print("JOIN QUERY:" + query);
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String field_name, String table_name, String where_con, String order_field, int order_by) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = "SELECT " + field_name + " FROM " + table_name;
        if (!this.sh.check_blank_data(where_con)) {
            query = query + " where " + where_con;
        }
        if (!this.sh.check_blank_data(order_field)) {
            if (order_by == 0) {
                query = query + " ORDER BY " + order_field + " ASC";
            } else {
                query = query + " ORDER BY " + order_field + " DESC";
            }
        }
        System.out.println("HISTORY JOIN QUERY:" + query);
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public void REMOVE(String table_name) {
        FIRE("DELETE FROM " + table_name);
    }

    public void REMOVE(String table_name, String where_con) {
        String query = "DELETE FROM " + table_name;
        if (!this.sh.check_blank_data(where_con)) {
            query = query + " WHERE " + where_con;
        }
        FIRE(query);
    }

    public int TOTAL_ROW(String table_name) {
        return Constant.SDB.rawQuery("SELECT * FROM " + table_name, (String[]) null).getCount();
    }

    public int TOTAL_ROW(String table_name, String where_con) {
        String query = "SELECT * FROM " + table_name;
        if (!this.sh.check_blank_data(where_con)) {
            query = query + " WHERE " + where_con;
        }
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        int count = c.getCount();
        if (c != null) {
            c.close();
        }
        return count;
    }

    public boolean IS_EXISTS(String table_name, String where_con) {
        String query = "SELECT * FROM " + table_name;
        if (!this.sh.check_blank_data(where_con)) {
            query = query + " WHERE " + where_con;
        }
        if (Constant.SDB.rawQuery(query, (String[]) null).getCount() > 0) {
            return true;
        }
        return false;
    }

    public String GET_LAST_ID(String table_name) {
        Cursor c = Constant.SDB.rawQuery("SELECT id FROM " + table_name, (String[]) null);
        if (!c.moveToLast()) {
            return "0";
        }
        return "" + c.getString(0);
    }

    public long NO_OF_AFFECTED_ROWS() {
        Cursor cursor = Constant.SDB.rawQuery("SELECT changes() AS affected_row_count", (String[]) null);
        if (cursor == null || cursor.getCount() <= 0 || !cursor.moveToFirst()) {
            return 0;
        }
        return cursor.getLong(cursor.getColumnIndex("affected_row_count"));
    }

    public void FIRE(String query) {
        Constant.SDB.execSQL(query);
    }

    public String MD5(String md5) {
        try {
            byte[] array = MessageDigest.getInstance("MD5").digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean exportDB() {
        FileChannel destination;
        File sd = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Databackup");
        if (!sd.exists()) {
            sd.mkdirs();
        }
        File currentDB = new File(Environment.getDataDirectory(), "/data/com.appname.appnamebasic/databases/3star.db");
        File backupDB = new File(sd, Constant.DATABASE_NAME);
        try {
            FileChannel source = new FileInputStream(currentDB).getChannel();
            try {
                destination = new FileOutputStream(backupDB).getChannel();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            try {
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                return true;
            } catch (Exception e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            return false;
        }
    }
}
