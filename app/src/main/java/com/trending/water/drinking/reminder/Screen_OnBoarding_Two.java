package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.databinding.ScreenOnboardingTwoBinding;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Two extends MasterBaseFragment<ScreenOnboardingTwoBinding> {

    @Override
    protected ScreenOnboardingTwoBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenOnboardingTwoBinding.inflate(inflater, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        binding.maleBlock.setOnClickListener(v -> setGender(true));
        binding.femaleBlock.setOnClickListener(v -> setGender(false));

        binding.txtUserName.setText(preferencesHelper.getString(URLFactory.KEY_USER_NAME, ""));

        // Initial state
        boolean isFemale = preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false);
        setGender(!isFemale);

        binding.txtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                preferencesHelper.savePreferences(URLFactory.KEY_USER_NAME, s.toString().trim());
            }
        });
    }

    public void setGender(boolean isMale) {
        preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
        if (isMale) {
            preferencesHelper.savePreferences(URLFactory.KEY_USER_GENDER, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_PREGNANT, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_BREASTFEEDING, false);

            binding.maleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_select));
            binding.imgMale.setImageResource(R.drawable.ic_male_selected);
            binding.femaleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_regular));
            binding.imgFemale.setImageResource(R.drawable.ic_female_normal);
        } else {
            preferencesHelper.savePreferences(URLFactory.KEY_USER_GENDER, true);

            binding.maleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_regular));
            binding.imgMale.setImageResource(R.drawable.ic_male_normal);
            binding.femaleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_select));
            binding.imgFemale.setImageResource(R.drawable.ic_female_selected);
        }
    }
}
