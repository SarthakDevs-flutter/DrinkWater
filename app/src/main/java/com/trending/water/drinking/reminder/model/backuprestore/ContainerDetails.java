package com.trending.water.drinking.reminder.model.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContainerDetails {
    @SerializedName("ContainerID")
    @Expose
    private String containerID;
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

    public String getContainerID() {
        return this.containerID;
    }

    public void setContainerID(String containerID2) {
        this.containerID = containerID2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
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

    public String getIsOpen() {
        return this.isOpen;
    }

    public void setIsOpen(String isOpen2) {
        this.isOpen = isOpen2;
    }

    public String getIsCustom() {
        return this.isCustom;
    }

    public void setIsCustom(String isCustom2) {
        this.isCustom = isCustom2;
    }
}
