package com.trending.water.drinking.reminder.model;

public class Menu {
    private boolean isSelected = false;
    private String menuName;

    public Menu(String menuName, boolean isSelected) {
        this.isSelected = isSelected;
        this.menuName = menuName;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
