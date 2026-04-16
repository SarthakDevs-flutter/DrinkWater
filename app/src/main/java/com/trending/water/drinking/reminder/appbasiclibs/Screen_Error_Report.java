package com.trending.water.drinking.reminder.appbasiclibs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trending.water.drinking.reminder.R;
import android.view.LayoutInflater;

import com.trending.water.drinking.reminder.appbasiclibs.utils.ExceptionHandler;
import com.trending.water.drinking.reminder.databinding.ScreenErrorReportBinding;

public class Screen_Error_Report extends BaseActivity<ScreenErrorReportBinding> {

    @Override
    protected ScreenErrorReportBinding inflateBinding(LayoutInflater inflater) {
        return ScreenErrorReportBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        String errorMsg = getIntent().getStringExtra("error");
        binding.error.setText(errorMsg);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        binding.btnSendError.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android Bug Report");
                intent.putExtra(Intent.EXTRA_TEXT, binding.error.getText().toString());
                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
            }
        });

        binding.btnCancelError.setOnClickListener(view -> AppClose.exitApplication(this));
    }
}
