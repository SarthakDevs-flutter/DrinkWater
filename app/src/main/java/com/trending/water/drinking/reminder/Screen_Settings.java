package com.trending.water.drinking.reminder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_Settings extends MasterBaseActivity {
    LinearLayout backup_restore_block;
    AppCompatTextView lbl_restore_and_backup;
    AppCompatTextView lbl_toolbar_title;
    LinearLayout left_icon_block;
    LinearLayout right_icon_block;
    SwitchCompat switch_notification;
    SwitchCompat switch_sound;
    LinearLayout weight_block;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_settings);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(this.mContext.getResources().getColor(R.color.str_green_card));
        }
        FindViewById();
        Body();
    }

    private void FindViewById() {
        this.right_icon_block = (LinearLayout) findViewById(R.id.right_icon_block);
        this.left_icon_block = (LinearLayout) findViewById(R.id.left_icon_block);
        this.lbl_toolbar_title = (AppCompatTextView) findViewById(R.id.lbl_toolbar_title);
        this.backup_restore_block = (LinearLayout) findViewById(R.id.backup_restore_block);
        this.weight_block = (LinearLayout) findViewById(R.id.weight_block);
        this.switch_notification = (SwitchCompat) findViewById(R.id.switch_notification);
        this.switch_sound = (SwitchCompat) findViewById(R.id.switch_sound);
        this.lbl_restore_and_backup = (AppCompatTextView) findViewById(R.id.lbl_restore_and_backup);
        this.lbl_restore_and_backup.setText(this.sh.get_string(R.string.str_backup_and_restore));
    }

    private void Body() {
        this.lbl_toolbar_title.setText(this.sh.get_string(R.string.str_settings));
        this.left_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Settings.this.finish();
            }
        });
        this.right_icon_block.setVisibility(View.GONE);
        this.backup_restore_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Settings.this.intent = new Intent(Screen_Settings.this.act, Screen_Backup_Restore.class);
                Screen_Settings.this.startActivity(Screen_Settings.this.intent);
            }
        });
        this.weight_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Settings.this.intent = new Intent(Screen_Settings.this.act, Screen_Profile.class);
                Screen_Settings.this.startActivity(Screen_Settings.this.intent);
            }
        });
        this.switch_notification.setChecked(this.ph.getBoolean(URLFactory.DISABLE_NOTIFICATION));
        this.switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Screen_Settings.this.ph.savePreferences(URLFactory.DISABLE_NOTIFICATION, isChecked);
            }
        });
        this.switch_sound.setChecked(this.ph.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER));
        this.switch_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Screen_Settings.this.ph.savePreferences(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER, isChecked);
            }
        });
    }
}
