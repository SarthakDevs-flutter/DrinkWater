package com.trending.water.drinking.reminder.model;

public class BackUpFileModel {
    boolean isSelected = false;
    private long lastmodify;
    private String name = "";
    private String path = "";

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path2) {
        this.path = path2;
    }

    public long getLastmodify() {
        return this.lastmodify;
    }

    public void setLastmodify(long lastmodify2) {
        this.lastmodify = lastmodify2;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void isSelected(boolean isSelected2) {
        this.isSelected = isSelected2;
    }
}
