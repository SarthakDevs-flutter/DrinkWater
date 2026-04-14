package com.trending.water.drinking.reminder.model;

public class AlarmModel {
    private String alarmId;
    private String alarmInterval;
    private String alarmType;
    private String drinkTime;
    private String id;

    private Integer isOff = 0;

    private Integer sunday = 0;
    private Integer monday = 0;
    private Integer tuesday = 0;
    private Integer wednesday = 0;
    private Integer thursday = 0;
    private Integer friday = 0;
    private Integer saturday = 0;

    private String alarmSundayId;
    private String alarmMondayId;
    private String alarmTuesdayId;
    private String alarmWednesdayId;
    private String alarmThursdayId;
    private String alarmFridayId;
    private String alarmSaturdayId;

    public String getDrinkTime() {
        return this.drinkTime;
    }

    public void setDrinkTime(String drinkTime) {
        this.drinkTime = drinkTime;
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
}
