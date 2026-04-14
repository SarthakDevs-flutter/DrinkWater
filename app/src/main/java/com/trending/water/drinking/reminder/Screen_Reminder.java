package com.trending.water.drinking.reminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.trending.water.drinking.reminder.adapter.AlarmAdapter;
import com.trending.water.drinking.reminder.adapter.IntervalAdapter;
import com.trending.water.drinking.reminder.adapter.SoundAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DateHelper;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.model.AlarmModel;
import com.trending.water.drinking.reminder.model.IntervalModel;
import com.trending.water.drinking.reminder.model.SoundModel;
import com.trending.water.drinking.reminder.receiver.MyAlarmManager;
import com.trending.water.drinking.reminder.utils.URLFactory;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Screen_Reminder extends MasterBaseActivity {

    private static final String TAG = "Screen_Reminder";

    private RecyclerView alarmRecyclerView;
    private AppCompatTextView lblNoRecordFound;
    private AppCompatTextView lblWakeupTime;
    private AppCompatTextView lblBedTime;
    private AppCompatTextView lblInterval;
    private AppCompatTextView lblWakeupTitle;
    private AppCompatTextView lblBedTitle;

    private RadioButton rdoAutoMode;
    private RadioButton rdoManualMode;
    private RadioButton rdoReminderOn;
    private RadioButton rdoReminderOff;
    private RadioButton rdoReminderSilent;

    private RelativeLayout autoReminderBlock;
    private RelativeLayout manualReminderBlock;
    private RelativeLayout btnAddReminder;
    private RelativeLayout btnSaveAutoReminder;
    
    private LinearLayout soundBlock;
    private SwitchCompat switchVibrate;
    private LinearLayout leftIconBlock;
    private LinearLayout rightIconBlock;

    private AlarmAdapter alarmAdapter;
    private final ArrayList<AlarmModel> alarmList = new ArrayList<>();
    private final List<IntervalModel> intervalList = new ArrayList<>();
    private final List<SoundModel> soundList = new ArrayList<>();

    private BottomSheetDialog soundSheetDialog;
    private IntervalAdapter intervalAdapter;
    private SoundAdapter soundAdapter;

    private int fromHour = 8;
    private int fromMinute = 0;
    private int toHour = 22;
    private int toMinute = 0;
    private int currentInterval = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_reminder);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.str_green_card));
        }

        findViewByIds();
        initView();
        setupListeners();
    }

    private void findViewByIds() {
        rightIconBlock = findViewById(R.id.right_icon_block);
        leftIconBlock = findViewById(R.id.left_icon_block);
        alarmRecyclerView = findViewById(R.id.alarmRecyclerView);
        lblNoRecordFound = findViewById(R.id.lbl_no_record_found);
        
        rdoReminderOn = findViewById(R.id.rdo_auto);
        rdoReminderOff = findViewById(R.id.rdo_off);
        rdoReminderSilent = findViewById(R.id.rdo_silent);
        
        soundBlock = findViewById(R.id.sound_block);
        switchVibrate = findViewById(R.id.switch_vibrate);
        
        lblWakeupTime = findViewById(R.id.lbl_wakeup_time);
        lblBedTime = findViewById(R.id.lbl_bed_time);
        lblInterval = findViewById(R.id.lbl_interval);
        
        manualReminderBlock = findViewById(R.id.manual_reminder_block);
        autoReminderBlock = findViewById(R.id.auto_reminder_block);
        
        rdoAutoMode = findViewById(R.id.rdo_auto_alarm);
        rdoManualMode = findViewById(R.id.rdo_manual_alarm);
        
        btnAddReminder = findViewById(R.id.add_reminder);
        btnSaveAutoReminder = findViewById(R.id.save_reminder);

        lblWakeupTitle = findViewById(R.id.lblwt);
        lblBedTitle = findViewById(R.id.lblbt);
    }

    private void initView() {
        // Toolbar and Titles
        lblWakeupTitle.setText(setFirstLetterLower(stringHelper.getString(R.string.str_wakeup_time)));
        lblBedTitle.setText(setFirstLetterLower(stringHelper.getString(R.string.str_bed_time)));

        loadAutoSettingsFromDb();
        updateRemindersModeView();
        updateVibrateToggle();
        updateReminderOptionView();
        
        setupAlarmList();
        loadSounds();
    }

    private String setFirstLetterLower(String str) {
        if (str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private void setupListeners() {
        leftIconBlock.setOnClickListener(v -> finish());
        rightIconBlock.setOnClickListener(v -> showDeleteAllMenu(v));

        lblWakeupTime.setOnClickListener(v -> openTimePickerForAuto(lblWakeupTime, true));
        lblBedTime.setOnClickListener(v -> openTimePickerForAuto(lblBedTime, false));
        lblInterval.setOnClickListener(v -> openIntervalPicker());

        rdoAutoMode.setOnClickListener(v -> switchToAutoMode());
        rdoManualMode.setOnClickListener(v -> switchToManualMode());

        rdoAutoMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) setAutoAlarmMode();
        });

        rdoManualMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) setManualAlarmMode();
        });

        btnSaveAutoReminder.setOnClickListener(v -> {
            if (isValidTimeRange()) {
                applyAutoAlarm(true);
            } else {
                alertHelper.customAlert(stringHelper.getString(R.string.str_from_to_invalid_validation));
            }
        });

        btnAddReminder.setOnClickListener(v -> openTimePickerForManual());

        rdoReminderOn.setOnClickListener(v -> preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_OPTION, 0));
        rdoReminderOff.setOnClickListener(v -> preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_OPTION, 1));
        rdoReminderSilent.setOnClickListener(v -> preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_OPTION, 2));

        switchVibrate.setOnCheckedChangeListener((buttonView, isChecked) -> 
            preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_VIBRATE, !isChecked));

        soundBlock.setOnClickListener(v -> openSoundPicker());
    }

    private void switchToAutoMode() {
        manualReminderBlock.setVisibility(View.GONE);
        autoReminderBlock.setVisibility(View.VISIBLE);
        preferencesHelper.savePreferences(URLFactory.KEY_IS_MANUAL_REMINDER, false);
    }

    private void switchToManualMode() {
        manualReminderBlock.setVisibility(View.VISIBLE);
        autoReminderBlock.setVisibility(View.GONE);
        preferencesHelper.savePreferences(URLFactory.KEY_IS_MANUAL_REMINDER, true);
        lblNoRecordFound.setVisibility(alarmList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateRemindersModeView() {
        boolean isManual = preferencesHelper.getBoolean(URLFactory.KEY_IS_MANUAL_REMINDER, false);
        if (isManual) {
            rdoManualMode.setChecked(true);
            switchToManualMode();
        } else {
            rdoAutoMode.setChecked(true);
            switchToAutoMode();
        }
    }

    private void updateVibrateToggle() {
        switchVibrate.setChecked(!preferencesHelper.getBoolean(URLFactory.KEY_REMINDER_VIBRATE, false));
    }

    private void updateReminderOptionView() {
        int option = preferencesHelper.getInt(URLFactory.KEY_REMINDER_OPTION, 0);
        if (option == 1) rdoReminderOff.setChecked(true);
        else if (option == 2) rdoReminderSilent.setChecked(true);
        else rdoReminderOn.setChecked(true);
    }

    private void setupAlarmList() {
        alarmAdapter = new AlarmAdapter(mActivity, alarmList, new AlarmAdapter.CallBack() {
            @Override
            public void onClickSelect(AlarmModel time, int position) {}

            @Override
            public void onClickRemove(AlarmModel time, int position) {
                confirmDeleteReminder(time, position);
            }

            @Override
            public void onClickEdit(AlarmModel time, int position) {
                if (time.getIsOff() != 1) openEditTimePicker(time);
            }

            @Override
            public void onClickSwitch(AlarmModel time, int position, boolean isOn) {
                toggleManualAlarm(time, position, isOn);
            }

            @Override
            public void onClickWeek(AlarmModel time, int position, int weekPos, boolean isOn) {
                toggleManualAlarmWeekDay(time, position, weekPos, isOn);
            }
        });

        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        alarmRecyclerView.setAdapter(alarmAdapter);
        loadManualAlarmsFromDb();
    }

    private void loadAutoSettingsFromDb() {
        ArrayList<HashMap<String, String>> data = databaseHelper.getdata("tbl_alarm_details", "AlarmType='R'");
        if (!data.isEmpty()) {
            String timeRange = data.get(0).get("AlarmTime");
            if (timeRange != null && timeRange.contains("-")) {
                String[] times = timeRange.split("-");
                if (times.length > 1) {
                    lblWakeupTime.setText(times[0].trim());
                    lblBedTime.setText(times[1].trim());
                    
                    fromHour = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", times[0].trim()));
                    fromMinute = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", times[0].trim()));
                    toHour = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", times[1].trim()));
                    toMinute = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", times[1].trim()));
                }
            }
            currentInterval = Integer.parseInt(data.get(0).get("AlarmInterval"));
            updateIntervalLabel();
        }
    }

    private void updateIntervalLabel() {
        if (currentInterval == 60) {
            lblInterval.setText(String.format(Locale.getDefault(), "1 %s", stringHelper.getString(R.string.str_hour)));
        } else {
            lblInterval.setText(String.format(Locale.getDefault(), "%d %s", currentInterval, stringHelper.getString(R.string.str_min)));
        }
    }

    private void setAutoAlarmMode() {
        cancelAllManualAlarms();
        applyAutoAlarm(false);
    }

    private void setManualAlarmMode() {
        cancelAllAutoAlarms();
        reScheduleAllManualAlarms();
    }

    private void cancelAllManualAlarms() {
        for (AlarmModel alarm : alarmList) {
            cancelManualReminderAlarms(alarm);
        }
    }

    private void cancelAllAutoAlarms() {
        ArrayList<HashMap<String, String>> autoAlarms = databaseHelper.getdata("tbl_alarm_details", "AlarmType='R'");
        for (HashMap<String, String> row : autoAlarms) {
            String primaryId = row.get("id");
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("AlarmId")));
            
            ArrayList<HashMap<String, String>> subAlarms = databaseHelper.getdata("tbl_alarm_sub_details", "SuperId=" + primaryId);
            for (HashMap<String, String> subRow : subAlarms) {
                MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(subRow.get("AlarmId")));
            }
        }
    }

    private void reScheduleAllManualAlarms() {
        for (AlarmModel alarm : alarmList) {
            if (alarm.getIsOff() == 0) {
                scheduleManualReminderAlarms(alarm);
            }
        }
    }

    private void scheduleManualReminderAlarms(AlarmModel alarm) {
        int hour = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", alarm.getDrinkTime().trim()));
        int min = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", alarm.getDrinkTime().trim()));

        if (alarm.getSunday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 1, hour, min, Integer.parseInt(alarm.getAlarmSundayId()));
        if (alarm.getMonday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 2, hour, min, Integer.parseInt(alarm.getAlarmMondayId()));
        if (alarm.getTuesday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 3, hour, min, Integer.parseInt(alarm.getAlarmTuesdayId()));
        if (alarm.getWednesday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 4, hour, min, Integer.parseInt(alarm.getAlarmWednesdayId()));
        if (alarm.getThursday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 5, hour, min, Integer.parseInt(alarm.getAlarmThursdayId()));
        if (alarm.getFriday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 6, hour, min, Integer.parseInt(alarm.getAlarmFridayId()));
        if (alarm.getSaturday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 7, hour, min, Integer.parseInt(alarm.getAlarmSaturdayId()));
    }

    private void cancelManualReminderAlarms(AlarmModel alarm) {
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(alarm.getAlarmSundayId()));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(alarm.getAlarmMondayId()));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(alarm.getAlarmTuesdayId()));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(alarm.getAlarmWednesdayId()));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(alarm.getAlarmThursdayId()));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(alarm.getAlarmFridayId()));
        MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(alarm.getAlarmSaturdayId()));
    }

    private void applyAutoAlarm(boolean moveBack) {
        if (!moveBack) loadAutoSettingsFromDb();

        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, fromHour);
        start.set(Calendar.MINUTE, fromMinute);
        start.set(Calendar.SECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, toHour);
        end.set(Calendar.MINUTE, toMinute);
        end.set(Calendar.SECOND, 0);

        if (isNextDayEnd()) end.add(Calendar.DAY_OF_YEAR, 1);

        if (start.getTimeInMillis() < end.getTimeInMillis()) {
            clearAutoAlarmsFromDisk();
            
            int masterId = (int) System.currentTimeMillis();
            ContentValues masterValues = new ContentValues();
            masterValues.put("AlarmTime", lblWakeupTime.getText().toString() + " - " + lblBedTime.getText().toString());
            masterValues.put("AlarmId", String.valueOf(masterId));
            masterValues.put("AlarmType", "R");
            masterValues.put("AlarmInterval", String.valueOf(currentInterval));
            databaseHelper.INSERT("tbl_alarm_details", masterValues);
            
            String lastInsertedId = databaseHelper.GET_LAST_ID("tbl_alarm_details");

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
            SimpleDateFormat displaySdf = new SimpleDateFormat("hh:mm a", Locale.US);

            while (start.getTimeInMillis() <= end.getTimeInMillis()) {
                try {
                    int subId = (int) System.currentTimeMillis();
                    String timeStr = displaySdf.format(sdf.parse(start.get(Calendar.HOUR_OF_DAY) + ":" + start.get(Calendar.MINUTE) + ":00"));
                    
                    MyAlarmManager.scheduleAutoRecurringAlarm(mContext, start, subId);
                    
                    ContentValues subValues = new ContentValues();
                    subValues.put("AlarmTime", timeStr);
                    subValues.put("AlarmId", String.valueOf(subId));
                    subValues.put("SuperId", lastInsertedId);
                    databaseHelper.INSERT("tbl_alarm_sub_details", subValues);
                } catch (Exception e) {
                    Log.e(TAG, "Error scheduling auto alarm", e);
                }
                start.add(Calendar.MINUTE, currentInterval);
                try { Thread.sleep(5); } catch (InterruptedException ignore) {} // Small delay to avoid same millis for ID
            }

            if (moveBack) finish();
        } else {
            alertHelper.customAlert(stringHelper.getString(R.string.str_from_to_invalid_validation));
        }
    }

    private void clearAutoAlarmsFromDisk() {
        ArrayList<HashMap<String, String>> autoRows = databaseHelper.getdata("tbl_alarm_details", "AlarmType='R'");
        for (HashMap<String, String> row : autoRows) {
            String id = row.get("id");
            MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(row.get("AlarmId")));
            
            ArrayList<HashMap<String, String>> subs = databaseHelper.getdata("tbl_alarm_sub_details", "SuperId=" + id);
            for (HashMap<String, String> sub : subs) {
                MyAlarmManager.cancelRecurringAlarm(mContext, Integer.parseInt(sub.get("AlarmId")));
            }
            databaseHelper.remove("tbl_alarm_details", "id=" + id);
            databaseHelper.remove("tbl_alarm_sub_details", "SuperId=" + id);
        }
    }

    private boolean isNextDayEnd() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
            return sdf.parse(lblWakeupTime.getText().toString().trim()).getTime() > 
                   sdf.parse(lblBedTime.getText().toString().trim()).getTime();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTimeRange() {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, fromHour);
        start.set(Calendar.MINUTE, fromMinute);
        
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, toHour);
        end.set(Calendar.MINUTE, toMinute);
        
        if (isNextDayEnd()) end.add(Calendar.DAY_OF_YEAR, 1);
        
        long diffMinutes = (end.getTimeInMillis() - start.getTimeInMillis()) / (60 * 1000);
        return currentInterval <= diffMinutes;
    }

    private void toggleManualAlarm(AlarmModel alarm, int position, boolean isOn) {
        databaseHelper.UPDATE("tbl_alarm_details", createValue("IsOff", isOn ? 0 : 1), "id=" + alarm.getId());
        alarmList.get(position).setIsOff(isOn ? 0 : 1);
        
        if (isOn) {
            scheduleManualReminderAlarms(alarm);
            // Auto enable a day if none selected
            if (!hasAnyDaySelected(alarm)) {
                autoAssignCurrentDay(alarm, position);
            }
        } else {
            cancelManualReminderAlarms(alarm);
        }
    }

    private boolean hasAnyDaySelected(AlarmModel alarm) {
        return alarm.getSunday() == 1 || alarm.getMonday() == 1 || alarm.getTuesday() == 1 || 
               alarm.getWednesday() == 1 || alarm.getThursday() == 1 || alarm.getFriday() == 1 || 
               alarm.getSaturday() == 1;
    }

    private void autoAssignCurrentDay(AlarmModel alarm, int pos) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hour = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", alarm.getDrinkTime().trim()));
        int min = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", alarm.getDrinkTime().trim()));
        
        ContentValues values = new ContentValues();
        String field = "";
        int alarmId = 0;
        
        switch (day) {
            case Calendar.SUNDAY: field = "Sunday"; alarmId = Integer.parseInt(alarm.getAlarmSundayId()); alarmList.get(pos).setSunday(1); break;
            case Calendar.MONDAY: field = "Monday"; alarmId = Integer.parseInt(alarm.getAlarmMondayId()); alarmList.get(pos).setMonday(1); break;
            case Calendar.TUESDAY: field = "Tuesday"; alarmId = Integer.parseInt(alarm.getAlarmTuesdayId()); alarmList.get(pos).setTuesday(1); break;
            case Calendar.WEDNESDAY: field = "Wednesday"; alarmId = Integer.parseInt(alarm.getAlarmWednesdayId()); alarmList.get(pos).setWednesday(1); break;
            case Calendar.THURSDAY: field = "Thursday"; alarmId = Integer.parseInt(alarm.getAlarmThursdayId()); alarmList.get(pos).setThursday(1); break;
            case Calendar.FRIDAY: field = "Friday"; alarmId = Integer.parseInt(alarm.getAlarmFridayId()); alarmList.get(pos).setFriday(1); break;
            case Calendar.SATURDAY: field = "Saturday"; alarmId = Integer.parseInt(alarm.getAlarmSaturdayId()); alarmList.get(pos).setSaturday(1); break;
        }
        
        if (!field.isEmpty()) {
            values.put(field, 1);
            databaseHelper.UPDATE("tbl_alarm_details", values, "id=" + alarm.getId());
            MyAlarmManager.scheduleManualRecurringAlarm(mContext, day, hour, min, alarmId);
            alarmAdapter.notifyDataSetChanged();
        }
    }

    private void toggleManualAlarmWeekDay(AlarmModel alarm, int pos, int weekIdx, boolean isOn) {
        int hour = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", alarm.getDrinkTime().trim()));
        int min = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", alarm.getDrinkTime().trim()));
        
        ContentValues values = new ContentValues();
        if (isOn) {
            values.put("IsOff", 0);
            alarmList.get(pos).setIsOff(0);
        }

        String field = "";
        int alarmId = 0;
        int dayEnum = 0;
        
        switch (weekIdx) {
            case 0: field = "Sunday"; alarmId = Integer.parseInt(alarm.getAlarmSundayId()); dayEnum = Calendar.SUNDAY; alarmList.get(pos).setSunday(isOn ? 1 : 0); break;
            case 1: field = "Monday"; alarmId = Integer.parseInt(alarm.getAlarmMondayId()); dayEnum = Calendar.MONDAY; alarmList.get(pos).setMonday(isOn ? 1 : 0); break;
            case 2: field = "Tuesday"; alarmId = Integer.parseInt(alarm.getAlarmTuesdayId()); dayEnum = Calendar.TUESDAY; alarmList.get(pos).setTuesday(isOn ? 1 : 0); break;
            case 3: field = "Wednesday"; alarmId = Integer.parseInt(alarm.getAlarmWednesdayId()); dayEnum = Calendar.WEDNESDAY; alarmList.get(pos).setWednesday(isOn ? 1 : 0); break;
            case 4: field = "Thursday"; alarmId = Integer.parseInt(alarm.getAlarmThursdayId()); dayEnum = Calendar.THURSDAY; alarmList.get(pos).setThursday(isOn ? 1 : 0); break;
            case 5: field = "Friday"; alarmId = Integer.parseInt(alarm.getAlarmFridayId()); dayEnum = Calendar.FRIDAY; alarmList.get(pos).setFriday(isOn ? 1 : 0); break;
            case 6: field = "Saturday"; alarmId = Integer.parseInt(alarm.getAlarmSaturdayId()); dayEnum = Calendar.SATURDAY; alarmList.get(pos).setSaturday(isOn ? 1 : 0); break;
        }
        
        values.put(field, isOn ? 1 : 0);
        databaseHelper.UPDATE("tbl_alarm_details", values, "id=" + alarm.getId());
        
        if (isOn) MyAlarmManager.scheduleManualRecurringAlarm(mContext, dayEnum, hour, min, alarmId);
        else MyAlarmManager.cancelRecurringAlarm(mContext, alarmId);
        
        if (!hasAnyDaySelected(alarmList.get(pos))) {
            databaseHelper.UPDATE("tbl_alarm_details", createValue("IsOff", 1), "id=" + alarm.getId());
            alarmList.get(pos).setIsOff(1);
        }
        
        alarmAdapter.notifyDataSetChanged();
    }

    private ContentValues createValue(String key, Object val) {
        ContentValues cv = new ContentValues();
        if (val instanceof Integer) cv.put(key, (Integer) val);
        else if (val instanceof String) cv.put(key, (String) val);
        return cv;
    }

    private void confirmDeleteReminder(AlarmModel alarm, int pos) {
        new AlertDialog.Builder(mActivity)
            .setMessage(stringHelper.getString(R.string.str_reminder_remove_confirm_message))
            .setPositiveButton(R.string.str_yes, (d, w) -> {
                cancelManualReminderAlarms(alarm);
                databaseHelper.remove("tbl_alarm_details", "id=" + alarm.getId());
                alarmList.remove(pos);
                alarmAdapter.notifyDataSetChanged();
                lblNoRecordFound.setVisibility(alarmList.isEmpty() ? View.VISIBLE : View.GONE);
            })
            .setNegativeButton(R.string.str_no, null)
            .show();
    }

    private void showDeleteAllMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.delete_all_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete_item) {
                new AlertDialog.Builder(mActivity)
                    .setMessage(stringHelper.getString(R.string.str_reminder_remove_all_confirm_message))
                    .setPositiveButton(R.string.str_yes, (d, w) -> deleteAllManualAlarms())
                    .setNegativeButton(R.string.str_no, null)
                    .show();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void deleteAllManualAlarms() {
        cancelAllManualAlarms();
        databaseHelper.remove("tbl_alarm_details", "AlarmType='M'");
        alarmList.clear();
        alarmAdapter.notifyDataSetChanged();
        switchToAutoMode();
        rdoAutoMode.setChecked(true);
        applyAutoAlarm(false);
    }

    private void loadManualAlarmsFromDb() {
        alarmList.clear();
        ArrayList<HashMap<String, String>> data = databaseHelper.getdata("tbl_alarm_details", "AlarmType='M'");
        for (HashMap<String, String> row : data) {
            AlarmModel model = new AlarmModel();
            model.setId(row.get("id"));
            model.setDrinkTime(row.get("AlarmTime"));
            model.setAlarmId(row.get("AlarmId"));
            model.setAlarmType(row.get("AlarmType"));
            model.setAlarmInterval(row.get("AlarmInterval"));
            model.setIsOff(Integer.parseInt(row.get("IsOff")));
            model.setSunday(Integer.parseInt(row.get("Sunday")));
            model.setMonday(Integer.parseInt(row.get("Monday")));
            model.setTuesday(Integer.parseInt(row.get("Tuesday")));
            model.setWednesday(Integer.parseInt(row.get("Wednesday")));
            model.setThursday(Integer.parseInt(row.get("Thursday")));
            model.setFriday(Integer.parseInt(row.get("Friday")));
            model.setSaturday(Integer.parseInt(row.get("Saturday")));
            model.setAlarmSundayId(row.get("SundayAlarmId"));
            model.setAlarmMondayId(row.get("MondayAlarmId"));
            model.setAlarmTuesdayId(row.get("TuesdayAlarmId"));
            model.setAlarmWednesdayId(row.get("WednesdayAlarmId"));
            model.setAlarmThursdayId(row.get("ThursdayAlarmId"));
            model.setAlarmFridayId(row.get("FridayAlarmId"));
            model.setAlarmSaturdayId(row.get("SaturdayAlarmId"));
            alarmList.add(model);
        }
        lblNoRecordFound.setVisibility(alarmList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void openIntervalPicker() {
        loadIntervals();
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_pick_interval, null);
        RecyclerView rv = view.findViewById(R.id.intervalRecyclerView);
        intervalAdapter = new IntervalAdapter(mActivity, intervalList, (item, pos) -> {
            for (IntervalModel m : intervalList) m.isSelected(false);
            intervalList.get(pos).isSelected(true);
            intervalAdapter.notifyDataSetChanged();
        });
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.setAdapter(intervalAdapter);

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            for (IntervalModel m : intervalList) {
                if (m.isSelected()) {
                    currentInterval = m.getId();
                    updateIntervalLabel();
                    break;
                }
            }
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void loadIntervals() {
        intervalList.clear();
        intervalList.add(newInterval(15, "15 " + stringHelper.getString(R.string.str_min)));
        intervalList.add(newInterval(30, "30 " + stringHelper.getString(R.string.str_min)));
        intervalList.add(newInterval(45, "45 " + stringHelper.getString(R.string.str_min)));
        intervalList.add(newInterval(60, "1 " + stringHelper.getString(R.string.str_hour)));
    }

    private IntervalModel newInterval(int id, String name) {
        IntervalModel m = new IntervalModel();
        m.setId(id);
        m.setName(name);
        m.isSelected(id == currentInterval);
        return m;
    }

    private void openTimePickerForAuto(AppCompatTextView label, boolean isFrom) {
        int h = isFrom ? fromHour : toHour;
        int m = isFrom ? fromMinute : toMinute;

        TimePickerDialog tpd = TimePickerDialog.newInstance((v, hourOfDay, minute, second) -> {
            if (isFrom) { fromHour = hourOfDay; fromMinute = minute; }
            else { toHour = hourOfDay; toMinute = minute; }
            
            try {
                SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat display = new SimpleDateFormat("hh:mm a", Locale.US);
                label.setText(display.format(hms.parse(hourOfDay + ":" + minute + ":00")));
            } catch (Exception ignored) {}
        }, h, m, DateFormat.is24HourFormat(mActivity));

        tpd.setSelectableTimes(generateTimepoints(23.5, 30));
        tpd.setMaxTime(23, 30, 0);
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        tpd.show(getSupportFragmentManager(), "TimePickerAuto");
    }

    private void openTimePickerForManual() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance((v, h, m, s) -> {
            try {
                SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat display = new SimpleDateFormat("hh:mm a", Locale.US);
                String timeStr = display.format(hms.parse(h + ":" + m + ":00"));

                if (!databaseHelper.IS_EXISTS("tbl_alarm_details", "AlarmTime='" + timeStr + "'")) {
                    saveNewManualAlarm(timeStr, h, m);
                } else {
                    alertHelper.customAlert(stringHelper.getString(R.string.str_set_alarm_validation));
                }
            } catch (Exception ignored) {}
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), DateFormat.is24HourFormat(mActivity));

        tpd.setSelectableTimes(generateTimepoints(23.5, 15));
        tpd.setMaxTime(23, 30, 0);
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        tpd.show(getSupportFragmentManager(), "TimePickerManual");
    }

    private void saveNewManualAlarm(String timeStr, int h, int m) {
        int masterId = (int) System.currentTimeMillis();
        int[] dayIds = new int[7];
        for (int i = 0; i < 7; i++) {
            dayIds[i] = (int) System.currentTimeMillis() + i + 1;
            MyAlarmManager.scheduleManualRecurringAlarm(mContext, i + 1, h, m, dayIds[i]);
            try { Thread.sleep(2); } catch (Exception ignore) {}
        }

        ContentValues cv = new ContentValues();
        cv.put("AlarmTime", timeStr);
        cv.put("AlarmId", String.valueOf(masterId));
        cv.put("SundayAlarmId", String.valueOf(dayIds[0]));
        cv.put("MondayAlarmId", String.valueOf(dayIds[1]));
        cv.put("TuesdayAlarmId", String.valueOf(dayIds[2]));
        cv.put("WednesdayAlarmId", String.valueOf(dayIds[3]));
        cv.put("ThursdayAlarmId", String.valueOf(dayIds[4]));
        cv.put("FridayAlarmId", String.valueOf(dayIds[5]));
        cv.put("SaturdayAlarmId", String.valueOf(dayIds[6]));
        cv.put("AlarmType", "M");
        cv.put("AlarmInterval", "0");
        cv.put("IsOff", 0);
        cv.put("Sunday", 1); cv.put("Monday", 1); cv.put("Tuesday", 1); cv.put("Wednesday", 1);
        cv.put("Thursday", 1); cv.put("Friday", 1); cv.put("Saturday", 1);
        
        databaseHelper.INSERT("tbl_alarm_details", cv);
        loadManualAlarmsFromDb();
        alarmAdapter.notifyDataSetChanged();
        alertHelper.customAlert(stringHelper.getString(R.string.str_successfully_set_alarm));
    }

    private void openEditTimePicker(AlarmModel alarm) {
        int h = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "HH", alarm.getDrinkTime().trim()));
        int m = Integer.parseInt(dateHelper.formatDateFromString("hh:mm a", "mm", alarm.getDrinkTime().trim()));

        TimePickerDialog tpd = TimePickerDialog.newInstance((v, selectedH, selectedM, s) -> {
            try {
                SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat display = new SimpleDateFormat("hh:mm a", Locale.US);
                String timeStr = display.format(hms.parse(selectedH + ":" + selectedM + ":00"));

                if (!databaseHelper.IS_EXISTS("tbl_alarm_details", "AlarmTime='" + timeStr + "'")) {
                    updateManualAlarm(alarm, timeStr, selectedH, selectedM);
                } else {
                    alertHelper.customAlert(stringHelper.getString(R.string.str_set_alarm_validation));
                }
            } catch (Exception ignored) {}
        }, h, m, DateFormat.is24HourFormat(mActivity));

        tpd.setSelectableTimes(generateTimepoints(23.5, 15));
        tpd.setMaxTime(23, 30, 0);
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        tpd.show(getSupportFragmentManager(), "TimePickerEdit");
    }

    private void updateManualAlarm(AlarmModel alarm, String timeStr, int h, int m) {
        databaseHelper.UPDATE("tbl_alarm_details", createValue("AlarmTime", timeStr), "id=" + alarm.getId());
        
        if (alarm.getSunday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 1, h, m, Integer.parseInt(alarm.getAlarmSundayId()));
        if (alarm.getMonday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 2, h, m, Integer.parseInt(alarm.getAlarmMondayId()));
        if (alarm.getTuesday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 3, h, m, Integer.parseInt(alarm.getAlarmTuesdayId()));
        if (alarm.getWednesday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 4, h, m, Integer.parseInt(alarm.getAlarmWednesdayId()));
        if (alarm.getThursday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 5, h, m, Integer.parseInt(alarm.getAlarmThursdayId()));
        if (alarm.getFriday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 6, h, m, Integer.parseInt(alarm.getAlarmFridayId()));
        if (alarm.getSaturday() == 1) MyAlarmManager.scheduleManualRecurringAlarm(mContext, 7, h, m, Integer.parseInt(alarm.getAlarmSaturdayId()));
        
        loadManualAlarmsFromDb();
        alarmAdapter.notifyDataSetChanged();
    }

    private void openSoundPicker() {
        soundSheetDialog = new BottomSheetDialog(mActivity);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sound_pick, null);
        RecyclerView rv = view.findViewById(R.id.soundRecyclerView);
        
        soundAdapter = new SoundAdapter(mActivity, soundList, (item, pos) -> {
            for (SoundModel s : soundList) s.isSelected(false);
            soundList.get(pos).isSelected(true);
            soundAdapter.notifyDataSetChanged();
            preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_SOUND, pos);
            if (pos > 0) playPreviewSound(pos);
        });
        
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.setAdapter(soundAdapter);
        
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> soundSheetDialog.dismiss());
        soundSheetDialog.setContentView(view);
        soundSheetDialog.show();
    }

    private void loadSounds() {
        soundList.clear();
        String[] names = {"Default", "Bell", "Blop", "Bong", "Click", "Echo droplet", "Mario droplet", "Ship bell", "Simple droplet", "Tiny droplet"};
        int selected = preferencesHelper.getInt(URLFactory.KEY_REMINDER_SOUND, 0);
        for (int i = 0; i < names.length; i++) {
            SoundModel m = new SoundModel();
            m.setId(i);
            m.setName(names[i]);
            m.isSelected(i == selected);
            soundList.add(m);
        }
    }

    private void playPreviewSound(int idx) {
        int resId = 0;
        switch (idx) {
            case 1: resId = R.raw.bell; break;
            case 2: resId = R.raw.blop; break;
            case 3: resId = R.raw.bong; break;
            case 4: resId = R.raw.click; break;
            case 5: resId = R.raw.echo_droplet; break;
            case 6: resId = R.raw.mario_droplet; break;
            case 7: resId = R.raw.ship_bell; break;
            case 8: resId = R.raw.simple_droplet; break;
            case 9: resId = R.raw.tiny_droplet; break;
        }
        if (resId != 0) {
            MediaPlayer mp = MediaPlayer.create(this, resId);
            mp.setOnCompletionListener(MediaPlayer::release);
            mp.start();
        }
    }

    public static Timepoint[] generateTimepoints(double maxHour, int minutesInterval) {
        int lastValue = (int) (60 * maxHour);
        List<Timepoint> points = new ArrayList<>();
        for (int m = 0; m <= lastValue; m += minutesInterval) {
            int h = m / 60;
            int mm = m % 60;
            if (h < 24) points.add(new Timepoint(h, mm));
        }
        return points.toArray(new Timepoint[0]);
    }
}
