package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.trending.water.drinking.reminder.adapter.ReportPagerAdapter;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.databinding.ScreenReportBinding;

public class Screen_Report extends MasterBaseAppCompatActivity<ScreenReportBinding> {

    private ReportPagerAdapter reportPagerAdapter;

    @Override
    protected ScreenReportBinding inflateBinding(LayoutInflater inflater) {
        return ScreenReportBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding.include1.lblToolbarTitle.setText(stringHelper.getString(R.string.str_drink_report));
        binding.include1.leftIconBlock.setOnClickListener(v -> finish());
        binding.include1.rightIconBlock.setVisibility(View.GONE);

        binding.rdoWeek.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_week)));
        binding.rdoMonth.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_month)));
        binding.rdoYear.setText(stringHelper.capitalizeFirst(stringHelper.getString(R.string.str_year)));

        binding.rdoWeek.setOnClickListener(v -> binding.viewPager.setCurrentItem(0));
        binding.rdoMonth.setOnClickListener(v -> binding.viewPager.setCurrentItem(1));
        binding.rdoYear.setOnClickListener(v -> binding.viewPager.setCurrentItem(2));

        reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), mContext);
        binding.viewPager.setAdapter(reportPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(5);

        binding.tabs.setupWithViewPager(binding.viewPager);

        binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.rdoWeek.setChecked(true);
                        break;
                    case 1:
                        binding.rdoMonth.setChecked(true);
                        break;
                    case 2:
                        binding.rdoYear.setChecked(true);
                        break;
                }
            }
        });
    }
}
