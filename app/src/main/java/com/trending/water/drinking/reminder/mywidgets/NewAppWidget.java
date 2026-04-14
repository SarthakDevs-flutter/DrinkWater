package com.trending.water.drinking.reminder.mywidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.Screen_Select_Bottle;
import com.trending.water.drinking.reminder.Screen_Splash;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DatabaseHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DateHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.PreferenceHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class NewAppWidget extends AppWidgetProvider {
    private static final String TAG = "NewAppWidget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        PreferenceHelper preferencesHelper = new PreferenceHelper(context);
        float dailyGoal = preferencesHelper.getFloat(URLFactory.DAILY_WATER);
        if (dailyGoal == 0.0f) dailyGoal = 2500.0f;

        float drankAmount = getTodayDrunkAmount(context);
        String unit = preferencesHelper.getString(URLFactory.WATER_UNIT);
        if (unit == null || unit.isEmpty()) unit = "ML";

        views.setTextViewText(R.id.appwidget_text, (int) drankAmount + "/" + (int) dailyGoal + " " + unit);
        views.setInt(R.id.circularProgressbar, "setMax", (int) dailyGoal);
        views.setInt(R.id.circularProgressbar, "setProgress", (int) drankAmount);

        // Intent logic
        int pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntentFlags |= PendingIntent.FLAG_IMMUTABLE;
        }

        Intent splashIntent = new Intent(context, Screen_Splash.class);
        splashIntent.putExtra("from_widget", true);
        splashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent splashPendingIntent = PendingIntent.getActivity(context, 1, splashIntent, pendingIntentFlags);
        views.setOnClickPendingIntent(R.id.widget, splashPendingIntent);

        Intent bottleIntent = new Intent(context, Screen_Select_Bottle.class);
        bottleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent bottlePendingIntent = PendingIntent.getActivity(context, 2, bottleIntent, pendingIntentFlags);
        views.setOnClickPendingIntent(R.id.add_water, bottlePendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static float getTodayDrunkAmount(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context, null);
        DateHelper dateHelper = new DateHelper();
        PreferenceHelper preferencesHelper = new PreferenceHelper(context);

        String today = dateHelper.getCurrentDate("dd-MM-yyyy");
        ArrayList<HashMap<String, String>> drankData = databaseHelper.getData("tbl_drink_details", "DrinkDate ='" + today + "'");

        float totalAmount = 0.0f;
        String unit = preferencesHelper.getString(URLFactory.WATER_UNIT);
        if (unit == null || unit.isEmpty()) unit = "ML";

        for (HashMap<String, String> entry : drankData) {
            try {
                String valueStr = unit.equalsIgnoreCase("ml") ? entry.get("ContainerValue") : entry.get("ContainerValueOZ");
                if (valueStr != null) {
                    totalAmount += Float.parseFloat(valueStr);
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing drink amount", e);
            }
        }
        return totalAmount;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
