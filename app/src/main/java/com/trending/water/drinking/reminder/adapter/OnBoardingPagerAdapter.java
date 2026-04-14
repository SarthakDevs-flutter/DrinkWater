package com.trending.water.drinking.reminder.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
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
    
    private final Context context;

    public OnBoardingPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Context context) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new Screen_OnBoarding_Three();
            case 2:
                return new Screen_OnBoarding_Seven();
            case 3:
                return new Screen_OnBoarding_Eight();
            case 4:
                return new Screen_OnBoarding_Four();
            case 5:
                return new Screen_OnBoarding_Six();
            case 6:
                return new Screen_OnBoarding_Five();
            case 0:
            default:
                return new Screen_OnBoarding_Two();
        }
    }

    @Override
    public int getCount() {
        return 7;
    }
}
