package com.trending.water.drinking.reminder;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.github.mikephil.charting.utils.Utils;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.Locale;

public class Screen_OnBoarding_Four extends MasterBaseFragment {
    
    private static final String TAG = "Screen_OnBoarding_Four";

    private AppCompatTextView lblGoal;
    private AppCompatTextView lblUnit;
    private AppCompatTextView lblSetGoalManually;
    
    private View itemView;
    private boolean isExecute = true;
    private boolean isExecuteSeekBar = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.screen_onboarding_four, container, false);
        findViewByIds(itemView);
        initView();
        return itemView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            updateGoalDisplay();
        }
    }

    private void findViewByIds(View view) {
        lblGoal = view.findViewById(R.id.lbl_goal);
        lblUnit = view.findViewById(R.id.lbl_unit);
        lblSetGoalManually = view.findViewById(R.id.lbl_set_goal_manually);
    }

    private void initView() {
        updateGoalDisplay();
        lblSetGoalManually.setOnClickListener(v -> showSetManuallyGoalDialog());
    }

    private void updateGoalDisplay() {
        if (preferencesHelper.getBoolean(URLFactory.KEY_SET_MANUALLY_GOAL, false)) {
            float manualGoal = preferencesHelper.getFloat(URLFactory.KEY_SET_MANUALLY_GOAL_VALUE, 2500.0f);
            URLFactory.dailyWaterValue = manualGoal;
            preferencesHelper.savePreferences(URLFactory.KEY_DAILY_WATER_GOAL, manualGoal);
            
            lblGoal.setText(String.format(Locale.US, "%d", (int) manualGoal));
            lblUnit.setText(preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true) ? "ML" : "FL OZ");
        } else {
            calculateGoal();
        }
    }

    private void calculateGoal() {
        String weightStr = preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "");
        if (stringHelper.check_blank_data(weightStr)) return;

        double weightKg;
        try {
            if (preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true)) {
                weightKg = Double.parseDouble(weightStr);
            } else {
                weightKg = HeightWeightHelper.convertLbToKg(Double.parseDouble(weightStr));
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing weight: " + weightStr, e);
            return;
        }

        boolean isFemale = preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false);
        boolean isActive = preferencesHelper.getBoolean(URLFactory.KEY_IS_ACTIVE, false);
        boolean isPregnant = preferencesHelper.getBoolean(URLFactory.KEY_IS_PREGNANT, false);
        boolean isBreastfeeding = preferencesHelper.getBoolean(URLFactory.KEY_IS_BREASTFEEDING, false);
        int weatherIdx = preferencesHelper.getInt(URLFactory.KEY_WEATHER_CONDITIONS, 0);

        double baseWater;
        if (isFemale) {
            baseWater = (isActive ? URLFactory.ACTIVE_FEMALE_WATER_ML_PER_KG : URLFactory.FEMALE_WATER_ML_PER_KG) * weightKg;
        } else {
            baseWater = (isActive ? URLFactory.ACTIVE_MALE_WATER_ML_PER_KG : URLFactory.MALE_WATER_ML_PER_KG) * weightKg;
        }

        double weatherFactor;
        switch (weatherIdx) {
            case 1: weatherFactor = URLFactory.WEATHER_CLOUDY_FACTOR; break;
            case 2: weatherFactor = URLFactory.WEATHER_RAINY_FACTOR; break;
            case 3: weatherFactor = URLFactory.WEATHER_SNOW_FACTOR; break;
            default: weatherFactor = URLFactory.WEATHER_SUNNY_FACTOR; break;
        }
        
        double calculatedGoalMl = baseWater * weatherFactor;

        if (isFemale) {
            if (isPregnant) calculatedGoalMl += URLFactory.PREGNANT_ADDITIONAL_WATER_ML;
            if (isBreastfeeding) calculatedGoalMl += URLFactory.BREASTFEEDING_ADDITIONAL_WATER_ML;
        }

        // Constraints
        calculatedGoalMl = Math.max(900.0, Math.min(calculatingGoalMl, 8000.0));

        if (preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true)) {
            lblUnit.setText("ML");
            URLFactory.dailyWaterValue = (float) Math.round(calculatedGoalMl);
        } else {
            lblUnit.setText("FL OZ");
            URLFactory.dailyWaterValue = (float) Math.round(HeightWeightHelper.convertMlToOz(calculatedGoalMl));
        }

        lblGoal.setText(String.format(Locale.US, "%d", (int) URLFactory.dailyWaterValue));
        preferencesHelper.savePreferences(URLFactory.KEY_DAILY_WATER_GOAL, URLFactory.dailyWaterValue);
    }

    private void showSetManuallyGoalDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_manually_goal, null, false);
        final AppCompatEditText etGoal = view.findViewById(R.id.lbl_goal);
        AppCompatTextView tvUnit = view.findViewById(R.id.lbl_unit);
        RelativeLayout btnCancel = view.findViewById(R.id.btn_cancel);
        RelativeLayout btnSave = view.findViewById(R.id.btn_save);
        final SeekBar seekBar = view.findViewById(R.id.seekbarGoal);

        boolean isMl = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        float currentGoal = preferencesHelper.getBoolean(URLFactory.KEY_SET_MANUALLY_GOAL, false) ? 
                preferencesHelper.getFloat(URLFactory.KEY_SET_MANUALLY_GOAL_VALUE, 2500f) : 
                preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 2500f);

        etGoal.setText(String.format(Locale.US, "%d", (int) currentGoal));
        tvUnit.setText(isMl ? "ML" : "FL OZ");

        int minVal = isMl ? 900 : 30;
        int maxVal = isMl ? 8000 : 270;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(minVal);
        }
        seekBar.setMax(maxVal);
        seekBar.setProgress((int) currentGoal);

        etGoal.setFilters(new InputFilter[]{
                new InputFilterWeightRange(0.1, (double) maxVal), 
                new InputFilter.LengthFilter(isMl ? 4 : 3)
        });

        etGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isExecuteSeekBar = false;
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String val = s.toString().trim();
                    if (!stringHelper.check_blank_data(val) && isExecute) {
                        seekBar.setProgress(Integer.parseInt(val));
                    }
                } catch (Exception ignored) {}
                isExecuteSeekBar = true;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isExecuteSeekBar) {
                    int finalProgress = progress;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        finalProgress = Math.max(progress, minVal);
                        seekBar.setProgress(finalProgress);
                    }
                    etGoal.setText(String.valueOf(finalProgress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { isExecute = false; }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { isExecute = true; }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String goalStr = etGoal.getText().toString().trim();
            if (stringHelper.check_blank_data(goalStr)) return;
            
            float newGoal = Float.parseFloat(goalStr);
            if (isMl && newGoal < 900) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_set_daily_goal_validation));
                return;
            } else if (!isMl && newGoal < 30) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_set_daily_goal_validation));
                return;
            }

            URLFactory.dailyWaterValue = newGoal;
            preferencesHelper.savePreferences(URLFactory.KEY_DAILY_WATER_GOAL, newGoal);
            preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, true);
            preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL_VALUE, newGoal);
            
            lblGoal.setText(String.format(Locale.US, "%d", (int) newGoal));
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }
}
