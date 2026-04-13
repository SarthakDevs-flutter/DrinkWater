package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Two extends MasterBaseFragment {
    RelativeLayout female_block;
    ImageView img_female;
    ImageView img_male;
    boolean isMaleGender = true;
    View item_view;
    AppCompatTextView lbl_female;
    AppCompatTextView lbl_male;
    RelativeLayout male_block;
    AppCompatEditText txt_user_name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_onboarding_two, container, false);
        FindViewById();
        Body();
        return this.item_view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void FindViewById() {
        this.male_block = (RelativeLayout) this.item_view.findViewById(R.id.male_block);
        this.img_male = (ImageView) this.item_view.findViewById(R.id.img_male);
        this.lbl_male = (AppCompatTextView) this.item_view.findViewById(R.id.lbl_male);
        this.female_block = (RelativeLayout) this.item_view.findViewById(R.id.female_block);
        this.img_female = (ImageView) this.item_view.findViewById(R.id.img_female);
        this.lbl_female = (AppCompatTextView) this.item_view.findViewById(R.id.lbl_female);
        this.txt_user_name = (AppCompatEditText) this.item_view.findViewById(R.id.txt_user_name);
    }

    private void Body() {
        this.male_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_OnBoarding_Two.this.setGender(true);
            }
        });
        this.female_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_OnBoarding_Two.this.setGender(false);
            }
        });
        this.txt_user_name.setText(this.ph.getString(URLFactory.USER_NAME));
        setGender(!this.ph.getBoolean(URLFactory.USER_GENDER));
        this.txt_user_name.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                Screen_OnBoarding_Two.this.ph.savePreferences(URLFactory.USER_NAME, Screen_OnBoarding_Two.this.txt_user_name.getText().toString().trim());
            }
        });
    }

    public void setGender(boolean isMale) {
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
        if (isMale) {
            this.isMaleGender = true;
            this.ph.savePreferences(URLFactory.USER_GENDER, false);
            this.ph.savePreferences(URLFactory.IS_PREGNANT, false);
            this.ph.savePreferences(URLFactory.IS_BREATFEEDING, false);
            this.male_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
            this.img_male.setImageResource(R.drawable.ic_male_selected);
            this.female_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
            this.img_female.setImageResource(R.drawable.ic_female_normal);
            return;
        }
        this.isMaleGender = false;
        this.ph.savePreferences(URLFactory.USER_GENDER, true);
        this.male_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
        this.img_male.setImageResource(R.drawable.ic_male_normal);
        this.female_block.setBackground(this.mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
        this.img_female.setImageResource(R.drawable.ic_female_selected);
    }
}
