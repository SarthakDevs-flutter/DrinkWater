package com.trending.water.drinking.reminder.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class MyAlarmManager {
    public static void scheduleAutoRecurringAlarm(Context context, Calendar updateTime2, int _id) {
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeInMillis(updateTime2.getTimeInMillis());
        if (updateTime.getTimeInMillis() < System.currentTimeMillis()) {
            updateTime.add(5, 1);
        }
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context, _id, new Intent(context, AlarmReceiver.class), 134217728);
        Log.d("MAINALARMID A :", _id + " @@@ " + updateTime.getTimeInMillis());
        ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setRepeating(0, updateTime.getTimeInMillis(), 86400000, recurringDownload);
    }

    public static void scheduleManualRecurringAlarm(Context context, int dayOfWeek, int hour, int minute, int _id) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, hour);
        calendar.set(12, minute);
        calendar.set(13, 0);
        calendar.set(7, dayOfWeek);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(6, 7);
        }
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context, _id, new Intent(context, AlarmReceiver.class), 134217728);
        Log.d("MAINALARMID M :", _id + " @@@ " + calendar.getTimeInMillis());
        ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setRepeating(0, calendar.getTimeInMillis(), 604800000, recurringDownload);
    }

    public static void cancelRecurringAlarm(Context context, int _id) {
        Log.d("MAINALARMID C :", "" + _id);
        ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(PendingIntent.getBroadcast(context, _id, new Intent(context, AlarmReceiver.class), 134217728));
    }

    public static void scheduleAutoBackupAlarm(Context context, Calendar updateTime2, int _id, int autoBackupType) {
        long interval;
        Context context2 = context;
        int i = _id;
        int i2 = autoBackupType;
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeInMillis(updateTime2.getTimeInMillis());
        if (updateTime.getTimeInMillis() < System.currentTimeMillis()) {
            updateTime.add(5, 1);
        }
        Intent intentAlarm = new Intent(context2, BackupReceiver.class);
        AlarmManager alarms = (AlarmManager) context2.getSystemService(NotificationCompat.CATEGORY_ALARM);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context2, i, intentAlarm, 134217728);
        if (i2 == 1) {
            interval = 604800000;
        } else if (i2 == 2) {
            interval = 2592000000L;
        } else {
            interval = 86400000;
        }
        Log.d("MAINBACKUP A :", i + " @@@ " + updateTime.getTimeInMillis() + " @@@ " + interval);
        alarms.setRepeating(0, updateTime.getTimeInMillis(), interval, recurringDownload);
    }
}
