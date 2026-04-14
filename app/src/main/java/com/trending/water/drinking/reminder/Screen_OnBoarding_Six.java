package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;
import java.util.Locale;

public class Screen_OnBoarding_Six extends MasterBaseFragment {

    private int fromHour = 8;
    private int fromMinute = 0;
    private int toHour = 22;
    private int toMinute = 0;

    private AppCompatTextView txtWakeupTime;
    private AppCompatTextView txtBedTime;
    private AppCompatTextView lblMessage;

    private RadioButton rdo15;
    private RadioButton rdo30;
    private RadioButton rdo45;
    private RadioButton rdo60;

    private View itemView;

    public static Timepoint[] generateTimepoints(double maxHour, int minutesInterval) {
        int lastValueInMinutes = (int) (60.0d * maxHour);
        List<Timepoint> timepoints = new ArrayList<>();
        int currentMinute = 0;
        while (currentMinute <= lastValueInMinutes) {
            int h = currentMinute / 60;
            int m = currentMinute % 60;
            if (h < 24) {
                timepoints.add(new Timepoint(h, m));
            }
            currentMinute += minutesInterval;
        }
        return timepoints.toArray(new Timepoint[0]);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.screen_onboarding_six, container, false);
        findViewByIds(itemView);
        initView();
        calculateIntervalConsumption();
        return itemView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            calculateIntervalConsumption();
        }
    }

    private void findViewByIds(View view) {
        txtWakeupTime = view.findViewById(R.id.txt_wakeup_time);
        txtBedTime = view.findViewById(R.id.txt_bed_time);
        rdo15 = view.findViewById(R.id.rdo_15);
        rdo30 = view.findViewById(R.id.rdo_30);
        rdo45 = view.findViewById(R.id.rdo_45);
        rdo60 = view.findViewById(R.id.rdo_60);
        lblMessage = view.findViewById(R.id.lbl_message);
    }

    private void initView() {
        String minStr = " " + stringHelper.getString(R.string.str_min);
        String hourStr = " " + stringHelper.getString(R.string.str_hour);

        rdo15.setText("15" + minStr);
        rdo30.setText("30" + minStr);
        rdo45.setText("45" + minStr);
        rdo60.setText("1" + hourStr);

        txtWakeupTime.setOnClickListener(v -> openTimePicker(txtWakeupTime, true));
        txtBedTime.setOnClickListener(v -> openTimePicker(txtBedTime, false));

        View.OnClickListener intervalListener = v -> calculateIntervalConsumption();
        rdo15.setOnClickListener(intervalListener);
        rdo30.setOnClickListener(intervalListener);
        rdo45.setOnClickListener(intervalListener);
        rdo60.setOnClickListener(intervalListener);
    }

    private void openTimePicker(final AppCompatTextView textView, final boolean isFrom) {
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute, second) -> {
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss", Locale.US);
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.US);
            try {
                if (isFrom) {
                    fromHour = hourOfDay;
                    fromMinute = minute;
                } else {
                    toHour = hourOfDay;
                    toMinute = minute;
                }
                String formattedTime = sdf12.format(sdf24.parse(hourOfDay + ":" + minute + ":00"));
                textView.setText(formattedTime);
                calculateIntervalConsumption();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

        int h = isFrom ? fromHour : toHour;
        int m = isFrom ? fromMinute : toMinute;

        TimePickerDialog tpd = TimePickerDialog.newInstance(listener, h, m, DateFormat.is24HourFormat(mContext));
        tpd.setSelectableTimes(generateTimepoints(23.5d, 30));
        tpd.setMaxTime(23, 30, 0);
        tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        tpd.show(getChildFragmentManager(), "TimePickerDialog");
    }

    private boolean isNextDayEnd() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        try {
            long wakeup = sdf.parse(txtWakeupTime.getText().toString().trim()).getTime();
            long bed = sdf.parse(txtBedTime.getText().toString().trim()).getTime();
            return wakeup > bed;
        } catch (Exception e) {
            return false;
        }
    }

    private void calculateIntervalConsumption() {
        if (!isAdded()) return;

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, fromHour);
        startTime.set(Calendar.MINUTE, fromMinute);
        startTime.set(Calendar.SECOND, 0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, toHour);
        endTime.set(Calendar.MINUTE, toMinute);
        endTime.set(Calendar.SECOND, 0);

        if (isNextDayEnd()) {
            endTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        long diffMills = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        float totalMinutes = diffMills / 60000f;

        int interval = rdo15.isChecked() ? 15 : rdo30.isChecked() ? 30 : rdo45.isChecked() ? 45 : 60;
        int consumptionPerInterval = 0;

        if (totalMinutes > 0) {
            consumptionPerInterval = Math.round(URLFactory.dailyWaterValue / (totalMinutes / (float) interval));
        }

        String unit = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true) ? "ML" : "FL OZ";
        String goalMsg = stringHelper.getString(R.string.str_goal_consume);
        goalMsg = goalMsg.replace("$1", consumptionPerInterval + " " + unit)
                .replace("$2", (int) URLFactory.dailyWaterValue + " " + unit);

        lblMessage.setText(goalMsg);

        // Save preferences
        preferencesHelper.savePreferences(URLFactory.KEY_WAKE_UP_TIME, txtWakeupTime.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_WAKE_UP_HOUR, fromHour);
        preferencesHelper.savePreferences(URLFactory.KEY_WAKE_UP_MINUTE, fromMinute);
        preferencesHelper.savePreferences(URLFactory.KEY_BED_TIME, txtBedTime.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_BED_TIME_HOUR, toHour);
        preferencesHelper.savePreferences(URLFactory.KEY_BED_TIME_MINUTE, toMinute);
        preferencesHelper.savePreferences(URLFactory.KEY_INTERVAL, interval);

        boolean ignoreStep = (consumptionPerInterval > (int) URLFactory.dailyWaterValue) || (consumptionPerInterval == 0);
        preferencesHelper.savePreferences(URLFactory.KEY_IGNORE_NEXT_STEP, ignoreStep);
    }
}
