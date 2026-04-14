package com.trending.water.drinking.reminder.appbasiclibs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.utils.ExceptionHandler;

public class Screen_Error_Report extends BaseActivity {
    private Button btnCancelError;
    private Button btnSendError;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.screen_error_report);

        errorTextView = findViewById(R.id.error);
        btnCancelError = findViewById(R.id.btn_cancel_error);
        btnSendError = findViewById(R.id.btn_send_error);

        String errorMsg = getIntent().getStringExtra("error");
        errorTextView.setText(errorMsg);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        btnSendError.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android Bug Report");
                intent.putExtra(Intent.EXTRA_TEXT, errorTextView.getText().toString());
                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
            }
        });

        btnCancelError.setOnClickListener(view -> AppClose.exitApplication(this));
    }
}
