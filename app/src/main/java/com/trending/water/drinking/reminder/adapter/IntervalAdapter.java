package com.trending.water.drinking.reminder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.base.BaseAdapter;
import com.trending.water.drinking.reminder.model.IntervalModel;

import java.util.List;

public class IntervalAdapter extends BaseAdapter<IntervalModel, IntervalAdapter.ViewHolder> {

    private final CallBack callBack;

    public IntervalAdapter(@NonNull Context context, @NonNull List<IntervalModel> intervals, @NonNull CallBack callBack) {
        super(context, intervals);
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_item_sound, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final IntervalModel interval = getItem(position);
        if (interval == null) return;

        holder.lblSoundName.setText(interval.getName());
        
        if (interval.isSelected()) {
            holder.imgSelected.setVisibility(View.VISIBLE);
            holder.itemBlock.getBackground().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.imgSelected.setVisibility(View.INVISIBLE);
            holder.itemBlock.getBackground().setTint(ContextCompat.getColor(context, R.color.white));
        }

        holder.itemBlock.setOnClickListener(v -> callBack.onClickSelect(interval, position));
    }

    public interface CallBack {
        void onClickSelect(IntervalModel intervalModel, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgSelected;
        final LinearLayout itemBlock;
        final TextView lblSoundName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBlock = itemView.findViewById(R.id.item_block);
            imgSelected = itemView.findViewById(R.id.img_selected);
            lblSoundName = itemView.findViewById(R.id.lbl_sound_name);
        }
    }
}
