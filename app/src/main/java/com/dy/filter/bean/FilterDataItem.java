package com.dy.filter.bean;

/**
 * Created by dy on 2017/1/18 9:39.
 */

public class FilterDataItem {
    private int id;
    private String name;
    private boolean isChecked;

    public FilterDataItem() {

    }

    public FilterDataItem(int id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
