package com.trending.water.drinking.reminder.model.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrinkDetails {
    @SerializedName("ContainerMeasure")
    @Expose
    private String containerMeasure;

    @SerializedName("ContainerValue")
    @Expose
    private String containerValue;

    @SerializedName("ContainerValueOZ")
    @Expose
    private String containerValueOZ;

    @SerializedName("DrinkDate")
    @Expose
    private String drinkDate;

    @SerializedName("DrinkDateTime")
    @Expose
    private String drinkDateTime;

    @SerializedName("DrinkTime")
    @Expose
    private String drinkTime;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("TodayGoal")
    @Expose
    private String todayGoal;

    @SerializedName("TodayGoalOZ")
    @Expose
    private String todayGoalOZ;

    public String getDrinkDateTime() {
        return this.drinkDateTime;
    }

    public void setDrinkDateTime(String drinkDateTime) {
        this.drinkDateTime = drinkDateTime;
    }

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

    public String getDrinkDate() {
        return this.drinkDate;
    }

    public void setDrinkDate(String drinkDate) {
        this.drinkDate = drinkDate;
    }

    public String getContainerMeasure() {
        return this.containerMeasure;
    }

    public void setContainerMeasure(String containerMeasure) {
        this.containerMeasure = containerMeasure;
    }

    public String getContainerValue() {
        return this.containerValue;
    }

    public void setContainerValue(String containerValue) {
        this.containerValue = containerValue;
    }

    public String getContainerValueOZ() {
        return this.containerValueOZ;
    }

    public void setContainerValueOZ(String containerValueOZ) {
        this.containerValueOZ = containerValueOZ;
    }

    public String getTodayGoal() {
        return this.todayGoal;
    }

    public void setTodayGoal(String todayGoal) {
        this.todayGoal = todayGoal;
    }

    public String getTodayGoalOZ() {
        return this.todayGoalOZ;
    }

    public void setTodayGoalOZ(String todayGoalOZ) {
        this.todayGoalOZ = todayGoalOZ;
    }
}
