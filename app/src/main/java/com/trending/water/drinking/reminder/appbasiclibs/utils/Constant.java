package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;

import com.trending.water.drinking.reminder.appbasiclibs.BaseActivity;

import java.util.List;

public class Constant extends BaseActivity {
    public static final String DATABASE_NAME = "3star.db";
    public static final boolean DEVELOPER_MODE = true;
    public static final int PICK_CONTACT = 1000;
    public static final String general_share_title = "Share";
    public static final String no_internet_message = "No Internet Connection!!!";
    public static final String[] videoIdRegex = {"\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};
    public static final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    public static SQLiteDatabase SDB = null;
    public static List<ResolveInfo> launchables = null;
    public static List<ResolveInfo> launchables_sel = null;
    public static PackageManager pm = null;
    public static String share_purchase_title = "Share To";
}
