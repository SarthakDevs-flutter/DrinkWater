package com.trending.water.drinking.reminder.model;

public class LanguageModel {
    boolean isSelected = false;
    private String code = "";
    private String name = "";
    private String title = "";

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void isSelected(boolean isSelected2) {
        this.isSelected = isSelected2;
    }
}
