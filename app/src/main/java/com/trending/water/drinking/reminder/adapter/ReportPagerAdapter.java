package com.trending.water.drinking.reminder.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.Screen_Month_Report;
import com.trending.water.drinking.reminder.Screen_Week_Report;
import com.trending.water.drinking.reminder.Screen_Year_Report;

public class ReportPagerAdapter extends FragmentStatePagerAdapter {
    Context mContext;
    Screen_Week_Report tab1Fragment = new Screen_Week_Report();
    Screen_Month_Report tab2Fragment = new Screen_Month_Report();
    Screen_Year_Report tab3Fragment = new Screen_Year_Report();

    public ReportPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return this.tab1Fragment;
        }
        if (position == 1) {
            return this.tab2Fragment;
        }
        return this.tab3Fragment;
    }

    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return this.mContext.getResources().getString(R.string.str_week);
        }
        if (position == 1) {
            return this.mContext.getResources().getString(R.string.str_month);
        }
        if (position == 2) {
            return this.mContext.getResources().getString(R.string.str_year);
        }
        return null;
    }
}
