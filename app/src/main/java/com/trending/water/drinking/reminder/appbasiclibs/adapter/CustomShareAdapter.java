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

import com.trending.water.drinking.reminder.R;

import java.util.ArrayList;
import java.util.List;

public class CustomShareAdapter extends ArrayAdapter<ResolveInfo> {
    ArrayList<String> app_name = new ArrayList<>();
    Context context;
    private PackageManager pm = null;

    public CustomShareAdapter(Context ctx, PackageManager pm2, List<ResolveInfo> apps) {
        super(ctx, R.layout.custom_share_list, apps);
        this.pm = pm2;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = newView(parent);
        }
        bindView(position, convertView);
        return convertView;
    }

    private View newView(ViewGroup parent) {
        return ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_share_list, parent, false);
    }

    public ArrayList<String> getAppName() {
        return this.app_name;
    }

    public String get_app_name(int position) {
        return "" + ((ResolveInfo) getItem(position)).loadLabel(this.pm);
    }

    private void bindView(int position, View row) {
        ArrayList<String> arrayList = this.app_name;
        arrayList.add("" + ((ResolveInfo) getItem(position)).loadLabel(this.pm));
        ((TextView) row.findViewById(R.id.label)).setText(((ResolveInfo) getItem(position)).loadLabel(this.pm));
        ((ImageView) row.findViewById(R.id.icon)).setImageDrawable(((ResolveInfo) getItem(position)).loadIcon(this.pm));
    }
}
