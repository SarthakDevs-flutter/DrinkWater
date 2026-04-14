package com.trending.water.drinking.reminder.base;

import android.content.Context;

import androidx.annotation.NonNull;

import com.trending.water.drinking.reminder.appbasiclibs.BaseFragment;
import com.trending.water.drinking.reminder.utils.DbHelper;

public class MasterBaseFragment extends BaseFragment {
    protected DbHelper dbHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            this.dbHelper = new DbHelper(getActivity(), getActivity());
        }
    }
}
