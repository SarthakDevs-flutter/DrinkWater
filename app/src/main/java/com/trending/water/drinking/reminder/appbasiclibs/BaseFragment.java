package com.trending.water.drinking.reminder.appbasiclibs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.trending.water.drinking.reminder.appbasiclibs.utils.Alert_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Bitmap_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Intent_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Map_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.String_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Utility_Function;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Zip_Helper;

public class BaseFragment extends Fragment {
    public Activity act;
    public Alert_Helper ah;
    public Bitmap_Helper bh;
    public Database_Helper dh;
    public Date_Helper dth;
    public Intent_Helper ih;
    public Intent intent;
    public Context mContext;
    public Map_Helper mah;
    public Preferences_Helper ph;
    public String_Helper sh;
    public Utility_Function uf;
    public Zip_Helper zh;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
        this.act = getActivity();
        this.uf = new Utility_Function(this.mContext, this.act);
        this.ah = new Alert_Helper(this.mContext);
        this.bh = new Bitmap_Helper(this.mContext);
        this.dth = new Date_Helper();
        this.dh = new Database_Helper(this.mContext, this.act);
        this.ih = new Intent_Helper(this.mContext, this.act);
        this.mah = new Map_Helper();
        this.sh = new String_Helper(this.mContext, this.act);
        this.ph = new Preferences_Helper(this.mContext, this.act);
        this.zh = new Zip_Helper(this.mContext);
        this.uf.permission_StrictMode();
    }
}
