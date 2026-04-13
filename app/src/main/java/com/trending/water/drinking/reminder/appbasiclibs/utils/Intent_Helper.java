package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.adapter.CustomShareAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Intent_Helper {
    Activity act;
    CustomShareAdapter app_adapter;
    ArrayList<String> appname = new ArrayList<>();
    Intent email = new Intent("android.intent.action.SEND");
    String[] lst_app_name = {"Gmail", "Facebook", "Twitter", "LinkedIn"};
    Context mContext;

    public Intent_Helper(Context mContext2, Activity act2) {
        this.mContext = mContext2;
        this.act = act2;
    }

    public void OpenContactList() {
        this.act.startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), 1000);
    }

    public void OpenGalllery() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*, video/*");
        this.act.startActivityForResult(intent, 101);
    }

    public void ShowPDF(File file) {
        PackageManager packageManager = this.mContext.getPackageManager();
        new Intent("android.intent.action.VIEW").setType("application/pdf");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        this.mContext.startActivity(intent);
    }

    public void ShareText(String title, String subject) {
        try {
            Intent share = new Intent("android.intent.action.SEND");
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra("android.intent.extra.SUBJECT", title);
            share.putExtra("android.intent.extra.TEXT", subject);
            this.mContext.startActivity(Intent.createChooser(share, Constant.general_share_title));
        } catch (Exception e) {
        }
    }

    public void ShareText() {
        try {
            Intent share = new Intent("android.intent.action.SEND");
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        } catch (Exception e) {
        }
    }

    public void ShareText_SpecificApp(String title, String subject) {
        List<Intent> targetShareIntents = new ArrayList<>();
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.setType("text/plain");
        PackageManager pm = this.mContext.getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana") || packageName.contains("com.whatsapp") || packageName.contains("com.google.android.apps.plus") || packageName.contains("com.google.android.talk") || packageName.contains("com.slack") || packageName.contains("com.google.android.gm") || packageName.contains("com.facebook.orca") || packageName.contains("com.yahoo.mobile") || packageName.contains("com.skype.raider") || packageName.contains("com.android.mms") || packageName.contains("com.linkedin.android") || packageName.contains("com.google.android.apps.messaging")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", title);
                    intent.putExtra("android.intent.extra.SUBJECT", subject);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Collections.sort(targetShareIntents, new Comparator<Intent>() {
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) targetShareIntents.toArray(new Parcelable[0]));
                this.mContext.startActivity(chooserIntent);
                return;
            }
            Toast.makeText(this.mContext, "No app to share.", Toast.LENGTH_LONG).show();
        }
    }

    public void get_share_list() {
        Constant.pm = this.mContext.getPackageManager();
        this.email.putExtra("android.intent.extra.EMAIL", new String[0]);
        this.email.putExtra("android.intent.extra.SUBJECT", "SUBJECT");
        this.email.putExtra("android.intent.extra.TEXT", "TEXT");
        this.email.setType("text/plain");
        Constant.launchables = Constant.pm.queryIntentActivities(this.email, 0);
        Constant.launchables_sel = new ArrayList();
        int k1 = 0;
        while (k1 < Constant.launchables.size()) {
            try {
                int position = k1;
                for (String lowerCase : this.lst_app_name) {
                    if (Constant.launchables.get(position).loadLabel(Constant.pm).toString().toLowerCase().contains(lowerCase.toLowerCase())) {
                        Constant.launchables_sel.add(Constant.launchables.get(position));
                    }
                }
                k1++;
            } catch (Exception e) {
                return;
            }
        }
        Collections.sort(Constant.launchables_sel, new ResolveInfo.DisplayNameComparator(Constant.pm));
        this.app_adapter = new CustomShareAdapter(this.mContext, Constant.pm, Constant.launchables_sel);
    }

    public void CustomShare(final String str_subject, final String str_text) {
        get_share_list();
        final Dialog dialog = new Dialog(this.mContext, R.style.AppDialogTheme);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.custom_share_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(this.mContext.getResources().getDisplayMetrics().widthPixels - 80, this.mContext.getResources().getDisplayMetrics().heightPixels - 100);
        ((TextView) dialog.findViewById(R.id.textView1)).setText(Constant.share_purchase_title);
        ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        lv.setAdapter(this.app_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                dialog.dismiss();
                ActivityInfo activity = ((ResolveInfo) Intent_Helper.this.app_adapter.getItem(position)).activityInfo;
                Intent_Helper.this.appname.clear();
                Intent_Helper.this.appname = Intent_Helper.this.app_adapter.getAppName();
                String str = Intent_Helper.this.app_adapter.get_app_name(position);
                ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                Intent_Helper.this.email.addCategory("android.intent.category.LAUNCHER");
                Intent_Helper.this.email.setFlags(270532608);
                Intent_Helper.this.email.setComponent(name);
                Intent intent = Intent_Helper.this.email;
                intent.putExtra("android.intent.extra.SUBJECT", "" + str_subject);
                Intent intent2 = Intent_Helper.this.email;
                intent2.putExtra("android.intent.extra.TEXT", "" + str_text);
                dialog.dismiss();
                Intent_Helper.this.mContext.startActivity(Intent_Helper.this.email);
            }
        });
        dialog.show();
    }

    public void SEND_EMAIL_WITH_ATTACHMENT(String[] filePaths) {
        ArrayList<Uri> uris = new ArrayList<>();
        Intent emailintent = new Intent("android.intent.action.SEND_MULTIPLE");
        emailintent.setType("text/plain");
        ResolveInfo best = null;
        for (ResolveInfo info : this.mContext.getPackageManager().queryIntentActivities(emailintent, 0)) {
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                best = info;
            }
        }
        if (best != null) {
            emailintent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        }
        emailintent.setType("message/rfc822");
        emailintent.putExtra("android.intent.extra.EMAIL", new String[]{""});
        emailintent.setType("application/pdf");
        boolean is_empty = true;
        for (String file : filePaths) {
            File fileIn = new File(file);
            if (fileIn.length() > 0) {
                uris.add(Uri.fromFile(fileIn));
                is_empty = false;
            }
        }
        if (!is_empty) {
            emailintent.putParcelableArrayListExtra("android.intent.extra.STREAM", uris);
            this.mContext.startActivity(emailintent);
        }
    }

    private void setAlarm(Calendar targetCal, int unique_id) {
        Intent intent = new Intent(this.act.getBaseContext(), Intent_Helper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.act.getBaseContext(), unique_id, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) this.act.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        }
    }

    private boolean appInstalledOrNot(String uri) {
        try {
            this.mContext.getPackageManager().getPackageInfo(uri, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void OpenFacebookPage(String fbpageid) {
        String facebookPageID = fbpageid;
        String facebookUrl = "https://www.facebook.com/" + facebookPageID;
        String facebookUrlScheme = "fb://page/" + facebookPageID;
        try {
            if (appInstalledOrNot("com.facebook.katana")) {
                if (this.mContext.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode >= 3002850) {
                    this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("fb://facewebmodal/f?href=" + facebookUrl)));
                } else {
                    this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(facebookUrlScheme)));
                }
                return;
            }
            this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana")));
        } catch (PackageManager.NameNotFoundException e) {
            this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(facebookUrl)));
        }
    }
}
