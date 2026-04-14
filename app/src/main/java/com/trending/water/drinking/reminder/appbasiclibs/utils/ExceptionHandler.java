package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Process;

import androidx.annotation.NonNull;

import com.trending.water.drinking.reminder.appbasiclibs.Screen_Error_Report;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Activity activity;

    public ExceptionHandler(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable exception) {
        StringWriter stackTraceWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTraceWriter));

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("************ CAUSE OF ERROR ************\n\n")
                .append(stackTraceWriter)
                .append("\n************ DEVICE INFORMATION ***********\n")
                .append("Brand: ").append(Build.BRAND).append("\n")
                .append("Device: ").append(Build.DEVICE).append("\n")
                .append("Model: ").append(Build.MODEL).append("\n")
                .append("Id: ").append(Build.ID).append("\n")
                .append("Product: ").append(Build.PRODUCT).append("\n")
                .append("\n************ FIRMWARE ************\n")
                .append("SDK: ").append(Build.VERSION.SDK_INT).append("\n")
                .append("Release: ").append(Build.VERSION.RELEASE).append("\n")
                .append("Incremental: ").append(Build.VERSION.INCREMENTAL).append("\n");

        Intent intent = new Intent(activity, Screen_Error_Report.class);
        intent.putExtra("error", reportBuilder.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        activity.startActivity(intent);

        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}
