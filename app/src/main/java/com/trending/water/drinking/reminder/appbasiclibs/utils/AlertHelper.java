package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.mycustom.MaterialProgressBar;

public class AlertHelper {
    private final Context context;
    private Dialog customDialog;
    private ProgressDialog progressDialog;

    public AlertHelper(@NonNull Context context) {
        this.context = context;
    }

    public void showAlert(@NonNull String message) {
        new AlertDialog.Builder(context)
                .setTitle("Info")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Close", null)
                .setMessage(message)
                .show();
    }

    public void alert(@NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void customAlert(@NonNull String message) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.activity_toast_custom_view, null);
        TextView toastTextView = toastView.findViewById(R.id.customToastText);
        if (toastTextView != null) {
            toastTextView.setText(message);
        }

        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 40);
        toast.show();
    }

    @SuppressWarnings("deprecation")
    public void showProgressDialog(@NonNull String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showCustomProgressDialog() {
        customDialog = new Dialog(context, R.style.AppDialogTheme);
        customDialog.requestWindowFeature(1);
        customDialog.setContentView(R.layout.custom_progress_dialog);
        customDialog.setCancelable(false);
        
        if (customDialog.getWindow() != null) {
            int width = context.getResources().getDisplayMetrics().widthPixels - 80;
            int height = context.getResources().getDisplayMetrics().heightPixels - 100;
            customDialog.getWindow().setLayout(width, height);
        }

        MaterialProgressBar progressBar = customDialog.findViewById(R.id.progress_wheel);
        if (progressBar != null) {
            progressBar.setBarColor(Color.parseColor("#3D6B70"));
        }
        customDialog.show();
    }

    public void closeCustomProgressDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
            customDialog = null;
        }
    }

    public boolean isCustomProgressShowing() {
        return customDialog != null && customDialog.isShowing();
    }

    public void showErrorDialog(@NonNull String message) {
        showAlert(message);
    }
}
