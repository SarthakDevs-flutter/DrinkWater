package com.trending.water.drinking.reminder.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static final String ACTION_SNOOZE = "SNOOZE_ACTION";
    private static final long SNOOZE_DURATION_MS = 120000; // 2 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm received: " + intent.getAction());
        
        WakeLocker.acquire(context);
        
        try {
            String action = intent.getAction();
            if (ACTION_SNOOZE.equalsIgnoreCase(action)) {
                handleSnooze(context);
            } else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
                new AlarmHelper(context).createAlarm();
            } else {
                new NotificationHelper(context).createNotification();
            }
        } finally {
            WakeLocker.release();
        }
    }

    private void handleSnooze(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if (alarmManager != null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags |= PendingIntent.FLAG_IMMUTABLE;
            }
            
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags);
            long triggerAt = Calendar.getInstance().getTimeInMillis() + SNOOZE_DURATION_MS;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
            }
        }
        
        if (notificationManager != null) {
            notificationManager.cancel(0);
        }
    }
}
