package com.datasource.as.metric;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.datasource.as.util.MetricUtil;
import com.datasource.as.util.ArrayUtil;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataSource {

    private final String PROMETHEUS_FILE = "metrics.prom";
    private final String[] METRICS_IDS = {"cpu1", "cpu2", "memory", "bandwidth", "score"};
    private final Integer INTERVAL = 120;
    ArrayList<Metric> metrics;
    private Integer maxLength;
    private CollectorRegistry registry;
    private MetricRegistry mrMetrics;

    private ScheduledExecutorService scheduler;
    private Runnable step = this::nextStep;

    private Integer currentStep;

    public DataSource() {
        this.metrics = MetricUtil.loadMetrics(new ArrayList<>(Arrays.asList(METRICS_IDS)));

        ArrayList<Integer> sizes = new ArrayList<>();
        for (Metric metric : metrics) {
            sizes.add(metric.getValues().size());
        }

        this.maxLength = ArrayUtil.getMaxInteger(sizes);
        this.registry = new CollectorRegistry();
        this.mrMetrics = new MetricRegistry();
        this.currentStep = 0;

        registerGauges(metrics);

        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduler.scheduleAtFixedRate(step, 0, 1, TimeUnit.SECONDS);
    }

    private void registerGauges(ArrayList<Metric> metrics) {
        for (Metric metric : metrics) {
            Gauge<Double> metricGauge = new Gauge<Double>() {
                @Override
                public Double getValue() {
                    return metric.getCurrentValue();
                }
            };
            mrMetrics.register(metric.getId(), metricGauge);
        }

        registry.register(new DropwizardExports(mrMetrics));
    }

    public void formatMetrics() {
        try (FileWriter writer = new FileWriter(PROMETHEUS_FILE)) {

            TextFormat.write004(writer, registry.metricFamilySamples());

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private void nextStep() {
        currentStep = currentStep == (maxLength + INTERVAL - 1) ? 0 : currentStep + 1;
        for (Metric metric : metrics) {
            Double currentValue = metric.getValues().get(currentStep);
            if (currentValue == null) {
                currentValue = 0d;
            }
            metric.setCurrentValue(currentValue);
        }
        formatMetrics();
    }

}
