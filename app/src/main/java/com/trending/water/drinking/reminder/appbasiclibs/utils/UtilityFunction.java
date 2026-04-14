package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.mycustom.ClickSpan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityFunction {
    private static final String TAG = "UtilityFunction";
    
    public static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+");
    public static final Pattern GST_PATTERN = Pattern.compile("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z\\d]{1}[Z]{1}[a-zA-Z\\d]{1}");
    
    private final Activity activity;
    private final Context context;
    private final StringHelper stringHelper;

    public UtilityFunction(@NonNull Context context, @NonNull Activity activity) {
        this.context = context;
        this.activity = activity;
        this.stringHelper = new StringHelper(context);
    }

    @NonNull
    public static String getYoutubeThumbnailUrl(@NonNull String videoUrl) {
        String videoId = extractVideoIdFromUrl(videoUrl);
        return "https://img.youtube.com/vi/" + (videoId != null ? videoId : "") + "/0.jpg";
    }

    @Nullable
    public static String extractVideoIdFromUrl(@NonNull String url) {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);
        for (String regex : Constant.VIDEO_ID_REGEX) {
            Matcher matcher = Pattern.compile(regex).matcher(youTubeLinkWithoutProtocolAndDomain);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    @NonNull
    private static String youTubeLinkWithoutProtocolAndDomain(@NonNull String url) {
        Matcher matcher = Pattern.compile(Constant.YOUTUBE_URL_REGEX).matcher(url);
        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }
        return url;
    }

    public void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    }

    public void enableVmStrictMode() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
    }

    public void hideKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void setupReadMore(@NonNull final TextView textView, @NonNull final View readMoreView, @NonNull final String content) {
        readMoreView.setOnClickListener(v -> {
            String readLess = stringHelper.getString(R.string.str_read_less);
            textView.setText(Html.fromHtml(content.trim() + " " + readLess));
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setSingleLine(false);
            textView.setEllipsize(null);
            readMoreView.setVisibility(View.GONE);
            setupReadLess(textView, readMoreView, content);
        });
    }

    public void setupReadLess(@NonNull final TextView textView, @NonNull final View readMoreView, @NonNull final String content) {
        String readLess = stringHelper.getString(R.string.str_read_less);
        clickify(textView, readLess, () -> {
            try {
                textView.setMaxLines(1);
                textView.setSingleLine(true);
                textView.setText(Html.fromHtml(content.trim()));
                readMoreView.setVisibility(View.VISIBLE);
                textView.setEllipsize(TextUtils.TruncateAt.END);
            } catch (Exception e) {
                Log.e(TAG, "Error in read less click", e);
            }
        });
    }

    public void clickify(@NonNull TextView view, @NonNull String clickableText, @NonNull ClickSpan.OnClickListener listener) {
        CharSequence text = view.getText();
        String content = text.toString();
        int start = content.indexOf(clickableText);
        int end = start + clickableText.length();

        if (start != -1) {
            Spannable spannable = (text instanceof Spannable) ? (Spannable) text : new SpannableString(text);
            spannable.setSpan(new ClickSpan(listener), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(spannable);
            
            MovementMethod movementMethod = view.getMovementMethod();
            if (!(movementMethod instanceof LinkMovementMethod)) {
                view.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    public void share(@NonNull String subject, @NonNull String link) {
        new IntentHelper(context, activity).customShare(subject, link);
    }

    public boolean isValidEmail(@Nullable String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidGstNo(@Nullable String gstNo) {
        return gstNo != null && GST_PATTERN.matcher(gstNo).matches();
    }

    public void saveNoteToExternalStorage(@NonNull String fileName, @NonNull String body) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists() && !root.mkdirs()) return;
            
            try (FileWriter writer = new FileWriter(new File(root, fileName))) {
                writer.append(body);
                writer.flush();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error saving note to SD card", e);
        }
    }

    public void setCustomFont(@NonNull String fontPath, @NonNull TextView textView) {
        try {
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath));
        } catch (Exception e) {
            Log.e(TAG, "Error setting custom font: " + fontPath, e);
        }
    }

    public boolean isTablet() {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    @SuppressLint("HardwareIds")
    @Nullable
    public String getDeviceImei() {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                return tm.getDeviceId();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Security exception when getting IMEI", e);
        }
        return null;
    }

    public void checkGpsEnabled() {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (lm != null && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showEnableGpsDialog();
        }
    }

    private void showEnableGpsDialog() {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(R.string.open_gps_dialog)
                .setPositiveButton(R.string.dialog_ok_button, (dialog, id) -> {
                    dialog.cancel();
                    activity.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                })
                .setNegativeButton(R.string.dialog_cancel_button, (dialog, id) -> dialog.cancel())
                .show();
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }

    public double getScreenSizeInInches() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / (double) dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / (double) dm.ydpi, 2);
        double screenSize = Math.sqrt(x + y);
        return Double.parseDouble(String.format(Locale.US, "%.2f", screenSize));
    }

    @NonNull
    public String getCountryZipCode() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) return "+";
        
        String countryId = tm.getSimCountryIso().toUpperCase(Locale.US);
        String[] countryCodeArray = context.getResources().getStringArray(R.array.CountryCodes);
        for (String entry : countryCodeArray) {
            String[] parts = entry.split(",");
            if (parts.length >= 2 && parts[1].trim().equalsIgnoreCase(countryId)) {
                return "+" + parts[0].trim();
            }
        }
        return "+";
    }
}
