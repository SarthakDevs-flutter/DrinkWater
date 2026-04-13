package com.trending.water.drinking.reminder.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String DEBUG_TAG = "AlarmReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG, "Recurring alarm; requesting download service.");
        WakeLocker.acquire(context);
        WakeLocker.release();
        String action = "" + intent.getAction();
        if (action.equalsIgnoreCase("SNOOZE_ACTION")) {
            ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 120000, PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_IMMUTABLE));
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            new AlarmHelper(context).createAlarm();
        } else {
            new NotificationHelper(context).createNotification();
        }
    }
}
