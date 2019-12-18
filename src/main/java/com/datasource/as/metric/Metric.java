package com.datasource.as.metric;

import java.util.ArrayList;

public class Metric {

    private String id;
    private ArrayList<Double> values;
    private Double currentValue;

    public Metric(String id, ArrayList<Double> values) {
        this.id = id;
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }
}
