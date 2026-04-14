package com.trending.water.drinking.reminder.model;

import androidx.annotation.NonNull;

public class NextReminderModel implements Comparable<NextReminderModel> {
    private final long millisecond;
    private final String time;

    public NextReminderModel(long millisecond, String time) {
        this.millisecond = millisecond;
        this.time = time;
    }

    public long getMillisecond() {
        return this.millisecond;
    }

    public String getTime() {
        return this.time;
    }

    @Override
    public int compareTo(NextReminderModel other) {
        return Long.compare(this.millisecond, other.millisecond);
    }

    @NonNull
    @Override
    public String toString() {
        return this.time;
    }
}
