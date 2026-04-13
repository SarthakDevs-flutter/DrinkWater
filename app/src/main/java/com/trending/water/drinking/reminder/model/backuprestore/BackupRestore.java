package com.trending.water.drinking.reminder.model.backuprestore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BackupRestore {
    @SerializedName("AlarmDetails")
    @Expose
    private List<AlarmDetails> alarmDetailsList = new ArrayList();
    @SerializedName("auto_backup")
    @Expose
    private boolean auto_backup = false;
    @SerializedName("auto_backup_id")
    @Expose
    private Integer auto_backup_id = 0;
    @SerializedName("auto_backup_type")
    @Expose
    private Integer auto_backup_type = 0;
    @SerializedName("ContainerDetails")
    @Expose
    private List<ContainerDetails> containerDetailsList = new ArrayList();
    @SerializedName("disable_notification")
    @Expose
    private boolean disable_notification = false;
    @SerializedName("disable_sound")
    @Expose
    private boolean disable_sound = false;
    @SerializedName("DrinkDetails")
    @Expose
    private List<DrinkDetails> drinkDetailsList = new ArrayList();
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
    private boolean is_active = false;
    @SerializedName("is_breastfeeding")
    @Expose
    private boolean is_breastfeeding = false;
    @SerializedName("is_pregnant")
    @Expose
    private boolean is_pregnant = false;
    @SerializedName("manual_reminder_active")
    @Expose
    private boolean manual_reminder_active = true;
    @SerializedName("reminder_option")
    @Expose
    private Integer reminder_option;
    @SerializedName("reminder_sound")
    @Expose
    private Integer reminder_sound;
    @SerializedName("reminder_vibrate")
    @Expose
    private boolean reminder_vibrate = true;
    @SerializedName("total_drink")
    @Expose
    private float total_drink;
    @SerializedName("total_height")
    @Expose
    private String total_height;
    @SerializedName("total_weight")
    @Expose
    private String total_weight;
    @SerializedName("user_gender")
    @Expose
    private boolean user_gender = true;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("weather_conditions")
    @Expose
    private Integer weather_conditions = 0;

    public List<AlarmDetails> getAlarmDetails() {
        return this.alarmDetailsList;
    }

    public void setAlarmDetails(List<AlarmDetails> alarmDetailsList2) {
        this.alarmDetailsList = alarmDetailsList2;
    }

    public List<ContainerDetails> getContainerDetails() {
        return this.containerDetailsList;
    }

    public void setContainerDetails(List<ContainerDetails> containerDetailsList2) {
        this.containerDetailsList = containerDetailsList2;
    }

    public List<DrinkDetails> getDrinkDetails() {
        return this.drinkDetailsList;
    }

    public void setDrinkDetails(List<DrinkDetails> drinkDetailsList2) {
        this.drinkDetailsList = drinkDetailsList2;
    }

    public String getTotalWeight() {
        return this.total_weight;
    }

    public void setTotalWeight(String total_weight2) {
        this.total_weight = total_weight2;
    }

    public float getTotalDrink() {
        return this.total_drink;
    }

    public void setTotalDrink(float total_drink2) {
        this.total_drink = total_drink2;
    }

    public boolean isKgUnit() {
        return this.isKgUnit;
    }

    public void isKgUnit(boolean isKgUnit2) {
        this.isKgUnit = isKgUnit2;
    }

    public boolean isMlUnit() {
        return this.isMlUnit;
    }

    public void isMlUnit(boolean isMlUnit2) {
        this.isMlUnit = isMlUnit2;
    }

    public String getTotalHeight() {
        return this.total_height;
    }

    public void setTotalHeight(String total_height2) {
        this.total_height = total_height2;
    }

    public String getUserName() {
        return this.user_name;
    }

    public void setUserName(String user_name2) {
        this.user_name = user_name2;
    }

    public boolean getUserGender() {
        return this.user_gender;
    }

    public void setUserGender(boolean user_gender2) {
        this.user_gender = user_gender2;
    }

    public boolean isCMUnit() {
        return this.isCMUnit;
    }

    public void isCMUnit(boolean isCMUnit2) {
        this.isCMUnit = isCMUnit2;
    }

    public Integer getReminderOption() {
        return this.reminder_option;
    }

    public void setReminderOption(Integer reminder_option2) {
        this.reminder_option = reminder_option2;
    }

    public Integer getReminderSound() {
        return this.reminder_sound;
    }

    public void setReminderSound(Integer reminder_sound2) {
        this.reminder_sound = reminder_sound2;
    }

    public boolean isReminderVibrate() {
        return this.reminder_vibrate;
    }

    public void isReminderVibrate(boolean reminder_vibrate2) {
        this.reminder_vibrate = reminder_vibrate2;
    }

    public boolean isDisableNotifiction() {
        return this.disable_notification;
    }

    public void isDisableNotifiction(boolean disable_notification2) {
        this.disable_notification = disable_notification2;
    }

    public boolean isManualReminderActive() {
        return this.manual_reminder_active;
    }

    public void isManualReminderActive(boolean manual_reminder_active2) {
        this.manual_reminder_active = manual_reminder_active2;
    }

    public boolean isDisableSound() {
        return this.disable_sound;
    }

    public void isDisableSound(boolean disable_sound2) {
        this.disable_sound = disable_sound2;
    }

    public boolean isAutoBackup() {
        return this.auto_backup;
    }

    public void isAutoBackup(boolean auto_backup2) {
        this.auto_backup = auto_backup2;
    }

    public Integer getAutoBackupType() {
        return this.auto_backup_type;
    }

    public void setAutoBackupType(Integer auto_backup_type2) {
        this.auto_backup_type = auto_backup_type2;
    }

    public Integer getAutoBackupId() {
        return this.auto_backup_id;
    }

    public void setAutoBackupID(Integer auto_backup_id2) {
        this.auto_backup_id = auto_backup_id2;
    }

    public boolean isActive() {
        return this.is_active;
    }

    public void isActive(boolean is_active2) {
        this.is_active = is_active2;
    }

    public boolean isPregnant() {
        return this.is_pregnant;
    }

    public void isPregnant(boolean is_pregnant2) {
        this.is_pregnant = is_pregnant2;
    }

    public boolean isBreastfeeding() {
        return this.is_breastfeeding;
    }

    public void isBreastfeeding(boolean is_breastfeeding2) {
        this.is_breastfeeding = is_breastfeeding2;
    }

    public Integer getWeatherConditions() {
        return this.weather_conditions;
    }

    public void setWeatherConditions(Integer weather_conditions2) {
        this.weather_conditions = weather_conditions2;
    }
}
