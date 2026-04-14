package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.appbasiclibs.adapter.CustomShareAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntentHelper {
    private static final String TAG = "IntentHelper";

    private final Activity activity;
    private final Context context;
    private final String[] targetAppLabels = {"Gmail", "Facebook", "Twitter", "LinkedIn"};

    public IntentHelper(@NonNull Context context, @NonNull Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void openContactList(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    public void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*, video/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public void showPdf(@NonNull File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error opening PDF", e);
            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareText(@NonNull String title, @NonNull String content) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(Intent.createChooser(shareIntent, Constant.GENERAL_SHARE_TITLE));
        } catch (Exception e) {
            Log.e(TAG, "Error sharing text", e);
        }
    }

    public void shareTextToSpecificApps(@NonNull String title, @NonNull String content) {
        List<Intent> targetShareIntents = new ArrayList<>();
        Intent baseIntent = new Intent(Intent.ACTION_SEND);
        baseIntent.setType("text/plain");

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(baseIntent, 0);

        if (!resInfos.isEmpty()) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                if (isTargetPackage(packageName)) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, content);
                    intent.putExtra(Intent.EXTRA_SUBJECT, title);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[0]));
                context.startActivity(chooserIntent);
            } else {
                shareText(title, content);
            }
        }
    }

    private boolean isTargetPackage(@NonNull String pkg) {
        return pkg.contains("com.twitter.android") || pkg.contains("com.facebook.katana") ||
                pkg.contains("com.whatsapp") || pkg.contains("com.google.android.apps.plus") ||
                pkg.contains("com.google.android.talk") || pkg.contains("com.slack") ||
                pkg.contains("com.google.android.gm") || pkg.contains("com.facebook.orca") ||
                pkg.contains("com.yahoo.mobile") || pkg.contains("com.skype.raider") ||
                pkg.contains("com.android.mms") || pkg.contains("com.linkedin.android") ||
                pkg.contains("com.google.android.apps.messaging");
    }

    public void customShare(@NonNull final String subject, @NonNull final String content) {
        PackageManager pm = context.getPackageManager();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        List<ResolveInfo> launchables = pm.queryIntentActivities(shareIntent, 0);
        List<ResolveInfo> filteredLaunchables = new ArrayList<>();

        for (ResolveInfo info : launchables) {
            String label = info.loadLabel(pm).toString().toLowerCase();
            for (String target : targetAppLabels) {
                if (label.contains(target.toLowerCase())) {
                    filteredLaunchables.add(info);
                    break;
                }
            }
        }
        Collections.sort(filteredLaunchables, new ResolveInfo.DisplayNameComparator(pm));

        final CustomShareAdapter adapter = new CustomShareAdapter(context, pm, filteredLaunchables);

        final Dialog dialog = new Dialog(context, R.style.AppDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_share_dialog);
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            int width = context.getResources().getDisplayMetrics().widthPixels - 80;
            int height = context.getResources().getDisplayMetrics().heightPixels - 100;
            dialog.getWindow().setLayout(width, height);
        }

        TextView titleView = dialog.findViewById(R.id.textView1);
        if (titleView != null) {
            titleView.setText(Constant.sharePurchaseTitle);
        }

        ListView listView = dialog.findViewById(R.id.listView1);
        if (listView != null) {
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                dialog.dismiss();
                ResolveInfo info = adapter.getItem(position);
                if (info != null) {
                    ActivityInfo activityInfo = info.activityInfo;
                    ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);

                    Intent specificIntent = new Intent(Intent.ACTION_SEND);
                    specificIntent.setType("text/plain");
                    specificIntent.setComponent(name);
                    specificIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    specificIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    specificIntent.putExtra(Intent.EXTRA_TEXT, content);
                    context.startActivity(specificIntent);
                }
            });
        }
        dialog.show();
    }

    public void sendEmailWithAttachments(@NonNull String[] filePaths) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (String path : filePaths) {
            File file = new File(path);
            if (file.exists() && file.length() > 0) {
                uris.add(FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file));
            }
        }

        if (uris.isEmpty()) return;

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> candidates = pm.queryIntentActivities(emailIntent, 0);
        for (ResolveInfo info : candidates) {
            if (info.activityInfo.packageName.contains("google.android.gm")) {
                emailIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                break;
            }
        }

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (Exception e) {
            Log.e(TAG, "Error sending email", e);
        }
    }

    public void openFacebookPage(@NonNull String fbPageId) {
        String facebookUrl = "https://www.facebook.com/" + fbPageId;
        try {
            int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + facebookUrl)));
            } else {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + fbPageId)));
            }
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }
}
