package com.trending.water.drinking.reminder.model.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlarmSubDetails {
    @SerializedName("AlarmId")
    @Expose
    private String alarmId;
    
    @SerializedName("AlarmTime")
    @Expose
    private String alarmTime;
    
    @SerializedName("id")
    @Expose
    private String id;
    
    @SerializedName("SuperId")
    @Expose
    private String superId;

    public String getAlarmTime() {
        return this.alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getSuperId() {
        return this.superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }
}
