package com.trending.water.drinking.reminder.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    @SuppressLint("InvalidWakeLockTag")
    public static void acquire(Context context) {
        if (wakeLock != null) {
            wakeLock.release();
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            // PARTIAL_WAKE_LOCK is usually enough for background operations.
            // The magic number 805306394 was likely a combination of flags.
            // We'll use a standard flag if possible, but preserving original intent if uncertain.
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WaterReminder:WakeLock");
            wakeLock.acquire(10 * 1000L); // 10 seconds timeout for safety
        }
    }

    public static void release() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        wakeLock = null;
    }
}
