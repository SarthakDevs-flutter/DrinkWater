package com.trending.water.drinking.reminder.model;

public class NextReminderModel implements Comparable<NextReminderModel> {
    private long millesecond;
    private String time;

    public NextReminderModel(long millesecond2, String time2) {
        this.millesecond = millesecond2;
        this.time = time2;
    }

    public long getMillesecond() {
        return this.millesecond;
    }

    public String getTime() {
        return this.time;
    }

    public int compareTo(NextReminderModel f) {
        if (this.millesecond > f.millesecond) {
            return 1;
        }
        if (this.millesecond < f.millesecond) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return this.time;
    }
}
