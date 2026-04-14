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

public class Screen_OnBoarding_Seven extends MasterBaseFragment {

    private RelativeLayout activeBlock;
    private RelativeLayout pregnantBlock;
    private RelativeLayout breastfeedingBlock;
    private ImageView imgActive;
    private ImageView imgPregnant;
    private ImageView imgBreastfeeding;

    private View itemView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.screen_onboarding_seven, container, false);
        findViewByIds(itemView);
        initView();
        return itemView;
    }

    private void findViewByIds(View view) {
        activeBlock = view.findViewById(R.id.active_block);
        pregnantBlock = view.findViewById(R.id.pregnant_block);
        breastfeedingBlock = view.findViewById(R.id.breastfeeding_block);
        imgActive = view.findViewById(R.id.img_active);
        imgPregnant = view.findViewById(R.id.img_pregnant);
        imgBreastfeeding = view.findViewById(R.id.img_breastfeeding);
    }

    private void initView() {
        updateUI();

        activeBlock.setOnClickListener(v -> {
            boolean isActive = preferencesHelper.getBoolean(URLFactory.KEY_IS_ACTIVE, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_ACTIVE, !isActive);
            preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
            updateUI();
        });

        pregnantBlock.setOnClickListener(v -> {
            boolean isPregnant = preferencesHelper.getBoolean(URLFactory.KEY_IS_PREGNANT, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_PREGNANT, !isPregnant);
            preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
            updateUI();
        });

        breastfeedingBlock.setOnClickListener(v -> {
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

        updateBlock(activeBlock, imgActive, isActive, R.drawable.active_selected, R.drawable.active);
        updateBlock(pregnantBlock, imgPregnant, isPregnant, R.drawable.pregnant_selected, R.drawable.pregnant);
        updateBlock(breastfeedingBlock, imgBreastfeeding, isBreastfeeding, R.drawable.breastfeeding_selected, R.drawable.breastfeeding);
    }

    private void updateBlock(RelativeLayout block, ImageView img, boolean isSelected, int selectedRes, int normalRes) {
        block.setBackground(ContextCompat.getDrawable(mContext, isSelected ? R.drawable.rdo_gender_select : R.drawable.rdo_gender_regular));
        img.setImageResource(isSelected ? selectedRes : normalRes);
    }
}
