package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.mycustom.MaterialProgressBar;

public class Alert_Helper {
    Dialog dialog1;
    Context mContext;
    ProgressDialog pDialog;

    public Alert_Helper(Context mContext2) {
        this.mContext = mContext2;
    }

    public void Show_Alert_Dialog(String msg) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this.mContext);
        ad.setTitle("Info");
        ad.setIcon(17301659);
        ad.setPositiveButton("Close", (DialogInterface.OnClickListener) null);
        ad.setMessage(msg);
        ad.show();
    }

    public void alert(String msg) {
        Toast.makeText(this.mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void customAlert(String msg) {
        View toastView = LayoutInflater.from(this.mContext).inflate(R.layout.activity_toast_custom_view, (ViewGroup) null);
        ((TextView) toastView.findViewById(R.id.customToastText)).setText(msg);
        Toast toast = new Toast(this.mContext);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(80, 0, 40);
        toast.show();
    }

    public void Show_Progress_Dialog(String msg) {
        this.pDialog = new ProgressDialog(this.mContext, 0);
        this.pDialog.setMessage(msg);
        this.pDialog.setIndeterminate(false);
        this.pDialog.setCancelable(false);
        this.pDialog.show();
    }

    public void Close_Progress_Dialog() {
        if (this.pDialog != null) {
            this.pDialog.dismiss();
        }
    }

    public void Show_Custom_Progress_Dialog() {
        this.dialog1 = new Dialog(this.mContext, R.style.AppDialogTheme);
        this.dialog1.requestWindowFeature(1);
        this.dialog1.setContentView(R.layout.custom_progress_dialog);
        this.dialog1.setCancelable(false);
        this.dialog1.getWindow().setLayout(this.mContext.getResources().getDisplayMetrics().widthPixels - 80, this.mContext.getResources().getDisplayMetrics().heightPixels - 100);
        ((MaterialProgressBar) this.dialog1.findViewById(R.id.progress_wheel)).setBarColor(Color.parseColor("#3D6B70"));
        this.dialog1.show();
    }

    public void Close_Custom_Progress_Dialog() {
        this.dialog1.dismiss();
    }

    public boolean is_show_Custom_Progress_Dialog() {
        if (this.dialog1 != null) {
            return this.dialog1.isShowing();
        }
        return false;
    }

    public void Show_Error_Dialog(String msg) {
        Show_Alert_Dialog(msg);
    }
}
