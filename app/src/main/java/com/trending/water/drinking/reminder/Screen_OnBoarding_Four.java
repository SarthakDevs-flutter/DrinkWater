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

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.github.mikephil.charting.utils.Utils;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

public class Screen_OnBoarding_Four extends MasterBaseFragment {
    boolean isExecute = true;
    boolean isExecuteSeekbar = true;
    View item_view;
    AppCompatTextView lbl_goal;
    AppCompatTextView lbl_set_goal_manually;
    AppCompatTextView lbl_unit;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_onboarding_four, container, false);
        FindViewById();
        Body();
        return this.item_view;
    }

    public String getData(String str) {
        return str.replace(",", ".");
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            return;
        }
        if (this.ph.getBoolean(URLFactory.SET_MANUALLY_GOAL)) {
            URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE);
            this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
            AppCompatTextView appCompatTextView = this.lbl_goal;
            appCompatTextView.setText(getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
            if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                this.lbl_unit.setText("ml");
            } else {
                this.lbl_unit.setText("fl oz");
            }
        } else {
            calculate_goal();
        }
    }

    private void FindViewById() {
        this.lbl_set_goal_manually = (AppCompatTextView) this.item_view.findViewById(R.id.lbl_set_goal_manually);
        this.lbl_goal = (AppCompatTextView) this.item_view.findViewById(R.id.lbl_goal);
        this.lbl_unit = (AppCompatTextView) this.item_view.findViewById(R.id.lbl_unit);
    }

    private void Body() {
        if (this.ph.getBoolean(URLFactory.SET_MANUALLY_GOAL)) {
            URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE);
            this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
            AppCompatTextView appCompatTextView = this.lbl_goal;
            appCompatTextView.setText(getData("" + URLFactory.DAILY_WATER_VALUE));
            if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                this.lbl_unit.setText("ml");
            } else {
                this.lbl_unit.setText("fl oz");
            }
        } else {
            calculate_goal();
        }
        this.lbl_set_goal_manually.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_OnBoarding_Four.this.showSetManuallyGoalDialog();
            }
        });
    }

    public void calculate_goal() {
        double tmp_kg;
        double tot_drink;
        double tot_drink2;
        String tmp_weight = "" + this.ph.getString(URLFactory.PERSON_WEIGHT);
        boolean isFemale = this.ph.getBoolean(URLFactory.USER_GENDER);
        boolean isActive = this.ph.getBoolean(URLFactory.IS_ACTIVE);
        boolean isPregnant = this.ph.getBoolean(URLFactory.IS_PREGNANT);
        boolean isBreastfeeding = this.ph.getBoolean(URLFactory.IS_BREATFEEDING);
        int weatherIdx = this.ph.getInt(URLFactory.WEATHER_CONSITIONS);
        if (!this.sh.check_blank_data(tmp_weight)) {
            if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                tmp_kg = Double.parseDouble("" + tmp_weight);
            } else {
                tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
            }
            Log.d("tot_drink 1 ", "" + Utils.DOUBLE_EPSILON);
            if (isFemale) {
                tot_drink = (isActive ? URLFactory.ACTIVE_FEMALE_WATER : URLFactory.FEMALE_WATER) * tmp_kg;
            } else {
                tot_drink = (isActive ? URLFactory.ACTIVE_MALE_WATER : URLFactory.MALE_WATER) * tmp_kg;
            }
            Log.d("tot_drink 2 ", "" + tot_drink);
            if (weatherIdx == 1) {
                tot_drink2 = tot_drink * URLFactory.WEATHER_CLOUDY;
            } else if (weatherIdx == 2) {
                tot_drink2 = tot_drink * URLFactory.WEATHER_RAINY;
            } else if (weatherIdx == 3) {
                tot_drink2 = tot_drink * URLFactory.WEATHER_SNOW;
            } else {
                tot_drink2 = tot_drink * URLFactory.WEATHER_SUNNY;
            }
            Log.d("tot_drink 3 ", "" + tot_drink2);
            if (isPregnant && isFemale) {
                tot_drink2 += URLFactory.PREGNANT_WATER;
            }
            Log.d("tot_drink 4 ", "" + tot_drink2);
            if (isBreastfeeding && isFemale) {
                tot_drink2 += URLFactory.BREASTFEEDING_WATER;
            }
            Log.d("tot_drink 5 ", "" + tot_drink2);
            if (tot_drink2 < 900.0d) {
                tot_drink2 = 900.0d;
            }
            if (tot_drink2 > 8000.0d) {
                tot_drink2 = 8000.0d;
            }
            Log.d("tot_drink 6 ", "" + tot_drink2);
            double tot_drink_fl_oz = HeightWeightHelper.mlToOzConverter(tot_drink2);
            if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                this.lbl_unit.setText("ml");
                URLFactory.DAILY_WATER_VALUE = (float) tot_drink2;
            } else {
                this.lbl_unit.setText("fl oz");
                URLFactory.DAILY_WATER_VALUE = (float) tot_drink_fl_oz;
            }
            URLFactory.DAILY_WATER_VALUE = (float) Math.round(URLFactory.DAILY_WATER_VALUE);
            this.lbl_goal.setText(getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
            this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
        }
    }

    public void calculate_goal_old() {
        double tmp_lbs;
        String tmp_weight = "" + this.ph.getString(URLFactory.PERSON_WEIGHT);
        if (!this.sh.check_blank_data(tmp_weight)) {
            if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                tmp_lbs = HeightWeightHelper.kgToLbConverter(Double.parseDouble(tmp_weight));
            } else {
                tmp_lbs = Double.parseDouble("" + tmp_weight);
            }
            double tmp_oz = 0.5d * tmp_lbs;
            double tmp_ml = HeightWeightHelper.ozToMlConverter(tmp_oz);
            if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                this.lbl_unit.setText("ml");
                URLFactory.DAILY_WATER_VALUE = (float) tmp_ml;
            } else {
                this.lbl_unit.setText("fl oz");
                URLFactory.DAILY_WATER_VALUE = (float) tmp_oz;
            }
            URLFactory.DAILY_WATER_VALUE = (float) Math.round(URLFactory.DAILY_WATER_VALUE);
            this.lbl_goal.setText(getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
            this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
        }
    }

    public void showSetManuallyGoalDialog() {
        Preferences_Helper preferences_Helper;
        String str;
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_set_manually_goal, (ViewGroup) null, false);
        final AppCompatEditText lbl_goal2 = (AppCompatEditText) view.findViewById(R.id.lbl_goal);
        AppCompatTextView lbl_unit2 = (AppCompatTextView) view.findViewById(R.id.lbl_unit);
        RelativeLayout btn_cancel = (RelativeLayout) view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_save = (RelativeLayout) view.findViewById(R.id.btn_save);
        final SeekBar seekbarGoal = (SeekBar) view.findViewById(R.id.seekbarGoal);
        if (this.ph.getBoolean(URLFactory.SET_MANUALLY_GOAL)) {
            lbl_goal2.setText(getData("" + ((int) this.ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE))));
        } else {
            lbl_goal2.setText(getData("" + ((int) this.ph.getFloat(URLFactory.DAILY_WATER))));
        }
        lbl_unit2.setText(this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz");
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            if (Build.VERSION.SDK_INT >= 26) {
                seekbarGoal.setMin(900);
            }
            seekbarGoal.setMax(8000);
            lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 8000.0d), new InputFilter.LengthFilter(4)});
        } else {
            if (Build.VERSION.SDK_INT >= 26) {
                seekbarGoal.setMin(30);
            }
            seekbarGoal.setMax(270);
            lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 270.0d), new InputFilter.LengthFilter(3)});
        }
        if (this.ph.getBoolean(URLFactory.SET_MANUALLY_GOAL)) {
            preferences_Helper = this.ph;
            str = URLFactory.SET_MANUALLY_GOAL_VALUE;
        } else {
            preferences_Helper = this.ph;
            str = URLFactory.DAILY_WATER;
        }
        seekbarGoal.setProgress((int) preferences_Helper.getFloat(str));
        lbl_goal2.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Screen_OnBoarding_Four.this.isExecuteSeekbar = false;
            }

            public void afterTextChanged(Editable editable) {
                try {
                    if (!Screen_OnBoarding_Four.this.sh.check_blank_data(lbl_goal2.getText().toString().trim()) && Screen_OnBoarding_Four.this.isExecute) {
                        seekbarGoal.setProgress(Integer.parseInt(lbl_goal2.getText().toString().trim()));
                    }
                } catch (Exception e) {
                }
                Screen_OnBoarding_Four.this.isExecuteSeekbar = true;
            }
        });
        seekbarGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBars, int progress, boolean fromUser) {
                if (Screen_OnBoarding_Four.this.isExecuteSeekbar) {
                    if (Build.VERSION.SDK_INT < 26) {
                        if (Screen_OnBoarding_Four.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                            int i = 900;
                            if (progress >= 900) {
                                i = progress;
                            }
                            progress = i;
                        } else {
                            int i2 = 30;
                            if (progress >= 30) {
                                i2 = progress;
                            }
                            progress = i2;
                        }
                        seekbarGoal.setProgress(progress);
                    }
                    AppCompatEditText appCompatEditText = lbl_goal2;
                    appCompatEditText.setText("" + progress);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                Screen_OnBoarding_Four.this.isExecute = false;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Screen_OnBoarding_Four.this.isExecute = true;
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_OnBoarding_Four.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && Float.parseFloat(lbl_goal2.getText().toString().trim()) >= 900.0f) {
                    URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
                    Screen_OnBoarding_Four.this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
                    AppCompatTextView appCompatTextView = Screen_OnBoarding_Four.this.lbl_goal;
                    Screen_OnBoarding_Four screen_OnBoarding_Four = Screen_OnBoarding_Four.this;
                    appCompatTextView.setText(screen_OnBoarding_Four.getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
                    Screen_OnBoarding_Four.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
                    Screen_OnBoarding_Four.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
                    dialog.dismiss();
                } else if (Screen_OnBoarding_Four.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) || Float.parseFloat(lbl_goal2.getText().toString().trim()) < 30.0f) {
                    Screen_OnBoarding_Four.this.ah.customAlert(Screen_OnBoarding_Four.this.sh.get_string(R.string.str_set_daily_goal_validation));
                } else {
                    URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
                    Screen_OnBoarding_Four.this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
                    AppCompatTextView appCompatTextView2 = Screen_OnBoarding_Four.this.lbl_goal;
                    Screen_OnBoarding_Four screen_OnBoarding_Four2 = Screen_OnBoarding_Four.this;
                    appCompatTextView2.setText(screen_OnBoarding_Four2.getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
                    Screen_OnBoarding_Four.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
                    Screen_OnBoarding_Four.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
