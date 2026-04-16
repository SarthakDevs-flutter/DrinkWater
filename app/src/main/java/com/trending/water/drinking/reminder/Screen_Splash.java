package com.trending.water.drinking.reminder;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.databinding.ScreenSplashBinding;
import com.trending.water.drinking.reminder.mywidgets.NewAppWidget;
import com.trending.water.drinking.reminder.utils.URLFactory;
import android.view.LayoutInflater;

public class Screen_Splash extends MasterBaseAppCompatActivity<ScreenSplashBinding> {

    @Override
    protected ScreenSplashBinding inflateBinding(LayoutInflater inflater) {
        return ScreenSplashBinding.inflate(inflater);
    }

    private static final int SPLASH_DELAY = 1000;

    private Handler splashHandler;
    private Runnable splashRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWidgets();
    }

    private void updateWidgets() {
        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAppData();
        startSplashScreen();
    }

    private void initAppData() {
        float dailyWaterGoal = preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 0.0f);
        if (dailyWaterGoal == 0.0f) {
            URLFactory.dailyWaterValue = 2500.0f;
        } else {
            URLFactory.dailyWaterValue = dailyWaterGoal;
        }

        String unit = preferencesHelper.getString(URLFactory.KEY_WATER_UNIT, "");
        if (stringHelper.check_blank_data(unit)) {
            URLFactory.waterUnitValue = "ML";
        } else {
            URLFactory.waterUnitValue = unit;
        }
    }

    private void startSplashScreen() {
        splashRunnable = () -> {
            Intent nextIntent;
            if (preferencesHelper.getBoolean(URLFactory.KEY_HIDE_WELCOME_SCREEN, false)) {
                nextIntent = new Intent(this, Screen_Dashboard.class);
            } else {
                // Initial setup for first-time users
                preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
                preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT, "80");
                preferencesHelper.savePreferences(URLFactory.KEY_USER_NAME, "");
                nextIntent = new Intent(this, Screen_OnBoarding.class);
            }
            startActivity(nextIntent);
            finish();
        };

        splashHandler = new Handler(Looper.getMainLooper());
        splashHandler.postDelayed(splashRunnable, SPLASH_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (splashHandler != null && splashRunnable != null) {
            splashHandler.removeCallbacks(splashRunnable);
        }
    }
}
