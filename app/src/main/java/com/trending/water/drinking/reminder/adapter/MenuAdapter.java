package com.trending.water.drinking.reminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.model.Menu;

import java.util.ArrayList;

@SuppressLint({"NewApi"})
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public final ArrayList<Menu> menu_name;
    CallBack callBack;
    Context mContext;

    public MenuAdapter(Context c, ArrayList<Menu> menu_name2, CallBack callBack2) {
        this.mContext = c;
        this.menu_name = menu_name2;
        this.callBack = callBack2;
    }

    public long getItemId(int position) {
        return 0;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_item_menu, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(this.menu_name.get(position).getMenuName());
        Glide.with(this.mContext).load(getImage(position)).into(holder.imageView);
        holder.item_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MenuAdapter.this.callBack.onClickSelect((Menu) MenuAdapter.this.menu_name.get(position), position);
            }
        });
        holder.selected_view.setVisibility(View.INVISIBLE);
        if (position == 4 || position == 7) {
            holder.lbl_divider.setVisibility(View.VISIBLE);
        } else {
            holder.lbl_divider.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.menu_name.size();
    }

    public Integer getImage(int pos) {
        Integer valueOf = Integer.valueOf(R.drawable.ic_menu_drink_water);
        switch (pos) {
            case 0:
                return Integer.valueOf(R.drawable.ic_menu_drink_water);
            case 1:
                return Integer.valueOf(R.drawable.ic_menu_history);
            case 2:
                return Integer.valueOf(R.drawable.ic_menu_report);
            case 3:
                return Integer.valueOf(R.drawable.ic_menu_settings);
            case 4:
                return Integer.valueOf(R.drawable.ic_menu_faq);
            case 5:
                return Integer.valueOf(R.drawable.ic_privacypolicy);
            case 6:
                return Integer.valueOf(R.drawable.ic_menu_share);
            case 7:
                return Integer.valueOf(R.drawable.ic_menu_go_premium);
            default:
                return Integer.valueOf(R.drawable.ic_menu_drink_water);
        }
    }

    public interface CallBack {
        void onClickSelect(Menu menu, int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout item_block;
        View lbl_divider;
        View selected_view;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.menu_name);
            this.imageView = (ImageView) itemView.findViewById(R.id.menu_img);
            this.item_block = (LinearLayout) itemView.findViewById(R.id.item_block);
            this.selected_view = itemView.findViewById(R.id.selected_view);
            this.lbl_divider = itemView.findViewById(R.id.lbl_divider);
        }
    }
}
