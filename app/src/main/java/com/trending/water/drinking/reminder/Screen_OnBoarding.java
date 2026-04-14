package com.trending.water.drinking.reminder;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.trending.water.drinking.reminder.adapter.OnBoardingPagerAdapter;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.custom.NonSwipeableViewPager;
import com.trending.water.drinking.reminder.receiver.MyAlarmManager;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Screen_OnBoarding extends MasterBaseAppCompatActivity {
    
    private static final String TAG = "Screen_OnBoarding";
    private static final int STORAGE_PERMISSION_CODE = 3;
    private static final int MAX_PAGE = 7;

    private NonSwipeableViewPager viewPager;
    private DotsIndicator dotsIndicator;
    private LinearLayout btnBack;
    private LinearLayout btnNext;
    private AppCompatTextView lblNext;
    private View space;
    
    private OnBoardingPagerAdapter pagerAdapter;
    private int currentPageIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_onboarding);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.water_color));
        }
        
        findViewByIds();
        initView();
        initListeners();
    }

    private void findViewByIds() {
        viewPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dots_indicator);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);
        lblNext = findViewById(R.id.lbl_next);
        space = findViewById(R.id.space);
    }

    private void initView() {
        dotsIndicator.setDotsClickable(false);
        pagerAdapter = new OnBoardingPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(10);
        dotsIndicator.setViewPager(viewPager);
        
        updateNavigationButtons(0);
    }

    private void initListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                updateNavigationButtons(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        btnBack.setOnClickListener(v -> {
            if (currentPageIndex > 0) {
                currentPageIndex--;
                viewPager.setCurrentItem(currentPageIndex);
            }
        });

        btnNext.setOnClickListener(v -> handleNextButtonClick());
    }

    private void updateNavigationButtons(int position) {
        if (position == 0) {
            btnBack.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
        } else {
            btnBack.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
        }

        if (position == MAX_PAGE - 1) {
            lblNext.setText(stringHelper.getString(R.string.str_get_started));
        } else {
            lblNext.setText(stringHelper.getString(R.string.str_next));
        }
    }

    private void handleNextButtonClick() {
        if (currentPageIndex == 0) {
            String name = preferencesHelper.getString(URLFactory.KEY_USER_NAME, "");
            if (stringHelper.check_blank_data(name)) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_your_name_validation));
                return;
            } else if (name.length() < 3) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_valid_name_validation));
                return;
            }
        }

        if (currentPageIndex == 1) {
            try {
                String heightStr = preferencesHelper.getString(URLFactory.KEY_PERSON_HEIGHT, "");
                String weightStr = preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "");
                
                if (stringHelper.check_blank_data(heightStr)) {
                    alertHelper.customAlert(stringHelper.getString(R.string.str_height_validation));
                    return;
                } else if (stringHelper.check_blank_data(weightStr)) {
                    alertHelper.customAlert(stringHelper.getString(R.string.str_weight_validation));
                    return;
                } else {
                    float height = Float.parseFloat(heightStr);
                    float weight = Float.parseFloat(weightStr);
                    if (height < 2.0f) {
                        alertHelper.customAlert(stringHelper.getString(R.string.str_height_validation));
                        return;
                    }
                    if (weight < 30.0f) {
                        alertHelper.customAlert(stringHelper.getString(R.string.str_weight_validation));
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing height/weight", e);
            }
        }

        if (currentPageIndex == 5) {
            String wakeupTime = preferencesHelper.getString(URLFactory.KEY_WAKE_UP_TIME, "");
            String bedTime = preferencesHelper.getString(URLFactory.KEY_BED_TIME, "");
            
            if (stringHelper.check_blank_data(wakeupTime) || stringHelper.check_blank_data(bedTime)) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_from_to_invalid_validation));
                return;
            } else if (preferencesHelper.getBoolean(URLFactory.KEY_IGNORE_NEXT_STEP, false)) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_from_to_invalid_validation));
                return;
            }
        }

        if (currentPageIndex < MAX_PAGE - 1) {
            currentPageIndex++;
            viewPager.setCurrentItem(currentPageIndex);
        } else {
            checkAndRequestPermissions();
        }
    }

    private boolean isNextDayEnd() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        try {
            long wakeup = sdf.parse(preferencesHelper.getString(URLFactory.KEY_WAKE_UP_TIME, "")).getTime();
            long bed = sdf.parse(preferencesHelper.getString(URLFactory.KEY_BED_TIME, "")).getTime();
            return wakeup > bed;
        } catch (Exception e) {
            return false;
        }
    }

    private void setAlarms() {
        int minuteInterval = preferencesHelper.getInt(URLFactory.KEY_INTERVAL, 30);
        String wakeupTimeStr = preferencesHelper.getString(URLFactory.KEY_WAKE_UP_TIME, "");
        String bedTimeStr = preferencesHelper.getString(URLFactory.KEY_BED_TIME, "");

        if (!stringHelper.check_blank_data(wakeupTimeStr) && !stringHelper.check_blank_data(bedTimeStr)) {
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, preferencesHelper.getInt(URLFactory.KEY_WAKE_UP_HOUR, 8));
            startTime.set(Calendar.MINUTE, preferencesHelper.getInt(URLFactory.KEY_WAKE_UP_MINUTE, 0));
            startTime.set(Calendar.SECOND, 0);

            Calendar endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, preferencesHelper.getInt(URLFactory.KEY_BED_TIME_HOUR, 22));
            endTime.set(Calendar.MINUTE, preferencesHelper.getInt(URLFactory.KEY_BED_TIME_MINUTE, 0));
            endTime.set(Calendar.SECOND, 0);

            if (isNextDayEnd()) {
                endTime.add(Calendar.DAY_OF_YEAR, 1);
            }

            if (startTime.getTimeInMillis() < endTime.getTimeInMillis()) {
                deleteAutoAlarms(true);
                
                int mainAlarmId = (int) System.currentTimeMillis();
                ContentValues mainAlarmValues = new ContentValues();
                mainAlarmValues.put("AlarmTime", wakeupTimeStr + " - " + bedTimeStr);
                mainAlarmValues.put("AlarmId", String.valueOf(mainAlarmId));
                mainAlarmValues.put("AlarmType", "R"); // Recurring
                mainAlarmValues.put("AlarmInterval", String.valueOf(minuteInterval));
                databaseHelper.insert("tbl_alarm_details", mainAlarmValues);
                
                String superId = databaseHelper.getLastId("tbl_alarm_details");

                SimpleDateFormat sdfInput = new SimpleDateFormat("HH:mm:ss", Locale.US);
                SimpleDateFormat sdfOutput = new SimpleDateFormat("hh:mm a", Locale.US);

                while (startTime.getTimeInMillis() <= endTime.getTimeInMillis()) {
                    try {
                        int subAlarmId = (int) System.currentTimeMillis();
                        String timeStr = startTime.get(Calendar.HOUR_OF_DAY) + ":" + startTime.get(Calendar.MINUTE) + ":" + startTime.get(Calendar.SECOND);
                        String formattedTime = sdfOutput.format(sdfInput.parse(timeStr));

                        if (!databaseHelper.isExists("tbl_alarm_details", "AlarmTime='" + formattedTime + "'") &&
                            !databaseHelper.isExists("tbl_alarm_sub_details", "AlarmTime='" + formattedTime + "'")) {
                            
                            MyAlarmManager.scheduleAutoRecurringAlarm(this, startTime, subAlarmId);
                            
                            ContentValues subAlarmValues = new ContentValues();
                            subAlarmValues.put("AlarmTime", formattedTime);
                            subAlarmValues.put("AlarmId", String.valueOf(subAlarmId));
                            subAlarmValues.put("SuperId", superId);
                            databaseHelper.insert("tbl_alarm_sub_details", subAlarmValues);

                            // Create day-specific alarms
                            createSpecificDayAlarms(formattedTime);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error scheduling alarm at " + startTime.getTime(), e);
                    }
                    startTime.add(Calendar.MINUTE, minuteInterval);
                    // Add small sleep to ensure unique timestamps for IDs if needed, 
                    // though System.currentTimeMillis() should change between loop iterations usually.
                    try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    private void createSpecificDayAlarms(String formattedTime) {
        try {
            int sundayId = (int) System.currentTimeMillis();
            int mondayId = (int) (System.currentTimeMillis() + 1);
            int tuesdayId = (int) (System.currentTimeMillis() + 2);
            int wednesdayId = (int) (System.currentTimeMillis() + 3);
            
            ContentValues dayValues = new ContentValues();
            dayValues.put("AlarmTime", formattedTime);
            dayValues.put("AlarmId", String.valueOf(sundayId));
            dayValues.put("SundayAlarmId", String.valueOf(sundayId));
            dayValues.put("MondayAlarmId", String.valueOf(mondayId));
            dayValues.put("TuesdayAlarmId", String.valueOf(tuesdayId));
            dayValues.put("WednesdayAlarmId", String.valueOf(wednesdayId));
            dayValues.put("ThursdayAlarmId", String.valueOf((int) System.currentTimeMillis() + 4));
            dayValues.put("FridayAlarmId", String.valueOf((int) System.currentTimeMillis() + 5));
            dayValues.put("SaturdayAlarmId", String.valueOf((int) System.currentTimeMillis() + 6));
            dayValues.put("AlarmType", "M"); // Manual/Specific Day
            dayValues.put("AlarmInterval", "0");
            databaseHelper.insert("tbl_alarm_details", dayValues);
        } catch (Exception e) {
            Log.e(TAG, "Error creating specific day alarms", e);
        }
    }

    private void deleteAutoAlarms(boolean deleteData) {
        List<HashMap<String, String>> alarms = databaseHelper.getData("tbl_alarm_details");
        for (HashMap<String, String> alarm : alarms) {
            MyAlarmManager.cancelRecurringAlarm(this, Integer.parseInt(alarm.get("AlarmId")));
            List<HashMap<String, String>> subAlarms = databaseHelper.getData("tbl_alarm_sub_details", "SuperId=" + alarm.get("id"));
            for (HashMap<String, String> subAlarm : subAlarms) {
                MyAlarmManager.cancelRecurringAlarm(this, Integer.parseInt(subAlarm.get("AlarmId")));
            }
        }
        if (deleteData) {
            databaseHelper.remove("tbl_alarm_details");
            databaseHelper.remove("tbl_alarm_sub_details");
        }
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            gotoHomeScreen();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            gotoHomeScreen();
        } else {
            requestPermissions(new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            gotoHomeScreen();
        }
    }

    private void gotoHomeScreen() {
        preferencesHelper.savePreferences(URLFactory.KEY_HIDE_WELCOME_SCREEN, true);
        setAlarms();
        Intent intent = new Intent(this, Screen_Dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            viewPager.setCurrentItem(currentPageIndex);
        } else {
            super.onBackPressed();
        }
    }
}
