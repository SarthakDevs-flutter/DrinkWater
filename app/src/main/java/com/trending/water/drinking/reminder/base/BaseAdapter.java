package com.trending.water.drinking.reminder.base;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final Context context;
    @NonNull
    protected final List<T> dataList;
    @NonNull
    protected final LayoutInflater inflater;

    public BaseAdapter(@NonNull Context context, @Nullable List<T> dataList) {
        this.context = context;
        this.dataList = dataList != null ? dataList : new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Nullable
    public T getItem(int position) {
        if (position >= 0 && position < dataList.size()) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    public List<T> getDataList() {
        return dataList;
    }
}
