package com.trending.water.drinking.reminder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.base.BaseAdapter;
import com.trending.water.drinking.reminder.model.Container;
import com.trending.water.drinking.reminder.utils.WaterValueUtils;

import java.util.List;

public class ContainerAdapterNew extends BaseAdapter<Container, ContainerAdapterNew.ViewHolder> {

    private final CallBack callBack;

    public ContainerAdapterNew(@NonNull Context context, @NonNull List<Container> containerList, @NonNull CallBack callBack) {
        super(context, containerList);
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Container container = getItem(position);
        if (container == null) return;

        holder.textView.setText(WaterValueUtils.getFormattedWaterValue(
                container.getContainerValue(),
                container.getContainerValueOZ()));

        int imageRes;
        if (container.isCustom()) {
            imageRes = R.drawable.ic_custom_ml;
        } else {
            try {
                imageRes = WaterValueUtils.getContainerImage(
                        Double.parseDouble(container.getContainerValue()),
                        Double.parseDouble(container.getContainerValueOZ()));
            } catch (NumberFormatException e) {
                imageRes = R.drawable.ic_custom_ml;
            }
        }

        Glide.with(context).load(imageRes).into(holder.imageView);

        holder.itemBlock.setOnClickListener(v -> callBack.onClickSelect(container, position));

        holder.imgSelected.setVisibility(container.isSelected() ? View.VISIBLE : View.INVISIBLE);
    }

    public interface CallBack {
        void onClickSelect(Container container, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final ImageView imgSelected;
        final LinearLayout itemBlock;
        final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.container_name);
            imageView = itemView.findViewById(R.id.container_img);
            itemBlock = itemView.findViewById(R.id.item_block);
            imgSelected = itemView.findViewById(R.id.img_selected);
        }
    }
}
