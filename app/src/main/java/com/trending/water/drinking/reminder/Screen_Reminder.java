package com.trending.water.drinking.reminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.trending.water.drinking.reminder.adapter.AlarmAdapter;
import com.trending.water.drinking.reminder.adapter.IntervalAdapter;
import com.trending.water.drinking.reminder.adapter.SoundAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.model.AlarmModel;
import com.trending.water.drinking.reminder.model.IntervalModel;
import com.trending.water.drinking.reminder.model.SoundModel;
import com.trending.water.drinking.reminder.receiver.MyAlarmManager;
import com.trending.water.drinking.reminder.utils.URLFactory;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
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
    RelativeLayout add_reminder;
    AlarmAdapter alarmAdapter;
    ArrayList<AlarmModel> alarmModelList = new ArrayList<>();
    RecyclerView alarmRecyclerView;
    RelativeLayout auto_reminder_block;
    BottomSheetDialog bottomSheetDialogSound;
    int from_hour = 0;
    int from_minute = 0;
    int interval = 30;
    IntervalAdapter intervalAdapter;
    AppCompatTextView lbl_bed_time;
    AppCompatTextView lbl_interval;
    AppCompatTextView lbl_no_record_found;
    AppCompatTextView lbl_wakeup_time;
    AppCompatTextView lblbt;
    AppCompatTextView lblwt;
    LinearLayout left_icon_block;
    List<String> lst_interval = new ArrayList();
    List<IntervalModel> lst_intervals = new ArrayList();
    List<SoundModel> lst_sounds = new ArrayList();
    RelativeLayout manual_reminder_block;
    RadioButton rdo_auto;
    RadioButton rdo_auto_alarm;
    RadioButton rdo_manual_alarm;
    RadioButton rdo_off;
    RadioButton rdo_silent;
    LinearLayout right_icon_block;
    RelativeLayout save_reminder;
    SoundAdapter soundAdapter;
    LinearLayout sound_block;
    SwitchCompat switch_vibrate;
    int to_hour = 0;
    int to_minute = 0;

    public static Timepoint[] generateTimepoints(double maxHour, int minutesInterval) {
        int lastValue = (int) (60.0d * maxHour);
        List<Timepoint> timepoints = new ArrayList<>();
        int minute = 0;
        while (minute <= lastValue) {
            int currentHour = minute / 60;
            int currentMinute = minute - (currentHour > 0 ? currentHour * 60 : 0);
            if (currentHour != 24) {
                timepoints.add(new Timepoint(currentHour, currentMinute));
            }
            minute += minutesInterval;
        }
        return (Timepoint[]) timepoints.toArray(new Timepoint[timepoints.size()]);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_reminder);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(this.mContext.getResources().getColor(R.color.str_green_card));
        }
        FindViewById();
        Body();
        this.lblwt = (AppCompatTextView) findViewById(R.id.lblwt);
        this.lblbt = (AppCompatTextView) findViewById(R.id.lblbt);
        String str = this.sh.get_string(R.string.str_bed_time);
        this.lblbt.setText(str.substring(0, 1).toUpperCase() + "" + str.substring(1).toLowerCase());
        String str2 = this.sh.get_string(R.string.str_wakeup_time);
        this.lblwt.setText(str2.substring(0, 1).toUpperCase() + "" + str2.substring(1).toLowerCase());
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details");
        Log.d("setAutoAlarmAnd", "" + new Gson().toJson((Object) arr_data));
        ArrayList<HashMap<String, String>> arr_data2 = this.dh.getdata("tbl_alarm_sub_details");
        Log.d("setAutoAlarmAnd", "" + new Gson().toJson((Object) arr_data2));
    }

    private void FindViewById() {
        this.right_icon_block = (LinearLayout) findViewById(R.id.right_icon_block);
        this.left_icon_block = (LinearLayout) findViewById(R.id.left_icon_block);
        this.alarmRecyclerView = (RecyclerView) findViewById(R.id.alarmRecyclerView);
        this.lbl_no_record_found = (AppCompatTextView) findViewById(R.id.lbl_no_record_found);
        this.rdo_auto = (RadioButton) findViewById(R.id.rdo_auto);
        this.rdo_off = (RadioButton) findViewById(R.id.rdo_off);
        this.rdo_silent = (RadioButton) findViewById(R.id.rdo_silent);
        this.sound_block = (LinearLayout) findViewById(R.id.sound_block);
        this.switch_vibrate = (SwitchCompat) findViewById(R.id.switch_vibrate);
        this.lbl_wakeup_time = (AppCompatTextView) findViewById(R.id.lbl_wakeup_time);
        this.lbl_bed_time = (AppCompatTextView) findViewById(R.id.lbl_bed_time);
        this.lbl_interval = (AppCompatTextView) findViewById(R.id.lbl_interval);
        this.manual_reminder_block = (RelativeLayout) findViewById(R.id.manual_reminder_block);
        this.auto_reminder_block = (RelativeLayout) findViewById(R.id.auto_reminder_block);
        this.rdo_auto_alarm = (RadioButton) findViewById(R.id.rdo_auto_alarm);
        this.rdo_manual_alarm = (RadioButton) findViewById(R.id.rdo_manual_alarm);
        this.add_reminder = (RelativeLayout) findViewById(R.id.add_reminder);
        this.save_reminder = (RelativeLayout) findViewById(R.id.save_reminder);
    }

    public void setAutoAlarmAndRemoveAllManualAlarm() {
        for (int k = 0; k < this.alarmModelList.size(); k++) {
            AlarmModel time = this.alarmModelList.get(k);
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmSundayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmMondayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmTuesdayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmWednesdayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmThursdayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmFridayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmSaturdayId()));
        }
        setAutoAlarm(false);
    }

    public void setAllManualAlarmAndRemoveAutoAlarm() {
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details", "AlarmType='R'");
        for (int k = 0; k < arr_data.size(); k++) {
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("AlarmId")));
            Database_Helper database_Helper = this.dh;
            ArrayList<HashMap<String, String>> arr_data2 = database_Helper.getdata("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data.get(k).get("id")));
            for (int j = 0; j < arr_data2.size(); j++) {
                MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data2.get(j).get("AlarmId")));
            }
        }
        for (int k2 = 0; k2 < this.alarmModelList.size(); k2++) {
            AlarmModel time = this.alarmModelList.get(k2);
            StringBuilder sb = new StringBuilder();
            sb.append("");
            Date_Helper date_Helper = this.dth;
            sb.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", time.getDrinkTime().trim()));
            int hourOfDay = Integer.parseInt(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            Date_Helper date_Helper2 = this.dth;
            sb2.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", time.getDrinkTime().trim()));
            int minute = Integer.parseInt(sb2.toString());
            Log.d("setAllManualAlarm : ", "" + time.getSunday());
            if (time.getSunday().intValue() == 1) {
                Context context = this.mContext;
                MyAlarmManager.scheduleManualRecurringAlarm(context, 1, hourOfDay, minute, Integer.parseInt("" + time.getAlarmSundayId()));
            }
            if (time.getMonday().intValue() == 1) {
                Context context2 = this.mContext;
                MyAlarmManager.scheduleManualRecurringAlarm(context2, 2, hourOfDay, minute, Integer.parseInt("" + time.getAlarmMondayId()));
            }
            if (time.getTuesday().intValue() == 1) {
                Context context3 = this.mContext;
                MyAlarmManager.scheduleManualRecurringAlarm(context3, 3, hourOfDay, minute, Integer.parseInt("" + time.getAlarmTuesdayId()));
            }
            if (time.getWednesday().intValue() == 1) {
                Context context4 = this.mContext;
                MyAlarmManager.scheduleManualRecurringAlarm(context4, 4, hourOfDay, minute, Integer.parseInt("" + time.getAlarmWednesdayId()));
            }
            if (time.getThursday().intValue() == 1) {
                Context context5 = this.mContext;
                MyAlarmManager.scheduleManualRecurringAlarm(context5, 5, hourOfDay, minute, Integer.parseInt("" + time.getAlarmThursdayId()));
            }
            if (time.getFriday().intValue() == 1) {
                Context context6 = this.mContext;
                MyAlarmManager.scheduleManualRecurringAlarm(context6, 6, hourOfDay, minute, Integer.parseInt("" + time.getAlarmFridayId()));
            }
            if (time.getSaturday().intValue() == 1) {
                Context context7 = this.mContext;
                MyAlarmManager.scheduleManualRecurringAlarm(context7, 7, hourOfDay, minute, Integer.parseInt("" + time.getAlarmSaturdayId()));
            }
        }
    }

    public void load_AutoDataFromDB() {
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details", "AlarmType='R'");
        if (arr_data.size() > 0) {
            String[] str_date = ((String) arr_data.get(0).get("AlarmTime")).split("-");
            if (str_date.length > 1) {
                this.lbl_wakeup_time.setText(str_date[0].trim());
                this.lbl_bed_time.setText(str_date[1].trim());
            }
            StringBuilder sb = new StringBuilder();
            sb.append("");
            Date_Helper date_Helper = this.dth;
            sb.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", str_date[0].trim()));
            this.from_hour = Integer.parseInt(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            Date_Helper date_Helper2 = this.dth;
            sb2.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", str_date[0].trim()));
            this.from_minute = Integer.parseInt(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            Date_Helper date_Helper3 = this.dth;
            sb3.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", str_date[1].trim()));
            this.to_hour = Integer.parseInt(sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            Date_Helper date_Helper4 = this.dth;
            sb4.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", str_date[1].trim()));
            this.to_minute = Integer.parseInt(sb4.toString());
            this.interval = Integer.parseInt("" + ((String) arr_data.get(0).get("AlarmInterval")));
            if (((String) arr_data.get(0).get("AlarmInterval")).equalsIgnoreCase("60")) {
                AppCompatTextView appCompatTextView = this.lbl_interval;
                appCompatTextView.setText("1 " + this.sh.get_string(R.string.str_hour));
                return;
            }
            AppCompatTextView appCompatTextView2 = this.lbl_interval;
            appCompatTextView2.setText(((String) arr_data.get(0).get("AlarmInterval")) + " " + this.sh.get_string(R.string.str_min));
        }
    }

    private void Body() {
        load_AutoDataFromDB();
        this.lbl_wakeup_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.openAutoTimePicker2(Screen_Reminder.this.lbl_wakeup_time, true);
            }
        });
        this.lbl_bed_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.openAutoTimePicker2(Screen_Reminder.this.lbl_bed_time, false);
            }
        });
        this.lbl_interval.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.openIntervalPicker();
            }
        });
        if (this.ph.getBoolean(URLFactory.IS_MANUAL_REMINDER)) {
            this.rdo_manual_alarm.setChecked(true);
            this.manual_reminder_block.setVisibility(View.VISIBLE);
            this.auto_reminder_block.setVisibility(View.GONE);
        } else {
            this.rdo_auto_alarm.setChecked(true);
            this.manual_reminder_block.setVisibility(View.GONE);
            this.auto_reminder_block.setVisibility(View.VISIBLE);
        }
        this.rdo_auto_alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.manual_reminder_block.setVisibility(View.GONE);
                Screen_Reminder.this.auto_reminder_block.setVisibility(View.VISIBLE);
                Screen_Reminder.this.ph.savePreferences(URLFactory.IS_MANUAL_REMINDER, false);
            }
        });
        this.rdo_manual_alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.manual_reminder_block.setVisibility(View.VISIBLE);
                Screen_Reminder.this.auto_reminder_block.setVisibility(View.GONE);
                Screen_Reminder.this.ph.savePreferences(URLFactory.IS_MANUAL_REMINDER, true);
                if (Screen_Reminder.this.alarmModelList.size() > 0) {
                    Screen_Reminder.this.lbl_no_record_found.setVisibility(View.GONE);
                } else {
                    Screen_Reminder.this.lbl_no_record_found.setVisibility(View.VISIBLE);
                }
            }
        });
        this.rdo_auto_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Screen_Reminder.this.setAutoAlarmAndRemoveAllManualAlarm();
                }
            }
        });
        this.rdo_manual_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Screen_Reminder.this.setAllManualAlarmAndRemoveAutoAlarm();
                }
            }
        });
        this.save_reminder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Screen_Reminder.this.isValidDate()) {
                    Screen_Reminder.this.setAutoAlarm(true);
                } else {
                    Screen_Reminder.this.ah.customAlert(Screen_Reminder.this.sh.get_string(R.string.str_from_to_invalid_validation));
                }
            }
        });
        this.add_reminder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.openTimePicker();
            }
        });
        this.lst_interval.clear();
        List<String> list = this.lst_interval;
        list.add("30 " + this.sh.get_string(R.string.str_minutes));
        List<String> list2 = this.lst_interval;
        list2.add("45 " + this.sh.get_string(R.string.str_minutes));
        List<String> list3 = this.lst_interval;
        list3.add("60 " + this.sh.get_string(R.string.str_minutes));
        List<String> list4 = this.lst_interval;
        list4.add("90 " + this.sh.get_string(R.string.str_minutes));
        List<String> list5 = this.lst_interval;
        list5.add("120 " + this.sh.get_string(R.string.str_minutes));
        this.left_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Reminder.this.finish();
            }
        });
        this.right_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Reminder.this.showMenu(view);
            }
        });
        load_alarm();
        this.switch_vibrate.setChecked(!this.ph.getBoolean(URLFactory.REMINDER_VIBRATE));
        this.switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Screen_Reminder.this.ph.savePreferences(URLFactory.REMINDER_VIBRATE, !isChecked);
            }
        });
        this.alarmAdapter = new AlarmAdapter(this.act, this.alarmModelList, new AlarmAdapter.CallBack() {
            public void onClickSelect(AlarmModel time, int position) {
            }

            public void onClickRemove(final AlarmModel time, final int position) {
                new AlertDialog.Builder(Screen_Reminder.this.act).setMessage(Screen_Reminder.this.sh.get_string(R.string.str_reminder_remove_confirm_message)).setPositiveButton(Screen_Reminder.this.sh.get_string(R.string.str_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmSundayId()));
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmMondayId()));
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmTuesdayId()));
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmWednesdayId()));
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmThursdayId()));
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmFridayId()));
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmSaturdayId()));
                        Screen_Reminder.this.alarmModelList.remove(position);
                        Database_Helper database_Helper = Screen_Reminder.this.dh;
                        database_Helper.REMOVE("tbl_alarm_details", "id=" + time.getId());
                        Screen_Reminder.this.alarmAdapter.notifyDataSetChanged();
                        if (Screen_Reminder.this.alarmModelList.size() > 0) {
                            Screen_Reminder.this.lbl_no_record_found.setVisibility(View.GONE);
                        } else {
                            Screen_Reminder.this.lbl_no_record_found.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton(Screen_Reminder.this.sh.get_string(R.string.str_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
            }

            public void onClickEdit(AlarmModel time, int position) {
                if (time.getIsOff().intValue() != 1) {
                    Screen_Reminder.this.openEditTimePicker(time);
                }
            }

            public void onClickSwitch(AlarmModel time, int position, boolean isOn) {
                int i = position;
                ContentValues initialValues = new ContentValues();
                initialValues.put("IsOff", Integer.valueOf(isOn ^ true ? 1 : 0));
                Database_Helper database_Helper = Screen_Reminder.this.dh;
                database_Helper.UPDATE("tbl_alarm_details", initialValues, "id=" + time.getId());
                Screen_Reminder.this.alarmModelList.get(i).setIsOff(Integer.valueOf(isOn ^ true ? 1 : 0));
                if (isOn) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    Date_Helper date_Helper = Screen_Reminder.this.dth;
                    sb.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", time.getDrinkTime().trim()));
                    int hourOfDay = Integer.parseInt(sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    Date_Helper date_Helper2 = Screen_Reminder.this.dth;
                    sb2.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", time.getDrinkTime().trim()));
                    int minute = Integer.parseInt(sb2.toString());
                    if (time.getSunday().intValue() == 1) {
                        Context context = Screen_Reminder.this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context, 1, hourOfDay, minute, Integer.parseInt("" + time.getAlarmSundayId()));
                    }
                    if (time.getMonday().intValue() == 1) {
                        Context context2 = Screen_Reminder.this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context2, 2, hourOfDay, minute, Integer.parseInt("" + time.getAlarmMondayId()));
                    }
                    if (time.getTuesday().intValue() == 1) {
                        Context context3 = Screen_Reminder.this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context3, 3, hourOfDay, minute, Integer.parseInt("" + time.getAlarmTuesdayId()));
                    }
                    if (time.getWednesday().intValue() == 1) {
                        Context context4 = Screen_Reminder.this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context4, 4, hourOfDay, minute, Integer.parseInt("" + time.getAlarmWednesdayId()));
                    }
                    if (time.getThursday().intValue() == 1) {
                        Context context5 = Screen_Reminder.this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context5, 5, hourOfDay, minute, Integer.parseInt("" + time.getAlarmThursdayId()));
                    }
                    if (time.getFriday().intValue() == 1) {
                        Context context6 = Screen_Reminder.this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context6, 6, hourOfDay, minute, Integer.parseInt("" + time.getAlarmFridayId()));
                    }
                    if (time.getSaturday().intValue() == 1) {
                        Context context7 = Screen_Reminder.this.mContext;
                        MyAlarmManager.scheduleManualRecurringAlarm(context7, 7, hourOfDay, minute, Integer.parseInt("" + time.getAlarmSaturdayId()));
                    }
                    if (Screen_Reminder.this.alarmModelList.get(i).getSunday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(i).getMonday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(i).getTuesday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(i).getThursday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(i).getWednesday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(i).getFriday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(i).getSunday().intValue() == 0) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("");
                        Date_Helper date_Helper3 = Screen_Reminder.this.dth;
                        sb3.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", time.getDrinkTime().trim()));
                        int tmp_from_hour = Integer.parseInt(sb3.toString());
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("");
                        Date_Helper date_Helper4 = Screen_Reminder.this.dth;
                        sb4.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", time.getDrinkTime().trim()));
                        int tmp_from_minute = Integer.parseInt(sb4.toString());
                        int week_pos = Calendar.getInstance().get(7);
                        ContentValues initialValues4 = new ContentValues();
                        if (week_pos == 1) {
                            initialValues4.put("Sunday", 1);
                            Screen_Reminder.this.alarmModelList.get(i).setSunday(1);
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 1, tmp_from_hour, tmp_from_minute, Integer.parseInt("" + time.getAlarmSundayId()));
                        } else if (week_pos == 2) {
                            initialValues4.put("Monday", 1);
                            Screen_Reminder.this.alarmModelList.get(i).setMonday(1);
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 2, tmp_from_hour, tmp_from_minute, Integer.parseInt("" + time.getAlarmMondayId()));
                        } else if (week_pos == 3) {
                            initialValues4.put("Tuesday", 1);
                            Screen_Reminder.this.alarmModelList.get(i).setTuesday(1);
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 3, tmp_from_hour, tmp_from_minute, Integer.parseInt("" + time.getAlarmTuesdayId()));
                        } else if (week_pos == 4) {
                            initialValues4.put("Wednesday", 1);
                            Screen_Reminder.this.alarmModelList.get(i).setWednesday(1);
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 4, tmp_from_hour, tmp_from_minute, Integer.parseInt("" + time.getAlarmWednesdayId()));
                        } else if (week_pos == 5) {
                            initialValues4.put("Thursday", 1);
                            Screen_Reminder.this.alarmModelList.get(i).setThursday(1);
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 5, tmp_from_hour, tmp_from_minute, Integer.parseInt("" + time.getAlarmThursdayId()));
                        } else if (week_pos == 6) {
                            initialValues4.put("Friday", 1);
                            Screen_Reminder.this.alarmModelList.get(i).setFriday(1);
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 6, tmp_from_hour, tmp_from_minute, Integer.parseInt("" + time.getAlarmFridayId()));
                        } else if (week_pos == 7) {
                            initialValues4.put("Saturday", 1);
                            Screen_Reminder.this.alarmModelList.get(i).setSaturday(1);
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 7, tmp_from_hour, tmp_from_minute, Integer.parseInt("" + time.getAlarmSaturdayId()));
                        }
                        Screen_Reminder.this.alarmRecyclerView.post(new Runnable() {
                            public void run() {
                                Screen_Reminder.this.alarmAdapter.notifyDataSetChanged();
                            }
                        });
                        return;
                    }
                    return;
                }
                MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmSundayId()));
                MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmMondayId()));
                MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmTuesdayId()));
                MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmWednesdayId()));
                MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmThursdayId()));
                MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmFridayId()));
                MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, Integer.parseInt(time.getAlarmSaturdayId()));
            }

            public void onClickWeek(AlarmModel time, int position, int week_pos, boolean isOn) {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                Date_Helper date_Helper = Screen_Reminder.this.dth;
                sb.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", time.getDrinkTime().trim()));
                int tmp_from_hour = Integer.parseInt(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                Date_Helper date_Helper2 = Screen_Reminder.this.dth;
                sb2.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", time.getDrinkTime().trim()));
                int tmp_from_minute = Integer.parseInt(sb2.toString());
                ContentValues initialValues = new ContentValues();
                if (isOn) {
                    initialValues.put("IsOff", "0");
                    Screen_Reminder.this.alarmModelList.get(position).setIsOff(Integer.valueOf(isOn ^ true ? 1 : 0));
                }
                if (week_pos == 0) {
                    initialValues.put("Sunday", Integer.valueOf(isOn ? 1 : 0));
                    Screen_Reminder.this.alarmModelList.get(position).setSunday(Integer.valueOf(isOn ? 1 : 0));
                    int _id = Integer.parseInt("" + time.getAlarmSundayId());
                    if (isOn) {
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 1, tmp_from_hour, tmp_from_minute, _id);
                    } else {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, _id);
                    }
                } else if (week_pos == 1) {
                    initialValues.put("Monday", Integer.valueOf(isOn ? 1 : 0));
                    Screen_Reminder.this.alarmModelList.get(position).setMonday(Integer.valueOf(isOn ? 1 : 0));
                    int _id2 = Integer.parseInt("" + time.getAlarmMondayId());
                    if (isOn) {
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 2, tmp_from_hour, tmp_from_minute, _id2);
                    } else {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, _id2);
                    }
                } else if (week_pos == 2) {
                    initialValues.put("Tuesday", Integer.valueOf(isOn ? 1 : 0));
                    Screen_Reminder.this.alarmModelList.get(position).setTuesday(Integer.valueOf(isOn ? 1 : 0));
                    int _id3 = Integer.parseInt("" + time.getAlarmTuesdayId());
                    if (isOn) {
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 3, tmp_from_hour, tmp_from_minute, _id3);
                    } else {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, _id3);
                    }
                } else if (week_pos == 3) {
                    initialValues.put("Wednesday", Integer.valueOf(isOn ? 1 : 0));
                    Screen_Reminder.this.alarmModelList.get(position).setWednesday(Integer.valueOf(isOn ? 1 : 0));
                    int _id4 = Integer.parseInt("" + time.getAlarmWednesdayId());
                    if (isOn) {
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 4, tmp_from_hour, tmp_from_minute, _id4);
                    } else {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, _id4);
                    }
                } else if (week_pos == 4) {
                    initialValues.put("Thursday", Integer.valueOf(isOn ? 1 : 0));
                    Screen_Reminder.this.alarmModelList.get(position).setThursday(Integer.valueOf(isOn ? 1 : 0));
                    int _id5 = Integer.parseInt("" + time.getAlarmThursdayId());
                    if (isOn) {
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 5, tmp_from_hour, tmp_from_minute, _id5);
                    } else {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, _id5);
                    }
                } else if (week_pos == 5) {
                    initialValues.put("Friday", Integer.valueOf(isOn ? 1 : 0));
                    Screen_Reminder.this.alarmModelList.get(position).setFriday(Integer.valueOf(isOn ? 1 : 0));
                    int _id6 = Integer.parseInt("" + time.getAlarmFridayId());
                    if (isOn) {
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 6, tmp_from_hour, tmp_from_minute, _id6);
                    } else {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, _id6);
                    }
                } else if (week_pos == 6) {
                    initialValues.put("Saturday", Integer.valueOf(isOn ? 1 : 0));
                    Screen_Reminder.this.alarmModelList.get(position).setSaturday(Integer.valueOf(isOn ? 1 : 0));
                    int _id7 = Integer.parseInt("" + time.getAlarmSaturdayId());
                    if (isOn) {
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 7, tmp_from_hour, tmp_from_minute, _id7);
                    } else {
                        MyAlarmManager.cancelRecurringAlarm(Screen_Reminder.this.mContext, _id7);
                    }
                }
                Database_Helper database_Helper = Screen_Reminder.this.dh;
                database_Helper.UPDATE("tbl_alarm_details", initialValues, "id=" + time.getId());
                if (Screen_Reminder.this.alarmModelList.get(position).getSunday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(position).getMonday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(position).getTuesday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(position).getThursday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(position).getWednesday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(position).getFriday().intValue() == 0 && Screen_Reminder.this.alarmModelList.get(position).getSunday().intValue() == 0) {
                    ContentValues initialValues2 = new ContentValues();
                    initialValues2.put("IsOff", "1");
                    Database_Helper database_Helper2 = Screen_Reminder.this.dh;
                    database_Helper2.UPDATE("tbl_alarm_details", initialValues2, "id=" + time.getId());
                    Screen_Reminder.this.alarmModelList.get(position).setIsOff(1);
                }
                Screen_Reminder.this.alarmAdapter.notifyDataSetChanged();
            }
        });
        this.alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this.act, RecyclerView.VERTICAL, false));
        this.alarmRecyclerView.setAdapter(this.alarmAdapter);
        if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 1) {
            this.rdo_off.setChecked(true);
        } else if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 2) {
            this.rdo_silent.setChecked(true);
        } else {
            this.rdo_auto.setChecked(true);
        }
        this.rdo_auto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.ph.savePreferences(URLFactory.REMINDER_OPTION, 0);
            }
        });
        this.rdo_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.ph.savePreferences(URLFactory.REMINDER_OPTION, 1);
            }
        });
        this.rdo_silent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.ph.savePreferences(URLFactory.REMINDER_OPTION, 2);
            }
        });
        loadSounds();
        this.sound_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Reminder.this.openSoundMenuPicker();
            }
        });
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() != R.id.delete_item) {
                    return false;
                }
                new AlertDialog.Builder(Screen_Reminder.this.act).setMessage(Screen_Reminder.this.sh.get_string(R.string.str_reminder_remove_all_confirm_message)).setPositiveButton(Screen_Reminder.this.sh.get_string(R.string.str_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Screen_Reminder.this.deleteAllManualAlarm(true);
                        Screen_Reminder.this.rdo_auto_alarm.setChecked(true);
                        dialog.dismiss();
                    }
                }).setNegativeButton(Screen_Reminder.this.sh.get_string(R.string.str_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
                return true;
            }
        });
        popup.inflate(R.menu.delete_all_menu);
        popup.show();
    }

    public void deleteAllManualAlarm(boolean alsoData) {
        for (int k = 0; k < this.alarmModelList.size(); k++) {
            AlarmModel time = this.alarmModelList.get(k);
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmSundayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmMondayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmTuesdayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmWednesdayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmThursdayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmFridayId()));
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt(time.getAlarmSaturdayId()));
            if (alsoData) {
                Database_Helper database_Helper = this.dh;
                database_Helper.REMOVE("tbl_alarm_details", "id=" + time.getId());
            }
        }
        if (alsoData) {
            this.alarmModelList.clear();
            this.alarmAdapter.notifyDataSetChanged();
        }
        this.ph.savePreferences(URLFactory.IS_MANUAL_REMINDER, false);
        this.manual_reminder_block.setVisibility(View.GONE);
        this.auto_reminder_block.setVisibility(View.VISIBLE);
        setAutoAlarm(false);
    }

    public void loadInterval() {
        this.lst_intervals.clear();
        List<IntervalModel> list = this.lst_intervals;
        list.add(getIntervalModel(15, "15 " + this.sh.get_string(R.string.str_min)));
        List<IntervalModel> list2 = this.lst_intervals;
        list2.add(getIntervalModel(30, "30 " + this.sh.get_string(R.string.str_min)));
        List<IntervalModel> list3 = this.lst_intervals;
        list3.add(getIntervalModel(45, "45 " + this.sh.get_string(R.string.str_min)));
        List<IntervalModel> list4 = this.lst_intervals;
        list4.add(getIntervalModel(60, "1 " + this.sh.get_string(R.string.str_hour)));
    }

    public IntervalModel getIntervalModel(int index, String name) {
        IntervalModel intervalModel = new IntervalModel();
        intervalModel.setId(index);
        intervalModel.setName(name);
        intervalModel.isSelected(this.interval == index);
        return intervalModel;
    }

    public void openIntervalPicker() {
        loadInterval();
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_pick_interval, (ViewGroup) null, false);
        RecyclerView intervalRecyclerView = (RecyclerView) view.findViewById(R.id.intervalRecyclerView);
        this.intervalAdapter = new IntervalAdapter(this.act, this.lst_intervals, new IntervalAdapter.CallBack() {
            public void onClickSelect(IntervalModel time, int position) {
                for (int k = 0; k < Screen_Reminder.this.lst_intervals.size(); k++) {
                    Screen_Reminder.this.lst_intervals.get(k).isSelected(false);
                }
                Screen_Reminder.this.lst_intervals.get(position).isSelected(true);
                Screen_Reminder.this.intervalAdapter.notifyDataSetChanged();
            }
        });
        intervalRecyclerView.setLayoutManager(new LinearLayoutManager(this.act, RecyclerView.VERTICAL, false));
        intervalRecyclerView.setAdapter(this.intervalAdapter);
        ((RelativeLayout) view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((RelativeLayout) view.findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int k = 0;
                while (true) {
                    if (k >= Screen_Reminder.this.lst_intervals.size()) {
                        break;
                    } else if (Screen_Reminder.this.lst_intervals.get(k).isSelected()) {
                        Screen_Reminder.this.interval = Screen_Reminder.this.lst_intervals.get(k).getId();
                        Screen_Reminder.this.lbl_interval.setText(Screen_Reminder.this.lst_intervals.get(k).getName());
                        break;
                    } else {
                        k++;
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void openAutoTimePicker2(final AppCompatTextView appCompatTextView, final boolean isFrom) {
        TimePickerDialog tpd;
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                try {
                    if (isFrom) {
                        Screen_Reminder.this.from_hour = hourOfDay;
                        Screen_Reminder.this.from_minute = minute;
                    } else {
                        Screen_Reminder.this.to_hour = hourOfDay;
                        Screen_Reminder.this.to_minute = minute;
                    }
                    Log.d("openAutoTimePicker : ", "" + Screen_Reminder.this.from_hour + "  @@@@  " + Screen_Reminder.this.from_minute);
                    String formatedDate = sdfs.format(sdf.parse("" + hourOfDay + ":" + minute + ":00"));
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(formatedDate);
                    appCompatTextView.setText(sb.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        Calendar now = Calendar.getInstance();
        if (isFrom) {
            now.set(11, this.from_hour);
            now.set(12, this.from_minute);
        } else {
            now.set(11, this.to_hour);
            now.set(12, this.to_minute);
        }
        if (!DateFormat.is24HourFormat(this.act)) {
            tpd = TimePickerDialog.newInstance(onTimeSetListener, now.get(11), now.get(12), false);
            tpd.setSelectableTimes(generateTimepoints(23.5d, 30));
            tpd.setMaxTime(23, 30, 0);
        } else {
            tpd = TimePickerDialog.newInstance(onTimeSetListener, now.get(11), now.get(12), true);
            tpd.setSelectableTimes(generateTimepoints(23.5d, 30));
            tpd.setMaxTime(23, 30, 0);
        }
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(this.mContext));
        tpd.show(getSupportFragmentManager(), "Datepickerdialog");
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(this.mContext));
    }

    public boolean isValidDate() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(11, this.from_hour);
        startTime.set(12, this.from_minute);
        startTime.set(13, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(11, this.to_hour);
        endTime.set(12, this.to_minute);
        endTime.set(13, 0);
        if (isNextDayEnd()) {
            endTime.add(5, 1);
        }
        Log.d("isValidDate", "" + startTime.getTimeInMillis() + " @@@ " + endTime.getTimeInMillis());
        long mills = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        int hours = (int) (mills / 3600000);
        int mins = (int) ((mills / 60000) % 60);
        if (((float) this.interval) <= ((float) ((hours * 60) + mins))) {
            return true;
        }
        return false;
    }

    public void deleteAutoAlarm(boolean alsoData) {
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details", "AlarmType='R'");
        for (int k = 0; k < arr_data.size(); k++) {
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("AlarmId")));
            Database_Helper database_Helper = this.dh;
            ArrayList<HashMap<String, String>> arr_data2 = database_Helper.getdata("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data.get(k).get("id")));
            for (int j = 0; j < arr_data2.size(); j++) {
                MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data2.get(j).get("AlarmId")));
            }
            if (alsoData) {
                Database_Helper database_Helper2 = this.dh;
                database_Helper2.REMOVE("tbl_alarm_details", "id=" + ((String) arr_data.get(k).get("id")));
                Database_Helper database_Helper3 = this.dh;
                database_Helper3.REMOVE("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data.get(k).get("id")));
            }
        }
    }

    public boolean isNextDayEnd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        try {
            if (simpleDateFormat.parse(this.lbl_wakeup_time.getText().toString().trim()).getTime() > simpleDateFormat.parse(this.lbl_bed_time.getText().toString().trim()).getTime()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void setAutoAlarm(boolean moveScreen) {
        ContentValues initialValues;
        int minute_interval = this.interval;
        if (this.sh.check_blank_data(this.lbl_wakeup_time.getText().toString()) || this.sh.check_blank_data(this.lbl_bed_time.getText().toString())) {
            this.ah.customAlert(this.sh.get_string(R.string.str_from_to_invalid_validation));
            return;
        }
        if (!moveScreen) {
            load_AutoDataFromDB();
        }
        Log.d("setAutoAlarm :", "" + this.from_hour + ":" + this.from_minute + "  @@@  " + this.to_hour + ":" + this.to_minute);
        Calendar startTime = Calendar.getInstance();
        int i = 11;
        startTime.set(11, this.from_hour);
        int i2 = 12;
        startTime.set(12, this.from_minute);
        int i3 = 13;
        startTime.set(13, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(11, this.to_hour);
        endTime.set(12, this.to_minute);
        endTime.set(13, 0);
        if (isNextDayEnd()) {
            endTime.add(5, 1);
        }
        if (startTime.getTimeInMillis() < endTime.getTimeInMillis()) {
            deleteAutoAlarm(true);
            int _id = (int) System.currentTimeMillis();
            ContentValues initialValues2 = new ContentValues();
            initialValues2.put("AlarmTime", "" + this.lbl_wakeup_time.getText().toString() + " - " + this.lbl_bed_time.getText().toString());
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(_id);
            initialValues2.put("AlarmId", sb.toString());
            initialValues2.put("AlarmType", "R");
            initialValues2.put("AlarmInterval", "" + minute_interval);
            this.dh.INSERT("tbl_alarm_details", initialValues2);
            String getLastId = this.dh.GET_LAST_ID("tbl_alarm_details");
            int i4 = _id;
            while (startTime.getTimeInMillis() <= endTime.getTimeInMillis()) {
                Log.d("ALARMTIME  : ", "" + startTime.get(i) + ":" + startTime.get(i2) + ":" + startTime.get(i3));
                try {
                    int _id2 = (int) System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                    String formatedDate = sdfs.format(sdf.parse(startTime.get(i) + ":" + startTime.get(i2) + ":" + startTime.get(13)));
                    MyAlarmManager.scheduleAutoRecurringAlarm(this.mContext, startTime, _id2);
                    ContentValues initialValues22 = new ContentValues();
                    StringBuilder sb2 = new StringBuilder();
                    initialValues = initialValues2;
                    try {
                        sb2.append("");
                        sb2.append(formatedDate);
                        initialValues22.put("AlarmTime", sb2.toString());
                        initialValues22.put("AlarmId", "" + _id2);
                        initialValues22.put("SuperId", "" + getLastId);
                        this.dh.INSERT("tbl_alarm_sub_details", initialValues22);
                    } catch (Exception e) {
                        e = e;
                    }
                } catch (Exception e2) {
                    initialValues = initialValues2;
                    e2.printStackTrace();
                    startTime.add(12, minute_interval);
                    initialValues2 = initialValues;
                    i = 11;
                    i2 = 12;
                    i3 = 13;
                }
                startTime.add(12, minute_interval);
                initialValues2 = initialValues;
                i = 11;
                i2 = 12;
                i3 = 13;
            }
            if (moveScreen) {
                finish();
                return;
            }
            return;
        }
        this.ah.customAlert(this.sh.get_string(R.string.str_from_to_invalid_validation));
    }

    public void openEditTimePicker(final AlarmModel alarmtime) {
        TimePickerDialog tpd;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        Date_Helper date_Helper = this.dth;
        sb.append(Date_Helper.FormateDateFromString("hh:mm a", "HH", alarmtime.getDrinkTime().trim()));
        int tmp_from_hour = Integer.parseInt(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        Date_Helper date_Helper2 = this.dth;
        sb2.append(Date_Helper.FormateDateFromString("hh:mm a", "mm", alarmtime.getDrinkTime().trim()));
        int tmp_from_minute = Integer.parseInt(sb2.toString());
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                int i = hourOfDay;
                int i2 = minute;
                String formatedDate = "";
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                Object obj = "";
                try {
                    Date dt = sdf.parse("" + i + ":" + i2 + ":00");
                    formatedDate = sdfs.format(dt);
                    try {
                        Database_Helper database_Helper = Screen_Reminder.this.dh;
                        if (!database_Helper.IS_EXISTS("tbl_alarm_details", "AlarmTime='" + formatedDate + "'")) {
                            ContentValues initialValues = new ContentValues();
                            initialValues.put("AlarmTime", "" + formatedDate);
                            Database_Helper database_Helper2 = Screen_Reminder.this.dh;
                            database_Helper2.UPDATE("tbl_alarm_details", initialValues, "id=" + alarmtime.getId());
                            int _id_sunday = Integer.parseInt("" + alarmtime.getAlarmSundayId());
                            int _id_monday = Integer.parseInt("" + alarmtime.getAlarmMondayId());
                            int _id_tuesday = Integer.parseInt("" + alarmtime.getAlarmTuesdayId());
                            int _id_wednesday = Integer.parseInt("" + alarmtime.getAlarmWednesdayId());
                            int _id_thursday = Integer.parseInt("" + alarmtime.getAlarmThursdayId());
                            int _id_friday = Integer.parseInt("" + alarmtime.getAlarmFridayId());
                            StringBuilder sb = new StringBuilder();
                            Date date = dt;
                            sb.append("");
                            sb.append(alarmtime.getAlarmSaturdayId());
                            int _id_saturday = Integer.parseInt(sb.toString());
                            String formatedDate2 = formatedDate;
                            if (alarmtime.getSunday().intValue() == 1) {
                                MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 1, i, i2, _id_sunday);
                            }
                            if (alarmtime.getMonday().intValue() == 1) {
                                MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 2, i, i2, _id_monday);
                            }
                            if (alarmtime.getTuesday().intValue() == 1) {
                                MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 3, i, i2, _id_tuesday);
                            }
                            if (alarmtime.getWednesday().intValue() == 1) {
                                MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 4, i, i2, _id_wednesday);
                            }
                            if (alarmtime.getThursday().intValue() == 1) {
                                MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 5, i, i2, _id_thursday);
                            }
                            if (alarmtime.getFriday().intValue() == 1) {
                                MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 6, i, i2, _id_friday);
                            }
                            if (alarmtime.getSaturday().intValue() == 1) {
                                MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 7, i, i2, _id_saturday);
                            }
                            Screen_Reminder.this.load_alarm();
                            Screen_Reminder.this.alarmAdapter.notifyDataSetChanged();
                            return;
                        }
                        String str2 = formatedDate;
                        Screen_Reminder.this.ah.customAlert(Screen_Reminder.this.sh.get_string(R.string.str_set_alarm_validation));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (ParseException e3) {
                    e3.printStackTrace();
                }
            }
        };
        Calendar now = Calendar.getInstance();
        now.set(11, tmp_from_hour);
        now.set(12, tmp_from_minute);
        if (!DateFormat.is24HourFormat(this.act)) {
            tpd = TimePickerDialog.newInstance(onTimeSetListener, now.get(11), now.get(12), false);
            tpd.setSelectableTimes(generateTimepoints(23.5d, 15));
            tpd.setMaxTime(23, 30, 0);
        } else {
            tpd = TimePickerDialog.newInstance(onTimeSetListener, now.get(11), now.get(12), true);
            tpd.setSelectableTimes(generateTimepoints(23.5d, 15));
            tpd.setMaxTime(23, 30, 0);
        }
        tpd.show(getSupportFragmentManager(), "Datepickerdialog");
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(this.mContext));
    }

    public void loadSounds() {
        this.lst_sounds.clear();
        this.lst_sounds.add(getSoundModel(0, "Default"));
        this.lst_sounds.add(getSoundModel(1, "Bell"));
        this.lst_sounds.add(getSoundModel(2, "Blop"));
        this.lst_sounds.add(getSoundModel(3, "Bong"));
        this.lst_sounds.add(getSoundModel(4, "Click"));
        this.lst_sounds.add(getSoundModel(5, "Echo droplet"));
        this.lst_sounds.add(getSoundModel(6, "Mario droplet"));
        this.lst_sounds.add(getSoundModel(7, "Ship bell"));
        this.lst_sounds.add(getSoundModel(8, "Simple droplet"));
        this.lst_sounds.add(getSoundModel(9, "Tiny droplet"));
    }

    public SoundModel getSoundModel(int index, String name) {
        SoundModel soundModel = new SoundModel();
        soundModel.setId(index);
        soundModel.setName(name);
        soundModel.isSelected(this.ph.getInt(URLFactory.REMINDER_SOUND) == index);
        return soundModel;
    }

    public void openSoundMenuPicker() {
        this.bottomSheetDialogSound = new BottomSheetDialog(this.act);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_sound_pick, (ViewGroup) null, false);
        RecyclerView soundRecyclerView = (RecyclerView) view.findViewById(R.id.soundRecyclerView);
        this.soundAdapter = new SoundAdapter(this.act, this.lst_sounds, new SoundAdapter.CallBack() {
            public void onClickSelect(SoundModel time, int position) {
                for (int k = 0; k < Screen_Reminder.this.lst_sounds.size(); k++) {
                    Screen_Reminder.this.lst_sounds.get(k).isSelected(false);
                }
                Screen_Reminder.this.lst_sounds.get(position).isSelected(true);
                Screen_Reminder.this.soundAdapter.notifyDataSetChanged();
                Screen_Reminder.this.ph.savePreferences(URLFactory.REMINDER_SOUND, position);
                if (position > 0) {
                    Screen_Reminder.this.playSound(position);
                }
            }
        });
        soundRecyclerView.setLayoutManager(new LinearLayoutManager(this.act, RecyclerView.VERTICAL, false));
        soundRecyclerView.setAdapter(this.soundAdapter);
         view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Reminder.this.bottomSheetDialogSound.dismiss();
            }
        });
        this.bottomSheetDialogSound.setContentView(view);
        this.bottomSheetDialogSound.show();
    }

    public void playSound(int idx) {
        MediaPlayer mp = null;
        if (idx == 1) {
            mp = MediaPlayer.create(this, R.raw.bell);
        } else if (idx == 2) {
            mp = MediaPlayer.create(this, R.raw.blop);
        } else if (idx == 3) {
            mp = MediaPlayer.create(this, R.raw.bong);
        } else if (idx == 4) {
            mp = MediaPlayer.create(this, R.raw.click);
        } else if (idx == 5) {
            mp = MediaPlayer.create(this, R.raw.echo_droplet);
        } else if (idx == 6) {
            mp = MediaPlayer.create(this, R.raw.mario_droplet);
        } else if (idx == 7) {
            mp = MediaPlayer.create(this, R.raw.ship_bell);
        } else if (idx == 8) {
            mp = MediaPlayer.create(this, R.raw.simple_droplet);
        } else if (idx == 9) {
            mp = MediaPlayer.create(this, R.raw.tiny_droplet);
        }
        mp.start();
    }

    public void load_alarm() {
        this.alarmModelList.clear();
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details", "AlarmType='M'");
        for (int k = 0; k < arr_data.size(); k++) {
            AlarmModel alarmModel = new AlarmModel();
            alarmModel.setDrinkTime((String) arr_data.get(k).get("AlarmTime"));
            alarmModel.setId((String) arr_data.get(k).get("id"));
            alarmModel.setAlarmId((String) arr_data.get(k).get("AlarmId"));
            alarmModel.setAlarmType((String) arr_data.get(k).get("AlarmType"));
            alarmModel.setAlarmInterval((String) arr_data.get(k).get("AlarmInterval"));
            alarmModel.setIsOff(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("IsOff"))));
            alarmModel.setSunday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Sunday"))));
            alarmModel.setMonday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Monday"))));
            alarmModel.setTuesday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Tuesday"))));
            alarmModel.setWednesday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Wednesday"))));
            alarmModel.setThursday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Thursday"))));
            alarmModel.setFriday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Friday"))));
            alarmModel.setSaturday(Integer.valueOf(Integer.parseInt((String) arr_data.get(k).get("Saturday"))));
            alarmModel.setAlarmSundayId((String) arr_data.get(k).get("SundayAlarmId"));
            alarmModel.setAlarmMondayId((String) arr_data.get(k).get("MondayAlarmId"));
            alarmModel.setAlarmTuesdayId((String) arr_data.get(k).get("TuesdayAlarmId"));
            alarmModel.setAlarmWednesdayId((String) arr_data.get(k).get("WednesdayAlarmId"));
            alarmModel.setAlarmThursdayId((String) arr_data.get(k).get("ThursdayAlarmId"));
            alarmModel.setAlarmFridayId((String) arr_data.get(k).get("FridayAlarmId"));
            alarmModel.setAlarmSaturdayId((String) arr_data.get(k).get("SaturdayAlarmId"));
            this.alarmModelList.add(alarmModel);
        }
        if (this.alarmModelList.size() > 0) {
            this.lbl_no_record_found.setVisibility(View.GONE);
        } else {
            this.lbl_no_record_found.setVisibility(View.VISIBLE);
        }
    }

    public void openTimePicker() {
        TimePickerDialog tpd;
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                int i = hourOfDay;
                int i2 = minute;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                Object obj = "";
                try {
                    Date dt = sdf.parse("" + i + ":" + i2 + ":00");
                    String formatedDate = sdfs.format(dt);
                    Database_Helper database_Helper = Screen_Reminder.this.dh;
                    if (!database_Helper.IS_EXISTS("tbl_alarm_details", "AlarmTime='" + formatedDate + "'")) {
                        int _id = (int) System.currentTimeMillis();
                        int _id_sunday = (int) System.currentTimeMillis();
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 1, i, i2, _id_sunday);
                        int _id_monday = (int) System.currentTimeMillis();
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 2, i, i2, _id_monday);
                        int _id_tuesday = (int) System.currentTimeMillis();
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 3, i, i2, _id_tuesday);
                        int _id_wednesday = (int) System.currentTimeMillis();
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 4, i, i2, _id_wednesday);
                        int _id_thursday = (int) System.currentTimeMillis();
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 5, i, i2, _id_thursday);
                        int _id_friday = (int) System.currentTimeMillis();
                        Date date = dt;
                        MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 6, i, i2, _id_friday);
                        SimpleDateFormat simpleDateFormat = sdf;
                        SimpleDateFormat simpleDateFormat2 = sdfs;
                        try {
                            int _id_saturday = (int) System.currentTimeMillis();
                            MyAlarmManager.scheduleManualRecurringAlarm(Screen_Reminder.this.mContext, 7, i, i2, _id_saturday);
                            ContentValues initialValues = new ContentValues();
                            initialValues.put("AlarmTime", "" + formatedDate);
                            initialValues.put("AlarmId", "" + _id);
                            initialValues.put("SundayAlarmId", "" + _id_sunday);
                            initialValues.put("MondayAlarmId", "" + _id_monday);
                            initialValues.put("TuesdayAlarmId", "" + _id_tuesday);
                            initialValues.put("WednesdayAlarmId", "" + _id_wednesday);
                            initialValues.put("ThursdayAlarmId", "" + _id_thursday);
                            initialValues.put("FridayAlarmId", "" + _id_friday);
                            initialValues.put("SaturdayAlarmId", "" + _id_saturday);
                            initialValues.put("AlarmType", "M");
                            initialValues.put("AlarmInterval", "0");
                            Screen_Reminder.this.dh.INSERT("tbl_alarm_details", initialValues);
                            Screen_Reminder.this.load_alarm();
                            Screen_Reminder.this.alarmAdapter.notifyDataSetChanged();
                            Screen_Reminder.this.ah.customAlert(Screen_Reminder.this.sh.get_string(R.string.str_successfully_set_alarm));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        SimpleDateFormat simpleDateFormat3 = sdf;
                        SimpleDateFormat simpleDateFormat4 = sdfs;
                        Screen_Reminder.this.ah.customAlert(Screen_Reminder.this.sh.get_string(R.string.str_set_alarm_validation));
                    }
                } catch (ParseException e2) {
                    SimpleDateFormat simpleDateFormat5 = sdf;
                    SimpleDateFormat simpleDateFormat6 = sdfs;
                    e2.printStackTrace();
                }
            }
        };
        Calendar now = Calendar.getInstance();
        if (!DateFormat.is24HourFormat(this.act)) {
            tpd = TimePickerDialog.newInstance(onTimeSetListener, now.get(11), now.get(12), false);
            tpd.setSelectableTimes(generateTimepoints(23.5d, 15));
            tpd.setMaxTime(23, 30, 0);
        } else {
            tpd = TimePickerDialog.newInstance(onTimeSetListener, now.get(11), now.get(12), true);
            tpd.setSelectableTimes(generateTimepoints(23.5d, 15));
            tpd.setMaxTime(23, 30, 0);
        }
        tpd.show(getSupportFragmentManager(), "Datepickerdialog");
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(this.mContext));
    }
}
