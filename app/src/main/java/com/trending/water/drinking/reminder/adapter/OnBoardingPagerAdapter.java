package com.trending.water.drinking.reminder.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.trending.water.drinking.reminder.Screen_OnBoarding_Eight;
import com.trending.water.drinking.reminder.Screen_OnBoarding_Five;
import com.trending.water.drinking.reminder.Screen_OnBoarding_Four;
import com.trending.water.drinking.reminder.Screen_OnBoarding_Seven;
import com.trending.water.drinking.reminder.Screen_OnBoarding_Six;
import com.trending.water.drinking.reminder.Screen_OnBoarding_Three;
import com.trending.water.drinking.reminder.Screen_OnBoarding_Two;

public class OnBoardingPagerAdapter extends FragmentStatePagerAdapter {
    Context mContext;
    Screen_OnBoarding_Two tab2Fragment = new Screen_OnBoarding_Two();
    Screen_OnBoarding_Three tab3Fragment = new Screen_OnBoarding_Three();
    Screen_OnBoarding_Four tab4Fragment = new Screen_OnBoarding_Four();
    Screen_OnBoarding_Five tab5Fragment = new Screen_OnBoarding_Five();
    Screen_OnBoarding_Six tab6Fragment = new Screen_OnBoarding_Six();
    Screen_OnBoarding_Seven tab7Fragment = new Screen_OnBoarding_Seven();
    Screen_OnBoarding_Eight tab8Fragment = new Screen_OnBoarding_Eight();

    public OnBoardingPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    public Fragment getItem(int position) {
        if (position == 1) {
            return this.tab3Fragment;
        }
        if (position == 2) {
            return this.tab7Fragment;
        }
        if (position == 3) {
            return this.tab8Fragment;
        }
        if (position == 4) {
            return this.tab4Fragment;
        }
        if (position == 5) {
            return this.tab6Fragment;
        }
        if (position == 6) {
            return this.tab5Fragment;
        }
        return this.tab2Fragment;
    }

    public int getCount() {
        return 7;
    }
}
