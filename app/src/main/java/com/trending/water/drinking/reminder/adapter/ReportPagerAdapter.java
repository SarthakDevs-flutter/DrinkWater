package com.trending.water.drinking.reminder.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.Screen_Month_Report;
import com.trending.water.drinking.reminder.Screen_Week_Report;
import com.trending.water.drinking.reminder.Screen_Year_Report;

public class ReportPagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;

    public ReportPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Context context) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Screen_Week_Report();
            case 1:
                return new Screen_Month_Report();
            case 2:
            default:
                return new Screen_Year_Report();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.str_week);
            case 1:
                return context.getString(R.string.str_month);
            case 2:
                return context.getString(R.string.str_year);
            default:
                return null;
        }
    }
}
