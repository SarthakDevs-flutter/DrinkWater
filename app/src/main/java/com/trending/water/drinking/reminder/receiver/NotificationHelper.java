package com.trending.water.drinking.reminder.receiver;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.Screen_Dashboard;
import com.trending.water.drinking.reminder.Screen_Select_Bottle;
import com.trending.water.drinking.reminder.Screen_Select_Snooze;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

class NotificationHelper {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String NOTIFICATION_SILENT_CHANNEL_ID = "10002";
    private static final String NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID = "10004";
    private static final String NOTIFICATION_VIBRATE_CHANNEL_ID = "10003";
    Date_Helper dth = new Date_Helper();
    Preferences_Helper ph;
    private Context mContext;

    NotificationHelper(Context context) {
        this.mContext = context;
        this.ph = new Preferences_Helper(this.mContext);
        if (URLFactory.notification_ringtone == null) {
            URLFactory.notification_ringtone = RingtoneManager.getRingtone(this.mContext, getSound());
        }
    }

    /* access modifiers changed from: package-private */
    public void createNotification() {
        Log.d("createNotification", "" + this.ph.getInt(URLFactory.REMINDER_OPTION));
        Log.d("createNotification V", "" + this.ph.getBoolean(URLFactory.REMINDER_VIBRATE));
        if (this.ph.getInt(URLFactory.REMINDER_OPTION) != 1) {
            if (!reachedDailyGoal() || !this.ph.getBoolean(URLFactory.DISABLE_NOTIFICATION)) {
                Intent intent = new Intent(this.mContext, Screen_Dashboard.class);
                intent.setFlags(603979776);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this.mContext, 0, intent, 134217728);
                Intent snoozeIntent = new Intent(this.mContext, Screen_Select_Snooze.class);
                snoozeIntent.setAction("SNOOZE_ACTION");
                snoozeIntent.setFlags(603979776);
                PendingIntent snoozePendingIntent = PendingIntent.getActivity(this.mContext, 0, snoozeIntent, 134217728);
                Intent addWaterIntent = new Intent(this.mContext, Screen_Select_Bottle.class);
                addWaterIntent.setAction("ADD_WATER_ACTION");
                addWaterIntent.setFlags(603979776);
                PendingIntent addWaterPendingIntent = PendingIntent.getActivity(this.mContext, 0, addWaterIntent, 134217728);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.mContext);
                mBuilder.setSmallIcon(R.drawable.ic_small_app_icon);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.mContext.getResources(), R.mipmap.ic_launcher));
                NotificationCompat.Builder contentTitle = mBuilder.setContentTitle(this.mContext.getResources().getString(R.string.str_drink_water));
                contentTitle.setContentText("" + get_today_report()).setAutoCancel(true).setContentIntent(resultPendingIntent).setColor(ContextCompat.getColor(this.mContext, R.color.colorPrimary));
                if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 0 && !this.ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                    mBuilder.setDefaults(-1);
                    if (Build.VERSION.SDK_INT < 26) {
                        mBuilder.setSound(getSound());
                    }
                } else if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 0 && this.ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                    mBuilder.setDefaults(1);
                    if (Build.VERSION.SDK_INT < 26) {
                        mBuilder.setSound(getSound());
                    }
                } else if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 2 && !this.ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                    mBuilder.setDefaults(2);
                    if (Build.VERSION.SDK_INT < 26) {
                        mBuilder.setSound((Uri) null);
                    }
                } else if (Build.VERSION.SDK_INT < 26) {
                    mBuilder.setSound((Uri) null);
                }
                mBuilder.addAction(R.drawable.ic_plus, this.mContext.getResources().getString(R.string.str_add_water), addWaterPendingIntent);
                mBuilder.addAction(R.drawable.ic_notification, this.mContext.getResources().getString(R.string.str_snooze), snoozePendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
                if (Build.VERSION.SDK_INT >= 26) {
                    mNotificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
                    mNotificationManager.deleteNotificationChannel(NOTIFICATION_VIBRATE_CHANNEL_ID);
                    if (this.ph.getInt(URLFactory.REMINDER_OPTION) == 0) {
                        if (!this.ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Reminder", 4);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
                            notificationChannel.setSound((Uri) null, (AudioAttributes) null);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        } else {
                            NotificationChannel notificationChannel2 = new NotificationChannel(NOTIFICATION_VIBRATE_CHANNEL_ID, "Vibrate Reminder", 4);
                            notificationChannel2.enableLights(true);
                            notificationChannel2.setSound((Uri) null, (AudioAttributes) null);
                            notificationChannel2.setLightColor(SupportMenu.CATEGORY_MASK);
                            notificationChannel2.enableVibration(false);
                            notificationChannel2.setVibrationPattern(new long[]{0});
                            mBuilder.setChannelId(NOTIFICATION_VIBRATE_CHANNEL_ID);
                            mNotificationManager.createNotificationChannel(notificationChannel2);
                        }
                        try {
                            if (!URLFactory.notification_ringtone.isPlaying()) {
                                URLFactory.notification_ringtone.play();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!this.ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                        NotificationChannel channel_none = new NotificationChannel(NOTIFICATION_SILENT_CHANNEL_ID, "Silent Reminder", 4);
                        channel_none.setSound((Uri) null, (AudioAttributes) null);
                        channel_none.enableVibration(true);
                        channel_none.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        mBuilder.setChannelId(NOTIFICATION_SILENT_CHANNEL_ID);
                        mNotificationManager.createNotificationChannel(channel_none);
                    } else {
                        NotificationChannel channel_none2 = new NotificationChannel(NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID, "Silent-Vibrate Reminder", 4);
                        channel_none2.setSound((Uri) null, (AudioAttributes) null);
                        channel_none2.enableVibration(false);
                        channel_none2.setVibrationPattern(new long[]{0});
                        mBuilder.setChannelId(NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID);
                        mNotificationManager.createNotificationChannel(channel_none2);
                    }
                }
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }

    public Uri getSound() {
        Uri uri = Settings.System.DEFAULT_NOTIFICATION_URI;
        Log.d("getSound", "" + this.ph.getInt(URLFactory.REMINDER_SOUND));
        if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 1) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.bell);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 2) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.blop);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 3) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.bong);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 4) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.click);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 5) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.echo_droplet);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 6) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.mario_droplet);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 7) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.ship_bell);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) == 8) {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.simple_droplet);
        } else if (this.ph.getInt(URLFactory.REMINDER_SOUND) != 9) {
            return uri;
        } else {
            return Uri.parse("android.resource://" + this.mContext.getPackageName() + "/" + R.raw.tiny_droplet);
        }
    }

    public boolean reachedDailyGoal() {
        double d;
        double d2;
        Constant.SDB = this.mContext.openOrCreateDatabase(Constant.DATABASE_NAME, 268435456, (SQLiteDatabase.CursorFactory) null);
        if (this.ph.getFloat(URLFactory.DAILY_WATER) == 0.0f) {
            URLFactory.DAILY_WATER_VALUE = 2500.0f;
        } else {
            URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.DAILY_WATER);
        }
        ArrayList<HashMap<String, String>> arr_data = getdata("tbl_drink_details", "DrinkDate ='" + this.dth.getCurrentDate("dd-MM-yyyy") + "'");
        float drink_water = 0.0f;
        for (int k = 0; k < arr_data.size(); k++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                d2 = (double) drink_water;
                d = Double.parseDouble((String) arr_data.get(k).get("ContainerValue"));
            } else {
                d2 = (double) drink_water;
                d = Double.parseDouble((String) arr_data.get(k).get("ContainerValueOZ"));
            }
            drink_water = (float) (d2 + d);
        }
        if (drink_water >= URLFactory.DAILY_WATER_VALUE) {
            return true;
        }
        return false;
    }

    @SuppressLint({"WrongConstant"})
    public String get_today_report() {
        double d;
        double d2;
        Constant.SDB = this.mContext.openOrCreateDatabase(Constant.DATABASE_NAME, 268435456, (SQLiteDatabase.CursorFactory) null);
        if (this.ph.getFloat(URLFactory.DAILY_WATER) == 0.0f) {
            URLFactory.DAILY_WATER_VALUE = 2500.0f;
        } else {
            URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.DAILY_WATER);
        }
        if (check_blank_data("" + this.ph.getString(URLFactory.WATER_UNIT))) {
            URLFactory.WATER_UNIT_VALUE = "ML";
        } else {
            URLFactory.WATER_UNIT_VALUE = this.ph.getString(URLFactory.WATER_UNIT);
        }
        ArrayList<HashMap<String, String>> arr_data = getdata("tbl_drink_details", "DrinkDate ='" + this.dth.getCurrentDate("dd-MM-yyyy") + "'");
        float drink_water = 0.0f;
        for (int k = 0; k < arr_data.size(); k++) {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                d2 = (double) drink_water;
                d = Double.parseDouble((String) arr_data.get(k).get("ContainerValue"));
            } else {
                d2 = (double) drink_water;
                d = Double.parseDouble((String) arr_data.get(k).get("ContainerValueOZ"));
            }
            drink_water = (float) (d2 + d);
        }
        return this.mContext.getResources().getString(R.string.str_have_u_had_any_water_yet);
    }

    public boolean check_blank_data(String data) {
        if (data.equals("") || data.isEmpty() || data.length() == 0 || data.equals("null") || data == null) {
            return true;
        }
        return false;
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con) {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<>();
        String query = ("SELECT * FROM " + table_name) + " where " + where_con;
        Cursor c = Constant.SDB.rawQuery(query, (String[]) null);
        System.out.println("SELECT QUERY : " + query);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                maplist.add(map);
            } while (c.moveToNext());
        }
        return maplist;
    }
}
