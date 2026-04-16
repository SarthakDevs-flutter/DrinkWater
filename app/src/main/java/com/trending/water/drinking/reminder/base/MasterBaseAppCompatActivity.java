package com.trending.water.drinking.reminder.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.BaseAppCompatActivity;
import com.trending.water.drinking.reminder.utils.DbHelper;

public abstract class MasterBaseAppCompatActivity<VB extends ViewBinding> extends BaseAppCompatActivity<VB> {
    protected DbHelper dbHelper;

    public static int getThemeColor(Context context) {
        return ContextCompat.getColor(context, R.color.colorPrimaryDark);
    }

    public static int[] getThemeColorArray(Context context) {
        return new int[]{Color.parseColor("#001455da"), Color.parseColor("#FF1455da")};
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbHelper = new DbHelper(this, this);
    }
}
