package com.trending.water.drinking.reminder;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.trending.water.drinking.reminder.adapter.ContainerAdapterNew;
import com.trending.water.drinking.reminder.adapter.MenuAdapter;
import com.trending.water.drinking.reminder.adapter.MyPageAdapter;
import com.trending.water.drinking.reminder.adapter.SoundAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.model.Container;
import com.trending.water.drinking.reminder.model.Menu;
import com.trending.water.drinking.reminder.model.NextReminderModel;
import com.trending.water.drinking.reminder.model.SoundModel;
import com.trending.water.drinking.reminder.mywidgets.NewAppWidget;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Screen_Dashboard extends MasterBaseAppCompatActivity {

    private static final String TAG = "Screen_Dashboard";

    public static Calendar filterCal;
    public static Calendar todayCal;
    public static Calendar yesterdayCal;

    private LottieAnimationView animationView;
    private RelativeLayout contentFrame;
    private RelativeLayout contentFrameTest;
    private RelativeLayout addWaterLayout;
    private RelativeLayout selectedContainerBlock;
    private RelativeLayout openHistory;
    private LinearLayout nextReminderBlock;
    private LinearLayout openProfile;
    private LinearLayout btnRateUs;
    private LinearLayout btnContactUs;
    private LinearLayout bannerView;

    private ImageView imgNext;
    private ImageView imgPre;
    private ImageView imgSelectedContainer;
    private ImageView imgUser;
    private ImageView btnMenu;
    private ImageView btnAlarm;

    private AppCompatTextView lblNextReminder;
    private AppCompatTextView lblToolbarTitle;
    private AppCompatTextView lblTotalConsumed;
    private AppCompatTextView lblDailyGoal;
    private AppCompatTextView lblUserName;
    private AppCompatTextView containerName;

    private DrawerLayout drawerLayout;
    private RecyclerView drawerRecyclerView;
    private BottomSheetDialog bottomSheetDialog;

    private ContainerAdapterNew containerAdapter;
    private MenuAdapter menuAdapter;
    private SoundAdapter soundAdapter;

    private final ArrayList<Container> containerList = new ArrayList<>();
    private final ArrayList<Menu> menuList = new ArrayList<>();
    private final List<SoundModel> soundList = new ArrayList<>();

    private int maxBottleHeight = 870;
    private int currentBottleHeight = 0;
    private int targetBottleHeight = 0;
    private int selectedContainerPos = 0;
    
    private float consumedWater = 0.0f;
    private float oldConsumedWater = 0.0f;
    
    private boolean isAnimating = false;
    private boolean isClickable = true;

    private Ringtone fillWaterRingtone;
    private Handler animationHandler;
    private Runnable animationRunnable;
    private Handler reminderHandler;
    private Runnable reminderRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_dashboard);

        initializeData();
        findViewByIds();
        initNavigationDrawer();
        setupListeners();
        
        initializeRingtone();
        checkBatteryOptimization();
    }

    private void initializeData() {
        if (preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 0.0f) == 0.0f) {
            URLFactory.dailyWaterValue = 2500.0f;
        } else {
            URLFactory.dailyWaterValue = preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 2500.0f);
        }

        if (stringHelper.check_blank_data(preferencesHelper.getString(URLFactory.KEY_WATER_UNIT, ""))) {
            URLFactory.waterUnitValue = "ML";
        } else {
            URLFactory.waterUnitValue = preferencesHelper.getString(URLFactory.KEY_WATER_UNIT, "ML");
        }

        filterCal = Calendar.getInstance();
        todayCal = Calendar.getInstance();
        yesterdayCal = Calendar.getInstance();
        yesterdayCal.add(Calendar.DAY_OF_YEAR, -1);
    }

    private void findViewByIds() {
        animationView = findViewById(R.id.animationView);
        contentFrame = findViewById(R.id.content_frame);
        contentFrameTest = findViewById(R.id.content_frame_test);
        lblNextReminder = findViewById(R.id.lbl_next_reminder);
        nextReminderBlock = findViewById(R.id.next_reminder_block);
        addWaterLayout = findViewById(R.id.add_water);
        containerName = findViewById(R.id.container_name);
        imgSelectedContainer = findViewById(R.id.img_selected_container);
        selectedContainerBlock = findViewById(R.id.selected_container_block);
        openHistory = findViewById(R.id.open_history);
        lblDailyGoal = findViewById(R.id.lbl_total_goal);
        lblTotalConsumed = findViewById(R.id.lbl_total_drunk);
        bannerView = findViewById(R.id.banner_view);
        
        btnMenu = findViewById(R.id.btn_menu);
        btnAlarm = findViewById(R.id.btn_alarm);
        lblToolbarTitle = findViewById(R.id.lbl_toolbar_title);
        imgPre = findViewById(R.id.img_pre);
        imgNext = findViewById(R.id.img_next);
        imgUser = findViewById(R.id.img_user);
        openProfile = findViewById(R.id.open_profile);
        btnRateUs = findViewById(R.id.btn_rate_us);
        btnContactUs = findViewById(R.id.btn_contact_us);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerRecyclerView = findViewById(R.id.left_drawer);
        lblUserName = findViewById(R.id.lbl_user_name);

        contentFrameTest.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            maxBottleHeight = contentFrameTest.getHeight() - 30;
        });
    }

    private void setupListeners() {
        lblToolbarTitle.setOnClickListener(v -> {
            filterCal.setTimeInMillis(todayCal.getTimeInMillis());
            lblToolbarTitle.setText(stringHelper.getString(R.string.str_today));
            setDashboardDate(dateHelper.getDate(URLFactory.DATE_FORMAT));
        });

        btnRateUs.setOnClickListener(v -> rateApp());
        btnContactUs.setOnClickListener(v -> contactUs());
        openProfile.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(mActivity, Screen_Profile.class));
        });
        btnAlarm.setOnClickListener(v -> showReminderSettingsDialog());
        btnMenu.setOnClickListener(v -> toggleDrawer());
        
        imgPre.setOnClickListener(v -> changeDateBy(-1));
        imgNext.setOnClickListener(v -> changeDateBy(1));
        
        selectedContainerBlock.setOnClickListener(v -> openChangeContainerPicker());
        openHistory.setOnClickListener(v -> startActivity(new Intent(mActivity, Screen_History.class)));
        
        addWaterLayout.setOnClickListener(v -> {
            if (!containerList.isEmpty() && isTodaySelected() && isClickable) {
                isClickable = false;
                executeAddWater();
            }
        });
    }

    private void initNavigationDrawer() {
        updateUserInfo();
        menuList.clear();
        menuList.add(new Menu(stringHelper.getString(R.string.str_home), true));
        menuList.add(new Menu(stringHelper.getString(R.string.str_drink_history), false));
        menuList.add(new Menu(stringHelper.getString(R.string.str_drink_report), false));
        menuList.add(new Menu(stringHelper.getString(R.string.str_settings), false));
        menuList.add(new Menu(stringHelper.getString(R.string.str_faqs), false));
        menuList.add(new Menu(stringHelper.getString(R.string.str_privacy_policy), false));
        menuList.add(new Menu(stringHelper.getString(R.string.str_tell_a_friend), false));

        menuAdapter = new MenuAdapter(mActivity, menuList, (menu, position) -> {
            closeDrawer();
            switch (position) {
                case 1: startActivity(new Intent(mActivity, Screen_History.class)); break;
                case 2: startActivity(new Intent(mActivity, Screen_Report.class)); break;
                case 3: startActivity(new Intent(mActivity, Screen_Settings.class)); break;
                case 4: startActivity(new Intent(mActivity, Screen_FAQ.class)); break;
                case 5: openPrivacyPolicy(); break;
                case 6: shareApp(); break;
            }
        });
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        drawerRecyclerView.setAdapter(menuAdapter);
    }

    private void initializeRingtone() {
        fillWaterRingtone = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fill_water_sound));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            fillWaterRingtone.setLooping(false);
        }
    }

    private void checkBatteryOptimization() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        String packageName = getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !pm.isIgnoringBatteryOptimizations(packageName)) {
            if (Build.MANUFACTURER.equalsIgnoreCase("OnePlus")) {
                showBatteryOptimizationDialog();
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Battery settings not found", e);
                }
            }
        }
    }

    private void showBatteryOptimizationDialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_battery_optimization, null, false);
        
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPageAdapter(mActivity));
        viewPager.setOffscreenPageLimit(5);
        
        DotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(viewPager);

        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_settings).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
        });
        
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (URLFactory.reloadDashboard) {
            initDashboard();
        } else {
            URLFactory.reloadDashboard = true;
        }
    }

    private void initDashboard() {
        updateUserInfo();
        lblToolbarTitle.setText(stringHelper.getString(R.string.str_today));
        loadAllContainers();
        updateIntakeData(dateHelper.getDate(URLFactory.DATE_FORMAT), false);
        startReminderPoller();
    }

    private void updateUserInfo() {
        lblUserName.setText(preferencesHelper.getString(URLFactory.KEY_USER_NAME, "User"));
        loadUserPhoto();
    }

    private void loadUserPhoto() {
        String photoPath = preferencesHelper.getString(URLFactory.KEY_USER_PHOTO, "");
        boolean isFemale = preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false);
        int defaultRes = isFemale ? R.drawable.female_white : R.drawable.male_white;

        if (stringHelper.check_blank_data(photoPath) || !new File(photoPath).exists()) {
            Glide.with(mActivity).load(defaultRes).apply(RequestOptions.circleCropTransform()).into(imgUser);
        } else {
            Glide.with(mActivity).load(photoPath).apply(RequestOptions.circleCropTransform()).into(imgUser);
        }
    }

    private void changeDateBy(int days) {
        filterCal.add(Calendar.DAY_OF_YEAR, days);
        if (filterCal.getTimeInMillis() > todayCal.getTimeInMillis()) {
            filterCal.add(Calendar.DAY_OF_YEAR, -1);
            return;
        }

        String filterDate = dateHelper.getDate(filterCal.getTimeInMillis(), URLFactory.DATE_FORMAT);
        String todayDate = dateHelper.getDate(todayCal.getTimeInMillis(), URLFactory.DATE_FORMAT);
        String yesterdayDate = dateHelper.getDate(yesterdayCal.getTimeInMillis(), URLFactory.DATE_FORMAT);

        if (filterDate.equalsIgnoreCase(todayDate)) {
            lblToolbarTitle.setText(stringHelper.getString(R.string.str_today));
        } else if (filterDate.equalsIgnoreCase(yesterdayDate)) {
            lblToolbarTitle.setText(stringHelper.getString(R.string.str_yesterday));
        } else {
            lblToolbarTitle.setText(filterDate);
        }
        
        setDashboardDate(filterDate);
    }

    private void setDashboardDate(String date) {
        updateIntakeData(date, false);
    }

    private boolean isTodaySelected() {
        String filterDate = dateHelper.getDate(filterCal.getTimeInMillis(), URLFactory.DATE_FORMAT);
        String todayDate = dateHelper.getDate(todayCal.getTimeInMillis(), URLFactory.DATE_FORMAT);
        return filterDate.equalsIgnoreCase(todayDate);
    }

    private void startReminderPoller() {
        if (reminderHandler != null) reminderHandler.removeCallbacks(reminderRunnable);
        reminderHandler = new Handler(Looper.getMainLooper());
        reminderRunnable = new Runnable() {
            @Override
            public void run() {
                updateNextReminderLabel();
                reminderHandler.postDelayed(this, 1000);
            }
        };
        reminderHandler.postDelayed(reminderRunnable, 1000);
    }

    private void updateNextReminderLabel() {
        List<NextReminderModel> nextReminders = new ArrayList<>();
        ArrayList<HashMap<String, String>> alarms = databaseHelper.getdata("tbl_alarm_details");

        for (HashMap<String, String> alarm : alarms) {
            String type = alarm.get("AlarmType");
            if ("R".equalsIgnoreCase(type)) {
                if (!preferencesHelper.getBoolean(URLFactory.KEY_IS_MANUAL_REMINDER, false)) {
                    ArrayList<HashMap<String, String>> subAlarms = databaseHelper.getdata("tbl_alarm_sub_details", "SuperId='" + alarm.get("id") + "'");
                    for (HashMap<String, String> sub : subAlarms) {
                        nextReminders.add(new NextReminderModel(getMilliFromAlarmTime(sub.get("AlarmTime")), sub.get("AlarmTime")));
                    }
                }
            } else if (preferencesHelper.getBoolean(URLFactory.KEY_IS_MANUAL_REMINDER, false) && "0".equals(alarm.get("IsOff"))) {
                nextReminders.add(new NextReminderModel(getMilliFromAlarmTime(alarm.get("AlarmTime")), alarm.get("AlarmTime")));
            }
        }

        Collections.sort(nextReminders);
        long now = System.currentTimeMillis();
        NextReminderModel next = null;
        for (NextReminderModel rem : nextReminders) {
            if (rem.getMillesecond() > now) {
                next = rem;
                break;
            }
        }

        if (next != null) {
            nextReminderBlock.setVisibility(View.VISIBLE);
            lblNextReminder.setText(stringHelper.getString(R.string.str_next_reminder).replace("$1", next.getTime()));
        } else {
            nextReminderBlock.setVisibility(View.INVISIBLE);
        }
    }

    private long getMilliFromAlarmTime(String timeStr) {
        try {
            String today = dateHelper.getDate("yyyy-MM-dd");
            return new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US).parse(today + " " + timeStr).getTime();
        } catch (ParseException e) {
            Log.e(TAG, "Parsing alarm time failed", e);
            return 0;
        }
    }

    private void updateIntakeData(String date, boolean isRegularAnimation) {
        ArrayList<HashMap<String, String>> intakeRecords = databaseHelper.getdata("tbl_drink_details", "DrinkDate ='" + date + "'");
        
        consumedWater = 0.0f;
        boolean isMl = URLFactory.waterUnitValue.equalsIgnoreCase("ML");

        for (HashMap<String, String> record : intakeRecords) {
            String valKey = isMl ? "ContainerValue" : "ContainerValueOZ";
            consumedWater += Float.parseFloat(record.get(valKey));
        }

        // Set Daily Goal Label
        float dailyGoal;
        if (date.equalsIgnoreCase(dateHelper.getCurrentDate(URLFactory.DATE_FORMAT))) {
            dailyGoal = URLFactory.dailyWaterValue;
        } else if (!intakeRecords.isEmpty()) {
            String goalKey = isMl ? "TodayGoal" : "TodayGoalOZ";
            dailyGoal = Float.parseFloat(intakeRecords.get(0).get(goalKey));
        } else {
            dailyGoal = URLFactory.dailyWaterValue;
        }

        lblTotalConsumed.setText(formatWaterValue(consumedWater));
        lblDailyGoal.setText(formatWaterValue(dailyGoal));
        
        animateBottle(consumedWater, dailyGoal, isRegularAnimation);
    }

    private void animateBottle(float consumed, float goal, boolean isRegular) {
        if (animationHandler != null) animationHandler.removeCallbacks(animationRunnable);
        
        isClickable = false;
        targetBottleHeight = Math.round((consumed * maxBottleHeight) / goal);
        final int step = 6;
        final long delay = isRegular ? 50 : 5;

        animationRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentBottleHeight < targetBottleHeight) {
                    currentBottleHeight = Math.min(currentBottleHeight + step, targetBottleHeight);
                    updateBottleHeight(currentBottleHeight);
                    animationHandler.postDelayed(this, delay);
                } else if (currentBottleHeight > targetBottleHeight && !isRegular) {
                    currentBottleHeight = Math.max(currentBottleHeight - step, targetBottleHeight);
                    updateBottleHeight(currentBottleHeight);
                    animationHandler.postDelayed(this, delay);
                } else {
                    isClickable = true;
                    checkGoalReached();
                }
            }
        };

        animationHandler = new Handler(Looper.getMainLooper());
        animationHandler.postDelayed(animationRunnable, delay);
        
        animationView.setVisibility(targetBottleHeight > 0 ? View.VISIBLE : View.GONE);
    }

    private void updateBottleHeight(int height) {
        contentFrame.getLayoutParams().height = height;
        contentFrame.requestLayout();
    }

    private void checkGoalReached() {
        if (oldConsumedWater < URLFactory.dailyWaterValue && consumedWater >= URLFactory.dailyWaterValue) {
            showGoalReachedDialog();
        }
        oldConsumedWater = consumedWater;
    }

    private void executeAddWater() {
        if (containerList.isEmpty()) return;

        Container selected = containerList.get(selectedContainerPos);
        float containerValue = URLFactory.waterUnitValue.equalsIgnoreCase("ML") ? 
                Float.parseFloat(selected.getContainerValue()) : 
                Float.parseFloat(selected.getContainerValueOZ());

        float maxLimit = URLFactory.waterUnitValue.equalsIgnoreCase("ML") ? 8000f : 270f;

        if (consumedWater >= maxLimit) {
            showLimitReachedDialog();
            isClickable = true;
            return;
        }

        if (consumedWater + containerValue > maxLimit) {
            showLimitReachedDialog();
            // We still allow adding up to the limit if they click again? 
            // The logic in original was a bit convoluted, let's keep it safe.
        }

        if (!preferencesHelper.getBoolean(URLFactory.KEY_DISABLE_SOUND, false)) {
            if (fillWaterRingtone.isPlaying()) fillWaterRingtone.stop();
            fillWaterRingtone.play();
        }

        ContentValues values = new ContentValues();
        values.put("ContainerValue", selected.getContainerValue());
        values.put("ContainerValueOZ", selected.getContainerValueOZ());
        values.put("DrinkDate", dateHelper.getDate(filterCal.getTimeInMillis(), URLFactory.DATE_FORMAT));
        values.put("DrinkTime", dateHelper.getCurrentTime(true));
        values.put("DrinkDateTime", dateHelper.getDate(filterCal.getTimeInMillis(), URLFactory.DATE_FORMAT) + " " + dateHelper.getCurrentDate("HH:mm:ss"));
        
        if (URLFactory.waterUnitValue.equalsIgnoreCase("ML")) {
            values.put("TodayGoal", String.valueOf(URLFactory.dailyWaterValue));
            values.put("TodayGoalOZ", String.valueOf(HeightWeightHelper.mlToOzConverter(URLFactory.dailyWaterValue)));
        } else {
            values.put("TodayGoal", String.valueOf(HeightWeightHelper.ozToMlConverter(URLFactory.dailyWaterValue)));
            values.put("TodayGoalOZ", String.valueOf(URLFactory.dailyWaterValue));
        }

        databaseHelper.INSERT("tbl_drink_details", values);
        
        updateIntakeData(dateHelper.getDate(filterCal.getTimeInMillis(), URLFactory.DATE_FORMAT), true);
        updateWidgets();
    }

    private void loadAllContainers() {
        containerList.clear();
        ArrayList<HashMap<String, String>> data = databaseHelper.getdata("tbl_container_details", "IsCustom", 1);
        int savedId = preferencesHelper.getInt(URLFactory.KEY_SELECTED_CONTAINER, 1);

        for (int i = 0; i < data.size(); i++) {
            HashMap<String, String> map = data.get(i);
            Container c = new Container();
            c.setContainerId(map.get("ContainerID"));
            c.setContainerValue(map.get("ContainerValue"));
            c.setContainerValueOZ(map.get("ContainerValueOZ"));
            c.isOpen("1".equals(map.get("IsOpen")));
            c.isCustom("1".equals(map.get("IsCustom")));
            
            boolean isSelected = String.valueOf(savedId).equals(c.getContainerId());
            c.isSelected(isSelected);
            if (isSelected) selectedContainerPos = i;
            
            containerList.add(c);
        }
        updateSelectedContainerInfo();
    }

    private void updateSelectedContainerInfo() {
        if (containerList.isEmpty()) return;
        
        Container selected = containerList.get(selectedContainerPos);
        String unit = URLFactory.waterUnitValue;
        String val = unit.equalsIgnoreCase("ML") ? selected.getContainerValue() : selected.getContainerValueOZ();
        
        containerName.setText(val + " " + unit);
        
        int iconRes = selected.isCustom() ? R.drawable.ic_custom_ml : getContainerIcon(val);
        Glide.with(mContext).load(iconRes).into(imgSelectedContainer);
    }

    private int getContainerIcon(String val) {
        double dVal = Double.parseDouble(val);
        if (URLFactory.waterUnitValue.equalsIgnoreCase("ML")) {
            if (dVal == 50) return R.drawable.ic_50_ml;
            if (dVal == 100) return R.drawable.ic_100_ml;
            if (dVal == 150) return R.drawable.ic_150_ml;
            if (dVal == 200) return R.drawable.ic_200_ml;
            if (dVal == 250) return R.drawable.ic_250_ml;
            if (dVal == 300) return R.drawable.ic_300_ml;
            if (dVal == 500) return R.drawable.ic_500_ml;
            if (dVal == 600) return R.drawable.ic_600_ml;
            if (dVal == 700) return R.drawable.ic_700_ml;
            if (dVal == 800) return R.drawable.ic_800_ml;
            if (dVal == 900) return R.drawable.ic_900_ml;
            if (dVal == 1000) return R.drawable.ic_1000_ml;
        } else {
            if (dVal == 2) return R.drawable.ic_50_ml;
            if (dVal == 3) return R.drawable.ic_100_ml;
            if (dVal == 5) return R.drawable.ic_150_ml;
            if (dVal == 7) return R.drawable.ic_200_ml;
            if (dVal == 8) return R.drawable.ic_250_ml;
            if (dVal == 10) return R.drawable.ic_300_ml;
            if (dVal == 17) return R.drawable.ic_500_ml;
            if (dVal == 20) return R.drawable.ic_600_ml;
            if (dVal == 24) return R.drawable.ic_700_ml;
            if (dVal == 27) return R.drawable.ic_800_ml;
            if (dVal == 30) return R.drawable.ic_900_ml;
            if (dVal == 34) return R.drawable.ic_1000_ml;
        }
        return R.drawable.ic_custom_ml;
    }

    private void openChangeContainerPicker() {
        bottomSheetDialog = new BottomSheetDialog(mActivity);
        bottomSheetDialog.setOnShowListener(dialog -> {
            FrameLayout bottomSheet = ((BottomSheetDialog) dialog).findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackground(null);
            }
        });

        View view = LayoutInflater.from(mActivity).inflate(R.layout.bottom_sheet_change_container, null, false);
        RecyclerView rv = view.findViewById(R.id.containerRecyclerView);
        rv.setLayoutManager(new GridLayoutManager(mContext, 3));
        
        containerAdapter = new ContainerAdapterNew(mActivity, containerList, (container, position) -> {
            bottomSheetDialog.dismiss();
            selectedContainerPos = position;
            preferencesHelper.savePreferences(URLFactory.KEY_SELECTED_CONTAINER, Integer.parseInt(container.getContainerId()));
            
            for (Container c : containerList) c.isSelected(false);
            containerList.get(position).isSelected(true);
            updateSelectedContainerInfo();
        });
        rv.setAdapter(containerAdapter);

        view.findViewById(R.id.add_custom_container).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openAddCustomContainerDialog();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void openAddCustomContainerDialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        
        View view = LayoutInflater.from(mActivity).inflate(R.layout.bottom_sheet_add_custom_container, null, false);
        AppCompatEditText etValue = view.findViewById(R.id.txt_value);
        AppCompatTextView tvUnitLabel = view.findViewById(R.id.lbl_unit);
        
        boolean isMl = URLFactory.waterUnitValue.equalsIgnoreCase("ML");
        tvUnitLabel.setText(stringHelper.getString(R.string.str_capacity).replace("$1", URLFactory.waterUnitValue));
        etValue.setFilters(new InputFilter[]{new InputFilterWeightRange(1.0, isMl ? 8000.0 : 270.0)});
        etValue.requestFocus();

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_add).setOnClickListener(v -> {
            String input = etValue.getText().toString().trim();
            if (stringHelper.check_blank_data(input) || "0".equals(input)) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_enter_value_validation));
                return;
            }

            double ml, oz;
            if (isMl) {
                ml = Double.parseDouble(input);
                oz = HeightWeightHelper.mlToOzConverter(ml);
            } else {
                oz = Double.parseDouble(input);
                ml = HeightWeightHelper.ozToMlConverter(oz);
            }

            int nextId = getNextContainerId();
            ContentValues values = new ContentValues();
            values.put("ContainerID", String.valueOf(nextId));
            values.put("ContainerValue", String.valueOf(Math.round(ml)));
            values.put("ContainerValueOZ", String.valueOf(Math.round(oz)));
            values.put("IsOpen", "1");
            values.put("IsCustom", "1");
            
            databaseHelper.INSERT("tbl_container_details", values);
            preferencesHelper.savePreferences(URLFactory.KEY_SELECTED_CONTAINER, nextId);
            
            loadAllContainers();
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private int getNextContainerId() {
        try (Cursor cursor = databaseHelper.rawQuery("SELECT MAX(ContainerID) FROM tbl_container_details", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0) + 1;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get next container ID", e);
        }
        return 100; // Default high start for custom IDs
    }

    private void showGoalReachedDialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_goal_reached, null, false);
        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_share).setOnClickListener(v -> {
            dialog.dismiss();
            shareAchievement();
        });
        
        dialog.setContentView(view);
        dialog.show();
    }

    private void showLimitReachedDialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_goal_target_reached, null, false);
        AppCompatTextView tvDesc = view.findViewById(R.id.lbl_desc);
        ImageView imgBottle = view.findViewById(R.id.img_bottle);

        boolean isMl = URLFactory.waterUnitValue.equalsIgnoreCase("ML");
        imgBottle.setImageResource(isMl ? R.drawable.ic_limit_ml : R.drawable.ic_limit_oz);
        tvDesc.setText(stringHelper.getString(R.string.str_you_should_not_drink_more_then_target)
                .replace("$1", isMl ? "8000 ML" : "270 FL OZ"));

        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    private void showReminderSettingsDialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_reminder, null, false);
        final RelativeLayout offBlock = view.findViewById(R.id.off_block);
        final RelativeLayout silentBlock = view.findViewById(R.id.silent_block);
        final RelativeLayout autoBlock = view.findViewById(R.id.auto_block);
        final ImageView imgOff = view.findViewById(R.id.img_off);
        final ImageView imgSilent = view.findViewById(R.id.img_silent);
        final ImageView imgAuto = view.findViewById(R.id.img_auto);
        
        view.findViewById(R.id.advance_settings).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(mActivity, Screen_Reminder.class));
        });

        view.findViewById(R.id.custom_sound_block).setOnClickListener(v -> openSoundMenuPicker());

        SwitchCompat switchVibrate = view.findViewById(R.id.switch_vibrate);
        switchVibrate.setChecked(!preferencesHelper.getBoolean(URLFactory.KEY_REMINDER_VIBRATE, false));
        switchVibrate.setOnCheckedChangeListener((bv, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_VIBRATE, !isChecked);
        });

        int option = preferencesHelper.getInt(URLFactory.KEY_REMINDER_OPTION, 0);
        updateReminderOptionUI(option, offBlock, silentBlock, autoBlock, imgOff, imgSilent, imgAuto);

        offBlock.setOnClickListener(v -> {
            updateReminderOptionUI(1, offBlock, silentBlock, autoBlock, imgOff, imgSilent, imgAuto);
            preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_OPTION, 1);
        });
        silentBlock.setOnClickListener(v -> {
            updateReminderOptionUI(2, offBlock, silentBlock, autoBlock, imgOff, imgSilent, imgAuto);
            preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_OPTION, 2);
        });
        autoBlock.setOnClickListener(v -> {
            updateReminderOptionUI(0, offBlock, silentBlock, autoBlock, imgOff, imgSilent, imgAuto);
            preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_OPTION, 0);
        });

        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> dialog.dismiss());
        
        dialog.setContentView(view);
        dialog.show();
    }

    private void updateReminderOptionUI(int option, View off, View silent, View auto, ImageView iOff, ImageView iSilent, ImageView iAuto) {
        off.setBackgroundResource(option == 1 ? R.drawable.drawable_circle_selected : R.drawable.drawable_circle_unselected);
        iOff.setImageResource(option == 1 ? R.drawable.ic_off_selected : R.drawable.ic_off_normal);
        
        silent.setBackgroundResource(option == 2 ? R.drawable.drawable_circle_selected : R.drawable.drawable_circle_unselected);
        iSilent.setImageResource(option == 2 ? R.drawable.ic_silent_selected : R.drawable.ic_silent_normal);
        
        auto.setBackgroundResource(option == 0 ? R.drawable.drawable_circle_selected : R.drawable.drawable_circle_unselected);
        iAuto.setImageResource(option == 0 ? R.drawable.ic_auto_selected : R.drawable.ic_auto_normal);
    }

    private void openSoundMenuPicker() {
        loadSoundsList();
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sound_pick, null, false);
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            for (int i = 0; i < soundList.size(); i++) {
                if (soundList.get(i).isSelected()) {
                    preferencesHelper.savePreferences(URLFactory.KEY_REMINDER_SOUND, i);
                    break;
                }
            }
            dialog.dismiss();
        });

        RecyclerView rv = view.findViewById(R.id.soundRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        soundAdapter = new SoundAdapter(mActivity, soundList, (sound, position) -> {
            for (SoundModel s : soundList) s.isSelected(false);
            soundList.get(position).isSelected(true);
            soundAdapter.notifyDataSetChanged();
            playPreviewSound(position);
        });
        rv.setAdapter(soundAdapter);

        dialog.setContentView(view);
        dialog.show();
    }

    private void loadSoundsList() {
        soundList.clear();
        String[] internalNames = {"Default", "Bell", "Blop", "Bong", "Click", "Echo Droplet", "Mario Droplet", "Ship Bell", "Simple Droplet", "Tiny Droplet"};
        int savedIdx = preferencesHelper.getInt(URLFactory.KEY_REMINDER_SOUND, 0);
        for (int i = 0; i < internalNames.length; i++) {
            SoundModel sm = new SoundModel();
            sm.setId(i);
            sm.setName(internalNames[i]);
            sm.isSelected(i == savedIdx);
            soundList.add(sm);
        }
    }

    private void playPreviewSound(int idx) {
        MediaPlayer mp;
        switch (idx) {
            case 1: mp = MediaPlayer.create(this, R.raw.bell); break;
            case 2: mp = MediaPlayer.create(this, R.raw.blop); break;
            case 3: mp = MediaPlayer.create(this, R.raw.bong); break;
            case 4: mp = MediaPlayer.create(this, R.raw.click); break;
            case 5: mp = MediaPlayer.create(this, R.raw.echo_droplet); break;
            case 6: mp = MediaPlayer.create(this, R.raw.mario_droplet); break;
            case 7: mp = MediaPlayer.create(this, R.raw.ship_bell); break;
            case 8: mp = MediaPlayer.create(this, R.raw.simple_droplet); break;
            case 9: mp = MediaPlayer.create(this, R.raw.tiny_droplet); break;
            default: mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI); break;
        }
        if (mp != null) {
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        }
    }

    private void updateWidgets() {
        Intent intent = new Intent(mActivity, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(mActivity).getAppWidgetIds(new ComponentName(mActivity, NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    private String formatWaterValue(float value) {
        return String.format(Locale.US, "%d %s", (int) value, URLFactory.waterUnitValue);
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void rateApp() {
        String pkg = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
        }
    }

    private void contactUs() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@yourdomain.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: Water Reminder");
            startActivity(intent);
        } catch (Exception ignored) {}
    }

    private void openPrivacyPolicy() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URLFactory.PRIVACY_POLICY_URL)));
        } catch (Exception ignored) {}
    }

    private void shareApp() {
        String msg = stringHelper.getString(R.string.app_share_txt).replace("#1", URLFactory.APP_SHARE_URL);
        intentHelper.ShareText(getApplicationName(mContext), msg);
    }

    private void shareAchievement() {
        String msg = stringHelper.getString(R.string.str_share_text)
                .replace("$1", formatWaterValue(consumedWater))
                .replace("$2", "@ " + URLFactory.APP_SHARE_URL);
        intentHelper.ShareText(getApplicationName(mContext), msg);
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (reminderHandler != null) reminderHandler.removeCallbacks(reminderRunnable);
        if (animationHandler != null) animationHandler.removeCallbacks(animationRunnable);
        super.onDestroy();
    }
}
