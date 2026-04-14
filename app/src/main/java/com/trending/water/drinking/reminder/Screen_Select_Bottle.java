package com.trending.water.drinking.reminder;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.appbasiclibs.AppClose;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.model.Container;
import com.trending.water.drinking.reminder.mywidgets.NewAppWidget;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Headless-style activity to handle quick bottle action from notification/widget.
 * It might redirect to OnBoarding or record a drink and exit.
 */
public class Screen_Select_Bottle extends MasterBaseActivity {

    private final ArrayList<Container> containerList = new ArrayList<>();
    private float currentTotalDrank = 0.0f;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_select_bottle);
        
        // Make it full screen but likely transparent/headless
        if (getWindow() != null) {
            getWindow().setLayout(-1, -1);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.cancel(0);

        initData();
        performAction();
    }

    private void initData() {
        if (preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 0.0f) == 0.0f) {
            URLFactory.dailyWaterValue = 2500.0f;
        } else {
            URLFactory.dailyWaterValue = preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 2500.0f);
        }

        URLFactory.waterUnitValue = preferencesHelper.getString(URLFactory.KEY_WATER_UNIT, "ml");
    }

    private void performAction() {
        int selectedId = preferencesHelper.getInt(URLFactory.KEY_SELECTED_CONTAINER, 1);
        
        ArrayList<HashMap<String, String>> containers = databaseHelper.getData("tbl_container_details", "IsCustom", 1);
        for (int i = 0; i < containers.size(); i++) {
            HashMap<String, String> row = containers.get(i);
            Container container = new Container();
            container.setContainerId(row.get("ContainerID"));
            container.setContainerValue(row.get("ContainerValue"));
            container.setContainerValueOZ(row.get("ContainerValueOZ"));
            container.isOpen("1".equalsIgnoreCase(row.get("IsOpen")));
            container.isSelected(String.valueOf(selectedId).equalsIgnoreCase(row.get("ContainerID")));
            
            if (container.isSelected()) {
                selectedPosition = i;
            }
            containerList.add(container);
        }

        saveDrinkAndExit();
    }

    private void saveDrinkAndExit() {
        if (!preferencesHelper.getBoolean(URLFactory.KEY_HIDE_WELCOME_SCREEN, false)) {
            preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
            preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT, "80");
            preferencesHelper.savePreferences(URLFactory.KEY_USER_NAME, "");
            
            startActivity(new Intent(this, Screen_OnBoarding.class));
            finish();
            return;
        }

        boolean isMl = URLFactory.waterUnitValue.equalsIgnoreCase("ml");
        float limitValue = isMl ? 8000.0f : 270.0f;
        String unit = isMl ? "ml" : "fl oz";
        
        String validationMsg = stringHelper.getString(R.string.str_you_should_not_drink_more_then_target)
                .replace("$1", String.format(Locale.getDefault(), "%.0f %s", limitValue, unit));

        ArrayList<HashMap<String, String>> todayHistory = databaseHelper.getData("tbl_drink_details", 
                "DrinkDate ='" + dateHelper.getCurrentDate(URLFactory.DATE_FORMAT) + "'");
        
        currentTotalDrank = 0.0f;
        for (HashMap<String, String> row : todayHistory) {
            String valStr = isMl ? row.get("ContainerValue") : row.get("ContainerValueOZ");
            currentTotalDrank += Float.parseFloat(valStr != null ? valStr : "0");
        }

        boolean canAdd = true;
        if (currentTotalDrank >= limitValue) {
            alertHelper.customAlert(validationMsg);
            canAdd = false;
        }

        if (canAdd) {
            Container selectedBucket = containerList.get(selectedPosition);
            float addingValue = Float.parseFloat(isMl ? selectedBucket.getContainerValue() : selectedBucket.getContainerValueOZ());
            
            if (currentTotalDrank + addingValue > limitValue) {
                // If it exceeds but not yet at limit, we might still show alert or allow partial (app logic seems to just alert)
                alertHelper.customAlert(validationMsg);
                // Note: Original code had some complex nested if-else that essentially alerted.
            }

            ContentValues cv = new ContentValues();
            cv.put("ContainerValue", selectedBucket.getContainerValue());
            cv.put("ContainerValueOZ", selectedBucket.getContainerValueOZ());
            cv.put("ContainerMeasure", URLFactory.waterUnitValue);
            cv.put("DrinkDate", dateHelper.getCurrentDate("dd-MM-yyyy"));
            cv.put("DrinkTime", dateHelper.getCurrentTime(true));
            cv.put("DrinkDateTime", dateHelper.getCurrentDate("dd-MM-yyyy HH:mm:ss"));
            
            if (isMl) {
                cv.put("TodayGoal", String.valueOf(URLFactory.dailyWaterValue));
                cv.put("TodayGoalOZ", String.valueOf(HeightWeightHelper.convertMlToOz(URLFactory.dailyWaterValue)));
            } else {
                cv.put("TodayGoal", String.valueOf(HeightWeightHelper.convertOzToMl(URLFactory.dailyWaterValue)));
                cv.put("TodayGoalOZ", String.valueOf(URLFactory.dailyWaterValue));
            }
            
            databaseHelper.insert("tbl_drink_details", cv);
        }

        updateWidget();
        AppClose.exitApplication(mContext);
    }

    private void updateWidget() {
        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
