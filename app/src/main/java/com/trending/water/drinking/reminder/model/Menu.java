package com.trending.water.drinking.reminder.model;

public class Menu {
    private boolean isSelected = false;
    private String menuName;

    public Menu(String menuName2, boolean isSelected2) {
        this.isSelected = isSelected2;
        this.menuName = menuName2;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void isSelected(boolean isSelected2) {
        this.isSelected = isSelected2;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName2) {
        this.menuName = menuName2;
    }
}
