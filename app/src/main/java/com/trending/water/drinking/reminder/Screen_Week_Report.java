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
import com.trending.water.drinking.reminder.model.History;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Screen_Week_Report extends MasterBaseFragment {

    private static final String TAG = "Screen_Week_Report";
    private final List<String> dateList = new ArrayList<>();
    private final List<Integer> intakeValueList = new ArrayList<>();
    private final List<Integer> goalValueList = new ArrayList<>();
    private final ArrayList<String> weekDaysLabels = new ArrayList<>();
    private final ArrayList<History> histories = new ArrayList<>();
    private BarChart barChart;
    private LineChart lineChart;
    private AppCompatTextView lblTitle;
    private AppCompatTextView txtAvgIntake;
    private AppCompatTextView txtDrinkFrequency;
    private AppCompatTextView txtDrinkCompletion;
    private ImageView imgPre;
    private ImageView imgNext;
    private Calendar currentStartCalendar;
    private Calendar currentEndCalendar;
    private Calendar startCalendarRef;
    private Calendar endCalendarRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_week_report, container, false);

        initLabels();
        findViewByIds(view);
        initCalendars();
        setupListeners();

        refreshData();

        return view;
    }

    private void initLabels() {
        weekDaysLabels.clear();
        weekDaysLabels.add(stringHelper.getString(R.string.sun));
        weekDaysLabels.add(stringHelper.getString(R.string.mon));
        weekDaysLabels.add(stringHelper.getString(R.string.tue));
        weekDaysLabels.add(stringHelper.getString(R.string.wed));
        weekDaysLabels.add(stringHelper.getString(R.string.thu));
        weekDaysLabels.add(stringHelper.getString(R.string.fri));
        weekDaysLabels.add(stringHelper.getString(R.string.sat));
    }

    private void findViewByIds(View view) {
        lineChart = view.findViewById(R.id.chartNew);
        barChart = view.findViewById(R.id.chart1);
        lblTitle = view.findViewById(R.id.lbl_title);
        imgPre = view.findViewById(R.id.img_pre);
        imgNext = view.findViewById(R.id.img_next);
        txtAvgIntake = view.findViewById(R.id.txt_avg_intake);
        txtDrinkFrequency = view.findViewById(R.id.txt_drink_fre);
        txtDrinkCompletion = view.findViewById(R.id.txt_drink_com);
    }

    private void initCalendars() {
        currentStartCalendar = Calendar.getInstance();
        currentStartCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        resetTime(currentStartCalendar, true);

        currentEndCalendar = Calendar.getInstance();
        currentEndCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
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
        imgPre.setOnClickListener(v -> {
            startCalendarRef.add(Calendar.DAY_OF_YEAR, -7);
            endCalendarRef.add(Calendar.DAY_OF_YEAR, -7);
            refreshData();
        });

        imgNext.setOnClickListener(v -> {
            if (startCalendarRef.getTimeInMillis() + (7 * 24 * 60 * 60 * 1000L) >= currentEndCalendar.getTimeInMillis()) {
                return;
            }
            startCalendarRef.add(Calendar.DAY_OF_YEAR, 7);
            endCalendarRef.add(Calendar.DAY_OF_YEAR, 7);
            refreshData();
        });
    }

    private void refreshData() {
        calculateIntakeStats();
        updateUI();
        setupBarChart();
        setupLineChart();
    }

    private void calculateIntakeStats() {
        String startStr = dateHelper.getDate(startCalendarRef.getTimeInMillis(), "dd MMM");
        String endStr = dateHelper.getDate(endCalendarRef.getTimeInMillis(), "dd MMM");
        lblTitle.setText(String.format("%s - %s", startStr, endStr));

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

            txtAvgIntake.setText(String.format(Locale.US, "%d %s/%s", avgIntake, unit, stringHelper.getString(R.string.day)));
            txtDrinkFrequency.setText(String.format(Locale.US, "%d %s/%s", avgFreq, freqUnit, stringHelper.getString(R.string.day)));

            int completion = (int) Math.round((totalDrink * 100.0) / totalGoal);
            txtDrinkCompletion.setText(String.format(Locale.US, "%d%%", completion));
        } else {
            txtAvgIntake.setText(String.format(Locale.US, "0 %s/%s", unit, stringHelper.getString(R.string.day)));
            txtDrinkFrequency.setText(String.format(Locale.US, "0 %s/%s", stringHelper.getString(R.string.time), stringHelper.getString(R.string.day)));
            txtDrinkCompletion.setText("0%");
        }
    }

    private void updateUI() {
        // Any extra UI updates if needed
    }

    private void setupBarChart() {
        barChart.clear();
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setHighlightFullBarEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(getMaxChartValue(intakeValueList, goalValueList));

        barChart.getAxisRight().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value;
                return (idx >= 0 && idx < weekDaysLabels.size()) ? weekDaysLabels.get(idx) : "";
            }
        });

        barChart.getLegend().setEnabled(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < intakeValueList.size(); i++) {
            entries.add(new BarEntry(i, (float) intakeValueList.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(ContextCompat.getColor(mContext, R.color.rdo_gender_select));
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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

        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void setupLineChart() {
        lineChart.clear();
        lineChart.getDescription().setEnabled(false);
        lineChart.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setLabelCount(dateList.size(), true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value;
                return (idx >= 0 && idx < dateList.size()) ? dateList.get(idx).substring(0, 5) : "";
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(getMaxChartValue(intakeValueList, goalValueList) + 100);

        lineChart.getAxisRight().setEnabled(false);

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < intakeValueList.size(); i++) {
            entries.add(new Entry(i, (float) intakeValueList.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        dataSet.setCircleColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, MasterBaseAppCompatActivity.getThemeColorArray(mActivity)));

        LineData data = new LineData(dataSet);
        data.setDrawValues(false);
        lineChart.setData(data);
        lineChart.animateY(1000);
        lineChart.invalidate();
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
        dialog.setOnDismissListener(d -> barChart.highlightValue(null));

        dialog.setContentView(view);
        dialog.show();
    }
}
