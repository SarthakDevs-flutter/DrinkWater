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
    private List<AlarmSubDetails> alarmSubDetailsList = new ArrayList();
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

    public void setAlarmTime(String alarmTime2) {
        this.alarmTime = alarmTime2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId2) {
        this.alarmId = alarmId2;
    }

    public String getAlarmInterval() {
        return this.alarmInterval;
    }

    public void setAlarmInterval(String alarmInterval2) {
        this.alarmInterval = alarmInterval2;
    }

    public String getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(String alarmType2) {
        this.alarmType = alarmType2;
    }

    public List<AlarmSubDetails> getAlarmSubDetails() {
        return this.alarmSubDetailsList;
    }

    public void setAlarmSubDetails(List<AlarmSubDetails> alarmSubDetailsList2) {
        this.alarmSubDetailsList = alarmSubDetailsList2;
    }

    public String getAlarmSundayId() {
        return this.alarmSundayId;
    }

    public void setAlarmSundayId(String alarmSundayId2) {
        this.alarmSundayId = alarmSundayId2;
    }

    public String getAlarmMondayId() {
        return this.alarmMondayId;
    }

    public void setAlarmMondayId(String alarmMondayId2) {
        this.alarmMondayId = alarmMondayId2;
    }

    public String getAlarmTuesdayId() {
        return this.alarmTuesdayId;
    }

    public void setAlarmTuesdayId(String alarmTuesdayId2) {
        this.alarmTuesdayId = alarmTuesdayId2;
    }

    public String getAlarmWednesdayId() {
        return this.alarmWednesdayId;
    }

    public void setAlarmWednesdayId(String alarmWednesdayId2) {
        this.alarmWednesdayId = alarmWednesdayId2;
    }

    public String getAlarmThursdayId() {
        return this.alarmThursdayId;
    }

    public void setAlarmThursdayId(String alarmThursdayId2) {
        this.alarmThursdayId = alarmThursdayId2;
    }

    public String getAlarmFridayId() {
        return this.alarmFridayId;
    }

    public void setAlarmFridayId(String alarmFridayId2) {
        this.alarmFridayId = alarmFridayId2;
    }

    public String getAlarmSaturdayId() {
        return this.alarmSaturdayId;
    }

    public void setAlarmSaturdayId(String alarmSaturdayId2) {
        this.alarmSaturdayId = alarmSaturdayId2;
    }

    public Integer getIsOff() {
        return this.isOff;
    }

    public void setIsOff(Integer isOff2) {
        this.isOff = isOff2;
    }

    public Integer getSunday() {
        return this.sunday;
    }

    public void setSunday(Integer sunday2) {
        this.sunday = sunday2;
    }

    public Integer getMonday() {
        return this.monday;
    }

    public void setMonday(Integer monday2) {
        this.monday = monday2;
    }

    public Integer getTuesday() {
        return this.tuesday;
    }

    public void setTuesday(Integer tuesday2) {
        this.tuesday = tuesday2;
    }

    public Integer getWednesday() {
        return this.wednesday;
    }

    public void setWednesday(Integer wednesday2) {
        this.wednesday = wednesday2;
    }

    public Integer getThursday() {
        return this.thursday;
    }

    public void setThursday(Integer thursday2) {
        this.thursday = thursday2;
    }

    public Integer getFriday() {
        return this.friday;
    }

    public void setFriday(Integer friday2) {
        this.friday = friday2;
    }

    public Integer getSaturday() {
        return this.saturday;
    }

    public void setSaturday(Integer saturday2) {
        this.saturday = saturday2;
    }
}
