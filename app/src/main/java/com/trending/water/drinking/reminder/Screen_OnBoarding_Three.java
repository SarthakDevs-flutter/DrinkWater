package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.github.mikephil.charting.utils.Utils;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.custom.DigitsInputFilter;
import com.trending.water.drinking.reminder.custom.InputFilterRange;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.lib.horizontalpicker.HorizontalPicker;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.List;

public class Screen_OnBoarding_Three extends MasterBaseFragment {
    
    private static final String TAG = "Screen_OnBoarding_Three";

    private HorizontalPicker pickerCm;
    private HorizontalPicker pickerFeet;
    private HorizontalPicker pickerKg;
    private HorizontalPicker pickerLb;
    private RadioButton rdoCm;
    private RadioButton rdoFeet;
    private RadioButton rdoKg;
    private RadioButton rdoLb;
    private AppCompatEditText txtHeight;
    private AppCompatEditText txtWeight;
    
    private final List<String> weightKgList = new ArrayList<>();
    private final List<String> weightLbList = new ArrayList<>();
    private final List<String> heightCmList = new ArrayList<>();
    private final List<String> heightFeetList = new ArrayList<>();
    private final List<Double> heightFeetElements = new ArrayList<>();
    
    private boolean isExecute = true;
    private boolean isExecuteWeight = true;
    private View itemView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.screen_onboarding_three, container, false);
        findViewByIds(itemView);
        
        initializePickers();
        initView();
        
        return itemView;
    }

    private void findViewByIds(View view) {
        txtHeight = view.findViewById(R.id.txt_height);
        txtWeight = view.findViewById(R.id.txt_weight);
        rdoCm = view.findViewById(R.id.rdo_cm);
        rdoFeet = view.findViewById(R.id.rdo_feet);
        rdoKg = view.findViewById(R.id.rdo_kg);
        rdoLb = view.findViewById(R.id.rdo_lb);
        pickerKg = view.findViewById(R.id.pickerKG);
        pickerLb = view.findViewById(R.id.pickerLB);
        pickerCm = view.findViewById(R.id.pickerCM);
        pickerFeet = view.findViewById(R.id.pickerFeet);
    }

    private void initializePickers() {
        initWeightKg();
        initWeightLb();
        initHeightCm();
        initHeightFeet();
        
        if (preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true)) {
            pickerLb.setVisibility(View.GONE);
        } else {
            pickerKg.setVisibility(View.GONE);
        }

        if (preferencesHelper.getBoolean(URLFactory.KEY_PERSON_HEIGHT_UNIT, true)) {
            pickerFeet.setVisibility(View.GONE);
        } else {
            pickerCm.setVisibility(View.GONE);
        }
    }

    private void initWeightKg() {
        weightKgList.clear();
        float currentWeight = 30.0f;
        weightKgList.add(String.valueOf(currentWeight));
        for (int i = 0; i < 200; i++) {
            currentWeight += 0.5f;
            weightKgList.add(String.valueOf(currentWeight));
        }
        
        CharSequence[] values = weightKgList.toArray(new CharSequence[0]);
        pickerKg.setValues(values);
        pickerKg.setSideItems(1);
        pickerKg.setOnItemSelectedListener(index -> {
            txtWeight.setText(values[index]);
            Log.d(TAG, "Weight KG selected: " + values[index]);
        });
        
        try {
            pickerKg.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {}
    }

    private void initWeightLb() {
        weightLbList.clear();
        for (int i = 66; i < 288; i++) {
            weightLbList.add(String.valueOf(i));
        }
        
        CharSequence[] values = weightLbList.toArray(new CharSequence[0]);
        pickerLb.setValues(values);
        pickerLb.setSideItems(1);
        pickerLb.setOnItemSelectedListener(index -> {
            txtWeight.setText(values[index]);
            Log.d(TAG, "Weight LB selected: " + values[index]);
        });
        
        try {
            pickerLb.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {}
    }

    private void initHeightCm() {
        heightCmList.clear();
        for (int i = 60; i < 241; i++) {
            heightCmList.add(String.valueOf(i));
        }
        
        CharSequence[] values = heightCmList.toArray(new CharSequence[0]);
        pickerCm.setValues(values);
        pickerCm.setSideItems(1);
        pickerCm.setOnItemSelectedListener(index -> {
            txtHeight.setText(values[index]);
            Log.d(TAG, "Height CM selected: " + values[index]);
        });
        
        try {
            pickerCm.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {}
    }

    private void initHeightFeet() {
        heightFeetList.clear();
        for (int i = 2; i <= 7; i++) {
            for (int j = 0; j <= 11; j++) {
                heightFeetList.add(i + "." + j);
            }
        }
        heightFeetList.add("8.0");

        CharSequence[] values = heightFeetList.toArray(new CharSequence[0]);
        pickerFeet.setValues(values);
        pickerFeet.setSideItems(1);
        pickerFeet.setOnItemSelectedListener(index -> {
            txtHeight.setText(values[index]);
            Log.d(TAG, "Height Feet selected: " + values[index]);
        });
        
        try {
            pickerFeet.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {}
    }

    private void initView() {
        populateHeightFeetElements();

        rdoKg.setOnCheckedChangeListener((cb, checked) -> {
            pickerKg.setVisibility(checked ? View.VISIBLE : View.GONE);
            pickerLb.setVisibility(checked ? View.GONE : View.VISIBLE);
        });

        rdoFeet.setOnCheckedChangeListener((cb, checked) -> {
            pickerFeet.setVisibility(checked ? View.VISIBLE : View.GONE);
            pickerCm.setVisibility(checked ? View.GONE : View.VISIBLE);
        });

        setupHeightTextWatcher();
        setupWeightTextWatcher();
        setupRadioClickListeners();
        
        initializeUIStates();
    }

    private void populateHeightFeetElements() {
        heightFeetElements.clear();
        for (String s : heightFeetList) {
            heightFeetElements.add(Double.parseDouble(s));
        }
    }

    private void setupHeightTextWatcher() {
        txtHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.equals(".")) {
                    txtHeight.setText("2.0");
                }
                
                if (!stringHelper.check_blank_data(input) && isExecute) {
                    if (rdoCm.isChecked()) {
                        int index = heightCmList.indexOf(input);
                        if (index != -1) pickerCm.setSelectedItem(index);
                    } else {
                        int index = heightFeetList.indexOf(input);
                        if (index != -1) pickerFeet.setSelectedItem(index);
                    }
                    saveHeightData();
                }
            }
        });
    }

    private void setupWeightTextWatcher() {
        txtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString().trim();
                    if (input.equals(".")) {
                        txtWeight.setText("30.0");
                    }
                    if (!stringHelper.check_blank_data(input) && isExecuteWeight) {
                        float weight = Float.parseFloat(input);
                        if (rdoKg.isChecked()) {
                            int index = weightKgList.indexOf(input);
                            if (index != -1) pickerKg.setSelectedItem(index);
                        } else {
                            int index = weightLbList.indexOf(String.valueOf((int) weight));
                            if (index != -1) pickerLb.setSelectedItem(index);
                        }
                    }
                    saveWeightData();
                } catch (Exception ignored) {}
            }
        });
    }

    private void setupRadioClickListeners() {
        rdoCm.setOnClickListener(v -> {
            String current = txtHeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                int cmValue = 150;
                try {
                    String clean = URLFactory.getData(current);
                    float feetVal = Float.parseFloat(clean);
                    int feet = (int) feetVal;
                    int inch = 0;
                    if (clean.contains(".")) {
                        String decimal = clean.substring(clean.indexOf(".") + 1);
                        if (!stringHelper.check_blank_data(decimal)) {
                            inch = Integer.parseInt(decimal);
                        }
                    }
                    cmValue = (int) Math.round(((feet * 12) + inch) * 2.54d);
                } catch (Exception ignored) {}
                
                int index = heightCmList.indexOf(String.valueOf(cmValue));
                if (index != -1) pickerCm.setSelectedItem(index);
                
                rdoFeet.setClickable(true);
                rdoCm.setClickable(false);
                txtHeight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
                txtHeight.setText(String.valueOf(cmValue));
                txtHeight.setSelection(txtHeight.getText().length());
                saveHeightData();
            } else {
                rdoFeet.setChecked(true);
                rdoCm.setChecked(false);
            }
        });

        rdoFeet.setOnClickListener(v -> {
            String current = txtHeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                String feetStr = "5.0";
                try {
                    int cm = (int) Float.parseFloat(current);
                    int totalInches = (int) Math.round(cm / 2.54d);
                    feetStr = (totalInches / 12) + "." + (totalInches % 12);
                } catch (Exception ignored) {}
                
                int index = heightFeetList.indexOf(URLFactory.getData(feetStr));
                if (index != -1) pickerFeet.setSelectedItem(index);
                
                rdoFeet.setClickable(false);
                rdoCm.setClickable(true);
                txtHeight.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, heightFeetElements)});
                txtHeight.setText(URLFactory.getData(feetStr));
                txtHeight.setSelection(txtHeight.getText().length());
                saveHeightData();
            } else {
                rdoFeet.setChecked(false);
                rdoCm.setChecked(true);
            }
        });

        rdoKg.setOnClickListener(v -> {
            String current = txtWeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                double lb = Double.parseDouble(current);
                double kg = lb > 0 ? Math.round(HeightWeightHelper.convertLbToKg(lb)) : 0;
                int index = weightKgList.indexOf(String.valueOf((float) kg));
                if (index != -1) pickerKg.setSelectedItem(index);
                
                txtWeight.setFilters(new InputFilter[]{new InputFilterWeightRange(0, 130.0d)});
                txtWeight.setText(URLFactory.getData(String.valueOf((int) kg)));
                rdoKg.setClickable(false);
                rdoLb.setClickable(true);
            }
            saveWeightData();
        });

        rdoLb.setOnClickListener(v -> {
            String current = txtWeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                double kg = Double.parseDouble(current);
                double lb = kg > 0 ? Math.round(HeightWeightHelper.kgToLbConverter(kg)) : 0;
                int index = weightLbList.indexOf(String.valueOf((int) lb));
                if (index != -1) pickerLb.setSelectedItem(index);
                
                txtWeight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
                txtWeight.setText(String.valueOf((int) lb));
                rdoKg.setClickable(true);
                rdoLb.setClickable(false);
            }
            saveWeightData();
        });
    }

    private void initializeUIStates() {
        boolean isCm = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_HEIGHT_UNIT, true);
        rdoCm.setChecked(isCm);
        rdoFeet.setChecked(!isCm);
        rdoCm.setClickable(!isCm);
        rdoFeet.setClickable(isCm);

        boolean isKg = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        rdoKg.setChecked(isKg);
        rdoLb.setChecked(!isKg);
        rdoKg.setClickable(!isKg);
        rdoLb.setClickable(isKg);

        String height = preferencesHelper.getString(URLFactory.KEY_PERSON_HEIGHT, "");
        if (!stringHelper.check_blank_data(height)) {
            txtHeight.setFilters(new InputFilter[]{isCm ? new DigitsInputFilter(3, 0, 240.0d) : new InputFilterRange(0, heightFeetElements)});
            txtHeight.setText(URLFactory.getData(height));
        } else {
            txtHeight.setText(isCm ? "150" : "5.0");
        }

        String weight = preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "");
        if (!stringHelper.check_blank_data(weight)) {
            txtWeight.setFilters(new InputFilter[]{isKg ? new InputFilterWeightRange(0, 130.0d) : new DigitsInputFilter(3, 0, 287.0d)});
            txtWeight.setText(URLFactory.getData(weight));
        } else {
            txtWeight.setText(isKg ? "80.0" : "176");
        }
    }

    private void saveHeightData() {
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT, txtHeight.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT_UNIT, rdoCm.isChecked());
        preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
    }

    private void saveWeightData() {
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT, txtWeight.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT_UNIT, rdoKg.isChecked());
        preferencesHelper.savePreferences(URLFactory.KEY_WATER_UNIT, rdoKg.isChecked() ? "ML" : "FL OZ");
        preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
    }
}
