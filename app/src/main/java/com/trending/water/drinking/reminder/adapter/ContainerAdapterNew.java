package com.trending.water.drinking.reminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.model.Container;
import com.trending.water.drinking.reminder.utils.URLFactory;

import java.util.ArrayList;

@SuppressLint({"NewApi"})
public class ContainerAdapterNew extends RecyclerView.Adapter<ContainerAdapterNew.ViewHolder> {
    /* access modifiers changed from: private */
    public final ArrayList<Container> containerArrayList;
    /* access modifiers changed from: private */
    public CallBack callBack;
    Context mContext;

    public ContainerAdapterNew(Context c, ArrayList<Container> containerArrayList2, CallBack callBack2) {
        this.mContext = c;
        this.containerArrayList = containerArrayList2;
        this.callBack = callBack2;
    }

    public long getItemId(int position) {
        return 0;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_item_container, parent, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                if (this.containerArrayList.get(position).getContainerValue().contains(".")) {
                    TextView textView = holder.textView;
                    textView.setText(URLFactory.decimalFormat.format(Double.parseDouble(this.containerArrayList.get(position).getContainerValue())) + " " + URLFactory.WATER_UNIT_VALUE);
                } else {
                    TextView textView2 = holder.textView;
                    textView2.setText(this.containerArrayList.get(position).getContainerValue() + " " + URLFactory.WATER_UNIT_VALUE);
                }
            } else if (this.containerArrayList.get(position).getContainerValue().contains(".")) {
                TextView textView3 = holder.textView;
                textView3.setText(URLFactory.decimalFormat.format(Double.parseDouble(this.containerArrayList.get(position).getContainerValueOZ())) + " " + URLFactory.WATER_UNIT_VALUE);
            } else {
                TextView textView4 = holder.textView;
                textView4.setText(this.containerArrayList.get(position).getContainerValueOZ() + " " + URLFactory.WATER_UNIT_VALUE);
            }
        } catch (Exception e) {
        }
        if (this.containerArrayList.get(position).isCustom()) {
            Glide.with(this.mContext).load(Integer.valueOf(R.drawable.ic_custom_ml)).into(holder.imageView);
        } else {
            Glide.with(this.mContext).load(getImage(position)).into(holder.imageView);
        }
        holder.item_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ContainerAdapterNew.this.callBack.onClickSelect((Container) ContainerAdapterNew.this.containerArrayList.get(position), position);
            }
        });
        if (this.containerArrayList.get(position).isSelected()) {
            holder.img_selected.setVisibility(View.VISIBLE);
        } else {
            holder.img_selected.setVisibility(View.INVISIBLE);
        }
    }

    public int getItemCount() {
        return this.containerArrayList.size();
    }

    public Integer getImage(int pos) {
        int i = pos;
        Integer drawable = Integer.valueOf(R.drawable.ic_custom_ml);
        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 50.0d) {
                return Integer.valueOf(R.drawable.ic_50_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 100.0d) {
                return Integer.valueOf(R.drawable.ic_100_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 150.0d) {
                return Integer.valueOf(R.drawable.ic_150_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 200.0d) {
                return Integer.valueOf(R.drawable.ic_200_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 250.0d) {
                return Integer.valueOf(R.drawable.ic_250_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 300.0d) {
                return Integer.valueOf(R.drawable.ic_300_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 500.0d) {
                return Integer.valueOf(R.drawable.ic_500_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 600.0d) {
                return Integer.valueOf(R.drawable.ic_600_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 700.0d) {
                return Integer.valueOf(R.drawable.ic_700_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 800.0d) {
                return Integer.valueOf(R.drawable.ic_800_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 900.0d) {
                return Integer.valueOf(R.drawable.ic_900_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValue()) == 1000.0d) {
                return Integer.valueOf(R.drawable.ic_1000_ml);
            }
            return drawable;
        } else if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 2.0d) {
            return Integer.valueOf(R.drawable.ic_50_ml);
        } else {
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 3.0d) {
                return Integer.valueOf(R.drawable.ic_100_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 5.0d) {
                return Integer.valueOf(R.drawable.ic_150_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 7.0d) {
                return Integer.valueOf(R.drawable.ic_200_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 8.0d) {
                return Integer.valueOf(R.drawable.ic_250_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 10.0d) {
                return Integer.valueOf(R.drawable.ic_300_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 17.0d) {
                return Integer.valueOf(R.drawable.ic_500_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 20.0d) {
                return Integer.valueOf(R.drawable.ic_600_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 24.0d) {
                return Integer.valueOf(R.drawable.ic_700_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 27.0d) {
                return Integer.valueOf(R.drawable.ic_800_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 30.0d) {
                return Integer.valueOf(R.drawable.ic_900_ml);
            }
            if (Double.parseDouble(this.containerArrayList.get(i).getContainerValueOZ()) == 34.0d) {
                return Integer.valueOf(R.drawable.ic_1000_ml);
            }
            return drawable;
        }
    }

    public interface CallBack {
        void onClickSelect(Container container, int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView img_selected;
        LinearLayout item_block;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.container_name);
            this.imageView = (ImageView) itemView.findViewById(R.id.container_img);
            this.item_block = (LinearLayout) itemView.findViewById(R.id.item_block);
            this.img_selected = (ImageView) itemView.findViewById(R.id.img_selected);
        }
    }
}
