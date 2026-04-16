package com.trending.water.drinking.reminder;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
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
import com.trending.water.drinking.reminder.databinding.ScreenMonthReportBinding;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Screen_Month_Report extends MasterBaseFragment<ScreenMonthReportBinding> {

    @Override
    protected ScreenMonthReportBinding inflateBinding(LayoutInflater inflater, ViewGroup container) {
        return ScreenMonthReportBinding.inflate(inflater, container, false);
    }

    private static final String TAG = "Screen_Month_Report";
    private final List<String> dateList = new ArrayList<>();
    private final List<Integer> intakeValueList = new ArrayList<>();
    private final List<Integer> goalValueList = new ArrayList<>();
    private Calendar currentStartCalendar;
    private Calendar currentEndCalendar;
    private Calendar startCalendarRef;
    private Calendar endCalendarRef;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initCalendars();
        setupListeners();
        refreshData();
    }

    private void initCalendars() {
        currentStartCalendar = Calendar.getInstance();
        currentStartCalendar.set(Calendar.DAY_OF_MONTH, currentStartCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        resetTime(currentStartCalendar, true);

        currentEndCalendar = Calendar.getInstance();
        currentEndCalendar.set(Calendar.DAY_OF_MONTH, currentEndCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
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
            startCalendarRef.add(Calendar.MONTH, -1);
            startCalendarRef.set(Calendar.DAY_OF_MONTH, startCalendarRef.getActualMinimum(Calendar.DAY_OF_MONTH));
            endCalendarRef.add(Calendar.MONTH, -1);
            endCalendarRef.set(Calendar.DAY_OF_MONTH, endCalendarRef.getActualMaximum(Calendar.DAY_OF_MONTH));
            refreshData();
        });

        binding.imgNext.setOnClickListener(v -> {
            startCalendarRef.add(Calendar.MONTH, 1);
            startCalendarRef.set(Calendar.DAY_OF_MONTH, startCalendarRef.getActualMinimum(Calendar.DAY_OF_MONTH));
            endCalendarRef.add(Calendar.MONTH, 1);
            endCalendarRef.set(Calendar.DAY_OF_MONTH, endCalendarRef.getActualMaximum(Calendar.DAY_OF_MONTH));

            if (startCalendarRef.getTimeInMillis() >= currentEndCalendar.getTimeInMillis()) {
                startCalendarRef.add(Calendar.MONTH, -1);
                startCalendarRef.set(Calendar.DAY_OF_MONTH, startCalendarRef.getActualMinimum(Calendar.DAY_OF_MONTH));
                endCalendarRef.add(Calendar.MONTH, -1);
                endCalendarRef.set(Calendar.DAY_OF_MONTH, endCalendarRef.getActualMaximum(Calendar.DAY_OF_MONTH));
                return;
            }
            refreshData();
        });
    }

    private void refreshData() {
        calculateIntakeStats();
        setupBarChart();
        setupLineChart();
    }

    private void calculateIntakeStats() {
        binding.lblTitle.setText(dateHelper.getDate(startCalendarRef.getTimeInMillis(), "MMM yyyy"));

        dateList.clear();
        intakeValueList.clear();
        goalValueList.clear();

        Calendar tempCal = (Calendar) startCalendarRef.clone();
        while (tempCal.getTimeInMillis() <= endCalendarRef.getTimeInMillis()) {
            dateList.add(dateHelper.getDate(tempCal.getTimeInMillis(), URLFactory.DATE_FORMAT));
            tempCal.add(Calendar.DAY_OF_YEAR, 1);
        }

        double totalDrink = 0;
        double totalGoal = 0;
        int frequencyCounter = 0;
        int activeDayCounter = 0;

        boolean isMl = URLFactory.waterUnitValue.equalsIgnoreCase("ML");

        for (String date : dateList) {
            List<HashMap<String, String>> records = databaseHelper.getData("tbl_drink_details", "DrinkDate ='" + date + "'");
            float dailyTotal = 0.0f;
            float dailyGoal = preferencesHelper.getFloat(URLFactory.KEY_DAILY_WATER_GOAL, 2500.0f);

            for (HashMap<String, String> record : records) {
                float val = Float.parseFloat(isMl ? record.get("ContainerValue") : record.get("ContainerValueOZ"));
                dailyTotal += val;
                if (val > 0) frequencyCounter++;
                dailyGoal = Float.parseFloat(isMl ? record.get("TodayGoal") : record.get("TodayGoalOZ"));
            }

            intakeValueList.add((int) dailyTotal);
            goalValueList.add((int) dailyGoal);

            if (dailyTotal > 0) {
                activeDayCounter++;
                totalGoal += dailyGoal;
            }
            totalDrink += dailyTotal;
        }

        updateStatsText(totalDrink, totalGoal, frequencyCounter, activeDayCounter);
    }

    private void updateStatsText(double totalDrink, double totalGoal, int frequency, int activeDays) {
        String unit = URLFactory.waterUnitValue;
        if (activeDays > 0) {
            int avgIntake = (int) Math.round(totalDrink / activeDays);
            int avgFreq = Math.round((float) frequency / activeDays);
            String freqUnit = avgFreq > 1 ? stringHelper.getString(R.string.times) : stringHelper.getString(R.string.time);

            binding.txtAvgIntake.setText(String.format(Locale.US, "%d %s/%s", avgIntake, unit, stringHelper.getString(R.string.day)));
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
        binding.chart1.setDrawBarShadow(false);
        binding.chart1.setDrawValueAboveBar(false);
        binding.chart1.setHighlightFullBarEnabled(false);
        binding.chart1.setPinchZoom(false);
        binding.chart1.setScaleEnabled(false);

        YAxis leftAxis = binding.chart1.getAxisLeft();
        leftAxis.setTextColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(getMaxChartValue(intakeValueList, goalValueList));

        binding.chart1.getAxisRight().setEnabled(false);

        XAxis xAxis = binding.chart1.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value;
                if (idx >= 0 && idx < dateList.size()) {
                    return dateHelper.formatDateFromString("dd-MM-yyyy", "dd", dateList.get(idx));
                }
                return "";
            }
        });

        binding.chart1.getLegend().setEnabled(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < intakeValueList.size(); i++) {
            entries.add(new BarEntry(i, (float) intakeValueList.get(i)));
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
                if (pos < intakeValueList.size() && intakeValueList.get(pos) > 0) {
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
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setLabelCount(Math.min(dateList.size(), 10), true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value;
                return (idx >= 0 && idx < dateList.size()) ? dateList.get(idx).substring(0, 2) : "";
            }
        });

        YAxis leftAxis = binding.chartNew.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(getMaxChartValue(intakeValueList, goalValueList) + 100);

        binding.chartNew.getAxisRight().setEnabled(false);

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < intakeValueList.size(); i++) {
            entries.add(new Entry(i, (float) intakeValueList.get(i)));
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

        String date = dateList.get(position);
        txtDate.setText(dateHelper.formatDateFromString("dd-MM-yyyy", "dd MMM", date));
        txtGoal.setText(String.format(Locale.US, "%d %s", goalValueList.get(position), URLFactory.waterUnitValue));
        txtConsumed.setText(String.format(Locale.US, "%d %s", intakeValueList.get(position), URLFactory.waterUnitValue));

        List<HashMap<String, String>> records = databaseHelper.getData("tbl_drink_details", "DrinkDate ='" + date + "'");
        String freqLabel = records.size() > 1 ? stringHelper.getString(R.string.times) : stringHelper.getString(R.string.time);
        txtFreq.setText(String.format(Locale.US, "%d %s", records.size(), freqLabel));

        view.findViewById(R.id.img_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.setOnDismissListener(d -> binding.chart1.highlightValue(null));

        dialog.setContentView(view);
        dialog.show();
    }
}
