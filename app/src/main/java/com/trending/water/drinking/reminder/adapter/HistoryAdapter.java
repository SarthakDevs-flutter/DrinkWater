package com.trending.water.drinking.reminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.model.History;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;

@SuppressLint({"NewApi"})
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public final ArrayList<History> historyArrayList;
    CallBack callBack;
    Context mContext;

    public HistoryAdapter(Context c, ArrayList<History> historyArrayList2, CallBack callBack2) {
        this.mContext = c;
        this.historyArrayList = historyArrayList2;
        this.callBack = callBack2;
    }

    public long getItemId(int position) {
        return 0;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_item_history, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.lbl_date.setText(this.historyArrayList.get(position).getDrinkDate());
        holder.lbl_total_day_water.setText(this.historyArrayList.get(position).getTotalML());
        String str = " " + this.historyArrayList.get(position).getContainerMeasure();
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            holder.container_name.setText(this.historyArrayList.get(position).getContainerValue() + str);
        } else {
            holder.container_name.setText(this.historyArrayList.get(position).getContainerValueOZ() + str);
        }
        holder.lbl_time.setText(this.historyArrayList.get(position).getDrinkTime());
        if (position == 0) {
            holder.super_item_block.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.super_item_block.setBackgroundColor(this.mContext.getResources().getColor(R.color.white));
        }
        if (position == 0) {
            holder.item_header_block.setVisibility(View.VISIBLE);
        } else if (showHeader(position)) {
            holder.item_header_block.setVisibility(View.VISIBLE);
        } else {
            holder.item_header_block.setVisibility(View.GONE);
        }
        holder.divider.setVisibility(View.VISIBLE);
        Glide.with(this.mContext).load(getImage(position)).into(holder.imageView);
        holder.item_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HistoryAdapter.this.callBack.onClickSelect((History) HistoryAdapter.this.historyArrayList.get(position), position);
            }
        });
        holder.btnRemoveRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HistoryAdapter.this.callBack.onClickRemove((History) HistoryAdapter.this.historyArrayList.get(position), position);
            }
        });
    }

    public boolean showHeader(int position) {
        if (this.historyArrayList.get(position).getDrinkDate().equalsIgnoreCase(this.historyArrayList.get(position - 1).getDrinkDate())) {
            return false;
        }
        return true;
    }

    public int getItemCount() {
        return this.historyArrayList.size();
    }

    public Integer getImage(int pos) {
        int i = pos;
        Integer drawable = Integer.valueOf(R.drawable.ic_custom_ml);
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            String val = this.historyArrayList.get(i).getContainerValue();
            if (Double.parseDouble(val) == 50.0d) {
                return Integer.valueOf(R.drawable.ic_50_ml);
            }
            if (Double.parseDouble(val) == 100.0d) {
                return Integer.valueOf(R.drawable.ic_100_ml);
            }
            if (Double.parseDouble(val) == 150.0d) {
                return Integer.valueOf(R.drawable.ic_150_ml);
            }
            if (Double.parseDouble(val) == 200.0d) {
                return Integer.valueOf(R.drawable.ic_200_ml);
            }
            if (Double.parseDouble(val) == 250.0d) {
                return Integer.valueOf(R.drawable.ic_250_ml);
            }
            if (Double.parseDouble(val) == 300.0d) {
                return Integer.valueOf(R.drawable.ic_300_ml);
            }
            if (Double.parseDouble(val) == 500.0d) {
                return Integer.valueOf(R.drawable.ic_500_ml);
            }
            if (Double.parseDouble(val) == 600.0d) {
                return Integer.valueOf(R.drawable.ic_600_ml);
            }
            if (Double.parseDouble(val) == 700.0d) {
                return Integer.valueOf(R.drawable.ic_700_ml);
            }
            if (Double.parseDouble(val) == 800.0d) {
                return Integer.valueOf(R.drawable.ic_800_ml);
            }
            if (Double.parseDouble(val) == 900.0d) {
                return Integer.valueOf(R.drawable.ic_900_ml);
            }
            if (Double.parseDouble(val) == 1000.0d) {
                return Integer.valueOf(R.drawable.ic_1000_ml);
            }
            return drawable;
        }
        String val2 = this.historyArrayList.get(i).getContainerValueOZ();
        if (Double.parseDouble(val2) == 2.0d) {
            return Integer.valueOf(R.drawable.ic_50_ml);
        }
        if (Double.parseDouble(val2) == 3.0d) {
            return Integer.valueOf(R.drawable.ic_100_ml);
        }
        if (Double.parseDouble(val2) == 5.0d) {
            return Integer.valueOf(R.drawable.ic_150_ml);
        }
        if (Double.parseDouble(val2) == 7.0d) {
            return Integer.valueOf(R.drawable.ic_200_ml);
        }
        if (Double.parseDouble(val2) == 8.0d) {
            return Integer.valueOf(R.drawable.ic_250_ml);
        }
        if (Double.parseDouble(val2) == 10.0d) {
            return Integer.valueOf(R.drawable.ic_300_ml);
        }
        if (Double.parseDouble(val2) == 17.0d) {
            return Integer.valueOf(R.drawable.ic_500_ml);
        }
        if (Double.parseDouble(val2) == 20.0d) {
            return Integer.valueOf(R.drawable.ic_600_ml);
        }
        if (Double.parseDouble(val2) == 24.0d) {
            return Integer.valueOf(R.drawable.ic_700_ml);
        }
        if (Double.parseDouble(val2) == 27.0d) {
            return Integer.valueOf(R.drawable.ic_800_ml);
        }
        if (Double.parseDouble(val2) == 30.0d) {
            return Integer.valueOf(R.drawable.ic_900_ml);
        }
        if (Double.parseDouble(val2) == 34.0d) {
            return Integer.valueOf(R.drawable.ic_1000_ml);
        }
        return drawable;
    }

    public interface CallBack {
        void onClickRemove(History history, int i);

        void onClickSelect(History history, int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView btnRemoveRow;
        TextView container_name;
        View divider;
        ImageView imageView;
        LinearLayout item_block;
        LinearLayout item_header_block;
        TextView lbl_date;
        TextView lbl_time;
        TextView lbl_total_day_water;
        RelativeLayout super_item_block;

        public ViewHolder(View itemView) {
            super(itemView);
            this.lbl_date = (TextView) itemView.findViewById(R.id.lbl_date);
            this.lbl_total_day_water = (TextView) itemView.findViewById(R.id.lbl_total_day_water);
            this.item_block = (LinearLayout) itemView.findViewById(R.id.item_block);
            this.item_header_block = (LinearLayout) itemView.findViewById(R.id.item_header_block);
            this.imageView = (ImageView) itemView.findViewById(R.id.container_img);
            this.container_name = (TextView) itemView.findViewById(R.id.container_name);
            this.lbl_time = (TextView) itemView.findViewById(R.id.lbl_time);
            this.divider = itemView.findViewById(R.id.divider);
            this.super_item_block = (RelativeLayout) itemView.findViewById(R.id.super_item_block);
            this.btnRemoveRow = (ImageView) itemView.findViewById(R.id.btnRemoveRow);
        }
    }
}
