package com.trending.water.drinking.reminder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.Objects;

public class Screen_Settings extends MasterBaseActivity {
    
    private AppCompatTextView lblRestoreAndBackup;
    private AppCompatTextView lblToolbarTitle;
    private View leftIconBlock;
    private View rightIconBlock;
    private View backupRestoreBlock;
    private View weightBlock;
    private SwitchCompat switchNotification;
    private SwitchCompat switchSound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_settings);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.str_green_card));
        }

        findViewByIds();
        initView();
    }

    private void findViewByIds() {
        rightIconBlock = findViewById(R.id.right_icon_block);
        leftIconBlock = findViewById(R.id.left_icon_block);
        lblToolbarTitle = findViewById(R.id.lbl_toolbar_title);
        backupRestoreBlock = findViewById(R.id.backup_restore_block);
        weightBlock = findViewById(R.id.weight_block);
        switchNotification = findViewById(R.id.switch_notification);
        switchSound = findViewById(R.id.switch_sound);
        lblRestoreAndBackup = findViewById(R.id.lbl_restore_and_backup);
    }

    private void initView() {
        lblToolbarTitle.setText(stringHelper.getString(R.string.str_settings));
        lblRestoreAndBackup.setText(stringHelper.getString(R.string.str_backup_and_restore));

        leftIconBlock.setOnClickListener(v -> finish());
        rightIconBlock.setVisibility(View.GONE);

        backupRestoreBlock.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, Screen_Backup_Restore.class));
        });

        weightBlock.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, Screen_Profile.class));
        });

        switchNotification.setChecked(preferencesHelper.getBoolean(URLFactory.KEY_DISABLE_NOTIFICATION, false));
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_DISABLE_NOTIFICATION, isChecked);
        });

        switchSound.setChecked(preferencesHelper.getBoolean(URLFactory.KEY_DISABLE_SOUND_ON_ADD, false));
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_DISABLE_SOUND_ON_ADD, isChecked);
        });
    }
}
