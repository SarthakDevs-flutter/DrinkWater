package com.trending.water.drinking.reminder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.trending.water.drinking.reminder.R;
import com.trending.water.drinking.reminder.base.BaseAdapter;
import com.trending.water.drinking.reminder.model.AlarmModel;

import java.util.List;

public class AlarmAdapter extends BaseAdapter<AlarmModel, AlarmAdapter.ViewHolder> {

    private final CallBack callBack;

    public AlarmAdapter(@NonNull Context context, @NonNull List<AlarmModel> alarmList, @NonNull CallBack callBack) {
        super(context, alarmList);
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_item_alarm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AlarmModel alarm = getItem(position);
        if (alarm == null) return;

        holder.lblTime.setText(alarm.getDrinkTime());
        if ("R".equalsIgnoreCase(alarm.getAlarmType())) {
            String every = context.getString(R.string.str_every);
            String minutes = context.getString(R.string.str_minutes).toLowerCase();
            String timeText = alarm.getDrinkTime() + "\n" + every + " " + alarm.getAlarmInterval() + " " + minutes;
            holder.lblTime.setText(timeText);
        }

        holder.itemBlock.setOnClickListener(v -> callBack.onClickSelect(alarm, position));
        holder.imgRemove.setOnClickListener(v -> showPopupMenu(v, alarm, position));

        holder.switchReminder.setOnCheckedChangeListener(null);
        holder.switchReminder.setChecked(alarm.getIsOff() != 1);
        holder.switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> callBack.onClickSwitch(alarm, position, isChecked));

        setupWeekCheckboxes(holder, alarm, position);
    }

    private void setupWeekCheckboxes(@NonNull ViewHolder holder, @NonNull final AlarmModel alarm, final int position) {
        holder.chkSunday.setChecked(alarm.getSunday() == 1);
        holder.chkMonday.setChecked(alarm.getMonday() == 1);
        holder.chkTuesday.setChecked(alarm.getTuesday() == 1);
        holder.chkWednesday.setChecked(alarm.getWednesday() == 1);
        holder.chkThursday.setChecked(alarm.getThursday() == 1);
        holder.chkFriday.setChecked(alarm.getFriday() == 1);
        holder.chkSaturday.setChecked(alarm.getSaturday() == 1);

        holder.chkSunday.setOnClickListener(v -> callBack.onClickWeek(alarm, position, 0, holder.chkSunday.isChecked()));
        holder.chkMonday.setOnClickListener(v -> callBack.onClickWeek(alarm, position, 1, holder.chkMonday.isChecked()));
        holder.chkTuesday.setOnClickListener(v -> callBack.onClickWeek(alarm, position, 2, holder.chkTuesday.isChecked()));
        holder.chkWednesday.setOnClickListener(v -> callBack.onClickWeek(alarm, position, 3, holder.chkWednesday.isChecked()));
        holder.chkThursday.setOnClickListener(v -> callBack.onClickWeek(alarm, position, 4, holder.chkThursday.isChecked()));
        holder.chkFriday.setOnClickListener(v -> callBack.onClickWeek(alarm, position, 5, holder.chkFriday.isChecked()));
        holder.chkSaturday.setOnClickListener(v -> callBack.onClickWeek(alarm, position, 6, holder.chkSaturday.isChecked()));
    }

    private void showPopupMenu(View view, final AlarmModel alarm, final int position) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.delete_item) {
                callBack.onClickRemove(alarm, position);
                return true;
            } else if (itemId == R.id.edit_item) {
                callBack.onClickEdit(alarm, position);
                return true;
            }
            return false;
        });
        popup.inflate(R.menu.manual_reminder_menu);
        popup.show();
    }

    public interface CallBack {
        void onClickEdit(AlarmModel alarm, int position);

        void onClickRemove(AlarmModel alarm, int position);

        void onClickSelect(AlarmModel alarm, int position);

        void onClickSwitch(AlarmModel alarm, int position, boolean isChecked);

        void onClickWeek(AlarmModel alarm, int position, int dayIndex, boolean isChecked);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox chkSunday, chkMonday, chkTuesday, chkWednesday, chkThursday, chkFriday, chkSaturday;
        final ImageView imgRemove;
        final LinearLayout itemBlock;
        final TextView lblTime;
        final SwitchCompat switchReminder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBlock = itemView.findViewById(R.id.item_block);
            imgRemove = itemView.findViewById(R.id.img_remove);
            lblTime = itemView.findViewById(R.id.lbl_time);
            switchReminder = itemView.findViewById(R.id.switch_reminder);
            chkSunday = itemView.findViewById(R.id.chk_sunday);
            chkMonday = itemView.findViewById(R.id.chk_monday);
            chkTuesday = itemView.findViewById(R.id.chk_tuesday);
            chkWednesday = itemView.findViewById(R.id.chk_wednesday);
            chkThursday = itemView.findViewById(R.id.chk_thursday);
            chkFriday = itemView.findViewById(R.id.chk_friday);
            chkSaturday = itemView.findViewById(R.id.chk_saturday);
        }
    }
}
