package com.trending.water.drinking.reminder.appbasiclibs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

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

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    protected VB binding;
    protected Activity mActivity;
    protected Context mContext;
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

    protected abstract VB inflateBinding(LayoutInflater inflater);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = inflateBinding(getLayoutInflater());
        if (binding != null) {
            setContentView(binding.getRoot());
        }

        this.mContext = this;
        this.mActivity = this;

        this.utilityFunction = new UtilityFunction(this.mContext, this.mActivity);
        this.alertHelper = new AlertHelper(this.mContext);
        this.bitmapHelper = new BitmapHelper(this.mContext);
        this.dateHelper = new DateHelper();
        this.databaseHelper = new DatabaseHelper(this.mContext, this.mActivity);
        this.intentHelper = new IntentHelper(this.mContext, this.mActivity);
        this.mapHelper = new MapHelper();
        this.stringHelper = new StringHelper(this.mContext);
        this.preferencesHelper = new PreferenceHelper(this.mContext);
        this.zipHelper = new ZipHelper();

        this.utilityFunction.enableStrictMode();
    }
}
