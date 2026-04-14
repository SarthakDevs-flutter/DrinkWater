package com.trending.water.drinking.reminder.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;

import com.trending.water.drinking.reminder.appbasiclibs.utils.DatabaseHelper;

import java.util.HashMap;

public class DbHelper {
    private final DatabaseHelper databaseHelper;

    public DbHelper(@NonNull Context context, @NonNull Activity activity) {
        this.databaseHelper = new DatabaseHelper(context, activity);
        createDatabaseTables();
    }

    private void createDatabaseTables() {
        // Table for container details
        HashMap<String, String> containerFields = new HashMap<>();
        containerFields.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        containerFields.put("ContainerID", "INTEGER DEFAULT 0");
        containerFields.put("ContainerValue", "TEXT");
        containerFields.put("ContainerValueOZ", "TEXT");
        containerFields.put("ContainerMeasure", "TEXT");
        containerFields.put("IsOpen", "TEXT");
        containerFields.put("IsCustom", "INTEGER DEFAULT 0");
        databaseHelper.createTable("tbl_container_details", containerFields);
        
        // Populate initial data if empty
        populateInitialContainerData();

        // Table for drink logs
        HashMap<String, String> drinkFields = new HashMap<>();
        drinkFields.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        drinkFields.put("ContainerValue", "TEXT");
        drinkFields.put("ContainerValueOZ", "TEXT");
        drinkFields.put("ContainerMeasure", "TEXT");
        drinkFields.put("DrinkDate", "TEXT");
        drinkFields.put("DrinkTime", "TEXT");
        drinkFields.put("DrinkDateTime", "TEXT");
        drinkFields.put("TodayGoal", "TEXT");
        drinkFields.put("TodayGoalOZ", "TEXT");
        databaseHelper.createTable("tbl_drink_details", drinkFields);

        // Table for alarm settings
        HashMap<String, String> alarmFields = new HashMap<>();
        alarmFields.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        alarmFields.put("AlarmTime", "TEXT");
        alarmFields.put("AlarmId", "TEXT");
        alarmFields.put("AlarmType", "TEXT");
        alarmFields.put("AlarmInterval", "TEXT");
        alarmFields.put("IsOff", "INTEGER DEFAULT 0");
        alarmFields.put("Sunday", "INTEGER DEFAULT 1");
        alarmFields.put("Monday", "INTEGER DEFAULT 1");
        alarmFields.put("Tuesday", "INTEGER DEFAULT 1");
        alarmFields.put("Wednesday", "INTEGER DEFAULT 1");
        alarmFields.put("Thursday", "INTEGER DEFAULT 1");
        alarmFields.put("Friday", "INTEGER DEFAULT 1");
        alarmFields.put("Saturday", "INTEGER DEFAULT 1");
        alarmFields.put("SundayAlarmId", "TEXT");
        alarmFields.put("MondayAlarmId", "TEXT");
        alarmFields.put("TuesdayAlarmId", "TEXT");
        alarmFields.put("WednesdayAlarmId", "TEXT");
        alarmFields.put("ThursdayAlarmId", "TEXT");
        alarmFields.put("FridayAlarmId", "TEXT");
        alarmFields.put("SaturdayAlarmId", "TEXT");
        databaseHelper.createTable("tbl_alarm_details", alarmFields);

        // Table for sub-alarm details
        HashMap<String, String> alarmSubFields = new HashMap<>();
        alarmSubFields.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        alarmSubFields.put("AlarmTime", "TEXT");
        alarmSubFields.put("AlarmId", "TEXT");
        alarmSubFields.put("SuperId", "TEXT");
        databaseHelper.createTable("tbl_alarm_sub_details", alarmSubFields);
    }

    private void populateInitialContainerData() {
        if (databaseHelper.totalRows("tbl_container_details") <= 0) {
            int[] valuesMl = {50, 100, 150, 200, 250, 300, 500, 600, 700, 800, 900, 1000};
            int[] valuesOz = {2, 3, 5, 7, 8, 10, 17, 20, 24, 27, 30, 34};
            int[] isOpenStates = {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0};
            
            for (int i = 0; i < valuesMl.length; i++) {
                ContentValues values = new ContentValues();
                values.put("ContainerID", i + 1);
                values.put("ContainerValue", String.valueOf(valuesMl[i]));
                values.put("ContainerValueOZ", String.valueOf(valuesOz[i]));
                values.put("IsOpen", String.valueOf(isOpenStates[i]));
                databaseHelper.insert("tbl_container_details", values);
            }
        }
    }

    public void clearAllData() {
        databaseHelper.remove("tbl_container_details");
        databaseHelper.remove("tbl_drink_details");
        databaseHelper.remove("tbl_alarm_details");
        databaseHelper.remove("tbl_alarm_sub_details");
        createDatabaseTables();
    }
}
