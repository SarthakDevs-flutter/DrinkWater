package com.trending.water.drinking.reminder.appbasiclibs.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.R;

import java.util.List;

public class CustomShareAdapter extends ArrayAdapter<ResolveInfo> {
    private final PackageManager packageManager;
    private final LayoutInflater inflater;

    public CustomShareAdapter(@NonNull Context context, @NonNull PackageManager packageManager, @NonNull List<ResolveInfo> apps) {
        super(context, R.layout.custom_share_list, apps);
        this.packageManager = packageManager;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_share_list, parent, false);
            holder = new ViewHolder();
            holder.label = convertView.findViewById(R.id.label);
            holder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ResolveInfo info = getItem(position);
        if (info != null) {
            holder.label.setText(info.loadLabel(packageManager));
            holder.icon.setImageDrawable(info.loadIcon(packageManager));
        }

        return convertView;
    }

    public String getAppName(int position) {
        ResolveInfo info = getItem(position);
        return info != null ? info.loadLabel(packageManager).toString() : "";
    }

    private static class ViewHolder {
        TextView label;
        ImageView icon;
    }
}
