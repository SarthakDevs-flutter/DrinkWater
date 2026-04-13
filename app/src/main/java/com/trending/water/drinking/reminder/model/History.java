package com.trending.water.drinking.reminder.model;

public class History {
    private String containerMeasure;
    private String containerValue;
    private String containerValueOZ;
    private String drinkDate;
    private String drinkTime;
    private String id;
    private String total_ml;

    public String getTotalML() {
        return this.total_ml;
    }

    public void setTotalML(String total_ml2) {
        this.total_ml = total_ml2;
    }

    public String getDrinkDate() {
        return this.drinkDate;
    }

    public void setDrinkDate(String drinkDate2) {
        this.drinkDate = drinkDate2;
    }

    public String getDrinkTime() {
        return this.drinkTime;
    }

    public void setDrinkTime(String drinkTime2) {
        this.drinkTime = drinkTime2;
    }

    public String getContainerValue() {
        return this.containerValue;
    }

    public void setContainerValue(String containerValue2) {
        this.containerValue = containerValue2;
    }

    public String getContainerMeasure() {
        return this.containerMeasure;
    }

    public void setContainerMeasure(String containerMeasure2) {
        this.containerMeasure = containerMeasure2;
    }

    public String getContainerValueOZ() {
        return this.containerValueOZ;
    }

    public void setContainerValueOZ(String containerValueOZ2) {
        this.containerValueOZ = containerValueOZ2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }
}
