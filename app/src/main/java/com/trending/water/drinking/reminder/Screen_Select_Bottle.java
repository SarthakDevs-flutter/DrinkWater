package com.trending.water.drinking.reminder;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.trending.water.drinking.reminder.appbasiclibs.AppClose;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.String_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.model.Container;
import com.trending.water.drinking.reminder.mywidgets.NewAppWidget;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class Screen_Select_Bottle extends MasterBaseActivity {
    ArrayList<Container> containerArrayList = new ArrayList<>();
    float drink_water = 0.0f;
    int selected_pos = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_select_bottle);
        getWindow().setLayout(-1, -1);
        ((NotificationManager) this.act.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
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
        FindViewById();
        Body();
    }

    private void FindViewById() {
    }

    public void saveDefaultContainer() {
        String str;
        if (!this.ph.getBoolean(URLFactory.HIDE_WELCOME_SCREEN)) {
            this.ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT, true);
            this.ph.savePreferences(URLFactory.PERSON_WEIGHT, "80");
            this.ph.savePreferences(URLFactory.USER_NAME, "");
            this.intent = new Intent(this.act, Screen_OnBoarding.class);
            startActivity(this.intent);
            finish();
            return;
        }
        boolean addRecord = true;
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            str = this.sh.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1", "8000 ml");
        } else {
            str = this.sh.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1", "270 fl oz");
        }
        Database_Helper database_Helper = this.dh;
        ArrayList<HashMap<String, String>> arr_data = database_Helper.getdata("tbl_drink_details", "DrinkDate ='" + this.dth.getCurrentDate(URLFactory.DATE_FORMAT) + "'");
        this.drink_water = 0.0f;
        for (int k = 0; k < arr_data.size(); k++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                this.drink_water = (float) (((double) this.drink_water) + Double.parseDouble("" + ((String) arr_data.get(k).get("ContainerValue"))));
            } else {
                this.drink_water = (float) (((double) this.drink_water) + Double.parseDouble("" + ((String) arr_data.get(k).get("ContainerValueOZ"))));
            }
        }
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") && this.drink_water > 8000.0f) {
            this.ah.customAlert(str);
            addRecord = false;
        } else if (!URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") && this.drink_water > 270.0f) {
            this.ah.customAlert(str);
            addRecord = false;
        }
        float count_drink_after_add_current_water = this.drink_water;
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            count_drink_after_add_current_water += Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValue());
        } else if (!URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            count_drink_after_add_current_water += Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValueOZ());
        }
        if (!URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") || count_drink_after_add_current_water <= 8000.0f) {
            if (!URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") && count_drink_after_add_current_water > 270.0f) {
                if (this.drink_water >= 270.0f) {
                    this.ah.customAlert(str);
                } else {
                    float f = URLFactory.DAILY_WATER_VALUE;
                    if (f < 270.0f - Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValueOZ())) {
                        this.ah.customAlert(str);
                    }
                }
            }
        } else if (this.drink_water >= 8000.0f) {
            this.ah.customAlert(str);
        } else {
            float f2 = URLFactory.DAILY_WATER_VALUE;
            if (f2 < 8000.0f - Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValue())) {
                this.ah.customAlert(str);
            }
        }
        if (this.drink_water == 8000.0f && URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            addRecord = false;
        } else if (this.drink_water == 270.0f && !URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            addRecord = false;
        }
        if (addRecord) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("ContainerValue", "" + this.containerArrayList.get(this.selected_pos).getContainerValue());
            initialValues.put("ContainerValueOZ", "" + this.containerArrayList.get(this.selected_pos).getContainerValueOZ());
            initialValues.put("ContainerMeasure", "" + this.ph.getString(URLFactory.WATER_UNIT));
            initialValues.put("DrinkDate", "" + this.dth.getCurrentDate("dd-MM-yyyy"));
            initialValues.put("DrinkTime", "" + this.dth.getCurrentTime(true));
            initialValues.put("DrinkDateTime", "" + this.dth.getCurrentDate("dd-MM-yyyy HH:mm:ss"));
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                initialValues.put("TodayGoal", "" + URLFactory.DAILY_WATER_VALUE);
                initialValues.put("TodayGoalOZ", "" + HeightWeightHelper.mlToOzConverter((double) URLFactory.DAILY_WATER_VALUE));
            } else {
                initialValues.put("TodayGoal", "" + HeightWeightHelper.ozToMlConverter((double) URLFactory.DAILY_WATER_VALUE));
                initialValues.put("TodayGoalOZ", "" + URLFactory.DAILY_WATER_VALUE);
            }
            this.dh.INSERT("tbl_drink_details", initialValues);
        }
        Intent intent = new Intent(this.act, NewAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", AppWidgetManager.getInstance(this.act).getAppWidgetIds(new ComponentName(this.act, NewAppWidget.class)));
        this.act.sendBroadcast(intent);
        AppClose.exitApplication(this.mContext);
    }

    private void Body() {
        String selected_container_id;
        if (this.ph.getInt(URLFactory.SELECTED_CONTAINER) == 0) {
            selected_container_id = "1";
        } else {
            selected_container_id = "" + this.ph.getInt(URLFactory.SELECTED_CONTAINER);
        }
        ArrayList<HashMap<String, String>> arr_container = this.dh.getdata("tbl_container_details", "IsCustom", 1);
        for (int k = 0; k < arr_container.size(); k++) {
            Container container = new Container();
            container.setContainerId((String) arr_container.get(k).get("ContainerID"));
            container.setContainerValue((String) arr_container.get(k).get("ContainerValue"));
            container.setContainerValueOZ((String) arr_container.get(k).get("ContainerValueOZ"));
            container.isOpen(((String) arr_container.get(k).get("IsOpen")).equalsIgnoreCase("1"));
            container.isSelected(selected_container_id.equalsIgnoreCase((String) arr_container.get(k).get("ContainerID")));
            if (container.isSelected()) {
                this.selected_pos = k;
            }
            this.containerArrayList.add(container);
        }
        saveDefaultContainer();
    }
}
