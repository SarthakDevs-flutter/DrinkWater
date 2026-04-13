package com.trending.water.drinking.reminder.receiver;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails;
import com.trending.water.drinking.reminder.model.backuprestore.BackupRestore;
import com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails;
import com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class BackupHelper {
    Date_Helper dth = new Date_Helper();
    Preferences_Helper ph;
    private Context mContext;

    BackupHelper(Context context) {
        this.mContext = context;
        this.ph = new Preferences_Helper(this.mContext);
    }

    /* access modifiers changed from: package-private */
    public void createAutoBackSetup() {
        if (this.ph.getBoolean(URLFactory.AUTO_BACK_UP)) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkStoragePermissions();
            } else {
                backup_data();
            }
        }
    }

    public void checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.mContext, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            backup_data();
        }
    }

    public void backup_data() {
        ArrayList<HashMap<String, String>> arr_data = getdata("tbl_container_details");
        BackupRestore backupRestore = new BackupRestore();
        List<ContainerDetails> containerDetailsList = new ArrayList<>();
        for (int k = 0; k < arr_data.size(); k++) {
            ContainerDetails containerDetails = new ContainerDetails();
            containerDetails.setContainerID((String) arr_data.get(k).get("ContainerID"));
            containerDetails.setContainerMeasure((String) arr_data.get(k).get("ContainerMeasure"));
            containerDetails.setContainerValue((String) arr_data.get(k).get("ContainerValue"));
            containerDetails.setContainerValueOZ((String) arr_data.get(k).get("ContainerValueOZ"));
            containerDetails.setIsOpen((String) arr_data.get(k).get("IsOpen"));
            containerDetails.setId((String) arr_data.get(k).get("id"));
            containerDetails.setIsCustom((String) arr_data.get(k).get("IsCustom"));
            containerDetailsList.add(containerDetails);
        }
        ArrayList<HashMap<String, String>> arr_data2 = getdata("tbl_drink_details");
        List<DrinkDetails> drinkDetailsList = new ArrayList<>();
        for (int k2 = 0; k2 < arr_data2.size(); k2++) {
            DrinkDetails drinkDetails = new DrinkDetails();
            drinkDetails.setDrinkDateTime((String) arr_data2.get(k2).get("DrinkDateTime"));
            drinkDetails.setDrinkDate((String) arr_data2.get(k2).get("DrinkDate"));
            drinkDetails.setDrinkTime((String) arr_data2.get(k2).get("DrinkTime"));
            drinkDetails.setContainerMeasure((String) arr_data2.get(k2).get("ContainerMeasure"));
            drinkDetails.setContainerValue((String) arr_data2.get(k2).get("ContainerValue"));
            drinkDetails.setContainerValueOZ((String) arr_data2.get(k2).get("ContainerValueOZ"));
            drinkDetails.setId((String) arr_data2.get(k2).get("id"));
            drinkDetails.setTodayGoal((String) arr_data2.get(k2).get("TodayGoal"));
            drinkDetails.setTodayGoalOZ((String) arr_data2.get(k2).get("TodayGoalOZ"));
            drinkDetailsList.add(drinkDetails);
        }
        ArrayList<HashMap<String, String>> arr_data3 = getdata("tbl_alarm_details");
        List<AlarmDetails> alarmDetailsList = new ArrayList<>();
        for (int k3 = 0; k3 < arr_data3.size(); k3++) {
            AlarmDetails alarmDetails = new AlarmDetails();
            alarmDetails.setAlarmId((String) arr_data3.get(k3).get("AlarmId"));
            alarmDetails.setAlarmInterval((String) arr_data3.get(k3).get("AlarmInterval"));
            alarmDetails.setAlarmTime((String) arr_data3.get(k3).get("AlarmTime"));
            alarmDetails.setAlarmType((String) arr_data3.get(k3).get("AlarmType"));
            alarmDetails.setId((String) arr_data3.get(k3).get("id"));
            alarmDetails.setAlarmSundayId((String) arr_data3.get(k3).get("SundayAlarmId"));
            alarmDetails.setAlarmMondayId((String) arr_data3.get(k3).get("MondayAlarmId"));
            alarmDetails.setAlarmTuesdayId((String) arr_data3.get(k3).get("TuesdayAlarmId"));
            alarmDetails.setAlarmWednesdayId((String) arr_data3.get(k3).get("WednesdayAlarmId"));
            alarmDetails.setAlarmThursdayId((String) arr_data3.get(k3).get("ThursdayAlarmId"));
            alarmDetails.setAlarmFridayId((String) arr_data3.get(k3).get("FridayAlarmId"));
            alarmDetails.setAlarmSaturdayId((String) arr_data3.get(k3).get("SaturdayAlarmId"));
            alarmDetails.setIsOff(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("IsOff"))));
            alarmDetails.setSunday(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("Sunday"))));
            alarmDetails.setMonday(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("Monday"))));
            alarmDetails.setTuesday(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("Tuesday"))));
            alarmDetails.setWednesday(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("Wednesday"))));
            alarmDetails.setThursday(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("Thursday"))));
            alarmDetails.setFriday(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("Friday"))));
            alarmDetails.setSaturday(Integer.valueOf(Integer.parseInt((String) arr_data3.get(k3).get("Saturday"))));
            List<AlarmSubDetails> alarmSubDetailsList = new ArrayList<>();
            ArrayList<HashMap<String, String>> arr_data22 = getdata("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data3.get(k3).get("id")));
            Log.d("arr_data2 : ", "" + arr_data22.size());
            for (int j = 0; j < arr_data22.size(); j++) {
                AlarmSubDetails alarmSubDetails = new AlarmSubDetails();
                alarmSubDetails.setAlarmId((String) arr_data22.get(j).get("AlarmId"));
                alarmSubDetails.setAlarmTime((String) arr_data22.get(j).get("AlarmTime"));
                alarmSubDetails.setId((String) arr_data22.get(j).get("id"));
                alarmSubDetails.setSuperId((String) arr_data22.get(j).get("SuperId"));
                alarmSubDetailsList.add(alarmSubDetails);
            }
            alarmDetails.setAlarmSubDetails(alarmSubDetailsList);
            alarmDetailsList.add(alarmDetails);
        }
        backupRestore.setContainerDetails(containerDetailsList);
        backupRestore.setDrinkDetails(drinkDetailsList);
        backupRestore.setAlarmDetails(alarmDetailsList);
        backupRestore.setTotalDrink(this.ph.getFloat(URLFactory.DAILY_WATER));
        backupRestore.setTotalWeight(this.ph.getString(URLFactory.PERSON_WEIGHT));
        backupRestore.setTotalHeight(this.ph.getString(URLFactory.PERSON_HEIGHT));
        backupRestore.isCMUnit(this.ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT));
        backupRestore.isKgUnit(this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT));
        backupRestore.isMlUnit(this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT));
        backupRestore.setReminderOption(Integer.valueOf(this.ph.getInt(URLFactory.REMINDER_OPTION)));
        backupRestore.setReminderSound(Integer.valueOf(this.ph.getInt(URLFactory.REMINDER_SOUND)));
        backupRestore.isDisableNotifiction(this.ph.getBoolean(URLFactory.DISABLE_NOTIFICATION));
        backupRestore.isManualReminderActive(this.ph.getBoolean(URLFactory.IS_MANUAL_REMINDER));
        backupRestore.isReminderVibrate(this.ph.getBoolean(URLFactory.REMINDER_VIBRATE));
        backupRestore.setUserName(this.ph.getString(URLFactory.USER_NAME));
        backupRestore.setUserGender(this.ph.getBoolean(URLFactory.USER_GENDER));
        backupRestore.isDisableSound(this.ph.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER));
        backupRestore.isAutoBackup(this.ph.getBoolean(URLFactory.AUTO_BACK_UP));
        backupRestore.setAutoBackupType(Integer.valueOf(this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)));
        backupRestore.setAutoBackupID(Integer.valueOf(this.ph.getInt(URLFactory.AUTO_BACK_UP_ID)));
        store_response(((JsonObject) new JsonParser().parse(new Gson().toJson((Object) backupRestore))).toString());
    }

    public void store_response(String plainBody) {
        File f = new File(Environment.getExternalStorageDirectory() + "/" + URLFactory.APP_DIRECTORY_NAME + "/");
        if (!f.exists()) {
            f.mkdir();
        }
        if (f.exists()) {
            String dt = this.dth.getCurrentDate("dd-MMM-yyyy hh:mm:ss a");
            File file = new File(Environment.getExternalStorageDirectory() + "/" + URLFactory.APP_DIRECTORY_NAME + "/Backup_" + dt + ".txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
                writer.append(plainBody);
                writer.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
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
