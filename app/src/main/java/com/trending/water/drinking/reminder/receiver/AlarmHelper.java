package com.trending.water.drinking.reminder.receiver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails;
import com.trending.water.drinking.reminder.model.backuprestore.BackupRestore;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

class AlarmHelper {
    List<AlarmDetails> alarmDetailsList = new ArrayList();
    Date_Helper dth = new Date_Helper();
    Preferences_Helper ph;
    private Context mContext;

    AlarmHelper(Context context) {
        this.mContext = context;
        this.ph = new Preferences_Helper(this.mContext);
        Constant.SDB = this.mContext.openOrCreateDatabase(Constant.DATABASE_NAME, Context.MODE_PRIVATE, (SQLiteDatabase.CursorFactory) null);
    }

    public void createAlarm() {
        loadData();
        setAlarm();
    }

    public void setAlarm() {
        BackupRestore backupRestore = new BackupRestore();
        backupRestore.setAlarmDetails(this.alarmDetailsList);
        backupRestore.isManualReminderActive(this.ph.getBoolean(URLFactory.IS_MANUAL_REMINDER));
        for (int k = 0; k < this.alarmDetailsList.size(); k++) {
            if (backupRestore.getAlarmDetails().get(k).getAlarmType().equalsIgnoreCase("M") && backupRestore.isManualReminderActive()) {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                Date_Helper date_Helper = this.dth;
                sb.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));
                int hourOfDay = Integer.parseInt(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                Date_Helper date_Helper2 = this.dth;
                sb2.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));
                int minute = Integer.parseInt(sb2.toString());
                if (backupRestore.getAlarmDetails().get(k).getSunday().intValue() == 1) {
                    Context context = this.mContext;
                    MyAlarmManager.scheduleManualRecurringAlarm(context, 1, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSundayId()));
                }
                if (backupRestore.getAlarmDetails().get(k).getMonday().intValue() == 1) {
                    Context context2 = this.mContext;
                    MyAlarmManager.scheduleManualRecurringAlarm(context2, 2, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmMondayId()));
                }
                if (backupRestore.getAlarmDetails().get(k).getTuesday().intValue() == 1) {
                    Context context3 = this.mContext;
                    MyAlarmManager.scheduleManualRecurringAlarm(context3, 3, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmTuesdayId()));
                }
                if (backupRestore.getAlarmDetails().get(k).getWednesday().intValue() == 1) {
                    Context context4 = this.mContext;
                    MyAlarmManager.scheduleManualRecurringAlarm(context4, 4, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmWednesdayId()));
                }
                if (backupRestore.getAlarmDetails().get(k).getThursday().intValue() == 1) {
                    Context context5 = this.mContext;
                    MyAlarmManager.scheduleManualRecurringAlarm(context5, 5, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmThursdayId()));
                }
                if (backupRestore.getAlarmDetails().get(k).getFriday().intValue() == 1) {
                    Context context6 = this.mContext;
                    MyAlarmManager.scheduleManualRecurringAlarm(context6, 6, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmFridayId()));
                }
                if (backupRestore.getAlarmDetails().get(k).getSaturday().intValue() == 1) {
                    Context context7 = this.mContext;
                    MyAlarmManager.scheduleManualRecurringAlarm(context7, 7, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSaturdayId()));
                }
            }
            List<AlarmSubDetails> alarmSubDetailsList = backupRestore.getAlarmDetails().get(k).getAlarmSubDetails();
            for (int j = 0; j < alarmSubDetailsList.size(); j++) {
                if (!backupRestore.isManualReminderActive()) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("");
                    Date_Helper date_Helper3 = this.dth;
                    sb3.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", alarmSubDetailsList.get(j).getAlarmTime().trim()));
                    int hourOfDay2 = Integer.parseInt(sb3.toString());
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("");
                    Date_Helper date_Helper4 = this.dth;
                    sb4.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", alarmSubDetailsList.get(j).getAlarmTime().trim()));
                    int minute2 = Integer.parseInt(sb4.toString());
                    Calendar updateTime = Calendar.getInstance();
                    updateTime.set(11, hourOfDay2);
                    updateTime.set(12, minute2);
                    updateTime.set(13, 0);
                    Context context8 = this.mContext;
                    MyAlarmManager.scheduleAutoRecurringAlarm(context8, updateTime, Integer.parseInt("" + alarmSubDetailsList.get(j).getAlarmId()));
                }
            }
        }
    }

    public void loadData() {
        ArrayList<HashMap<String, String>> arr_data = getdata("tbl_alarm_details");
        this.alarmDetailsList.clear();
        for (int k = 0; k < arr_data.size(); k++) {
            AlarmDetails alarmDetails = new AlarmDetails();
            alarmDetails.setAlarmId((String) arr_data.get(k).get("AlarmId"));
            alarmDetails.setAlarmInterval((String) arr_data.get(k).get("AlarmInterval"));
            alarmDetails.setAlarmTime((String) arr_data.get(k).get("AlarmTime"));
            alarmDetails.setAlarmType((String) arr_data.get(k).get("AlarmType"));
            alarmDetails.setId((String) arr_data.get(k).get("id"));
            alarmDetails.setAlarmSundayId((String) arr_data.get(k).get("SundayAlarmId"));
            alarmDetails.setAlarmMondayId((String) arr_data.get(k).get("MondayAlarmId"));
            alarmDetails.setAlarmTuesdayId((String) arr_data.get(k).get("TuesdayAlarmId"));
            alarmDetails.setAlarmWednesdayId((String) arr_data.get(k).get("WednesdayAlarmId"));
            alarmDetails.setAlarmThursdayId((String) arr_data.get(k).get("ThursdayAlarmId"));
            alarmDetails.setAlarmFridayId((String) arr_data.get(k).get("FridayAlarmId"));
            alarmDetails.setAlarmSaturdayId((String) arr_data.get(k).get("SaturdayAlarmId"));
            alarmDetails.setIsOff(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("IsOff"))));
            alarmDetails.setSunday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Sunday"))));
            alarmDetails.setMonday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Monday"))));
            alarmDetails.setTuesday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Tuesday"))));
            alarmDetails.setWednesday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Wednesday"))));
            alarmDetails.setThursday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Thursday"))));
            alarmDetails.setFriday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Friday"))));
            alarmDetails.setSaturday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Saturday"))));
            List<AlarmSubDetails> alarmSubDetailsList = new ArrayList<>();
            ArrayList<HashMap<String, String>> arr_data2 = getdata("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data.get(k).get("id")));
            Log.d("arr_data2 : ", "" + arr_data2.size());
            for (int j = 0; j < arr_data2.size(); j++) {
                AlarmSubDetails alarmSubDetails = new AlarmSubDetails();
                alarmSubDetails.setAlarmId((String) arr_data2.get(j).get("AlarmId"));
                alarmSubDetails.setAlarmTime((String) arr_data2.get(j).get("AlarmTime"));
                alarmSubDetails.setId((String) arr_data2.get(j).get("id"));
                alarmSubDetails.setSuperId((String) arr_data2.get(j).get("SuperId"));
                alarmSubDetailsList.add(alarmSubDetails);
            }
            alarmDetails.setAlarmSubDetails(alarmSubDetailsList);
            this.alarmDetailsList.add(alarmDetails);
        }
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        Cursor c = Constant.SDB.rawQuery("SELECT * FROM " + table_name, (String[]) null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = ("SELECT * FROM " + table_name) + " where " + where_con;
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        System.out.println("SELECT QUERY : " + query);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }
}
