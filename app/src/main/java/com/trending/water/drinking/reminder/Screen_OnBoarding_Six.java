package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatTextView;

import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.utils.URLFactory;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Screen_OnBoarding_Six extends MasterBaseFragment {
    int from_hour = 8;
    int from_minute = 0;
    View item_view;
    AppCompatTextView lbl_message;
    RadioButton rdo_15;
    RadioButton rdo_30;
    RadioButton rdo_45;
    RadioButton rdo_60;
    int to_hour = 22;
    int to_minute = 0;
    AppCompatTextView txt_bed_time;
    AppCompatTextView txt_wakeup_time;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_onboarding_six, container, false);
        FindViewById();
        Body();
        setCount();
        return this.item_view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setCount();
        }
    }

    private void FindViewById() {
        this.txt_wakeup_time = (AppCompatTextView) this.item_view.findViewById(R.id.txt_wakeup_time);
        this.txt_bed_time = (AppCompatTextView) this.item_view.findViewById(R.id.txt_bed_time);
        this.rdo_15 = (RadioButton) this.item_view.findViewById(R.id.rdo_15);
        this.rdo_30 = (RadioButton) this.item_view.findViewById(R.id.rdo_30);
        this.rdo_45 = (RadioButton) this.item_view.findViewById(R.id.rdo_45);
        this.rdo_60 = (RadioButton) this.item_view.findViewById(R.id.rdo_60);
        this.lbl_message = (AppCompatTextView) this.item_view.findViewById(R.id.lbl_message);
    }

    private void Body() {
        RadioButton radioButton = this.rdo_15;
        radioButton.setText("15 " + this.sh.get_string(R.string.str_min));
        RadioButton radioButton2 = this.rdo_30;
        radioButton2.setText("30 " + this.sh.get_string(R.string.str_min));
        RadioButton radioButton3 = this.rdo_45;
        radioButton3.setText("45 " + this.sh.get_string(R.string.str_min));
        RadioButton radioButton4 = this.rdo_60;
        radioButton4.setText("1 " + this.sh.get_string(R.string.str_hour));
        this.txt_wakeup_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_OnBoarding_Six.this.openAutoTimePicker(Screen_OnBoarding_Six.this.txt_wakeup_time, true);
            }
        });
        this.txt_bed_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_OnBoarding_Six.this.openAutoTimePicker(Screen_OnBoarding_Six.this.txt_bed_time, false);
            }
        });
        this.rdo_15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_OnBoarding_Six.this.setCount();
            }
        });
        this.rdo_30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_OnBoarding_Six.this.setCount();
            }
        });
        this.rdo_45.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_OnBoarding_Six.this.setCount();
            }
        });
        this.rdo_60.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_OnBoarding_Six.this.setCount();
            }
        });
    }

    public void openAutoTimePicker(final AppCompatTextView appCompatTextView, final boolean isFrom) {
        TimePickerDialog tpd;
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                try {
                    if (isFrom) {
                        Screen_OnBoarding_Six.this.from_hour = hourOfDay;
                        Screen_OnBoarding_Six.this.from_minute = minute;
                    } else {
                        Screen_OnBoarding_Six.this.to_hour = hourOfDay;
                        Screen_OnBoarding_Six.this.to_minute = minute;
                    }
                    String formatedDate = sdfs.format(sdf.parse("" + hourOfDay + ":" + minute + ":00"));
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(formatedDate);
                    appCompatTextView.setText(sb.toString());
                    Screen_OnBoarding_Six.this.setCount();
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
//        tpd.show(this.act.getFragmentManager(), "Datepickerdialog");
        tpd.show(getChildFragmentManager(), "Datepickerdialog");
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(this.mContext));
    }

    public int getDifference() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            long difference = simpleDateFormat.parse(this.txt_bed_time.getText().toString().trim()).getTime() - simpleDateFormat.parse(this.txt_wakeup_time.getText().toString().trim()).getTime();
            int days = (int) (difference / 86400000);
            return ((int) ((difference - ((long) (86400000 * days))) - ((long) (3600000 * ((int) ((difference - ((long) (days * 86400000))) / 3600000)))))) / 60000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isValidDate() {
        if ((this.rdo_15.isChecked() ? 15 : this.rdo_30.isChecked() ? 30 : this.rdo_45.isChecked() ? 45 : 60) <= getDifference()) {
            return true;
        }
        return false;
    }

    public boolean isNextDayEnd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            if (simpleDateFormat.parse(this.txt_wakeup_time.getText().toString().trim()).getTime() > simpleDateFormat.parse(this.txt_bed_time.getText().toString().trim()).getTime()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void setCount() {
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
        long mills = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        float total_minutes = (float) ((((int) (mills / 3600000)) * 60) + ((int) ((mills / 60000) % 60)));
        int interval = this.rdo_15.isChecked() ? 15 : this.rdo_30.isChecked() ? 30 : this.rdo_45.isChecked() ? 45 : 60;
        int consume = 0;
        if (total_minutes > 0.0f) {
            consume = Math.round(URLFactory.DAILY_WATER_VALUE / (total_minutes / ((float) interval)));
        }
        String unit = this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz";
        AppCompatTextView appCompatTextView = this.lbl_message;
        String str = this.sh.get_string(R.string.str_goal_consume);
        String replace = str.replace("$1", "" + consume + " " + unit);
        appCompatTextView.setText(replace.replace("$2", "" + ((int) URLFactory.DAILY_WATER_VALUE) + " " + unit));
        this.ph.savePreferences(URLFactory.WAKE_UP_TIME, this.txt_wakeup_time.getText().toString().trim());
        this.ph.savePreferences(URLFactory.WAKE_UP_TIME_HOUR, this.from_hour);
        this.ph.savePreferences(URLFactory.WAKE_UP_TIME_MINUTE, this.from_minute);
        this.ph.savePreferences(URLFactory.BED_TIME, this.txt_bed_time.getText().toString().trim());
        this.ph.savePreferences(URLFactory.BED_TIME_HOUR, this.to_hour);
        this.ph.savePreferences(URLFactory.BED_TIME_MINUTE, this.to_minute);
        this.ph.savePreferences(URLFactory.INTERVAL, interval);
        if (consume > ((int) URLFactory.DAILY_WATER_VALUE)) {
            this.ph.savePreferences(URLFactory.IGNORE_NEXT_STEP, true);
        } else if (consume == 0) {
            this.ph.savePreferences(URLFactory.IGNORE_NEXT_STEP, true);
        } else {
            this.ph.savePreferences(URLFactory.IGNORE_NEXT_STEP, false);
        }
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("hh:mm aa", Locale.US).parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
}
