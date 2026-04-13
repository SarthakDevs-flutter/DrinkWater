package com.trending.water.drinking.reminder;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.trending.water.drinking.reminder.appbasiclibs.utils.String_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.mywidgets.NewAppWidget;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_Splash extends MasterBaseAppCompatActivity {
    Handler handler;
    ImageView img_splash_logo;
    int millisecond = 1000;
    Runnable runnable;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.screen_splash);
        this.img_splash_logo = (ImageView) findViewById(R.id.img_splash_logo);
        Intent intent = new Intent(this.act, NewAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", AppWidgetManager.getInstance(this.act).getAppWidgetIds(new ComponentName(this.act, NewAppWidget.class)));
        this.act.sendBroadcast(intent);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.ph.getFloat(URLFactory.DAILY_WATER) == 0.0f) {
            URLFactory.DAILY_WATER_VALUE = 2500.0f;
        } else {
            URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.DAILY_WATER);
        }
        String_Helper string_Helper = this.sh;
        if (string_Helper.check_blank_data("" + this.ph.getString(URLFactory.WATER_UNIT))) {
            URLFactory.WATER_UNIT_VALUE = "ml";
        } else {
            URLFactory.WATER_UNIT_VALUE = this.ph.getString(URLFactory.WATER_UNIT);
        }
        this.runnable = new Runnable() {
            public void run() {
                if (Screen_Splash.this.ph.getBoolean(URLFactory.HIDE_WELCOME_SCREEN)) {
                    Screen_Splash.this.intent = new Intent(Screen_Splash.this, Screen_Dashboard.class);
                } else {
                    Screen_Splash.this.ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT, true);
                    Screen_Splash.this.ph.savePreferences(URLFactory.PERSON_WEIGHT, "80");
                    Screen_Splash.this.ph.savePreferences(URLFactory.USER_NAME, "");
                    Screen_Splash.this.intent = new Intent(Screen_Splash.this, Screen_OnBoarding.class);
                }
                Screen_Splash.this.startActivity(Screen_Splash.this.intent);
                Screen_Splash.this.finish();
            }
        };
        this.handler = new Handler();
        this.handler.postDelayed(this.runnable, (long) this.millisecond);
    }
}
