package com.trending.water.drinking.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trending.water.drinking.reminder.adapter.HistoryAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Database_Helper;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Date_Helper;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.model.History;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class Screen_History extends MasterBaseActivity {
    HistoryAdapter adapter;
    int afterLoad = 0;
    int beforeLoad = 0;
    ArrayList<History> histories = new ArrayList<>();
    RecyclerView historyRecyclerView;
    boolean isLoading = true;
    AppCompatTextView lbl_no_record_found;
    AppCompatTextView lbl_toolbar_title;
    LinearLayout left_icon_block;
    NestedScrollView nestedScrollView;
    int page = 0;
    int perPage = 20;
    LinearLayout right_icon_block;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_history);
        FindViewById();
        Body();
    }

    private void FindViewById() {
        this.right_icon_block = (LinearLayout) findViewById(R.id.right_icon_block);
        this.left_icon_block = (LinearLayout) findViewById(R.id.left_icon_block);
        this.lbl_toolbar_title = (AppCompatTextView) findViewById(R.id.lbl_toolbar_title);
        this.historyRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        this.lbl_no_record_found = (AppCompatTextView) findViewById(R.id.lbl_no_record_found);
        this.nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
    }

    private void Body() {
        this.lbl_toolbar_title.setText(this.sh.get_string(R.string.str_drink_history));
        this.left_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_History.this.finish();
            }
        });
        this.right_icon_block.setVisibility(View.GONE);
        this.historyRecyclerView.setNestedScrollingEnabled(false);
        this.adapter = new HistoryAdapter(this.act, this.histories, new HistoryAdapter.CallBack() {
            public void onClickSelect(History history, int position) {
            }

            public void onClickRemove(final History history, int position) {
                new AlertDialog.Builder(Screen_History.this.act).setMessage(Screen_History.this.sh.get_string(R.string.str_history_remove_confirm_message)).setPositiveButton(Screen_History.this.sh.get_string(R.string.str_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Database_Helper database_Helper = Screen_History.this.dh;
                        database_Helper.REMOVE("tbl_drink_details", "id=" + history.getId());
                        Screen_History.this.page = 0;
                        Screen_History.this.isLoading = true;
                        Screen_History.this.histories.clear();
                        Screen_History.this.load_history(false);
                        Screen_History.this.adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton(Screen_History.this.sh.get_string(R.string.str_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        this.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this.act, RecyclerView.VERTICAL, false));
        this.historyRecyclerView.setAdapter(this.adapter);
        load_history(false);
        this.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    Log.i("nested_sync", "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i("nested_sync", "Scroll UP");
                }
                if (scrollY == 0) {
                    Log.i("nested_sync", "TOP SCROLL");
                }
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    Log.i("nested_sync", "BOTTOM SCROLL");
                    if (Screen_History.this.isLoading) {
                        Screen_History.this.isLoading = false;
                        Screen_History.this.page++;
                        Screen_History.this.load_history(true);
                    }
                }
            }
        });
    }

    public void load_history(boolean closeLoader) {
        double d;
        double d2;
        int start_idx = this.page * this.perPage;
        Cursor c = Constant.SDB.rawQuery("SELECT * FROM tbl_drink_details ORDER BY datetime(substr(DrinkDateTime, 7, 4) || '-' || substr(DrinkDateTime, 4, 2) || '-' || substr(DrinkDateTime, 1, 2) || ' ' || substr(DrinkDateTime, 12, 8)) DESC limit " + start_idx + "," + this.perPage, (String[]) null);
        ArrayList<HashMap<String, String>> arr_data = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                arr_data.add(map);
            } while (c.moveToNext());
        }
        String mes_unit = URLFactory.WATER_UNIT_VALUE;
        for (int k = 0; k < arr_data.size(); k++) {
            History history = new History();
            history.setId((String) arr_data.get(k).get("id"));
            history.setContainerMeasure(mes_unit);
            history.setContainerValue("" + ((int) Double.parseDouble((String) arr_data.get(k).get("ContainerValue"))));
            history.setContainerValueOZ("" + ((int) Double.parseDouble((String) arr_data.get(k).get("ContainerValueOZ"))));
            history.setDrinkDate((String) arr_data.get(k).get("DrinkDate"));
            Date_Helper date_Helper = this.dth;
            history.setDrinkTime(Date_Helper.FormateDateFromString("HH:mm", "hh:mm a", (String) arr_data.get(k).get("DrinkTime")));
            Database_Helper database_Helper = this.dh;
            ArrayList<HashMap<String, String>> arr_data2 = database_Helper.getdata("tbl_drink_details", "DrinkDate ='" + ((String) arr_data.get(k).get("DrinkDate")) + "'");
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
            history.setTotalML("" + ((int) tot) + " " + mes_unit);
            this.histories.add(history);
        }
        this.afterLoad = this.histories.size();
        if (this.afterLoad == 0) {
            this.isLoading = false;
        } else if (this.afterLoad > this.beforeLoad) {
            this.isLoading = true;
        } else {
            this.isLoading = false;
        }
        if (this.histories.size() > 0) {
            this.lbl_no_record_found.setVisibility(View.GONE);
        } else {
            this.lbl_no_record_found.setVisibility(View.VISIBLE);
        }
        this.adapter.notifyDataSetChanged();
    }
}
