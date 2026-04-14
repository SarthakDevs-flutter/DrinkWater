package com.trending.water.drinking.reminder.receiver;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DatabaseHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DateHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.PreferenceHelper;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails;
import com.trending.water.drinking.reminder.model.backuprestore.BackupRestore;
import com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails;
import com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class BackupHelper {
    private static final String TAG = "BackupHelper";
    private final Context context;
    private final PreferenceHelper preferencesHelper;
    private final DateHelper dateHelper = new DateHelper();
    private final DatabaseHelper databaseHelper;

    BackupHelper(Context context) {
        this.context = context;
        this.preferencesHelper = new PreferenceHelper(context);
        this.databaseHelper = new DatabaseHelper(context, null);
    }

    void createAutoBackup() {
        if (preferencesHelper.getBoolean(URLFactory.AUTO_BACK_UP)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkStoragePermissionsAndBackup();
            } else {
                performBackup();
            }
        }
    }

    private void checkStoragePermissionsAndBackup() {
        boolean hasRead = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        boolean hasWrite = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        
        if (hasRead && hasWrite) {
            performBackup();
        } else {
            Log.w(TAG, "Storage permissions not granted for auto backup");
        }
    }

    public void performBackup() {
        BackupRestore backupRestore = new BackupRestore();
        
        // 1. Container Details
        ArrayList<HashMap<String, String>> containerData = databaseHelper.getData("tbl_container_details");
        List<ContainerDetails> containerDetailsList = new ArrayList<>();
        for (HashMap<String, String> map : containerData) {
            ContainerDetails container = new ContainerDetails();
            container.setContainerId(map.get("ContainerID"));
            container.setContainerMeasure(map.get("ContainerMeasure"));
            container.setContainerValue(map.get("ContainerValue"));
            container.setContainerValueOZ(map.get("ContainerValueOZ"));
            container.setIsOpen(map.get("IsOpen"));
            container.setId(map.get("id"));
            container.setIsCustom(map.get("IsCustom"));
            containerDetailsList.add(container);
        }
        backupRestore.setContainerDetails(containerDetailsList);

        // 2. Drink Details
        ArrayList<HashMap<String, String>> drinkData = databaseHelper.getData("tbl_drink_details");
        List<DrinkDetails> drinkDetailsList = new ArrayList<>();
        for (HashMap<String, String> map : drinkData) {
            DrinkDetails drink = new DrinkDetails();
            drink.setDrinkDateTime(map.get("DrinkDateTime"));
            drink.setDrinkDate(map.get("DrinkDate"));
            drink.setDrinkTime(map.get("DrinkTime"));
            drink.setContainerMeasure(map.get("ContainerMeasure"));
            drink.setContainerValue(map.get("ContainerValue"));
            drink.setContainerValueOZ(map.get("ContainerValueOZ"));
            drink.setId(map.get("id"));
            drink.setTodayGoal(map.get("TodayGoal"));
            drink.setTodayGoalOZ(map.get("TodayGoalOZ"));
            drinkDetailsList.add(drink);
        }
        backupRestore.setDrinkDetails(drinkDetailsList);

        // 3. Alarm Details
        ArrayList<HashMap<String, String>> alarmData = databaseHelper.getData("tbl_alarm_details");
        List<AlarmDetails> alarmDetailsList = new ArrayList<>();
        for (HashMap<String, String> map : alarmData) {
            AlarmDetails alarm = new AlarmDetails();
            alarm.setAlarmId(map.get("AlarmId"));
            alarm.setAlarmInterval(map.get("AlarmInterval"));
            alarm.setAlarmTime(map.get("AlarmTime"));
            alarm.setAlarmType(map.get("AlarmType"));
            alarm.setId(map.get("id"));
            alarm.setAlarmSundayId(map.get("SundayAlarmId"));
            alarm.setAlarmMondayId(map.get("MondayAlarmId"));
            alarm.setAlarmTuesdayId(map.get("TuesdayAlarmId"));
            alarm.setAlarmWednesdayId(map.get("WednesdayAlarmId"));
            alarm.setAlarmThursdayId(map.get("ThursdayAlarmId"));
            alarm.setAlarmFridayId(map.get("FridayAlarmId"));
            alarm.setAlarmSaturdayId(map.get("SaturdayAlarmId"));
            alarm.setIsOff(safeParseInt(map.get("IsOff")));
            alarm.setSunday(safeParseInt(map.get("Sunday")));
            alarm.setMonday(safeParseInt(map.get("Monday")));
            alarm.setTuesday(safeParseInt(map.get("Tuesday")));
            alarm.setWednesday(safeParseInt(map.get("Wednesday")));
            alarm.setThursday(safeParseInt(map.get("Thursday")));
            alarm.setFriday(safeParseInt(map.get("Friday")));
            alarm.setSaturday(safeParseInt(map.get("Saturday")));

            ArrayList<HashMap<String, String>> subAlarmData = databaseHelper.getData("tbl_alarm_sub_details", "SuperId=" + map.get("id"));
            List<AlarmSubDetails> subDetailsList = new ArrayList<>();
            for (HashMap<String, String> subMap : subAlarmData) {
                AlarmSubDetails subAlarm = new AlarmSubDetails();
                subAlarm.setAlarmId(subMap.get("AlarmId"));
                subAlarm.setAlarmTime(subMap.get("AlarmTime"));
                subAlarm.setId(subMap.get("id"));
                subAlarm.setSuperId(subMap.get("SuperId"));
                subDetailsList.add(subAlarm);
            }
            alarm.setAlarmSubDetails(subDetailsList);
            alarmDetailsList.add(alarm);
        }
        backupRestore.setAlarmDetails(alarmDetailsList);

        // 4. Preferences
        backupRestore.setTotalDrink(preferencesHelper.getFloat(URLFactory.DAILY_WATER));
        backupRestore.setTotalWeight(preferencesHelper.getString(URLFactory.PERSON_WEIGHT));
        backupRestore.setTotalHeight(preferencesHelper.getString(URLFactory.PERSON_HEIGHT));
        backupRestore.setCMUnit(preferencesHelper.getBoolean(URLFactory.PERSON_HEIGHT_UNIT));
        backupRestore.setKgUnit(preferencesHelper.getBoolean(URLFactory.PERSON_WEIGHT_UNIT));
        backupRestore.setMlUnit(preferencesHelper.getBoolean(URLFactory.PERSON_WEIGHT_UNIT));
        backupRestore.setReminderOption(preferencesHelper.getInt(URLFactory.REMINDER_OPTION));
        backupRestore.setReminderSound(preferencesHelper.getInt(URLFactory.REMINDER_SOUND));
        backupRestore.setDisableNotification(preferencesHelper.getBoolean(URLFactory.DISABLE_NOTIFICATION));
        backupRestore.setManualReminderActive(preferencesHelper.getBoolean(URLFactory.IS_MANUAL_REMINDER));
        backupRestore.setReminderVibrate(preferencesHelper.getBoolean(URLFactory.REMINDER_VIBRATE));
        backupRestore.setUserName(preferencesHelper.getString(URLFactory.USER_NAME));
        backupRestore.setUserGender(preferencesHelper.getBoolean(URLFactory.USER_GENDER));
        backupRestore.setDisableSound(preferencesHelper.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER));
        backupRestore.setAutoBackup(preferencesHelper.getBoolean(URLFactory.AUTO_BACK_UP));
        backupRestore.setAutoBackupType(preferencesHelper.getInt(URLFactory.AUTO_BACK_UP_TYPE));
        backupRestore.setAutoBackupId(preferencesHelper.getInt(URLFactory.AUTO_BACK_UP_ID));

        String json = new Gson().toJson(backupRestore);
        saveBackupToFile(json);
    }

    private void saveBackupToFile(String content) {
        File dir = new File(Environment.getExternalStorageDirectory(), URLFactory.APP_DIRECTORY_NAME);
        if (!dir.exists() && !dir.mkdirs()) {
            Log.e(TAG, "Failed to create directory: " + dir.getAbsolutePath());
            return;
        }

        String timestamp = dateHelper.getCurrentDate("dd-MMM-yyyy_HH-mm-ss");
        File file = new File(dir, "Backup_" + timestamp + ".txt");

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(content);
            Log.d(TAG, "Backup saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Error writing backup file", e);
        }
    }

    private int safeParseInt(String value) {
        try {
            return (value != null) ? Integer.parseInt(value) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
