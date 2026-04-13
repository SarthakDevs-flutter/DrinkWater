package com.trending.water.drinking.reminder.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.BaseAppCompatActivity;
import com.trending.water.drinking.reminder.utils.DB_Helper;

public class MasterBaseAppCompatActivity extends BaseAppCompatActivity {
    DB_Helper dbh;

    public static int getThemeColor(Context ctx) {
        return ctx.getResources().getColor(R.color.colorPrimaryDark);
    }

    public static int[] getThemeColorArray(Context ctx) {
        return new int[]{Color.parseColor("#001455da"), Color.parseColor("#FF1455da")};
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbh = new DB_Helper(this, this);
    }
}
