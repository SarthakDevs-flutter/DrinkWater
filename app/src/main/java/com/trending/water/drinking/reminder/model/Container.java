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

    public Container(String containerId, String containerValue, boolean isSelected, boolean isOpen, boolean isCustom) {
        this.isSelected = isSelected;
        this.containerValue = containerValue;
        this.containerId = containerId;
        this.isOpen = isOpen;
        this.isCustom = isCustom;
    }

    public boolean isCustom() {
        return this.isCustom;
    }

    public void setCustom(boolean custom) {
        this.isCustom = custom;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
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

    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
