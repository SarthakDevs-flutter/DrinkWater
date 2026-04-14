package com.trending.water.drinking.reminder.model.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AlarmDetails {
    @SerializedName("FridayAlarmId")
    @Expose
    private String alarmFridayId;

    @SerializedName("AlarmId")
    @Expose
    private String alarmId;

    @SerializedName("AlarmInterval")
    @Expose
    private String alarmInterval;

    @SerializedName("MondayAlarmId")
    @Expose
    private String alarmMondayId;

    @SerializedName("SaturdayAlarmId")
    @Expose
    private String alarmSaturdayId;

    @SerializedName("AlarmSubDetails")
    @Expose
    private List<AlarmSubDetails> alarmSubDetailsList = new ArrayList<>();

    @SerializedName("SundayAlarmId")
    @Expose
    private String alarmSundayId;

    @SerializedName("ThursdayAlarmId")
    @Expose
    private String alarmThursdayId;

    @SerializedName("AlarmTime")
    @Expose
    private String alarmTime;

    @SerializedName("TuesdayAlarmId")
    @Expose
    private String alarmTuesdayId;

    @SerializedName("AlarmType")
    @Expose
    private String alarmType;

    @SerializedName("WednesdayAlarmId")
    @Expose
    private String alarmWednesdayId;

    @SerializedName("Friday")
    @Expose
    private Integer friday = 0;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("IsOff")
    @Expose
    private Integer isOff = 0;

    @SerializedName("Monday")
    @Expose
    private Integer monday = 0;

    @SerializedName("Saturday")
    @Expose
    private Integer saturday = 0;

    @SerializedName("Sunday")
    @Expose
    private Integer sunday = 0;

    @SerializedName("Thursday")
    @Expose
    private Integer thursday = 0;

    @SerializedName("Tuesday")
    @Expose
    private Integer tuesday = 0;

    @SerializedName("Wednesday")
    @Expose
    private Integer wednesday = 0;

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

    public String getAlarmInterval() {
        return this.alarmInterval;
    }

    public void setAlarmInterval(String alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public String getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public List<AlarmSubDetails> getAlarmSubDetails() {
        return this.alarmSubDetailsList;
    }

    public void setAlarmSubDetails(List<AlarmSubDetails> alarmSubDetailsList) {
        this.alarmSubDetailsList = alarmSubDetailsList;
    }

    public String getAlarmSundayId() {
        return this.alarmSundayId;
    }

    public void setAlarmSundayId(String alarmSundayId) {
        this.alarmSundayId = alarmSundayId;
    }

    public String getAlarmMondayId() {
        return this.alarmMondayId;
    }

    public void setAlarmMondayId(String alarmMondayId) {
        this.alarmMondayId = alarmMondayId;
    }

    public String getAlarmTuesdayId() {
        return this.alarmTuesdayId;
    }

    public void setAlarmTuesdayId(String alarmTuesdayId) {
        this.alarmTuesdayId = alarmTuesdayId;
    }

    public String getAlarmWednesdayId() {
        return this.alarmWednesdayId;
    }

    public void setAlarmWednesdayId(String alarmWednesdayId) {
        this.alarmWednesdayId = alarmWednesdayId;
    }

    public String getAlarmThursdayId() {
        return this.alarmThursdayId;
    }

    public void setAlarmThursdayId(String alarmThursdayId) {
        this.alarmThursdayId = alarmThursdayId;
    }

    public String getAlarmFridayId() {
        return this.alarmFridayId;
    }

    public void setAlarmFridayId(String alarmFridayId) {
        this.alarmFridayId = alarmFridayId;
    }

    public String getAlarmSaturdayId() {
        return this.alarmSaturdayId;
    }

    public void setAlarmSaturdayId(String alarmSaturdayId) {
        this.alarmSaturdayId = alarmSaturdayId;
    }

    public Integer getIsOff() {
        return this.isOff;
    }

    public void setIsOff(Integer isOff) {
        this.isOff = isOff;
    }

    public Integer getSunday() {
        return this.sunday;
    }

    public void setSunday(Integer sunday) {
        this.sunday = sunday;
    }

    public Integer getMonday() {
        return this.monday;
    }

    public void setMonday(Integer monday) {
        this.monday = monday;
    }

    public Integer getTuesday() {
        return this.tuesday;
    }

    public void setTuesday(Integer tuesday) {
        this.tuesday = tuesday;
    }

    public Integer getWednesday() {
        return this.wednesday;
    }

    public void setWednesday(Integer wednesday) {
        this.wednesday = wednesday;
    }

    public Integer getThursday() {
        return this.thursday;
    }

    public void setThursday(Integer thursday) {
        this.thursday = thursday;
    }

    public Integer getFriday() {
        return this.friday;
    }

    public void setFriday(Integer friday) {
        this.friday = friday;
    }

    public Integer getSaturday() {
        return this.saturday;
    }

    public void setSaturday(Integer saturday) {
        this.saturday = saturday;
    }
}
