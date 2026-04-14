package com.trending.water.drinking.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.trending.water.drinking.reminder.R;

public class MyPageAdapter extends PagerAdapter {
    private final Context context;

    public MyPageAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        int layoutResId = (position == 0) ? R.layout.screen_battery_optimize_one : R.layout.screen_battery_optimize_two;
        
        View layout = LayoutInflater.from(context).inflate(layoutResId, collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }
}
