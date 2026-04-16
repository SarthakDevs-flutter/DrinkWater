package com.trending.water.drinking.reminder.appbasiclibs;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {
    protected VB binding;

    protected abstract VB inflateBinding(LayoutInflater inflater, ViewGroup container);

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = getActivity();

        if (this.mActivity != null) {
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

            this.utilityFunction.permissionStrictMode();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = inflateBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
