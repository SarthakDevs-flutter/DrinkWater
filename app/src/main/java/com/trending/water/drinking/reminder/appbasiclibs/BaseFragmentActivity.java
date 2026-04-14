package com.trending.water.drinking.reminder.appbasiclibs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.trending.water.drinking.reminder.appbasiclibs.utils.AlertHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.BitmapHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DatabaseHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DateHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.IntentHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.MapHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.PreferenceHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.StringHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.UtilityFunction;
import com.trending.water.drinking.reminder.appbasiclibs.utils.ZipHelper;

public class BaseFragmentActivity extends FragmentActivity {
    protected Activity activity;
    protected Context context;
    
    protected AlertHelper alertHelper;
    protected BitmapHelper bitmapHelper;
    protected DatabaseHelper databaseHelper;
    protected DateHelper dateHelper;
    protected IntentHelper intentHelper;
    protected MapHelper mapHelper;
    protected PreferenceHelper preferencesHelper;
    protected StringHelper stringHelper;
    protected UtilityFunction utilityFunction;
    protected ZipHelper zipHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.context = this;
        this.activity = this;
        
        this.utilityFunction = new UtilityFunction(this.context, this.activity);
        this.alertHelper = new AlertHelper(this.context);
        this.bitmapHelper = new BitmapHelper(this.context);
        this.dateHelper = new DateHelper();
        this.databaseHelper = new DatabaseHelper(this.context, this.activity);
        this.intentHelper = new IntentHelper(this.context, this.activity);
        this.mapHelper = new MapHelper();
        this.stringHelper = new StringHelper(this.context);
        this.preferencesHelper = new PreferenceHelper(this.context);
        this.zipHelper = new ZipHelper();
        
        this.utilityFunction.permissionStrictMode();
    }
}
