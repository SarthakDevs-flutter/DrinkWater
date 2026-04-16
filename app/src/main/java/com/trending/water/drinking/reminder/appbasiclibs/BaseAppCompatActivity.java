package com.trending.water.drinking.reminder.appbasiclibs;

import android.os.Bundle;

import androidx.viewbinding.ViewBinding;

public abstract class BaseAppCompatActivity<VB extends ViewBinding> extends BaseActivity<VB> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
