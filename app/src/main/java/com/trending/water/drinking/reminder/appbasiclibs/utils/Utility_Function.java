package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.widget.ActivityChooserView;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.mycustom.ClickSpan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility_Function {
    public static final Pattern emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+");
    public static final Pattern gstPattern = Pattern.compile("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z\\d]{1}[Z]{1}[a-zA-Z\\d]{1}");
    Activity act;
    Context mContext;
    String_Helper sh;

    public Utility_Function(Context mContext2, Activity act2) {
        this.mContext = mContext2;
        this.act = act2;
        this.sh = new String_Helper(mContext2, act2);
    }

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
        return "http://img.youtube.com/vi/" + extractVideoIdFromUrl(videoUrl) + "/0.jpg";
    }

    public static String extractVideoIdFromUrl(String url) {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);
        for (String regex : Constant.videoIdRegex) {
            Matcher matcher = Pattern.compile(regex).matcher(youTubeLinkWithoutProtocolAndDomain);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private static String youTubeLinkWithoutProtocolAndDomain(String url) {
        Matcher matcher = Pattern.compile(Constant.youTubeUrlRegEx).matcher(url);
        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }
        return url;
    }

    public void permission_StrictMode() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        }
    }

    public void permission_VMStrictMode() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
    }

    public void hideKeyboard() {
        View view = this.act.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) this.act.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void clickify_read_more_click(final TextView txt, final View read_more, final String str) {
        read_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                txt.setText(Html.fromHtml(Html.fromHtml(str.toString().trim() + " " + Utility_Function.this.sh.get_string(R.string.str_read_less)).toString()));
                txt.setMaxLines(Integer.MAX_VALUE);
                txt.setSingleLine(false);
                txt.setEllipsize((TextUtils.TruncateAt) null);
                read_more.setVisibility(View.GONE);
                Utility_Function.this.clickify_read_less_click(txt, read_more, str);
            }
        });
    }

    public void clickify_read_less_click(final TextView txt, final View read_more, final String str) {
        clickify(txt, "" + this.sh.get_string(R.string.str_read_less), new ClickSpan.OnClickListener() {
            public void onClick() {
                try {
                    txt.setMaxLines(1);
                    txt.setSingleLine(true);
                    txt.setText(Html.fromHtml(Html.fromHtml(str.toString().trim()).toString()));
                    read_more.setVisibility(View.VISIBLE);
                    txt.setEllipsize(TextUtils.TruncateAt.END);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clickify(TextView view, String clickableText, ClickSpan.OnClickListener listener) {
        CharSequence text = view.getText();
        String string = text.toString();
        ClickSpan span = new ClickSpan(listener);
        int start = string.indexOf(clickableText);
        int end = clickableText.length() + start;
        if (start != -1) {
            if (text instanceof Spannable) {
                ((Spannable) text).setSpan(span, start, end, 33);
            } else {
                SpannableString s = SpannableString.valueOf(text);
                s.setSpan(span, start, end, 33);
                view.setText(s);
            }
            MovementMethod m = view.getMovementMethod();
            if (m == null || !(m instanceof LinkMovementMethod)) {
                view.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    public void share_wish(String str_subject, String str_link) {
        new Intent_Helper(this.mContext, this.act).CustomShare(str_subject, str_link);
    }

    public boolean isValidEmail(String email) {
        if (emailPattern.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public boolean isValidGSTNO(String no) {
        if (gstPattern.matcher(no).matches()) {
            return true;
        }
        return false;
    }

    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            FileWriter writer = new FileWriter(new File(root, sFileName));
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadbanner() {
    }

    public void setLabelFont(String font_name, TextView lbl_set) {
        lbl_set.setTypeface(Typeface.createFromAsset(this.mContext.getAssets(), font_name));
    }

    public boolean isTablet() {
        return (this.mContext.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public String getIMEI() {
        Context context = this.mContext;
        Context context2 = this.mContext;
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public void check_Enable_GPS() {
        if (((LocationManager) this.act.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            CheckEnableGPS();
        } else {
            turnGPSOn();
        }
    }

    public void CheckEnableGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.act);
        builder.setCancelable(false);
        builder.setMessage(this.act.getResources().getString(R.string.open_gps_dialog)).setPositiveButton(this.act.getResources().getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int id) {
                d.cancel();
                Utility_Function.this.act.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        }).setNegativeButton(this.act.getResources().getString(R.string.dialog_cancel_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int id) {
                d.cancel();
            }
        });
        builder.create().show();
    }

    public void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.act.sendBroadcast(intent);
    }

    public void turnGPSOff() {
    }

    public boolean chkinternet() {
        NetworkInfo i = ((ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (i != null && i.isConnected() && i.isAvailable()) {
            return true;
        }
        return false;
    }

    public double get_screen_size() {
        DisplayMetrics dm = new DisplayMetrics();
        this.act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        DisplayMetrics displayMetrics = dm;
        String screenInformation = String.format("%.2f", new Object[]{Double.valueOf(Math.sqrt(Math.pow(((double) width) / ((double) dens), 2.0d) + Math.pow(((double) height) / ((double) dens), 2.0d)))});
        return Double.parseDouble("" + screenInformation);
    }

    public String GetCountryZipCode() {
        String CountryZipCode = "";
        String CountryID = ((TelephonyManager) this.mContext.getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso().toUpperCase();
        String[] rl = this.mContext.getResources().getStringArray(R.array.CountryCodes);
        int i = 0;
        while (true) {
            if (i >= rl.length) {
                break;
            }
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
            i++;
        }
        return "+" + CountryZipCode;
    }
}
