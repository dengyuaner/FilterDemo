package com.dy.filter.bean;

import java.util.List;

/**
 * Created by dy on 2017/1/18 9:33.
 */

public class FilterData {
    private int cusType;
    private List<FilterDataItem> exponent;
    private List<FilterDataItem> group;
    private List<FilterDataItem> time;

    public List<FilterDataItem> getExponent() {
        return exponent;
    }

    public void setExponent(List<FilterDataItem> exponent) {
        this.exponent = exponent;
    }

    public List<FilterDataItem> getGroup() {
        return group;
    }

    public void setGroup(List<FilterDataItem> group) {
        this.group = group;
    }

    public List<FilterDataItem> getTime() {
        return time;
    }

    public void setTime(List<FilterDataItem> time) {
        this.time = time;
    }

    public int getCusType() {
        return cusType;
    }

    public void setCusType(int cusType) {
        this.cusType = cusType;
    }
}
