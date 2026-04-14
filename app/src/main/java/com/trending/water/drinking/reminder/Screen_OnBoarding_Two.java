package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Two extends MasterBaseFragment {
    
    private RelativeLayout maleBlock;
    private RelativeLayout femaleBlock;
    private ImageView imgMale;
    private ImageView imgFemale;
    private AppCompatTextView lblMale;
    private AppCompatTextView lblFemale;
    private AppCompatEditText txtUserName;
    
    private View itemView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.screen_onboarding_two, container, false);
        findViewByIds(itemView);
        initView();
        return itemView;
    }

    private void findViewByIds(View view) {
        maleBlock = view.findViewById(R.id.male_block);
        imgMale = view.findViewById(R.id.img_male);
        lblMale = view.findViewById(R.id.lbl_male);
        femaleBlock = view.findViewById(R.id.female_block);
        imgFemale = view.findViewById(R.id.img_female);
        lblFemale = view.findViewById(R.id.lbl_female);
        txtUserName = view.findViewById(R.id.txt_user_name);
    }

    private void initView() {
        maleBlock.setOnClickListener(v -> setGender(true));
        femaleBlock.setOnClickListener(v -> setGender(false));

        txtUserName.setText(preferencesHelper.getString(URLFactory.KEY_USER_NAME, ""));
        
        // Initial state
        boolean isFemale = preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false);
        setGender(!isFemale);

        txtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
            
            maleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_select));
            imgMale.setImageResource(R.drawable.ic_male_selected);
            femaleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_regular));
            imgFemale.setImageResource(R.drawable.ic_female_normal);
        } else {
            preferencesHelper.savePreferences(URLFactory.KEY_USER_GENDER, true);
            
            maleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_regular));
            imgMale.setImageResource(R.drawable.ic_male_normal);
            femaleBlock.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rdo_gender_select));
            imgFemale.setImageResource(R.drawable.ic_female_selected);
        }
    }
}
