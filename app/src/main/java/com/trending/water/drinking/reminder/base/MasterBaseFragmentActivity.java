package com.trending.water.drinking.reminder.base;

import android.os.Bundle;

import com.trending.water.drinking.reminder.appbasiclibs.BaseFragmentActivity;
import com.trending.water.drinking.reminder.utils.DB_Helper;

public class MasterBaseFragmentActivity extends BaseFragmentActivity {
    DB_Helper dbh;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbh = new DB_Helper(this, this);
    }
}
