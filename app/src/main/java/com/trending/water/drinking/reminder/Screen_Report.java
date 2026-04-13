package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.trending.water.drinking.reminder.adapter.ReportPagerAdapter;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.custom.NonSwipeableViewPager;

public class Screen_Report extends MasterBaseAppCompatActivity {
    AppCompatTextView lbl_toolbar_title;
    LinearLayout left_icon_block;
    RadioButton rdo_month;
    RadioButton rdo_week;
    RadioButton rdo_year;
    ReportPagerAdapter reportPagerAdapter;
    LinearLayout right_icon_block;
    TabLayout tabs;
    NonSwipeableViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.screen_report);
        FindViewById();
        Body();
    }

    private void FindViewById() {
        this.right_icon_block = (LinearLayout) findViewById(R.id.right_icon_block);
        this.left_icon_block = (LinearLayout) findViewById(R.id.left_icon_block);
        this.lbl_toolbar_title = (AppCompatTextView) findViewById(R.id.lbl_toolbar_title);
        this.tabs = (TabLayout) findViewById(R.id.tabs);
        this.viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        this.rdo_week = (RadioButton) findViewById(R.id.rdo_week);
        this.rdo_month = (RadioButton) findViewById(R.id.rdo_month);
        this.rdo_year = (RadioButton) findViewById(R.id.rdo_year);
        this.rdo_week.setText(this.sh.firstLetterCaps(this.sh.get_string(R.string.str_week)));
        this.rdo_month.setText(this.sh.firstLetterCaps(this.sh.get_string(R.string.str_month)));
        this.rdo_year.setText(this.sh.firstLetterCaps(this.sh.get_string(R.string.str_year)));
        this.rdo_week.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Report.this.viewPager.setCurrentItem(0);
            }
        });
        this.rdo_month.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Report.this.viewPager.setCurrentItem(1);
            }
        });
        this.rdo_year.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Report.this.viewPager.setCurrentItem(2);
            }
        });
    }

    private void Body() {
        this.lbl_toolbar_title.setText(this.sh.get_string(R.string.str_drink_report));
        this.left_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_Report.this.finish();
            }
        });
        this.right_icon_block.setVisibility(View.GONE);
        this.tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        this.reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), this.mContext);
        this.viewPager.setAdapter(this.reportPagerAdapter);
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.viewPager.setOffscreenPageLimit(5);
        this.tabs.setupWithViewPager(this.viewPager);
    }
}
