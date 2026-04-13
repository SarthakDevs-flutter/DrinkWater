package com.trending.water.drinking.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.receiver.AlarmReceiver;

import java.util.Calendar;

public class Screen_Select_Snooze extends MasterBaseActivity {
    AppCompatTextView one;
    AppCompatTextView three;
    AppCompatTextView two;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_select_snooze);
        getWindow().setLayout(-1, -1);
        ((NotificationManager) this.act.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
        FindViewById();
        Body();
    }

    private void FindViewById() {
        this.one = (AppCompatTextView) findViewById(R.id.one);
        this.two = (AppCompatTextView) findViewById(R.id.two);
        this.three = (AppCompatTextView) findViewById(R.id.three);
    }

    private void Body() {
        AppCompatTextView appCompatTextView = this.one;
        appCompatTextView.setText("5 " + this.sh.get_string(R.string.str_minutes));
        AppCompatTextView appCompatTextView2 = this.two;
        appCompatTextView2.setText("10 " + this.sh.get_string(R.string.str_minutes));
        AppCompatTextView appCompatTextView3 = this.three;
        appCompatTextView3.setText("15 " + this.sh.get_string(R.string.str_minutes));
        this.one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Select_Snooze.this.setSnooze(5);
                Screen_Select_Snooze.this.finish();
            }
        });
        this.two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Select_Snooze.this.setSnooze(10);
                Screen_Select_Snooze.this.finish();
            }
        });
        this.three.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Select_Snooze.this.setSnooze(15);
                Screen_Select_Snooze.this.finish();
            }
        });
    }

    public void setSnooze(int minutes) {
        ((AlarmManager) this.act.getSystemService(Context.ALARM_SERVICE)).setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + ((long) (60000 * minutes)), PendingIntent.getBroadcast(this.act, 0, new Intent(this.act, AlarmReceiver.class), PendingIntent.FLAG_IMMUTABLE));
    }
}
