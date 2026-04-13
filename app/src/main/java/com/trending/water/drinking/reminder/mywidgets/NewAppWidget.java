package com.trending.water.drinking.reminder.mywidgets;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.Screen_Select_Bottle;
import com.trending.water.drinking.reminder.Screen_Splash;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class NewAppWidget extends AppWidgetProvider {
    static float drink_water = 0.0f;
    static Context mContext;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setTextViewText(R.id.appwidget_text, get_today_report());

        views.setInt(R.id.circularProgressbar, "setMax", (int) URLFactory.DAILY_WATER_VALUE);
        views.setInt(R.id.circularProgressbar, "setProgress", (int) drink_water);

        // Intent 1 → Open Splash
        Intent launchMain = new Intent(context, Screen_Splash.class);
        launchMain.putExtra("from_widget", true);
        launchMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent1 = PendingIntent.getActivity(
                context,
                1, // unique request code
                launchMain,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        views.setOnClickPendingIntent(R.id.widget, pendingIntent1);

        // Intent 2 → Open Bottle Screen
        Intent launchMain2 = new Intent(context, Screen_Select_Bottle.class);
        launchMain2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent2 = PendingIntent.getActivity(
                context,
                2, // different request code
                launchMain2,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        views.setOnClickPendingIntent(R.id.add_water, pendingIntent2);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @SuppressLint({"WrongConstant"})
    public static String get_today_report() {
        Constant.SDB = mContext.openOrCreateDatabase(Constant.DATABASE_NAME, 268435456, (SQLiteDatabase.CursorFactory) null);
        Date_Helper dth = new Date_Helper();
        Preferences_Helper ph = new Preferences_Helper(mContext);
        ArrayList<HashMap<String, String>> arr_data = getdata("tbl_drink_details", "DrinkDate ='" + dth.getCurrentDate("dd-MM-yyyy") + "'");
        if (ph.getFloat(URLFactory.DAILY_WATER) == 0.0f) {
            URLFactory.DAILY_WATER_VALUE = 2500.0f;
        } else {
            URLFactory.DAILY_WATER_VALUE = ph.getFloat(URLFactory.DAILY_WATER);
        }
        if (check_blank_data("" + ph.getString(URLFactory.WATER_UNIT))) {
            URLFactory.WATER_UNIT_VALUE = "ML";
        } else {
            URLFactory.WATER_UNIT_VALUE = ph.getString(URLFactory.WATER_UNIT);
        }
        drink_water = 0.0f;
        for (int k = 0; k < arr_data.size(); k++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                drink_water = (float) (((double) drink_water) + Double.parseDouble((String) arr_data.get(k).get("ContainerValue")));
            } else {
                drink_water = (float) (((double) drink_water) + Double.parseDouble((String) arr_data.get(k).get("ContainerValueOZ")));
            }
        }
        return "" + ((int) drink_water) + "/" + ((int) URLFactory.DAILY_WATER_VALUE) + " " + URLFactory.WATER_UNIT_VALUE;
    }

    public static boolean check_blank_data(String data) {
        if (data.equals("") || data.isEmpty() || data.length() == 0 || data.equals("null") || data == null) {
            return true;
        }
        return false;
    }

    public static ArrayList<HashMap<String, String>> getdata(String table_name, String where_con) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = ("SELECT * FROM " + table_name) + " where " + where_con;
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

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void onEnabled(Context context) {
    }

    public void onDisabled(Context context) {
    }
}
