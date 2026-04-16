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
import com.trending.water.drinking.reminder.databinding.ScreenOnboardingEightBinding;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Eight extends MasterBaseFragment<ScreenOnboardingEightBinding> {

    @Override
    protected ScreenOnboardingEightBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenOnboardingEightBinding.inflate(inflater, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        int initialWeather = preferencesHelper.getInt(URLFactory.KEY_WEATHER_CONDITIONS, 0);
        updateWeatherUI(initialWeather);

        binding.sunnyBlock.setOnClickListener(v -> selectWeather(0));
        binding.cloudyBlock.setOnClickListener(v -> selectWeather(1));
        binding.rainyBlock.setOnClickListener(v -> selectWeather(2));
        binding.snowBlock.setOnClickListener(v -> selectWeather(3));
    }

    private void selectWeather(int index) {
        preferencesHelper.savePreferences(URLFactory.KEY_WEATHER_CONDITIONS, index);
        preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
        updateWeatherUI(index);
    }

    private void updateWeatherUI(int selectedIndex) {
        updateWeatherBlock(binding.sunnyBlock, binding.imgSunny, selectedIndex == 0, R.drawable.sunny_selected, R.drawable.sunny);
        updateWeatherBlock(binding.cloudyBlock, binding.imgCloudy, selectedIndex == 1, R.drawable.cloudy_selected, R.drawable.cloudy);
        updateWeatherBlock(binding.rainyBlock, binding.imgRainy, selectedIndex == 2, R.drawable.rainy_selected, R.drawable.rainy);
        updateWeatherBlock(binding.snowBlock, binding.imgSnow, selectedIndex == 3, R.drawable.snow_selected, R.drawable.snow);
    }

    private void updateWeatherBlock(RelativeLayout block, ImageView img, boolean isSelected, int selectedRes, int normalRes) {
        block.setBackground(ContextCompat.getDrawable(mContext, isSelected ? R.drawable.rdo_gender_select : R.drawable.rdo_gender_regular));
        img.setImageResource(isSelected ? selectedRes : normalRes);
    }
}
