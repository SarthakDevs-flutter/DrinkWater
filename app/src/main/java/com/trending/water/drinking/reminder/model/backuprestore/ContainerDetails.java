package com.trending.water.drinking.reminder.model.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContainerDetails {
    @SerializedName("ContainerID")
    @Expose
    private String containerId;
    
    @SerializedName("ContainerMeasure")
    @Expose
    private String containerMeasure;
    
    @SerializedName("ContainerValue")
    @Expose
    private String containerValue;
    
    @SerializedName("ContainerValueOZ")
    @Expose
    private String containerValueOZ;
    
    @SerializedName("id")
    @Expose
    private String id;
    
    @SerializedName("IsCustom")
    @Expose
    private String isCustom;
    
    @SerializedName("IsOpen")
    @Expose
    private String isOpen;

    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsOpen() {
        return this.isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getIsCustom() {
        return this.isCustom;
    }

    public void setIsCustom(String isCustom) {
        this.isCustom = isCustom;
    }
}
