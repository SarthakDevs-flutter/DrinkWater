package com.trending.water.drinking.reminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trending.water.drinking.reminder.adapter.FileAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.model.BackUpFileModel;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails;
import com.trending.water.drinking.reminder.model.backuprestore.BackupRestore;
import com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails;
import com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails;
import com.trending.water.drinking.reminder.receiver.MyAlarmManager;
import com.trending.water.drinking.reminder.utils.FileUtils2;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Screen_Backup_Restore extends MasterBaseActivity {
    private static final int ALL_PERMISSION = 3;
    private static final int SELECT_FILE = 1;
    LinearLayout auto_backup_option_block;
    Calendar auto_backup_time = Calendar.getInstance();
    LinearLayout backup_block;
    LinearLayout clear_block;
    boolean executeBackup = true;
    FileAdapter fileAdapter;
    AppCompatTextView lbl_toolbar_title;
    LinearLayout left_icon_block;
    List<BackUpFileModel> lst_backup_file = new ArrayList();
    RadioButton rdo_daily;
    RadioButton rdo_monthly;
    RadioButton rdo_weekly;
    LinearLayout restore_block;
    LinearLayout right_icon_block;
    SwitchCompat switch_auto_backup;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_backup_restore);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(this.mContext.getResources().getColor(R.color.str_green_card));
        }
        FindViewById();
        Body();
    }

    private void FindViewById() {
        this.right_icon_block = (LinearLayout) findViewById(R.id.right_icon_block);
        this.left_icon_block = (LinearLayout) findViewById(R.id.left_icon_block);
        this.lbl_toolbar_title = (AppCompatTextView) findViewById(R.id.lbl_toolbar_title);
        this.backup_block = (LinearLayout) findViewById(R.id.backup_block);
        this.restore_block = (LinearLayout) findViewById(R.id.restore_block);
        this.clear_block = (LinearLayout) findViewById(R.id.clear_block);
        this.switch_auto_backup = (SwitchCompat) findViewById(R.id.switch_auto_backup);
        this.auto_backup_option_block = (LinearLayout) findViewById(R.id.auto_backup_option_block);
        this.rdo_daily = (RadioButton) findViewById(R.id.rdo_daily);
        this.rdo_weekly = (RadioButton) findViewById(R.id.rdo_weekly);
        this.rdo_monthly = (RadioButton) findViewById(R.id.rdo_monthly);
    }

    public void setTime() {
        this.auto_backup_time.set(9, 0);
        this.auto_backup_time.set(11, 1);
        this.auto_backup_time.set(12, 0);
        this.auto_backup_time.set(13, 0);
        this.auto_backup_time.set(14, 0);
    }

    private void Body() {
        this.lbl_toolbar_title.setText(this.sh.capitalize(this.sh.get_string(R.string.str_backup_and_restore)));
        this.left_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Backup_Restore.this.finish();
            }
        });
        int i = 8;
        this.right_icon_block.setVisibility(View.GONE);
        this.switch_auto_backup.setChecked(this.ph.getBoolean(URLFactory.AUTO_BACK_UP));
        LinearLayout linearLayout = this.auto_backup_option_block;
        if (this.ph.getBoolean(URLFactory.AUTO_BACK_UP)) {
            i = 0;
        }
        linearLayout.setVisibility(i);
        this.switch_auto_backup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Screen_Backup_Restore.this.auto_backup_option_block.setVisibility(isChecked ? View.VISIBLE : android.view.View.GONE);
                Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP, isChecked);
                if (isChecked) {
                    Screen_Backup_Restore.this.setTime();
                    int _id = (int) System.currentTimeMillis();
                    Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, _id);
                    if (Screen_Backup_Restore.this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 0) {
                        MyAlarmManager.scheduleAutoBackupAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.auto_backup_time, _id, 0);
                    } else if (Screen_Backup_Restore.this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 1) {
                        MyAlarmManager.scheduleAutoBackupAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.auto_backup_time, _id, 1);
                    } else if (Screen_Backup_Restore.this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 2) {
                        MyAlarmManager.scheduleAutoBackupAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.auto_backup_time, _id, 2);
                    }
                } else {
                    MyAlarmManager.cancelRecurringAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.ph.getInt(URLFactory.AUTO_BACK_UP_ID));
                }
            }
        });
        boolean z = true;
        this.rdo_daily.setChecked(this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 0);
        this.rdo_weekly.setChecked(this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 1);
        RadioButton radioButton = this.rdo_monthly;
        if (this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) != 2) {
            z = false;
        }
        radioButton.setChecked(z);
        setTime();
        this.rdo_daily.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAlarmManager.cancelRecurringAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.ph.getInt(URLFactory.AUTO_BACK_UP_ID));
                Screen_Backup_Restore.this.setTime();
                Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE, 0);
                int _id = (int) System.currentTimeMillis();
                Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, _id);
                MyAlarmManager.scheduleAutoBackupAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.auto_backup_time, _id, 0);
            }
        });
        this.rdo_weekly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAlarmManager.cancelRecurringAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.ph.getInt(URLFactory.AUTO_BACK_UP_ID));
                Screen_Backup_Restore.this.setTime();
                Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE, 1);
                int _id = (int) System.currentTimeMillis();
                Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, _id);
                MyAlarmManager.scheduleAutoBackupAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.auto_backup_time, _id, 1);
            }
        });
        this.rdo_monthly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAlarmManager.cancelRecurringAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.ph.getInt(URLFactory.AUTO_BACK_UP_ID));
                Screen_Backup_Restore.this.setTime();
                Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE, 2);
                int _id = (int) System.currentTimeMillis();
                Screen_Backup_Restore.this.ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, _id);
                MyAlarmManager.scheduleAutoBackupAlarm(Screen_Backup_Restore.this.mContext, Screen_Backup_Restore.this.auto_backup_time, _id, 2);
            }
        });
        this.backup_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Backup_Restore.this.executeBackup = true;
                if (Build.VERSION.SDK_INT >= 23) {
                    Screen_Backup_Restore.this.checkStoragePermissions();
                } else {
                    new backupFromDBData().execute(new String[0]);
                }
            }
        });
        this.restore_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Backup_Restore.this.executeBackup = false;
                if (Build.VERSION.SDK_INT >= 23) {
                    Screen_Backup_Restore.this.checkStoragePermissions();
                } else {
                    Screen_Backup_Restore.this.restore_data();
                }
            }
        });
        this.clear_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(Screen_Backup_Restore.this.act).setMessage(Screen_Backup_Restore.this.sh.get_string(R.string.str_clear_all_data_confirmation_messge)).setPositiveButton(Screen_Backup_Restore.this.sh.get_string(R.string.str_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        String path = Environment.getExternalStorageDirectory().toString() + "/" + URLFactory.APP_DIRECTORY_NAME;
                        Log.d("Files", "Path: " + path);
                        File[] files = new File(path).listFiles();
                        for (int k = 0; k < files.length; k++) {
                            if (files[k].exists()) {
                                files[k].delete();
                            }
                        }
                    }
                }).setNegativeButton(Screen_Backup_Restore.this.sh.get_string(R.string.str_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    public void checkStoragePermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this.act, "android.permission.READ_EXTERNAL_STORAGE") != 0 || ContextCompat.checkSelfPermission(this.act, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 3);
        } else if (this.executeBackup) {
            new backupFromDBData().execute(new String[0]);
        } else {
            restore_data();
        }
    }

    public void backup_data() {
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_container_details");
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
        ArrayList<HashMap<String, String>> arr_data2 = this.dh.getdata("tbl_drink_details");
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
        ArrayList<HashMap<String, String>> arr_data3 = this.dh.getdata("tbl_alarm_details");
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
            Database_Helper database_Helper = this.dh;
            ArrayList<HashMap<String, String>> arr_data22 = database_Helper.getdata("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data3.get(k3).get("id")));
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
        backupRestore.isActive(this.ph.getBoolean(URLFactory.IS_ACTIVE));
        backupRestore.isBreastfeeding(this.ph.getBoolean(URLFactory.IS_BREATFEEDING));
        backupRestore.isPregnant(this.ph.getBoolean(URLFactory.IS_PREGNANT));
        backupRestore.setWeatherConditions(Integer.valueOf(this.ph.getInt(URLFactory.WEATHER_CONSITIONS)));
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

    public void restore_data() {
        load_backup_file();
        if (this.lst_backup_file.size() > 0) {
            openBackUpFilePicker();
        } else {
            this.ah.customAlert(this.sh.get_string(R.string.str_no_backup_found));
        }
    }

    public void load_backup_file() {
        this.lst_backup_file.clear();
        String path = Environment.getExternalStorageDirectory().toString() + "/" + URLFactory.APP_DIRECTORY_NAME;
        Log.d("Files", "Path: " + path);
        File[] files = new File(path).listFiles();
        List<File> lst_file = new ArrayList<>();
        if (files != null && files.length > 1) {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File object1, File object2) {
                    return (int) (object1.lastModified() > object2.lastModified() ? object1.lastModified() : object2.lastModified());
                }
            });
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".txt")) {
                lst_file.add(files[i]);
            }
        }
        Collections.reverse(lst_file);
        Log.d("Files", "Size: " + files.length);
        int i2 = 0;
        while (i2 < lst_file.size()) {
            Log.d("Files", "FileName:" + lst_file.get(i2).getName() + " @@@ " + lst_file.get(i2).lastModified());
            BackUpFileModel backUpFileModel = new BackUpFileModel();
            backUpFileModel.setName(lst_file.get(i2).getName());
            backUpFileModel.setPath(lst_file.get(i2).getPath());
            backUpFileModel.setLastmodify(lst_file.get(i2).lastModified());
            backUpFileModel.isSelected(i2 == 0);
            this.lst_backup_file.add(backUpFileModel);
            i2++;
        }
    }

    public void openBackUpFilePicker() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_backup_pick, (ViewGroup) null, false);
        ((AppCompatTextView) view.findViewById(R.id.btn_text)).setText(this.sh.get_string(R.string.str_restore));
        ((RelativeLayout) view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((RelativeLayout) view.findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(Screen_Backup_Restore.this.act).setTitle(Screen_Backup_Restore.this.sh.get_string(R.string.str_restore_all_data_confirmation_messge)).setPositiveButton(Screen_Backup_Restore.this.sh.get_string(R.string.str_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog2, int whichButton) {
                        dialog2.dismiss();
                        int pos = -1;
                        for (int k = 0; k < Screen_Backup_Restore.this.lst_backup_file.size(); k++) {
                            if (Screen_Backup_Restore.this.lst_backup_file.get(k).isSelected()) {
                                pos = k;
                            }
                        }
                        dialog.dismiss();
                        if (pos >= 0) {
                            new restoreFromBackupData(Screen_Backup_Restore.this.lst_backup_file.get(pos).getPath()).execute(new String[0]);
                        }
                    }
                }).setNegativeButton(Screen_Backup_Restore.this.sh.get_string(R.string.str_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog2, int whichButton) {
                        dialog2.dismiss();
                    }
                }).show();
            }
        });
        RecyclerView soundRecyclerView = (RecyclerView) view.findViewById(R.id.soundRecyclerView);
        this.fileAdapter = new FileAdapter(this.act, this.lst_backup_file, new FileAdapter.CallBack() {
            public void onClickSelect(BackUpFileModel backUpFileModel, int position) {
                for (int k = 0; k < Screen_Backup_Restore.this.lst_backup_file.size(); k++) {
                    Screen_Backup_Restore.this.lst_backup_file.get(k).isSelected(false);
                }
                Screen_Backup_Restore.this.lst_backup_file.get(position).isSelected(true);
                Screen_Backup_Restore.this.fileAdapter.notifyDataSetChanged();
            }
        });
        soundRecyclerView.setLayoutManager(new LinearLayoutManager(this.act, androidx.recyclerview.widget.RecyclerView.VERTICAL, false));
        soundRecyclerView.setAdapter(this.fileAdapter);
        dialog.setContentView(view);
        dialog.show();
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0166 A[LOOP:3: B:26:0x015c->B:28:0x0166, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0260 A[LOOP:4: B:30:0x0256->B:32:0x0260, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x03ab  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0901  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x090c  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x090f  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x09b3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void restoreAppData(String r20) {
        /*
            r19 = this;
            r1 = r19
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r2 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException -> 0x002a }
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ IOException -> 0x002a }
            r4 = r20
            r3.<init>(r4)     // Catch:{ IOException -> 0x0028 }
            r0.<init>(r3)     // Catch:{ IOException -> 0x0028 }
        L_0x0014:
            java.lang.String r3 = r0.readLine()     // Catch:{ IOException -> 0x0028 }
            r5 = r3
            if (r3 == 0) goto L_0x0024
            r2.append(r5)     // Catch:{ IOException -> 0x0028 }
            java.lang.String r3 = "\n"
            r2.append(r3)     // Catch:{ IOException -> 0x0028 }
            goto L_0x0014
        L_0x0024:
            r0.close()     // Catch:{ IOException -> 0x0028 }
            goto L_0x0030
        L_0x0028:
            r0 = move-exception
            goto L_0x002d
        L_0x002a:
            r0 = move-exception
            r4 = r20
        L_0x002d:
            r0.printStackTrace()
        L_0x0030:
            java.lang.String r0 = r2.toString()
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r3 = r1.dh
            java.lang.String r5 = "tbl_alarm_details"
            java.util.ArrayList r3 = r3.getdata(r5)
            r6 = 0
        L_0x003d:
            int r7 = r3.size()
            if (r6 >= r7) goto L_0x0147
            java.lang.Object r7 = r3.get(r6)
            java.util.HashMap r7 = (java.util.HashMap) r7
            java.lang.String r8 = "AlarmType"
            java.lang.Object r7 = r7.get(r8)
            java.lang.String r7 = (java.lang.String) r7
            java.lang.String r8 = "M"
            boolean r7 = r7.equalsIgnoreCase(r8)
            if (r7 == 0) goto L_0x00fb
            android.content.Context r7 = r1.mContext
            java.lang.Object r8 = r3.get(r6)
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.String r9 = "SundayAlarmId"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            int r8 = java.lang.Integer.parseInt(r8)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r7, r8)
            android.content.Context r7 = r1.mContext
            java.lang.Object r8 = r3.get(r6)
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.String r9 = "MondayAlarmId"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            int r8 = java.lang.Integer.parseInt(r8)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r7, r8)
            android.content.Context r7 = r1.mContext
            java.lang.Object r8 = r3.get(r6)
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.String r9 = "TuesdayAlarmId"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            int r8 = java.lang.Integer.parseInt(r8)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r7, r8)
            android.content.Context r7 = r1.mContext
            java.lang.Object r8 = r3.get(r6)
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.String r9 = "WednesdayAlarmId"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            int r8 = java.lang.Integer.parseInt(r8)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r7, r8)
            android.content.Context r7 = r1.mContext
            java.lang.Object r8 = r3.get(r6)
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.String r9 = "ThursdayAlarmId"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            int r8 = java.lang.Integer.parseInt(r8)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r7, r8)
            android.content.Context r7 = r1.mContext
            java.lang.Object r8 = r3.get(r6)
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.String r9 = "FridayAlarmId"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            int r8 = java.lang.Integer.parseInt(r8)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r7, r8)
            android.content.Context r7 = r1.mContext
            java.lang.Object r8 = r3.get(r6)
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.String r9 = "SaturdayAlarmId"
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            int r8 = java.lang.Integer.parseInt(r8)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r7, r8)
            goto L_0x0143
        L_0x00fb:
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r7 = r1.dh
            java.lang.String r8 = "tbl_alarm_sub_details"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "SuperId="
            r9.append(r10)
            java.lang.Object r10 = r3.get(r6)
            java.util.HashMap r10 = (java.util.HashMap) r10
            java.lang.String r11 = "id"
            java.lang.Object r10 = r10.get(r11)
            java.lang.String r10 = (java.lang.String) r10
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.util.ArrayList r7 = r7.getdata(r8, r9)
            r8 = 0
        L_0x0123:
            int r9 = r7.size()
            if (r8 >= r9) goto L_0x0143
            android.content.Context r9 = r1.mContext
            java.lang.Object r10 = r7.get(r8)
            java.util.HashMap r10 = (java.util.HashMap) r10
            java.lang.String r11 = "AlarmId"
            java.lang.Object r10 = r10.get(r11)
            java.lang.String r10 = (java.lang.String) r10
            int r10 = java.lang.Integer.parseInt(r10)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r9, r10)
            int r8 = r8 + 1
            goto L_0x0123
        L_0x0143:
            int r6 = r6 + 1
            goto L_0x003d
        L_0x0147:
            com.google.gson.Gson r6 = new com.google.gson.Gson
            r6.<init>()
            java.lang.Class<com.trending.water.drinking.reminder.model.backuprestore.BackupRestore> r7 = com.trending.water.drinking.reminder.model.backuprestore.BackupRestore.class
            java.lang.Object r6 = r6.fromJson((java.lang.String) r0, r7)
            com.trending.water.drinking.reminder.model.backuprestore.BackupRestore r6 = (com.trending.water.drinking.reminder.model.backuprestore.BackupRestore) r6
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r7 = r1.dh
            java.lang.String r8 = "tbl_container_details"
            r7.REMOVE(r8)
            r7 = 0
        L_0x015c:
            java.util.List r8 = r6.getContainerDetails()
            int r8 = r8.size()
            if (r7 >= r8) goto L_0x024e
            android.content.ContentValues r8 = new android.content.ContentValues
            r8.<init>()
            java.lang.String r9 = "ContainerID"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getContainerDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails) r11
            java.lang.String r11 = r11.getContainerID()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "ContainerValue"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getContainerDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails) r11
            java.lang.String r11 = r11.getContainerValue()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "ContainerValueOZ"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getContainerDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails) r11
            java.lang.String r11 = r11.getContainerValueOZ()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "ContainerMeasure"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getContainerDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails) r11
            java.lang.String r11 = r11.getContainerMeasure()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "IsOpen"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getContainerDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails) r11
            java.lang.String r11 = r11.getIsOpen()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "IsCustom"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getContainerDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails) r11
            java.lang.String r11 = r11.getIsCustom()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r9 = r1.dh
            java.lang.String r10 = "tbl_container_details"
            r9.INSERT((java.lang.String) r10, (android.content.ContentValues) r8)
            int r7 = r7 + 1
            goto L_0x015c
        L_0x024e:
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r7 = r1.dh
            java.lang.String r8 = "tbl_drink_details"
            r7.REMOVE(r8)
            r7 = 0
        L_0x0256:
            java.util.List r8 = r6.getDrinkDetails()
            int r8 = r8.size()
            if (r7 >= r8) goto L_0x0390
            android.content.ContentValues r8 = new android.content.ContentValues
            r8.<init>()
            java.lang.String r9 = "ContainerValue"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getContainerValue()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "ContainerValueOZ"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getContainerValueOZ()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "ContainerMeasure"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getContainerMeasure()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "DrinkDate"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getDrinkDate()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "DrinkTime"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getDrinkTime()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "DrinkDateTime"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getDrinkDateTime()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "TodayGoal"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getTodayGoal()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            java.lang.String r9 = "TodayGoalOZ"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = ""
            r10.append(r11)
            java.util.List r11 = r6.getDrinkDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails) r11
            java.lang.String r11 = r11.getTodayGoalOZ()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            r8.put(r9, r10)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r9 = r1.dh
            java.lang.String r10 = "tbl_drink_details"
            r9.INSERT((java.lang.String) r10, (android.content.ContentValues) r8)
            int r7 = r7 + 1
            goto L_0x0256
        L_0x0390:
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r7 = r1.dh
            java.lang.String r8 = "tbl_alarm_details"
            r7.REMOVE(r8)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r7 = r1.dh
            java.lang.String r8 = "tbl_alarm_sub_details"
            r7.REMOVE(r8)
            r7 = 0
        L_0x039f:
            java.util.List r8 = r6.getAlarmDetails()
            int r8 = r8.size()
            r9 = 2
            r10 = 1
            if (r7 >= r8) goto L_0x08b5
            android.content.ContentValues r8 = new android.content.ContentValues
            r8.<init>()
            java.lang.String r11 = "AlarmTime"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = ""
            r12.append(r13)
            java.util.List r13 = r6.getAlarmDetails()
            java.lang.Object r13 = r13.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r13 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r13
            java.lang.String r13 = r13.getAlarmTime()
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            r8.put(r11, r12)
            java.lang.String r11 = "AlarmId"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = ""
            r12.append(r13)
            java.util.List r13 = r6.getAlarmDetails()
            java.lang.Object r13 = r13.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r13 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r13
            java.lang.String r13 = r13.getAlarmId()
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            r8.put(r11, r12)
            java.lang.String r11 = "AlarmType"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = ""
            r12.append(r13)
            java.util.List r13 = r6.getAlarmDetails()
            java.lang.Object r13 = r13.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r13 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r13
            java.lang.String r13 = r13.getAlarmType()
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            r8.put(r11, r12)
            java.lang.String r11 = "AlarmInterval"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = ""
            r12.append(r13)
            java.util.List r13 = r6.getAlarmDetails()
            java.lang.Object r13 = r13.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r13 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r13
            java.lang.String r13 = r13.getAlarmInterval()
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            r8.put(r11, r12)
            java.lang.String r11 = "SundayAlarmId"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.String r12 = r12.getAlarmSundayId()
            r8.put(r11, r12)
            java.lang.String r11 = "MondayAlarmId"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.String r12 = r12.getAlarmMondayId()
            r8.put(r11, r12)
            java.lang.String r11 = "TuesdayAlarmId"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.String r12 = r12.getAlarmTuesdayId()
            r8.put(r11, r12)
            java.lang.String r11 = "WednesdayAlarmId"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.String r12 = r12.getAlarmWednesdayId()
            r8.put(r11, r12)
            java.lang.String r11 = "ThursdayAlarmId"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.String r12 = r12.getAlarmThursdayId()
            r8.put(r11, r12)
            java.lang.String r11 = "FridayAlarmId"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.String r12 = r12.getAlarmFridayId()
            r8.put(r11, r12)
            java.lang.String r11 = "SaturdayAlarmId"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.String r12 = r12.getAlarmSaturdayId()
            r8.put(r11, r12)
            java.lang.String r11 = "IsOff"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getIsOff()
            r8.put(r11, r12)
            java.lang.String r11 = "Sunday"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getSunday()
            r8.put(r11, r12)
            java.lang.String r11 = "Monday"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getMonday()
            r8.put(r11, r12)
            java.lang.String r11 = "Tuesday"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getTuesday()
            r8.put(r11, r12)
            java.lang.String r11 = "Wednesday"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getWednesday()
            r8.put(r11, r12)
            java.lang.String r11 = "Thursday"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getThursday()
            r8.put(r11, r12)
            java.lang.String r11 = "Friday"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getFriday()
            r8.put(r11, r12)
            java.lang.String r11 = "Saturday"
            java.util.List r12 = r6.getAlarmDetails()
            java.lang.Object r12 = r12.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r12 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r12
            java.lang.Integer r12 = r12.getSaturday()
            r8.put(r11, r12)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r11 = r1.dh
            java.lang.String r12 = "tbl_alarm_details"
            r11.INSERT((java.lang.String) r12, (android.content.ContentValues) r8)
            java.util.List r11 = r6.getAlarmDetails()
            java.lang.Object r11 = r11.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r11 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r11
            java.lang.String r11 = r11.getAlarmType()
            java.lang.String r12 = "M"
            boolean r11 = r11.equalsIgnoreCase(r12)
            if (r11 == 0) goto L_0x078b
            boolean r11 = r6.isManualReminderActive()
            if (r11 == 0) goto L_0x078b
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = ""
            r11.append(r12)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper r12 = r1.dth
            java.lang.String r12 = "hh:mm a"
            java.lang.String r13 = "HH"
            java.util.List r14 = r6.getAlarmDetails()
            java.lang.Object r14 = r14.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r14 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r14
            java.lang.String r14 = r14.getAlarmTime()
            java.lang.String r14 = r14.trim()
            java.lang.String r12 = com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper.FormateDateFromString(r12, r13, r14)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            int r11 = java.lang.Integer.parseInt(r11)
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = ""
            r12.append(r13)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper r13 = r1.dth
            java.lang.String r13 = "hh:mm a"
            java.lang.String r14 = "mm"
            java.util.List r15 = r6.getAlarmDetails()
            java.lang.Object r15 = r15.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r15
            java.lang.String r15 = r15.getAlarmTime()
            java.lang.String r15 = r15.trim()
            java.lang.String r13 = com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper.FormateDateFromString(r13, r14, r15)
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            int r12 = java.lang.Integer.parseInt(r12)
            java.util.List r13 = r6.getAlarmDetails()
            java.lang.Object r13 = r13.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r13 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r13
            java.lang.Integer r13 = r13.getSunday()
            int r13 = r13.intValue()
            if (r13 != r10) goto L_0x061e
            android.content.Context r13 = r1.mContext
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.util.List r15 = r6.getAlarmDetails()
            java.lang.Object r15 = r15.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r15
            java.lang.String r15 = r15.getAlarmSundayId()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            int r14 = java.lang.Integer.parseInt(r14)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleManualRecurringAlarm(r13, r10, r11, r12, r14)
        L_0x061e:
            java.util.List r13 = r6.getAlarmDetails()
            java.lang.Object r13 = r13.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r13 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r13
            java.lang.Integer r13 = r13.getMonday()
            int r13 = r13.intValue()
            if (r13 != r10) goto L_0x065a
            android.content.Context r13 = r1.mContext
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.util.List r15 = r6.getAlarmDetails()
            java.lang.Object r15 = r15.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r15
            java.lang.String r15 = r15.getAlarmMondayId()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            int r14 = java.lang.Integer.parseInt(r14)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleManualRecurringAlarm(r13, r9, r11, r12, r14)
        L_0x065a:
            java.util.List r9 = r6.getAlarmDetails()
            java.lang.Object r9 = r9.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r9 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r9
            java.lang.Integer r9 = r9.getTuesday()
            int r9 = r9.intValue()
            if (r9 != r10) goto L_0x0697
            android.content.Context r9 = r1.mContext
            r13 = 3
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.util.List r15 = r6.getAlarmDetails()
            java.lang.Object r15 = r15.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r15
            java.lang.String r15 = r15.getAlarmTuesdayId()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            int r14 = java.lang.Integer.parseInt(r14)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleManualRecurringAlarm(r9, r13, r11, r12, r14)
        L_0x0697:
            java.util.List r9 = r6.getAlarmDetails()
            java.lang.Object r9 = r9.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r9 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r9
            java.lang.Integer r9 = r9.getWednesday()
            int r9 = r9.intValue()
            if (r9 != r10) goto L_0x06d4
            android.content.Context r9 = r1.mContext
            r13 = 4
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.util.List r15 = r6.getAlarmDetails()
            java.lang.Object r15 = r15.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r15
            java.lang.String r15 = r15.getAlarmWednesdayId()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            int r14 = java.lang.Integer.parseInt(r14)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleManualRecurringAlarm(r9, r13, r11, r12, r14)
        L_0x06d4:
            java.util.List r9 = r6.getAlarmDetails()
            java.lang.Object r9 = r9.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r9 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r9
            java.lang.Integer r9 = r9.getThursday()
            int r9 = r9.intValue()
            if (r9 != r10) goto L_0x0711
            android.content.Context r9 = r1.mContext
            r13 = 5
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.util.List r15 = r6.getAlarmDetails()
            java.lang.Object r15 = r15.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r15
            java.lang.String r15 = r15.getAlarmThursdayId()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            int r14 = java.lang.Integer.parseInt(r14)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleManualRecurringAlarm(r9, r13, r11, r12, r14)
        L_0x0711:
            java.util.List r9 = r6.getAlarmDetails()
            java.lang.Object r9 = r9.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r9 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r9
            java.lang.Integer r9 = r9.getFriday()
            int r9 = r9.intValue()
            if (r9 != r10) goto L_0x074e
            android.content.Context r9 = r1.mContext
            r13 = 6
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.util.List r15 = r6.getAlarmDetails()
            java.lang.Object r15 = r15.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r15
            java.lang.String r15 = r15.getAlarmFridayId()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            int r14 = java.lang.Integer.parseInt(r14)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleManualRecurringAlarm(r9, r13, r11, r12, r14)
        L_0x074e:
            java.util.List r9 = r6.getAlarmDetails()
            java.lang.Object r9 = r9.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r9 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r9
            java.lang.Integer r9 = r9.getSaturday()
            int r9 = r9.intValue()
            if (r9 != r10) goto L_0x078b
            android.content.Context r9 = r1.mContext
            r10 = 7
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = ""
            r13.append(r14)
            java.util.List r14 = r6.getAlarmDetails()
            java.lang.Object r14 = r14.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r14 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r14
            java.lang.String r14 = r14.getAlarmSaturdayId()
            r13.append(r14)
            java.lang.String r13 = r13.toString()
            int r13 = java.lang.Integer.parseInt(r13)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleManualRecurringAlarm(r9, r10, r11, r12, r13)
        L_0x078b:
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r9 = r1.dh
            java.lang.String r10 = "tbl_alarm_details"
            java.lang.String r9 = r9.GET_LAST_ID(r10)
            java.util.List r10 = r6.getAlarmDetails()
            java.lang.Object r10 = r10.get(r7)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails r10 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails) r10
            java.util.List r10 = r10.getAlarmSubDetails()
            r11 = 0
        L_0x07a2:
            int r12 = r10.size()
            if (r11 >= r12) goto L_0x08af
            android.content.ContentValues r12 = new android.content.ContentValues
            r12.<init>()
            java.lang.String r13 = "AlarmTime"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.lang.Object r15 = r10.get(r11)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails) r15
            java.lang.String r15 = r15.getAlarmTime()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            r12.put(r13, r14)
            java.lang.String r13 = "AlarmId"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            java.lang.Object r15 = r10.get(r11)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails r15 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails) r15
            java.lang.String r15 = r15.getAlarmId()
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            r12.put(r13, r14)
            java.lang.String r13 = "SuperId"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = ""
            r14.append(r15)
            r14.append(r9)
            java.lang.String r14 = r14.toString()
            r12.put(r13, r14)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper r13 = r1.dh
            java.lang.String r14 = "tbl_alarm_sub_details"
            r13.INSERT((java.lang.String) r14, (android.content.ContentValues) r12)
            boolean r13 = r6.isManualReminderActive()
            if (r13 != 0) goto L_0x08a7
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = ""
            r13.append(r14)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper r14 = r1.dth
            java.lang.String r14 = "hh:mm a"
            java.lang.String r15 = "HH"
            java.lang.Object r16 = r10.get(r11)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails r16 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails) r16
            java.lang.String r16 = r16.getAlarmTime()
            java.lang.String r5 = r16.trim()
            java.lang.String r5 = com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper.FormateDateFromString(r14, r15, r5)
            r13.append(r5)
            java.lang.String r5 = r13.toString()
            int r5 = java.lang.Integer.parseInt(r5)
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = ""
            r13.append(r14)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper r14 = r1.dth
            java.lang.String r14 = "hh:mm a"
            java.lang.String r15 = "mm"
            java.lang.Object r16 = r10.get(r11)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails r16 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails) r16
            java.lang.String r16 = r16.getAlarmTime()
            r17 = r0
            java.lang.String r0 = r16.trim()
            java.lang.String r0 = com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper.FormateDateFromString(r14, r15, r0)
            r13.append(r0)
            java.lang.String r0 = r13.toString()
            int r0 = java.lang.Integer.parseInt(r0)
            java.util.Calendar r13 = java.util.Calendar.getInstance()
            r14 = 11
            r13.set(r14, r5)
            r14 = 12
            r13.set(r14, r0)
            r14 = 13
            r15 = 0
            r13.set(r14, r15)
            android.content.Context r14 = r1.mContext
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r18 = r0
            java.lang.String r0 = ""
            r15.append(r0)
            java.lang.Object r0 = r10.get(r11)
            com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails r0 = (com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails) r0
            java.lang.String r0 = r0.getAlarmId()
            r15.append(r0)
            java.lang.String r0 = r15.toString()
            int r0 = java.lang.Integer.parseInt(r0)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleAutoRecurringAlarm(r14, r13, r0)
            goto L_0x08a9
        L_0x08a7:
            r17 = r0
        L_0x08a9:
            int r11 = r11 + 1
            r0 = r17
            goto L_0x07a2
        L_0x08af:
            r17 = r0
            int r7 = r7 + 1
            goto L_0x039f
        L_0x08b5:
            r17 = r0
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.DAILY_WATER
            float r7 = r6.getTotalDrink()
            r0.savePreferences((java.lang.String) r5, (float) r7)
            float r0 = r6.getTotalDrink()
            com.trending.water.drinking.reminder.utils.URLFactory.DAILY_WATER_VALUE = r0
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.PERSON_WEIGHT
            java.lang.String r7 = r6.getTotalWeight()
            r0.savePreferences((java.lang.String) r5, (java.lang.String) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.PERSON_HEIGHT
            java.lang.String r7 = r6.getTotalHeight()
            r0.savePreferences((java.lang.String) r5, (java.lang.String) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.PERSON_WEIGHT_UNIT
            boolean r7 = r6.isKgUnit()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.PERSON_HEIGHT_UNIT
            boolean r7 = r6.isCMUnit()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.WATER_UNIT
            boolean r7 = r6.isMlUnit()
            if (r7 == 0) goto L_0x0901
            java.lang.String r7 = "ml"
            goto L_0x0903
        L_0x0901:
            java.lang.String r7 = "fl oz"
        L_0x0903:
            r0.savePreferences((java.lang.String) r5, (java.lang.String) r7)
            boolean r0 = r6.isMlUnit()
            if (r0 == 0) goto L_0x090f
            java.lang.String r0 = "ml"
            goto L_0x0911
        L_0x090f:
            java.lang.String r0 = "fl oz"
        L_0x0911:
            com.trending.water.drinking.reminder.utils.URLFactory.WATER_UNIT_VALUE = r0
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.REMINDER_OPTION
            java.lang.Integer r7 = r6.getReminderOption()
            int r7 = r7.intValue()
            r0.savePreferences((java.lang.String) r5, (int) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.REMINDER_SOUND
            java.lang.Integer r7 = r6.getReminderSound()
            int r7 = r7.intValue()
            r0.savePreferences((java.lang.String) r5, (int) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.DISABLE_NOTIFICATION
            boolean r7 = r6.isDisableNotifiction()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.IS_MANUAL_REMINDER
            boolean r7 = r6.isManualReminderActive()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.REMINDER_VIBRATE
            boolean r7 = r6.isReminderVibrate()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.USER_NAME
            java.lang.String r7 = r6.getUserName()
            r0.savePreferences((java.lang.String) r5, (java.lang.String) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.USER_GENDER
            boolean r7 = r6.getUserGender()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.DISABLE_SOUND_WHEN_ADD_WATER
            boolean r7 = r6.isDisableSound()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP
            boolean r7 = r6.isAutoBackup()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP_TYPE
            java.lang.Integer r7 = r6.getAutoBackupType()
            int r7 = r7.intValue()
            r0.savePreferences((java.lang.String) r5, (int) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP_ID
            java.lang.Integer r7 = r6.getAutoBackupId()
            int r7 = r7.intValue()
            r0.savePreferences((java.lang.String) r5, (int) r7)
            android.content.Context r0 = r1.mContext
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r5 = r1.ph
            java.lang.String r7 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP_ID
            int r5 = r5.getInt(r7)
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.cancelRecurringAlarm(r0, r5)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP
            boolean r0 = r0.getBoolean(r5)
            if (r0 == 0) goto L_0x09f8
            r19.setTime()
            long r7 = java.lang.System.currentTimeMillis()
            int r0 = (int) r7
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r5 = r1.ph
            java.lang.String r7 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP_ID
            r5.savePreferences((java.lang.String) r7, (int) r0)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r5 = r1.ph
            java.lang.String r7 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP_TYPE
            int r5 = r5.getInt(r7)
            if (r5 != 0) goto L_0x09d5
            android.content.Context r5 = r1.mContext
            java.util.Calendar r7 = r1.auto_backup_time
            r8 = 0
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleAutoBackupAlarm(r5, r7, r0, r8)
            goto L_0x09f8
        L_0x09d5:
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r5 = r1.ph
            java.lang.String r7 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP_TYPE
            int r5 = r5.getInt(r7)
            if (r5 != r10) goto L_0x09e7
            android.content.Context r5 = r1.mContext
            java.util.Calendar r7 = r1.auto_backup_time
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleAutoBackupAlarm(r5, r7, r0, r10)
            goto L_0x09f8
        L_0x09e7:
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r5 = r1.ph
            java.lang.String r7 = com.trending.water.drinking.reminder.utils.URLFactory.AUTO_BACK_UP_TYPE
            int r5 = r5.getInt(r7)
            if (r5 != r9) goto L_0x09f8
            android.content.Context r5 = r1.mContext
            java.util.Calendar r7 = r1.auto_backup_time
            com.trending.water.drinking.reminder.receiver.MyAlarmManager.scheduleAutoBackupAlarm(r5, r7, r0, r9)
        L_0x09f8:
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.IS_ACTIVE
            boolean r7 = r6.isActive()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.IS_BREATFEEDING
            boolean r7 = r6.isBreastfeeding()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.IS_PREGNANT
            boolean r7 = r6.isPregnant()
            r0.savePreferences((java.lang.String) r5, (boolean) r7)
            com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper r0 = r1.ph
            java.lang.String r5 = com.trending.water.drinking.reminder.utils.URLFactory.WEATHER_CONSITIONS
            java.lang.Integer r7 = r6.getWeatherConditions()
            int r7 = r7.intValue()
            r0.savePreferences((java.lang.String) r5, (int) r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trending.water.drinking.reminder.Screen_Backup_Restore.restoreAppData(java.lang.String):void");
    }

    public void selectFile() {
        startActivityForResult(getFileChooserIntent(), 1);
    }

    private Intent getFileChooserIntent() {
        String[] mimeTypes = {"text/plain"};
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (Build.VERSION.SDK_INT >= 19) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra("android.intent.extra.MIME_TYPES", mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr = mimeTypesStr + mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String getLastId;
        int i = 1;
        if (requestCode != 1) {
            int i2 = resultCode;
        } else if (resultCode == -1) {
            String path = FileUtils2.getPath(this.act, data.getData());
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(path));
                while (true) {
                    String readLine = br.readLine();
                    String line = readLine;
                    if (readLine == null) {
                        break;
                    }
                    text.append(line);
                    text.append("\n");
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String decrypted = text.toString();
            ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details");
            for (int k = 0; k < arr_data.size(); k++) {
                if (((String) arr_data.get(k).get("AlarmType")).equalsIgnoreCase("M")) {
                    MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("SundayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("MondayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("TuesdayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("WednesdayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("ThursdayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("FridayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("SaturdayAlarmId")));
                } else {
                    Database_Helper database_Helper = this.dh;
                    ArrayList<HashMap<String, String>> arr_data2 = database_Helper.getdata("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data.get(k).get("id")));
                    for (int j = 0; j < arr_data2.size(); j++) {
                        MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data2.get(j).get("AlarmId")));
                    }
                }
            }
            BackupRestore backupRestore = (BackupRestore) new Gson().fromJson(decrypted, BackupRestore.class);
            this.dh.REMOVE("tbl_container_details");
            for (int k2 = 0; k2 < backupRestore.getContainerDetails().size(); k2++) {
                ContentValues initialValues = new ContentValues();
                initialValues.put("ContainerID", "" + backupRestore.getContainerDetails().get(k2).getContainerID());
                initialValues.put("ContainerValue", "" + backupRestore.getContainerDetails().get(k2).getContainerValue());
                initialValues.put("ContainerValueOZ", "" + backupRestore.getContainerDetails().get(k2).getContainerValueOZ());
                initialValues.put("ContainerMeasure", "" + backupRestore.getContainerDetails().get(k2).getContainerMeasure());
                initialValues.put("IsOpen", "" + backupRestore.getContainerDetails().get(k2).getIsOpen());
                initialValues.put("IsCustom", "" + backupRestore.getContainerDetails().get(k2).getIsCustom());
                this.dh.INSERT("tbl_container_details", initialValues);
            }
            this.dh.REMOVE("tbl_drink_details");
            for (int k3 = 0; k3 < backupRestore.getDrinkDetails().size(); k3++) {
                ContentValues initialValues2 = new ContentValues();
                initialValues2.put("ContainerValue", "" + backupRestore.getDrinkDetails().get(k3).getContainerValue());
                initialValues2.put("ContainerValueOZ", "" + backupRestore.getDrinkDetails().get(k3).getContainerValueOZ());
                initialValues2.put("ContainerMeasure", "" + backupRestore.getDrinkDetails().get(k3).getContainerMeasure());
                initialValues2.put("DrinkDate", "" + backupRestore.getDrinkDetails().get(k3).getDrinkDate());
                initialValues2.put("DrinkTime", "" + backupRestore.getDrinkDetails().get(k3).getDrinkTime());
                initialValues2.put("DrinkDateTime", "" + backupRestore.getDrinkDetails().get(k3).getDrinkDateTime());
                initialValues2.put("TodayGoal", "" + backupRestore.getDrinkDetails().get(k3).getTodayGoal());
                initialValues2.put("TodayGoalOZ", "" + backupRestore.getDrinkDetails().get(k3).getTodayGoalOZ());
                this.dh.INSERT("tbl_drink_details", initialValues2);
            }
            this.dh.REMOVE("tbl_alarm_details");
            this.dh.REMOVE("tbl_alarm_sub_details");
            int k4 = 0;
            while (k4 < backupRestore.getAlarmDetails().size()) {
                ContentValues initialValues3 = new ContentValues();
                initialValues3.put("AlarmTime", "" + backupRestore.getAlarmDetails().get(k4).getAlarmTime());
                initialValues3.put("AlarmId", "" + backupRestore.getAlarmDetails().get(k4).getAlarmId());
                initialValues3.put("AlarmType", "" + backupRestore.getAlarmDetails().get(k4).getAlarmType());
                initialValues3.put("AlarmInterval", "" + backupRestore.getAlarmDetails().get(k4).getAlarmInterval());
                initialValues3.put("SundayAlarmId", backupRestore.getAlarmDetails().get(k4).getAlarmSundayId());
                initialValues3.put("MondayAlarmId", backupRestore.getAlarmDetails().get(k4).getAlarmMondayId());
                initialValues3.put("TuesdayAlarmId", backupRestore.getAlarmDetails().get(k4).getAlarmTuesdayId());
                initialValues3.put("WednesdayAlarmId", backupRestore.getAlarmDetails().get(k4).getAlarmWednesdayId());
                initialValues3.put("ThursdayAlarmId", backupRestore.getAlarmDetails().get(k4).getAlarmThursdayId());
                initialValues3.put("FridayAlarmId", backupRestore.getAlarmDetails().get(k4).getAlarmFridayId());
                initialValues3.put("SaturdayAlarmId", backupRestore.getAlarmDetails().get(k4).getAlarmSaturdayId());
                initialValues3.put("IsOff", backupRestore.getAlarmDetails().get(k4).getIsOff());
                initialValues3.put("Sunday", backupRestore.getAlarmDetails().get(k4).getSunday());
                initialValues3.put("Monday", backupRestore.getAlarmDetails().get(k4).getMonday());
                initialValues3.put("Tuesday", backupRestore.getAlarmDetails().get(k4).getTuesday());
                initialValues3.put("Wednesday", backupRestore.getAlarmDetails().get(k4).getWednesday());
                initialValues3.put("Thursday", backupRestore.getAlarmDetails().get(k4).getThursday());
                initialValues3.put("Friday", backupRestore.getAlarmDetails().get(k4).getFriday());
                initialValues3.put("Saturday", backupRestore.getAlarmDetails().get(k4).getSaturday());
                this.dh.INSERT("tbl_alarm_details", initialValues3);
                if (backupRestore.getAlarmDetails().get(k4).getAlarmType().equalsIgnoreCase("M") && backupRestore.isManualReminderActive()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    Date_Helper date_Helper = this.dth;
                    sb.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", backupRestore.getAlarmDetails().get(k4).getAlarmTime().trim()));
                    int hourOfDay = Integer.parseInt(sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    Date_Helper date_Helper2 = this.dth;
                    sb2.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", backupRestore.getAlarmDetails().get(k4).getAlarmTime().trim()));
                    int minute = Integer.parseInt(sb2.toString());
                    if (backupRestore.getAlarmDetails().get(k4).getSunday().intValue() == i) {
                        Context context = this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context, i, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k4).getAlarmSundayId()));
                    }
                    if (backupRestore.getAlarmDetails().get(k4).getMonday().intValue() == i) {
                        Context context2 = this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context2, 2, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k4).getAlarmMondayId()));
                    }
                    if (backupRestore.getAlarmDetails().get(k4).getTuesday().intValue() == i) {
                        Context context3 = this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context3, 3, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k4).getAlarmTuesdayId()));
                    }
                    if (backupRestore.getAlarmDetails().get(k4).getWednesday().intValue() == 1) {
                        Context context4 = this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context4, 4, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k4).getAlarmWednesdayId()));
                    }
                    if (backupRestore.getAlarmDetails().get(k4).getThursday().intValue() == 1) {
                        Context context5 = this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context5, 5, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k4).getAlarmThursdayId()));
                    }
                    if (backupRestore.getAlarmDetails().get(k4).getFriday().intValue() == 1) {
                        Context context6 = this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context6, 6, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k4).getAlarmFridayId()));
                    }
                    if (backupRestore.getAlarmDetails().get(k4).getSaturday().intValue() == 1) {
                        Context context7 = this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context7, 7, hourOfDay, minute, Integer.parseInt("" + backupRestore.getAlarmDetails().get(k4).getAlarmSaturdayId()));
                    }
                }
                String getLastId2 = this.dh.GET_LAST_ID("tbl_alarm_details");
                List<AlarmSubDetails> alarmSubDetailsList = backupRestore.getAlarmDetails().get(k4).getAlarmSubDetails();
                int j2 = 0;
                while (j2 < alarmSubDetailsList.size()) {
                    ContentValues initialValues22 = new ContentValues();
                    StringBuilder sb3 = new StringBuilder();
                    String decrypted2 = decrypted;
                    sb3.append("");
                    sb3.append(alarmSubDetailsList.get(j2).getAlarmTime());
                    initialValues22.put("AlarmTime", sb3.toString());
                    initialValues22.put("AlarmId", "" + alarmSubDetailsList.get(j2).getAlarmId());
                    initialValues22.put("SuperId", "" + getLastId2);
                    this.dh.INSERT("tbl_alarm_sub_details", initialValues22);
                    if (!backupRestore.isManualReminderActive()) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("");
                        Date_Helper date_Helper3 = this.dth;
                        getLastId = getLastId2;
                        sb4.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", alarmSubDetailsList.get(j2).getAlarmTime().trim()));
                        int hourOfDay2 = Integer.parseInt(sb4.toString());
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("");
                        Date_Helper date_Helper4 = this.dth;
                        sb5.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", alarmSubDetailsList.get(j2).getAlarmTime().trim()));
                        int minute2 = Integer.parseInt(sb5.toString());
                        Calendar updateTime = Calendar.getInstance();
                        updateTime.set(11, hourOfDay2);
                        updateTime.set(12, minute2);
                        updateTime.set(13, 0);
                        Context context8 = this.mContext;
                        StringBuilder sb6 = new StringBuilder();
                        int i3 = hourOfDay2;
                        sb6.append("");
                        sb6.append(alarmSubDetailsList.get(j2).getAlarmId());
                        MyAlarmManager.scheduleAutoRecurringAlarm(context8, updateTime, Integer.parseInt(sb6.toString()));
                    } else {
                        getLastId = getLastId2;
                    }
                    j2++;
                    decrypted = decrypted2;
                    getLastId2 = getLastId;
                    int i4 = requestCode;
                }
                k4++;
                i = 1;
                int i5 = requestCode;
            }
            this.ph.savePreferences(URLFactory.DAILY_WATER, backupRestore.getTotalDrink());
            URLFactory.DAILY_WATER_VALUE = backupRestore.getTotalDrink();
            this.ph.savePreferences(URLFactory.PERSON_WEIGHT, backupRestore.getTotalWeight());
            this.ph.savePreferences(URLFactory.PERSON_HEIGHT, backupRestore.getTotalHeight());
            this.ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT, backupRestore.isKgUnit());
            this.ph.savePreferences(URLFactory.PERSON_HEIGHT_UNIT, backupRestore.isCMUnit());
            this.ph.savePreferences(URLFactory.WATER_UNIT, backupRestore.isMlUnit() ? "ml" : "fl oz");
            URLFactory.WATER_UNIT_VALUE = backupRestore.isMlUnit() ? "ml" : "fl oz";
            this.ph.savePreferences(URLFactory.REMINDER_OPTION, backupRestore.getReminderOption().intValue());
            this.ph.savePreferences(URLFactory.REMINDER_SOUND, backupRestore.getReminderSound().intValue());
            this.ph.savePreferences(URLFactory.DISABLE_NOTIFICATION, backupRestore.isDisableNotifiction());
            this.ph.savePreferences(URLFactory.IS_MANUAL_REMINDER, backupRestore.isManualReminderActive());
            this.ph.savePreferences(URLFactory.REMINDER_VIBRATE, backupRestore.isReminderVibrate());
            this.ph.savePreferences(URLFactory.USER_NAME, backupRestore.getUserName());
            this.ph.savePreferences(URLFactory.USER_GENDER, backupRestore.getUserGender());
            this.ph.savePreferences(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER, backupRestore.isDisableSound());
            this.ph.savePreferences(URLFactory.AUTO_BACK_UP, backupRestore.isAutoBackup());
            this.ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE, backupRestore.getAutoBackupType().intValue());
            this.ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, backupRestore.getAutoBackupId().intValue());
            MyAlarmManager.cancelRecurringAlarm(this.mContext, this.ph.getInt(URLFactory.AUTO_BACK_UP_ID));
            if (this.ph.getBoolean(URLFactory.AUTO_BACK_UP)) {
                setTime();
                int _id = (int) System.currentTimeMillis();
                this.ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, _id);
                if (this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 0) {
                    MyAlarmManager.scheduleAutoBackupAlarm(this.mContext, this.auto_backup_time, _id, 0);
                } else if (this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 1) {
                    MyAlarmManager.scheduleAutoBackupAlarm(this.mContext, this.auto_backup_time, _id, 1);
                } else if (this.ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) == 2) {
                    MyAlarmManager.scheduleAutoBackupAlarm(this.mContext, this.auto_backup_time, _id, 2);
                }
            }
            this.ph.savePreferences(URLFactory.IS_ACTIVE, backupRestore.isActive());
            this.ph.savePreferences(URLFactory.IS_BREATFEEDING, backupRestore.isBreastfeeding());
            this.ph.savePreferences(URLFactory.IS_PREGNANT, backupRestore.isPregnant());
            this.ph.savePreferences(URLFactory.WEATHER_CONSITIONS, backupRestore.getWeatherConditions().intValue());
            this.ah.customAlert(this.sh.get_string(R.string.str_restore_msg));
            refreshApp();
        }
    }

    public void refreshApp() {
        this.intent = new Intent(this.act, Screen_Dashboard.class);
        this.intent.setFlags(268468224);
        overridePendingTransition(0, 0);
        startActivity(this.intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 3 || grantResults.length <= 0 || grantResults[0] != 0) {
            return;
        }
        if (this.executeBackup) {
            new backupFromDBData().execute(new String[0]);
        } else {
            restore_data();
        }
    }

    private class backupFromDBData extends AsyncTask<String, String, String> {
        private backupFromDBData() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            Screen_Backup_Restore.this.ah.Show_Custom_Progress_Dialog();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... str) {
            try {
                Screen_Backup_Restore.this.backup_data();
                return "";
            } catch (Exception e) {
                Log.d("Response : ", "" + e.getMessage());
                return "";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            Screen_Backup_Restore.this.ah.Close_Custom_Progress_Dialog();
            Screen_Backup_Restore.this.ah.customAlert(Screen_Backup_Restore.this.sh.get_string(R.string.str_backup_msg));
        }
    }

    private class restoreFromBackupData extends AsyncTask<String, String, String> {
        String path;

        public restoreFromBackupData(String path2) {
            this.path = path2;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            Screen_Backup_Restore.this.ah.Show_Custom_Progress_Dialog();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... str) {
            try {
                Screen_Backup_Restore.this.restoreAppData(this.path);
                return "";
            } catch (Exception e) {
                Log.d("Response : ", "" + e.getMessage());
                return "";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            Screen_Backup_Restore.this.ah.Close_Custom_Progress_Dialog();
            Screen_Backup_Restore.this.ah.customAlert(Screen_Backup_Restore.this.sh.get_string(R.string.str_restore_msg));
            Screen_Backup_Restore.this.refreshApp();
        }
    }
}
