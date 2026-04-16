package com.trending.water.drinking.reminder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.databinding.ScreenOnboardingFiveBinding;

public class Screen_OnBoarding_Five extends MasterBaseFragment<ScreenOnboardingFiveBinding> {

    @Override
    protected ScreenOnboardingFiveBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenOnboardingFiveBinding.inflate(inflater, container, false);
    }
}
