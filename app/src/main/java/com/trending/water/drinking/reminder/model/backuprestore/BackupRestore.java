package com.trending.water.drinking.reminder.model.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BackupRestore {
    @SerializedName("AlarmDetails")
    @Expose
    private List<AlarmDetails> alarmDetailsList = new ArrayList<>();

    @SerializedName("auto_backup")
    @Expose
    private boolean autoBackup = false;

    @SerializedName("auto_backup_id")
    @Expose
    private Integer autoBackupId = 0;

    @SerializedName("auto_backup_type")
    @Expose
    private Integer autoBackupType = 0;

    @SerializedName("ContainerDetails")
    @Expose
    private List<ContainerDetails> containerDetailsList = new ArrayList<>();

    @SerializedName("disable_notification")
    @Expose
    private boolean disableNotification = false;

    @SerializedName("disable_sound")
    @Expose
    private boolean disableSound = false;

    @SerializedName("DrinkDetails")
    @Expose
    private List<DrinkDetails> drinkDetailsList = new ArrayList<>();

    @SerializedName("isCMUnit")
    @Expose
    private boolean isCMUnit = true;

    @SerializedName("isKgUnit")
    @Expose
    private boolean isKgUnit = true;

    @SerializedName("isMlUnit")
    @Expose
    private boolean isMlUnit = true;

    @SerializedName("is_active")
    @Expose
    private boolean isActive = false;

    @SerializedName("is_breastfeeding")
    @Expose
    private boolean isBreastfeeding = false;

    @SerializedName("is_pregnant")
    @Expose
    private boolean isPregnant = false;

    @SerializedName("manual_reminder_active")
    @Expose
    private boolean manualReminderActive = true;

    @SerializedName("reminder_option")
    @Expose
    private Integer reminderOption;

    @SerializedName("reminder_sound")
    @Expose
    private Integer reminderSound;

    @SerializedName("reminder_vibrate")
    @Expose
    private boolean reminderVibrate = true;

    @SerializedName("total_drink")
    @Expose
    private float totalDrink;

    @SerializedName("total_height")
    @Expose
    private String totalHeight;

    @SerializedName("total_weight")
    @Expose
    private String totalWeight;

    @SerializedName("user_gender")
    @Expose
    private boolean userGender = true;

    @SerializedName("user_name")
    @Expose
    private String userName;

    @SerializedName("weather_conditions")
    @Expose
    private Integer weatherConditions = 0;

    public List<AlarmDetails> getAlarmDetails() {
        return this.alarmDetailsList;
    }

    public void setAlarmDetails(List<AlarmDetails> alarmDetailsList) {
        this.alarmDetailsList = alarmDetailsList;
    }

    public List<ContainerDetails> getContainerDetails() {
        return this.containerDetailsList;
    }

    public void setContainerDetails(List<ContainerDetails> containerDetailsList) {
        this.containerDetailsList = containerDetailsList;
    }

    public List<DrinkDetails> getDrinkDetails() {
        return this.drinkDetailsList;
    }

    public void setDrinkDetails(List<DrinkDetails> drinkDetailsList) {
        this.drinkDetailsList = drinkDetailsList;
    }

    public String getTotalWeight() {
        return this.totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public float getTotalDrink() {
        return this.totalDrink;
    }

    public void setTotalDrink(float totalDrink) {
        this.totalDrink = totalDrink;
    }

    public boolean isKgUnit() {
        return this.isKgUnit;
    }

    public void setKgUnit(boolean kgUnit) {
        this.isKgUnit = kgUnit;
    }

    public boolean isMlUnit() {
        return this.isMlUnit;
    }

    public void setMlUnit(boolean mlUnit) {
        this.isMlUnit = mlUnit;
    }

    public String getTotalHeight() {
        return this.totalHeight;
    }

    public void setTotalHeight(String totalHeight) {
        this.totalHeight = totalHeight;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getUserGender() {
        return this.userGender;
    }

    public void setUserGender(boolean userGender) {
        this.userGender = userGender;
    }

    public boolean isCMUnit() {
        return this.isCMUnit;
    }

    public void setCMUnit(boolean CMUnit) {
        this.isCMUnit = CMUnit;
    }

    public Integer getReminderOption() {
        return this.reminderOption;
    }

    public void setReminderOption(Integer reminderOption) {
        this.reminderOption = reminderOption;
    }

    public Integer getReminderSound() {
        return this.reminderSound;
    }

    public void setReminderSound(Integer reminderSound) {
        this.reminderSound = reminderSound;
    }

    public boolean isReminderVibrate() {
        return this.reminderVibrate;
    }

    public void setReminderVibrate(boolean reminderVibrate) {
        this.reminderVibrate = reminderVibrate;
    }

    public boolean isDisableNotification() {
        return this.disableNotification;
    }

    public void setDisableNotification(boolean disableNotification) {
        this.disableNotification = disableNotification;
    }

    public boolean isManualReminderActive() {
        return this.manualReminderActive;
    }

    public void setManualReminderActive(boolean manualReminderActive) {
        this.manualReminderActive = manualReminderActive;
    }

    public boolean isDisableSound() {
        return this.disableSound;
    }

    public void setDisableSound(boolean disableSound) {
        this.disableSound = disableSound;
    }

    public boolean isAutoBackup() {
        return this.autoBackup;
    }

    public void setAutoBackup(boolean autoBackup) {
        this.autoBackup = autoBackup;
    }

    public Integer getAutoBackupType() {
        return this.autoBackupType;
    }

    public void setAutoBackupType(Integer autoBackupType) {
        this.autoBackupType = autoBackupType;
    }

    public Integer getAutoBackupId() {
        return this.autoBackupId;
    }

    public void setAutoBackupId(Integer autoBackupId) {
        this.autoBackupId = autoBackupId;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isPregnant() {
        return this.isPregnant;
    }

    public void setPregnant(boolean pregnant) {
        this.isPregnant = pregnant;
    }

    public boolean isBreastfeeding() {
        return this.isBreastfeeding;
    }

    public void setBreastfeeding(boolean breastfeeding) {
        this.isBreastfeeding = breastfeeding;
    }

    public Integer getWeatherConditions() {
        return this.weatherConditions;
    }

    public void setWeatherConditions(Integer weatherConditions) {
        this.weatherConditions = weatherConditions;
    }
}
