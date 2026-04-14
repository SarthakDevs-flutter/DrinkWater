package com.trending.water.drinking.reminder.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class MyAlarmManager {
    private static final String TAG = "MyAlarmManager";

    public static void scheduleAutoRecurringAlarm(Context context, Calendar triggerTime, int alarmId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(triggerTime.getTimeInMillis());

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = getBroadcastPendingIntent(context, alarmId, intent);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Log.d(TAG, "Scheduling auto alarm ID: " + alarmId + " at " + calendar.getTimeInMillis());
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void scheduleManualRecurringAlarm(Context context, int dayOfWeek, int hour, int minute, int alarmId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = getBroadcastPendingIntent(context, alarmId, intent);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Log.d(TAG, "Scheduling manual alarm ID: " + alarmId + " for day " + dayOfWeek + " at " + calendar.getTimeInMillis());
            // 7 days in milliseconds
            long interval = 7 * AlarmManager.INTERVAL_DAY;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
        }
    }

    public static void cancelRecurringAlarm(Context context, int alarmId) {
        Log.d(TAG, "Canceling alarm ID: " + alarmId);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            alarmManager.cancel(getBroadcastPendingIntent(context, alarmId, intent));
        }
    }

    public static void scheduleAutoBackupAlarm(Context context, Calendar triggerTime, int alarmId, int autoBackupType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(triggerTime.getTimeInMillis());

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, BackupReceiver.class);
        PendingIntent pendingIntent = getBroadcastPendingIntent(context, alarmId, intent);

        long interval;
        if (autoBackupType == 1) { // Weekly
            interval = 7 * AlarmManager.INTERVAL_DAY;
        } else if (autoBackupType == 2) { // Monthly
            interval = 30 * AlarmManager.INTERVAL_DAY;
        } else { // Daily
            interval = AlarmManager.INTERVAL_DAY;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Log.d(TAG, "Scheduling backup alarm ID: " + alarmId + " type: " + autoBackupType + " at " + calendar.getTimeInMillis());
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
        }
    }

    private static PendingIntent getBroadcastPendingIntent(Context context, int alarmId, Intent intent) {
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        return PendingIntent.getBroadcast(context, alarmId, intent, flags);
    }
}
