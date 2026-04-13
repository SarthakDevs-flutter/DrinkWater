package com.trending.water.drinking.reminder.model;

public class SoundModel {
    private int id;
    private boolean isSelected = false;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void isSelected(boolean isSelected2) {
        this.isSelected = isSelected2;
    }
}
