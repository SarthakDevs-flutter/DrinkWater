package com.trending.water.drinking.reminder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.databinding.ScreenSettingsBinding;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_Settings extends MasterBaseActivity<ScreenSettingsBinding> {

    @Override
    protected ScreenSettingsBinding inflateBinding(LayoutInflater inflater) {
        return ScreenSettingsBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.str_green_card));
        }

        initView();
    }

    private void initView() {
        binding.include1.lblToolbarTitle.setText(stringHelper.getString(R.string.str_settings));
        binding.lblRestoreAndBackup.setText(stringHelper.getString(R.string.str_backup_and_restore));

        binding.include1.leftIconBlock.setOnClickListener(v -> finish());
        binding.include1.rightIconBlock.setVisibility(View.GONE);

        binding.backupRestoreBlock.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, Screen_Backup_Restore.class));
        });

        binding.weightBlock.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, Screen_Profile.class));
        });

        binding.switchNotification.setChecked(preferencesHelper.getBoolean(URLFactory.KEY_DISABLE_NOTIFICATION, false));
        binding.switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_DISABLE_NOTIFICATION, isChecked);
        });

        binding.switchSound.setChecked(preferencesHelper.getBoolean(URLFactory.KEY_DISABLE_SOUND_ON_ADD, false));
        binding.switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_DISABLE_SOUND_ON_ADD, isChecked);
        });
    }
}
