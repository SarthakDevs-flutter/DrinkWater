package com.trending.water.drinking.reminder.model;

public class History {
    private String containerMeasure;
    private String containerValue;
    private String containerValueOZ;
    private String drinkDate;
    private String drinkTime;
    private String id;
    private String totalMl;

    public String getTotalML() {
        return this.totalMl;
    }

    public void setTotalML(String totalMl) {
        this.totalMl = totalMl;
    }

    public String getDrinkDate() {
        return this.drinkDate;
    }

    public void setDrinkDate(String drinkDate) {
        this.drinkDate = drinkDate;
    }

    public String getDrinkTime() {
        return this.drinkTime;
    }

    public void setDrinkTime(String drinkTime) {
        this.drinkTime = drinkTime;
    }

    public String getContainerValue() {
        return this.containerValue;
    }

    public void setContainerValue(String containerValue) {
        this.containerValue = containerValue;
    }

    public String getContainerMeasure() {
        return this.containerMeasure;
    }

    public void setContainerMeasure(String containerMeasure) {
        this.containerMeasure = containerMeasure;
    }

    public String getContainerValueOZ() {
        return this.containerValueOZ;
    }

    public void setContainerValueOZ(String containerValueOZ) {
        this.containerValueOZ = containerValueOZ;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
