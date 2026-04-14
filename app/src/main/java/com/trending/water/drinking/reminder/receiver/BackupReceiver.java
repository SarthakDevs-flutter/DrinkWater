package com.trending.water.drinking.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.trending.water.drinking.reminder.appbasiclibs.utils.PreferenceHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.Calendar;

public class BackupReceiver extends BroadcastReceiver {
    private static final String TAG = "BackupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Backup alarm received: " + intent.getAction());

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            PreferenceHelper preferencesHelper = new PreferenceHelper(context);
            if (preferencesHelper.getBoolean(URLFactory.AUTO_BACK_UP)) {
                int backupId = (int) (System.currentTimeMillis() / 1000); // Using seconds to fit in int if needed
                Calendar backupTime = Calendar.getInstance();
                backupTime.set(Calendar.HOUR_OF_DAY, 1);
                backupTime.set(Calendar.MINUTE, 0);
                backupTime.set(Calendar.SECOND, 0);
                backupTime.set(Calendar.MILLISECOND, 0);

                preferencesHelper.savePreferences(URLFactory.AUTO_BACK_UP_ID, backupId);
                int backupType = preferencesHelper.getInt(URLFactory.AUTO_BACK_UP_TYPE);

                MyAlarmManager.scheduleAutoBackupAlarm(context, backupTime, backupId, backupType);
            }
        } else {
            new BackupHelper(context).createAutoBackup();
        }
    }
}
