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

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.model.SoundModel;

import java.util.List;

@SuppressLint({"NewApi"})
public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public final List<SoundModel> sounds;
    CallBack callBack;
    Context mContext;

    public SoundAdapter(Context c, List<SoundModel> sounds2, CallBack callBack2) {
        this.mContext = c;
        this.sounds = sounds2;
        this.callBack = callBack2;
    }

    public long getItemId(int position) {
        return 0;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_item_sound, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.lbl_sound_name.setText(this.sounds.get(position).getName());
        if (this.sounds.get(position).isSelected()) {
            holder.img_selected.setVisibility(View.VISIBLE);
            holder.item_block.getBackground().setTint(this.mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.img_selected.setVisibility(View.INVISIBLE);
            holder.item_block.getBackground().setTint(this.mContext.getResources().getColor(R.color.white));
        }
        holder.item_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SoundAdapter.this.callBack.onClickSelect((SoundModel) SoundAdapter.this.sounds.get(position), position);
            }
        });
    }

    public int getItemCount() {
        return this.sounds.size();
    }

    public interface CallBack {
        void onClickSelect(SoundModel soundModel, int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_selected;
        LinearLayout item_block;
        TextView lbl_sound_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item_block = (LinearLayout) itemView.findViewById(R.id.item_block);
            this.img_selected = (ImageView) itemView.findViewById(R.id.img_selected);
            this.lbl_sound_name = (TextView) itemView.findViewById(R.id.lbl_sound_name);
        }
    }
}
