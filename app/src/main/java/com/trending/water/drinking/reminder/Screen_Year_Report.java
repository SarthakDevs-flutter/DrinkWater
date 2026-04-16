package com.trending.water.drinking.reminder;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.databinding.ScreenYearReportBinding;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Screen_Year_Report extends MasterBaseFragment<ScreenYearReportBinding> {

    private static final String TAG = "Screen_Year_Report";
    private final List<String> monthLabelList = new ArrayList<>();
    private final List<String> monthQueryList = new ArrayList<>();
    private final List<Integer> intakeAvgValueList = new ArrayList<>();
    private final List<Integer> goalAvgValueList = new ArrayList<>();
    private Calendar currentStartCalendar;
    private Calendar currentEndCalendar;
    private Calendar startCalendarRef;
    private Calendar endCalendarRef;
    private String[] monthNames;
    private String[] monthNamesShort;

    @Override
    protected ScreenYearReportBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenYearReportBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLabels();
        initCalendars();
        setupListeners();
        refreshData();
    }

    private void initLabels() {
        monthNames = stringHelper.getStringArray(R.array.month_list2);
        monthNamesShort = stringHelper.getStringArray(R.array.month_list);
    }


    private void initCalendars() {
        currentStartCalendar = Calendar.getInstance();
        currentStartCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        currentStartCalendar.set(Calendar.DAY_OF_MONTH, 1);
        resetTime(currentStartCalendar, true);

        currentEndCalendar = Calendar.getInstance();
        currentEndCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        currentEndCalendar.set(Calendar.DAY_OF_MONTH, 31);
        resetTime(currentEndCalendar, false);

        startCalendarRef = (Calendar) currentStartCalendar.clone();
        endCalendarRef = (Calendar) currentEndCalendar.clone();
    }

    private void resetTime(Calendar cal, boolean isStart) {
        if (isStart) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        }
    }

    private void setupListeners() {
        binding.imgPre.setOnClickListener(v -> {
            startCalendarRef.add(Calendar.YEAR, -1);
            endCalendarRef.add(Calendar.YEAR, -1);
            refreshData();
        });

        binding.imgNext.setOnClickListener(v -> {
            if (startCalendarRef.getTimeInMillis() + (365L * 24 * 60 * 60 * 1000L) >= currentEndCalendar.getTimeInMillis()) {
                return;
            }
            startCalendarRef.add(Calendar.YEAR, 1);
            endCalendarRef.add(Calendar.YEAR, 1);
            refreshData();
        });
    }

    private void refreshData() {
        calculateIntakeStats();
        setupBarChart();
        setupLineChart();
    }

    private void calculateIntakeStats() {
        binding.lblTitle.setText(dateHelper.getDate(startCalendarRef.getTimeInMillis(), "yyyy"));

        monthLabelList.clear();
        monthQueryList.clear();
        intakeAvgValueList.clear();
        goalAvgValueList.clear();

        Calendar tempCal = (Calendar) startCalendarRef.clone();
        for (int i = 0; i < 12; i++) {
            monthLabelList.add(monthNames[tempCal.get(Calendar.MONTH)] + "-" + tempCal.get(Calendar.YEAR));
            monthQueryList.add(stringHelper.get_2_point(String.valueOf(tempCal.get(Calendar.MONTH) + 1)) + "-" + tempCal.get(Calendar.YEAR));
            tempCal.add(Calendar.MONTH, 1);
        }

        double totalDrinkAgg = 0;
        double totalGoalAgg = 0;
        int totalFrequency = 0;
        int totalDayCounter = 0;

        boolean isMl = URLFactory.waterUnitValue.equalsIgnoreCase("ML");

        for (String query : monthQueryList) {
            // "DrinkDate like '%MM-YYYY%'"
            List<HashMap<String, String>> records = databaseHelper.getData("tbl_drink_details", "DrinkDate like '%" + query + "%'");

            float monthIntakeTotal = 0;
            float monthGoalTotal = 0;
            Set<String> uniqueDates = new HashSet<>();
            int monthFrequency = 0;

            for (HashMap<String, String> record : records) {
                float val = Float.parseFloat(isMl ? record.get("ContainerValue") : record.get("ContainerValueOZ"));
                monthIntakeTotal += val;
                if (val > 0) monthFrequency++;
                uniqueDates.add(record.get("DrinkDate"));
            }

            for (String uniqueDate : uniqueDates) {
                List<HashMap<String, String>> dayRecords = databaseHelper.getData("tbl_drink_details", "DrinkDate='" + uniqueDate + "'");
                if (!dayRecords.isEmpty()) {
                    monthGoalTotal += Float.parseFloat(isMl ? dayRecords.get(0).get("TodayGoal") : dayRecords.get(0).get("TodayGoalOZ"));
                }
            }

            int avgIntake = uniqueDates.isEmpty() ? 0 : Math.round(monthIntakeTotal / uniqueDates.size());
            int avgGoal = uniqueDates.isEmpty() ? (int) preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 2500.0f) : Math.round(monthGoalTotal / uniqueDates.size());

            intakeAvgValueList.add(avgIntake);
            goalAvgValueList.add(avgGoal);

            totalDrinkAgg += monthIntakeTotal;
            totalGoalAgg += monthGoalTotal;
            totalFrequency += monthFrequency;
            totalDayCounter += uniqueDates.size();
        }

        updateStatsText(totalDrinkAgg, totalGoalAgg, totalFrequency, totalDayCounter, isMl);
    }

    private void updateStatsText(double totalDrink, double totalGoal, int frequency, int activeDays, boolean isMl) {
        String unit = URLFactory.waterUnitValue;
        if (activeDays > 0) {
            int avgIntake = (int) Math.round(totalDrink / activeDays);
            int avgFreq = Math.round((float) frequency / activeDays);
            String freqUnit = avgFreq > 1 ? stringHelper.getString(R.string.times) : stringHelper.getString(R.string.time);

            binding.txtAvgIntake.setText(String.format(Locale.US, "%d %s/%s", avgIntake, unit, stringHelper.getString(R.string.day)));
            binding.txtDrinkFre.setText(String.format(Locale.US, "%d %s/%s", avgFreq, freqUnit, stringHelper.getString(R.string.day)));

            int completion = (int) Math.round((totalDrink * 100.0) / totalGoal);
            binding.txtDrinkCom.setText(String.format(Locale.US, "%d%%", completion));
        } else {
            binding.txtAvgIntake.setText(String.format(Locale.US, "0 %s/%s", unit, stringHelper.getString(R.string.day)));
            binding.txtDrinkFre.setText(String.format(Locale.US, "0 %s/%s", stringHelper.getString(R.string.time), stringHelper.getString(R.string.day)));
            binding.txtDrinkCom.setText("0%");
        }
    }

    private void setupBarChart() {
        binding.chart1.clear();
        binding.chart1.getDescription().setEnabled(false);
        binding.chart1.setDrawGridBackground(false);
        binding.chart1.setDrawValueAboveBar(false);
        binding.chart1.setPinchZoom(false);
        binding.chart1.setScaleEnabled(false);

        YAxis leftAxis = binding.chart1.getAxisLeft();
        leftAxis.setTextColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(getMaxChartValue(intakeAvgValueList, goalAvgValueList));

        binding.chart1.getAxisRight().setEnabled(false);

        XAxis xAxis = binding.chart1.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        xAxis.setLabelRotationAngle(-90f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value;
                return (idx >= 0 && idx < monthNamesShort.length) ? monthNamesShort[idx] : "";
            }
        });

        binding.chart1.getLegend().setEnabled(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < intakeAvgValueList.size(); i++) {
            entries.add(new BarEntry(i, (float) intakeAvgValueList.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        binding.chart1.setData(data);
        binding.chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos = (int) e.getX();
                if (pos < intakeAvgValueList.size() && intakeAvgValueList.get(pos) > 0) {
                    showDayDetailsDialog(pos);
                }
            }

            @Override
            public void onNothingSelected() {
            }
        });

        binding.chart1.animateY(1000);
        binding.chart1.invalidate();
    }

    private void setupLineChart() {
        binding.chartNew.clear();
        binding.chartNew.getDescription().setEnabled(false);
        binding.chartNew.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));

        XAxis xAxis = binding.chartNew.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-90f);
        xAxis.setLabelCount(monthLabelList.size(), true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value;
                return (idx >= 0 && idx < monthLabelList.size()) ? monthLabelList.get(idx) : "";
            }
        });

        YAxis leftAxis = binding.chartNew.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(getMaxChartValue(intakeAvgValueList, goalAvgValueList) + 100);

        binding.chartNew.getAxisRight().setEnabled(false);

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < intakeAvgValueList.size(); i++) {
            entries.add(new Entry(i, (float) intakeAvgValueList.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        dataSet.setCircleColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        dataSet.setLineWidth(1.5f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, MasterBaseAppCompatActivity.getThemeColorArray(mActivity)));

        LineData data = new LineData(dataSet);
        data.setDrawValues(false);
        binding.chartNew.setData(data);
        binding.chartNew.animateY(1000);
        binding.chartNew.invalidate();
    }

    private float getMaxChartValue(List<Integer> intakes, List<Integer> goals) {
        float max = 0;
        for (int val : intakes) if (val > max) max = val;
        for (int val : goals) if (val > max) max = val;

        int unit = URLFactory.waterUnitValue.equalsIgnoreCase("ML") ? 1000 : 35;
        if (max < 1) return (float) (unit * 3);

        return (float) ((((int) (max / unit)) + 1) * unit);
    }

    private void showDayDetailsDialog(int position) {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_day_info_chart, null, false);
        AppCompatTextView txtDate = view.findViewById(R.id.txt_date);
        AppCompatTextView txtGoal = view.findViewById(R.id.txt_goal);
        AppCompatTextView txtConsumed = view.findViewById(R.id.txt_consumed);
        AppCompatTextView txtFreq = view.findViewById(R.id.txt_frequency);

        String label = monthLabelList.get(position);
        txtDate.setText(label);

        String unit = URLFactory.waterUnitValue;
        txtGoal.setText(String.format(Locale.US, "%d %s/%s", goalAvgValueList.get(position), unit, stringHelper.getString(R.string.day)));
        txtConsumed.setText(String.format(Locale.US, "%d %s/%s", intakeAvgValueList.get(position), unit, stringHelper.getString(R.string.day)));

        String query = monthQueryList.get(position);
        List<HashMap<String, String>> records = databaseHelper.getData("tbl_drink_details", "DrinkDate like '%" + query + "%'");
        Set<String> uniqueDates = new HashSet<>();
        for (HashMap<String, String> r : records) uniqueDates.add(r.get("DrinkDate"));

        if (!uniqueDates.isEmpty()) {
            int avgFreq = Math.round((float) records.size() / uniqueDates.size());
            String freqLabel = avgFreq > 1 ? stringHelper.getString(R.string.times) : stringHelper.getString(R.string.time);
            txtFreq.setText(String.format(Locale.US, "%d %s/%s", avgFreq, freqLabel, stringHelper.getString(R.string.day)));
        } else {
            txtFreq.setText(String.format(Locale.US, "0 %s/%s", stringHelper.getString(R.string.time), stringHelper.getString(R.string.day)));
        }

        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setOnDismissListener(d -> binding.chart1.highlightValue(null));

        dialog.setContentView(view);
        dialog.show();
    }
}
