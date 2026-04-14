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
import com.trending.water.drinking.reminder.model.Menu;

import java.util.List;

public class MenuAdapter extends BaseAdapter<Menu, MenuAdapter.ViewHolder> {

    private final CallBack callBack;

    public MenuAdapter(@NonNull Context context, @NonNull List<Menu> menuList, @NonNull CallBack callBack) {
        super(context, menuList);
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Menu menuItem = getItem(position);
        if (menuItem == null) return;

        holder.textView.setText(menuItem.getMenuName());
        Glide.with(context).load(getMenuImage(position)).into(holder.imageView);

        holder.itemBlock.setOnClickListener(v -> callBack.onClickSelect(menuItem, position));

        holder.selectedView.setVisibility(View.INVISIBLE);

        // Divider logic
        if (position == 4 || position == 7) {
            holder.lblDivider.setVisibility(View.VISIBLE);
        } else {
            holder.lblDivider.setVisibility(View.GONE);
        }
    }

    private int getMenuImage(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_menu_drink_water;
            case 1:
                return R.drawable.ic_menu_history;
            case 2:
                return R.drawable.ic_menu_report;
            case 3:
                return R.drawable.ic_menu_settings;
            case 4:
                return R.drawable.ic_menu_faq;
            case 5:
                return R.drawable.ic_privacypolicy;
            case 6:
                return R.drawable.ic_menu_share;
            case 7:
                return R.drawable.ic_menu_go_premium;
            default:
                return R.drawable.ic_menu_drink_water;
        }
    }

    public interface CallBack {
        void onClickSelect(Menu menu, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final LinearLayout itemBlock;
        final View lblDivider;
        final View selectedView;
        final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.menu_name);
            imageView = itemView.findViewById(R.id.menu_img);
            itemBlock = itemView.findViewById(R.id.item_block);
            selectedView = itemView.findViewById(R.id.selected_view);
            lblDivider = itemView.findViewById(R.id.lbl_divider);
        }
    }
}
