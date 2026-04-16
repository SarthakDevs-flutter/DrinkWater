package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.databinding.ScreenOnboardingSixBinding;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Six extends MasterBaseFragment<ScreenOnboardingSixBinding> {

    @Override
    protected ScreenOnboardingSixBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenOnboardingSixBinding.inflate(inflater, container, false);
    }

    private int fromHour = 8;
    private int fromMinute = 0;
    private int toHour = 22;
    private int toMinute = 0;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        calculateIntervalConsumption();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            calculateIntervalConsumption();
        }
    }


    private void initView() {
        String minStr = " " + stringHelper.getString(R.string.str_min);
        String hourStr = " " + stringHelper.getString(R.string.str_hour);

        binding.rdo15.setText("15" + minStr);
        binding.rdo30.setText("30" + minStr);
        binding.rdo45.setText("45" + minStr);
        binding.rdo60.setText("1" + hourStr);

        binding.txtWakeupTime.setOnClickListener(v -> openTimePicker(binding.txtWakeupTime, true));
        binding.txtBedTime.setOnClickListener(v -> openTimePicker(binding.txtBedTime, false));

        View.OnClickListener intervalListener = v -> calculateIntervalConsumption();
        binding.rdo15.setOnClickListener(intervalListener);
        binding.rdo30.setOnClickListener(intervalListener);
        binding.rdo45.setOnClickListener(intervalListener);
        binding.rdo60.setOnClickListener(intervalListener);
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
            long wakeup = sdf.parse(binding.txtWakeupTime.getText().toString().trim()).getTime();
            long bed = sdf.parse(binding.txtBedTime.getText().toString().trim()).getTime();
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

        int interval = binding.rdo15.isChecked() ? 15 : binding.rdo30.isChecked() ? 30 : binding.rdo45.isChecked() ? 45 : 60;
        int consumptionPerInterval = 0;

        if (totalMinutes > 0) {
            consumptionPerInterval = Math.round(URLFactory.dailyWaterValue / (totalMinutes / (float) interval));
        }

        String unit = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true) ? "ML" : "FL OZ";
        String goalMsg = stringHelper.getString(R.string.str_goal_consume);
        goalMsg = goalMsg.replace("$1", consumptionPerInterval + " " + unit)
                .replace("$2", (int) URLFactory.dailyWaterValue + " " + unit);

        binding.lblMessage.setText(goalMsg);

        // Save preferences
        preferencesHelper.savePreferences(URLFactory.KEY_WAKE_UP_TIME, binding.txtWakeupTime.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_WAKE_UP_HOUR, fromHour);
        preferencesHelper.savePreferences(URLFactory.KEY_WAKE_UP_MINUTE, fromMinute);
        preferencesHelper.savePreferences(URLFactory.KEY_BED_TIME, binding.txtBedTime.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_BED_TIME_HOUR, toHour);
        preferencesHelper.savePreferences(URLFactory.KEY_BED_TIME_MINUTE, toMinute);
        preferencesHelper.savePreferences(URLFactory.KEY_INTERVAL, interval);

        boolean ignoreStep = (consumptionPerInterval > (int) URLFactory.dailyWaterValue) || (consumptionPerInterval == 0);
        preferencesHelper.savePreferences(URLFactory.KEY_IGNORE_NEXT_STEP, ignoreStep);
    }
}
