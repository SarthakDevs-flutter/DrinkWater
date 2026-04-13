package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Seven extends MasterBaseFragment {
    RelativeLayout active_block;
    LinearLayout block_for_female;
    RelativeLayout breastfeeding_block;
    ImageView img_active;
    ImageView img_breastfeeding;
    ImageView img_pregnant;
    View item_view;
    RelativeLayout pregnant_block;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_onboarding_seven, container, false);
        FindViewById();
        Body();
        return this.item_view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.ph.getBoolean(URLFactory.IS_PREGNANT)) {
                this.pregnant_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
                this.img_pregnant.setImageResource(R.drawable.pregnant_selected);
            } else {
                this.pregnant_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
                this.img_pregnant.setImageResource(R.drawable.pregnant);
            }
            if (this.ph.getBoolean(URLFactory.IS_BREATFEEDING)) {
                this.breastfeeding_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
                this.img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected);
            } else {
                this.breastfeeding_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
                this.img_breastfeeding.setImageResource(R.drawable.breastfeeding);
            }
            int i = 0;
            if (this.ph.getBoolean(URLFactory.USER_GENDER)) {
                this.pregnant_block.setFocusableInTouchMode(true);
                this.pregnant_block.setClickable(true);
                this.pregnant_block.setFocusable(true);
                this.pregnant_block.setAlpha(1.0f);
                for (int i2 = 0; i2 < this.pregnant_block.getChildCount(); i2++) {
                    this.pregnant_block.getChildAt(i2).setEnabled(true);
                }
                this.breastfeeding_block.setFocusableInTouchMode(true);
                this.breastfeeding_block.setClickable(true);
                this.breastfeeding_block.setFocusable(true);
                this.breastfeeding_block.setAlpha(1.0f);
                while (true) {
                    int i3 = i;
                    if (i3 < this.breastfeeding_block.getChildCount()) {
                        this.breastfeeding_block.getChildAt(i3).setEnabled(true);
                        i = i3 + 1;
                    } else {
                        return;
                    }
                }
            } else {
                this.pregnant_block.setFocusableInTouchMode(false);
                this.pregnant_block.setClickable(false);
                this.pregnant_block.setFocusable(false);
                this.pregnant_block.setAlpha(0.5f);
                for (int i4 = 0; i4 < this.pregnant_block.getChildCount(); i4++) {
                    this.pregnant_block.getChildAt(i4).setEnabled(false);
                }
                this.breastfeeding_block.setFocusableInTouchMode(false);
                this.breastfeeding_block.setClickable(false);
                this.breastfeeding_block.setFocusable(false);
                this.breastfeeding_block.setAlpha(0.5f);
                for (int i5 = 0; i5 < this.breastfeeding_block.getChildCount(); i5++) {
                    this.breastfeeding_block.getChildAt(i5).setEnabled(false);
                }
            }
        }
    }

    private void FindViewById() {
        this.block_for_female = (LinearLayout) this.item_view.findViewById(R.id.block_for_female);
        this.active_block = (RelativeLayout) this.item_view.findViewById(R.id.active_block);
        this.pregnant_block = (RelativeLayout) this.item_view.findViewById(R.id.pregnant_block);
        this.breastfeeding_block = (RelativeLayout) this.item_view.findViewById(R.id.breastfeeding_block);
        this.img_active = (ImageView) this.item_view.findViewById(R.id.img_active);
        this.img_pregnant = (ImageView) this.item_view.findViewById(R.id.img_pregnant);
        this.img_breastfeeding = (ImageView) this.item_view.findViewById(R.id.img_breastfeeding);
    }

    private void Body() {
        setActive();
        setBreastfeeding();
        setPregnant();
        this.active_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_OnBoarding_Seven.this.ph.getBoolean(URLFactory.IS_ACTIVE)) {
                    Screen_OnBoarding_Seven.this.ph.savePreferences(URLFactory.IS_ACTIVE, false);
                } else {
                    Screen_OnBoarding_Seven.this.ph.savePreferences(URLFactory.IS_ACTIVE, true);
                }
                Screen_OnBoarding_Seven.this.setActive();
            }
        });
        this.pregnant_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_OnBoarding_Seven.this.ph.getBoolean(URLFactory.IS_PREGNANT)) {
                    Screen_OnBoarding_Seven.this.ph.savePreferences(URLFactory.IS_PREGNANT, false);
                } else {
                    Screen_OnBoarding_Seven.this.ph.savePreferences(URLFactory.IS_PREGNANT, true);
                }
                Screen_OnBoarding_Seven.this.setPregnant();
            }
        });
        this.breastfeeding_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_OnBoarding_Seven.this.ph.getBoolean(URLFactory.IS_BREATFEEDING)) {
                    Screen_OnBoarding_Seven.this.ph.savePreferences(URLFactory.IS_BREATFEEDING, false);
                } else {
                    Screen_OnBoarding_Seven.this.ph.savePreferences(URLFactory.IS_BREATFEEDING, true);
                }
                Screen_OnBoarding_Seven.this.setBreastfeeding();
            }
        });
    }

    public void setActive() {
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
        if (this.ph.getBoolean(URLFactory.IS_ACTIVE)) {
            this.active_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
            this.img_active.setImageResource(R.drawable.active_selected);
            return;
        }
        this.active_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
        this.img_active.setImageResource(R.drawable.active);
    }

    public void setPregnant() {
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
        if (this.ph.getBoolean(URLFactory.IS_PREGNANT)) {
            this.pregnant_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
            this.img_pregnant.setImageResource(R.drawable.pregnant_selected);
            return;
        }
        this.pregnant_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
        this.img_pregnant.setImageResource(R.drawable.pregnant);
    }

    public void setBreastfeeding() {
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
        if (this.ph.getBoolean(URLFactory.IS_BREATFEEDING)) {
            this.breastfeeding_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
            this.img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected);
            return;
        }
        this.breastfeeding_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
        this.img_breastfeeding.setImageResource(R.drawable.breastfeeding);
    }
}
