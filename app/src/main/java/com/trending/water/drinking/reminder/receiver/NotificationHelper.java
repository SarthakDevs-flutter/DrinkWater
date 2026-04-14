package com.trending.water.drinking.reminder.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.Screen_Dashboard;
import com.trending.water.drinking.reminder.Screen_Select_Bottle;
import com.trending.water.drinking.reminder.Screen_Select_Snooze;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DatabaseHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DateHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.PreferenceHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

class NotificationHelper {
    private static final String TAG = "NotificationHelper";
    private static final String CHANNEL_REMINDER = "10001";
    private static final String CHANNEL_VIBRATE = "10003";
    private static final String CHANNEL_SILENT = "10002";
    private static final String CHANNEL_SILENT_VIBRATE = "10004";

    private final Context context;
    private final PreferenceHelper preferencesHelper;
    private final DateHelper dateHelper = new DateHelper();

    NotificationHelper(Context context) {
        this.context = context;
        this.preferencesHelper = new PreferenceHelper(context);
        if (URLFactory.notificationRingtone == null) {
            URLFactory.notificationRingtone = RingtoneManager.getRingtone(context, getSoundUri());
        }
    }

    void createNotification() {
        int reminderOption = preferencesHelper.getInt(URLFactory.REMINDER_OPTION);
        boolean isVibrate = preferencesHelper.getBoolean(URLFactory.REMINDER_VIBRATE);
        boolean disableNotification = preferencesHelper.getBoolean(URLFactory.DISABLE_NOTIFICATION);

        if (reminderOption == 1) return; // Reminders off

        if (isDailyGoalReached() && disableNotification) return;

        Intent intent = new Intent(context, Screen_Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        int pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntentFlags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, pendingIntentFlags);

        Intent snoozeIntent = new Intent(context, Screen_Select_Snooze.class);
        snoozeIntent.setAction("SNOOZE_ACTION");
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(context, 0, snoozeIntent, pendingIntentFlags);

        Intent addWaterIntent = new Intent(context, Screen_Select_Bottle.class);
        addWaterIntent.setAction("ADD_WATER_ACTION");
        addWaterIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent addWaterPendingIntent = PendingIntent.getActivity(context, 0, addWaterIntent, pendingIntentFlags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_REMINDER);
        builder.setSmallIcon(R.drawable.ic_small_app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(context.getString(R.string.str_drink_water))
                .setContentText(getTodayReport())
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .addAction(R.drawable.ic_plus, context.getString(R.string.str_add_water), addWaterPendingIntent)
                .addAction(R.drawable.ic_notification, context.getString(R.string.str_snooze), snoozePendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId;
            String channelName;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (reminderOption == 0) { // Normal
                if (!isVibrate) {
                    channelId = CHANNEL_REMINDER;
                    channelName = "Reminder";
                } else {
                    channelId = CHANNEL_VIBRATE;
                    channelName = "Vibrate Reminder";
                }
                
                try {
                    if (URLFactory.notificationRingtone != null && !URLFactory.notificationRingtone.isPlaying()) {
                        URLFactory.notificationRingtone.play();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error playing sound", e);
                }
            } else { // Silent or other
                if (!isVibrate) {
                    channelId = CHANNEL_SILENT;
                    channelName = "Silent Reminder";
                } else {
                    channelId = CHANNEL_SILENT_VIBRATE;
                    channelName = "Silent-Vibrate Reminder";
                }
            }

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setSound(null, null); // We handle sound manually or via system if enabled
            if (isVibrate) {
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {
                channel.enableVibration(false);
            }
            
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        } else {
            // Pre-Oreo sound/vibrate settings
            if (reminderOption == 0) {
                builder.setSound(getSoundUri());
                if (isVibrate) builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
                else builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
            } else {
                builder.setSound(null);
            }
        }

        notificationManager.notify(0, builder.build());
    }

    public Uri getSoundUri() {
        int soundIndex = preferencesHelper.getInt(URLFactory.REMINDER_SOUND);
        int resId;
        switch (soundIndex) {
            case 1: resId = R.raw.bell; break;
            case 2: resId = R.raw.blop; break;
            case 3: resId = R.raw.bong; break;
            case 4: resId = R.raw.click; break;
            case 5: resId = R.raw.echo_droplet; break;
            case 6: resId = R.raw.mario_droplet; break;
            case 7: resId = R.raw.ship_bell; break;
            case 8: resId = R.raw.simple_droplet; break;
            case 9: resId = R.raw.tiny_droplet; break;
            default: return Settings.System.DEFAULT_NOTIFICATION_URI;
        }
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
    }

    private float getTodayDrunkAmount() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        String today = dateHelper.getCurrentDate("dd-MM-yyyy");
        ArrayList<HashMap<String, String>> drankData = databaseHelper.getData("tbl_drink_details", "DrinkDate ='" + today + "'");
        
        float totalAmount = 0.0f;
        String unit = preferencesHelper.getString(URLFactory.WATER_UNIT);
        if (unit.isEmpty()) unit = "ML";

        for (HashMap<String, String> entry : drankData) {
            try {
                String valueStr = unit.equalsIgnoreCase("ml") ? entry.get("ContainerValue") : entry.get("ContainerValueOZ");
                if (valueStr != null) {
                    totalAmount += Float.parseFloat(valueStr);
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing drink amount", e);
            }
        }
        return totalAmount;
    }

    public boolean isDailyGoalReached() {
        float goal = preferencesHelper.getFloat(URLFactory.DAILY_WATER);
        if (goal == 0.0f) goal = 2500.0f;
        
        return getTodayDrunkAmount() >= goal;
    }

    public String getTodayReport() {
        // This could be expanded to show actual progress
        return context.getString(R.string.str_have_u_had_any_water_yet);
    }
}
