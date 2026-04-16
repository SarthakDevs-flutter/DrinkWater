package com.trending.water.drinking.reminder.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.trending.water.drinking.reminder.appbasiclibs.BaseActivity;
import com.trending.water.drinking.reminder.utils.DbHelper;

public abstract class MasterBaseActivity<VB extends ViewBinding> extends BaseActivity<VB> {
    protected DbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbHelper = new DbHelper(this, this);
    }
}
