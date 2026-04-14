package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.trending.water.drinking.reminder.adapter.ReportPagerAdapter;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.custom.NonSwipeableViewPager;

public class Screen_Report extends MasterBaseAppCompatActivity {
    
    private AppCompatTextView lblToolbarTitle;
    private LinearLayout leftIconBlock;
    private LinearLayout rightIconBlock;
    private RadioButton rdoMonth;
    private RadioButton rdoWeek;
    private RadioButton rdoYear;
    private TabLayout tabs;
    private NonSwipeableViewPager viewPager;
    
    private ReportPagerAdapter reportPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_report);
        findViewByIds();
        initView();
    }

    private void findViewByIds() {
        rightIconBlock = findViewById(R.id.right_icon_block);
        leftIconBlock = findViewById(R.id.left_icon_block);
        lblToolbarTitle = findViewById(R.id.lbl_toolbar_title);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        rdoWeek = findViewById(R.id.rdo_week);
        rdoMonth = findViewById(R.id.rdo_month);
        rdoYear = findViewById(R.id.rdo_year);
    }

    private void initView() {
        lblToolbarTitle.setText(stringHelper.getString(R.string.str_drink_report));
        leftIconBlock.setOnClickListener(v -> finish());
        rightIconBlock.setVisibility(View.GONE);

        rdoWeek.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_week)));
        rdoMonth.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_month)));
        rdoYear.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_year)));

        rdoWeek.setOnClickListener(v -> viewPager.setCurrentItem(0));
        rdoMonth.setOnClickListener(v -> viewPager.setCurrentItem(1));
        rdoYear.setOnClickListener(v -> viewPager.setCurrentItem(2));

        reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), mContext);
        viewPager.setAdapter(reportPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        
        tabs.setupWithViewPager(viewPager);
        
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: rdoWeek.setChecked(true); break;
                    case 1: rdoMonth.setChecked(true); break;
                    case 2: rdoYear.setChecked(true); break;
                }
            }
        });
    }
}
