package com.trending.water.drinking.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.Locale;

/**
 * Headless-style activity to handle snooze action from notification.
 * Provides options for 5, 10, and 15 minutes.
 */
public class Screen_Select_Snooze extends MasterBaseActivity {

    private AppCompatTextView lblSnooze5;
    private AppCompatTextView lblSnooze10;
    private AppCompatTextView lblSnooze15;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_select_snooze);

        if (getWindow() != null) {
            getWindow().setLayout(-1, -1);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.cancel(0);

        findViewByIds();
        initView();
        setupListeners();
    }

    private void findViewByIds() {
        lblSnooze5 = findViewById(R.id.one);
        lblSnooze10 = findViewById(R.id.two);
        lblSnooze15 = findViewById(R.id.three);
    }

    private void initView() {
        String minStr = stringHelper.getString(R.string.str_minutes);
        lblSnooze5.setText(String.format(Locale.getDefault(), "5 %s", minStr));
        lblSnooze10.setText(String.format(Locale.getDefault(), "10 %s", minStr));
        lblSnooze15.setText(String.format(Locale.getDefault(), "15 %s", minStr));
    }

    private void setupListeners() {
        lblSnooze5.setOnClickListener(v -> handleSnooze(5));
        lblSnooze10.setOnClickListener(v -> handleSnooze(10));
        lblSnooze15.setOnClickListener(v -> handleSnooze(15));
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
