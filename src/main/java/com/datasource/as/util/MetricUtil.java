package com.datasource.as.util;

import com.datasource.as.metric.Metric;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MetricUtil {

    public static ArrayList<Metric> loadMetrics(ArrayList<String> metricIds) {
        ArrayList<Metric> metrics = new ArrayList<>();

        for (String metricId : metricIds) {
            JSONArray metricJson = new JSONObject(FileUtil.readFile("resources/" + metricId + ".json"))
                    .getJSONArray("values");
            ArrayList<Double> values = new ArrayList<>();
            for (int i = 0; i < metricJson.length(); i++) {
                values.add(metricJson.getDouble(i));
            }
            metrics.add(new Metric(metricId, values));
        }

        return metrics;
    }

}
