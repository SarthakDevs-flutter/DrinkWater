package com.trending.water.drinking.reminder.base;

import android.content.Context;

import com.trending.water.drinking.reminder.appbasiclibs.BaseFragment;
import com.trending.water.drinking.reminder.utils.DB_Helper;

public class MasterBaseFragment extends BaseFragment {
    DB_Helper dbh;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.dbh = new DB_Helper(getActivity(), getActivity());
    }
}
