package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.databinding.ScreenOnboardingFiveBinding;

public class Screen_OnBoarding_Five extends MasterBaseFragment<ScreenOnboardingFiveBinding> {

    @Override
    protected ScreenOnboardingFiveBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenOnboardingFiveBinding.inflate(inflater, container, false);
    }
}
