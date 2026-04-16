package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.utils.Utils;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.custom.DigitsInputFilter;
import com.trending.water.drinking.reminder.custom.InputFilterRange;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.databinding.ScreenOnboardingThreeBinding;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.List;

public class Screen_OnBoarding_Three extends MasterBaseFragment<ScreenOnboardingThreeBinding> {

    private static final String TAG = "Screen_OnBoarding_Three";
    private final List<String> weightKgList = new ArrayList<>();
    private final List<String> weightLbList = new ArrayList<>();
    private final List<String> heightCmList = new ArrayList<>();
    private final List<String> heightFeetList = new ArrayList<>();
    private final List<Double> heightFeetElements = new ArrayList<>();
    private boolean isExecute = true;
    private boolean isExecuteWeight = true;
    private View itemView;

    @Override
    protected ScreenOnboardingThreeBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenOnboardingThreeBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializePickers();
        initView();
    }


    private void initializePickers() {
        initWeightKg();
        initWeightLb();
        initHeightCm();
        initHeightFeet();

        if (preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true)) {
            binding.pickerLB.setVisibility(View.GONE);
        } else {
            binding.pickerKG.setVisibility(View.GONE);
        }

        if (preferencesHelper.getBoolean(URLFactory.KEY_PERSON_HEIGHT_UNIT, true)) {
            binding.pickerFeet.setVisibility(View.GONE);
        } else {
            binding.pickerCM.setVisibility(View.GONE);
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
        binding.pickerKG.setValues(values);
        binding.pickerKG.setSideItems(1);
        binding.pickerKG.setOnItemSelectedListener(index -> {
            binding.txtWeight.setText(values[index]);
            Log.d(TAG, "Weight KG selected: " + values[index]);
        });

        try {
            binding.pickerKG.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {
        }
    }

    private void initWeightLb() {
        weightLbList.clear();
        for (int i = 66; i < 288; i++) {
            weightLbList.add(String.valueOf(i));
        }

        CharSequence[] values = weightLbList.toArray(new CharSequence[0]);
        binding.pickerLB.setValues(values);
        binding.pickerLB.setSideItems(1);
        binding.pickerLB.setOnItemSelectedListener(index -> {
            binding.txtWeight.setText(values[index]);
            Log.d(TAG, "Weight LB selected: " + values[index]);
        });

        try {
            binding.pickerLB.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {
        }
    }

    private void initHeightCm() {
        heightCmList.clear();
        for (int i = 60; i < 241; i++) {
            heightCmList.add(String.valueOf(i));
        }

        CharSequence[] values = heightCmList.toArray(new CharSequence[0]);
        binding.pickerCM.setValues(values);
        binding.pickerCM.setSideItems(1);
        binding.pickerCM.setOnItemSelectedListener(index -> {
            binding.txtHeight.setText(values[index]);
            Log.d(TAG, "Height CM selected: " + values[index]);
        });

        try {
            binding.pickerCM.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {
        }
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
        binding.pickerFeet.setValues(values);
        binding.pickerFeet.setSideItems(1);
        binding.pickerFeet.setOnItemSelectedListener(index -> {
            binding.txtHeight.setText(values[index]);
            Log.d(TAG, "Height Feet selected: " + values[index]);
        });

        try {
            binding.pickerFeet.setTextSize(getResources().getDimension(R.dimen.dp_30));
        } catch (Exception ignored) {
        }
    }

    private void initView() {
        populateHeightFeetElements();

        binding.rdoKg.setOnCheckedChangeListener((cb, checked) -> {
            binding.pickerKG.setVisibility(checked ? View.VISIBLE : View.GONE);
            binding.pickerLB.setVisibility(checked ? View.GONE : View.VISIBLE);
        });

        binding.rdoFeet.setOnCheckedChangeListener((cb, checked) -> {
            binding.pickerFeet.setVisibility(checked ? View.VISIBLE : View.GONE);
            binding.pickerCM.setVisibility(checked ? View.GONE : View.VISIBLE);
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
        binding.txtHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.equals(".")) {
                    binding.txtHeight.setText("2.0");
                }

                if (!stringHelper.check_blank_data(input) && isExecute) {
                    if (binding.rdoCm.isChecked()) {
                        int index = heightCmList.indexOf(input);
                        if (index != -1) binding.pickerCM.setSelectedItem(index);
                    } else {
                        int index = heightFeetList.indexOf(input);
                        if (index != -1) binding.pickerFeet.setSelectedItem(index);
                    }
                    saveHeightData();
                }
            }
        });
    }

    private void setupWeightTextWatcher() {
        binding.txtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString().trim();
                    if (input.equals(".")) {
                        binding.txtWeight.setText("30.0");
                    }
                    if (!stringHelper.check_blank_data(input) && isExecuteWeight) {
                        float weight = Float.parseFloat(input);
                        if (binding.rdoKg.isChecked()) {
                            int index = weightKgList.indexOf(input);
                            if (index != -1) binding.pickerKG.setSelectedItem(index);
                        } else {
                            int index = weightLbList.indexOf(String.valueOf((int) weight));
                            if (index != -1) binding.pickerLB.setSelectedItem(index);
                        }
                    }
                    saveWeightData();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void setupRadioClickListeners() {
        binding.rdoCm.setOnClickListener(v -> {
            String current = binding.txtHeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                int cmValue = 150;
                try {
                    String clean = getData(current);
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
                } catch (Exception ignored) {
                }

                int index = heightCmList.indexOf(String.valueOf(cmValue));
                if (index != -1) binding.pickerCM.setSelectedItem(index);

                binding.rdoFeet.setClickable(true);
                binding.rdoCm.setClickable(false);
                binding.txtHeight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
                binding.txtHeight.setText(String.valueOf(cmValue));
                binding.txtHeight.setSelection(binding.txtHeight.getText().length());
                saveHeightData();
            } else {
                binding.rdoFeet.setChecked(true);
                binding.rdoCm.setChecked(false);
            }
        });

        binding.rdoFeet.setOnClickListener(v -> {
            String current = binding.txtHeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                String feetStr = "5.0";
                try {
                    int cm = (int) Float.parseFloat(current);
                    int totalInches = (int) Math.round(cm / 2.54d);
                    feetStr = (totalInches / 12) + "." + (totalInches % 12);
                } catch (Exception ignored) {
                }

                int index = heightFeetList.indexOf(getData(feetStr));
                if (index != -1) binding.pickerFeet.setSelectedItem(index);

                binding.rdoFeet.setClickable(false);
                binding.rdoCm.setClickable(true);
                binding.txtHeight.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, heightFeetElements)});
                binding.txtHeight.setText(getData(feetStr));
                binding.txtHeight.setSelection(binding.txtHeight.getText().length());
                saveHeightData();
            } else {
                binding.rdoFeet.setChecked(false);
                binding.rdoCm.setChecked(true);
            }
        });

        binding.rdoKg.setOnClickListener(v -> {
            String current = binding.txtWeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                double lb = Double.parseDouble(current);
                double kg = lb > 0 ? Math.round(HeightWeightHelper.convertLbToKg(lb)) : 0;
                int index = weightKgList.indexOf(String.valueOf((float) kg));
                if (index != -1) binding.pickerKG.setSelectedItem(index);

                binding.txtWeight.setFilters(new InputFilter[]{new InputFilterWeightRange(0, 130.0d)});
                binding.txtWeight.setText(getData(String.valueOf((int) kg)));
                binding.rdoKg.setClickable(false);
                binding.rdoLb.setClickable(true);
            }
            saveWeightData();
        });

        binding.rdoLb.setOnClickListener(v -> {
            String current = binding.txtWeight.getText().toString();
            if (!stringHelper.check_blank_data(current)) {
                double kg = Double.parseDouble(current);
                double lb = kg > 0 ? Math.round(HeightWeightHelper.convertKgToLb(kg)) : 0;
                int index = weightLbList.indexOf(String.valueOf((int) lb));
                if (index != -1) binding.pickerLB.setSelectedItem(index);

                binding.txtWeight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
                binding.txtWeight.setText(String.valueOf((int) lb));
                binding.rdoKg.setClickable(true);
                binding.rdoLb.setClickable(false);
            }
            saveWeightData();
        });
    }

    private void initializeUIStates() {
        boolean isCm = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_HEIGHT_UNIT, true);
        binding.rdoCm.setChecked(isCm);
        binding.rdoFeet.setChecked(!isCm);
        binding.rdoCm.setClickable(!isCm);
        binding.rdoFeet.setClickable(isCm);

        boolean isKg = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        binding.rdoKg.setChecked(isKg);
        binding.rdoLb.setChecked(!isKg);
        binding.rdoKg.setClickable(!isKg);
        binding.rdoLb.setClickable(isKg);

        String height = preferencesHelper.getString(URLFactory.KEY_PERSON_HEIGHT, "");
        if (!stringHelper.check_blank_data(height)) {
            binding.txtHeight.setFilters(new InputFilter[]{isCm ? new DigitsInputFilter(3, 0, 240.0d) : new InputFilterRange(0, heightFeetElements)});
            binding.txtHeight.setText(getData(height));
        } else {
            binding.txtHeight.setText(isCm ? "150" : "5.0");
        }

        String weight = preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "");
        if (!stringHelper.check_blank_data(weight)) {
            binding.txtWeight.setFilters(new InputFilter[]{isKg ? new InputFilterWeightRange(0, 130.0d) : new DigitsInputFilter(3, 0, 287.0d)});
            binding.txtWeight.setText(getData(weight));
        } else {
            binding.txtWeight.setText(isKg ? "80.0" : "176");
        }
    }

    private void saveHeightData() {
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT, binding.txtHeight.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT_UNIT, binding.rdoCm.isChecked());
        preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
    }

    private void saveWeightData() {
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT, binding.txtWeight.getText().toString().trim());
        preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT_UNIT, binding.rdoKg.isChecked());
        preferencesHelper.savePreferences(URLFactory.KEY_WATER_UNIT, binding.rdoKg.isChecked() ? "ML" : "FL OZ");
        preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, false);
    }

    public String getData(String str) {
        return str.replace(",", ".");
    }
}
