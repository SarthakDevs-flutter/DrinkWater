package com.trending.water.drinking.reminder.appbasiclibs;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewbinding.ViewBinding;

import com.trending.water.drinking.reminder.appbasiclibs.utils.AlertHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.BitmapHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DatabaseHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.DateHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.IntentHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.MapHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.PreferenceHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.StringHelper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.UtilityFunction;
import com.trending.water.drinking.reminder.appbasiclibs.utils.ZipHelper;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.io.File;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    protected VB binding;
    protected Activity mActivity;
    protected Context mContext;
    protected AlertHelper alertHelper;
    protected BitmapHelper bitmapHelper;
    protected DatabaseHelper databaseHelper;
    protected DateHelper dateHelper;
    protected IntentHelper intentHelper;
    protected MapHelper mapHelper;
    protected PreferenceHelper preferencesHelper;
    protected StringHelper stringHelper;
    protected UtilityFunction utilityFunction;
    protected ZipHelper zipHelper;

    protected ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    onActivityResultImagePicker(uri);
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    protected ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    onActivityResultCameraPicker(setupCameraUri());
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });


    protected void onActivityResultImagePicker(Uri uri){}

    protected void onActivityResultCameraPicker(Uri uri){}



    protected abstract VB inflateBinding(LayoutInflater inflater);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = inflateBinding(getLayoutInflater());
        if (binding != null) {
            setContentView(binding.getRoot());
            addInsetPaddingSupport();
        }

        this.mContext = this;
        this.mActivity = this;

        this.utilityFunction = new UtilityFunction(this.mContext, this.mActivity);
        this.alertHelper = new AlertHelper(this.mContext);
        this.bitmapHelper = new BitmapHelper(this.mContext);
        this.dateHelper = new DateHelper();
        this.databaseHelper = new DatabaseHelper(this.mContext, this.mActivity);
        this.intentHelper = new IntentHelper(this.mContext, this.mActivity);
        this.mapHelper = new MapHelper();
        this.stringHelper = new StringHelper(this.mContext);
        this.preferencesHelper = new PreferenceHelper(this.mContext);
        this.zipHelper = new ZipHelper();

        this.utilityFunction.enableStrictMode();
    }

    private void addInsetPaddingSupport() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    protected Uri setupCameraUri() {
        try {
            File root = new File(Environment.getExternalStorageDirectory() + "/" + URLFactory.APP_DIRECTORY_NAME + "/" + URLFactory.PROFILE_DIR_NAME + "/");
            if (!root.exists()) {
                root.mkdirs();
            }
            if (!root.exists()) root.mkdirs();
            File file = new File(root, "profile_image.png");
            return FileProvider.getUriForFile(mActivity, getPackageName() + ".provider", file);
        } catch (Exception e) {
            Log.e("TAG_camera", "Error setting up camera URI", e);
        }
        return null;
    }
}
