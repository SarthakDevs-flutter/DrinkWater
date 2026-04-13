package com.trending.water.drinking.reminder;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.PowerManager;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.trending.water.drinking.reminder.adapter.ContainerAdapterNew;
import com.trending.water.drinking.reminder.adapter.MenuAdapter;
import com.trending.water.drinking.reminder.adapter.MyPageAdapter;
import com.trending.water.drinking.reminder.adapter.SoundAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Alert_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.String_Helper;
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
import java.io.PrintStream;
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
    public static Calendar filter_cal;
    public static Calendar today_cal;
    public static Calendar yesterday_cal;
    ContainerAdapterNew adapter;
    RelativeLayout add_water;
    LottieAnimationView animationView;
    LinearLayout banner_view;
    BottomSheetDialog bottomSheetDialog;
    ImageView btn_alarm;
    LinearLayout btn_contact_us;
    ImageView btn_menu;
    LinearLayout btn_rate_us;
    boolean btnclick = true;
    ArrayList<Container> containerArrayList = new ArrayList<>();
    AppCompatTextView container_name;
    RelativeLayout content_frame;
    RelativeLayout content_frame_test;
    int cp = 0;
    float drink_water = 0.0f;
    Handler handler;
    Handler handlerReminder;
    ImageView img_next;
    ImageView img_pre;
    ImageView img_selected_container;
    ImageView img_user;
    AppCompatTextView lbl_next_reminder;
    AppCompatTextView lbl_toolbar_title;
    AppCompatTextView lbl_total_drunk;
    AppCompatTextView lbl_total_goal;
    AppCompatTextView lbl_user_name;
    List<SoundModel> lst_sounds = new ArrayList();
    DrawerLayout mDrawerLayout;
    RecyclerView mDrawerList;
    int max_bottle_height = 870;
    MenuAdapter menuAdapter;
    ArrayList<Menu> menu_name = new ArrayList<>();
    LinearLayout next_reminder_block;
    int np = 0;
    float old_drink_water = 0.0f;
    RelativeLayout open_history;
    LinearLayout open_profile;
    int progress_bottle_height = 0;
    Ringtone ringtone;
    Runnable runnable;
    Runnable runnableReminder;
    RelativeLayout selected_container_block;
    int selected_pos = 0;
    SoundAdapter soundAdapter;

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.screen_dashboard);
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
        Context context = this.mContext;
        this.ringtone = RingtoneManager.getRingtone(context, Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.fill_water_sound));
        if (Build.VERSION.SDK_INT >= 28) {
            this.ringtone.setLooping(false);
        }
        try {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            String packageName = getApplicationContext().getPackageName();
            if (Build.VERSION.SDK_INT >= 23 && !pm.isIgnoringBatteryOptimizations(packageName)) {
                if (Build.MANUFACTURER.equalsIgnoreCase("OnePlus")) {
                    showPermissionDialog();
                    return;
                }
                Intent intent = new Intent("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showPermissionDialog() {
        try {
            final Dialog dialog = new Dialog(this.act);
            dialog.requestWindowFeature(1);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_battery_optimization, (ViewGroup) null, false);
            ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            viewPager.setAdapter(new MyPageAdapter(this.act));
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                }

                public void onPageScrollStateChanged(int state) {
                }
            });
            viewPager.setOffscreenPageLimit(5);
            ((DotsIndicator) view.findViewById(R.id.dots_indicator)).setViewPager(viewPager);
            ((ImageView) view.findViewById(R.id.img_cancel)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ((RelativeLayout) view.findViewById(R.id.btn_settings)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    Screen_Dashboard.this.startActivity(new Intent("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS"));
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                }
            });
            dialog.setContentView(view);
            dialog.show();
        } catch (Exception e) {
            Alert_Helper alert_Helper = this.ah;
            alert_Helper.Show_Alert_Dialog("" + e.getMessage());
        }
    }

    public void loadPhoto() {
        boolean check_blank_data = this.sh.check_blank_data(this.ph.getString(URLFactory.USER_PHOTO));
        int i = R.drawable.male_white;
        if (check_blank_data) {
            RequestManager with = Glide.with(this.act);
            if (this.ph.getBoolean(URLFactory.USER_GENDER)) {
                i = R.drawable.female_white;
            }
            with.load(Integer.valueOf(i)).apply((BaseRequestOptions<?>) RequestOptions.circleCropTransform()).into(this.img_user);
            return;
        }
        boolean ex = false;
        try {
            if (new File(this.ph.getString(URLFactory.USER_PHOTO)).exists()) {
                ex = true;
            }
        } catch (Exception e) {
        }
        if (ex) {
            Glide.with(this.act).load(this.ph.getString(URLFactory.USER_PHOTO)).apply((BaseRequestOptions<?>) RequestOptions.circleCropTransform()).into(this.img_user);
            return;
        }
        RequestManager with2 = Glide.with(this.act);
        if (this.ph.getBoolean(URLFactory.USER_GENDER)) {
            i = R.drawable.female_white;
        }
        with2.load(Integer.valueOf(i)).apply((BaseRequestOptions<?>) RequestOptions.circleCropTransform()).into(this.img_user);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (URLFactory.RELOAD_DASHBOARD) {
            init();
        } else {
            URLFactory.RELOAD_DASHBOARD = true;
        }
    }

    public void init() {
        initMenuScreen();
        FindViewById();
        Body();
        loadAds();
        getAllReminderData();
        fetchNextReminder();
    }

    public void initMenuScreen() {
        filter_cal = Calendar.getInstance();
        today_cal = Calendar.getInstance();
        yesterday_cal = Calendar.getInstance();
        yesterday_cal.add(5, -1);
        menuBody();
        this.lbl_toolbar_title.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Dashboard.filter_cal.setTimeInMillis(Screen_Dashboard.today_cal.getTimeInMillis());
                Screen_Dashboard.this.lbl_toolbar_title.setText(Screen_Dashboard.this.sh.get_string(R.string.str_today));
                Screen_Dashboard screen_Dashboard = Screen_Dashboard.this;
                Date_Helper date_Helper = Screen_Dashboard.this.dth;
                screen_Dashboard.setCustomDate(Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
            }
        });
        this.lbl_user_name.setText(this.ph.getString(URLFactory.USER_NAME));
    }

    public void menuBody() {
        this.btn_menu = (ImageView) findViewById(R.id.btn_menu);
        this.btn_alarm = (ImageView) findViewById(R.id.btn_alarm);
        this.lbl_toolbar_title = (AppCompatTextView) findViewById(R.id.lbl_toolbar_title);
        this.img_pre = (ImageView) findViewById(R.id.img_pre);
        this.img_next = (ImageView) findViewById(R.id.img_next);
        this.img_user = (ImageView) findViewById(R.id.img_user);
        this.open_profile = (LinearLayout) findViewById(R.id.open_profile);
        this.btn_rate_us = (LinearLayout) findViewById(R.id.btn_rate_us);
        this.btn_contact_us = (LinearLayout) findViewById(R.id.btn_contact_us);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        this.lbl_user_name = (AppCompatTextView) findViewById(R.id.lbl_user_name);
        loadPhoto();
        this.lbl_toolbar_title.setText(this.sh.get_string(R.string.str_today));
        this.lbl_user_name.setText(this.ph.getString(URLFactory.USER_NAME));
        this.menu_name.clear();
        this.menu_name.add(new Menu(this.sh.get_string(R.string.str_home), true));
        this.menu_name.add(new Menu(this.sh.get_string(R.string.str_drink_history), false));
        this.menu_name.add(new Menu(this.sh.get_string(R.string.str_drink_report), false));
        this.menu_name.add(new Menu(this.sh.get_string(R.string.str_settings), false));
        this.menu_name.add(new Menu(this.sh.get_string(R.string.str_faqs), false));
        this.menu_name.add(new Menu(this.sh.get_string(R.string.str_privacy_policy), false));
        this.menu_name.add(new Menu(this.sh.get_string(R.string.str_tell_a_friend), false));
        this.menuAdapter = new MenuAdapter(this.act, this.menu_name, new MenuAdapter.CallBack() {
            public void onClickSelect(Menu menu, int position) {
                Screen_Dashboard.this.mDrawerLayout.closeDrawer(Gravity.LEFT);
                if (position == 1) {
                    Screen_Dashboard.this.intent = new Intent(Screen_Dashboard.this.act, Screen_History.class);
                    Screen_Dashboard.this.startActivity(Screen_Dashboard.this.intent);
                } else if (position == 2) {
                    Screen_Dashboard.this.intent = new Intent(Screen_Dashboard.this.act, Screen_Report.class);
                    Screen_Dashboard.this.startActivity(Screen_Dashboard.this.intent);
                } else if (position == 3) {
                    Screen_Dashboard.this.intent = new Intent(Screen_Dashboard.this.act, Screen_Settings.class);
                    Screen_Dashboard.this.startActivity(Screen_Dashboard.this.intent);
                } else if (position == 4) {
                    Screen_Dashboard.this.intent = new Intent(Screen_Dashboard.this.act, Screen_FAQ.class);
                    Screen_Dashboard.this.startActivity(Screen_Dashboard.this.intent);
                } else if (position == 5) {
                    Intent i = new Intent("android.intent.action.VIEW");
                    i.setData(Uri.parse(URLFactory.PRIVACY_POLICY_ULR));
                    Screen_Dashboard.this.startActivity(i);
                } else if (position == 6) {
                    Screen_Dashboard.this.ih.ShareText(Screen_Dashboard.getApplicationName(Screen_Dashboard.this.mContext), Screen_Dashboard.this.sh.get_string(R.string.app_share_txt).replace("#1", URLFactory.APP_SHARE_URL));
                }
            }
        });
        this.btn_rate_us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String appPackageName = Screen_Dashboard.this.getPackageName();
                try {
                    Screen_Dashboard screen_Dashboard = Screen_Dashboard.this;
                    screen_Dashboard.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    Screen_Dashboard screen_Dashboard2 = Screen_Dashboard.this;
                    screen_Dashboard2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        this.btn_contact_us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("mailto:youremail@gmail.com"));
                    intent.putExtra("android.intent.extra.SUBJECT", "");
                    intent.putExtra("android.intent.extra.TEXT", "");
                    Screen_Dashboard.this.startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        this.mDrawerList.setLayoutManager(new LinearLayoutManager(getActivity(), androidx.recyclerview.widget.RecyclerView.VERTICAL, false));
        this.mDrawerList.setAdapter(this.menuAdapter);
        this.open_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (Screen_Dashboard.this.mDrawerLayout.isDrawerOpen(android.view.Gravity.LEFT)) {
                        Screen_Dashboard.this.mDrawerLayout.closeDrawer(android.view.Gravity.LEFT);
                    }
                } catch (Exception e) {
                }
                Screen_Dashboard.this.startActivity(new Intent(Screen_Dashboard.this.act, Screen_Profile.class));
            }
        });
        this.btn_alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Dashboard.this.showReminderDialog();
            }
        });
        this.btn_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    if (Screen_Dashboard.this.mDrawerLayout.isDrawerOpen(android.view.Gravity.LEFT)) {
                        Screen_Dashboard.this.mDrawerLayout.closeDrawer(android.view.Gravity.LEFT);
                    } else {
                        Screen_Dashboard.this.mDrawerLayout.openDrawer(android.view.Gravity.LEFT);
                    }
                } catch (Exception e) {
                }
            }
        });
        this.img_pre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Dashboard.filter_cal.add(5, -1);
                Date_Helper date_Helper = Screen_Dashboard.this.dth;
                String date = Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT);
                Date_Helper date_Helper2 = Screen_Dashboard.this.dth;
                if (date.equalsIgnoreCase(Date_Helper.getDate(Screen_Dashboard.yesterday_cal.getTimeInMillis(), URLFactory.DATE_FORMAT))) {
                    Screen_Dashboard.this.lbl_toolbar_title.setText(Screen_Dashboard.this.sh.get_string(R.string.str_yesterday));
                } else {
                    AppCompatTextView appCompatTextView = Screen_Dashboard.this.lbl_toolbar_title;
                    Date_Helper date_Helper3 = Screen_Dashboard.this.dth;
                    appCompatTextView.setText(Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
                }
                Screen_Dashboard screen_Dashboard = Screen_Dashboard.this;
                Date_Helper date_Helper4 = Screen_Dashboard.this.dth;
                screen_Dashboard.setCustomDate(Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
            }
        });
        this.img_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Dashboard.filter_cal.add(5, 1);
                if (Screen_Dashboard.filter_cal.getTimeInMillis() > Screen_Dashboard.today_cal.getTimeInMillis()) {
                    Screen_Dashboard.filter_cal.add(5, -1);
                    return;
                }
                Date_Helper date_Helper = Screen_Dashboard.this.dth;
                String date = Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT);
                Date_Helper date_Helper2 = Screen_Dashboard.this.dth;
                if (date.equalsIgnoreCase(Date_Helper.getDate(Screen_Dashboard.today_cal.getTimeInMillis(), URLFactory.DATE_FORMAT))) {
                    Screen_Dashboard.this.lbl_toolbar_title.setText(Screen_Dashboard.this.sh.get_string(R.string.str_today));
                } else {
                    Date_Helper date_Helper3 = Screen_Dashboard.this.dth;
                    String date2 = Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT);
                    Date_Helper date_Helper4 = Screen_Dashboard.this.dth;
                    if (date2.equalsIgnoreCase(Date_Helper.getDate(Screen_Dashboard.yesterday_cal.getTimeInMillis(), URLFactory.DATE_FORMAT))) {
                        Screen_Dashboard.this.lbl_toolbar_title.setText(Screen_Dashboard.this.sh.get_string(R.string.str_yesterday));
                    } else {
                        AppCompatTextView appCompatTextView = Screen_Dashboard.this.lbl_toolbar_title;
                        Date_Helper date_Helper5 = Screen_Dashboard.this.dth;
                        appCompatTextView.setText(Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
                    }
                }
                Screen_Dashboard screen_Dashboard = Screen_Dashboard.this;
                Date_Helper date_Helper6 = Screen_Dashboard.this.dth;
                screen_Dashboard.setCustomDate(Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
            }
        });
    }

    public void loadAds() {
        Screen_Dashboard.this.execute_add_water();
        Screen_Dashboard.this.btnclick = true;
    }

    public void getAllReminderData() {
        List<NextReminderModel> reminder_data = new ArrayList<>();
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_alarm_details");
        for (int k = 0; k < arr_data.size(); k++) {
            if (((String) arr_data.get(k).get("AlarmType")).equalsIgnoreCase("R")) {
                if (!this.ph.getBoolean(URLFactory.IS_MANUAL_REMINDER)) {
                    Database_Helper database_Helper = this.dh;
                    ArrayList<HashMap<String, String>> arr_data2 = database_Helper.getdata("tbl_alarm_sub_details", "SuperId='" + ((String) arr_data.get(k).get("id")) + "'");
                    for (int j = 0; j < arr_data2.size(); j++) {
                        reminder_data.add(new NextReminderModel(getMillisecond((String) arr_data2.get(j).get("AlarmTime")), (String) arr_data2.get(j).get("AlarmTime")));
                    }
                }
            } else if (this.ph.getBoolean(URLFactory.IS_MANUAL_REMINDER) && ((String) arr_data.get(k).get("IsOff")).equalsIgnoreCase("0")) {
                reminder_data.add(new NextReminderModel(getMillisecond((String) arr_data.get(k).get("AlarmTime")), (String) arr_data.get(k).get("AlarmTime")));
            }
        }
        Date mDate = new Date();
        Collections.sort(reminder_data);
        int tmp_pos = 0;
        int k2 = 0;
        while (true) {
            if (k2 >= reminder_data.size()) {
                break;
            } else if (reminder_data.get(k2).getMillesecond() > mDate.getTime()) {
                tmp_pos = k2;
                break;
            } else {
                k2++;
            }
        }
        this.next_reminder_block.setVisibility(View.VISIBLE);
        if (reminder_data.size() > 0) {
            this.lbl_next_reminder.setText(this.sh.get_string(R.string.str_next_reminder).replace("$1", reminder_data.get(tmp_pos).getTime()));
        } else {
            this.next_reminder_block.setVisibility(View.INVISIBLE);
        }
    }

    public long getMillisecond(String givenDateString) {
        long timeInMilliseconds = 0;
        try {
            timeInMilliseconds = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US).parse(this.dth.getFormatDate("yyyy-MM-dd") + " " + givenDateString).getTime();
            PrintStream printStream = System.out;
            printStream.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
            this.ah.Show_Alert_Dialog(e.getMessage());
            return timeInMilliseconds;
        }
    }

    public void fetchNextReminder() {
        this.runnableReminder = new Runnable() {
            public void run() {
                Screen_Dashboard.this.getAllReminderData();
                Screen_Dashboard.this.handlerReminder.postDelayed(Screen_Dashboard.this.runnableReminder, 1000);
            }
        };
        this.handlerReminder = new Handler();
        this.handlerReminder.postDelayed(this.runnableReminder, 1000);
    }

    private void FindViewById() {
        this.animationView = (LottieAnimationView) findViewById(R.id.animationView);
        this.content_frame = (RelativeLayout) findViewById(R.id.content_frame);
        this.content_frame_test = (RelativeLayout) findViewById(R.id.content_frame_test);
        this.content_frame.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int w = Screen_Dashboard.this.content_frame.getWidth();
                int h = Screen_Dashboard.this.content_frame.getHeight();
                Log.v("getWidthHeight", w + "   -   " + h);
            }
        });
        this.content_frame_test.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int w = Screen_Dashboard.this.content_frame_test.getWidth();
                int h = Screen_Dashboard.this.content_frame_test.getHeight();
                Log.v("getWidthHeight test", w + "   -   " + h);
                Screen_Dashboard.this.max_bottle_height = h + -30;
            }
        });
        this.lbl_next_reminder = (AppCompatTextView) findViewById(R.id.lbl_next_reminder);
        this.next_reminder_block = (LinearLayout) findViewById(R.id.next_reminder_block);
        this.add_water = (RelativeLayout) findViewById(R.id.add_water);
        this.container_name = (AppCompatTextView) findViewById(R.id.container_name);
        this.img_selected_container = (ImageView) findViewById(R.id.img_selected_container);
        this.selected_container_block = (RelativeLayout) findViewById(R.id.selected_container_block);
        this.open_history = (RelativeLayout) findViewById(R.id.open_history);
        this.lbl_total_goal = (AppCompatTextView) findViewById(R.id.lbl_total_goal);
        this.lbl_total_drunk = (AppCompatTextView) findViewById(R.id.lbl_total_drunk);
        this.banner_view = (LinearLayout) findViewById(R.id.banner_view);
    }

    private void Body() {
        Database_Helper database_Helper = this.dh;
        ArrayList<HashMap<String, String>> arr_data = database_Helper.getdata("tbl_drink_details", "DrinkDate ='" + this.dth.getCurrentDate(URLFactory.DATE_FORMAT) + "'");
        this.old_drink_water = 0.0f;
        for (int k = 0; k < arr_data.size(); k++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                this.old_drink_water = (float) (((double) this.old_drink_water) + Double.parseDouble("" + ((String) arr_data.get(k).get("ContainerValue"))));
            } else {
                this.old_drink_water = (float) (((double) this.old_drink_water) + Double.parseDouble("" + ((String) arr_data.get(k).get("ContainerValueOZ"))));
            }
        }
        count_today_drink(false);
        this.selected_container_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Dashboard.this.openChangeContainerPicker();
            }
        });
        this.open_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Dashboard.this.intent = new Intent(Screen_Dashboard.this.act, Screen_History.class);
                Screen_Dashboard.this.startActivity(Screen_Dashboard.this.intent);
            }
        });
        this.add_water.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_Dashboard.this.containerArrayList.size() > 0) {
                    Date_Helper date_Helper = Screen_Dashboard.this.dth;
                    String date = Date_Helper.getDate(Screen_Dashboard.filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT);
                    Date_Helper date_Helper2 = Screen_Dashboard.this.dth;
                    if (date.equalsIgnoreCase(Date_Helper.getDate(Screen_Dashboard.today_cal.getTimeInMillis(), URLFactory.DATE_FORMAT)) && Screen_Dashboard.this.btnclick) {
                        Screen_Dashboard.this.btnclick = false;
                        if (new Random().nextBoolean()) {
                            URLFactory.RELOAD_DASHBOARD = false;
                            Screen_Dashboard.this.execute_add_water();
                            URLFactory.RELOAD_DASHBOARD = true;
                        } else {
                            Screen_Dashboard.this.execute_add_water();
                        }
                    }
                }
            }
        });
        load_all_container();
        String unit = this.ph.getString(URLFactory.WATER_UNIT);
        if (unit.equalsIgnoreCase("ml")) {
            AppCompatTextView appCompatTextView = this.container_name;
            appCompatTextView.setText("" + this.containerArrayList.get(this.selected_pos).getContainerValue() + " " + unit);
            if (this.containerArrayList.get(this.selected_pos).isCustom()) {
                Glide.with(this.mContext).load(Integer.valueOf(R.drawable.ic_custom_ml)).into(this.img_selected_container);
            } else {
                Glide.with(this.mContext).load(getImage(this.containerArrayList.get(this.selected_pos).getContainerValue())).into(this.img_selected_container);
            }
        } else {
            AppCompatTextView appCompatTextView2 = this.container_name;
            appCompatTextView2.setText("" + this.containerArrayList.get(this.selected_pos).getContainerValueOZ() + " " + unit);
            if (this.containerArrayList.get(this.selected_pos).isCustom()) {
                Glide.with(this.mContext).load(Integer.valueOf(R.drawable.ic_custom_ml)).into(this.img_selected_container);
            } else {
                Glide.with(this.mContext).load(getImage(this.containerArrayList.get(this.selected_pos).getContainerValueOZ())).into(this.img_selected_container);
            }
        }
        this.adapter = new ContainerAdapterNew(this.act, this.containerArrayList, new ContainerAdapterNew.CallBack() {
            public void onClickSelect(Container menu, int position) {
                Screen_Dashboard.this.bottomSheetDialog.dismiss();
                Screen_Dashboard.this.selected_pos = position;
                Screen_Dashboard.this.ph.savePreferences(URLFactory.SELECTED_CONTAINER, Integer.parseInt(menu.getContainerId()));
                for (int k = 0; k < Screen_Dashboard.this.containerArrayList.size(); k++) {
                    Screen_Dashboard.this.containerArrayList.get(k).isSelected(false);
                }
                Screen_Dashboard.this.containerArrayList.get(position).isSelected(true);
                Screen_Dashboard.this.adapter.notifyDataSetChanged();
                String unit = Screen_Dashboard.this.ph.getString(URLFactory.WATER_UNIT);
                if (unit.equalsIgnoreCase("ml")) {
                    AppCompatTextView appCompatTextView = Screen_Dashboard.this.container_name;
                    appCompatTextView.setText("" + menu.getContainerValue() + " " + unit);
                    if (menu.isCustom()) {
                        Glide.with(Screen_Dashboard.this.mContext).load(Integer.valueOf(R.drawable.ic_custom_ml)).into(Screen_Dashboard.this.img_selected_container);
                    } else {
                        Glide.with(Screen_Dashboard.this.mContext).load(Screen_Dashboard.this.getImage(menu.getContainerValue())).into(Screen_Dashboard.this.img_selected_container);
                    }
                } else {
                    AppCompatTextView appCompatTextView2 = Screen_Dashboard.this.container_name;
                    appCompatTextView2.setText("" + menu.getContainerValueOZ() + " " + unit);
                    if (menu.isCustom()) {
                        Glide.with(Screen_Dashboard.this.mContext).load(Integer.valueOf(R.drawable.ic_custom_ml)).into(Screen_Dashboard.this.img_selected_container);
                    } else {
                        Glide.with(Screen_Dashboard.this.mContext).load(Screen_Dashboard.this.getImage(menu.getContainerValueOZ())).into(Screen_Dashboard.this.img_selected_container);
                    }
                }
            }
        });
    }

    public void execute_add_water() {
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") && this.drink_water > 8000.0f) {
            showDailyMoreThanTargetDialog();
            this.btnclick = true;
        } else if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") || this.drink_water <= 270.0f) {
            float count_drink_after_add_current_water = this.drink_water;
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                count_drink_after_add_current_water += Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValue());
            } else if (!URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                count_drink_after_add_current_water += Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValueOZ());
            }
            Log.d("above8000", "" + URLFactory.WATER_UNIT_VALUE + " @@@  " + this.drink_water + " @@@ " + count_drink_after_add_current_water);
            if (!URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") || count_drink_after_add_current_water <= 8000.0f) {
                if (!URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") && count_drink_after_add_current_water > 270.0f) {
                    if (this.drink_water >= 270.0f) {
                        showDailyMoreThanTargetDialog();
                    } else {
                        float f = URLFactory.DAILY_WATER_VALUE;
                        if (f < 270.0f - Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValueOZ())) {
                            showDailyMoreThanTargetDialog();
                        }
                    }
                }
            } else if (this.drink_water >= 8000.0f) {
                showDailyMoreThanTargetDialog();
            } else {
                float f2 = URLFactory.DAILY_WATER_VALUE;
                if (f2 < 8000.0f - Float.parseFloat("" + this.containerArrayList.get(this.selected_pos).getContainerValue())) {
                    showDailyMoreThanTargetDialog();
                }
            }
            if (this.drink_water == 8000.0f && URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                this.btnclick = true;
            } else if (this.drink_water != 270.0f || URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                if (!this.ph.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER)) {
                    this.ringtone.stop();
                    this.ringtone.play();
                }
                ContentValues initialValues = new ContentValues();
                initialValues.put("ContainerValue", "" + this.containerArrayList.get(this.selected_pos).getContainerValue());
                initialValues.put("ContainerValueOZ", "" + this.containerArrayList.get(this.selected_pos).getContainerValueOZ());
                StringBuilder sb = new StringBuilder();
                sb.append("");
                Date_Helper date_Helper = this.dth;
                sb.append(Date_Helper.getDate(filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
                initialValues.put("DrinkDate", sb.toString());
                initialValues.put("DrinkTime", "" + this.dth.getCurrentTime(true));
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                Date_Helper date_Helper2 = this.dth;
                sb2.append(Date_Helper.getDate(filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
                sb2.append(" ");
                sb2.append(this.dth.getCurrentDate("HH:mm:ss"));
                initialValues.put("DrinkDateTime", sb2.toString());
                if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                    initialValues.put("TodayGoal", "" + URLFactory.DAILY_WATER_VALUE);
                    initialValues.put("TodayGoalOZ", "" + HeightWeightHelper.mlToOzConverter((double) URLFactory.DAILY_WATER_VALUE));
                } else {
                    initialValues.put("TodayGoal", "" + HeightWeightHelper.ozToMlConverter((double) URLFactory.DAILY_WATER_VALUE));
                    initialValues.put("TodayGoalOZ", "" + URLFactory.DAILY_WATER_VALUE);
                }
                this.dh.INSERT("tbl_drink_details", initialValues);
                count_today_drink(true);
                Intent intent = new Intent(this.act, NewAppWidget.class);
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                intent.putExtra("appWidgetIds", AppWidgetManager.getInstance(this.act).getAppWidgetIds(new ComponentName(this.act, NewAppWidget.class)));
                this.act.sendBroadcast(intent);
            } else {
                this.btnclick = true;
            }
        } else {
            showDailyMoreThanTargetDialog();
            this.btnclick = true;
        }
    }

    public void load_all_container() {
        String selected_container_id;
        this.containerArrayList.clear();
        ArrayList<HashMap<String, String>> arr_container = this.dh.getdata("tbl_container_details", "IsCustom", 1);
        if (this.ph.getInt(URLFactory.SELECTED_CONTAINER) == 0) {
            selected_container_id = "1";
        } else {
            selected_container_id = "" + this.ph.getInt(URLFactory.SELECTED_CONTAINER);
        }
        for (int k = 0; k < arr_container.size(); k++) {
            Container container = new Container();
            container.setContainerId((String) arr_container.get(k).get("ContainerID"));
            container.setContainerValue((String) arr_container.get(k).get("ContainerValue"));
            container.setContainerValueOZ((String) arr_container.get(k).get("ContainerValueOZ"));
            container.isOpen(((String) arr_container.get(k).get("IsOpen")).equalsIgnoreCase("1"));
            container.isSelected(selected_container_id.equalsIgnoreCase((String) arr_container.get(k).get("ContainerID")));
            container.isCustom(((String) arr_container.get(k).get("IsCustom")).equalsIgnoreCase("1"));
            if (container.isSelected()) {
                this.selected_pos = k;
            }
            this.containerArrayList.add(container);
        }
    }

    public void count_today_drink(boolean isRegularAnimation) {
        Database_Helper database_Helper = this.dh;
        StringBuilder sb = new StringBuilder();
        sb.append("DrinkDate ='");
        Date_Helper date_Helper = this.dth;
        sb.append(Date_Helper.getDate(filter_cal.getTimeInMillis(), URLFactory.DATE_FORMAT));
        sb.append("'");
        ArrayList<HashMap<String, String>> arr_data = database_Helper.getdata("tbl_drink_details", sb.toString());
        this.drink_water = 0.0f;
        for (int k = 0; k < arr_data.size(); k++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                this.drink_water = (float) (((double) this.drink_water) + Double.parseDouble("" + ((String) arr_data.get(k).get("ContainerValue"))));
            } else {
                this.drink_water = (float) (((double) this.drink_water) + Double.parseDouble("" + ((String) arr_data.get(k).get("ContainerValueOZ"))));
            }
        }
        AppCompatTextView appCompatTextView = this.lbl_total_drunk;
        appCompatTextView.setText(getData("" + ((int) this.drink_water) + " " + URLFactory.WATER_UNIT_VALUE));
        AppCompatTextView appCompatTextView2 = this.lbl_total_goal;
        appCompatTextView2.setText(getData("" + ((int) URLFactory.DAILY_WATER_VALUE) + " " + URLFactory.WATER_UNIT_VALUE));
        refresh_bottle(true, isRegularAnimation);
    }

    public String getData(String str) {
        return str.replace(",", ".");
    }

    public void callDialog() {
        if (this.old_drink_water < URLFactory.DAILY_WATER_VALUE && this.drink_water >= URLFactory.DAILY_WATER_VALUE) {
            showDailyGoalReachedDialog();
        }
        this.old_drink_water = this.drink_water;
    }

    public void refresh_bottle(boolean isFromCurrentProgress, boolean isRegularAnimation) {
        final long animationDuration = isRegularAnimation ? 50 : 5;
        if (!(this.handler == null || this.runnable == null)) {
            this.handler.removeCallbacks(this.runnable);
        }
        this.btnclick = false;
        this.cp = this.progress_bottle_height;
        this.np = Math.round((this.drink_water * ((float) this.max_bottle_height)) / URLFactory.DAILY_WATER_VALUE);
        if (this.cp <= this.np && isFromCurrentProgress) {
            this.animationView.setVisibility(View.VISIBLE);
            this.runnable = new Runnable() {
                public void run() {
                    if (Screen_Dashboard.this.cp > Screen_Dashboard.this.max_bottle_height) {
                        Screen_Dashboard.this.btnclick = true;
                        Screen_Dashboard.this.callDialog();
                    } else if (Screen_Dashboard.this.cp < Screen_Dashboard.this.np) {
                        Screen_Dashboard.this.cp += 6;
                        Screen_Dashboard.this.content_frame.getLayoutParams().height = Screen_Dashboard.this.cp;
                        Screen_Dashboard.this.content_frame.requestLayout();
                        Screen_Dashboard.this.handler.postDelayed(Screen_Dashboard.this.runnable, animationDuration);
                    } else {
                        Screen_Dashboard.this.btnclick = true;
                        Screen_Dashboard.this.callDialog();
                    }
                }
            };
            this.handler = new Handler();
            this.handler.postDelayed(this.runnable, animationDuration);
        } else if (this.np == 0) {
            this.animationView.setVisibility(View.GONE);
            this.content_frame.getLayoutParams().height = this.np;
            this.content_frame.requestLayout();
            this.btnclick = true;
            callDialog();
        } else {
            this.content_frame.getLayoutParams().height = 0;
            this.cp = 0;
            this.animationView.setVisibility(View.VISIBLE);
            this.runnable = new Runnable() {
                public void run() {
                    if (Screen_Dashboard.this.cp > Screen_Dashboard.this.max_bottle_height) {
                        Screen_Dashboard.this.btnclick = true;
                        Screen_Dashboard.this.callDialog();
                    } else if (Screen_Dashboard.this.cp < Screen_Dashboard.this.np) {
                        Screen_Dashboard.this.cp += 6;
                        Screen_Dashboard.this.content_frame.getLayoutParams().height = Screen_Dashboard.this.cp;
                        Screen_Dashboard.this.content_frame.requestLayout();
                        Screen_Dashboard.this.handler.postDelayed(Screen_Dashboard.this.runnable, animationDuration);
                    } else {
                        Screen_Dashboard.this.btnclick = true;
                        Screen_Dashboard.this.callDialog();
                    }
                }
            };
            this.handler = new Handler();
            this.handler.postDelayed(this.runnable, animationDuration);
        }
        this.progress_bottle_height = this.np;
        if (this.np > 0) {
            this.animationView.setVisibility(View.VISIBLE);
        } else {
            this.animationView.setVisibility(View.GONE);
        }
    }

    public void count_specific_day_drink(String custom_date) {
        ArrayList<HashMap<String, String>> arr_dataO = this.dh.getdata("tbl_drink_details", "DrinkDate ='" + custom_date + "'");
        this.old_drink_water = 0.0f;
        for (int k = 0; k < arr_dataO.size(); k++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                this.old_drink_water = (float) (((double) this.old_drink_water) + Double.parseDouble("" + ((String) arr_dataO.get(k).get("ContainerValue"))));
            } else {
                this.old_drink_water = (float) (((double) this.old_drink_water) + Double.parseDouble("" + ((String) arr_dataO.get(k).get("ContainerValueOZ"))));
            }
        }
        ArrayList<HashMap<String, String>> arr_data22 = this.dh.getdata("tbl_drink_details", "DrinkDate ='" + custom_date + "'", "id", 1);
        double total_drink = Utils.DOUBLE_EPSILON;
        if (arr_data22.size() > 0) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                total_drink = Double.parseDouble((String) arr_data22.get(0).get("TodayGoal"));
            } else {
                total_drink = Double.parseDouble((String) arr_data22.get(0).get("TodayGoalOZ"));
            }
        }
        ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_drink_details", "DrinkDate ='" + custom_date + "'");
        this.drink_water = 0.0f;
        for (int k2 = 0; k2 < arr_data.size(); k2++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                this.drink_water += (float) Integer.parseInt((String) arr_data.get(k2).get("ContainerValue"));
            } else {
                this.drink_water += (float) Integer.parseInt((String) arr_data.get(k2).get("ContainerValueOZ"));
            }
        }
        if (custom_date.equalsIgnoreCase(this.dth.getCurrentDate(URLFactory.DATE_FORMAT))) {
            URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.DAILY_WATER);
        } else if (total_drink > Utils.DOUBLE_EPSILON) {
            URLFactory.DAILY_WATER_VALUE = Float.parseFloat("" + total_drink);
        } else {
            URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.DAILY_WATER);
        }
        this.lbl_total_drunk.setText(getData("" + ((int) this.drink_water) + " " + URLFactory.WATER_UNIT_VALUE));
        this.lbl_total_goal.setText(getData("" + ((int) URLFactory.DAILY_WATER_VALUE) + " " + URLFactory.WATER_UNIT_VALUE));
        refresh_bottle(false, false);
    }

    public Activity getActivity() {
        return this.act;
    }

    public void setCustomDate(String date) {
        count_specific_day_drink(date);
    }

    public void openChangeContainerPicker() {
        this.bottomSheetDialog = new BottomSheetDialog(this.act);
        this.bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                FrameLayout bottomSheet = (FrameLayout) ((BottomSheetDialog) dialog).findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior from = BottomSheetBehavior.from(bottomSheet);
                    bottomSheet.setBackground((Drawable) null);
                }
            }
        });
        View view = LayoutInflater.from(this.act).inflate(R.layout.bottom_sheet_change_container, (ViewGroup) null, false);
        RecyclerView containerRecyclerViewN = (RecyclerView) view.findViewById(R.id.containerRecyclerView);
        containerRecyclerViewN.setLayoutManager(new GridLayoutManager((Context) this.act, 3, RecyclerView.VERTICAL, false));
        containerRecyclerViewN.setAdapter(this.adapter);
        ((RelativeLayout) view.findViewById(R.id.add_custom_container)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Dashboard.this.bottomSheetDialog.dismiss();
                Screen_Dashboard.this.openCustomContainerPicker();
            }
        });
        this.bottomSheetDialog.setContentView(view);
        this.bottomSheetDialog.show();
    }

    public void openCustomContainerPicker() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.bottom_sheet_add_custom_container, (ViewGroup) null, false);
        RelativeLayout btn_cancel = (RelativeLayout) view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_add = (RelativeLayout) view.findViewById(R.id.btn_add);
        ImageView img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        final AppCompatEditText txt_value = (AppCompatEditText) view.findViewById(R.id.txt_value);
        ((AppCompatTextView) view.findViewById(R.id.lbl_unit)).setText(this.sh.get_string(R.string.str_capacity).replace("$1", URLFactory.WATER_UNIT_VALUE));
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            txt_value.setFilters(new InputFilter[]{new InputFilterWeightRange(1.0d, 8000.0d)});
        } else {
            txt_value.setFilters(new InputFilter[]{new InputFilterWeightRange(1.0d, 270.0d)});
        }
        txt_value.requestFocus();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        img_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                double tfloz;
                double tml;
                if (Screen_Dashboard.this.sh.check_blank_data(txt_value.getText().toString().trim())) {
                    Screen_Dashboard.this.ah.customAlert(Screen_Dashboard.this.sh.get_string(R.string.str_enter_value_validation));
                } else if (Integer.parseInt(txt_value.getText().toString().trim()) == 0) {
                    Screen_Dashboard.this.ah.customAlert(Screen_Dashboard.this.sh.get_string(R.string.str_enter_value_validation));
                } else {
                    if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                        tml = (double) Float.parseFloat(txt_value.getText().toString().trim());
                        tfloz = HeightWeightHelper.mlToOzConverter(tml);
                    } else {
                        tfloz = (double) Float.parseFloat(txt_value.getText().toString().trim());
                        tml = HeightWeightHelper.ozToMlConverter(tfloz);
                    }
                    Log.d("HeightWeightHelper", "" + tml + " @@@ " + tfloz);
                    Cursor c = Constant.SDB.rawQuery("SELECT MAX(ContainerID) FROM tbl_container_details", (String[]) null);
                    int nextContainerID = 0;
                    try {
                        if (c.getCount() > 0) {
                            c.moveToNext();
                            nextContainerID = Integer.parseInt(c.getString(0)) + 1;
                        }
                    } catch (Exception e) {
                    }
                    ContentValues initialValues = new ContentValues();
                    initialValues.put("ContainerID", "" + nextContainerID);
                    initialValues.put("ContainerValue", "" + Math.round(tml));
                    initialValues.put("ContainerValueOZ", "" + Math.round(tfloz));
                    initialValues.put("IsOpen", "1");
                    initialValues.put("IsCustom", "1");
                    Screen_Dashboard.this.dh.INSERT("tbl_container_details", initialValues);
                    Screen_Dashboard.this.load_all_container();
                    Screen_Dashboard.this.ph.savePreferences(URLFactory.SELECTED_CONTAINER, nextContainerID);
                    int tmp_pos = -1;
                    for (int k = 0; k < Screen_Dashboard.this.containerArrayList.size(); k++) {
                        try {
                            if (nextContainerID == Integer.parseInt(Screen_Dashboard.this.containerArrayList.get(k).getContainerId())) {
                                Screen_Dashboard.this.containerArrayList.get(k).isSelected(true);
                                tmp_pos = k;
                            } else {
                                Screen_Dashboard.this.containerArrayList.get(k).isSelected(false);
                            }
                        } catch (Exception e2) {
                            Screen_Dashboard.this.containerArrayList.get(k).isSelected(false);
                        }
                    }
                    String unit = Screen_Dashboard.this.ph.getString(URLFactory.WATER_UNIT);
                    if (tmp_pos >= 0) {
                        Screen_Dashboard.this.selected_pos = tmp_pos;
                        Container menu = Screen_Dashboard.this.containerArrayList.get(tmp_pos);
                        if (unit.equalsIgnoreCase("ml")) {
                            Screen_Dashboard.this.container_name.setText("" + menu.getContainerValue() + " " + unit);
                            if (menu.isCustom()) {
                                Glide.with(Screen_Dashboard.this.mContext).load(Integer.valueOf(R.drawable.ic_custom_ml)).into(Screen_Dashboard.this.img_selected_container);
                            } else {
                                Glide.with(Screen_Dashboard.this.mContext).load(Screen_Dashboard.this.getImage(menu.getContainerValue())).into(Screen_Dashboard.this.img_selected_container);
                            }
                        } else {
                            Screen_Dashboard.this.container_name.setText("" + menu.getContainerValueOZ() + " " + unit);
                            if (menu.isCustom()) {
                                Glide.with(Screen_Dashboard.this.mContext).load(Integer.valueOf(R.drawable.ic_custom_ml)).into(Screen_Dashboard.this.img_selected_container);
                            } else {
                                Glide.with(Screen_Dashboard.this.mContext).load(Screen_Dashboard.this.getImage(menu.getContainerValueOZ())).into(Screen_Dashboard.this.img_selected_container);
                            }
                        }
                    }
                    Screen_Dashboard.this.adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public Integer getImage(String val) {
        Integer drawable = Integer.valueOf(R.drawable.ic_custom_ml);
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            if (Double.parseDouble(val) == 50.0d) {
                return Integer.valueOf(R.drawable.ic_50_ml);
            }
            if (Double.parseDouble(val) == 100.0d) {
                return Integer.valueOf(R.drawable.ic_100_ml);
            }
            if (Double.parseDouble(val) == 150.0d) {
                return Integer.valueOf(R.drawable.ic_150_ml);
            }
            if (Double.parseDouble(val) == 200.0d) {
                return Integer.valueOf(R.drawable.ic_200_ml);
            }
            if (Double.parseDouble(val) == 250.0d) {
                return Integer.valueOf(R.drawable.ic_250_ml);
            }
            if (Double.parseDouble(val) == 300.0d) {
                return Integer.valueOf(R.drawable.ic_300_ml);
            }
            if (Double.parseDouble(val) == 500.0d) {
                return Integer.valueOf(R.drawable.ic_500_ml);
            }
            if (Double.parseDouble(val) == 600.0d) {
                return Integer.valueOf(R.drawable.ic_600_ml);
            }
            if (Double.parseDouble(val) == 700.0d) {
                return Integer.valueOf(R.drawable.ic_700_ml);
            }
            if (Double.parseDouble(val) == 800.0d) {
                return Integer.valueOf(R.drawable.ic_800_ml);
            }
            if (Double.parseDouble(val) == 900.0d) {
                return Integer.valueOf(R.drawable.ic_900_ml);
            }
            if (Double.parseDouble(val) == 1000.0d) {
                return Integer.valueOf(R.drawable.ic_1000_ml);
            }
            return drawable;
        } else if (Double.parseDouble(val) == 2.0d) {
            return Integer.valueOf(R.drawable.ic_50_ml);
        } else {
            if (Double.parseDouble(val) == 3.0d) {
                return Integer.valueOf(R.drawable.ic_100_ml);
            }
            if (Double.parseDouble(val) == 5.0d) {
                return Integer.valueOf(R.drawable.ic_150_ml);
            }
            if (Double.parseDouble(val) == 7.0d) {
                return Integer.valueOf(R.drawable.ic_200_ml);
            }
            if (Double.parseDouble(val) == 8.0d) {
                return Integer.valueOf(R.drawable.ic_250_ml);
            }
            if (Double.parseDouble(val) == 10.0d) {
                return Integer.valueOf(R.drawable.ic_300_ml);
            }
            if (Double.parseDouble(val) == 17.0d) {
                return Integer.valueOf(R.drawable.ic_500_ml);
            }
            if (Double.parseDouble(val) == 20.0d) {
                return Integer.valueOf(R.drawable.ic_600_ml);
            }
            if (Double.parseDouble(val) == 24.0d) {
                return Integer.valueOf(R.drawable.ic_700_ml);
            }
            if (Double.parseDouble(val) == 27.0d) {
                return Integer.valueOf(R.drawable.ic_800_ml);
            }
            if (Double.parseDouble(val) == 30.0d) {
                return Integer.valueOf(R.drawable.ic_900_ml);
            }
            if (Double.parseDouble(val) == 34.0d) {
                return Integer.valueOf(R.drawable.ic_1000_ml);
            }
            return drawable;
        }
    }

    public void showDailyGoalReachedDialog() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_goal_reached, (ViewGroup) null, false);
        ((ImageView) view.findViewById(R.id.img_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((RelativeLayout) view.findViewById(R.id.btn_share)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                String packageName = Screen_Dashboard.this.mContext.getPackageName();
                String str = Screen_Dashboard.this.sh.get_string(R.string.str_share_text);
                String share_text = str.replace("$1", "" + ((int) Screen_Dashboard.this.drink_water) + " " + URLFactory.WATER_UNIT_VALUE);
                StringBuilder sb = new StringBuilder();
                sb.append("@ ");
                sb.append(URLFactory.APP_SHARE_URL);
                Screen_Dashboard.this.ih.ShareText(Screen_Dashboard.getApplicationName(Screen_Dashboard.this.mContext), share_text.replace("$2", sb.toString()));
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void showDailyMoreThanTargetDialog() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_goal_target_reached, (ViewGroup) null, false);
        AppCompatTextView lbl_desc = (AppCompatTextView) view.findViewById(R.id.lbl_desc);
        ImageView img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        View findViewById = view.findViewById(R.id.btn_share);
        ImageView img_bottle = (ImageView) view.findViewById(R.id.img_bottle);
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            img_bottle.setImageResource(R.drawable.ic_limit_ml);
        } else {
            img_bottle.setImageResource(R.drawable.ic_limit_oz);
        }
        lbl_desc.setText(this.sh.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1", URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") ? "8000 ml" : "270 fl oz"));
        img_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void showReminderDialog() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_reminder, (ViewGroup) null, false);
        ImageView img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        RelativeLayout btn_save = (RelativeLayout) view.findViewById(R.id.btn_save);
        final RelativeLayout off_block = (RelativeLayout) view.findViewById(R.id.off_block);
        final RelativeLayout silent_block = (RelativeLayout) view.findViewById(R.id.silent_block);
        final RelativeLayout auto_block = (RelativeLayout) view.findViewById(R.id.auto_block);
        final ImageView img_off = (ImageView) view.findViewById(R.id.img_off);
        final ImageView img_silent = (ImageView) view.findViewById(R.id.img_silent);
        final ImageView img_auto = (ImageView) view.findViewById(R.id.img_auto);
        AppCompatTextView advance_settings = (AppCompatTextView) view.findViewById(R.id.advance_settings);
        advance_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                Screen_Dashboard.this.startActivity(new Intent(Screen_Dashboard.this.act, Screen_Reminder.class));
            }
        });
        LinearLayout custom_sound_block = (LinearLayout) view.findViewById(R.id.custom_sound_block);
        custom_sound_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Dashboard.this.openSoundMenuPicker();
            }
        });
        SwitchCompat switch_vibrate2 = (SwitchCompat) view.findViewById(R.id.switch_vibrate);
        switch_vibrate2.setChecked(!this.ph.getBoolean(URLFactory.REMINDER_VIBRATE));
        switch_vibrate2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Screen_Dashboard.this.ph.savePreferences(URLFactory.REMINDER_VIBRATE, !isChecked);
            }
        });
        if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 1) {
            off_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
            img_off.setImageResource(R.drawable.ic_off_selected);
            silent_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
            img_silent.setImageResource(R.drawable.ic_silent_normal);
            auto_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
            img_auto.setImageResource(R.drawable.ic_auto_normal);
        } else if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 2) {
            off_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
            img_off.setImageResource(R.drawable.ic_off_normal);
            silent_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
            img_silent.setImageResource(R.drawable.ic_silent_selected);
            auto_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
            img_auto.setImageResource(R.drawable.ic_auto_normal);
        } else {
            off_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
            img_off.setImageResource(R.drawable.ic_off_normal);
            silent_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
            img_silent.setImageResource(R.drawable.ic_silent_normal);
            auto_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
            img_auto.setImageResource(R.drawable.ic_auto_selected);
        }

        off_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                off_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
                img_off.setImageResource(R.drawable.ic_off_selected);
                silent_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
                img_silent.setImageResource(R.drawable.ic_silent_normal);
                auto_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
                img_auto.setImageResource(R.drawable.ic_auto_normal);
                Screen_Dashboard.this.ph.savePreferences(URLFactory.REMINDER_OPTION, 1);
            }
        });
        silent_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                off_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
                img_off.setImageResource(R.drawable.ic_off_normal);
                silent_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
                img_silent.setImageResource(R.drawable.ic_silent_selected);
                auto_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
                img_auto.setImageResource(R.drawable.ic_auto_normal);
                Screen_Dashboard.this.ph.savePreferences(URLFactory.REMINDER_OPTION, 2);
            }
        });
        auto_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                off_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
                img_off.setImageResource(R.drawable.ic_off_normal);
                silent_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
                img_silent.setImageResource(R.drawable.ic_silent_normal);
                auto_block.setBackground(Screen_Dashboard.this.mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
                img_auto.setImageResource(R.drawable.ic_auto_selected);
                Screen_Dashboard.this.ph.savePreferences(URLFactory.REMINDER_OPTION, 0);
            }
        });
        img_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void openSoundMenuPicker() {
        loadSounds();
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_sound_pick, (ViewGroup) null, false);
        ((RelativeLayout) view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((RelativeLayout) view.findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int k = 0;
                while (true) {
                    if (k >= Screen_Dashboard.this.lst_sounds.size()) {
                        break;
                    } else if (Screen_Dashboard.this.lst_sounds.get(k).isSelected()) {
                        Screen_Dashboard.this.ph.savePreferences(URLFactory.REMINDER_SOUND, k);
                        break;
                    } else {
                        k++;
                    }
                }
                dialog.dismiss();
            }
        });
        RecyclerView soundRecyclerView = (RecyclerView) view.findViewById(R.id.soundRecyclerView);
        this.soundAdapter = new SoundAdapter(this.act, this.lst_sounds, new SoundAdapter.CallBack() {
            public void onClickSelect(SoundModel time, int position) {
                for (int k = 0; k < Screen_Dashboard.this.lst_sounds.size(); k++) {
                    Screen_Dashboard.this.lst_sounds.get(k).isSelected(false);
                }
                Screen_Dashboard.this.lst_sounds.get(position).isSelected(true);
                Screen_Dashboard.this.soundAdapter.notifyDataSetChanged();
                Screen_Dashboard.this.playSound(position);
            }
        });
        soundRecyclerView.setLayoutManager(new LinearLayoutManager(this.act, RecyclerView.VERTICAL, false));
        soundRecyclerView.setAdapter(this.soundAdapter);
        dialog.setContentView(view);
        dialog.show();
    }

    public void loadSounds() {
        this.lst_sounds.clear();
        this.lst_sounds.add(getSoundModel(0, "Default"));
        this.lst_sounds.add(getSoundModel(1, "Bell"));
        this.lst_sounds.add(getSoundModel(2, "Blop"));
        this.lst_sounds.add(getSoundModel(3, "Bong"));
        this.lst_sounds.add(getSoundModel(4, "Click"));
        this.lst_sounds.add(getSoundModel(5, "Echo droplet"));
        this.lst_sounds.add(getSoundModel(6, "Mario droplet"));
        this.lst_sounds.add(getSoundModel(7, "Ship bell"));
        this.lst_sounds.add(getSoundModel(8, "Simple droplet"));
        this.lst_sounds.add(getSoundModel(9, "Tiny droplet"));
    }

    public SoundModel getSoundModel(int index, String name) {
        SoundModel soundModel = new SoundModel();
        soundModel.setId(index);
        soundModel.setName(name);
        soundModel.isSelected(this.ph.getInt(URLFactory.REMINDER_SOUND) == index);
        return soundModel;
    }

    public void playSound(int idx) {
        MediaPlayer mp = null;
        if (idx == 0) {
            mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        } else if (idx == 1) {
            mp = MediaPlayer.create(this, R.raw.bell);
        } else if (idx == 2) {
            mp = MediaPlayer.create(this, R.raw.blop);
        } else if (idx == 3) {
            mp = MediaPlayer.create(this, R.raw.bong);
        } else if (idx == 4) {
            mp = MediaPlayer.create(this, R.raw.click);
        } else if (idx == 5) {
            mp = MediaPlayer.create(this, R.raw.echo_droplet);
        } else if (idx == 6) {
            mp = MediaPlayer.create(this, R.raw.mario_droplet);
        } else if (idx == 7) {
            mp = MediaPlayer.create(this, R.raw.ship_bell);
        } else if (idx == 8) {
            mp = MediaPlayer.create(this, R.raw.simple_droplet);
        } else if (idx == 9) {
            mp = MediaPlayer.create(this, R.raw.tiny_droplet);
        }
        mp.start();
    }

    public void onBackPressed() {
        try {
            if (this.mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                this.mDrawerLayout.closeDrawer(android.view.Gravity.LEFT);
            } else {
                finish();
            }
        } catch (Exception e) {
        }
    }
}
