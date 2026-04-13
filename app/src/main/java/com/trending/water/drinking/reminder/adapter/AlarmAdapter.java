package com.trending.water.drinking.reminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.model.AlarmModel;

import java.util.ArrayList;

@SuppressLint({"NewApi"})
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public final ArrayList<AlarmModel> alarmList;
    CallBack callBack;
    Context mContext;

    public AlarmAdapter(Context c, ArrayList<AlarmModel> alarmList2, CallBack callBack2) {
        this.mContext = c;
        this.alarmList = alarmList2;
        this.callBack = callBack2;
    }

    public long getItemId(int position) {
        return 0;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.row_item_alarm, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.lbl_time.setText(this.alarmList.get(position).getDrinkTime());
        if (this.alarmList.get(position).getAlarmType().equalsIgnoreCase("R")) {
            TextView textView = holder.lbl_time;
            textView.setText(holder.lbl_time.getText() + "\n" + this.mContext.getResources().getString(R.string.str_every) + " " + this.alarmList.get(position).getAlarmInterval() + " " + this.mContext.getResources().getString(R.string.str_minutes).toLowerCase());
        }
        holder.item_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlarmAdapter.this.callBack.onClickSelect((AlarmModel) AlarmAdapter.this.alarmList.get(position), position);
            }
        });
        holder.img_remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlarmAdapter.this.showMenu(view, (AlarmModel) AlarmAdapter.this.alarmList.get(position), position);
            }
        });
        boolean z = false;
        holder.switch_reminder.setChecked(this.alarmList.get(position).getIsOff().intValue() != 1);
        holder.switch_reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmAdapter.this.callBack.onClickSwitch((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, isChecked);
            }
        });
        holder.chk_sunday.setChecked(this.alarmList.get(position).getSunday().intValue() == 1);
        holder.chk_monday.setChecked(this.alarmList.get(position).getMonday().intValue() == 1);
        holder.chk_tuesday.setChecked(this.alarmList.get(position).getTuesday().intValue() == 1);
        holder.chk_wednesday.setChecked(this.alarmList.get(position).getWednesday().intValue() == 1);
        holder.chk_thursday.setChecked(this.alarmList.get(position).getThursday().intValue() == 1);
        holder.chk_friday.setChecked(this.alarmList.get(position).getFriday().intValue() == 1);
        CheckBox checkBox = holder.chk_saturday;
        if (this.alarmList.get(position).getSaturday().intValue() == 1) {
            z = true;
        }
        checkBox.setChecked(z);
        holder.chk_sunday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmAdapter.this.callBack.onClickWeek((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, 0, holder.chk_sunday.isChecked());
            }
        });
        holder.chk_monday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmAdapter.this.callBack.onClickWeek((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, 1, holder.chk_monday.isChecked());
            }
        });
        holder.chk_tuesday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmAdapter.this.callBack.onClickWeek((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, 2, holder.chk_tuesday.isChecked());
            }
        });
        holder.chk_wednesday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmAdapter.this.callBack.onClickWeek((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, 3, holder.chk_wednesday.isChecked());
            }
        });
        holder.chk_thursday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmAdapter.this.callBack.onClickWeek((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, 4, holder.chk_thursday.isChecked());
            }
        });
        holder.chk_friday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmAdapter.this.callBack.onClickWeek((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, 5, holder.chk_friday.isChecked());
            }
        });
        holder.chk_saturday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmAdapter.this.callBack.onClickWeek((AlarmModel) AlarmAdapter.this.alarmList.get(position), position, 6, holder.chk_saturday.isChecked());
            }
        });
    }

    public void showMenu(View v, final AlarmModel alarmModel, final int position) {
        PopupMenu popup = new PopupMenu(this.mContext, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.delete_item) {
                    AlarmAdapter.this.callBack.onClickRemove(alarmModel, position);
                    return true;
                } else if (itemId != R.id.edit_item) {
                    return false;
                } else {
                    AlarmAdapter.this.callBack.onClickEdit(alarmModel, position);
                    return true;
                }
            }
        });
        popup.inflate(R.menu.manual_reminder_menu);
        popup.show();
    }

    public int getItemCount() {
        return this.alarmList.size();
    }

    public interface CallBack {
        void onClickEdit(AlarmModel alarmModel, int i);

        void onClickRemove(AlarmModel alarmModel, int i);

        void onClickSelect(AlarmModel alarmModel, int i);

        void onClickSwitch(AlarmModel alarmModel, int i, boolean z);

        void onClickWeek(AlarmModel alarmModel, int i, int i2, boolean z);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox chk_friday;
        CheckBox chk_monday;
        CheckBox chk_saturday;
        CheckBox chk_sunday;
        CheckBox chk_thursday;
        CheckBox chk_tuesday;
        CheckBox chk_wednesday;
        ImageView img_remove;
        LinearLayout item_block;
        TextView lbl_time;
        SwitchCompat switch_reminder;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item_block = (LinearLayout) itemView.findViewById(R.id.item_block);
            this.img_remove = (ImageView) itemView.findViewById(R.id.img_remove);
            this.lbl_time = (TextView) itemView.findViewById(R.id.lbl_time);
            this.switch_reminder = (SwitchCompat) itemView.findViewById(R.id.switch_reminder);
            this.chk_sunday = (CheckBox) itemView.findViewById(R.id.chk_sunday);
            this.chk_monday = (CheckBox) itemView.findViewById(R.id.chk_monday);
            this.chk_tuesday = (CheckBox) itemView.findViewById(R.id.chk_tuesday);
            this.chk_wednesday = (CheckBox) itemView.findViewById(R.id.chk_wednesday);
            this.chk_thursday = (CheckBox) itemView.findViewById(R.id.chk_thursday);
            this.chk_friday = (CheckBox) itemView.findViewById(R.id.chk_friday);
            this.chk_saturday = (CheckBox) itemView.findViewById(R.id.chk_saturday);
        }
    }
}
