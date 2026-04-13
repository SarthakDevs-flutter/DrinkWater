package com.trending.water.drinking.reminder.model;

public class Container {
    private String containerId;
    private String containerValue;
    private String containerValueOZ;
    private boolean isCustom = false;
    private boolean isOpen = false;
    private boolean isSelected = false;

    public Container() {
    }

    public Container(String containerId2, String containerValue2, boolean isSelected2, boolean isOpen2, boolean isCustom2) {
        this.isSelected = isSelected2;
        this.containerValue = containerValue2;
        this.containerId = containerId2;
        this.isOpen = isOpen2;
        this.isCustom = isCustom2;
    }

    public boolean isCustom() {
        return this.isCustom;
    }

    public void isCustom(boolean isCustom2) {
        this.isCustom = isCustom2;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void isSelected(boolean isSelected2) {
        this.isSelected = isSelected2;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void isOpen(boolean isOpen2) {
        this.isOpen = isOpen2;
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

    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId2) {
        this.containerId = containerId2;
    }
}
