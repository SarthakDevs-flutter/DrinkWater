package com.trending.water.drinking.reminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Preferences_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.String_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.custom.DigitsInputFilter;
import com.trending.water.drinking.reminder.custom.InputFilterRange;
import com.trending.water.drinking.reminder.custom.InputFilterWeightRange;
import com.trending.water.drinking.reminder.lib.cropper.CropImage;
import com.trending.water.drinking.reminder.mywidgets.NewAppWidget;
import com.trending.water.drinking.reminder.utils.FileUtils;
import com.trending.water.drinking.reminder.utils.HeightWeightHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Screen_Profile extends MasterBaseAppCompatActivity {
    private static final int PICK_Camera_IMAGE = 2;
    private static final int SELECT_FILE1 = 1;
    private static final int STORAGE_PERMISSION = 3;
    View active_line;
    BottomSheetDialog bottomSheetDialog;
    RelativeLayout change_profile;
    LinearLayout edit_user_name_block;
    LinearLayout gender_block;
    LinearLayout goal_block;
    LinearLayout height_block;
    List<String> height_cm_lst = new ArrayList();
    List<Double> height_feet_elements = new ArrayList();
    List<String> height_feet_lst = new ArrayList();
    Uri imageUri;
    ImageView img_user;
    boolean isExecute = true;
    boolean isExecuteSeekbar = true;
    AppCompatTextView lbl_active;
    AppCompatTextView lbl_breastfeeding;
    AppCompatTextView lbl_gender;
    AppCompatTextView lbl_goal;
    AppCompatTextView lbl_height;
    AppCompatTextView lbl_other_factor;
    AppCompatTextView lbl_pregnant;
    AppCompatTextView lbl_toolbar_title;
    AppCompatTextView lbl_weather;
    AppCompatTextView lbl_weight;
    LinearLayout left_icon_block;
    PopupWindow mDropdown = null;
    PopupWindow mDropdownWeather = null;
    LinearLayout other_factors;
    RadioButton rdo_cm;
    RadioButton rdo_feet;
    RadioButton rdo_kg;
    RadioButton rdo_lb;
    LinearLayout right_icon_block;
    Uri selectedImage;
    SwitchCompat switch_active;
    SwitchCompat switch_breastfeeding;
    SwitchCompat switch_pregnant;
    AppCompatTextView txt_gender;
    AppCompatTextView txt_goal;
    AppCompatTextView txt_height;
    AppCompatTextView txt_user_name;
    AppCompatTextView txt_weather;
    AppCompatTextView txt_weight;
    LinearLayout weather_block;
    LinearLayout weight_block;
    List<String> weight_kg_lst = new ArrayList();
    List<String> weight_lb_lst = new ArrayList();
    private String selectedImagePath;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.screen_profile);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(this.mContext.getResources().getColor(R.color.water_color));
        }
        URLFactory.DAILY_WATER_VALUE = this.ph.getFloat(URLFactory.DAILY_WATER);
        FindViewById();
        Header();
        Body();
        init_WeightKG();
        init_WeightLB();
        init_HeightCM();
        init_HeightFeet();
        loadHeightData();
    }

    public void loadHeightData() {
        this.height_feet_elements.clear();
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
    }

    public void FindViewById() {
        String_Helper string_Helper;
        int i;
        StringBuilder sb;
        String str;
        this.right_icon_block = (LinearLayout) findViewById(R.id.right_icon_block);
        this.left_icon_block = (LinearLayout) findViewById(R.id.left_icon_block);
        this.lbl_toolbar_title = (AppCompatTextView) findViewById(R.id.lbl_toolbar_title);
        this.txt_weight = (AppCompatTextView) findViewById(R.id.txt_weight);
        this.img_user = (ImageView) findViewById(R.id.img_user);
        this.txt_user_name = (AppCompatTextView) findViewById(R.id.txt_user_name);
        this.edit_user_name_block = (LinearLayout) findViewById(R.id.edit_user_name_block);
        this.change_profile = (RelativeLayout) findViewById(R.id.change_profile);
        this.gender_block = (LinearLayout) findViewById(R.id.gender_block);
        this.height_block = (LinearLayout) findViewById(R.id.height_block);
        this.weight_block = (LinearLayout) findViewById(R.id.weight_block);
        this.goal_block = (LinearLayout) findViewById(R.id.goal_block);
        this.active_line = findViewById(R.id.active_line);
        this.lbl_active = (AppCompatTextView) findViewById(R.id.lbl_active);
        this.lbl_pregnant = (AppCompatTextView) findViewById(R.id.lbl_pregnant);
        this.lbl_breastfeeding = (AppCompatTextView) findViewById(R.id.lbl_breastfeeding);
        this.switch_active = (SwitchCompat) findViewById(R.id.switch_active);
        this.switch_pregnant = (SwitchCompat) findViewById(R.id.switch_pregnant);
        this.switch_breastfeeding = (SwitchCompat) findViewById(R.id.switch_breastfeeding);
        this.other_factors = (LinearLayout) findViewById(R.id.other_factors);
        this.lbl_other_factor = (AppCompatTextView) findViewById(R.id.lbl_other_factor);
        this.weather_block = (LinearLayout) findViewById(R.id.weather_block);
        this.txt_weather = (AppCompatTextView) findViewById(R.id.txt_weather);
        this.lbl_weather = (AppCompatTextView) findViewById(R.id.lbl_weather);
        this.lbl_gender = (AppCompatTextView) findViewById(R.id.lbl_gender);
        this.txt_gender = (AppCompatTextView) findViewById(R.id.txt_gender);
        this.lbl_height = (AppCompatTextView) findViewById(R.id.lbl_height);
        this.txt_height = (AppCompatTextView) findViewById(R.id.txt_height);
        this.lbl_weight = (AppCompatTextView) findViewById(R.id.lbl_weight);
        this.txt_weight = (AppCompatTextView) findViewById(R.id.txt_weight);
        this.txt_goal = (AppCompatTextView) findViewById(R.id.txt_goal);
        this.lbl_goal = (AppCompatTextView) findViewById(R.id.lbl_goal);
        convertUpperCase(this.lbl_gender);
        convertUpperCase(this.lbl_weight);
        convertUpperCase(this.lbl_height);
        convertUpperCase(this.lbl_goal);
        convertUpperCase(this.lbl_active);
        convertUpperCase(this.lbl_pregnant);
        convertUpperCase(this.lbl_breastfeeding);
        convertUpperCase(this.lbl_weather);
        convertUpperCase(this.lbl_other_factor);
        this.txt_user_name.setText(this.ph.getString(URLFactory.USER_NAME));
        AppCompatTextView appCompatTextView = this.txt_gender;
        if (this.ph.getBoolean(URLFactory.USER_GENDER)) {
            string_Helper = this.sh;
            i = R.string.str_female;
        } else {
            string_Helper = this.sh;
            i = R.string.str_male;
        }
        appCompatTextView.setText(string_Helper.get_string(i));
        loadPhoto();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.ph.getString(URLFactory.PERSON_HEIGHT));
        sb2.append(" ");
        sb2.append(this.ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT) ? "cm" : "feet");
        this.txt_height.setText(sb2.toString());
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            sb = new StringBuilder();
            sb.append(URLFactory.decimalFormat2.format(Double.parseDouble(this.ph.getString(URLFactory.PERSON_WEIGHT))));
            str = " kg";
        } else {
            sb = new StringBuilder();
            sb.append(this.ph.getString(URLFactory.PERSON_WEIGHT));
            str = " lb";
        }
        sb.append(str);
        this.txt_weight.setText(sb.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
        sb3.append(" ");
        sb3.append(this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz");
        this.txt_goal.setText(sb3.toString());
    }

    public void loadPhoto() {
        boolean check_blank_data = this.sh.check_blank_data(this.ph.getString(URLFactory.USER_PHOTO));
        int i = R.drawable.male_white;
        if (check_blank_data) {
            RequestManager with = Glide.with(this.act);
            if (this.ph.getBoolean(URLFactory.USER_GENDER)) {
                i = R.drawable.female_white;
            }
            with.load(Integer.valueOf(i)).apply((BaseRequestOptions<?>) RequestOptions.circleCropTransform()).into(this.img_user);
            return;
        }
        boolean ex = false;
        try {
            if (new File(this.ph.getString(URLFactory.USER_PHOTO)).exists()) {
                ex = true;
            }
        } catch (Exception e) {
        }
        if (ex) {
            Glide.with(this.act).load(this.ph.getString(URLFactory.USER_PHOTO)).apply((BaseRequestOptions<?>) RequestOptions.circleCropTransform()).into(this.img_user);
            return;
        }
        RequestManager with2 = Glide.with(this.act);
        if (this.ph.getBoolean(URLFactory.USER_GENDER)) {
            i = R.drawable.female_white;
        }
        with2.load(Integer.valueOf(i)).apply((BaseRequestOptions<?>) RequestOptions.circleCropTransform()).into(this.img_user);
    }

    public String getData(String str) {
        return str.replace(",", ".");
    }

    public void convertUpperCase(AppCompatTextView appCompatTextView) {
        appCompatTextView.setText(appCompatTextView.getText().toString().toUpperCase());
    }

    public void Header() {
        this.lbl_toolbar_title.setText(this.sh.get_string(R.string.str_my_profile));
        this.left_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Profile.this.finish();
            }
        });
        this.right_icon_block.setVisibility(View.GONE);
    }

    public void Body() {
        String str;
        this.change_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    Screen_Profile.this.checkStoragePermissions();
                } else {
                    Screen_Profile.this.openPicker();
                }
            }
        });
        this.gender_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopupWindow unused = Screen_Profile.this.initiatePopupWindow(v);
            }
        });
        this.edit_user_name_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Profile.this.openNameDialog();
            }
        });
        this.goal_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Profile.this.showSetManuallyGoalDialog();
            }
        });
        this.height_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Profile.this.openHeightDialog();
            }
        });
        this.weight_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Profile.this.openWeightDialog();
            }
        });
        this.switch_active.setChecked(this.ph.getBoolean(URLFactory.IS_ACTIVE));
        this.switch_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                double tmp_kg;
                double diff;
                double diff2;
                Screen_Profile.this.ph.savePreferences(URLFactory.IS_ACTIVE, isChecked);
                String tmp_weight = "" + Screen_Profile.this.ph.getString(URLFactory.PERSON_WEIGHT);
                boolean isFemale = Screen_Profile.this.ph.getBoolean(URLFactory.USER_GENDER);
                float min = Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? 900.0f : 30.0f;
                float max = Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? 8000.0f : 270.0f;
                int weatherIdx = Screen_Profile.this.ph.getInt(URLFactory.WEATHER_CONSITIONS);
                Log.d("maxmaxmaxmax : ", "" + max + " @@@ " + min + "  @@@  " + tmp_weight);
                if (Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                    tmp_kg = Double.parseDouble("" + tmp_weight);
                } else {
                    tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
                }
                Log.d("maxmaxmaxmax : ", "" + tmp_kg);
                if (isFemale) {
                    diff = URLFactory.DEACTIVE_FEMALE_WATER * tmp_kg;
                } else {
                    diff = URLFactory.DEACTIVE_MALE_WATER * tmp_kg;
                }
                Log.d("maxmaxmaxmax DIFF : ", "" + diff);
                if (weatherIdx == 1) {
                    diff2 = diff * URLFactory.WEATHER_CLOUDY;
                } else if (weatherIdx == 2) {
                    diff2 = diff * URLFactory.WEATHER_RAINY;
                } else if (weatherIdx == 3) {
                    diff2 = diff * URLFactory.WEATHER_SNOW;
                } else {
                    diff2 = diff * URLFactory.WEATHER_SUNNY;
                }
                Log.d("maxmaxmaxmax : ", "" + diff2 + " @@@ " + URLFactory.DAILY_WATER_VALUE);
                if (isChecked) {
                    if (Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                        URLFactory.DAILY_WATER_VALUE = (float) (((double) URLFactory.DAILY_WATER_VALUE) + diff2);
                    } else {
                        URLFactory.DAILY_WATER_VALUE = (float) (((double) URLFactory.DAILY_WATER_VALUE) + HeightWeightHelper.mlToOzConverter(diff2));
                    }
                    if (URLFactory.DAILY_WATER_VALUE > max) {
                        URLFactory.DAILY_WATER_VALUE = max;
                    }
                } else {
                    if (Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                        URLFactory.DAILY_WATER_VALUE = (float) (((double) URLFactory.DAILY_WATER_VALUE) - diff2);
                    } else {
                        URLFactory.DAILY_WATER_VALUE = (float) (((double) URLFactory.DAILY_WATER_VALUE) - HeightWeightHelper.mlToOzConverter(diff2));
                    }
                    if (URLFactory.DAILY_WATER_VALUE > max) {
                        URLFactory.DAILY_WATER_VALUE = max;
                    }
                }
                URLFactory.DAILY_WATER_VALUE = (float) Math.round(URLFactory.DAILY_WATER_VALUE);
                StringBuilder sb = new StringBuilder();
                sb.append(Screen_Profile.this.getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
                sb.append(" ");
                sb.append(Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz");
                Screen_Profile.this.txt_goal.setText(sb.toString());
                Screen_Profile.this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
            }
        });
        this.switch_breastfeeding.setChecked(this.ph.getBoolean(URLFactory.IS_BREATFEEDING));
        this.switch_breastfeeding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Screen_Profile.this.ph.savePreferences(URLFactory.IS_BREATFEEDING, isChecked);
                Screen_Profile.this.setSwitchData(isChecked, URLFactory.BREASTFEEDING_WATER);
            }
        });
        this.switch_pregnant.setChecked(this.ph.getBoolean(URLFactory.IS_PREGNANT));
        this.switch_pregnant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Screen_Profile.this.ph.savePreferences(URLFactory.IS_PREGNANT, isChecked);
                Screen_Profile.this.setSwitchData(isChecked, URLFactory.PREGNANT_WATER);
            }
        });
        this.other_factors.setVisibility(this.ph.getBoolean(URLFactory.USER_GENDER) ? 0 : 8);
        if (this.ph.getInt(URLFactory.WEATHER_CONSITIONS) == 1) {
            str = this.sh.get_string(R.string.cloudy);
        } else if (this.ph.getInt(URLFactory.WEATHER_CONSITIONS) == 2) {
            str = this.sh.get_string(R.string.rainy);
        } else if (this.ph.getInt(URLFactory.WEATHER_CONSITIONS) == 3) {
            str = this.sh.get_string(R.string.snow);
        } else {
            str = this.sh.get_string(R.string.sunny);
        }
        this.txt_weather.setText(str);
        this.weather_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopupWindow unused = Screen_Profile.this.initiateWeatherPopupWindow(Screen_Profile.this.switch_active);
            }
        });
        calculateActiveValue();
    }

    public void openPicker() {
        this.bottomSheetDialog = new BottomSheetDialog(this.act);
        View view = LayoutInflater.from(this.act).inflate(R.layout.bottom_sheet_pick_image, (ViewGroup) null, false);
        AppCompatTextView btnGallery = (AppCompatTextView) view.findViewById(R.id.btnGallery);
        AppCompatTextView btnCamera = (AppCompatTextView) view.findViewById(R.id.btnCamera);
        AppCompatTextView btnCancel = (AppCompatTextView) view.findViewById(R.id.btnCancel);
        AppCompatTextView btnRemove = (AppCompatTextView) view.findViewById(R.id.btnRemove);
        View btnRemoveLine = view.findViewById(R.id.btnRemoveLine);
        if (this.sh.check_blank_data(this.ph.getString(URLFactory.USER_PHOTO))) {
            btnRemove.setVisibility(View.GONE);
            btnRemoveLine.setVisibility(View.GONE);
        } else {
            btnRemove.setVisibility(View.VISIBLE);
            btnRemoveLine.setVisibility(View.VISIBLE);
        }
        btnGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Profile.this.selectImage();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Profile.this.captureImage();
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Profile.this.bottomSheetDialog.dismiss();
                new AlertDialog.Builder(Screen_Profile.this.act).setMessage(Screen_Profile.this.sh.get_string(R.string.str_remove_photo_confirmation_message)).setPositiveButton(Screen_Profile.this.sh.get_string(R.string.str_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Screen_Profile.this.ph.savePreferences(URLFactory.USER_PHOTO, "");
                        Screen_Profile.this.loadPhoto();
                    }
                }).setNegativeButton(Screen_Profile.this.sh.get_string(R.string.str_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Profile.this.bottomSheetDialog.dismiss();
            }
        });
        this.bottomSheetDialog.setContentView(view);
        this.bottomSheetDialog.show();
    }

    public void setSwitchData(boolean isChecked, double water) {
        double diff;
        float min = this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? 900.0f : 30.0f;
        float max = this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? 8000.0f : 270.0f;
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            diff = water;
        } else {
            diff = HeightWeightHelper.mlToOzConverter(water);
        }
        if (isChecked) {
            URLFactory.DAILY_WATER_VALUE = (float) (((double) URLFactory.DAILY_WATER_VALUE) + diff);
            if (URLFactory.DAILY_WATER_VALUE > max) {
                URLFactory.DAILY_WATER_VALUE = max;
            }
        } else {
            URLFactory.DAILY_WATER_VALUE = (float) (((double) URLFactory.DAILY_WATER_VALUE) - diff);
            if (URLFactory.DAILY_WATER_VALUE < min) {
                URLFactory.DAILY_WATER_VALUE = min;
            }
        }
        URLFactory.DAILY_WATER_VALUE = (float) Math.round(URLFactory.DAILY_WATER_VALUE);
        StringBuilder sb = new StringBuilder();
        sb.append(getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
        sb.append(" ");
        sb.append(this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz");
        this.txt_goal.setText(sb.toString());
        this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
        calculateActiveValue();
    }

    public void showGenderMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.female_item) {
                    Screen_Profile.this.ph.savePreferences(URLFactory.USER_GENDER, true);
                    Screen_Profile.this.loadPhoto();
                    return true;
                } else if (itemId != R.id.male_item) {
                    return false;
                } else {
                    Screen_Profile.this.ph.savePreferences(URLFactory.USER_GENDER, false);
                    Screen_Profile.this.loadPhoto();
                    return true;
                }
            }
        });
        popup.inflate(R.menu.gender_menu);
        popup.show();
    }

    /* access modifiers changed from: private */
    public PopupWindow initiatePopupWindow(View v) {
        try {
            View layout = ((LayoutInflater) getApplicationContext().getSystemService("layout_inflater")).inflate(R.layout.row_item_gender, (ViewGroup) null);
            TextView lbl_male = (TextView) layout.findViewById(R.id.lbl_male);
            lbl_male.setText(this.sh.get_string(R.string.str_male));
            TextView lbl_female = (TextView) layout.findViewById(R.id.lbl_female);
            lbl_female.setText(this.sh.get_string(R.string.str_female));
            lbl_male.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Screen_Profile.this.ph.savePreferences(URLFactory.USER_GENDER, false);
                    Screen_Profile.this.loadPhoto();
                    Screen_Profile.this.mDropdown.dismiss();
                    Screen_Profile.this.txt_gender.setText(Screen_Profile.this.sh.get_string(R.string.str_male));
                    Screen_Profile.this.other_factors.setVisibility(View.GONE);
                    Screen_Profile.this.switch_breastfeeding.setChecked(false);
                    Screen_Profile.this.switch_pregnant.setChecked(false);
                    Screen_Profile.this.calculate_goal();
                }
            });
            lbl_female.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Screen_Profile.this.ph.savePreferences(URLFactory.USER_GENDER, true);
                    Screen_Profile.this.loadPhoto();
                    Screen_Profile.this.mDropdown.dismiss();
                    Screen_Profile.this.txt_gender.setText(Screen_Profile.this.sh.get_string(R.string.str_female));
                    Screen_Profile.this.other_factors.setVisibility(View.VISIBLE);
                    Screen_Profile.this.calculate_goal();
                }
            });
            layout.measure(0, 0);
            this.mDropdown = new PopupWindow(layout, -2, -2, true);
            this.mDropdown.showAsDropDown(v, 5, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.mDropdown;
    }

    /* access modifiers changed from: private */
    public PopupWindow initiateWeatherPopupWindow(View v) {
        try {
            View layout = ((LayoutInflater) getApplicationContext().getSystemService("layout_inflater")).inflate(R.layout.row_item_weather, (ViewGroup) null);
            TextView lbl_sunny = (TextView) layout.findViewById(R.id.lbl_sunny);
            lbl_sunny.setText(this.sh.get_string(R.string.sunny));
            TextView lbl_cloudy = (TextView) layout.findViewById(R.id.lbl_cloudy);
            lbl_cloudy.setText(this.sh.get_string(R.string.cloudy));
            TextView lbl_rainy = (TextView) layout.findViewById(R.id.lbl_rainy);
            lbl_rainy.setText(this.sh.get_string(R.string.rainy));
            TextView lbl_snow = (TextView) layout.findViewById(R.id.lbl_snow);
            lbl_snow.setText(this.sh.get_string(R.string.snow));
            lbl_sunny.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Screen_Profile.this.ph.savePreferences(URLFactory.WEATHER_CONSITIONS, 0);
                    Screen_Profile.this.mDropdownWeather.dismiss();
                    Screen_Profile.this.txt_weather.setText(Screen_Profile.this.sh.get_string(R.string.sunny));
                    Screen_Profile.this.calculate_goal();
                }
            });
            lbl_cloudy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Screen_Profile.this.ph.savePreferences(URLFactory.WEATHER_CONSITIONS, 1);
                    Screen_Profile.this.mDropdownWeather.dismiss();
                    Screen_Profile.this.txt_weather.setText(Screen_Profile.this.sh.get_string(R.string.cloudy));
                    Screen_Profile.this.calculate_goal();
                }
            });
            lbl_rainy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Screen_Profile.this.ph.savePreferences(URLFactory.WEATHER_CONSITIONS, 2);
                    Screen_Profile.this.mDropdownWeather.dismiss();
                    Screen_Profile.this.txt_weather.setText(Screen_Profile.this.sh.get_string(R.string.rainy));
                    Screen_Profile.this.calculate_goal();
                }
            });
            lbl_snow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Screen_Profile.this.ph.savePreferences(URLFactory.WEATHER_CONSITIONS, 3);
                    Screen_Profile.this.mDropdownWeather.dismiss();
                    Screen_Profile.this.txt_weather.setText(Screen_Profile.this.sh.get_string(R.string.snow));
                    Screen_Profile.this.calculate_goal();
                }
            });
            layout.measure(0, 0);
            this.mDropdownWeather = new PopupWindow(layout, -2, -2, true);
            this.mDropdownWeather.showAsDropDown(v, 5, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.mDropdownWeather;
    }

    public void openNameDialog() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_user_name, (ViewGroup) null, false);
        final AppCompatEditText txt_name = (AppCompatEditText) view.findViewById(R.id.txt_name);
        txt_name.requestFocus();
        ((RelativeLayout) view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        ((ImageView) view.findViewById(R.id.img_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        txt_name.setText(this.ph.getString(URLFactory.USER_NAME));
        txt_name.setSelection(txt_name.getText().toString().trim().length());
        ((RelativeLayout) view.findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_Profile.this.sh.check_blank_data(txt_name.getText().toString().trim())) {
                    Screen_Profile.this.ah.customAlert(Screen_Profile.this.sh.get_string(R.string.str_your_name_validation));
                } else if (txt_name.getText().toString().trim().length() < 3) {
                    Screen_Profile.this.ah.customAlert(Screen_Profile.this.sh.get_string(R.string.str_valid_name_validation));
                } else {
                    Screen_Profile.this.ph.savePreferences(URLFactory.USER_NAME, txt_name.getText().toString().trim());
                    Screen_Profile.this.txt_user_name.setText(Screen_Profile.this.ph.getString(URLFactory.USER_NAME));
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
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
                Screen_Profile.this.isExecuteSeekbar = false;
            }

            public void afterTextChanged(Editable editable) {
                try {
                    if (!Screen_Profile.this.sh.check_blank_data(lbl_goal2.getText().toString().trim()) && Screen_Profile.this.isExecute) {
                        seekbarGoal.setProgress(Integer.parseInt(lbl_goal2.getText().toString().trim()));
                    }
                } catch (Exception e) {
                }
                Screen_Profile.this.isExecuteSeekbar = true;
            }
        });
        seekbarGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBars, int progress, boolean fromUser) {
                if (Screen_Profile.this.isExecuteSeekbar) {
                    if (Build.VERSION.SDK_INT < 26) {
                        if (Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
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
                Screen_Profile.this.isExecute = false;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Screen_Profile.this.isExecute = true;
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String unit = Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz";
                if (Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && Float.parseFloat(lbl_goal2.getText().toString().trim()) >= 900.0f) {
                    URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
                    Screen_Profile.this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
                    AppCompatTextView appCompatTextView = Screen_Profile.this.txt_goal;
                    StringBuilder sb = new StringBuilder();
                    Screen_Profile screen_Profile = Screen_Profile.this;
                    sb.append(screen_Profile.getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
                    sb.append(" ");
                    sb.append(unit);
                    appCompatTextView.setText(sb.toString());
                    Screen_Profile.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
                    Screen_Profile.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
                    dialog.dismiss();
                    Screen_Profile.this.refreshWidget();
                } else if (Screen_Profile.this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) || Float.parseFloat(lbl_goal2.getText().toString().trim()) < 30.0f) {
                    Screen_Profile.this.ah.customAlert(Screen_Profile.this.sh.get_string(R.string.str_set_daily_goal_validation));
                } else {
                    URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
                    Screen_Profile.this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
                    AppCompatTextView appCompatTextView2 = Screen_Profile.this.txt_goal;
                    StringBuilder sb2 = new StringBuilder();
                    Screen_Profile screen_Profile2 = Screen_Profile.this;
                    sb2.append(screen_Profile2.getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
                    sb2.append(" ");
                    sb2.append(unit);
                    appCompatTextView2.setText(sb2.toString());
                    Screen_Profile.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
                    Screen_Profile.this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
                    dialog.dismiss();
                    Screen_Profile.this.refreshWidget();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void openHeightDialog() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_height, (ViewGroup) null, false);
        RelativeLayout btn_cancel = (RelativeLayout) view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_add = (RelativeLayout) view.findViewById(R.id.btn_add);
        ImageView img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        final AppCompatEditText txt_dialog_height = (AppCompatEditText) view.findViewById(R.id.txt_height);
        this.rdo_cm = (RadioButton) view.findViewById(R.id.rdo_cm);
        this.rdo_feet = (RadioButton) view.findViewById(R.id.rdo_feet);
        txt_dialog_height.requestFocus();
        this.rdo_cm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Screen_Profile.this.sh.check_blank_data(txt_dialog_height.getText().toString())) {
                    int final_height_cm = 61;
                    try {
                        String tmp_height = Screen_Profile.this.getData(txt_dialog_height.getText().toString().trim());
                        int d = (int) Float.parseFloat(txt_dialog_height.getText().toString().trim());
                        Log.d("after_decimal", "" + tmp_height.indexOf("."));
                        if (tmp_height.indexOf(".") > 0) {
                            String after_decimal = tmp_height.substring(tmp_height.indexOf(".") + 1);
                            if (!Screen_Profile.this.sh.check_blank_data(after_decimal)) {
                                final_height_cm = (int) Math.round(2.54d * ((double) ((d * 12) + Integer.parseInt(after_decimal))));
                            } else {
                                final_height_cm = (int) Math.round(((double) (d * 12)) * 2.54d);
                            }
                        } else {
                            final_height_cm = (int) Math.round(((double) (d * 12)) * 2.54d);
                        }
                    } catch (Exception e) {
                    }
                    Screen_Profile.this.rdo_feet.setClickable(true);
                    Screen_Profile.this.rdo_cm.setClickable(false);
                    txt_dialog_height.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
                    AppCompatEditText appCompatEditText = txt_dialog_height;
                    Screen_Profile screen_Profile = Screen_Profile.this;
                    appCompatEditText.setText(screen_Profile.getData("" + final_height_cm));
                    txt_dialog_height.setSelection(txt_dialog_height.length());
                    return;
                }
                Screen_Profile.this.rdo_feet.setChecked(true);
                Screen_Profile.this.rdo_cm.setChecked(false);
            }
        });
        this.rdo_feet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Screen_Profile.this.sh.check_blank_data(txt_dialog_height.getText().toString())) {
                    String final_height_feet = "5.0";
                    try {
                        int tmp_height_inch = (int) Math.round(((double) ((int) Float.parseFloat(txt_dialog_height.getText().toString().trim()))) / 2.54d);
                        final_height_feet = (tmp_height_inch / 12) + "." + (tmp_height_inch % 12);
                    } catch (Exception e) {
                    }
                    Screen_Profile.this.rdo_feet.setClickable(false);
                    Screen_Profile.this.rdo_cm.setClickable(true);
                    txt_dialog_height.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, Screen_Profile.this.height_feet_elements)});
                    txt_dialog_height.setText(Screen_Profile.this.getData(final_height_feet));
                    txt_dialog_height.setSelection(txt_dialog_height.length());
                    return;
                }
                Screen_Profile.this.rdo_feet.setChecked(false);
                Screen_Profile.this.rdo_cm.setChecked(true);
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
        if (!this.sh.check_blank_data(this.ph.getString(URLFactory.PERSON_HEIGHT))) {
            if (this.rdo_cm.isChecked()) {
                txt_dialog_height.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
                txt_dialog_height.setText(getData(this.ph.getString(URLFactory.PERSON_HEIGHT)));
            } else {
                txt_dialog_height.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, this.height_feet_elements)});
                txt_dialog_height.setText(getData(this.ph.getString(URLFactory.PERSON_HEIGHT)));
            }
        } else if (this.rdo_cm.isChecked()) {
            txt_dialog_height.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 240.0d)});
            txt_dialog_height.setText("150");
        } else {
            txt_dialog_height.setFilters(new InputFilter[]{new InputFilterRange(Utils.DOUBLE_EPSILON, this.height_feet_elements)});
            txt_dialog_height.setText("5.0");
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        img_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        txt_dialog_height.setSelection(txt_dialog_height.getText().toString().length());
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_Profile.this.sh.check_blank_data(txt_dialog_height.getText().toString().trim())) {
                    Screen_Profile.this.ah.customAlert(Screen_Profile.this.sh.get_string(R.string.str_height_validation));
                    return;
                }
                String str = txt_dialog_height.getText().toString().trim();
                if (Screen_Profile.this.rdo_feet.isChecked() && !str.contains(".11") && !str.contains(".10")) {
                    str = URLFactory.decimalFormat2.format(Double.parseDouble(str));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" ");
                sb.append(Screen_Profile.this.rdo_feet.isChecked() ? "feet" : "cm");
                Screen_Profile.this.txt_height.setText(sb.toString());
                Screen_Profile.this.saveData(txt_dialog_height);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void openWeightDialog() {
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_weight, (ViewGroup) null, false);
        RelativeLayout btn_cancel = (RelativeLayout) view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_add = (RelativeLayout) view.findViewById(R.id.btn_add);
        ImageView img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        final AppCompatEditText txt_dialog_weight = (AppCompatEditText) view.findViewById(R.id.txt_weight);
        this.rdo_kg = (RadioButton) view.findViewById(R.id.rdo_kg);
        this.rdo_lb = (RadioButton) view.findViewById(R.id.rdo_lb);
        txt_dialog_weight.requestFocus();
        this.rdo_kg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Screen_Profile.this.sh.check_blank_data(txt_dialog_weight.getText().toString())) {
                    double weight_in_lb = Double.parseDouble(txt_dialog_weight.getText().toString());
                    double weight_in_kg = Utils.DOUBLE_EPSILON;
                    if (weight_in_lb > Utils.DOUBLE_EPSILON) {
                        weight_in_kg = (double) Math.round(HeightWeightHelper.lbToKgConverter(weight_in_lb));
                    }
                    txt_dialog_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 130.0d)});
                    AppCompatEditText appCompatEditText = txt_dialog_weight;
                    Screen_Profile screen_Profile = Screen_Profile.this;
                    appCompatEditText.setText(screen_Profile.getData("" + URLFactory.decimalFormat2.format((long) ((int) weight_in_kg))));
                    Screen_Profile.this.rdo_kg.setClickable(false);
                    Screen_Profile.this.rdo_lb.setClickable(true);
                }
            }
        });
        this.rdo_lb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Screen_Profile.this.sh.check_blank_data(txt_dialog_weight.getText().toString())) {
                    double weight_in_kg = Double.parseDouble(txt_dialog_weight.getText().toString());
                    double weight_in_lb = Utils.DOUBLE_EPSILON;
                    if (weight_in_kg > Utils.DOUBLE_EPSILON) {
                        weight_in_lb = (double) Math.round(HeightWeightHelper.kgToLbConverter(weight_in_kg));
                    }
                    txt_dialog_weight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
                    AppCompatEditText appCompatEditText = txt_dialog_weight;
                    Screen_Profile screen_Profile = Screen_Profile.this;
                    appCompatEditText.setText(screen_Profile.getData("" + ((int) weight_in_lb)));
                    Screen_Profile.this.rdo_kg.setClickable(true);
                    Screen_Profile.this.rdo_lb.setClickable(false);
                }
            }
        });
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            this.rdo_kg.setChecked(true);
            this.rdo_kg.setClickable(false);
            this.rdo_lb.setClickable(true);
        } else {
            this.rdo_lb.setChecked(true);
            this.rdo_kg.setClickable(true);
            this.rdo_lb.setClickable(false);
        }
        if (!this.sh.check_blank_data(this.ph.getString(URLFactory.PERSON_WEIGHT))) {
            if (this.rdo_kg.isChecked()) {
                txt_dialog_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 130.0d)});
                txt_dialog_weight.setText(getData(this.ph.getString(URLFactory.PERSON_WEIGHT)));
            } else {
                txt_dialog_weight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
                txt_dialog_weight.setText(getData(this.ph.getString(URLFactory.PERSON_WEIGHT)));
            }
        } else if (this.rdo_kg.isChecked()) {
            txt_dialog_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(Utils.DOUBLE_EPSILON, 130.0d)});
            txt_dialog_weight.setText("80.0");
        } else {
            txt_dialog_weight.setFilters(new InputFilter[]{new DigitsInputFilter(3, 0, 287.0d)});
            txt_dialog_weight.setText("176");
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        img_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        txt_dialog_weight.setSelection(txt_dialog_weight.getText().toString().length());
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Screen_Profile.this.sh.check_blank_data(txt_dialog_weight.getText().toString().trim())) {
                    Screen_Profile.this.ah.customAlert(Screen_Profile.this.sh.get_string(R.string.str_weight_validation));
                    return;
                }
                String str = txt_dialog_weight.getText().toString().trim();
                if (Screen_Profile.this.rdo_kg.isChecked()) {
                    str = URLFactory.decimalFormat2.format(Double.parseDouble(str));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" ");
                sb.append(Screen_Profile.this.rdo_kg.isChecked() ? "kg" : "lb");
                Screen_Profile.this.txt_weight.setText(sb.toString());
                Screen_Profile.this.saveWeightData(txt_dialog_weight);
                Screen_Profile.this.calculate_goal();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
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
                URLFactory.DAILY_WATER_VALUE = (float) tmp_ml;
            } else {
                URLFactory.DAILY_WATER_VALUE = (float) tmp_oz;
            }
            URLFactory.DAILY_WATER_VALUE = (float) Math.round(URLFactory.DAILY_WATER_VALUE);
            StringBuilder sb = new StringBuilder();
            sb.append(getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
            sb.append(" ");
            sb.append(this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz");
            this.txt_goal.setText(sb.toString());
            this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
        }
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
                URLFactory.DAILY_WATER_VALUE = (float) tot_drink2;
            } else {
                URLFactory.DAILY_WATER_VALUE = (float) tot_drink_fl_oz;
            }
            URLFactory.DAILY_WATER_VALUE = (float) Math.round(URLFactory.DAILY_WATER_VALUE);
            StringBuilder sb = new StringBuilder();
            sb.append(getData("" + ((int) URLFactory.DAILY_WATER_VALUE)));
            sb.append(" ");
            sb.append(this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) ? "ml" : "fl oz");
            this.txt_goal.setText(sb.toString());
            this.ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
            refreshWidget();
            calculateActiveValue();
        }
    }

    public void calculateActiveValue() {
        String pstr;
        String bstr;
        double tmp_kg;
        double diff;
        double diff2;
        String bstr2;
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            pstr = ((int) URLFactory.PREGNANT_WATER) + " ml";
        } else {
            pstr = ((int) Math.round(HeightWeightHelper.mlToOzConverter(URLFactory.PREGNANT_WATER))) + " fl oz";
        }
        this.lbl_pregnant.setText(this.sh.get_string(R.string.pregnant));
        convertUpperCase(this.lbl_pregnant);
        this.lbl_pregnant.setText(this.lbl_pregnant.getText().toString() + " (+" + pstr + ")");
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            bstr = ((int) URLFactory.BREASTFEEDING_WATER) + " ml";
        } else {
            bstr = ((int) Math.round(HeightWeightHelper.mlToOzConverter(URLFactory.BREASTFEEDING_WATER))) + " fl oz";
        }
        this.lbl_breastfeeding.setText(this.sh.get_string(R.string.breastfeeding));
        convertUpperCase(this.lbl_breastfeeding);
        this.lbl_breastfeeding.setText(this.lbl_breastfeeding.getText().toString() + " (+" + bstr + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.ph.getString(URLFactory.PERSON_WEIGHT));
        String tmp_weight = sb.toString();
        boolean isFemale = this.ph.getBoolean(URLFactory.USER_GENDER);
        int weatherIdx = this.ph.getInt(URLFactory.WEATHER_CONSITIONS);
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            tmp_kg = Double.parseDouble("" + tmp_weight);
        } else {
            tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
        }
        if (isFemale) {
            diff = URLFactory.DEACTIVE_FEMALE_WATER * tmp_kg;
        } else {
            diff = URLFactory.DEACTIVE_MALE_WATER * tmp_kg;
        }
        if (weatherIdx == 1) {
            diff2 = diff * URLFactory.WEATHER_CLOUDY;
        } else if (weatherIdx == 2) {
            diff2 = diff * URLFactory.WEATHER_RAINY;
        } else if (weatherIdx == 3) {
            diff2 = diff * URLFactory.WEATHER_SNOW;
        } else {
            diff2 = diff * URLFactory.WEATHER_SUNNY;
        }
        if (this.ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            bstr2 = ((int) Math.round(diff2)) + " ml";
        } else {
            bstr2 = ((int) Math.round(HeightWeightHelper.mlToOzConverter(diff2))) + " fl oz";
        }
        this.lbl_active.setText(this.sh.get_string(R.string.active));
        convertUpperCase(this.lbl_active);
        this.lbl_active.setText(this.lbl_active.getText().toString() + " (+" + bstr2 + ")");
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void saveData(AppCompatEditText txt_name) {
        Log.d("saveData", "" + txt_name.getText().toString().trim());
        Preferences_Helper preferences_Helper = this.ph;
        String str = URLFactory.PERSON_HEIGHT;
        preferences_Helper.savePreferences(str, "" + txt_name.getText().toString().trim());
        this.ph.savePreferences(URLFactory.PERSON_HEIGHT_UNIT, this.rdo_cm.isChecked());
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
    }

    public void saveWeightData(AppCompatEditText txt_name) {
        Log.d("saveWeightData", "" + this.rdo_kg.isChecked() + " @@@ " + txt_name.getText().toString().trim());
        Preferences_Helper preferences_Helper = this.ph;
        String str = URLFactory.PERSON_WEIGHT;
        preferences_Helper.savePreferences(str, "" + txt_name.getText().toString().trim());
        this.ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT, this.rdo_kg.isChecked());
        this.ph.savePreferences(URLFactory.WATER_UNIT, this.rdo_kg.isChecked() ? "ml" : "fl oz");
        this.ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, false);
        URLFactory.WATER_UNIT_VALUE = this.rdo_kg.isChecked() ? "ml" : "fl oz";
        refreshWidget();
    }

    public void refreshWidget() {
        Intent intent = new Intent(this.act, NewAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", AppWidgetManager.getInstance(this.act).getAppWidgetIds(new ComponentName(this.act, NewAppWidget.class)));
        this.act.sendBroadcast(intent);
    }

    public void init_WeightKG() {
        this.weight_kg_lst.clear();
        List<String> list = this.weight_kg_lst;
        list.add("" + 30.0f);
        float f = 30.0f;
        for (int k = 0; k < 200; k++) {
            f = (float) (((double) f) + 0.5d);
            List<String> list2 = this.weight_kg_lst;
            list2.add("" + f);
        }
        CharSequence[] st = new CharSequence[this.weight_kg_lst.size()];
        for (int k2 = 0; k2 < this.weight_kg_lst.size(); k2++) {
            st[k2] = "" + this.weight_kg_lst.get(k2);
        }
    }

    public void init_WeightLB() {
        this.weight_lb_lst.clear();
        for (int k = 66; k < 288; k++) {
            List<String> list = this.weight_lb_lst;
            list.add("" + k);
        }
        CharSequence[] st = new CharSequence[this.weight_lb_lst.size()];
        for (int k2 = 0; k2 < this.weight_lb_lst.size(); k2++) {
            st[k2] = "" + this.weight_lb_lst.get(k2);
        }
    }

    public void init_HeightCM() {
        this.height_cm_lst.clear();
        for (int k = 60; k < 241; k++) {
            List<String> list = this.height_cm_lst;
            list.add("" + k);
        }
        CharSequence[] st = new CharSequence[this.height_cm_lst.size()];
        for (int k2 = 0; k2 < this.height_cm_lst.size(); k2++) {
            st[k2] = "" + this.height_cm_lst.get(k2);
        }
    }

    public void init_HeightFeet() {
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
        CharSequence[] st = new CharSequence[this.height_feet_lst.size()];
        for (int k = 0; k < this.height_feet_lst.size(); k++) {
            st[k] = "" + this.height_feet_lst.get(k);
        }
    }

    public void selectImage() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
    }

    public void captureImage() {
        getSaveImageUri();
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra("output", this.imageUri);
        startActivityForResult(cameraIntent, 2);
    }

    public void getSaveImageUri() {
        try {
            File root = new File(Environment.getExternalStorageDirectory() + "/" + URLFactory.APP_DIRECTORY_NAME + "/" + URLFactory.APP_PROFILE_DIRECTORY_NAME + "/");
            if (!root.exists()) {
                root.mkdirs();
            }
            File sdImageMainDirectory = new File(root, "profile_image.png");
            if (Build.VERSION.SDK_INT >= 24) {
                Activity activity = this.act;
                this.imageUri = FileProvider.getUriForFile(activity, this.act.getPackageName() + ".provider", sdImageMainDirectory);
                this.selectedImagePath = sdImageMainDirectory.getAbsolutePath();
                return;
            }
            this.imageUri = Uri.fromFile(sdImageMainDirectory);
            this.selectedImagePath = FileUtils.getPath(this.act, this.imageUri);
        } catch (Exception e) {
            Log.d("Incident Photo ", "Error occurred. Please try again later." + e.getMessage());
        }
    }

    @RequiresApi(api = 23)
    public void checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this.act, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this.act, "android.permission.CAMERA") == 0) {
            openPicker();
        } else {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 3);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3 && grantResults.length > 0 && grantResults[0] == 0) {
            openPicker();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 203) {
            switch (requestCode) {
                case 1:
                    if (resultCode != -1) {
                        return;
                    }
                    if (ContextCompat.checkSelfPermission(this.act, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        this.bottomSheetDialog.dismiss();
                        return;
                    }
                    try {
                        this.selectedImage = data.getData();
                        if (this.selectedImage != null) {
                            this.bottomSheetDialog.dismiss();
                            CropImage.activity(this.selectedImage).start(this.act);
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                case 2:
                    if (resultCode == -1) {
                        String str = this.selectedImagePath;
                        this.bottomSheetDialog.dismiss();
                        CropImage.activity(this.imageUri).start(this.act);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                Uri resultUri = result.getUri();
                this.ph.savePreferences(URLFactory.USER_PHOTO, FileUtils.getPath(this.act, resultUri));
                Glide.with(this.act).load(resultUri).apply((BaseRequestOptions<?>) RequestOptions.circleCropTransform()).into(this.img_user);
            } else if (resultCode == 204) {
                result.getError();
            }
        }
    }
}
