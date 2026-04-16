package com.trending.water.drinking.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.databinding.ScreenSelectSnoozeBinding;
import com.trending.water.drinking.reminder.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.Locale;

public class Screen_Select_Snooze extends MasterBaseActivity<ScreenSelectSnoozeBinding> {

    @Override
    protected ScreenSelectSnoozeBinding inflateBinding(LayoutInflater inflater) {
        return ScreenSelectSnoozeBinding.inflate(inflater);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getWindow() != null) {
            getWindow().setLayout(-1, -1);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.cancel(0);

        initView();
        setupListeners();
    }


    private void initView() {
        String minStr = stringHelper.getString(R.string.str_minutes);
        binding.one.setText(String.format(Locale.getDefault(), "5 %s", minStr));
        binding.two.setText(String.format(Locale.getDefault(), "10 %s", minStr));
        binding.three.setText(String.format(Locale.getDefault(), "15 %s", minStr));
    }

    private void setupListeners() {
        binding.one.setOnClickListener(v -> handleSnooze(5));
        binding.two.setOnClickListener(v -> handleSnooze(10));
        binding.three.setOnClickListener(v -> handleSnooze(15));
    }

    private void handleSnooze(int minutes) {
        setSnoozeAlarm(minutes);
        finish();
    }

    private void setSnoozeAlarm(int minutes) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        long triggerAtMillis = Calendar.getInstance().getTimeInMillis() + ((long) minutes * 60 * 1000);
        am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
    }
}
