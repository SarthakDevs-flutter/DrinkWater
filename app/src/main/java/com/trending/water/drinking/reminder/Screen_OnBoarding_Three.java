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

import androidx.appcompat.widget.AppCompatEditText;

import com.github.mikephil.charting.utils.Utils;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.custom.DigitsInputFilter;
import com.trending.water.drinking.reminder.custom.InputFilterRange;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;
import com.trending.water.drinking.reminder.lib.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

public class Screen_OnBoarding_Three extends MasterBaseFragment {
    List<String> height_cm_lst = new ArrayList();
    List<Double> height_feet_elements = new ArrayList();
    List<String> height_feet_lst = new ArrayList();
    boolean isExecute = true;
    boolean isExecuteSeekbar = true;
    boolean isExecuteSeekbarWeight = true;
    boolean isExecuteWeight = true;
    View item_view;
    HorizontalPicker pickerCM;
    HorizontalPicker pickerFeet;
    HorizontalPicker pickerKG;
    HorizontalPicker pickerLB;
    RadioButton rdo_cm;
    RadioButton rdo_feet;
    RadioButton rdo_kg;
    RadioButton rdo_lb;
    AppCompatEditText txt_height;
    AppCompatEditText txt_weight;
    List<String> weight_kg_lst = new ArrayList();
    List<String> weight_lb_lst = new ArrayList();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_onboarding_three, container, false);
        FindViewById();
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            init_WeightLB();
            init_WeightKG();
            this.pickerLB.setVisibility(View.GONE);
        } else {
            init_WeightKG();
            init_WeightLB();
            this.pickerKG.setVisibility(View.GONE);
        }
        if (this.ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT)) {
            init_HeightFeet();
            init_HeightCM();
            this.pickerFeet.setVisibility(View.GONE);
        } else {
            init_HeightCM();
            init_HeightFeet();
            this.pickerCM.setVisibility(View.GONE);
        }
        Body();
        this.rdo_kg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int i = 8;
                Screen_OnBoarding_Three.this.pickerKG.setVisibility(b ? View.VISIBLE : android.view.View.GONE);
                HorizontalPicker horizontalPicker = Screen_OnBoarding_Three.this.pickerLB;
                if (!b) {
                    i = 0;
                }
                horizontalPicker.setVisibility(i);
            }
        });
        this.rdo_feet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int i = 8;
                Screen_OnBoarding_Three.this.pickerFeet.setVisibility(b ? View.VISIBLE : android.view.View.GONE);
                HorizontalPicker horizontalPicker = Screen_OnBoarding_Three.this.pickerCM;
                if (!b) {
                    i = 0;
                }
                horizontalPicker.setVisibility(i);
            }
        });
        return this.item_view;
    }

    public void init_WeightKG() {
        this.pickerKG = (HorizontalPicker) this.item_view.findViewById(R.id.pickerKG);
        this.weight_kg_lst.clear();
        List<String> list = this.weight_kg_lst;
        list.add("" + 30.0f);
        float f = 30.0f;
        for (int k = 0; k < 200; k++) {
            f = (float) (((double) f) + 0.5d);
            List<String> list2 = this.weight_kg_lst;
            list2.add("" + f);
        }
        final CharSequence[] st = new CharSequence[this.weight_kg_lst.size()];
        for (int k2 = 0; k2 < this.weight_kg_lst.size(); k2++) {
            st[k2] = "" + this.weight_kg_lst.get(k2);
        }
        this.pickerKG.setValues(st);
        this.pickerKG.setSideItems(1);
        this.pickerKG.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            public void onItemSelected(int index) {
                Screen_OnBoarding_Three.this.txt_weight.setText(st[index]);
                Log.d("MYHSCROLL : ", "onItemSelected KG");
            }
        });
        try {
            this.pickerKG.setTextSize(this.act.getResources().getDimension(R.dimen.dp_30));
        } catch (Exception e) {
        }
    }

    public void init_WeightLB() {
        this.pickerLB = (HorizontalPicker) this.item_view.findViewById(R.id.pickerLB);
        this.weight_lb_lst.clear();
        for (int k = 66; k < 288; k++) {
            List<String> list = this.weight_lb_lst;
            list.add("" + k);
        }
        final CharSequence[] st = new CharSequence[this.weight_lb_lst.size()];
        for (int k2 = 0; k2 < this.weight_lb_lst.size(); k2++) {
            st[k2] = "" + this.weight_lb_lst.get(k2);
        }
        this.pickerLB.setValues(st);
        this.pickerLB.setSideItems(1);
        this.pickerLB.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            public void onItemSelected(int index) {
                Screen_OnBoarding_Three.this.txt_weight.setText(st[index]);
                Log.d("MYHSCROLL : ", "onItemSelected LB");
            }
        });
        try {
            this.pickerLB.setTextSize(this.act.getResources().getDimension(R.dimen.dp_30));
        } catch (Exception e) {
        }
    }

    public void init_HeightCM() {
        this.pickerCM = (HorizontalPicker) this.item_view.findViewById(R.id.pickerCM);
        this.height_cm_lst.clear();
        for (int k = 60; k < 241; k++) {
            List<String> list = this.height_cm_lst;
            list.add("" + k);
        }
        final CharSequence[] st = new CharSequence[this.height_cm_lst.size()];
        for (int k2 = 0; k2 < this.height_cm_lst.size(); k2++) {
            st[k2] = "" + this.height_cm_lst.get(k2);
        }
        this.pickerCM.setValues(st);
        this.pickerCM.setSideItems(1);
        this.pickerCM.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            public void onItemSelected(int index) {
                Screen_OnBoarding_Three.this.txt_height.setText(st[index]);
                Log.d("MYHSCROLL : ", "onItemSelected 2");
            }
        });
        try {
            this.pickerCM.setTextSize(this.act.getResources().getDimension(R.dimen.dp_30));
        } catch (Exception e) {
        }
    }

    public void init_HeightFeet() {
        this.pickerFeet = (HorizontalPicker) this.item_view.findViewById(R.id.pickerFeet);
        this.height_feet_lst.clear();
        this.height_feet_lst.add("2.0");
        this.height_feet_lst.add("2.1");
        this.height_feet_lst.add("2.2");
        this.height_feet_lst.add("2.3");
        this.height_feet_lst.add("2.4");
        this.height_feet_lst.add("2.5");
        this.height_feet_lst.add("2.6");
        this.height_feet_lst.add("2.7");
        this.height_feet_lst.add("2.8");
        this.height_feet_lst.add("2.9");
        this.height_feet_lst.add("2.10");
        this.height_feet_lst.add("2.11");
        this.height_feet_lst.add("3.0");
        this.height_feet_lst.add("3.1");
        this.height_feet_lst.add("3.2");
        this.height_feet_lst.add("3.3");
        this.height_feet_lst.add("3.4");
        this.height_feet_lst.add("3.5");
        this.height_feet_lst.add("3.6");
        this.height_feet_lst.add("3.7");
        this.height_feet_lst.add("3.8");
        this.height_feet_lst.add("3.9");
        this.height_feet_lst.add("3.10");
        this.height_feet_lst.add("3.11");
        this.height_feet_lst.add("4.0");
        this.height_feet_lst.add("4.1");
        this.height_feet_lst.add("4.2");
        this.height_feet_lst.add("4.3");
        this.height_feet_lst.add("4.4");
        this.height_feet_lst.add("4.5");
        this.height_feet_lst.add("4.6");
        this.height_feet_lst.add("4.7");
        this.height_feet_lst.add("4.8");
        this.height_feet_lst.add("4.9");
        this.height_feet_lst.add("4.10");
        this.height_feet_lst.add("4.11");
        this.height_feet_lst.add("5.0");
        this.height_feet_lst.add("5.1");
        this.height_feet_lst.add("5.2");
        this.height_feet_lst.add("5.3");
        this.height_feet_lst.add("5.4");
        this.height_feet_lst.add("5.5");
        this.height_feet_lst.add("5.6");
        this.height_feet_lst.add("5.7");
        this.height_feet_lst.add("5.8");
        this.height_feet_lst.add("5.9");
        this.height_feet_lst.add("5.10");
        this.height_feet_lst.add("5.11");
        this.height_feet_lst.add("6.0");
        this.height_feet_lst.add("6.1");
        this.height_feet_lst.add("6.2");
        this.height_feet_lst.add("6.3");
        this.height_feet_lst.add("6.4");
        this.height_feet_lst.add("6.5");
        this.height_feet_lst.add("6.6");
        this.height_feet_lst.add("6.7");
        this.height_feet_lst.add("6.8");
        this.height_feet_lst.add("6.9");
        this.height_feet_lst.add("6.10");
        this.height_feet_lst.add("6.11");
        this.height_feet_lst.add("7.0");
        this.height_feet_lst.add("7.1");
        this.height_feet_lst.add("7.2");
        this.height_feet_lst.add("7.3");
        this.height_feet_lst.add("7.4");
        this.height_feet_lst.add("7.5");
        this.height_feet_lst.add("7.6");
        this.height_feet_lst.add("7.7");
        this.height_feet_lst.add("7.8");
        this.height_feet_lst.add("7.9");
        this.height_feet_lst.add("7.10");
        this.height_feet_lst.add("7.11");
        this.height_feet_lst.add("8.0");
        final CharSequence[] st = new CharSequence[this.height_feet_lst.size()];
        for (int k = 0; k < this.height_feet_lst.size(); k++) {
            st[k] = "" + this.height_feet_lst.get(k);
        }
        this.pickerFeet.setValues(st);
        this.pickerFeet.setSideItems(1);
        this.pickerFeet.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            public void onItemSelected(int index) {
                Screen_OnBoarding_Three.this.txt_height.setText(st[index]);
                Log.d("MYHSCROLL : ", "onItemSelected");
            }
        });
        try {
            this.pickerFeet.setTextSize(this.act.getResources().getDimension(R.dimen.dp_30));
        } catch (Exception e) {
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void FindViewById() {
        this.txt_height = (AppCompatEditText) this.item_view.findViewById(R.id.txt_height);
        this.txt_weight = (AppCompatEditText) this.item_view.findViewById(R.id.txt_weight);
        this.rdo_cm = (RadioButton) this.item_view.findViewById(R.id.rdo_cm);
        this.rdo_feet = (RadioButton) this.item_view.findViewById(R.id.rdo_feet);
        this.rdo_kg = (RadioButton) this.item_view.findViewById(R.id.rdo_kg);
        this.rdo_lb = (RadioButton) this.item_view.findViewById(R.id.rdo_lb);
    }

    private void Body() {
        this.height_feet_elements.add(Double.valueOf(2.0d));
        this.height_feet_elements.add(Double.valueOf(2.1d));
        this.height_feet_elements.add(Double.valueOf(2.2d));
        this.height_feet_elements.add(Double.valueOf(2.3d));
        this.height_feet_elements.add(Double.valueOf(2.4d));
        this.height_feet_elements.add(Double.valueOf(2.5d));
        this.height_feet_elements.add(Double.valueOf(2.6d));
        this.height_feet_elements.add(Double.valueOf(2.7d));
        this.height_feet_elements.add(Double.valueOf(2.8d));
        this.height_feet_elements.add(Double.valueOf(2.9d));
        this.height_feet_elements.add(Double.valueOf(2.1d));
        this.height_feet_elements.add(Double.valueOf(2.11d));
        this.height_feet_elements.add(Double.valueOf(3.0d));
        this.height_feet_elements.add(Double.valueOf(3.1d));
        this.height_feet_elements.add(Double.valueOf(3.2d));
        this.height_feet_elements.add(Double.valueOf(3.3d));
        this.height_feet_elements.add(Double.valueOf(3.4d));
        this.height_feet_elements.add(Double.valueOf(3.5d));
        this.height_feet_elements.add(Double.valueOf(3.6d));
        this.height_feet_elements.add(Double.valueOf(3.7d));
        this.height_feet_elements.add(Double.valueOf(3.8d));
        this.height_feet_elements.add(Double.valueOf(3.9d));
        this.height_feet_elements.add(Double.valueOf(3.1d));
        this.height_feet_elements.add(Double.valueOf(3.11d));
        this.height_feet_elements.add(Double.valueOf(4.0d));
        this.height_feet_elements.add(Double.valueOf(4.1d));
        this.height_feet_elements.add(Double.valueOf(4.2d));
        this.height_feet_elements.add(Double.valueOf(4.3d));
        this.height_feet_elements.add(Double.valueOf(4.4d));
        this.height_feet_elements.add(Double.valueOf(4.5d));
        this.height_feet_elements.add(Double.valueOf(4.6d));
        this.height_feet_elements.add(Double.valueOf(4.7d));
        this.height_feet_elements.add(Double.valueOf(4.8d));
        this.height_feet_elements.add(Double.valueOf(4.9d));
        this.height_feet_elements.add(Double.valueOf(4.1d));
        this.height_feet_elements.add(Double.valueOf(4.11d));
        this.height_feet_elements.add(Double.valueOf(5.0d));
        this.height_feet_elements.add(Double.valueOf(5.1d));
        this.height_feet_elements.add(Double.valueOf(5.2d));
        this.height_feet_elements.add(Double.valueOf(5.3d));
        this.height_feet_elements.add(Double.valueOf(5.4d));
        this.height_feet_elements.add(Double.valueOf(5.5d));
        this.height_feet_elements.add(Double.valueOf(5.6d));
        this.height_feet_elements.add(Double.valueOf(5.7d));
        this.height_feet_elements.add(Double.valueOf(5.8d));
        this.height_feet_elements.add(Double.valueOf(5.9d));
        this.height_feet_elements.add(Double.valueOf(5.1d));
        this.height_feet_elements.add(Double.valueOf(5.11d));
        this.height_feet_elements.add(Double.valueOf(6.0d));
        this.height_feet_elements.add(Double.valueOf(6.1d));
        this.height_feet_elements.add(Double.valueOf(6.2d));
        this.height_feet_elements.add(Double.valueOf(6.3d));
        this.height_feet_elements.add(Double.valueOf(6.4d));
        this.height_feet_elements.add(Double.valueOf(6.5d));
        this.height_feet_elements.add(Double.valueOf(6.6d));
        this.height_feet_elements.add(Double.valueOf(6.7d));
        this.height_feet_elements.add(Double.valueOf(6.8d));
        this.height_feet_elements.add(Double.valueOf(6.9d));
        this.height_feet_elements.add(Double.valueOf(6.1d));
        this.height_feet_elements.add(Double.valueOf(6.11d));
        this.height_feet_elements.add(Double.valueOf(7.0d));
        this.height_feet_elements.add(Double.valueOf(7.1d));
        this.height_feet_elements.add(Double.valueOf(7.2d));
        this.height_feet_elements.add(Double.valueOf(7.3d));
        this.height_feet_elements.add(Double.valueOf(7.4d));
        this.height_feet_elements.add(Double.valueOf(7.5d));
        this.height_feet_elements.add(Double.valueOf(7.6d));
        this.height_feet_elements.add(Double.valueOf(7.7d));
        this.height_feet_elements.add(Double.valueOf(7.8d));
        this.height_feet_elements.add(Double.valueOf(7.9d));
        this.height_feet_elements.add(Double.valueOf(7.1d));
        this.height_feet_elements.add(Double.valueOf(7.11d));
        this.height_feet_elements.add(Double.valueOf(8.0d));
        this.isExecute = false;
        this.isExecuteSeekbar = false;
        this.isExecuteWeight = false;
        this.isExecuteSeekbarWeight = false;
        this.txt_height.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, this.height_feet_elements)});
        this.txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 130.0d)});
        this.isExecute = true;
        this.isExecuteSeekbar = true;
        this.isExecuteWeight = true;
        this.isExecuteSeekbarWeight = true;
        this.txt_height.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Screen_OnBoarding_Three.this.isExecuteSeekbar = false;
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                if (Screen_OnBoarding_Three.this.txt_height.getText().toString().trim().equalsIgnoreCase(".")) {
                    Screen_OnBoarding_Three.this.txt_height.setText("2.0");
                }
                String height = Screen_OnBoarding_Three.this.txt_height.getText().toString().trim();
                if (!Screen_OnBoarding_Three.this.sh.check_blank_data(height) && Screen_OnBoarding_Three.this.isExecute) {
                    Log.d("MYHSCROLL : ", "afterTextChanged");
                    float h = Float.parseFloat(height);
                    int k = 0;
                    if (!Screen_OnBoarding_Three.this.rdo_feet.isChecked()) {
                        while (true) {
                            int k2 = k;
                            if (k2 >= Screen_OnBoarding_Three.this.height_cm_lst.size()) {
                                break;
                            }
                            Log.d("height_cm_lst", k2 + "  " + h + " " + Float.parseFloat(Screen_OnBoarding_Three.this.height_cm_lst.get(k2)));
                            if (h == Float.parseFloat(Screen_OnBoarding_Three.this.height_cm_lst.get(k2))) {
                                Screen_OnBoarding_Three.this.pickerCM.setSelectedItem(k2);
                                break;
                            }
                            k = k2 + 1;
                        }
                    } else {
                        while (true) {
                            int k3 = k;
                            if (k3 >= Screen_OnBoarding_Three.this.height_feet_lst.size()) {
                                break;
                            }
                            Log.d("height_feet_lst", k3 + "  " + h + " " + Float.parseFloat(Screen_OnBoarding_Three.this.height_feet_lst.get(k3)));
                            if (height.equalsIgnoreCase(Screen_OnBoarding_Three.this.height_feet_lst.get(k3))) {
                                Screen_OnBoarding_Three.this.pickerFeet.setSelectedItem(k3);
                                break;
                            }
                            k = k3 + 1;
                        }
                    }
                    Screen_OnBoarding_Three.this.saveData();
                }
                Screen_OnBoarding_Three.this.isExecuteSeekbar = true;
            }
        });
        this.rdo_cm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Screen_OnBoarding_Three.this.sh.check_blank_data(Screen_OnBoarding_Three.this.txt_height.getText().toString())) {
                    int final_height_cm = 61;
                    try {
                        String tmp_height = Screen_OnBoarding_Three.this.getData(Screen_OnBoarding_Three.this.txt_height.getText().toString().trim());
                        int d = (int) Float.parseFloat(Screen_OnBoarding_Three.this.txt_height.getText().toString().trim());
                        Log.d("after_decimal", "" + tmp_height.indexOf("."));
                        if (tmp_height.indexOf(".") > 0) {
                            String after_decimal = tmp_height.substring(tmp_height.indexOf(".") + 1);
                            if (!Screen_OnBoarding_Three.this.sh.check_blank_data(after_decimal)) {
                                final_height_cm = (int) Math.round(2.54d * ((double) ((d * 12) + Integer.parseInt(after_decimal))));
                            } else {
                                final_height_cm = (int) Math.round(((double) (d * 12)) * 2.54d);
                            }
                        } else {
                            final_height_cm = (int) Math.round(((double) (d * 12)) * 2.54d);
                        }
                    } catch (Exception e) {
                    }
                    int k = 0;
                    while (true) {
                        if (k >= Screen_OnBoarding_Three.this.height_cm_lst.size()) {
                            break;
                        } else if (Integer.parseInt(Screen_OnBoarding_Three.this.height_cm_lst.get(k)) == final_height_cm) {
                            Screen_OnBoarding_Three.this.pickerCM.setSelectedItem(k);
                            break;
                        } else {
                            k++;
                        }
                    }
                    Screen_OnBoarding_Three.this.rdo_feet.setClickable(true);
                    Screen_OnBoarding_Three.this.rdo_cm.setClickable(false);
                    Screen_OnBoarding_Three.this.txt_height.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
                    AppCompatEditText appCompatEditText = Screen_OnBoarding_Three.this.txt_height;
                    Screen_OnBoarding_Three screen_OnBoarding_Three = Screen_OnBoarding_Three.this;
                    appCompatEditText.setText(screen_OnBoarding_Three.getData("" + final_height_cm));
                    Screen_OnBoarding_Three.this.txt_height.setSelection(Screen_OnBoarding_Three.this.txt_height.getText().toString().trim().length());
                    Screen_OnBoarding_Three.this.saveData();
                    return;
                }
                Screen_OnBoarding_Three.this.rdo_feet.setChecked(true);
                Screen_OnBoarding_Three.this.rdo_cm.setChecked(false);
            }
        });
        this.rdo_feet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Screen_OnBoarding_Three.this.sh.check_blank_data(Screen_OnBoarding_Three.this.txt_height.getText().toString())) {
                    String final_height_feet = "5.0";
                    try {
                        int tmp_height_inch = (int) Math.round(((double) ((int) Float.parseFloat(Screen_OnBoarding_Three.this.txt_height.getText().toString().trim()))) / 2.54d);
                        final_height_feet = (tmp_height_inch / 12) + "." + (tmp_height_inch % 12);
                    } catch (Exception e) {
                    }
                    int k = 0;
                    while (true) {
                        if (k >= Screen_OnBoarding_Three.this.height_feet_lst.size()) {
                            break;
                        } else if (Screen_OnBoarding_Three.this.getData(final_height_feet).equalsIgnoreCase(Screen_OnBoarding_Three.this.height_feet_lst.get(k))) {
                            Screen_OnBoarding_Three.this.pickerFeet.setSelectedItem(k);
                            break;
                        } else {
                            k++;
                        }
                    }
                    Screen_OnBoarding_Three.this.rdo_feet.setClickable(false);
                    Screen_OnBoarding_Three.this.rdo_cm.setClickable(true);
                    Screen_OnBoarding_Three.this.txt_height.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, Screen_OnBoarding_Three.this.height_feet_elements)});
                    Screen_OnBoarding_Three.this.txt_height.setText(Screen_OnBoarding_Three.this.getData(final_height_feet));
                    Screen_OnBoarding_Three.this.txt_height.setSelection(Screen_OnBoarding_Three.this.txt_height.getText().toString().trim().length());
                    Screen_OnBoarding_Three.this.saveData();
                    return;
                }
                Screen_OnBoarding_Three.this.rdo_feet.setChecked(false);
                Screen_OnBoarding_Three.this.rdo_cm.setChecked(true);
            }
        });
        this.txt_weight.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Screen_OnBoarding_Three.this.isExecuteSeekbarWeight = false;
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    if (Screen_OnBoarding_Three.this.txt_weight.getText().toString().trim().equalsIgnoreCase(".")) {
                        Screen_OnBoarding_Three.this.txt_weight.setText("30.0");
                    }
                    String weight = Screen_OnBoarding_Three.this.txt_weight.getText().toString().trim();
                    if (!Screen_OnBoarding_Three.this.sh.check_blank_data(weight) && Screen_OnBoarding_Three.this.isExecuteWeight) {
                        Log.d("MYHSCROLL : ", "afterTextChanged W");
                        float h = Float.parseFloat(weight);
                        int tot = (int) h;
                        int k = 0;
                        if (!Screen_OnBoarding_Three.this.rdo_kg.isChecked()) {
                            while (true) {
                                int k2 = k;
                                if (k2 >= Screen_OnBoarding_Three.this.weight_lb_lst.size()) {
                                    break;
                                }
                                if (Integer.parseInt(Screen_OnBoarding_Three.this.weight_lb_lst.get(k2)) == tot) {
                                    Screen_OnBoarding_Three.this.pickerLB.setSelectedItem(k2);
                                }
                                k = k2 + 1;
                            }
                        } else {
                            int pos = 0;
                            while (true) {
                                if (k >= Screen_OnBoarding_Three.this.weight_kg_lst.size()) {
                                    break;
                                } else if (h == Float.parseFloat(Screen_OnBoarding_Three.this.weight_kg_lst.get(k))) {
                                    pos = k;
                                    break;
                                } else {
                                    k++;
                                }
                            }
                            Screen_OnBoarding_Three.this.pickerKG.setSelectedItem(pos);
                        }
                    }
                    Screen_OnBoarding_Three.this.saveWeightData();
                } catch (Exception e) {
                }
                Screen_OnBoarding_Three.this.isExecuteSeekbarWeight = true;
            }
        });
        this.rdo_kg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Screen_OnBoarding_Three.this.sh.check_blank_data(Screen_OnBoarding_Three.this.txt_weight.getText().toString())) {
                    double weight_in_lb = Double.parseDouble(Screen_OnBoarding_Three.this.txt_weight.getText().toString());
                    double weight_in_kg = Utils.DOUBLE_EPSILON;
                    if (weight_in_lb > Utils.DOUBLE_EPSILON) {
                        weight_in_kg = (double) Math.round(HeightWeightHelper.lbToKgConverter(weight_in_lb));
                    }
                    int tmp = (int) weight_in_kg;
                    int sel_pos = 0;
                    for (int k = 0; k < Screen_OnBoarding_Three.this.weight_kg_lst.size(); k++) {
                        if (Float.parseFloat(Screen_OnBoarding_Three.this.weight_kg_lst.get(k)) == Float.parseFloat("" + tmp)) {
                            sel_pos = k;
                        }
                    }
                    Screen_OnBoarding_Three.this.pickerKG.setSelectedItem(sel_pos);
                    Screen_OnBoarding_Three.this.txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 130.0d)});
                    Screen_OnBoarding_Three.this.txt_weight.setText(Screen_OnBoarding_Three.this.getData("" + URLFactory.decimalFormat2.format((long) tmp)));
                    Screen_OnBoarding_Three.this.rdo_kg.setClickable(false);
                    Screen_OnBoarding_Three.this.rdo_lb.setClickable(true);
                }
                Screen_OnBoarding_Three.this.saveWeightData();
            }
        });
        this.rdo_lb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Screen_OnBoarding_Three.this.sh.check_blank_data(Screen_OnBoarding_Three.this.txt_weight.getText().toString())) {
                    double weight_in_kg = Double.parseDouble(Screen_OnBoarding_Three.this.txt_weight.getText().toString());
                    double weight_in_lb = Utils.DOUBLE_EPSILON;
                    if (weight_in_kg > Utils.DOUBLE_EPSILON) {
                        weight_in_lb = (double) Math.round(HeightWeightHelper.kgToLbConverter(weight_in_kg));
                    }
                    int tmp = (int) weight_in_lb;
                    int sel_pos = 0;
                    for (int k = 0; k < Screen_OnBoarding_Three.this.weight_lb_lst.size(); k++) {
                        if (Float.parseFloat(Screen_OnBoarding_Three.this.weight_lb_lst.get(k)) == Float.parseFloat("" + tmp)) {
                            sel_pos = k;
                        }
                    }
                    Screen_OnBoarding_Three.this.pickerLB.setSelectedItem(sel_pos);
                    Screen_OnBoarding_Three.this.txt_weight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
                    Screen_OnBoarding_Three.this.txt_weight.setText(Screen_OnBoarding_Three.this.getData("" + tmp));
                    Screen_OnBoarding_Three.this.rdo_kg.setClickable(true);
                    Screen_OnBoarding_Three.this.rdo_lb.setClickable(false);
                }
                Screen_OnBoarding_Three.this.saveWeightData();
            }
        });
        if (this.ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT)) {
            this.rdo_cm.setChecked(true);
            this.rdo_cm.setClickable(false);
            this.rdo_feet.setClickable(true);
        } else {
            this.rdo_feet.setChecked(true);
            this.rdo_cm.setClickable(true);
            this.rdo_feet.setClickable(false);
        }
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            this.rdo_kg.setChecked(true);
            this.rdo_kg.setClickable(false);
            this.rdo_lb.setClickable(true);
        } else {
            this.rdo_lb.setChecked(true);
            this.rdo_kg.setClickable(true);
            this.rdo_lb.setClickable(false);
        }
        if (!this.sh.check_blank_data(this.ph.getString(URLFactory.PERSON_HEIGHT))) {
            if (this.rdo_cm.isChecked()) {
                this.txt_height.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
                this.txt_height.setText(getData(this.ph.getString(URLFactory.PERSON_HEIGHT)));
            } else {
                this.txt_height.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, this.height_feet_elements)});
                this.txt_height.setText(getData(this.ph.getString(URLFactory.PERSON_HEIGHT)));
            }
        } else if (this.rdo_cm.isChecked()) {
            this.txt_height.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
            this.txt_height.setText("150");
        } else {
            this.txt_height.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, this.height_feet_elements)});
            this.txt_height.setText("5.0");
        }
        if (!this.sh.check_blank_data(this.ph.getString(URLFactory.PERSON_WEIGHT))) {
            if (this.rdo_kg.isChecked()) {
                this.txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 130.0d)});
                this.txt_weight.setText(getData(this.ph.getString(URLFactory.PERSON_WEIGHT)));
                return;
            }
            this.txt_weight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
            this.txt_weight.setText(getData(this.ph.getString(URLFactory.PERSON_WEIGHT)));
        } else if (this.rdo_kg.isChecked()) {
            this.txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 130.0d)});
            this.txt_weight.setText("80.0");
        } else {
            this.txt_weight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
            this.txt_weight.setText("176");
        }
    }

    public void saveData() {
        Log.d("saveData", "" + this.txt_height.getText().toString().trim() + " @@@ " + this.txt_weight.getText().toString().trim());
        Preferences_Helper preferences_Helper = this.ph;
        String str = URLFactory.PERSON_HEIGHT;
        preferences_Helper.savePreferences(str, "" + this.txt_height.getText().toString().trim());
        this.ph.savePreferences(URLFactory.PERSON_HEIGHT_UNIT, this.rdo_cm.isChecked());
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
    }

    public void saveWeightData() {
        Log.d("saveWeightData", "" + this.rdo_kg.isChecked() + " @@@ " + this.txt_weight.getText().toString().trim());
        Preferences_Helper preferences_Helper = this.ph;
        String str = URLFactory.PERSON_WEIGHT;
        preferences_Helper.savePreferences(str, "" + this.txt_weight.getText().toString().trim());
        this.ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT, this.rdo_kg.isChecked());
        this.ph.savePreferences(URLFactory.WATER_UNIT, this.rdo_kg.isChecked() ? "ml" : "fl oz");
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
    }

    public String getData(String str) {
        return str.replace(",", ".");
    }
}
