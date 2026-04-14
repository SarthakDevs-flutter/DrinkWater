package com.trending.water.drinking.reminder.utils;

import android.media.Ringtone;
import java.text.DecimalFormat;

public class URLFactory {
    // Water Calculation Constants
    public static final double ACTIVE_FEMALE_WATER = 40.0;
    public static final double ACTIVE_MALE_WATER = 50.0;
    public static final double DEACTIVE_FEMALE_WATER = 11.43;
    public static final double DEACTIVE_MALE_WATER = 14.29;
    public static final double FEMALE_WATER = 28.57;
    public static final double MALE_WATER = 35.71;
    public static final double PREGNANT_WATER_ADDITIONAL = 700.0;
    public static final double BREASTFEEDING_WATER_ADDITIONAL = 700.0;

    // Weather Multipliers
    public static final double WEATHER_SUNNY = 1.0;
    public static final double WEATHER_CLOUDY = 0.85;
    public static final double WEATHER_SNOW = 0.88;
    public static final double WEATHER_RAINY = 0.68;

    // Preference Keys
    public static final String KEY_USER_NAME = "user_name";
    public static final String USER_NAME = KEY_USER_NAME;
    public static final String KEY_USER_GENDER = "user_gender";
    public static final String USER_GENDER = KEY_USER_GENDER;
    public static final String KEY_USER_PHOTO = "user_photo";
    public static final String KEY_PERSON_WEIGHT = "person_weight";
    public static final String PERSON_WEIGHT = KEY_PERSON_WEIGHT;
    public static final String KEY_PERSON_WEIGHT_UNIT = "person_weight_unit";
    public static final String PERSON_WEIGHT_UNIT = KEY_PERSON_WEIGHT_UNIT;
    public static final String KEY_PERSON_HEIGHT = "person_height";
    public static final String PERSON_HEIGHT = KEY_PERSON_HEIGHT;
    public static final String KEY_PERSON_HEIGHT_UNIT = "person_height_unit";
    public static final String PERSON_HEIGHT_UNIT = KEY_PERSON_HEIGHT_UNIT;
    public static final String KEY_WATER_UNIT = "water_unit";
    public static final String WATER_UNIT = KEY_WATER_UNIT;
    public static final String KEY_DAILY_WATER_GOAL = "daily_water";
    public static final String DAILY_WATER = KEY_DAILY_WATER_GOAL;
    public static final String KEY_SET_MANUALLY_GOAL = "set_manually_goal";
    public static final String KEY_SET_MANUALLY_GOAL_VALUE = "set_manually_goal_value";
    
    public static final String KEY_WAKE_UP_TIME = "wakeup_time";
    public static final String KEY_WAKE_UP_HOUR = "wakeup_time_hour";
    public static final String KEY_WAKE_UP_MINUTE = "wakeup_time_minute";
    public static final String KEY_BED_TIME = "bed_time";
    public static final String KEY_BED_TIME_HOUR = "bed_time_hour";
    public static final String KEY_BED_TIME_MINUTE = "bed_time_minute";
    
    public static final String KEY_INTERVAL = "interval";
    public static final String KEY_REMINDER_OPTION = "reminder_option";
    public static final String REMINDER_OPTION = KEY_REMINDER_OPTION;
    public static final String KEY_REMINDER_SOUND = "reminder_sound";
    public static final String REMINDER_SOUND = KEY_REMINDER_SOUND;
    public static final String KEY_REMINDER_VIBRATE = "reminder_vibrate";
    public static final String REMINDER_VIBRATE = KEY_REMINDER_VIBRATE;
    public static final String KEY_IS_MANUAL_REMINDER = "manual_reminder_active";
    public static final String IS_MANUAL_REMINDER = KEY_IS_MANUAL_REMINDER;
    
    public static final String KEY_IS_ACTIVE = "is_active";
    public static final String KEY_IS_PREGNANT = "is_pregnant";
    public static final String KEY_IS_BREASTFEEDING = "is_breastfeeding";
    public static final String KEY_WEATHER_CONDITIONS = "weather_conditions";
    
    public static final String KEY_HIDE_WELCOME_SCREEN = "hide_welcome_screen";
    public static final String KEY_DISABLE_NOTIFICATION = "disable_notification";
    public static final String DISABLE_NOTIFICATION = KEY_DISABLE_NOTIFICATION;
    public static final String KEY_DISABLE_SOUND_ON_ADD = "disable_sound_when_add_water";
    public static final String DISABLE_SOUND_WHEN_ADD_WATER = KEY_DISABLE_SOUND_ON_ADD;
    public static final String KEY_SELECTED_CONTAINER = "selected_container";
    public static final String KEY_IGNORE_NEXT_STEP = "ignore_next_step";
    
    public static final String KEY_AUTO_BACKUP = "auto_backup";
    public static final String AUTO_BACK_UP = KEY_AUTO_BACKUP;
    public static final String KEY_AUTO_BACKUP_ID = "auto_backup_id";
    public static final String AUTO_BACK_UP_ID = KEY_AUTO_BACKUP_ID;
    public static final String KEY_AUTO_BACKUP_TYPE = "auto_backup_type";
    public static final String AUTO_BACK_UP_TYPE = KEY_AUTO_BACKUP_TYPE;

    // Application Constants
    public static final String APP_DIRECTORY_NAME = "Water Diary";
    public static final String PROFILE_DIR_NAME = "profile";
    public static final String APP_SHARE_URL = "https://share.html";
    public static final String PRIVACY_POLICY_URL = "https://privacy-policy.html";
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    // Static Mutables (Consider moving to a Manager later)
    public static float dailyWaterValue = 0.0f;
    public static String waterUnitValue = "ML";
    public static boolean reloadDashboard = true;
    public static Ringtone notificationRingtone;

    // Formatters
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");
    public static final DecimalFormat DECIMAL_FORMAT_SINGLE = new DecimalFormat("#0.0");
}
