package com.trending.water.drinking.reminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.custom.DigitsInputFilter;
import com.trending.water.drinking.reminder.custom.InputFilterRange;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.databinding.ScreenProfileBinding;
import com.trending.water.drinking.reminder.lib.cropper.CropImage;
import com.trending.water.drinking.reminder.mywidgets.NewAppWidget;
import com.trending.water.drinking.reminder.utils.FileUtils;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Screen_Profile extends MasterBaseAppCompatActivity<ScreenProfileBinding> {

    @Override
    protected ScreenProfileBinding inflateBinding(LayoutInflater inflater) {
        return ScreenProfileBinding.inflate(inflater);
    }


    private static final String TAG = "Screen_Profile";
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_CAMERA_IMAGE = 2;
    private static final int REQUEST_STORAGE_PERMISSION = 3;
    private final List<Double> heightFeetElements = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;

    private PopupWindow popupMenu;
    private Uri imageUri;
    private String selectedImagePath;
    private boolean isGoalUpdateExecuting = true;
    private RadioButton rdoCm;
    private RadioButton rdoFeet;
    private RadioButton rdoKg;
    private RadioButton rdoLb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.water_color));
        }

        URLFactory.dailyWaterValue = preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 2500.0f);

        initView();
        setupListeners();
        loadHeightData();
    }

    private void initView() {
        binding.headerBlock.lblToolbarTitle.setText(stringHelper.getString(R.string.str_my_profile));
        binding.headerBlock.rightIconBlock.setVisibility(View.GONE);

        // Header and labels
        setUpperCase(binding.lblGender);
        setUpperCase(binding.lblWeight);
        setUpperCase(binding.lblHeight);
        setUpperCase(binding.lblGoal);
        setUpperCase(binding.lblActive);
        setUpperCase(binding.lblPregnant);
        setUpperCase(binding.lblBreastfeeding);
        setUpperCase(binding.lblWeather);
        setUpperCase(binding.lblOtherFactor);

        binding.txtUserName.setText(preferencesHelper.getString(URLFactory.KEY_USER_NAME, "User"));
        binding.txtGender.setText(preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false) ?
                stringHelper.getString(R.string.str_female) : stringHelper.getString(R.string.str_male));

        loadUserPhoto();
        updateHeightDisplay();
        updateWeightDisplay();
        updateGoalDisplay();

        binding.switchActive.setChecked(preferencesHelper.getBoolean(URLFactory.KEY_IS_ACTIVE, false));
        binding.switchBreastfeeding.setChecked(preferencesHelper.getBoolean(URLFactory.KEY_IS_BREASTFEEDING, false));
        binding.switchPregnant.setChecked(preferencesHelper.getBoolean(URLFactory.KEY_IS_PREGNANT, false));

        binding.otherFactors.setVisibility(preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false) ? View.VISIBLE : View.GONE);

        updateWeatherDisplay();
        calculateActiveFactors();
    }

    private void setupListeners() {
        binding.headerBlock.leftIconBlock.setOnClickListener(v -> finish());

        binding.changeProfile.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkStoragePermissions();
            } else {
                openImagePicker();
            }
        });

        binding.genderBlock.setOnClickListener(v -> showGenderPopup(v));
        binding.editUserNameBlock.setOnClickListener(v -> openUserNameDialog());
        binding.goalBlock.setOnClickListener(v -> showSetGoalManuallyDialog());
        binding.heightBlock.setOnClickListener(v -> openHeightDialog());
        binding.weightBlock.setOnClickListener(v -> openWeightDialog());

        binding.switchActive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_IS_ACTIVE, isChecked);
            calculateGoalBasedOnFactors();
        });

        binding.switchBreastfeeding.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_IS_BREASTFEEDING, isChecked);
            calculateGoalBasedOnFactors();
        });

        binding.switchPregnant.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.savePreferences(URLFactory.KEY_IS_PREGNANT, isChecked);
            calculateGoalBasedOnFactors();
        });

        binding.weatherBlock.setOnClickListener(v -> showWeatherPopup(v));
    }

    private void loadHeightData() {
        heightFeetElements.clear();
        for (int ft = 2; ft <= 7; ft++) {
            for (int in = 0; in <= 11; in++) {
                heightFeetElements.add(Double.parseDouble(ft + "." + in));
            }
        }
        heightFeetElements.add(8.0);
    }

    private void setUpperCase(AppCompatTextView textView) {
        textView.setText(textView.getText().toString().toUpperCase());
    }

    private void loadUserPhoto() {
        String photoPath = preferencesHelper.getString(URLFactory.KEY_USER_PHOTO, "");
        int placeholder = preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false) ?
                R.drawable.female_white : R.drawable.male_white;

        if (photoPath.isEmpty() || !new File(photoPath).exists()) {
            Glide.with(mActivity).load(placeholder).apply(RequestOptions.circleCropTransform()).into(binding.imgUser);
        } else {
            Glide.with(mActivity).load(photoPath).apply(RequestOptions.circleCropTransform()).into(binding.imgUser);
        }
    }

    private void updateHeightDisplay() {
        boolean isCm = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_HEIGHT_UNIT, true);
        String height = preferencesHelper.getString(URLFactory.KEY_PERSON_HEIGHT, "160");
        binding.txtHeight.setText(String.format(Locale.getDefault(), "%s %s", height, isCm ? "cm" : "ft"));
    }

    private void updateWeightDisplay() {
        boolean isKg = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        String weight = preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "70");
        binding.txtWeight.setText(String.format(Locale.getDefault(), "%s %s", weight, isKg ? "kg" : "lb"));
    }

    private void updateGoalDisplay() {
        boolean isMl = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        int goal = (int) URLFactory.dailyWaterValue;
        binding.txtGoal.setText(String.format(Locale.getDefault(), "%d %s", goal, isMl ? "ml" : "fl oz"));
    }

    private void updateWeatherDisplay() {
        int idx = preferencesHelper.getInt(URLFactory.KEY_WEATHER_CONDITIONS, 0);
        String[] weatherArr = {
                stringHelper.getString(R.string.sunny),
                stringHelper.getString(R.string.cloudy),
                stringHelper.getString(R.string.rainy),
                stringHelper.getString(R.string.snow)
        };
       binding. txtWeather.setText(weatherArr[idx]);
    }

    private void calculateActiveFactors() {
        boolean isKg = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        String unit = isKg ? "ml" : "fl oz";

        // Pregnant
        int pVal = isKg ? (int) URLFactory.PREGNANT_WATER_ADDITIONAL : (int) Math.round(HeightWeightHelper.convertMlToOz(URLFactory.PREGNANT_WATER_ADDITIONAL));
        binding. lblPregnant.setText(String.format(Locale.getDefault(), "%s (+%d %s)", stringHelper.getString(R.string.pregnant).toUpperCase(), pVal, unit));

        // Breastfeeding
        int bVal = isKg ? (int) URLFactory.BREASTFEEDING_WATER_ADDITIONAL : (int) Math.round(HeightWeightHelper.convertMlToOz(URLFactory.BREASTFEEDING_WATER_ADDITIONAL));
        binding.  lblBreastfeeding.setText(String.format(Locale.getDefault(), "%s (+%d %s)", stringHelper.getString(R.string.breastfeeding).toUpperCase(), bVal, unit));

        // Active
        double weight = Double.parseDouble(preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "70"));
        double weightKg = isKg ? weight : HeightWeightHelper.convertLbToKg(weight);
        boolean isFemale = preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false);

        double baseDiff = (isFemale ? URLFactory.DEACTIVE_FEMALE_WATER : URLFactory.DEACTIVE_MALE_WATER) * weightKg;
        int weatherIdx = preferencesHelper.getInt(URLFactory.KEY_WEATHER_CONDITIONS, 0);
        double weatherFactor = URLFactory.WEATHER_SUNNY;
        if (weatherIdx == 1) weatherFactor = URLFactory.WEATHER_CLOUDY;
        else if (weatherIdx == 2) weatherFactor = URLFactory.WEATHER_RAINY;
        else if (weatherIdx == 3) weatherFactor = URLFactory.WEATHER_SNOW;

        int activeVal = (int) Math.round(baseDiff * weatherFactor);
        if (!isKg) activeVal = (int) Math.round(HeightWeightHelper.convertMlToOz(activeVal));

        binding.lblActive.setText(String.format(Locale.getDefault(), "%s (+%d %s)", stringHelper.getString(R.string.active).toUpperCase(), activeVal, unit));
    }

    private void calculateGoalBasedOnFactors() {
        double weight = Double.parseDouble(preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "70"));
        boolean isKg = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        double weightKg = isKg ? weight : HeightWeightHelper.convertLbToKg(weight);

        boolean isFemale = preferencesHelper.getBoolean(URLFactory.KEY_USER_GENDER, false);
        boolean isActive = preferencesHelper.getBoolean(URLFactory.KEY_IS_ACTIVE, false);
        boolean isPregnant = preferencesHelper.getBoolean(URLFactory.KEY_IS_PREGNANT, false);
        boolean isBreastfeeding = preferencesHelper.getBoolean(URLFactory.KEY_IS_BREASTFEEDING, false);
        int weatherIdx = preferencesHelper.getInt(URLFactory.KEY_WEATHER_CONDITIONS, 0);

        double baseFactor;
        if (isFemale) {
            baseFactor = isActive ? URLFactory.ACTIVE_FEMALE_WATER : URLFactory.FEMALE_WATER;
        } else {
            baseFactor = isActive ? URLFactory.ACTIVE_MALE_WATER : URLFactory.MALE_WATER;
        }

        double drinkTotalMl = baseFactor * weightKg;

        double weatherFactor = URLFactory.WEATHER_SUNNY;
        if (weatherIdx == 1) weatherFactor = URLFactory.WEATHER_CLOUDY;
        else if (weatherIdx == 2) weatherFactor = URLFactory.WEATHER_RAINY;
        else if (weatherIdx == 3) weatherFactor = URLFactory.WEATHER_SNOW;

        drinkTotalMl *= weatherFactor;

        if (isFemale) {
            if (isPregnant) drinkTotalMl += URLFactory.PREGNANT_WATER_ADDITIONAL;
            if (isBreastfeeding) drinkTotalMl += URLFactory.BREASTFEEDING_WATER_ADDITIONAL;
        }

        // Constraints
        if (drinkTotalMl < 900) drinkTotalMl = 900;
        if (drinkTotalMl > 8000) drinkTotalMl = 8000;

        URLFactory.dailyWaterValue = (float) (isKg ? drinkTotalMl : HeightWeightHelper.convertMlToOz(drinkTotalMl));
        URLFactory.dailyWaterValue = (float) Math.round(URLFactory.dailyWaterValue);

        preferencesHelper.savePreferences(URLFactory.KEY_DAILY_WATER_GOAL, URLFactory.dailyWaterValue);
        updateGoalDisplay();
        calculateActiveFactors();
        refreshWidget();
    }

    private void showGenderPopup(View v) {
        View layout = LayoutInflater.from(this).inflate(R.layout.row_item_gender, null);
        TextView lblMale = layout.findViewById(R.id.lbl_male);
        TextView lblFemale = layout.findViewById(R.id.lbl_female);

        lblMale.setText(stringHelper.getString(R.string.str_male));
        lblFemale.setText(stringHelper.getString(R.string.str_female));

        lblMale.setOnClickListener(view -> {
            preferencesHelper.savePreferences(URLFactory.KEY_USER_GENDER, false);
            updateGenderView(false);
            popupMenu.dismiss();
        });

        lblFemale.setOnClickListener(view -> {
            preferencesHelper.savePreferences(URLFactory.KEY_USER_GENDER, true);
            updateGenderView(true);
            popupMenu.dismiss();
        });

        popupMenu = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupMenu.showAsDropDown(v, 5, 5);
    }

    private void updateGenderView(boolean isFemale) {
       binding.txtGender.setText(stringHelper.getString(isFemale ? R.string.str_female : R.string.str_male));
        binding.  otherFactors.setVisibility(isFemale ? View.VISIBLE : View.GONE);
        if (!isFemale) {
            binding.  switchPregnant.setChecked(false);
            binding.  switchBreastfeeding.setChecked(false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_PREGNANT, false);
            preferencesHelper.savePreferences(URLFactory.KEY_IS_BREASTFEEDING, false);
        }
        loadUserPhoto();
        calculateGoalBasedOnFactors();
    }

    private void showWeatherPopup(View v) {
        View layout = LayoutInflater.from(this).inflate(R.layout.row_item_weather, null);
        TextView lblSunny = layout.findViewById(R.id.lbl_sunny);
        TextView lblCloudy = layout.findViewById(R.id.lbl_cloudy);
        TextView lblRainy = layout.findViewById(R.id.lbl_rainy);
        TextView lblSnow = layout.findViewById(R.id.lbl_snow);

        lblSunny.setText(stringHelper.getString(R.string.sunny));
        lblCloudy.setText(stringHelper.getString(R.string.cloudy));
        lblRainy.setText(stringHelper.getString(R.string.rainy));
        lblSnow.setText(stringHelper.getString(R.string.snow));

        lblSunny.setOnClickListener(view -> updateWeather(0));
        lblCloudy.setOnClickListener(view -> updateWeather(1));
        lblRainy.setOnClickListener(view -> updateWeather(2));
        lblSnow.setOnClickListener(view -> updateWeather(3));

        popupMenu = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupMenu.showAsDropDown(v, 5, 5);
    }

    private void updateWeather(int idx) {
        preferencesHelper.savePreferences(URLFactory.KEY_WEATHER_CONDITIONS, idx);
        updateWeatherDisplay();
        calculateGoalBasedOnFactors();
        if (popupMenu != null) popupMenu.dismiss();
    }

    private void openUserNameDialog() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_user_name, null);
        AppCompatEditText etName = view.findViewById(R.id.txt_name);
        etName.setText(preferencesHelper.getString(URLFactory.KEY_USER_NAME, ""));
        etName.setSelection(etName.getText().length());

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_add).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_your_name_validation));
            } else if (name.length() < 3) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_valid_name_validation));
            } else {
                preferencesHelper.savePreferences(URLFactory.KEY_USER_NAME, name);
                binding.txtUserName.setText(name);
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void showSetGoalManuallyDialog() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_set_manually_goal, null);
        AppCompatEditText etGoal = view.findViewById(R.id.lbl_goal);
        AppCompatTextView lblUnit = view.findViewById(R.id.lbl_unit);
        SeekBar seekBar = view.findViewById(R.id.seekbarGoal);

        boolean isKg = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        lblUnit.setText(isKg ? "ml" : "fl oz");

        int min = isKg ? 900 : 30;
        int max = isKg ? 8000 : 270;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(min);
        }
        seekBar.setMax(max);

        int currentGoal = (int) URLFactory.dailyWaterValue;
        etGoal.setText(String.valueOf(currentGoal));
        seekBar.setProgress(currentGoal);

        etGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isGoalUpdateExecuting = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String val = s.toString().trim();
                    if (!val.isEmpty()) {
                        int progress = Integer.parseInt(val);
                        seekBar.setProgress(progress);
                    }
                } catch (Exception ignored) {
                }
                isGoalUpdateExecuting = true;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isGoalUpdateExecuting) {
                    if (progress < min) {
                        progress = min;
                        seekBar.setProgress(min);
                    }
                    etGoal.setText(String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isGoalUpdateExecuting = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String val = etGoal.getText().toString().trim();
            if (!val.isEmpty()) {
                float newGoal = Float.parseFloat(val);
                if (newGoal >= min && newGoal <= max) {
                    URLFactory.dailyWaterValue = newGoal;
                    preferencesHelper.savePreferences(URLFactory.KEY_DAILY_WATER_GOAL, newGoal);
                    preferencesHelper.savePreferences(URLFactory.KEY_SET_MANUALLY_GOAL, true);
                    updateGoalDisplay();
                    refreshWidget();
                    dialog.dismiss();
                } else {
                    alertHelper.customAlert(stringHelper.getString(R.string.str_set_daily_goal_validation));
                }
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void openHeightDialog() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_height, null);
        AppCompatEditText etHeight = view.findViewById(R.id.txt_height);
        rdoCm = view.findViewById(R.id.rdo_cm);
        rdoFeet = view.findViewById(R.id.rdo_feet);

        boolean isCm = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_HEIGHT_UNIT, true);
        rdoCm.setChecked(isCm);
        rdoFeet.setChecked(!isCm);

        etHeight.setText(preferencesHelper.getString(URLFactory.KEY_PERSON_HEIGHT, "160"));
        etHeight.setFilters(new InputFilter[]{isCm ? new DigitsInputFilter(3, 0, 240.0) : new InputFilterRange(0.0, heightFeetElements)});

        rdoCm.setOnClickListener(v -> {
            if (!etHeight.getText().toString().isEmpty()) {
                double h = Double.parseDouble(etHeight.getText().toString());
                int cm = (int) Math.round(HeightWeightHelper.convertFeetToCm(h));
                etHeight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0)});
                etHeight.setText(String.valueOf(cm));
            }
            rdoCm.setClickable(false);
            rdoFeet.setClickable(true);
        });

        rdoFeet.setOnClickListener(v -> {
            if (!etHeight.getText().toString().isEmpty()) {
                double cm = Double.parseDouble(etHeight.getText().toString());
                double ft = HeightWeightHelper.convertCmToFeet(cm);
                etHeight.setFilters(new InputFilter[]{new InputFilterRange(0.0, heightFeetElements)});
                etHeight.setText(String.format(Locale.US, "%.1f", ft));
            }
            rdoCm.setClickable(true);
            rdoFeet.setClickable(false);
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_add).setOnClickListener(v -> {
            String val = etHeight.getText().toString().trim();
            if (val.isEmpty()) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_height_validation));
                return;
            }
            preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT, val);
            preferencesHelper.savePreferences(URLFactory.KEY_PERSON_HEIGHT_UNIT, rdoCm.isChecked());
            updateHeightDisplay();
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void openWeightDialog() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_weight, null);
        AppCompatEditText etWeight = view.findViewById(R.id.txt_weight);
        rdoKg = view.findViewById(R.id.rdo_kg);
        rdoLb = view.findViewById(R.id.rdo_lb);

        boolean isKg = preferencesHelper.getBoolean(URLFactory.KEY_PERSON_WEIGHT_UNIT, true);
        rdoKg.setChecked(isKg);
        rdoLb.setChecked(!isKg);

        etWeight.setText(preferencesHelper.getString(URLFactory.KEY_PERSON_WEIGHT, "70"));
        etWeight.setFilters(new InputFilter[]{isKg ? new InputFilterWeightRange(0.0, 130.0) : new DigitsInputFilter(3, 0, 287.0)});

        rdoKg.setOnClickListener(v -> {
            if (!etWeight.getText().toString().isEmpty()) {
                double lb = Double.parseDouble(etWeight.getText().toString());
                double kg = HeightWeightHelper.convertLbToKg(lb);
                etWeight.setFilters(new InputFilter[]{new InputFilterWeightRange(0.0, 130.0)});
                etWeight.setText(String.format(Locale.US, "%.1f", kg));
            }
            rdoKg.setClickable(false);
            rdoLb.setClickable(true);
        });

        rdoLb.setOnClickListener(v -> {
            if (!etWeight.getText().toString().isEmpty()) {
                double kg = Double.parseDouble(etWeight.getText().toString());
                double lb = HeightWeightHelper.convertKgToLb(kg);
                etWeight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0)});
                etWeight.setText(String.valueOf((int) Math.round(lb)));
            }
            rdoKg.setClickable(true);
            rdoLb.setClickable(false);
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_add).setOnClickListener(v -> {
            String val = etWeight.getText().toString().trim();
            if (val.isEmpty()) {
                alertHelper.customAlert(stringHelper.getString(R.string.str_weight_validation));
                return;
            }
            preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT, val);
            preferencesHelper.savePreferences(URLFactory.KEY_PERSON_WEIGHT_UNIT, rdoKg.isChecked());
            preferencesHelper.savePreferences(URLFactory.KEY_WATER_UNIT, rdoKg.isChecked() ? "ml" : "fl oz");
            URLFactory.waterUnitValue = rdoKg.isChecked() ? "ml" : "fl oz";

            updateWeightDisplay();
            calculateGoalBasedOnFactors();
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void openImagePicker() {
        bottomSheetDialog = new BottomSheetDialog(mActivity);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.bottom_sheet_pick_image, null);

        View rowRemove = view.findViewById(R.id.btnRemove);
        View rowRemoveLine = view.findViewById(R.id.btnRemoveLine);

        if (preferencesHelper.getString(URLFactory.KEY_USER_PHOTO, "").isEmpty()) {
            rowRemove.setVisibility(View.GONE);
            rowRemoveLine.setVisibility(View.GONE);
        }

        view.findViewById(R.id.btnGallery).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        });

        view.findViewById(R.id.btnCamera).setOnClickListener(v -> {
            setupCameraUri();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
        });

        view.findViewById(R.id.btnRemove).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            new AlertDialog.Builder(mActivity)
                    .setMessage(stringHelper.getString(R.string.str_remove_photo_confirmation_message))
                    .setPositiveButton(R.string.str_yes, (d, w) -> {
                        preferencesHelper.savePreferences(URLFactory.KEY_USER_PHOTO, "");
                        loadUserPhoto();
                    })
                    .setNegativeButton(R.string.str_no, null)
                    .show();
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void setupCameraUri() {
        try {
            File root = new File(Environment.getExternalStorageDirectory() + "/" + URLFactory.APP_DIRECTORY_NAME + "/" + URLFactory.PROFILE_DIR_NAME + "/");
            if (!root.exists()) {
                root.mkdirs();
            }
            if (!root.exists()) root.mkdirs();
            File file = new File(root, "profile_image.png");
            imageUri = FileProvider.getUriForFile(mActivity, getPackageName() + ".provider", file);
            selectedImagePath = file.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "Error setting up camera URI", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == 0) {
            openImagePicker();
        } else {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == 0) {
            openImagePicker();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (bottomSheetDialog != null) bottomSheetDialog.dismiss();

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                Uri selected = data.getData();
                if (selected != null) CropImage.activity(selected).start(this);
            } else if (requestCode == REQUEST_CAMERA_IMAGE) {
                CropImage.activity(imageUri).start(this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result != null) {
                    Uri resultUri = result.getUri();
                    String path = FileUtils.getPath(this, resultUri);
                    preferencesHelper.savePreferences(URLFactory.KEY_USER_PHOTO, path);
                    loadUserPhoto();
                }
            }
        }
    }

    private void refreshWidget() {
        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
