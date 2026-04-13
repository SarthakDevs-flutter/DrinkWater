package com.trending.water.drinking.reminder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.DashPathEffect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.trending.water.drinking.reminder.adapter.HistoryAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.String_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseAppCompatActivity;
import com.trending.water.drinking.reminder.base.MasterBaseFragment;
import com.trending.water.drinking.reminder.model.History;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Screen_Week_Report extends MasterBaseFragment {
    private final RectF onValueSelectedRectF = new RectF();
    HistoryAdapter adapter;
    BottomSheetDialog bottomSheetDialog;
    BarChart chart;
    LineChart chartNew;
    Calendar current_end_calendar;
    Calendar current_start_calendar;
    Calendar end_calendarN;
    ArrayList<History> histories = new ArrayList<>();
    ImageView img_next;
    ImageView img_pre;
    View item_view;
    AppCompatTextView lbl_title;
    List<String> lst_date = new ArrayList();
    List<Integer> lst_date_goal_val = new ArrayList();
    List<Integer> lst_date_goal_val_2 = new ArrayList();
    List<Integer> lst_date_val = new ArrayList();
    ArrayList<String> lst_week = new ArrayList<>();
    Calendar start_calendarN;
    AppCompatTextView txt_avg_intake;
    AppCompatTextView txt_drink_com;
    AppCompatTextView txt_drink_fre;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.item_view = inflater.inflate(R.layout.screen_week_report, container, false);
        this.lst_week.clear();
        this.lst_week.add(this.sh.get_string(R.string.sun));
        this.lst_week.add(this.sh.get_string(R.string.mon));
        this.lst_week.add(this.sh.get_string(R.string.tue));
        this.lst_week.add(this.sh.get_string(R.string.wed));
        this.lst_week.add(this.sh.get_string(R.string.thu));
        this.lst_week.add(this.sh.get_string(R.string.fri));
        this.lst_week.add(this.sh.get_string(R.string.sat));
        FindViewById();
        setCurrentWeekInfo();
        Body();
        return this.item_view;
    }

    private void FindViewById() {
        this.chartNew = (LineChart) this.item_view.findViewById(R.id.chartNew);
        this.chart = (BarChart) this.item_view.findViewById(R.id.chart1);
        this.lbl_title = (AppCompatTextView) this.item_view.findViewById(R.id.lbl_title);
        this.img_pre = (ImageView) this.item_view.findViewById(R.id.img_pre);
        this.img_next = (ImageView) this.item_view.findViewById(R.id.img_next);
        this.txt_avg_intake = (AppCompatTextView) this.item_view.findViewById(R.id.txt_avg_intake);
        this.txt_drink_fre = (AppCompatTextView) this.item_view.findViewById(R.id.txt_drink_fre);
        this.txt_drink_com = (AppCompatTextView) this.item_view.findViewById(R.id.txt_drink_com);
    }

    public void setCurrentWeekInfo() {
        this.current_start_calendar = Calendar.getInstance();
        this.current_start_calendar.set(7, 1);
        this.current_start_calendar.set(11, 0);
        this.current_start_calendar.set(12, 0);
        this.current_start_calendar.set(13, 0);
        this.current_start_calendar.set(14, 0);
        this.current_end_calendar = Calendar.getInstance();
        this.current_end_calendar.set(7, 7);
        this.current_end_calendar.set(11, 23);
        this.current_end_calendar.set(12, 59);
        this.current_end_calendar.set(13, 59);
        this.current_end_calendar.set(14, 999);
    }

    private void Body() {
        this.start_calendarN = Calendar.getInstance();
        this.start_calendarN.set(7, 1);
        this.start_calendarN.set(11, 0);
        this.start_calendarN.set(12, 0);
        this.start_calendarN.set(13, 0);
        this.start_calendarN.set(14, 0);
        this.end_calendarN = Calendar.getInstance();
        this.end_calendarN.set(7, 7);
        this.end_calendarN.set(11, 23);
        this.end_calendarN.set(12, 59);
        this.end_calendarN.set(13, 59);
        this.end_calendarN.set(14, 999);
        Log.d("MIN_MAX_WEEK_DATE : ", "" + this.start_calendarN.getTimeInMillis() + " @@@ " + this.end_calendarN.getTimeInMillis());
        loadData(this.start_calendarN, this.end_calendarN);
        this.img_pre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Week_Report.this.start_calendarN.add(5, -7);
                Screen_Week_Report.this.end_calendarN.add(5, -7);
                Screen_Week_Report.this.loadData(Screen_Week_Report.this.start_calendarN, Screen_Week_Report.this.end_calendarN);
                Screen_Week_Report.this.generateBarDataNew();
            }
        });
        this.img_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Screen_Week_Report.this.start_calendarN.add(5, 7);
                Screen_Week_Report.this.end_calendarN.add(5, 7);
                Log.d("MIN_MAX_DATE 2.1 : ", "" + Screen_Week_Report.this.start_calendarN.getTimeInMillis() + " @@@ " + Screen_Week_Report.this.end_calendarN.getTimeInMillis());
                if (Screen_Week_Report.this.start_calendarN.getTimeInMillis() >= Screen_Week_Report.this.current_end_calendar.getTimeInMillis()) {
                    Screen_Week_Report.this.start_calendarN.add(5, -7);
                    Screen_Week_Report.this.end_calendarN.add(5, -7);
                    return;
                }
                Screen_Week_Report.this.loadData(Screen_Week_Report.this.start_calendarN, Screen_Week_Report.this.end_calendarN);
                Screen_Week_Report.this.generateBarDataNew();
            }
        });
        generateDataNew();
        generateBarDataNew();
    }

    public void loadData(Calendar start_calendar2, Calendar end_calendar2) {
        Calendar start_calendar;
        Calendar end_calendar;
        float tot;
        String last_goal;
        Calendar start_calendar3 = Calendar.getInstance();
        start_calendar3.setTimeInMillis(start_calendar2.getTimeInMillis());
        Calendar end_calendar3 = Calendar.getInstance();
        end_calendar3.setTimeInMillis(end_calendar2.getTimeInMillis());
        AppCompatTextView appCompatTextView = this.lbl_title;
        StringBuilder sb = new StringBuilder();
        Date_Helper date_Helper = this.dth;
        sb.append(Date_Helper.getDate(start_calendar3.getTimeInMillis(), "dd MMM"));
        sb.append(" - ");
        Date_Helper date_Helper2 = this.dth;
        sb.append(Date_Helper.getDate(end_calendar3.getTimeInMillis(), "dd MMM"));
        appCompatTextView.setText(sb.toString());
        this.lst_date.clear();
        this.lst_date_goal_val.clear();
        this.lst_date_val.clear();
        this.lst_date_goal_val_2.clear();
        do {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.sh.get_2_point("" + start_calendar3.get(5)));
            sb2.append("-");
            sb2.append(this.sh.get_2_point("" + (start_calendar3.get(2) + 1)));
            sb2.append("-");
            sb2.append(start_calendar3.get(1));
            Log.d("DATE2 : ", sb2.toString());
            List<String> list = this.lst_date;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(this.sh.get_2_point("" + start_calendar3.get(5)));
            sb3.append("-");
            sb3.append(this.sh.get_2_point("" + (start_calendar3.get(2) + 1)));
            sb3.append("-");
            sb3.append(start_calendar3.get(1));
            list.add(sb3.toString());
            start_calendar3.add(5, 1);
        } while (start_calendar3.getTimeInMillis() <= end_calendar3.getTimeInMillis());
        double total_drink = Utils.DOUBLE_EPSILON;
        double total_goal = 0.0d;
        int frequency_counter = 0;
        int day_counter = 0;
        int i = 0;
        while (i < this.lst_date.size()) {
            ArrayList<HashMap<String, String>> arr_data = this.dh.getdata("tbl_drink_details", "DrinkDate ='" + this.lst_date.get(i) + "'");
            float tot2 = 0.0f;
            String last_goal2 = "0";
            int frequency_counter2 = frequency_counter;
            int j = 0;
            while (j < arr_data.size()) {
                if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                    start_calendar = start_calendar3;
                    end_calendar = end_calendar3;
                    tot = (float) (((double) tot2) + Double.parseDouble((String) arr_data.get(j).get("ContainerValue")));
                    last_goal = (String) arr_data.get(j).get("TodayGoal");
                    if (Double.parseDouble((String) arr_data.get(j).get("ContainerValue")) > Utils.DOUBLE_EPSILON) {
                        frequency_counter2++;
                    }
                } else {
                    start_calendar = start_calendar3;
                    end_calendar = end_calendar3;
                    tot = (float) (((double) tot2) + Double.parseDouble((String) arr_data.get(j).get("ContainerValueOZ")));
                    last_goal = (String) arr_data.get(j).get("TodayGoalOZ");
                    if (Double.parseDouble((String) arr_data.get(j).get("ContainerValueOZ")) > Utils.DOUBLE_EPSILON) {
                        frequency_counter2++;
                    }
                }
                tot2 = tot;
                last_goal2 = last_goal;
                j++;
                start_calendar3 = start_calendar;
                end_calendar3 = end_calendar;
            }
            Calendar start_calendar4 = start_calendar3;
            Calendar end_calendar4 = end_calendar3;
            this.lst_date_val.add(Integer.valueOf((int) tot2));
            if (tot2 > 0.0f) {
                day_counter++;
                total_goal += (double) Float.parseFloat(last_goal2);
            }
            total_drink += (double) tot2;
            if (tot2 == 0.0f && Math.round(Float.parseFloat(last_goal2)) == 0) {
                int ii = (int) this.ph.getFloat(URLFactory.DAILY_WATER);
                this.lst_date_goal_val.add(Integer.valueOf(ii));
                this.lst_date_goal_val_2.add(Integer.valueOf(ii));
            } else if (tot2 > ((float) Math.round(Float.parseFloat(last_goal2)))) {
                this.lst_date_goal_val.add(0);
                this.lst_date_goal_val_2.add(Integer.valueOf(Math.round(Float.parseFloat(last_goal2))));
            } else {
                this.lst_date_goal_val.add(Integer.valueOf(Math.round(Float.parseFloat(last_goal2)) - ((int) tot2)));
                this.lst_date_goal_val_2.add(Integer.valueOf(Math.round(Float.parseFloat(last_goal2))));
            }
            i++;
            frequency_counter = frequency_counter2;
            start_calendar3 = start_calendar4;
            end_calendar3 = end_calendar4;
        }
        Calendar calendar = end_calendar3;
        try {
            int avg = (int) Math.round(total_drink / ((double) day_counter));
            int avg_fre = Math.round(Float.parseFloat("" + frequency_counter) / Float.parseFloat("" + day_counter));
            String str = avg_fre > 1 ? this.sh.get_string(R.string.times) : this.sh.get_string(R.string.time);
            this.txt_avg_intake.setText("" + avg + " " + URLFactory.WATER_UNIT_VALUE + "/" + this.sh.get_string(R.string.day));
            this.txt_drink_fre.setText("" + avg_fre + " " + str + "/" + this.sh.get_string(R.string.day));
        } catch (Exception e) {
            this.txt_avg_intake.setText("0 " + URLFactory.WATER_UNIT_VALUE + "/" + this.sh.get_string(R.string.day));
            this.txt_drink_fre.setText("0 " + this.sh.get_string(R.string.time) + "/" + this.sh.get_string(R.string.day));
        }
        try {
            int avg_com = (int) Math.round((100.0d * total_drink) / total_goal);
            this.txt_drink_com.setText("" + avg_com + "%");
        } catch (Exception e2) {
            this.txt_drink_com.setText("0%");
        }
        Log.d("lst_date_val : ", "" + new Gson().toJson((Object) this.lst_date_val));
        Log.d("lst_date_val 2 : ", "" + new Gson().toJson((Object) this.lst_date_goal_val));
    }

    public void generateBarDataNew() {
        this.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onValueSelected(Entry e, Highlight h) {
                if (e != null) {
                    try {
                        if (Screen_Week_Report.this.lst_date_val.get((int) e.getX()).intValue() > 0) {
                            Screen_Week_Report.this.showDayDetailsDialog((int) e.getX());
                        }
                    } catch (Exception e2) {
                    }
                }
            }

            public void onNothingSelected() {
            }
        });
        this.chart.clear();
        this.chart.getDescription().setEnabled(false);
        this.chart.setMaxVisibleValueCount(40);
        this.chart.setDrawGridBackground(false);
        this.chart.setDrawBarShadow(false);
        this.chart.setDrawValueAboveBar(false);
        this.chart.setHighlightFullBarEnabled(false);
        YAxis leftAxis = this.chart.getAxisLeft();
        leftAxis.setTextColor(this.mContext.getResources().getColor(R.color.rdo_gender_select));
        leftAxis.setAxisMaximum(getMaxBarGraphVal());
        leftAxis.setAxisMinimum(0.0f);
        this.chart.getAxisRight().setEnabled(false);
        this.chart.setExtraBottomOffset(20.0f);
        XAxis xLabels = this.chart.getXAxis();
        xLabels.setDrawGridLines(false);
        xLabels.setGranularityEnabled(false);
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setTextColor(this.mContext.getResources().getColor(R.color.rdo_gender_select));
        xLabels.setValueFormatter(new ValueFormatter() {
            public String getFormattedValue(float value) {
                try {
                    if (Screen_Week_Report.this.lst_week.size() > ((int) value)) {
                        return Screen_Week_Report.this.lst_week.get((int) value);
                    }
                    return "N/A";
                } catch (Exception e) {
                    return "N/A";
                }
            }
        });
        Legend l = this.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8.0f);
        l.setFormToTextSpace(4.0f);
        l.setXEntrySpace(6.0f);
        l.setEnabled(false);
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < this.lst_date.size(); i++) {
            float val1 = (float) this.lst_date_val.get(i).intValue();
            Log.d("========", "" + val1 + " @@@ " + ((float) this.lst_date_goal_val.get(i).intValue()));
            values.add(new BarEntry((float) i, val1, getResources().getDrawable(R.drawable.ic_launcher_background)));
        }
        if (this.chart.getData() == null || ((BarData) this.chart.getData()).getDataSetCount() <= 0) {
            BarDataSet set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setHighLightAlpha(50);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData((List<IBarDataSet>) dataSets);
            data.setValueFormatter(new ValueFormatter() {
                public String getFormattedValue(float value) {
                    if (value == 0.0f) {
                        return "";
                    }
                    int k = 0;
                    while (k < Screen_Week_Report.this.lst_date_goal_val.size()) {
                        try {
                            if (Screen_Week_Report.this.lst_date_goal_val.get(k).intValue() == ((int) value)) {
                                return "" + Screen_Week_Report.this.lst_date_goal_val_2.get(k);
                            }
                            k++;
                        } catch (Exception e) {
                        }
                    }
                    return "" + ((int) value);
                }
            });
            data.setValueTextColor(this.mContext.getResources().getColor(R.color.btn_back));
            set1.setDrawValues(false);
            set1.setValueTextSize(10.0f);
            this.chart.setData(data);
        } else {
            BarDataSet set12 = (BarDataSet) ((BarData) this.chart.getData()).getDataSetByIndex(0);
            set12.setValues(values);
            set12.setHighLightAlpha(50);
            set12.setDrawValues(false);
            ((BarData) this.chart.getData()).notifyDataChanged();
            this.chart.notifyDataSetChanged();
        }
        this.chart.animateY(1500);
        this.chart.setPinchZoom(false);
        this.chart.setScaleEnabled(false);
        this.chart.invalidate();
    }

    public float getMaxBarGraphVal() {
        float drink_val = 0.0f;
        for (int k = 0; k < this.lst_date_val.size(); k++) {
            if (k == 0) {
                drink_val = Float.parseFloat("" + this.lst_date_val.get(k));
            } else {
                if (drink_val < Float.parseFloat("" + this.lst_date_val.get(k))) {
                    drink_val = Float.parseFloat("" + this.lst_date_val.get(k));
                }
            }
        }
        float goal_val = 0.0f;
        for (int k2 = 0; k2 < this.lst_date_goal_val_2.size(); k2++) {
            if (k2 == 0) {
                goal_val = Float.parseFloat("" + this.lst_date_goal_val_2.get(k2));
            } else {
                if (goal_val < Float.parseFloat("" + this.lst_date_goal_val_2.get(k2))) {
                    goal_val = Float.parseFloat("" + this.lst_date_goal_val_2.get(k2));
                }
            }
        }
        int singleUnit = URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml") ? 1000 : 35;
        float max_val = drink_val < goal_val ? goal_val : drink_val;
        if (drink_val < 1.0f) {
            max_val = (float) (singleUnit * 3);
        }
        return (float) ((((int) (max_val / ((float) singleUnit))) + 1) * singleUnit);
    }

    private int[] getColors() {
        return new int[]{this.mContext.getResources().getColor(R.color.rdo_gender_select)};
    }

    public void showDayDetailsDialog(final int position) {
        String_Helper string_Helper;
        int i;
        final Dialog dialog = new Dialog(this.act);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(16);
        View view = LayoutInflater.from(this.act).inflate(R.layout.dialog_day_info_chart, (ViewGroup) null, false);
        AppCompatTextView txt_frequency = (AppCompatTextView) view.findViewById(R.id.txt_frequency);
        ImageView img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        Date_Helper date_Helper = this.dth;
        ((AppCompatTextView) view.findViewById(R.id.txt_date)).setText(Date_Helper.FormateDateFromString("dd-MM-yyyy", "dd MMM", this.lst_date.get(position)));
        ((AppCompatTextView) view.findViewById(R.id.txt_goal)).setText("" + this.lst_date_goal_val_2.get(position) + " " + URLFactory.WATER_UNIT_VALUE);
        ((AppCompatTextView) view.findViewById(R.id.txt_consumed)).setText("" + this.lst_date_val.get(position) + " " + URLFactory.WATER_UNIT_VALUE);
        Database_Helper database_Helper = this.dh;
        ArrayList<HashMap<String, String>> arr_data = database_Helper.getdata("tbl_drink_details", "DrinkDate ='" + this.lst_date.get(position) + "'");
        if (arr_data.size() > 1) {
            string_Helper = this.sh;
            i = R.string.times;
        } else {
            string_Helper = this.sh;
            i = R.string.time;
        }
        String str = string_Helper.get_string(i);
        txt_frequency.setText(arr_data.size() + " " + str);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                Screen_Week_Report.this.chart.highlightValue((float) position, -1);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void generateDataNew() {
        this.chartNew.setBackgroundColor(-1);
        this.chartNew.clear();
        this.chartNew.getDescription().setEnabled(false);
        this.chartNew.setTouchEnabled(true);
        this.chartNew.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onValueSelected(Entry e, Highlight h) {
                e.getY();
            }

            public void onNothingSelected() {
            }
        });
        this.chartNew.setDrawGridBackground(false);
        this.chartNew.setDragEnabled(true);
        this.chartNew.setScaleEnabled(true);
        this.chartNew.setPinchZoom(true);
        XAxis xAxis = this.chartNew.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-90.0f);
        xAxis.setLabelCount(this.lst_date.size(), true);
        xAxis.setValueFormatter(new ValueFormatter() {
            public String getFormattedValue(float value) {
                try {
                    if (Screen_Week_Report.this.lst_date.size() > ((int) value)) {
                        return Screen_Week_Report.this.lst_date.get((int) value);
                    }
                    return "N/A";
                } catch (Exception e) {
                    return "N/A";
                }
            }
        });
        xAxis.enableGridDashedLine(10.0f, 0.0f, 0.0f);
        YAxis yAxis = this.chartNew.getAxisLeft();
        this.chartNew.getAxisRight().setEnabled(false);
        yAxis.enableGridDashedLine(10.0f, 0.0f, 0.0f);
        yAxis.setAxisMaximum(getMaxGraphVal());
        yAxis.setAxisMinimum(-50.0f);
        setData(this.lst_date.size());
        this.chartNew.animateY(1500);
        this.chartNew.getLegend().setForm(Legend.LegendForm.LINE);
        this.chartNew.setHorizontalScrollBarEnabled(true);
    }

    public float getMaxGraphVal() {
        float val = 1.0f;
        for (int k = 0; k < this.lst_date_val.size(); k++) {
            if (k == 0) {
                val = Float.parseFloat("" + this.lst_date_val.get(k));
            } else {
                if (val < Float.parseFloat("" + this.lst_date_val.get(k))) {
                    val = Float.parseFloat("" + this.lst_date_val.get(k));
                }
            }
        }
        return 100.0f + val;
    }

    private void setData(int count) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int nextInt = new Random().nextInt(90) + 10;
            int i1 = this.lst_date_val.get(i).intValue();
            values.add(new Entry((float) i, Float.parseFloat("" + i1), getResources().getDrawable(R.drawable.ic_add)));
        }
        if (this.chartNew.getData() == null || ((LineData) this.chartNew.getData()).getDataSetCount() <= 0) {
            LineDataSet set1 = new LineDataSet(values, "");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawIcons(false);
            set1.enableDashedLine(10.0f, 0.0f, 0.0f);
            set1.setColor(MasterBaseAppCompatActivity.getThemeColor(this.mContext));
            set1.setCircleColor(MasterBaseAppCompatActivity.getThemeColor(this.mContext));
            set1.setLineWidth(2.0f);
            set1.setCircleRadius(5.0f);
            set1.setDrawCircleHole(false);
            set1.setFormLineWidth(0.0f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10.0f, 0.0f}, 0.0f));
            set1.setFormSize(15.0f);
            set1.setValueTextSize(9.0f);
            set1.enableDashedHighlightLine(10.0f, 5.0f, 0.0f);
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return Screen_Week_Report.this.chartNew.getAxisLeft().getAxisMinimum();
                }
            });
            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.line_chart_fade_back);
                set1.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, MasterBaseAppCompatActivity.getThemeColorArray(this.act)));
            } else {
                set1.setFillColor(ViewCompat.MEASURED_STATE_MASK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            this.chartNew.setData(new LineData((List<ILineDataSet>) dataSets));
            LineDataSet lineDataSet = set1;
            return;
        }
        LineDataSet set12 = (LineDataSet) ((LineData) this.chartNew.getData()).getDataSetByIndex(0);
        set12.setValues(values);
        set12.notifyDataSetChanged();
        set12.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ((LineData) this.chartNew.getData()).notifyDataChanged();
        this.chartNew.notifyDataSetChanged();
    }

    public void openMenuPicker(String selected_date) {
        this.bottomSheetDialog = new BottomSheetDialog(this.act);
        View view = LayoutInflater.from(this.act).inflate(R.layout.screen_history, (ViewGroup) null, false);
        RecyclerView historyRecyclerView = (RecyclerView) view.findViewById(R.id.historyRecyclerView);
        load_history(selected_date);
        this.adapter = new HistoryAdapter(this.act, this.histories, new HistoryAdapter.CallBack() {
            public void onClickSelect(History history, int position) {
            }

            public void onClickRemove(History history, int position) {
            }
        });
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this.act, 1, false));
        historyRecyclerView.setAdapter(this.adapter);
        this.bottomSheetDialog.setContentView(view);
        this.bottomSheetDialog.show();
    }

    public void load_history(String selected_date) {
        double d;
        double d2;
        this.histories.clear();
        Database_Helper database_Helper = this.dh;
        ArrayList<HashMap<String, String>> arr_data = database_Helper.getdata("tbl_drink_details", "DrinkDate ='" + selected_date + "'", "datetime(substr(DrinkDateTime, 7, 4) || '-' || substr(DrinkDateTime, 4, 2) || '-' || substr(DrinkDateTime, 1, 2) || ' ' || substr(DrinkDateTime, 12, 8))", 1);
        String mes_unit = URLFactory.WATER_UNIT_VALUE;
        for (int k = 0; k < arr_data.size(); k++) {
            History history = new History();
            history.setId((String) arr_data.get(k).get("id"));
            history.setContainerMeasure(mes_unit);
            history.setContainerValue(URLFactory.decimalFormat.format(Double.parseDouble((String) arr_data.get(k).get("ContainerValue"))) + " " + mes_unit);
            history.setContainerValueOZ(URLFactory.decimalFormat.format(Double.parseDouble((String) arr_data.get(k).get("ContainerValueOZ"))) + " " + mes_unit);
            history.setDrinkDate((String) arr_data.get(k).get("DrinkDate"));
            Date_Helper date_Helper = this.dth;
            history.setDrinkTime(Date_Helper.FormateDateFromString("HH:mm", "hh:mm a", (String) arr_data.get(k).get("DrinkTime")));
            Database_Helper database_Helper2 = this.dh;
            ArrayList<HashMap<String, String>> arr_data2 = database_Helper2.getdata("tbl_drink_details", "DrinkDate ='" + ((String) arr_data.get(k).get("DrinkDate")) + "'");
            float tot = 0.0f;
            for (int j = 0; j < arr_data2.size(); j++) {
                if (mes_unit.equalsIgnoreCase("ml")) {
                    d2 = (double) tot;
                    d = Double.parseDouble((String) arr_data2.get(j).get("ContainerValue"));
                } else {
                    d2 = (double) tot;
                    d = Double.parseDouble((String) arr_data2.get(j).get("ContainerValueOZ"));
                }
                tot = (float) (d2 + d);
            }
            history.setTotalML(URLFactory.decimalFormat.format((double) tot) + " " + mes_unit);
            this.histories.add(history);
        }
    }
}
