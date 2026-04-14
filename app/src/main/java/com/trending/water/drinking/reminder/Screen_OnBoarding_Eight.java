package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Eight extends MasterBaseFragment {
    
    private RelativeLayout sunnyBlock;
    private RelativeLayout cloudyBlock;
    private RelativeLayout rainyBlock;
    private RelativeLayout snowBlock;
    private ImageView imgSunny;
    private ImageView imgCloudy;
    private ImageView imgRainy;
    private ImageView imgSnow;
    
    private View itemView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.screen_onboarding_eight, container, false);
        findViewByIds(itemView);
        initView();
        return itemView;
    }

    private void findViewByIds(View view) {
        sunnyBlock = view.findViewById(R.id.sunny_block);
        cloudyBlock = view.findViewById(R.id.cloudy_block);
        rainyBlock = view.findViewById(R.id.rainy_block);
        snowBlock = view.findViewById(R.id.snow_block);
        imgSunny = view.findViewById(R.id.img_sunny);
        imgCloudy = view.findViewById(R.id.img_cloudy);
        imgRainy = view.findViewById(R.id.img_rainy);
        imgSnow = view.findViewById(R.id.img_snow);
    }

    private void initView() {
        int initialWeather = preferencesHelper.getInt(URLFactory.KEY_WEATHER_CONDITIONS, 0);
        updateWeatherUI(initialWeather);

        sunnyBlock.setOnClickListener(v -> selectWeather(0));
        cloudyBlock.setOnClickListener(v -> selectWeather(1));
        rainyBlock.setOnClickListener(v -> selectWeather(2));
        snowBlock.setOnClickListener(v -> selectWeather(3));
    }

    private void selectWeather(int index) {
        preferencesHelper.savePreferences(URLFactory.KEY_WEATHER_CONDITIONS, index);
        preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
        updateWeatherUI(index);
    }

    private void updateWeatherUI(int selectedIndex) {
        updateWeatherBlock(sunnyBlock, imgSunny, selectedIndex == 0, R.drawable.sunny_selected, R.drawable.sunny);
        updateWeatherBlock(cloudyBlock, imgCloudy, selectedIndex == 1, R.drawable.cloudy_selected, R.drawable.cloudy);
        updateWeatherBlock(rainyBlock, imgRainy, selectedIndex == 2, R.drawable.rainy_selected, R.drawable.rainy);
        updateWeatherBlock(snowBlock, imgSnow, selectedIndex == 3, R.drawable.snow_selected, R.drawable.snow);
    }

    private void updateWeatherBlock(RelativeLayout block, ImageView img, boolean isSelected, int selectedRes, int normalRes) {
        block.setBackground(ContextCompat.getDrawable(mContext, isSelected ? R.drawable.rdo_gender_select : R.drawable.rdo_gender_regular));
        img.setImageResource(isSelected ? selectedRes : normalRes);
    }
}
