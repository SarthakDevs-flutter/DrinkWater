package com.trending.water.drinking.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.Calendar;

public class BackupReceiver extends BroadcastReceiver {
    private static final String DEBUG_TAG = "AlarmReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG, "Recurring alarm 2; requesting download service.");
        if (("" + intent.getAction()).equals("android.intent.action.BOOT_COMPLETED")) {
            Preferences_Helper ph = new Preferences_Helper(context);
            if (ph.getBoolean(URLFactory.AUTO_BACK_UP)) {
                int _id = (int) System.currentTimeMillis();
                Calendar auto_backup_time = Calendar.getInstance();
                auto_backup_time.set(11, 1);
                auto_backup_time.set(12, 0);
                auto_backup_time.set(13, 0);
                auto_backup_time.set(14, 0);
                ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, _id);
                if (ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 0) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 0);
                } else if (ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 1) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 1);
                } else if (ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 2) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 2);
                }
            }
        } else {
            new BackupHelper(context).createAutoBackSetup();
        }
    }
}
