package com.trending.water.drinking.reminder;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.trending.water.drinking.reminder.adapter.OnBoardingPagerAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.custom.NonSwipeableViewPager;
import com.trending.water.drinking.reminder.receiver.MyAlarmManager;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Screen_OnBoarding extends MasterBaseAppCompatActivity {
    private static final int ALL_PERMISSION = 3;
    LinearLayout btn_back;
    LinearLayout btn_next;
    int current_page_idx = 0;
    DotsIndicator dots_indicator;
    AppCompatTextView lbl_next;
    int max_page = 7;
    OnBoardingPagerAdapter onBoardingPagerAdapter;
    View space;
    NonSwipeableViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.screen_onboarding);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(this.mContext.getResources().getColor(R.color.water_color));
        }
        FindViewById();
        Body();
    }

    public void FindViewById() {
        this.viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        this.dots_indicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        this.btn_back = (LinearLayout) findViewById(R.id.btn_back);
        this.btn_next = (LinearLayout) findViewById(R.id.btn_next);
        this.lbl_next = (AppCompatTextView) findViewById(R.id.lbl_next);
        this.space = findViewById(R.id.space);
        this.dots_indicator.setDotsClickable(false);
    }

    public void Body() {
        this.onBoardingPagerAdapter = new OnBoardingPagerAdapter(getSupportFragmentManager(), this.mContext);
        this.viewPager.setAdapter(this.onBoardingPagerAdapter);
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                Screen_OnBoarding.this.current_page_idx = position;
                if (position == 0) {
                    Screen_OnBoarding.this.btn_back.setVisibility(View.GONE);
                    Screen_OnBoarding.this.space.setVisibility(View.GONE);
                } else {
                    Screen_OnBoarding.this.btn_back.setVisibility(View.VISIBLE);
                    Screen_OnBoarding.this.space.setVisibility(View.VISIBLE);
                }
                if (position == Screen_OnBoarding.this.max_page - 1) {
                    Screen_OnBoarding.this.lbl_next.setText(Screen_OnBoarding.this.sh.get_string(R.string.str_get_started));
                } else {
                    Screen_OnBoarding.this.lbl_next.setText(Screen_OnBoarding.this.sh.get_string(R.string.str_next));
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.viewPager.setOffscreenPageLimit(10);
        this.dots_indicator.setViewPager(this.viewPager);
        this.btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int i = Screen_OnBoarding.this.current_page_idx;
                Screen_OnBoarding screen_OnBoarding = Screen_OnBoarding.this;
                screen_OnBoarding.current_page_idx--;
                Screen_OnBoarding.this.viewPager.setCurrentItem(Screen_OnBoarding.this.current_page_idx);
            }
        });
        this.btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_OnBoarding.this.current_page_idx == 0) {
                    if (Screen_OnBoarding.this.sh.check_blank_data(Screen_OnBoarding.this.ph.getString(URLFactory.USER_NAME))) {
                        Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_your_name_validation));
                        return;
                    } else if (Screen_OnBoarding.this.ph.getString(URLFactory.USER_NAME).length() < 3) {
                        Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_valid_name_validation));
                        return;
                    }
                }
                if (Screen_OnBoarding.this.current_page_idx == 1) {
                    try {
                        if (Screen_OnBoarding.this.sh.check_blank_data(Screen_OnBoarding.this.ph.getString(URLFactory.PERSON_HEIGHT))) {
                            Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_height_validation));
                            return;
                        } else if (Screen_OnBoarding.this.sh.check_blank_data(Screen_OnBoarding.this.ph.getString(URLFactory.PERSON_WEIGHT))) {
                            Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_weight_validation));
                            return;
                        } else {
                            if (Float.parseFloat("" + Screen_OnBoarding.this.ph.getString(URLFactory.PERSON_HEIGHT)) < 2.0f) {
                                Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_height_validation));
                                return;
                            }
                            if (Float.parseFloat("" + Screen_OnBoarding.this.ph.getString(URLFactory.PERSON_WEIGHT)) < 30.0f) {
                                Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_weight_validation));
                                return;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                if (Screen_OnBoarding.this.current_page_idx == 5) {
                    if (Screen_OnBoarding.this.sh.check_blank_data(Screen_OnBoarding.this.ph.getString(URLFactory.WAKE_UP_TIME)) || Screen_OnBoarding.this.sh.check_blank_data(Screen_OnBoarding.this.ph.getString(URLFactory.BED_TIME))) {
                        Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_from_to_invalid_validation));
                        return;
                    } else if (Screen_OnBoarding.this.ph.getBoolean(URLFactory.IGNORE_NEXT_STEP)) {
                        Screen_OnBoarding.this.ah.customAlert(Screen_OnBoarding.this.sh.get_string(R.string.str_from_to_invalid_validation));
                        return;
                    }
                }
                if (Screen_OnBoarding.this.current_page_idx < Screen_OnBoarding.this.max_page - 1) {
                    Screen_OnBoarding.this.current_page_idx++;
                    Screen_OnBoarding.this.viewPager.setCurrentItem(Screen_OnBoarding.this.current_page_idx);
                } else if (Build.VERSION.SDK_INT >= 23) {
                    Screen_OnBoarding.this.checkStoragePermissions();
                } else {
                    Screen_OnBoarding.this.gotoHomeScreen();
                }
            }
        });
    }

    public boolean isNextDayEnd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            if (simpleDateFormat.parse(this.ph.getString(URLFactory.WAKE_UP_TIME)).getTime() > simpleDateFormat.parse(this.ph.getString(URLFactory.BED_TIME)).getTime()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void setAlarm() {
        int _id;
        String getLastId;
        ContentValues initialValues;
        Calendar endTime;
        int _id_monday = 0;
        int _id_tuesday = 0;
        int minute_interval = this.ph.getInt(URLFactory.INTERVAL);
        if (!this.sh.check_blank_data(this.ph.getString(URLFactory.WAKE_UP_TIME)) && !this.sh.check_blank_data(this.ph.getString(URLFactory.BED_TIME))) {
            Calendar startTime = Calendar.getInstance();
            int i = 11;
            startTime.set(11, this.ph.getInt(URLFactory.WAKE_UP_TIME_HOUR));
            int i2 = 12;
            startTime.set(12, this.ph.getInt(URLFactory.WAKE_UP_TIME_MINUTE));
            int i3 = 13;
            startTime.set(13, 0);
            Calendar endTime2 = Calendar.getInstance();
            endTime2.set(11, this.ph.getInt(URLFactory.BED_TIME_HOUR));
            endTime2.set(12, this.ph.getInt(URLFactory.BED_TIME_MINUTE));
            endTime2.set(13, 0);
            if (isNextDayEnd()) {
                endTime2.add(5, 1);
            }
            if (startTime.getTimeInMillis() < endTime2.getTimeInMillis()) {
                deleteAutoAlarm(true);
                int _id2 = (int) System.currentTimeMillis();
                ContentValues initialValues2 = new ContentValues();
                initialValues2.put("AlarmTime", "" + this.ph.getString(URLFactory.WAKE_UP_TIME) + " - " + this.ph.getString(URLFactory.BED_TIME));
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(_id2);
                initialValues2.put("AlarmId", sb.toString());
                initialValues2.put("AlarmType", "R");
                initialValues2.put("AlarmInterval", "" + minute_interval);
                this.dh.INSERT("tbl_alarm_details", initialValues2);
                String getLastId2 = this.dh.GET_LAST_ID("tbl_alarm_details");
                int _id3 = _id2;
                while (startTime.getTimeInMillis() <= endTime2.getTimeInMillis()) {
                    Log.d("ALARMTIME  : ", "" + startTime.get(i) + ":" + startTime.get(i2) + ":" + startTime.get(i3));
                    try {
                        int _id4 = (int) System.currentTimeMillis();
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
                            String time = startTime.get(i) + ":" + startTime.get(i2) + ":" + startTime.get(i3);
                            String formatedDate = sdfs.format(sdf.parse(time));
                            if (!this.dh.IS_EXISTS("tbl_alarm_details", "AlarmTime='" + formatedDate + "'")) {
                                if (!this.dh.IS_EXISTS("tbl_alarm_sub_details", "AlarmTime='" + formatedDate + "'")) {
                                    MyAlarmManager.scheduleAutoRecurringAlarm(this.mContext, startTime, _id4);
                                    ContentValues initialValues22 = new ContentValues();
                                    initialValues22.put("AlarmTime", "" + formatedDate);
                                    initialValues22.put("AlarmId", "" + _id4);
                                    initialValues22.put("SuperId", "" + getLastId2);
                                    this.dh.INSERT("tbl_alarm_sub_details", initialValues22);
                                    int _id_sunday = (int) System.currentTimeMillis();
                                    endTime = endTime2;
                                    try {
                                        _id_monday = (int) System.currentTimeMillis();
                                        initialValues = initialValues2;
                                    } catch (Exception e) {
                                        e = e;
                                        initialValues = initialValues2;
                                        getLastId = getLastId2;
                                        _id = _id4;
                                        e.printStackTrace();
                                        _id3 = _id;
                                        startTime.add(12, minute_interval);
                                        endTime2 = endTime;
                                        initialValues2 = initialValues;
                                        getLastId2 = getLastId;
                                        i = 11;
                                        i2 = 12;
                                        i3 = 13;
                                    }
                                    try {
                                        _id_tuesday = (int) System.currentTimeMillis();
                                        getLastId = getLastId2;
                                    } catch (Exception e2) {
                                        getLastId = getLastId2;
                                        _id = _id4;
                                        e2.printStackTrace();
                                        _id3 = _id;
                                        startTime.add(12, minute_interval);
                                        endTime2 = endTime;
                                        initialValues2 = initialValues;
                                        getLastId2 = getLastId;
                                        i = 11;
                                        i2 = 12;
                                        i3 = 13;
                                    }
                                    try {
                                        int _id_wednesday = (int) System.currentTimeMillis();
                                        _id = _id4;
                                        try {
                                            SimpleDateFormat simpleDateFormat = sdf;
                                            SimpleDateFormat simpleDateFormat2 = sdfs;
                                            ContentValues initialValues3 = new ContentValues();
                                            ContentValues contentValues = initialValues22;
                                            StringBuilder sb2 = new StringBuilder();
                                            String str = time;
                                            sb2.append("");
                                            sb2.append(formatedDate);
                                            initialValues3.put("AlarmTime", sb2.toString());
                                            initialValues3.put("AlarmId", "" + _id_sunday);
                                            initialValues3.put("SundayAlarmId", "" + _id_sunday);
                                            initialValues3.put("MondayAlarmId", "" + _id_monday);
                                            initialValues3.put("TuesdayAlarmId", "" + _id_tuesday);
                                            initialValues3.put("WednesdayAlarmId", "" + _id_wednesday);
                                            initialValues3.put("ThursdayAlarmId", "" + ((int) System.currentTimeMillis()));
                                            initialValues3.put("FridayAlarmId", "" + ((int) System.currentTimeMillis()));
                                            initialValues3.put("SaturdayAlarmId", "" + ((int) System.currentTimeMillis()));
                                            initialValues3.put("AlarmType", "M");
                                            initialValues3.put("AlarmInterval", "0");
                                            this.dh.INSERT("tbl_alarm_details", initialValues3);
                                        } catch (Exception e3) {
                                            e3.printStackTrace();
                                        }
                                    } catch (Exception e4) {
                                        _id = _id4;
                                        e4.printStackTrace();
                                        _id3 = _id;
                                        startTime.add(12, minute_interval);
                                        endTime2 = endTime;
                                        initialValues2 = initialValues;
                                        getLastId2 = getLastId;
                                        i = 11;
                                        i2 = 12;
                                        i3 = 13;
                                    }
                                    _id3 = _id;
                                    startTime.add(12, minute_interval);
                                    endTime2 = endTime;
                                    initialValues2 = initialValues;
                                    getLastId2 = getLastId;
                                    i = 11;
                                    i2 = 12;
                                    i3 = 13;
                                }
                            }
                            endTime = endTime2;
                            initialValues = initialValues2;
                            getLastId = getLastId2;
                            _id = _id4;
                        } catch (Exception e5) {
                            endTime = endTime2;
                            initialValues = initialValues2;
                            getLastId = getLastId2;
                            _id = _id4;
                            e5.printStackTrace();
                            _id3 = _id;
                            startTime.add(12, minute_interval);
                            endTime2 = endTime;
                            initialValues2 = initialValues;
                            getLastId2 = getLastId;
                            i = 11;
                            i2 = 12;
                            i3 = 13;
                        }
                    } catch (Exception e6) {
                        endTime = endTime2;
                        initialValues = initialValues2;
                        getLastId = getLastId2;
                        _id = _id3;
                        e6.printStackTrace();
                        _id3 = _id;
                        startTime.add(12, minute_interval);
                        endTime2 = endTime;
                        initialValues2 = initialValues;
                        getLastId2 = getLastId;
                        i = 11;
                        i2 = 12;
                        i3 = 13;
                    }
                    _id3 = _id;
                    startTime.add(12, minute_interval);
                    endTime2 = endTime;
                    initialValues2 = initialValues;
                    getLastId2 = getLastId;
                    i = 11;
                    i2 = 12;
                    i3 = 13;
                }
                return;
            }
            Calendar calendar = endTime2;
        }
    }

    public void deleteAutoAlarm(boolean alsoData) {
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details");
        for (int k = 0; k < arr_data.size(); k++) {
            MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data.get(k).get("AlarmId")));
            Database_Helper database_Helper = this.dh;
            ArrayList<HashMap<String, String>> arr_data2 = database_Helper.getdata("tbl_alarm_sub_details", "SuperId=" + ((String) arr_data.get(k).get("id")));
            for (int j = 0; j < arr_data2.size(); j++) {
                MyAlarmManager.cancelRecurringAlarm(this.mContext, Integer.parseInt((String) arr_data2.get(j).get("AlarmId")));
            }
        }
        if (alsoData) {
            this.dh.REMOVE("tbl_alarm_details");
            this.dh.REMOVE("tbl_alarm_sub_details");
        }
    }

    public void checkStoragePermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this.act, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this.act, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            gotoHomeScreen();
        } else {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 3);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3) {
            gotoHomeScreen();
        }
    }

    public void gotoHomeScreen() {
        this.ph.savePreferences(URLFactory.HIDE_WELCOME_SCREEN, true);
        setAlarm();
        this.intent = new Intent(this.act, Screen_Dashboard.class);
        startActivity(this.intent);
        finish();
    }

    public void onBackPressed() {
        if (this.current_page_idx > 0) {
            this.current_page_idx--;
            this.viewPager.setCurrentItem(this.current_page_idx);
            return;
        }
        super.onBackPressed();
    }
}
