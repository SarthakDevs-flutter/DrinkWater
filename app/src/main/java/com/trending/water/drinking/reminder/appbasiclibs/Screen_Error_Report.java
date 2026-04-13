package com.trending.water.drinking.reminder.appbasiclibs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.utils.ExceptionHandler;

public class Screen_Error_Report extends BaseActivity {
    Button btn_cancel_error;
    Button btn_send_error;
    TextView error;
    Context mContext;

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    @TargetApi(9)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.screen_error_report);
        this.mContext = this;
        this.error = (TextView) findViewById(R.id.error);
        this.btn_cancel_error = (Button) findViewById(R.id.btn_cancel_error);
        this.btn_send_error = (Button) findViewById(R.id.btn_send_error);
        this.error.setText(getIntent().getStringExtra("error"));
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.btn_send_error.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("mailto:"));
                    intent.putExtra("android.intent.extra.SUBJECT", "Android Bug Report");
                    intent.putExtra("android.intent.extra.TEXT", "" + Screen_Error_Report.this.error.getText().toString());
                    Screen_Error_Report.this.startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(Screen_Error_Report.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        this.btn_cancel_error.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppClose.exitApplication(Screen_Error_Report.this.mContext);
            }
        });
    }
}
