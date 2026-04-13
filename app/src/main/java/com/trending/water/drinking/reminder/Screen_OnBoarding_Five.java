package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trending.water.drinking.reminder.base.MasterBaseFragment;

public class Screen_OnBoarding_Five extends MasterBaseFragment {
    View item_view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_onboarding_five, container, false);
        FindViewById();
        Body();
        return this.item_view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void FindViewById() {
    }

    private void Body() {
    }
}
