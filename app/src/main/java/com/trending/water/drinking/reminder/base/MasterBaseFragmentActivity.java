package com.trending.water.drinking.reminder.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.appbasiclibs.BaseFragmentActivity;
import com.trending.water.drinking.reminder.utils.DbHelper;

public class MasterBaseFragmentActivity extends BaseFragmentActivity {
    protected DbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbHelper = new DbHelper(this, this);
    }
}
