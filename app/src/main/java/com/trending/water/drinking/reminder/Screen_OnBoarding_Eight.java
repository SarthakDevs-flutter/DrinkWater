package com.trending.water.drinking.reminder;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Eight extends MasterBaseFragment {
    RelativeLayout cloudy_block;
    ImageView img_cloudy;
    ImageView img_rainy;
    ImageView img_snow;
    ImageView img_sunny;
    View item_view;
    RelativeLayout rainy_block;
    RelativeLayout snow_block;
    RelativeLayout sunny_block;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_onboarding_eight, container, false);
        FindViewById();
        Body();
        return this.item_view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void FindViewById() {
        this.sunny_block = (RelativeLayout) this.item_view.findViewById(R.id.sunny_block);
        this.cloudy_block = (RelativeLayout) this.item_view.findViewById(R.id.cloudy_block);
        this.rainy_block = (RelativeLayout) this.item_view.findViewById(R.id.rainy_block);
        this.snow_block = (RelativeLayout) this.item_view.findViewById(R.id.snow_block);
        this.img_sunny = (ImageView) this.item_view.findViewById(R.id.img_sunny);
        this.img_cloudy = (ImageView) this.item_view.findViewById(R.id.img_cloudy);
        this.img_rainy = (ImageView) this.item_view.findViewById(R.id.img_rainy);
        this.img_snow = (ImageView) this.item_view.findViewById(R.id.img_snow);
    }

    private void Body() {
        setWeather(this.ph.getInt(URLFactory.WEATHER_CONSITIONS));
        this.sunny_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_OnBoarding_Eight.this.setWeather(0);
            }
        });
        this.cloudy_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_OnBoarding_Eight.this.setWeather(1);
            }
        });
        this.rainy_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_OnBoarding_Eight.this.setWeather(2);
            }
        });
        this.snow_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_OnBoarding_Eight.this.setWeather(3);
            }
        });
    }

    public void setWeather(int idx) {
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
        this.ph.savePreferences(URLFactory.WEATHER_CONSITIONS, idx);
        RelativeLayout relativeLayout = this.sunny_block;
        if (idx == 0) {
            drawable = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select);
        } else {
            drawable = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular);
        }
        relativeLayout.setBackground(drawable);
        this.img_sunny.setImageResource(idx == 0 ? R.drawable.sunny_selected : R.drawable.sunny);
        RelativeLayout relativeLayout2 = this.cloudy_block;
        if (idx == 1) {
            drawable2 = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select);
        } else {
            drawable2 = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular);
        }
        relativeLayout2.setBackground(drawable2);
        this.img_cloudy.setImageResource(idx == 1 ? R.drawable.cloudy_selected : R.drawable.cloudy);
        RelativeLayout relativeLayout3 = this.rainy_block;
        if (idx == 2) {
            drawable3 = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select);
        } else {
            drawable3 = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular);
        }
        relativeLayout3.setBackground(drawable3);
        this.img_rainy.setImageResource(idx == 2 ? R.drawable.rainy_selected : R.drawable.rainy);
        RelativeLayout relativeLayout4 = this.snow_block;
        if (idx == 3) {
            drawable4 = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select);
        } else {
            drawable4 = this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular);
        }
        relativeLayout4.setBackground(drawable4);
        this.img_snow.setImageResource(idx == 3 ? R.drawable.snow_selected : R.drawable.snow);
    }
}
