package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class Constant {
    public static final String DATABASE_NAME = "3star.db";
    public static final boolean DEVELOPER_MODE = true;
    public static final int PICK_CONTACT = 1000;

    public static final String GENERAL_SHARE_TITLE = "Share";
    public static final String NO_INTERNET_MESSAGE = "No Internet Connection!!!";

    public static final String[] VIDEO_ID_REGEX = {
            "\\?vi?=([^&]*)",
            "watch\\?.*v=([^&]*)",
            "(?:embed|vi?)/([^/?]*)",
            "^([A-Za-z0-9\\-]*)"
    };

    public static final String YOUTUBE_URL_REGEX = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";

    // Static state fields (considered legacy, preserve for now)
    public static SQLiteDatabase database = null;
    public static List<ResolveInfo> launchablesList = null;
    public static List<ResolveInfo> selectedLaunchablesList = null;
    public static PackageManager packageManager = null;
    public static String sharePurchaseTitle = "Share To";
}
