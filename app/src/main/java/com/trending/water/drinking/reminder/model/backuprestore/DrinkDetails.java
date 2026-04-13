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

    public void setDrinkDateTime(String drinkDateTime2) {
        this.drinkDateTime = drinkDateTime2;
    }

    public String getDrinkTime() {
        return this.drinkTime;
    }

    public void setDrinkTime(String drinkTime2) {
        this.drinkTime = drinkTime2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getDrinkDate() {
        return this.drinkDate;
    }

    public void setDrinkDate(String drinkDate2) {
        this.drinkDate = drinkDate2;
    }

    public String getContainerMeasure() {
        return this.containerMeasure;
    }

    public void setContainerMeasure(String containerMeasure2) {
        this.containerMeasure = containerMeasure2;
    }

    public String getContainerValue() {
        return this.containerValue;
    }

    public void setContainerValue(String containerValue2) {
        this.containerValue = containerValue2;
    }

    public String getContainerValueOZ() {
        return this.containerValueOZ;
    }

    public void setContainerValueOZ(String containerValueOZ2) {
        this.containerValueOZ = containerValueOZ2;
    }

    public String getTodayGoal() {
        return this.todayGoal;
    }

    public void setTodayGoal(String todayGoal2) {
        this.todayGoal = todayGoal2;
    }

    public String getTodayGoalOZ() {
        return this.todayGoalOZ;
    }

    public void setTodayGoalOZ(String todayGoalOZ2) {
        this.todayGoalOZ = todayGoalOZ2;
    }
}
