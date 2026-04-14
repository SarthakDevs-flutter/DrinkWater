package com.trending.water.drinking.reminder;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trending.water.drinking.reminder.adapter.HistoryAdapter;
import com.trending.water.drinking.reminder.appbasiclibs.utils.Constant;
import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.model.History;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Screen_History extends MasterBaseActivity {
    
    private RecyclerView historyRecyclerView;
    private AppCompatTextView lblNoRecordFound;
    private AppCompatTextView lblToolbarTitle;
    private LinearLayout leftIconBlock;
    private LinearLayout rightIconBlock;
    private NestedScrollView nestedScrollView;
    
    private HistoryAdapter adapter;
    private final ArrayList<History> histories = new ArrayList<>();
    
    private boolean isLoading = true;
    private int page = 0;
    private final int perPage = 20;
    private int beforeLoad = 0;
    private int afterLoad = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_history);
        findViewByIds();
        initView();
    }

    private void findViewByIds() {
        rightIconBlock = findViewById(R.id.right_icon_block);
        leftIconBlock = findViewById(R.id.left_icon_block);
        lblToolbarTitle = findViewById(R.id.lbl_toolbar_title);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        lblNoRecordFound = findViewById(R.id.lbl_no_record_found);
        nestedScrollView = findViewById(R.id.nestedScrollView);
    }

    private void initView() {
        lblToolbarTitle.setText(stringHelper.getString(R.string.str_drink_history));
        leftIconBlock.setOnClickListener(v -> finish());
        rightIconBlock.setVisibility(View.GONE);
        
        historyRecyclerView.setNestedScrollingEnabled(false);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        
        adapter = new HistoryAdapter(mActivity, histories, new HistoryAdapter.CallBack() {
            @Override
            public void onClickSelect(History history, int position) {
                // Not implemented in original
            }

            @Override
            public void onClickRemove(History history, int position) {
                showRemoveConfirmationDialog(history);
            }
        });
        historyRecyclerView.setAdapter(adapter);
        
        loadHistory(false);
        
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            boolean isAtBottom = scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
            if (isAtBottom && isLoading) {
                isLoading = false;
                page++;
                loadHistory(true);
            }
        });
    }

    private void showRemoveConfirmationDialog(History history) {
        new AlertDialog.Builder(mActivity)
                .setMessage(stringHelper.getString(R.string.str_history_remove_confirm_message))
                .setPositiveButton(stringHelper.getString(R.string.str_yes), (dialog, which) -> {
                    databaseHelper.remove("tbl_drink_details", "id=" + history.getId());
                    resetAndReload();
                    dialog.dismiss();
                })
                .setNegativeButton(stringHelper.getString(R.string.str_no), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void resetAndReload() {
        page = 0;
        isLoading = true;
        histories.clear();
        beforeLoad = 0;
        loadHistory(false);
    }

    public void loadHistory(boolean isPagination) {
        int startIndex = page * perPage;
        beforeLoad = histories.size();
        
        // Complex SQL ordering preserved
        String query = "SELECT * FROM tbl_drink_details ORDER BY datetime(substr(DrinkDateTime, 7, 4) || '-' || substr(DrinkDateTime, 4, 2) || '-' || substr(DrinkDateTime, 1, 2) || ' ' || substr(DrinkDateTime, 12, 8)) DESC limit " + startIndex + "," + perPage;
        
        try (Cursor c = Constant.database.rawQuery(query, null)) {
            ArrayList<HashMap<String, String>> records = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        map.put(c.getColumnName(i), c.getString(i));
                    }
                    records.add(map);
                } while (c.moveToNext());
            }

            String unit = URLFactory.waterUnitValue;
            for (HashMap<String, String> record : records) {
                History history = new History();
                history.setId(record.get("id"));
                history.setContainerMeasure(unit);
                
                double val = Double.parseDouble(record.get("ContainerValue"));
                double valOz = Double.parseDouble(record.get("ContainerValueOZ"));
                
                history.setContainerValue(String.valueOf((int) val));
                history.setContainerValueOZ(String.valueOf((int) valOz));
                history.setDrinkDate(record.get("DrinkDate"));
                history.setDrinkTime(dateHelper.formatDateFromString("HH:mm", "hh:mm a", record.get("DrinkTime")));

                // Calculate total for that day
                List<HashMap<String, String>> dayRecords = databaseHelper.getData("tbl_drink_details", "DrinkDate ='" + record.get("DrinkDate") + "'");
                float dayTotal = 0.0f;
                for (HashMap<String, String> dr : dayRecords) {
                    dayTotal += Float.parseFloat(unit.equalsIgnoreCase("ML") ? dr.get("ContainerValue") : dr.get("ContainerValueOZ"));
                }
                
                history.setTotalML((int) dayTotal + " " + unit);
                histories.add(history);
            }
        } catch (Exception e) {
            Log.e("Screen_History", "Error loading history", e);
        }

        afterLoad = histories.size();
        isLoading = afterLoad > beforeLoad;
        
        lblNoRecordFound.setVisibility(histories.isEmpty() ? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();
    }
}
