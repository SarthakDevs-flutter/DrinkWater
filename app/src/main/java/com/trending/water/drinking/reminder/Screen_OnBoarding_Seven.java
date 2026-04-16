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
import com.trending.water.drinking.reminder.databinding.ScreenOnboardingSevenBinding;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Seven extends MasterBaseFragment<ScreenOnboardingSevenBinding> {

    private View itemView;

    @Override
    protected ScreenOnboardingSevenBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenOnboardingSevenBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        updateUI();

        binding.activeBlock.setOnClickListener(v -> {
            boolean isActive = preferencesHelper.getBoolean(URLFactory.KEY_IS_ACTIVE, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_ACTIVE, !isActive);
            preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
            updateUI();
        });

        binding.pregnantBlock.setOnClickListener(v -> {
            boolean isPregnant = preferencesHelper.getBoolean(URLFactory.KEY_IS_PREGNANT, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_PREGNANT, !isPregnant);
            preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
            updateUI();
        });

        binding.breastfeedingBlock.setOnClickListener(v -> {
            boolean isBreastfeeding = preferencesHelper.getBoolean(URLFactory.KEY_IS_BREASTFEEDING, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_BREASTFEEDING, !isBreastfeeding);
            preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
            updateUI();
        });
    }

    private void updateUI() {
        boolean isActive = preferencesHelper.getBoolean(URLFactory.KEY_IS_ACTIVE, false);
        boolean isPregnant = preferencesHelper.getBoolean(URLFactory.KEY_IS_PREGNANT, false);
        boolean isBreastfeeding = preferencesHelper.getBoolean(URLFactory.KEY_IS_BREASTFEEDING, false);

        updateBlock(binding.activeBlock, binding.imgActive, isActive, R.drawable.active_selected, R.drawable.active);
        updateBlock(binding.pregnantBlock, binding.imgPregnant, isPregnant, R.drawable.pregnant_selected, R.drawable.pregnant);
        updateBlock(binding.breastfeedingBlock, binding.imgBreastfeeding, isBreastfeeding, R.drawable.breastfeeding_selected, R.drawable.breastfeeding);
    }

    private void updateBlock(RelativeLayout block, ImageView img, boolean isSelected, int selectedRes, int normalRes) {
        block.setBackground(ContextCompat.getDrawable(mContext, isSelected ? R.drawable.rdo_gender_select : R.drawable.rdo_gender_regular));
        img.setImageResource(isSelected ? selectedRes : normalRes);
    }
}
