package com.trending.water.drinking.reminder.receiver;

import android.content.Context;
import android.util.Log;

import com.trending.water.drinking.reminder.appbasiclibs.utils.DatabaseHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DateHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.PreferenceHelper;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails;
import com.trending.water.drinking.reminder.model.backuprestore.BackupRestore;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

class AlarmHelper {
    private static final String TAG = "AlarmHelper";
    private final List<AlarmDetails> alarmDetailsList = new ArrayList<>();
    private final Context context;
    private final PreferenceHelper preferencesHelper;
    private final DatabaseHelper databaseHelper;

    AlarmHelper(Context context) {
        this.context = context;
        this.preferencesHelper = new PreferenceHelper(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void createAlarm() {
        loadData();
        setAlarm();
    }

    public void setAlarm() {
        BackupRestore backupRestore = new BackupRestore();
        backupRestore.setAlarmDetails(this.alarmDetailsList);
        backupRestore.setManualReminderActive(this.preferencesHelper.getBoolean(URLFactory.IS_MANUAL_REMINDER));

        for (AlarmDetails alarm : this.alarmDetailsList) {
            String alarmTime = alarm.getAlarmTime().trim();
            
            if ("M".equalsIgnoreCase(alarm.getAlarmType()) && backupRestore.isManualReminderActive()) {
                int hourOfDay = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", alarmTime));
                int minute = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", alarmTime));

                if (alarm.getSunday() == 1) {
                    MyAlarmManager.scheduleManualRecurringAlarm(context, Calendar.SUNDAY, hourOfDay, minute, Integer.parseInt(alarm.getAlarmSundayId()));
                }
                if (alarm.getMonday() == 1) {
                    MyAlarmManager.scheduleManualRecurringAlarm(context, Calendar.MONDAY, hourOfDay, minute, Integer.parseInt(alarm.getAlarmMondayId()));
                }
                if (alarm.getTuesday() == 1) {
                    MyAlarmManager.scheduleManualRecurringAlarm(context, Calendar.TUESDAY, hourOfDay, minute, Integer.parseInt(alarm.getAlarmTuesdayId()));
                }
                if (alarm.getWednesday() == 1) {
                    MyAlarmManager.scheduleManualRecurringAlarm(context, Calendar.WEDNESDAY, hourOfDay, minute, Integer.parseInt(alarm.getAlarmWednesdayId()));
                }
                if (alarm.getThursday() == 1) {
                    MyAlarmManager.scheduleManualRecurringAlarm(context, Calendar.THURSDAY, hourOfDay, minute, Integer.parseInt(alarm.getAlarmThursdayId()));
                }
                if (alarm.getFriday() == 1) {
                    MyAlarmManager.scheduleManualRecurringAlarm(context, Calendar.FRIDAY, hourOfDay, minute, Integer.parseInt(alarm.getAlarmFridayId()));
                }
                if (alarm.getSaturday() == 1) {
                    MyAlarmManager.scheduleManualRecurringAlarm(context, Calendar.SATURDAY, hourOfDay, minute, Integer.parseInt(alarm.getAlarmSaturdayId()));
                }
            }

            if (!backupRestore.isManualReminderActive()) {
                for (AlarmSubDetails subAlarm : alarm.getAlarmSubDetails()) {
                    String subAlarmTime = subAlarm.getAlarmTime().trim();
                    int hour = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", subAlarmTime));
                    int minute = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", subAlarmTime));

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    MyAlarmManager.scheduleAutoRecurringAlarm(context, calendar, Integer.parseInt(subAlarm.getAlarmId()));
                }
            }
        }
    }

    public void loadData() {
        ArrayList<HashMap<String, String>> alarmData = databaseHelper.getData("tbl_alarm_details");
        this.alarmDetailsList.clear();

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
            this.alarmDetailsList.add(alarm);
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
