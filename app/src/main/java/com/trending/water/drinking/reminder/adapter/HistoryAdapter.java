package com.trending.water.drinking.reminder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.base.BaseAdapter;
import com.trending.water.drinking.reminder.model.History;
import com.trending.water.drinking.reminder.utils.WaterValueUtils;

import java.util.List;

public class HistoryAdapter extends BaseAdapter<History, HistoryAdapter.ViewHolder> {

    private final CallBack callBack;

    public HistoryAdapter(@NonNull Context context, @NonNull List<History> historyList, @NonNull CallBack callBack) {
        super(context, historyList);
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final History history = getItem(position);
        if (history == null) return;

        holder.lblDate.setText(history.getDrinkDate());
        holder.lblTotalDayWater.setText(history.getTotalML());
        
        String measure = " " + history.getContainerMeasure();
        holder.containerName.setText(WaterValueUtils.getFormattedWaterValue(
                history.getContainerValue(),
                history.getContainerValueOZ()) + measure);

        holder.lblTime.setText(history.getDrinkTime());

        // Header and background logic
        if (position == 0) {
            holder.superItemBlock.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.itemHeaderBlock.setVisibility(View.VISIBLE);
        } else {
            holder.superItemBlock.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.itemHeaderBlock.setVisibility(shouldShowHeader(position) ? View.VISIBLE : View.GONE);
        }

        holder.divider.setVisibility(View.VISIBLE);

        try {
            int imageRes = WaterValueUtils.getContainerImage(
                    Double.parseDouble(history.getContainerValue()),
                    Double.parseDouble(history.getContainerValueOZ()));
            Glide.with(context).load(imageRes).into(holder.imageView);
        } catch (NumberFormatException e) {
            // Handle parsing error if needed
        }

        holder.itemBlock.setOnClickListener(v -> callBack.onClickSelect(history, position));
        holder.btnRemoveRow.setOnClickListener(v -> callBack.onClickRemove(history, position));
    }

    public boolean shouldShowHeader(int position) {
        if (position <= 0) return true;
        History current = getItem(position);
        History previous = getItem(position - 1);
        return current != null && previous != null && !current.getDrinkDate().equalsIgnoreCase(previous.getDrinkDate());
    }

    public interface CallBack {
        void onClickRemove(History history, int position);
        void onClickSelect(History history, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView btnRemoveRow;
        final TextView containerName;
        final View divider;
        final ImageView imageView;
        final LinearLayout itemBlock;
        final LinearLayout itemHeaderBlock;
        final TextView lblDate;
        final TextView lblTime;
        final TextView lblTotalDayWater;
        final RelativeLayout superItemBlock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblDate = itemView.findViewById(R.id.lbl_date);
            lblTotalDayWater = itemView.findViewById(R.id.lbl_total_day_water);
            itemBlock = itemView.findViewById(R.id.item_block);
            itemHeaderBlock = itemView.findViewById(R.id.item_header_block);
            imageView = itemView.findViewById(R.id.container_img);
            containerName = itemView.findViewById(R.id.container_name);
            lblTime = itemView.findViewById(R.id.lbl_time);
            divider = itemView.findViewById(R.id.divider);
            superItemBlock = itemView.findViewById(R.id.super_item_block);
            btnRemoveRow = itemView.findViewById(R.id.btnRemoveRow);
        }
    }
}
