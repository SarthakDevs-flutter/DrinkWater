package com.trending.water.drinking.reminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.trending.water.drinking.reminder.adapter.FileAdapter;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.databinding.ScreenBackupRestoreBinding;
import com.trending.water.drinking.reminder.model.BackUpFileModel;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmDetails;
import com.trending.water.drinking.reminder.model.backuprestore.AlarmSubDetails;
import com.trending.water.drinking.reminder.model.backuprestore.BackupRestore;
import com.trending.water.drinking.reminder.model.backuprestore.ContainerDetails;
import com.trending.water.drinking.reminder.model.backuprestore.DrinkDetails;
import com.trending.water.drinking.reminder.receiver.MyAlarmManager;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
/* ... rest of imports ... */

public class Screen_Backup_Restore extends MasterBaseActivity<ScreenBackupRestoreBinding> {

    private static final String TAG = "Screen_Backup_Restore";
    private static final int REQ_PERMISSIONS = 3;
    private static final int REQ_SELECT_FILE = 1;
    private final Calendar autoBackupTime = Calendar.getInstance();
    private final List<BackUpFileModel> backupFileList = new ArrayList<>();
    private boolean isPendingBackup = true;
    private FileAdapter fileAdapter;

    @Override
    protected ScreenBackupRestoreBinding inflateBinding(LayoutInflater inflater) {
        return ScreenBackupRestoreBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.str_green_card));
        }

        initView();
        setupListeners();
    }

    private void initView() {
        binding.include1.lblToolbarTitle.setText(stringHelper.capitalizeAll(stringHelper.getString(R.string.str_backup_and_restore)));
        binding.include1.rightIconBlock.setVisibility(View.GONE);

        binding.lblBackup.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_backup)));
        binding.lblRestore.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_restore)));
        binding.lblClear.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_clear_all_data)));
        binding.lblAutoBackup.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_auto_backup)));

        binding.rdoDaily.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_daily)));
        binding.rdoWeekly.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_weekly)));
        binding.rdoMonthly.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_monthly)));

        boolean isAutoBackupOn = preferencesHelper.getBoolean(URLFactory.KEY_AUTO_BACKUP, false);
        binding.switchAutoBackup.setChecked(isAutoBackupOn);
        binding.autoBackupOptionBlock.setVisibility(isAutoBackupOn ? View.VISIBLE : View.GONE);

        int type = preferencesHelper.getInt(URLFactory.KEY_AUTO_BACKUP_TYPE, 0);
        binding.rdoDaily.setChecked(type == 0);
        binding.rdoWeekly.setChecked(type == 1);
        binding.rdoMonthly.setChecked(type == 2);

        setupAutoBackupTime();
    }

    private void setupAutoBackupTime() {
        autoBackupTime.set(Calendar.HOUR_OF_DAY, 9);
        autoBackupTime.set(Calendar.MINUTE, 0);
        autoBackupTime.set(Calendar.SECOND, 0);
        autoBackupTime.set(Calendar.MILLISECOND, 0);
    }

    private void setupListeners() {
        binding.include1.leftIconBlock.setOnClickListener(v -> finish());

        binding.switchAutoBackup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.autoBackupOptionBlock.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            preferencesHelper.savePreferences(URLFactory.KEY_AUTO_BACKUP, isChecked);

            if (isChecked) {
                scheduleAutoBackup();
            } else {
                MyAlarmManager.cancelRecurringAlarm(mContext, preferencesHelper.getInt(URLFactory.KEY_AUTO_BACKUP_ID, 0));
            }
        });

        binding.rdoDaily.setOnClickListener(v -> updateBackupType(0));
        binding.rdoWeekly.setOnClickListener(v -> updateBackupType(1));
        binding.rdoMonthly.setOnClickListener(v -> updateBackupType(2));

        binding.backupBlock.setOnClickListener(v -> {
            isPendingBackup = true;
            validateAndRunAction();
        });

        binding.restoreBlock.setOnClickListener(v -> {
            isPendingBackup = false;
            validateAndRunAction();
        });

        binding.clearBlock.setOnClickListener(v -> confirmClearBackups());
    }

    private void updateBackupType(int type) {
        MyAlarmManager.cancelRecurringAlarm(mContext, preferencesHelper.getInt(URLFactory.KEY_AUTO_BACKUP_ID, 0));
        preferencesHelper.savePreferences(URLFactory.KEY_AUTO_BACKUP_TYPE, type);
        scheduleAutoBackup();
    }

    private void scheduleAutoBackup() {
        setupAutoBackupTime();
        int id = (int) System.currentTimeMillis();
        preferencesHelper.savePreferences(URLFactory.KEY_AUTO_BACKUP_ID, id);
        int type = preferencesHelper.getInt(URLFactory.KEY_AUTO_BACKUP_TYPE, 0);
        MyAlarmManager.scheduleAutoBackupAlarm(mContext, autoBackupTime, id, type);
    }

    private void validateAndRunAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0 ||
                    ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, REQ_PERMISSIONS);
                return;
            }
        }

        if (isPendingBackup) {
            new BackupDataTask().execute();
        } else {
            showRestorePicker();
        }
    }

    private void confirmClearBackups() {
        new AlertDialog.Builder(mActivity)
                .setMessage(stringHelper.getString(R.string.str_clear_all_data_confirmation_messge))
                .setPositiveButton(R.string.str_yes, (dialog, which) -> {
                    File dir = new File(Environment.getExternalStorageDirectory(), URLFactory.APP_DIRECTORY_NAME);
                    if (dir.exists() && dir.isDirectory()) {
                        File[] files = dir.listFiles();
                        if (files != null) {
                            for (File f : files) f.delete();
                        }
                    }
                    alertHelper.customAlert(stringHelper.getString(R.string.str_successfully_clear_data));
                })
                .setNegativeButton(R.string.str_no, null)
                .show();
    }

    private void showRestorePicker() {
        loadBackupFiles();
        if (backupFileList.isEmpty()) {
            alertHelper.customAlert(stringHelper.getString(R.string.str_no_backup_found));
            return;
        }

        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_backup_pick, null);
        ((AppCompatTextView) view.findViewById(R.id.btn_text)).setText(stringHelper.getString(R.string.str_restore));

        RecyclerView rv = view.findViewById(R.id.soundRecyclerView);
        fileAdapter = new FileAdapter(mActivity, backupFileList, (item, pos) -> {
            for (BackUpFileModel m : backupFileList) m.setSelected(false);
            backupFileList.get(pos).setSelected(true);
            fileAdapter.notifyDataSetChanged();
        });
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.setAdapter(fileAdapter);

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            new AlertDialog.Builder(mActivity)
                    .setTitle(stringHelper.getString(R.string.str_restore_all_data_confirmation_messge))
                    .setPositiveButton(R.string.str_yes, (d, w) -> {
                        int pos = -1;
                        for (int i = 0; i < backupFileList.size(); i++) {
                            if (backupFileList.get(i).isSelected()) {
                                pos = i;
                                break;
                            }
                        }
                        if (pos >= 0) {
                            new RestoreDataTask(backupFileList.get(pos).getPath()).execute();
                        }
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.str_no, null)
                    .show();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void loadBackupFiles() {
        backupFileList.clear();
        File dir = new File(Environment.getExternalStorageDirectory(), URLFactory.APP_DIRECTORY_NAME);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(f -> f.getName().endsWith(".txt"));
            if (files != null && files.length > 0) {
                Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                for (int i = 0; i < files.length; i++) {
                    BackUpFileModel m = new BackUpFileModel();
                    m.setName(files[i].getName());
                    m.setPath(files[i].getPath());
                    m.setLastModify(files[i].lastModified());
                    m.setSelected(i == 0);
                    backupFileList.add(m);
                }
            }
        }
    }

    private List<ContainerDetails> fetchContainerData() {
        List<ContainerDetails> list = new ArrayList<>();
        List<HashMap<String, String>> rows = databaseHelper.getData("tbl_container_details");
        for (HashMap<String, String> row : rows) {
            ContainerDetails d = new ContainerDetails();
            d.setContainerId(row.get("ContainerID"));
            d.setContainerMeasure(row.get("ContainerMeasure"));
            d.setContainerValue(row.get("ContainerValue"));
            d.setContainerValueOZ(row.get("ContainerValueOZ"));
            d.setIsOpen(row.get("IsOpen"));
            d.setId(row.get("id"));
            d.setIsCustom(row.get("IsCustom"));
            list.add(d);
        }
        return list;
    }

    private List<DrinkDetails> fetchDrinkData() {
        List<DrinkDetails> list = new ArrayList<>();
        List<HashMap<String, String>> rows = databaseHelper.getData("tbl_drink_details");
        for (HashMap<String, String> row : rows) {
            DrinkDetails d = new DrinkDetails();
            d.setDrinkDateTime(row.get("DrinkDateTime"));
            d.setDrinkDate(row.get("DrinkDate"));
            d.setDrinkTime(row.get("DrinkTime"));
            d.setContainerMeasure(row.get("ContainerMeasure"));
            d.setContainerValue(row.get("ContainerValue"));
            d.setContainerValueOZ(row.get("ContainerValueOZ"));
            d.setId(row.get("id"));
            d.setTodayGoal(row.get("TodayGoal"));
            d.setTodayGoalOZ(row.get("TodayGoalOZ"));
            list.add(d);
        }
        return list;
    }

    private List<AlarmDetails> fetchAlarmData() {
        List<AlarmDetails> list = new ArrayList<>();
        List<HashMap<String, String>> rows = databaseHelper.getData("tbl_alarm_details");
        for (HashMap<String, String> row : rows) {
            AlarmDetails d = new AlarmDetails();
            d.setAlarmId(row.get("AlarmId"));
            d.setAlarmInterval(row.get("AlarmInterval"));
            d.setAlarmTime(row.get("AlarmTime"));
            d.setAlarmType(row.get("AlarmType"));
            d.setId(row.get("id"));
            d.setAlarmSundayId(row.get("SundayAlarmId"));
            d.setAlarmMondayId(row.get("MondayAlarmId"));
            d.setAlarmTuesdayId(row.get("TuesdayAlarmId"));
            d.setAlarmWednesdayId(row.get("WednesdayAlarmId"));
            d.setAlarmThursdayId(row.get("ThursdayAlarmId"));
            d.setAlarmFridayId(row.get("FridayAlarmId"));
            d.setAlarmSaturdayId(row.get("SaturdayAlarmId"));
            d.setIsOff(Integer.parseInt(row.get("IsOff")));
            d.setSunday(Integer.parseInt(row.get("Sunday")));
            d.setMonday(Integer.parseInt(row.get("Monday")));
            d.setTuesday(Integer.parseInt(row.get("Tuesday")));
            d.setWednesday(Integer.parseInt(row.get("Wednesday")));
            d.setThursday(Integer.parseInt(row.get("Thursday")));
            d.setFriday(Integer.parseInt(row.get("Friday")));
            d.setSaturday(Integer.parseInt(row.get("Saturday")));

            List<AlarmSubDetails> subList = new ArrayList<>();
            List<HashMap<String, String>> subRows = databaseHelper.getData("tbl_alarm_sub_details", "SuperId=" + row.get("id"));
            for (HashMap<String, String> subRow : subRows) {
                AlarmSubDetails sd = new AlarmSubDetails();
                sd.setAlarmId(subRow.get("AlarmId"));
                sd.setAlarmTime(subRow.get("AlarmTime"));
                sd.setId(subRow.get("id"));
                sd.setSuperId(subRow.get("SuperId"));
                subList.add(sd);
            }
            d.setAlarmSubDetails(subList);
            list.add(d);
        }
        return list;
    }

    private boolean writeToFile(String content) {
        File dir = new File(Environment.getExternalStorageDirectory(), URLFactory.APP_DIRECTORY_NAME);
        if (!dir.exists()) dir.mkdirs();

        String stamp = dateHelper.getCurrentDate("dd-MMM-yyyy hh:mm:ss a");
        File file = new File(dir, "Backup_" + stamp + ".txt");
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.append(content);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "File write error", e);
            return false;
        }
    }

    private void cancelExistingAlarms() {
        List<HashMap<String, String>> rows = databaseHelper.getData("tbl_alarm_details");
        for (HashMap<String, String> row : rows) {
            if ("M".equalsIgnoreCase(row.get("AlarmType"))) {
                cancelManual(row);
            } else {
                List<HashMap<String, String>> subs = databaseHelper.getData("tbl_alarm_sub_details", "SuperId=" + row.get("id"));
                for (HashMap<String, String> sub : subs) {
                    MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(sub.get("AlarmId")));
                }
            }
        }
    }

    private void cancelManual(HashMap<String, String> row) {
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("SundayAlarmId")));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("MondayAlarmId")));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("TuesdayAlarmId")));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("WednesdayAlarmId")));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("ThursdayAlarmId")));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("FridayAlarmId")));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("SaturdayAlarmId")));
    }

    private void applyPreferences(BackupRestore data) {
        preferencesHelper.savePreferences(URLFactory.KEY_DAILY_WATER_GOAL, data.getTotalDrink());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT, data.getTotalWeight());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT, data.getTotalHeight());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT_UNIT, data.isKgUnit());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT_UNIT, data.isCMUnit());
        preferencesHelper.savePreferences(URLFactory.KEY_WATER_UNIT, data.isMlUnit() ? "ml" : "fl oz");
        preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_OPTION, data.getReminderOption());
        preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_SOUND, data.getReminderSound());
        preferencesHelper.savePreferences(URLFactory.KEY_DISABLE_NOTIFICATION, data.isDisableNotification());
        preferencesHelper.savePreferences(URLFactory.KEY_IS_MANUAL_REMINDER, data.isManualReminderActive());
        preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_VIBRATE, data.isReminderVibrate());
        preferencesHelper.savePreferences(URLFactory.KEY_USER_NAME, data.getUserName());
        preferencesHelper.savePreferences(URLFactory.KEY_USER_GENDER, data.getUserGender());
        preferencesHelper.savePreferences(URLFactory.KEY_DISABLE_SOUND_ON_ADD, data.isDisableSound());
        preferencesHelper.savePreferences(URLFactory.KEY_AUTO_BACKUP, data.isAutoBackup());
        preferencesHelper.savePreferences(URLFactory.KEY_AUTO_BACKUP_TYPE, data.getAutoBackupType());
        preferencesHelper.savePreferences(URLFactory.KEY_AUTO_BACKUP_ID, data.getAutoBackupId());
        preferencesHelper.savePreferences(URLFactory.KEY_IS_ACTIVE, data.isActive());
        preferencesHelper.savePreferences(URLFactory.KEY_IS_BREASTFEEDING, data.isBreastfeeding());
        preferencesHelper.savePreferences(URLFactory.KEY_IS_PREGNANT, data.isPregnant());
        preferencesHelper.savePreferences(URLFactory.KEY_WEATHER_CONDITIONS, data.getWeatherConditions());

        URLFactory.dailyWaterValue = data.getTotalDrink();
        URLFactory.waterUnitValue = data.isMlUnit() ? "ml" : "fl oz";
    }

    private void rescheduleAlarms(BackupRestore data) {
        if (data.isManualReminderActive()) {
            for (AlarmDetails d : data.getAlarmDetails()) {
                if ("M".equalsIgnoreCase(d.getAlarmType())) {
                    int h = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", d.getAlarmTime().trim()));
                    int m = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", d.getAlarmTime().trim()));
                    if (d.getSunday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, 1, h, m, Integer.parseInt(d.getAlarmSundayId()));
                    if (d.getMonday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, 2, h, m, Integer.parseInt(d.getAlarmMondayId()));
                    if (d.getTuesday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, 3, h, m, Integer.parseInt(d.getAlarmTuesdayId()));
                    if (d.getWednesday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, 4, h, m, Integer.parseInt(d.getAlarmWednesdayId()));
                    if (d.getThursday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, 5, h, m, Integer.parseInt(d.getAlarmThursdayId()));
                    if (d.getFriday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, 6, h, m, Integer.parseInt(d.getAlarmFridayId()));
                    if (d.getSaturday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, 7, h, m, Integer.parseInt(d.getAlarmSaturdayId()));
                }
            }
        } else {
            for (AlarmDetails d : data.getAlarmDetails()) {
                for (AlarmSubDetails sd : d.getAlarmSubDetails()) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", sd.getAlarmTime().trim())));
                    cal.set(Calendar.MINUTE, Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", sd.getAlarmTime().trim())));
                    cal.set(Calendar.SECOND, 0);
                    MyAlarmManager.scheduleAutoRecurringAlarm(mContext, cal, Integer.parseInt(sd.getAlarmId()));
                }
            }
        }

        if (data.isAutoBackup()) {
            scheduleAutoBackup();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                validateAndRunAction();
            } else {
                alertHelper.customAlert(stringHelper.getString(R.string.str_permission_denied));
            }
        }
    }

    private class BackupDataTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                BackupRestore data = new BackupRestore();
                data.setContainerDetails(fetchContainerData());
                data.setDrinkDetails(fetchDrinkData());
                data.setAlarmDetails(fetchAlarmData());

                // Prefs
                data.setTotalDrink(preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 0.0f));
                data.setTotalWeight(preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, ""));
                data.setTotalHeight(preferencesHelper.getString(URLFactory.KEY_PERSON_HEIGHT, ""));
                data.setCMUnit(preferencesHelper.getBoolean(URLFactory.KEY_PERSON_HEIGHT_UNIT, true));
                data.setKgUnit(preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true));
                data.setMlUnit(preferencesHelper.getString(URLFactory.KEY_WATER_UNIT, "ml").equalsIgnoreCase("ml"));
                data.setReminderOption(preferencesHelper.getInt(URLFactory.KEY_REMINDER_OPTION, 0));
                data.setReminderSound(preferencesHelper.getInt(URLFactory.KEY_REMINDER_SOUND, 0));
                data.setDisableNotification(preferencesHelper.getBoolean(URLFactory.KEY_DISABLE_NOTIFICATION, false));
                data.setManualReminderActive(preferencesHelper.getBoolean(URLFactory.KEY_IS_MANUAL_REMINDER, false));
                data.setReminderVibrate(preferencesHelper.getBoolean(URLFactory.KEY_REMINDER_VIBRATE, false));
                data.setUserName(preferencesHelper.getString(URLFactory.KEY_USER_NAME, ""));
                data.setUserGender(preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, true));
                data.setDisableSound(preferencesHelper.getBoolean(URLFactory.KEY_DISABLE_SOUND_ON_ADD, false));
                data.setAutoBackup(preferencesHelper.getBoolean(URLFactory.KEY_AUTO_BACKUP, false));
                data.setAutoBackupType(preferencesHelper.getInt(URLFactory.KEY_AUTO_BACKUP_TYPE, 0));
                data.setAutoBackupId(preferencesHelper.getInt(URLFactory.KEY_AUTO_BACKUP_ID, 0));
                data.setActive(preferencesHelper.getBoolean(URLFactory.KEY_IS_ACTIVE, false));
                data.setBreastfeeding(preferencesHelper.getBoolean(URLFactory.KEY_IS_BREASTFEEDING, false));
                data.setPregnant(preferencesHelper.getBoolean(URLFactory.KEY_IS_PREGNANT, false));
                data.setWeatherConditions(preferencesHelper.getInt(URLFactory.KEY_WEATHER_CONDITIONS, 0));

                String json = new Gson().toJson(data);
                return writeToFile(json);
            } catch (Exception e) {
                Log.e(TAG, "Backup failed", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success)
                alertHelper.customAlert(stringHelper.getString(R.string.str_successfully_backup));
            else alertHelper.customAlert(stringHelper.getString(R.string.str_error_occurred));
        }
    }

    private class RestoreDataTask extends AsyncTask<Void, Void, Boolean> {
        private final String filePath;

        RestoreDataTask(String path) {
            this.filePath = path;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line).append("\n");
                }

                BackupRestore data = new Gson().fromJson(sb.toString(), BackupRestore.class);
                if (data == null) return false;

                cancelExistingAlarms();

                databaseHelper.remove("tbl_container_details");
                for (ContainerDetails d : data.getContainerDetails()) {
                    ContentValues cv = new ContentValues();
                    cv.put("ContainerID", d.getContainerId());
                    cv.put("ContainerValue", d.getContainerValue());
                    cv.put("ContainerValueOZ", d.getContainerValueOZ());
                    cv.put("ContainerMeasure", d.getContainerMeasure());
                    cv.put("IsOpen", d.getIsOpen());
                    cv.put("IsCustom", d.getIsCustom());
                    databaseHelper.insert("tbl_container_details", cv);
                }

                databaseHelper.remove("tbl_drink_details");
                for (DrinkDetails d : data.getDrinkDetails()) {
                    ContentValues cv = new ContentValues();
                    cv.put("ContainerValue", d.getContainerValue());
                    cv.put("ContainerValueOZ", d.getContainerValueOZ());
                    cv.put("ContainerMeasure", d.getContainerMeasure());
                    cv.put("DrinkDate", d.getDrinkDate());
                    cv.put("DrinkTime", d.getDrinkTime());
                    cv.put("DrinkDateTime", d.getDrinkDateTime());
                    cv.put("TodayGoal", d.getTodayGoal());
                    cv.put("TodayGoalOZ", d.getTodayGoalOZ());
                    databaseHelper.insert("tbl_drink_details", cv);
                }

                databaseHelper.remove("tbl_alarm_details");
                databaseHelper.remove("tbl_alarm_sub_details");
                for (AlarmDetails d : data.getAlarmDetails()) {
                    ContentValues cv = new ContentValues();
                    cv.put("AlarmTime", d.getAlarmTime());
                    cv.put("AlarmId", d.getAlarmId());
                    cv.put("AlarmType", d.getAlarmType());
                    cv.put("AlarmInterval", d.getAlarmInterval());
                    cv.put("SundayAlarmId", d.getAlarmSundayId());
                    cv.put("MondayAlarmId", d.getAlarmMondayId());
                    cv.put("TuesdayAlarmId", d.getAlarmTuesdayId());
                    cv.put("WednesdayAlarmId", d.getAlarmWednesdayId());
                    cv.put("ThursdayAlarmId", d.getAlarmThursdayId());
                    cv.put("FridayAlarmId", d.getAlarmFridayId());
                    cv.put("SaturdayAlarmId", d.getAlarmSaturdayId());
                    cv.put("IsOff", d.getIsOff());
                    cv.put("Sunday", d.getSunday());
                    cv.put("Monday", d.getMonday());
                    cv.put("Tuesday", d.getTuesday());
                    cv.put("Wednesday", d.getWednesday());
                    cv.put("Thursday", d.getThursday());
                    cv.put("Friday", d.getFriday());
                    cv.put("Saturday", d.getSaturday());
                    databaseHelper.insert("tbl_alarm_details", cv);
                    String lastId = databaseHelper.getLastId("tbl_alarm_details");

                    for (AlarmSubDetails sd : d.getAlarmSubDetails()) {
                        ContentValues scv = new ContentValues();
                        scv.put("AlarmTime", sd.getAlarmTime());
                        scv.put("AlarmId", sd.getAlarmId());
                        scv.put("SuperId", lastId);
                        databaseHelper.insert("tbl_alarm_sub_details", scv);
                    }
                }

                applyPreferences(data);
                rescheduleAlarms(data);

                return true;
            } catch (Exception e) {
                Log.e(TAG, "Restore failed", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_successfully_restore));
                initView();
            } else {
                alertHelper.customAlert(stringHelper.getString(R.string.str_error_occurred));
            }
        }
    }
}
