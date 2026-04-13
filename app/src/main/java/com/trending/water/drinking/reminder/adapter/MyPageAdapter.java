package com.trending.water.drinking.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.trending.water.drinking.reminder.R;

public class MyPageAdapter extends PagerAdapter {
    Context mContext;
    int resId = 0;

    public MyPageAdapter(Context context) {
        this.mContext = context;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        switch (position) {
            case 0:
                this.resId = R.layout.screen_battery_optimize_one;
                break;
            case 1:
                this.resId = R.layout.screen_battery_optimize_two;
                break;
        }
        ViewGroup layout = (ViewGroup) inflater.inflate(this.resId, collection, false);
        collection.addView(layout);
        return layout;
    }

    public int getCount() {
        return 2;
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
